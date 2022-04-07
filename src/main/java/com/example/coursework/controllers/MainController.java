package com.example.coursework.controllers;

import com.example.coursework.models.User;
import com.example.coursework.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("toast", "vlad");
        model.addAttribute("title", "vlad");
        return "home";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam String name, Model model) {
        User user = new User();
        user.setUsername(name);
        userRepository.save(user);
        return "redirect:/users";
    }

    @PostMapping ("/users/{id}/remove")
    public String userRemove(@PathVariable(value = "id") long id, Model model) {
        if(!userRepository.existsById(id))
            return "bad";
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return  "redirect:/users";
    }
}