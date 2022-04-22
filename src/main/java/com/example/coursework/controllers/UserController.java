package com.example.coursework.controllers;

import com.example.coursework.dto.UserDto;
import com.example.coursework.mappers.UserMapper;
import com.example.coursework.models.Role;
import com.example.coursework.models.Status.StatusEnum;
import com.example.coursework.models.User;
import com.example.coursework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/users/{id}")
    public String openUserProfile(@PathVariable(value = "id") long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");
        model.addAttribute("user", user);
        return "user-profile";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable(value = "id") long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PostMapping(value = "/users/{id}/edit")
    public String editUserPost(@ModelAttribute UserDto user, RedirectAttributes redirectAttributes, Model model) {
        String toast = "";
        var status = userService.editUser(UserMapper.INSTANCE.toModel(user));
        switch (status)
        {
            case BadEmail: toast = "Email " + user.getEmail() + " уже используется"; break;
            case BadName: toast = "Имя пользователя " + user.getUsername() + " занято"; break;
            case ConfirmEmail: toast = "Перейдите на почтовый ящик и выполните подтверждение новой почты"; break;
            case Successfully: toast = "Данные пользователя успешно изменены"; break;
            case BadRoles: toast = "У пользователя должна быть хотя бы 1 роль"; break;
            case BadUser: throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");
        }

        model.addAttribute("toast", toast);
        if (status == StatusEnum.BadEmail || status == StatusEnum.BadName || status == StatusEnum.BadRoles) {
            model.addAttribute("user", userService.getUserById(user.getId()));
            model.addAttribute("roles", Role.values());
            return "user-edit";
        }

        redirectAttributes.addFlashAttribute("toast", toast);
        return "redirect:/users/" + user.getId();
    }

    @GetMapping("/confirmEmail/{email}/{code}")
    public String confirmEmail(Model model, @PathVariable String email, @PathVariable String code) {
        boolean isActivated = userService.confirmNewEmail(email, code);
        if (isActivated)
            model.addAttribute("toast", "Почта успешно изменена на " + email);
        else
            model.addAttribute("toast", "Произошла ошибка при изменении почты.\r\nКод не действителен");
        return "home";
    }

    @PostMapping("/users/{id}/uploadAvatar")
    public @ResponseBody String uploadAvatar(@PathVariable(value = "id") long id, String image, @AuthenticationPrincipal User user) throws IOException {
        if (!userService.uploadAvatar(id, image))
            return "error";

        if (user.getId() == id)
            user.setAvatarUrl(userService.getUserById(id).getAvatarUrl());

        return StatusEnum.Successfully.toString();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('Admin')")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "users";
    }

    @PostMapping("/users/{id}/changeStatus")
    @PreAuthorize("hasAuthority('Admin')")
    public String changeStatusUser(@PathVariable(value = "id") long id, RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("toast", userService.changeUserStatus(id) ?
                "Статус пользователя успешно изменен" : "Произошла ошибка при изменении статуса пользователя");
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/remove")
    @PreAuthorize("hasAuthority('Admin')")
    public String removeUser(@PathVariable(value = "id") long id, RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("toast", userService.removeUser(id) ?
                "Пользователь успешно удален" : "Произошла ошибка при удалении пользователя");
        return "redirect:/users";
    }

    @GetMapping("/users/add")
    @PreAuthorize("hasAuthority('Admin')")
    public String addUserView(Model model) {
        model.addAttribute("roles", Role.values());
        return "add-user";
    }

    @PostMapping("/users/add")
    @PreAuthorize("hasAuthority('Admin')")
    public String addUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes)
    {
        model.addAttribute("roles", Role.values());

        if (!userDto.getPassword().equals(userDto.getAgainPassword()))
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "password",  "Пароли не совпадают"));
        if (bindingResult.hasErrors())
            return "add-user";

        switch (userService.addUser(userDto)) {
            case BadName:
                model.addAttribute("toast", "Имя пользователя занято");
                return "add-user";
            case BadEmail:
                model.addAttribute("toast", "Пользователь с email " + userDto.getEmail() + " уже существует");
                return "add-user";
            case BadRoles:
                model.addAttribute("toast", "Должно быть выбрана хотя бы 1 роль");
                return "add-user";
        }

        redirectAttributes.addFlashAttribute("toast", "Пользователь успешно добавлен");
        return "redirect:/users";
    }

    @ModelAttribute("user")
    public UserDto userDto() {
        return new UserDto();
    }
}
