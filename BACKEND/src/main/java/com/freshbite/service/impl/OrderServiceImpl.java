package com.freshbite.service.impl;

import com.freshbite.dto.OrderRequest;
import com.freshbite.dto.OrderResponse;
import com.freshbite.entity.*;
import com.freshbite.exception.BadRequestException;
import com.freshbite.exception.ResourceNotFoundException;
import com.freshbite.repository.CartItemRepository;
import com.freshbite.repository.OrderRepository;
import com.freshbite.repository.UserRepository;
import com.freshbite.service.CartService;
import com.freshbite.service.OrderService;
import com.freshbite.util.MapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            CartItemRepository cartItemRepository,
                            CartService cartService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
    }

    @Override
    public OrderResponse placeOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty. Add meals before placing an order.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(request.getCustomerName());
        order.setPhone(request.getPhone());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setOrderStatus(OrderStatus.PENDING);

        double totalAmount = 0;

        for (CartItem cartItem : cartItems) {
            Meal meal = cartItem.getMeal();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMeal(meal);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(meal.getPrice());
            order.getOrderItems().add(orderItem);
            totalAmount += meal.getPrice() * cartItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);

        return MapperUtil.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return MapperUtil.toOrderResponseList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();
        return MapperUtil.toOrderResponseList(orders);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setOrderStatus(orderStatus);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid order status: " + status);
        }

        Order updatedOrder = orderRepository.save(order);
        return MapperUtil.toOrderResponse(updatedOrder);
    }
}
