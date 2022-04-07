package com.example.coursework.controllers;

import com.example.coursework.dto.UserDto;
import com.example.coursework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class SignUpController {

    @ModelAttribute("user")
    public UserDto userDto() {
        return new UserDto();
    }

    @Autowired
    private UserService userService;

    @GetMapping("/sign-up")
    public  String signUp()
    {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes)
    {
        if (!userDto.getPassword().equals(userDto.getAgainPassword()))
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "password",  "Пароли не совпадают"));
        if (bindingResult.hasErrors())
            return "sign-up";

        switch (userService.addUser(userDto)) {
            case BadName:
                model.addAttribute("toast", "Имя пользователя занято");
                return "sign-up";
            case BadEmail:
                model.addAttribute("toast", "Пользователь с email " + userDto.getEmail() + " уже существует");
                return "sign-up";
            default: break;
        }

        redirectAttributes.addFlashAttribute("email", userDto.getEmail());
        return "redirect:/check-email-box";
    }

    @GetMapping("/activate/{code}")
    public String confirmEmail(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated)
            model.addAttribute("toast", "Почта успешно подтверждена");
        else
            model.addAttribute("toast", "Произошла ошибка при подтвреждении почты.\r\nКод не действителен");
        return "login";
    }

    @GetMapping("/check-email-box")
    public  String checkEmailBox(Model model)
    {
        return "check-email-box";
    }
}
