package com.example.coursework.controllers;

import com.example.coursework.dto.UserDto;
import com.example.coursework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @ModelAttribute("user")
    public UserDto userDto() {
        return new UserDto();
    }

    @GetMapping("/forget-password")
    public  String forgetPassword()
    {
        return  "forget-password";
    }

    @PostMapping("/forget-password")
    public  String sendRecoverPasswordEmail(String email, Model model, RedirectAttributes redirectAttributes)
    {
        if (!userService.sendRecoverPasswordLink(email)) {
            model.addAttribute("toast", "Пользователя с email " + email + " не существует");
            return "forget-password";
        }

        redirectAttributes.addFlashAttribute("email", email);
        return "redirect:/check-email-box-recover-password";
    }

    @GetMapping("/recover-password/{code}")
    public String recoverPassword(Model model, @PathVariable String code) {
        if (!userService.checkRecoverPasswordCode(code)) {
            model.addAttribute("toast", "Код восстановления пароля не действителен");
            return "login";
        }
        return "/recover-password";
    }

    @PostMapping("/recover-password/{code}")
    public String recoverPasswordUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult, Model model, @PathVariable String code) {
        if (bindingResult.hasErrors())
            return "/recover-password";

        userService.recoverPassword(userDto.getPassword(), code);
        model.addAttribute("toast", "Пароль успешно обновлен");
        return "/login";
    }

    @GetMapping("/check-email-box-recover-password")
    public  String checkEmailBoxRecoverPassword(Model model)
    {
        return "check-email-box-recover-password";
    }

    @GetMapping("/login-error")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                if (ex.getMessage().contains("locked"))
                    errorMessage = "Учетная запись пользователя заблокирована";
                else if(ex.getMessage().contains("disabled"))
                    errorMessage = "Почта не подтверждена";
                else
                    errorMessage = "Неверный логин или пароль";
            }
        }

        model.addAttribute("toast", errorMessage);
        return "login";
    }
}
