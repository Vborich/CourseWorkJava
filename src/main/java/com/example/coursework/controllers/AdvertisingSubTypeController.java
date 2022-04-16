package com.example.coursework.controllers;

import com.example.coursework.models.Advertising;
import com.example.coursework.models.AdvertisingSubtype;
import com.example.coursework.services.AdvertisingService;
import com.example.coursework.services.AdvertisingSubtypeService;
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
public class AdvertisingSubTypeController {

    @Autowired
    AdvertisingService advertisingService;

    @Autowired
    AdvertisingSubtypeService advertisingSubtypeService;

    @GetMapping("/advertisings/{id}/subtypes")
    public String viewAdvertisingSubtypes(@PathVariable(value = "id") long id, Model model) {
        Advertising advertising = advertisingService.getAdvertisingById(id);
        if (advertising == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");

        model.addAttribute("advertisingSubtypes", advertising.getAdvertisingSubtypes());
        model.addAttribute("id", id);
        return "advertising-subtypes";
    }

    @GetMapping("/advertisings/{id}/subtypes/add")
    public String addAdvertisingTypeToAdvertising(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("id", id);
        return "add-advertising-subtype-to-advertising";
    }

    @PostMapping("/advertisings/{id}/subtypes/add")
    public String addAdvertisingTypeToAdvertisingPost(@PathVariable(value = "id") long id,
                                       @ModelAttribute("advertisingSubtype") AdvertisingSubtype advertisingSubtype,
                                       BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors())
            return "add-advertising-subtype-to-advertising";

        advertisingSubtypeService.addAdvertisingSubtype(advertisingSubtype, id);
        redirectAttributes.addFlashAttribute("toast", "Тип рекламы успешно добавлен");
        return "redirect:/advertisings/" + id + "/subtypes";
    }

    @PostMapping("/advertisings/{id}/subtypes/{subtypeId}/remove")
    public String removeAdvertisingSubtype(@PathVariable(value = "id") long id, @PathVariable(value = "subtypeId") long subtypeId,
                                           RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("toast", advertisingSubtypeService.removeAdvertisingSubtype(subtypeId) ?
                "Тип рекламы успешно удален" : "Произошла ошибка при удалении типа рекламы");
        return "redirect:/advertisings/"+ id +"/subtypes";
    }

    @GetMapping("/advertising-subtypes/{id}")
    public String viewAdvertisingSubtype(@PathVariable(value = "id") long id, Model model) {
        AdvertisingSubtype advertisingSubtype = advertisingSubtypeService.getAdvertisingSubtype(id);
        if (advertisingSubtype == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");
        model.addAttribute("advertisingSubtype", advertisingSubtype);
        return "advertising-subtype";
    }

    @GetMapping("/advertising-subtypes/{id}/edit")
    public String editAdvertisingSubtype(@PathVariable(value = "id") long id, Model model) {
        AdvertisingSubtype advertisingSubtype = advertisingSubtypeService.getAdvertisingSubtype(id);
        if (advertisingSubtype == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");
        model.addAttribute("advertisingSubtype", advertisingSubtype);
        return "edit-advertising-subtype";
    }

    @PostMapping("/advertising-subtypes/{id}/edit")
    public String editAdvertisingPost(@Valid @ModelAttribute("advertisingSubtype") AdvertisingSubtype advertisingSubtype,
                                      BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors())
            return "edit-advertising-subtype";

        advertisingSubtypeService.editAdvertising(advertisingSubtype);
        redirectAttributes.addFlashAttribute("toast", "Данные типа рекламы успешно изменены");
        return "redirect:/advertising-subtypes/" + advertisingSubtype.getId();
    }

    @ModelAttribute("advertisingSubtype")
    public AdvertisingSubtype advertisingSubtype() {
        return new AdvertisingSubtype();
    }
}
