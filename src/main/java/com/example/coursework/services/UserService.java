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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    HttpServletRequest httpServletRequest;

    public String getContextPath()
    {
        String port = String.valueOf(httpServletRequest.getLocalPort());
        return httpServletRequest.getRequestURL().toString().split(port)[0] + port;
    }

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
        if (user != null && user.isEmailConfirmed())
            return StatusEnum.BadEmail;

        User addedUser = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), true,
                userDto.getEmail(), UUID.randomUUID().toString(), Collections.singleton(Role.User));
        if (user != null)
            addedUser.setId(user.getId());
        userRepository.save(addedUser);

        mailSender.send(addedUser.getEmail(), EmailsEnum.SignUpSubject.label, String.format(EmailsEnum.SignUpBody.label,
                addedUser.getUsername(), getContextPath(), addedUser.getActivationCode()));
        return StatusEnum.Successfully;
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null)
            return false;
        user.setActivationCode(null);
        user.setEmailConfirmed(true);
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
                user.getUsername(), getContextPath(), user.getRecoveringPasswordCode()));
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

    public User getUserById(long id)
    {
        if (!userRepository.existsById(id))
            return null;
        return userRepository.findById(id).get();
    }

    public boolean uploadAvatar(long id, String image) throws IOException {
        if (!userRepository.existsById(id))
            return false;
        User user = userRepository.findById(id).get();
        var url = cloudinaryService.uploadImage(image);
        if (url != "")
        {
            user.setAvatarUrl(url);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public StatusEnum editUser(User user)
    {
        User userDb = getUserById(user.getId());
        if (userDb == null)
           return StatusEnum.BadUser;

        User userByName = userRepository.findByUsername(user.getUsername());
        if (userByName != null && userByName.getId() != user.getId())
            return StatusEnum.BadName;

        User userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail != null && userByEmail.getId() != user.getId())
            return StatusEnum.BadEmail;

        userDb.setUsername(user.getUsername());
        userDb.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userDb);

        if (!userDb.getEmail().equals(user.getEmail()))
        {
            userDb.setActivationCode(UUID.randomUUID().toString());
            userRepository.save(userDb);
            mailSender.send(user.getEmail(), EmailsEnum.ConfirmEmailSubject.label,
                    String.format(EmailsEnum.ConfirmEmailBody.label, user.getUsername(),
                            getContextPath(), user.getEmail(), userDb.getActivationCode()));
            return StatusEnum.ConfirmEmail;
        }
        return StatusEnum.Successfully;
    }

    public boolean confirmNewEmail(String email, String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null)
            return false;

        user.setActivationCode(null);
        user.setEmail(email);
        userRepository.save(user);
        return true;
    }
}
