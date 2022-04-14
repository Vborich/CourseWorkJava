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

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }
}