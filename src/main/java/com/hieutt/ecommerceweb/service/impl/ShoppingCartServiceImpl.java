package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.dto.ShoppingCartDto;
import com.hieutt.ecommerceweb.entity.Book;
import com.hieutt.ecommerceweb.entity.CartItem;
import com.hieutt.ecommerceweb.entity.ShoppingCart;
import com.hieutt.ecommerceweb.entity.User;
import com.hieutt.ecommerceweb.exception.ResourceNotFoundException;
import com.hieutt.ecommerceweb.repository.BookRepository;
import com.hieutt.ecommerceweb.repository.CartItemRepository;
import com.hieutt.ecommerceweb.repository.ShoppingCartRepository;
import com.hieutt.ecommerceweb.repository.UserRepository;
import com.hieutt.ecommerceweb.service.ShoppingCartService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Map<String, String> message;

    public ShoppingCartServiceImpl(ShoppingCartRepository cartRepository, CartItemRepository cartItemRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.message = new HashMap<>();
    }

    @Override
    public ShoppingCartDto createShoppingCart(ShoppingCartDto cartDto) {
        ShoppingCart cart = cartRepository.save(mapToEntity(cartDto));
        return mapToDto(cart);
    }

    @Override
    public List<ShoppingCartDto> getAllShoppingCarts() {
        List<ShoppingCart> carts = cartRepository.findAll();
        return carts.stream()
                .map(cart -> mapToDto(cart))
                .collect(Collectors.toList());
    }

    @Override
    public ShoppingCartDto getShoppingCartById(Long id) {
        ShoppingCart cart = findCartById(id);
        return mapToDto(cart);
    }

    @Override
    public ShoppingCartDto updateShoppingCart(Long id, ShoppingCartDto cartDto) {
        ShoppingCart cart = findCartById(id);
        cart.setQuantity(cartDto.getQuantity());
        cart.setTotalPrice(cartDto.getTotalPrice());

        cartRepository.save(cart);
        return mapToDto(cart);
    }

    @Override
    public void deleteShoppingCart(Long id) {
        ShoppingCart cart = findCartById(id);
        cartRepository.delete(cart);
    }

    @Override
    public Map<String, String> addToCart(Long bookId, Principal principal) {
        Book book = bookRepository.findById(bookId).get();
        ShoppingCart cart = getCartByUser(principal);
        CartItem cartItem;
        if (book.getQuantity() == 0) {
            message.put("type", "error");
            message.put("detail", "Sorry 😓 this book is out of stock 💔");
            return message;
        }
        if (cartItemRepository.existsByShoppingCartAndBook(cart, book)) {
            cartItem = cartItemRepository.findByShoppingCartAndBook(cart, book);
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setTotalPrice(cartItem.getBook().getPrice() * cartItem.getQuantity());
        }
        else {
            cartItem = CartItem.builder()
                    .shoppingCart(cart)
                    .book(book)
                    .quantity(1)
                    .build();
            cartItem.setTotalPrice(cartItem.getBook().getPrice() * cartItem.getQuantity());
        }
        cartItemRepository.save(cartItem);
        cart.setQuantity(calcCartQuantity(cart));

        // cal total price of cart
        double cartTotalPrice = calcCartTotalPrice(cart);
        cart.setTotalPrice(cartTotalPrice);
        cartRepository.save(cart);

        message.put("type", "success");
        message.put("detail", "Added to cart successfully 👌");
        return message;
    }

    @Override
    public ShoppingCart getCartByUser(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).get();
        return cartRepository.findShoppingCartByUser(user);
    }

    @Override
    public Map<String, String> updateCart(Long cartItemId, int quantity, String action) {
        if (action.equals("update")) {
            CartItem cartItem = cartItemRepository.findById(cartItemId).get();
            ShoppingCart cart = cartItem.getShoppingCart();
            // update cartItem
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(cartItem.getBook().getPrice() * cartItem.getQuantity());
            cartItemRepository.save(cartItem);

            // update Cart
            cart.setQuantity(calcCartQuantity(cart));
            cart.setTotalPrice(calcCartTotalPrice(cart));
            cartRepository.save(cart);

            // message
            message.put("message", "Updated shopping cart successfully 🤗");
        }
        if (action.equals("delete")) {
            CartItem cartItem = cartItemRepository.findById(cartItemId).get();
            ShoppingCart cart = cartItem.getShoppingCart();
            // update cartItem
            cart.getCartItemList().remove(cartItem);
            cartItemRepository.delete(cartItem);

            // update Cart
            cart.setQuantity(cart.getCartItemList().size());
            cart.setTotalPrice(calcCartTotalPrice(cart));
            cartRepository.save(cart);

            // message
            message.put("message", "Deleted cart item from the shopping cart successfully 🤗");
        }

        return message;
    }

    private double calcCartTotalPrice(ShoppingCart cart) {
        List<CartItem> cartItems = cart.getCartItemList();
        return cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(0.0, Double::sum);
    }

    private int calcCartQuantity(ShoppingCart cart) {
        List<CartItem> cartItems = cart.getCartItemList();
        return cartItems.stream()
                .map(CartItem::getQuantity)
                .reduce(0, Integer::sum);
    }

    private ShoppingCart findCartById(Long id) {
        return cartRepository
                .findById(id).orElseThrow(()
                        -> new ResourceNotFoundException("Shopping cart", "id", id));
    }

    private ShoppingCartDto mapToDto(ShoppingCart cart) {
        return modelMapper.map(cart, ShoppingCartDto.class);
    }
    private ShoppingCart mapToEntity(ShoppingCartDto cartDto) {
        return modelMapper.map(cartDto, ShoppingCart.class);
    }
}
