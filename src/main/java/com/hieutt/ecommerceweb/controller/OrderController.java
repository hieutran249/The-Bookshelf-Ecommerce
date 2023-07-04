package com.hieutt.ecommerceweb.controller;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {
    @GetMapping
    public String getAllOrders() {
        return "/admin/order";
    }
}
