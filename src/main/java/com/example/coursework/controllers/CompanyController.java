package com.example.coursework.controllers;

import com.example.coursework.dto.CompanyDto;
import com.example.coursework.mappers.CompanyMapper;
import com.example.coursework.mappers.CompanyMapperImp;
import com.example.coursework.models.Company;
import com.example.coursework.models.User;
import com.example.coursework.services.CompanyService;
import com.example.coursework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.ArrayList;

@Controller
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;

    CompanyMapper companyMapper = new CompanyMapperImp();

    @GetMapping("/companies/add")
    public String addCompany(Model model) {
        model.addAttribute("action", "add");
        return "add-company";
    }

    @PostMapping("/companies/add")
    public String addCompanyPost(@Valid @ModelAttribute("company") CompanyDto companyDto, BindingResult bindingResult, @AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes)
    {
        model.addAttribute("action", "add");
        if (bindingResult.hasErrors())
            return "add-company";

        if (!companyService.addCompany(companyMapper.toModel(companyDto)))
        {
            model.addAttribute("toast", "Компании с данным именем уже существует");
            return "add-company";
        }

        redirectAttributes.addFlashAttribute("toast", "Компания успешно добавлена");
        return "redirect:/companies";
    }

    @GetMapping("/companies")
    @PreAuthorize("hasAuthority('Admin')")
    public String viewCompanies(Model model) {
        model.addAttribute("companies", companyService.getCompanies());
        return "companies";
    }

    @PostMapping("/companies/{id}/remove")
    @PreAuthorize("hasAuthority('Admin')")
    public String removeUser(@PathVariable(value = "id") long id, RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("toast", companyService.removeCompany(id) ?
                "Компания успешно удалена" : "Произошла ошибка при удалении компании");
        return "redirect:/companies";
    }

    @GetMapping("/companies/{id}/edit")
    public String editCompany(@PathVariable(value = "id") long id, Model model) {
        Company company = companyService.getCompanyById(id);
        if (company == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");
        model.addAttribute("company", companyMapper.toDto(company));
        return "edit-company";
    }

    @PostMapping("/companies/{id}/edit")
    public String editCompanyPost(@Valid @ModelAttribute("company") CompanyDto companyDto, BindingResult bindingResult,
                                  Model model, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors())
            return "edit-company";
        if (!companyService.editCompany(companyMapper.toModel(companyDto)))
        {
            model.addAttribute("toast", "Компании с данным именем уже существует");
            return "edit-company";
        }

        redirectAttributes.addFlashAttribute("toast", "Данные компании успешно изменены");
        return "redirect:/companies/" + companyDto.getId();
    }

    @GetMapping("/companies/{id}")
    public String viewCompany(@PathVariable(value = "id") long id, Model model) {
        Company company = companyService.getCompanyById(id);
        if (company == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");
        model.addAttribute("company", company);
        return "company";
    }

    @GetMapping("/companies/{id}/users")
    public String viewCompanyUsers(@PathVariable(value = "id") long id, Model model) {
        Company company = companyService.getCompanyById(id);
        if (company == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be NOT FOUND (CODE 404)\n");

        ArrayList<User> users = new ArrayList<>(company.getUsers());
        if (company.getUserOwner() != null) {
            users.remove(company.getUserOwner());
            users.add(0, company.getUserOwner());
        }

        model.addAttribute("users", users);
        model.addAttribute("id", id);
        return "company-users";
    }

    @GetMapping("/companies/{id}/users/add")
    public String addUserToCompany(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("users", userService.getUsersWithoutCompany());
        model.addAttribute("id", id);
        return "add-user-to-company";
    }

    @PostMapping("/companies/{id}/users/add")
    public String addUserToCompanyPost(@PathVariable(value = "id") long id, long userId,
                                       RedirectAttributes redirectAttributes, Model model) {
        switch (companyService.addUserToCompany(id, userId))
        {
            case BadUserCompany:
                model.addAttribute("toast", "Пользователь уже добавлен в компанию");
                return "add-user-to-company";
            case BadCompany:
            case BadUser:
                redirectAttributes.addFlashAttribute("toast", "Произошла ошибка при добавлении пользователя");
                break;
            default:
                redirectAttributes.addFlashAttribute("toast", "Пользователь успешно добавлен в компанию");
                break;
        }

        return "redirect:/companies/" + id + "/users";
    }

    @PostMapping("/companies/{id}/users/{userId}/remove")
    public String removeUserFromCompany(@PathVariable(value = "id") long id, @PathVariable(value = "userId") long userId,
                                        RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("toast", userService.removeUserFromCompany(userId) ?
                "Пользователь успешно удален из компании" : "Произошла ошибка при удалении пользователя из компании");
        return "redirect:/companies/" + id + "/users";
    }

    @PostMapping("/companies/{id}/users/{userId}/setOwner")
    public String setCompanyOwner(@PathVariable(value = "id") long id, @PathVariable(value = "userId") long userId,
                                  RedirectAttributes redirectAttributes, @AuthenticationPrincipal User user,
                                  Model model) {
        redirectAttributes.addFlashAttribute("toast", companyService.setUserOwner(id, userId) ?
                "Пользователь успешно назначен владельцом компании" : "Произошла ошибка при назначении владельца компании");
        return "redirect:/companies/" + id + (user.getAuthorities()
                .stream().anyMatch(ga -> ga.getAuthority().equals("User"))  ? "" : "/users");
    }

    @GetMapping("/company/join")
    public String changeCompanyForUser(@AuthenticationPrincipal User authUser, RedirectAttributes redirectAttributes, Model model) {
        User user = userService.getUserById(authUser.getId());
        if (user.getOwnCompany() != null)
        {
            redirectAttributes.addFlashAttribute("toast", "Необходимо назначить нового владельца компании");
            return "redirect:/companies/" + user.getOwnCompany().getId();
        }
        model.addAttribute("companies", companyService.getCompanies());
        return "user-change-company";
    }

    @PostMapping("/company/join")
    public String changeCompanyForUserPost(long companyId, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes, Model model) {
        if (!companyService.changeCompanyForUser(companyId, user.getId()))
        {
            redirectAttributes.addFlashAttribute("toast", "Произошла ошибка при вступлении в компанию");
            return "redirect:/companies/" + user.getCompany().getId();
        }

        user.setCompany(companyService.getCompanyById(companyId));
        redirectAttributes.addFlashAttribute("toast","Вы успешно вступили в компанию!");
        return "redirect:/companies/" + companyId;
    }

    @ModelAttribute("company")
    public CompanyDto companyDto() {
        return new CompanyDto();
    }

    @GetMapping("/companies/create")
    public String createCompany(Model model) {
        model.addAttribute("action", "create");
        return "add-company";
    }

    @PostMapping("/companies/create")
    public String createCompanyPost(@Valid @ModelAttribute("company") CompanyDto companyDto, BindingResult bindingResult, @AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes)
    {
        model.addAttribute("action", "create");
        if (bindingResult.hasErrors())
            return "add-company";

        if (!companyService.createCompany(companyMapper.toModel(companyDto), user.getId()))
        {
            model.addAttribute("toast", "Компании с данным именем уже существует");
            return "add-company";
        }

        user.setCompany(userService.getUserById(user.getId()).getCompany());
        redirectAttributes.addFlashAttribute("toast", "Компания успешно добавлена");
        return "redirect:/companies/" + user.getCompany().getId();
    }
}
