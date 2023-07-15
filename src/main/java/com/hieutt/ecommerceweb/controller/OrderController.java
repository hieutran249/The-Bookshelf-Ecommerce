package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.entity.ShoppingCart;
import com.hieutt.ecommerceweb.entity.User;
import com.hieutt.ecommerceweb.service.OrderService;
import com.hieutt.ecommerceweb.service.ShoppingCartService;
import com.hieutt.ecommerceweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final ShoppingCartService cartService;

    public OrderController(OrderService orderService, UserService userService, ShoppingCartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/customer/orders")
    public String getOrdersByUser(Model model, Principal principal) {
        User user = userService.getCurrentUser(principal);
        model.addAttribute("orders", orderService.getOrdersByUser(user));
        model.addAttribute("title", "My orders");
        return "customer/orders";
    }
    @PostMapping("/customer/place-order")
    public String placeOrder(@RequestParam(value = "paymentMethod") String paymentMethod,
                             Principal principal,
                             Model model,
                             HttpSession session) {
        User user = userService.getCurrentUser(principal);
        orderService.createOrder(user, paymentMethod);
        model.addAttribute("orders", orderService.getOrdersByUser(user));
        model.addAttribute("success", "You have successfully ordered 👏 " + "\n" +
                "Please wait for the admin to accept your order 🙏");
        model.addAttribute("title", "My orders");
        // reset cart in session
        ShoppingCart cart = cartService.getCartByUser(principal);
        session.removeAttribute("cart");
        session.setAttribute("cart", cart);
        return "customer/orders";
    }

    @GetMapping("/customer/cancel-order/{id}")
    public String cancelOrder(@PathVariable(value = "id") Long orderId,
                              RedirectAttributes redirectAttributes) {
        orderService.cancelOrder(orderId);
        redirectAttributes.addFlashAttribute("canceled", "You have canceled the order 😭");
        return "redirect:/customer/orders";
    }

    @GetMapping("/admin/orders")
    public String getAllOrders() {
        return "admin/orders";
    }
}
