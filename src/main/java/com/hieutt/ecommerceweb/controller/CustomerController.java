package com.hieutt.ecommerceweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/customers")
public class CustomerController {
    @GetMapping
    public String getAllCustomers() {
        return "/admin/customer";
    }
}
