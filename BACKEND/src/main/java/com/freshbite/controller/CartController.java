package com.freshbite.controller;

import com.freshbite.dto.ApiResponse;
import com.freshbite.dto.CartItemRequest;
import com.freshbite.dto.CartItemResponse;
import com.freshbite.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long userId) {
        List<CartItemResponse> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart retrieved", items));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> addToCart(@PathVariable Long userId,
                                                 @Valid @RequestBody CartItemRequest request) {
        CartItemResponse item = cartService.addToCart(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item added to cart", item));
    }

    @PutMapping("/{userId}/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long userId,
                                                        @PathVariable Long cartItemId,
                                                        @RequestBody Map<String, Integer> body) {
        Integer quantity = body.get("quantity");
        CartItemResponse item = cartService.updateCartItem(userId, cartItemId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Cart updated", item));
    }

    @DeleteMapping("/{userId}/{cartItemId}")
    public ResponseEntity<ApiResponse> removeCartItem(@PathVariable Long userId,
                                                        @PathVariable Long cartItemId) {
        cartService.removeCartItem(userId, cartItemId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart"));
    }
}
