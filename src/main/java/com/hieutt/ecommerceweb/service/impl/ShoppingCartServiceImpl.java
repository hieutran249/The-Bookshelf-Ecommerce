package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.dto.ShoppingCartDto;
import com.hieutt.ecommerceweb.entity.ShoppingCart;
import com.hieutt.ecommerceweb.exception.ResourceNotFoundException;
import com.hieutt.ecommerceweb.repository.ShoppingCartRepository;
import com.hieutt.ecommerceweb.service.ShoppingCartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public ShoppingCartServiceImpl(ShoppingCartRepository cartRepository) {
        this.cartRepository = cartRepository;
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
