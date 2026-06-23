package com.freshbite.service.impl;

import com.freshbite.dto.CartItemRequest;
import com.freshbite.dto.CartItemResponse;
import com.freshbite.entity.CartItem;
import com.freshbite.entity.Meal;
import com.freshbite.entity.User;
import com.freshbite.exception.BadRequestException;
import com.freshbite.exception.ResourceNotFoundException;
import com.freshbite.repository.CartItemRepository;
import com.freshbite.repository.MealRepository;
import com.freshbite.repository.UserRepository;
import com.freshbite.service.CartService;
import com.freshbite.util.MapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    public CartServiceImpl(CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           MealRepository mealRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(Long userId) {
        validateUser(userId);
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return MapperUtil.toCartItemResponseList(cartItems);
    }

    @Override
    public CartItemResponse addToCart(Long userId, CartItemRequest request) {
        User user = validateUser(userId);
        Meal meal = mealRepository.findById(request.getMealId())
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + request.getMealId()));

        CartItem cartItem = cartItemRepository.findByUserIdAndMealId(userId, request.getMealId())
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setMeal(meal);
            cartItem.setQuantity(request.getQuantity());
        }

        CartItem savedItem = cartItemRepository.save(cartItem);
        return MapperUtil.toCartItemResponse(savedItem);
    }

    @Override
    public CartItemResponse updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        validateUser(userId);

        if (quantity < 1) {
            throw new BadRequestException("Quantity must be at least 1");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getUser().getId().equals(userId)) {
            throw new BadRequestException("Cart item does not belong to this user");
        }

        cartItem.setQuantity(quantity);
        CartItem updatedItem = cartItemRepository.save(cartItem);
        return MapperUtil.toCartItemResponse(updatedItem);
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
        validateUser(userId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getUser().getId().equals(userId)) {
            throw new BadRequestException("Cart item does not belong to this user");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(Long userId) {
        validateUser(userId);
        cartItemRepository.deleteByUserId(userId);
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}
