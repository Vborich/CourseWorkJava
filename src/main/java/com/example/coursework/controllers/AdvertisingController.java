package com.example.coursework.controllers;

import com.example.coursework.models.Advertising;
import com.example.coursework.services.AdvertisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasAuthority('Admin')")
public class AdvertisingController {

    @Autowired
    AdvertisingService advertisingService;

    @GetMapping("/advertisings")
    public String viewAdvertisings(Model model) {
        model.addAttribute("advertisings", advertisingService.getAdvertisings());
        return "advertisings";
    }

    @GetMapping("/advertisings/add")
    public String addAdvertising(Model model) {
        return "add-advertising";
    }

    @PostMapping("/advertisings/add")
    public String addAdvertisingPost(@Valid @ModelAttribute("advertising") Advertising advertising, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors())
            return "add-advertising";

        if (!advertisingService.addAdvertising(advertising))
        {
            model.addAttribute("toast", "Реклама с таким названием уже существует");
            return "add-advertising";
        }

        redirectAttributes.addFlashAttribute("toast", "Реклама успешно добавлена");
        return "redirect:/advertisings";
    }

    @PostMapping("/advertisings/{id}/remove")
    public String removeAdvertising(@PathVariable(value = "id") long id, RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("toast",  advertisingService.removeAdvertising(id) ?
                "Реклама успешно удалена" : "Произошла ошибка при удалении рекламы");
        return "redirect:/advertisings";
    }

    @GetMapping("/advertisings/{id}/edit")
    public String editAdvertising(@PathVariable(value = "id") long id, Model model) {
        Advertising advertising =  advertisingService.getAdvertisingById(id);
        if (advertising == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");
        model.addAttribute("advertising", advertising);
        return "edit-advertising";
    }

    @GetMapping("/advertisings/{id}")
    public String viewAdvertising(@PathVariable(value = "id") long id, Model model) {
        Advertising advertising = advertisingService.getAdvertisingById(id);
        if (advertising == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");
        model.addAttribute("advertising", advertising);
        return "advertising";
    }

    @PostMapping("/advertisings/{id}/edit")
    public String editAdvertisingPost(@Valid @ModelAttribute("advertising") Advertising advertising, BindingResult bindingResult,
                                  Model model, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors())
            return "edit-advertising";

        if (!advertisingService.editAdvertising(advertising))
        {
            model.addAttribute("toast", "Реклама с таким названием уже существует");
            return "edit-advertising";
        }

        redirectAttributes.addFlashAttribute("toast", "Данные рекламы успешно изменены");
        return "redirect:/advertisings/" + advertising.getId();
    }

    @ModelAttribute("advertising")
    public Advertising advertising() {
        return new Advertising();
    }
}
