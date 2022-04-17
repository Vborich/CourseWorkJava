package com.example.coursework.controllers;

import com.example.coursework.dto.CompanyDto;
import com.example.coursework.models.User;
import com.example.coursework.services.AdvertisingSubtypeService;
import com.example.coursework.services.OrderService;
import com.example.coursework.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class OrderController {

    @Autowired
    AdvertisingSubtypeService advertisingSubtypeService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/advertising-subtypes/{id}/orders/add")
    public String addOrder(@PathVariable(value = "id") long id, @AuthenticationPrincipal User user,
                           RedirectAttributes redirectAttributes, Model model) {
        if (user.getCompany() == null)
        {
            redirectAttributes.addFlashAttribute("toast",
                    "Чтобы сделать заказ необходимо состоять в компании");
            return "redirect:/";
        }
        model.addAttribute("advertising", advertisingSubtypeService.getAdvertisingSubtype(id));
        return "add-order";
    }

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/advertising-subtypes/{id}/orders/add")
    public String addCompanyPost(@PathVariable(value = "id") long id, int countUnits, @AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes)
    {
        if (!orderService.addOrder(countUnits, user.getId(), id))
        {
            model.addAttribute("toast", "Стоимость заказа слишком высокая");
            model.addAttribute("advertising", advertisingSubtypeService.getAdvertisingSubtype(id));
            return "add-order";
        }

        redirectAttributes.addFlashAttribute("toast", "Заказ успешно оформлен");
        return "redirect:/";
    }

    @GetMapping("/my-orders")
    public String viewMyOrders(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("orders", userService.getUserById(user.getId()).getOrders());
        return "my-orders";
    }

    @GetMapping("/orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderService.getOrders());
        return "orders";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable(value = "id") long id,
                                        RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("toast", orderService.cancelOrder(id) ?
                "Заказ успешно отменен" : "Произошла ошибка при отмене заказа");
        return "redirect:/orders/" + id;
    }

    @PostMapping("/orders/{id}/accept")
    public String acceptOrder(@PathVariable(value = "id") long id,
                              RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addFlashAttribute("toast", orderService.acceptOrder(id) ?
                "Заказ успешно принят" : "Произошла ошибка при принятии заказа");
        return "redirect:/orders/" + id;
    }

    @GetMapping("/orders/{id}")
    public String viewOrder(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "order";
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/advertising-subtypes/{id}/orders/add-to-user")
    public String addOrderToUser(@PathVariable(value = "id") long id,
                           RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("advertising", advertisingSubtypeService.getAdvertisingSubtype(id));
        model.addAttribute("users", userService.getUsersWithUserRole());
        return "add-order-to-user";
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/advertising-subtypes/{id}/orders/add-to-user")
    public String addOrderToUserPost(@PathVariable(value = "id") long id, int countUnits, long userId, Model model,
                                     RedirectAttributes redirectAttributes)
    {
        if (!orderService.addOrder(countUnits, userId, id))
        {
            model.addAttribute("toast", "Стоимость заказа слишком высокая");
            model.addAttribute("advertising", advertisingSubtypeService.getAdvertisingSubtype(id));
            model.addAttribute("users", userService.getUsersWithUserRole());
            return "add-order-to-user";
        }

        redirectAttributes.addFlashAttribute("toast", "Заказ успешно оформлен");
        return "redirect:/";
    }

}
