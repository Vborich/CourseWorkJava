package com.example.coursework.services;

import com.example.coursework.dto.UserDto;
import com.example.coursework.models.Company;
import com.example.coursework.models.Order;
import com.example.coursework.models.Role;
import com.example.coursework.models.Status.StatusEnum;
import com.example.coursework.models.User;
import com.example.coursework.repo.UserRepository;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    MailSender mailSender;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CloudinaryService cloudinaryService;

    @SneakyThrows
    @Test
    void uploadAvatar() {
        User user = new User();
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(5L);
        Mockito.doReturn(true).when(userRepository).existsById(5L);

        byte[] fileContent = FileUtils.readFileToByteArray(new File("testImage.png"));
        String base64Image = "base64," + Base64.encodeBase64String(fileContent);
        boolean isUpload = userService.uploadAvatar(5, base64Image);

        Assert.assertTrue(isUpload);
        Assert.assertNotNull(user.getAvatarUrl());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void uploadAvatarWithBadImage() {
        User user = new User();

        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(5L);
        Mockito.doReturn(true).when(userRepository).existsById(5L);

        boolean isUpload = userService.uploadAvatar(5, "vlad");

        Assert.assertFalse(isUpload);
        Assert.assertNull(user.getAvatarUrl());

        Mockito.verify(userRepository, Mockito.times(0)).save(user);
    }

    @Test
    void changeUserStatus() {
        User user = new User();
        user.setActive(true);

        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(5L);
        Mockito.doReturn(true).when(userRepository).existsById(5L);

        boolean isChanged = userService.changeUserStatus(5);

        Assert.assertTrue(isChanged);
        Assert.assertFalse(user.isActive());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void changeUserStatusNotExistUser() {
        boolean isChanged = userService.changeUserStatus(5);
        Assert.assertFalse(isChanged);
    }

    @Test
    void removeUserFromCompany() {
        User user = new User();
        user.setCompany(new Company());
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(5L);
        Mockito.doReturn(true).when(userRepository).existsById(5L);

        boolean isRemoved = userService.removeUserFromCompany(5);

        Assert.assertTrue(isRemoved);
        Assert.assertNull(user.getCompany());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void removeUserFromCompanyNotExistUser() {
        boolean isRemoved = userService.removeUserFromCompany(5);
        Assert.assertFalse(isRemoved);
    }

    @Test
    void signUpUser() {
        UserDto user  = new UserDto();
        user.setRoles(Collections.singleton(Role.User));

        StatusEnum status = userService.addUser(user);

        Assert.assertEquals(StatusEnum.Successfully, status);
    }

    @Test
    void signUpUserWithExistName() {
        UserDto user  = new UserDto();
        user.setUsername("admin");
        user.setRoles(Collections.singleton(Role.User));

        Mockito.doReturn(new User()).when(userRepository).findByUsername("admin");

        StatusEnum status = userService.addUser(user);

        Assert.assertEquals(StatusEnum.BadName, status);
    }
}