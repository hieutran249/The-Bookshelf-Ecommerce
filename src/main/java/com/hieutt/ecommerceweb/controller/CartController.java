package com.hieutt.ecommerceweb.controller;

import com.hieutt.ecommerceweb.dto.UserDto;
import com.hieutt.ecommerceweb.entity.ShoppingCart;
import com.hieutt.ecommerceweb.entity.User;
import com.hieutt.ecommerceweb.service.ShoppingCartService;
import com.hieutt.ecommerceweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

@RequestMapping("/customer")
@PreAuthorize("hasRole('CUSTOMER')")
@Controller
public class CartController {
    private final ShoppingCartService cartService;
    private final UserService userService;

    public CartController(ShoppingCartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/my-cart")
    public String getCart(Model model, Principal principal, HttpSession session) {
        model.addAttribute("title", "My Cart");
        ShoppingCart cart = cartService.getCartByUser(principal);
        session.setAttribute("cart", cart);
        return "customer/cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam(value = "id") Long bookId,
                            Principal principal,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request,
                            HttpSession session) {
        Map<String, String> message = cartService.addToCart(bookId, principal);
        redirectAttributes.addFlashAttribute("type", message.get("type"));
        redirectAttributes.addFlashAttribute("detail", message.get("detail"));
        ShoppingCart cart = cartService.getCartByUser(principal);
        session.setAttribute("cart", cart);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/update-cart")
    public String updateCart(@RequestParam(value = "id") Long cartItemId,
                             @RequestParam(value = "quantity") int quantity,
                             @RequestParam(value = "action") String action,
                             RedirectAttributes redirectAttributes,
                             HttpSession session,
                             Principal principal) {
        Map<String, String> message = cartService.updateCart(cartItemId, quantity, action);
        redirectAttributes.addFlashAttribute("message", message);
        ShoppingCart cart = cartService.getCartByUser(principal);
        session.setAttribute("cart", cart);
        return "redirect:/customer/my-cart";
    }

    @GetMapping("/checkout")
    public String checkout(Principal principal, RedirectAttributes redirectAttributes, Model model) {
        User user = userService.getCurrentUser(principal);
        if (user.getAddress() == null || user.getPhoneNumber() == null) {
            redirectAttributes.addFlashAttribute("type", "addInfo");
            redirectAttributes.addFlashAttribute("detail", "Please add your address and phone number before checking out 🙏");
            return "redirect:/customer/my-account";
        }
        ShoppingCart cart = cartService.getCartByUser(principal);
        if (cart.getCartItemList().isEmpty()) {
            redirectAttributes.addFlashAttribute("type", "emptyCart");
            redirectAttributes.addFlashAttribute("detail", "You cannot checkout because your shopping cart is empty 🤦‍♂️");
            return "redirect:/customer/my-cart";
        }
        model.addAttribute("title", "Check out");
        return "customer/checkout";
    }
}