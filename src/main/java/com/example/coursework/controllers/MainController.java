package com.example.coursework.controllers;

import com.example.coursework.models.Advertising;
import com.example.coursework.models.User;
import com.example.coursework.repo.UserRepository;
import com.example.coursework.services.AdvertisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    AdvertisingService advertisingService;

    @GetMapping("/")
    public String home(Model model) {
        ArrayList<Advertising> advertisings = new ArrayList<Advertising>((Collection<? extends Advertising>)
                advertisingService.getAdvertisings());
        advertisings.sort(Comparator.comparing(Advertising::getDescription));
        model.addAttribute("advertisings", advertisings);
        return "home";
    }

    @GetMapping("/view-advertising/{id}")
    public String viewAdvertising(@PathVariable(value = "id") long id, Model model) {
        Advertising advertising = advertisingService.getAdvertisingById(id);
        if (advertising == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");
        model.addAttribute("advertising", advertising);
        return "view-advertising";
    }
}