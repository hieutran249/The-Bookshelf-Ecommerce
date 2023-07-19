package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.dto.OrderDto;
import com.hieutt.ecommerceweb.entity.OrderStatus;
import com.hieutt.ecommerceweb.entity.ShoppingCart;
import com.hieutt.ecommerceweb.entity.User;
import com.hieutt.ecommerceweb.service.OrderService;
import com.hieutt.ecommerceweb.service.ShoppingCartService;
import com.hieutt.ecommerceweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

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


    // CUSTOMER
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customer/orders")
    public String getOrdersByUser(Model model, Principal principal) {
        User user = userService.getCurrentUser(principal);
        model.addAttribute("orders", orderService.getOrdersByUser(user));
        model.addAttribute("title", "My orders");
        return "customer/orders";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
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

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customer/cancel-order/{id}")
    public String cancelOrder(@PathVariable(value = "id") Long orderId,
                              RedirectAttributes redirectAttributes) {
        orderService.cancelOrder(orderId);
        redirectAttributes.addFlashAttribute("canceled", "You have canceled the order 😭");
        return "redirect:/customer/orders";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customer/return-order/{id}")
    public String returnOrder(@PathVariable(value = "id") Long orderId,
                              RedirectAttributes redirectAttributes) {
        orderService.returnOrder(orderId);
        redirectAttributes.addFlashAttribute("returned", "You have returned the order 😭");
        return "redirect:/customer/orders";
    }


    // ADMIN
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/admin/orders")
    public String getAllOrders(Model model) {
        List<OrderDto> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("size", orders.size());
        model.addAttribute("title", "Manage Orders");
        return "admin/orders";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/admin/orders/accept/{id}")
    public String acceptOrder(@PathVariable("id") Long orderId,
                              RedirectAttributes redirectAttributes) {
        orderService.acceptOrder(orderId);
        redirectAttributes.addFlashAttribute("accepted", "You have accepted the order 👍");
        return "redirect:/admin/orders";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/admin/orders/reject/{id}")
    public String rejectOrder(@PathVariable("id") Long orderId,
                              RedirectAttributes redirectAttributes) {
        orderService.rejectOrder(orderId);
        redirectAttributes.addFlashAttribute("accepted", "You have rejected the order 👎");
        return "redirect:/admin/orders";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/admin/orders/detail/{id}")
    public String getOrderDetail(@PathVariable("id") Long orderId,
                                 Model model) {
        OrderDto order = orderService.getOrderById(orderId);
        switch (order.getOrderStatus()) {
            case QUEUED -> model.addAttribute("status", "The order hasn't been accepted ❌");
            case PACKAGING -> model.addAttribute("status", "The books are being packaged 📦");
            case DELIVERING -> model.addAttribute("status", "The books are being delivered 🚚");
            case DELIVERED -> model.addAttribute("status", "The books has been delivered 👌");
            case RETURNED -> model.addAttribute("status", "The books has been returned 😢");
            case CLOSED -> model.addAttribute("status", "The order has been closed 🔒");
            case REJECTED -> model.addAttribute("status", "The order has been rejected 🚫");
            case CANCELED -> model.addAttribute("status", "The order has been canceled 🤬");
        }
        model.addAttribute("order", order);
        model.addAttribute("title", "Order Details");
        return "admin/order-detail";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/admin/orders/nextStage/{id}")
    public String doNextStage(@RequestParam(value = "status") String status,
                              @PathVariable(value = "id") Long orderId) {
        orderService.doNextStage(orderId, status);

        return "redirect:/admin/orders/detail/" + orderId;
    }
}
