package com.example.coursework.services;

import com.example.coursework.dto.UserDto;
import com.example.coursework.models.Emails.EmailsEnum;
import com.example.coursework.models.Role;
import com.example.coursework.models.Status.StatusEnum;
import com.example.coursework.models.User;
import com.example.coursework.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public StatusEnum addUser(UserDto userDto)
    {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user != null)
            return StatusEnum.BadName;
        user = userRepository.findByEmail(userDto.getEmail());
        if (user != null && user.getActivationCode() == null)
            return StatusEnum.BadEmail;

        User addedUser = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), true,
                userDto.getEmail(), UUID.randomUUID().toString(), Collections.singleton(Role.User));
        if (user != null)
            addedUser.setId(user.getId());
        userRepository.save(addedUser);

        mailSender.send(addedUser.getEmail(), EmailsEnum.SignUpSubject.label, String.format(EmailsEnum.SignUpBody.label,
                addedUser.getUsername(), addedUser.getActivationCode()));
        return StatusEnum.Successfully;
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null)
            return false;
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public boolean sendRecoverPasswordLink(String email)
    {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return false;
        user.setRecoveringPasswordCode(UUID.randomUUID().toString());
        userRepository.save(user);
        mailSender.send(user.getEmail(), EmailsEnum.RecoverPasswordSubject.label, String.format(EmailsEnum.RecoverPasswordBody.label,
                user.getUsername(), user.getRecoveringPasswordCode()));
        return true;
    }

    public boolean checkRecoverPasswordCode(String code)
    {
        User user = userRepository.findByRecoveringPasswordCode(code);
        if (user == null)
            return  false;
        return  true;
    }

    public void recoverPassword(String password, String code)
    {
        User user = userRepository.findByRecoveringPasswordCode(code);
        user.setPassword(passwordEncoder.encode(password));
        user.setRecoveringPasswordCode(null);
        userRepository.save(user);
    }
}
