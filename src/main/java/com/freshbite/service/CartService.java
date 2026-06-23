package com.freshbite.service;

import com.freshbite.dto.CartItemRequest;
import com.freshbite.dto.CartItemResponse;

import java.util.List;

public interface CartService {

    List<CartItemResponse> getCartItems(Long userId);

    CartItemResponse addToCart(Long userId, CartItemRequest request);

    CartItemResponse updateCartItem(Long userId, Long cartItemId, Integer quantity);

    void removeCartItem(Long userId, Long cartItemId);

    void clearCart(Long userId);
}
