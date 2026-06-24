package com.freshbite.util;

import com.freshbite.dto.*;
import com.freshbite.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public final class MapperUtil {

    private MapperUtil() {
    }

    public static UserResponse toUserResponse(User user, boolean admin) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                admin
        );
    }

    public static MealResponse toMealResponse(Meal meal) {
        return new MealResponse(
                meal.getId(),
                meal.getName(),
                meal.getDescription(),
                meal.getCategory(),
                meal.getCalories(),
                meal.getProtein(),
                meal.getCarbs(),
                meal.getFats(),
                meal.getPrice(),
                meal.getImageUrl()
        );
    }

    public static List<MealResponse> toMealResponseList(List<Meal> meals) {
        return meals.stream().map(MapperUtil::toMealResponse).collect(Collectors.toList());
    }

    public static CartItemResponse toCartItemResponse(CartItem cartItem) {
        Meal meal = cartItem.getMeal();
        double subtotal = meal.getPrice() * cartItem.getQuantity();
        return new CartItemResponse(
                cartItem.getId(),
                meal.getId(),
                meal.getName(),
                meal.getImageUrl(),
                meal.getPrice(),
                cartItem.getQuantity(),
                subtotal
        );
    }

    public static List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItems) {
        return cartItems.stream().map(MapperUtil::toCartItemResponse).collect(Collectors.toList());
    }

    public static OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getMeal().getId(),
                orderItem.getMeal().getName(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }

    public static OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(MapperUtil::toOrderItemResponse)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getCustomerName(),
                order.getPhone(),
                order.getDeliveryAddress(),
                order.getTotalAmount(),
                order.getOrderStatus().name(),
                order.getCreatedAt(),
                items
        );
    }

    public static List<OrderResponse> toOrderResponseList(List<Order> orders) {
        return orders.stream().map(MapperUtil::toOrderResponse).collect(Collectors.toList());
    }

    public static Meal applyMealRequest(Meal meal, MealRequest request) {
        meal.setName(request.getName());
        meal.setDescription(request.getDescription());
        meal.setCategory(request.getCategory());
        meal.setCalories(request.getCalories());
        meal.setProtein(request.getProtein());
        meal.setCarbs(request.getCarbs());
        meal.setFats(request.getFats());
        meal.setPrice(request.getPrice());
        meal.setImageUrl(request.getImageUrl());
        return meal;
    }
}
