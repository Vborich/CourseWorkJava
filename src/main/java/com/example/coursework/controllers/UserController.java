package com.example.coursework.controllers;

import com.example.coursework.models.Status.StatusEnum;
import com.example.coursework.models.User;
import com.example.coursework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return "user-edit";
    }

    @PostMapping("/users/{id}/edit")
    public String editUserPost(User user, RedirectAttributes redirectAttributes, Model model) {
        String toast = "";
        var status = userService.editUser(user);
        switch (status)
        {
            case BadEmail: toast = "Email " + user.getEmail() + " уже используется"; break;
            case BadName: toast = "Имя пользователя " + user.getUsername() + " занято"; break;
            case ConfirmEmail: toast = "Перейдите на почтовый ящик и выполните подтверждение новой почты"; break;
            case Successfully: toast = "Данные пользователя успешно изменены"; break;
            case BadUser: throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");
        }
        model.addAttribute("toast", toast);
        if (status == StatusEnum.BadEmail || status == StatusEnum.BadName)
            return "user-edit";

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
        user.setAvatarUrl(userService.getUserById(id).getAvatarUrl());
        return StatusEnum.Successfully.toString();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('Admin')")
    public String getUsers(Model model) {
      return "users";
    }
}
