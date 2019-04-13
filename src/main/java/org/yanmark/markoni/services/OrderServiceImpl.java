package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Order;
import org.yanmark.markoni.domain.models.services.OrderServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderServiceModel saveOrder(ProductServiceModel productService,
                                       OrderServiceModel orderService,
                                       UserServiceModel userService,
                                       Integer quantity) {
        getAllOrdersByCustomer(userService.getUsername())
                .forEach(order -> {
                    if (order.getProduct().getId().equals(productService.getId()) &&
                            order.getCustomer().getId().equals(userService.getId())) {
                        throw new IllegalArgumentException("Customer already has this order!");
                    }
                });
        orderService.setProduct(productService);
        orderService.setOrderedOn(LocalDate.now());
        orderService.setCustomer(userService);
        orderService.setQuantity(quantity);
        orderService.setPrice(productService.getPrice().multiply(BigDecimal.valueOf(quantity)));
        Order order = this.modelMapper.map(orderService, Order.class);
        try {
            order = this.orderRepository.saveAndFlush(order);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(order, OrderServiceModel.class);
    }

    @Override
    public OrderServiceModel updateOrder(OrderServiceModel orderService) {
        Order order = this.modelMapper.map(orderService, Order.class);
        try {
            order = this.orderRepository.saveAndFlush(order);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(order, OrderServiceModel.class);
    }

    @Override
    public void deleteOrder(String id) {
        try {
            this.orderRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public List<OrderServiceModel> getAllOrders() {
        List<Order> orders = this.orderRepository.findAll();
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }
        orders = orders.stream()
                .peek(order -> {
                    if (LocalDate.now().isAfter(order.getOrderedOn().plusMonths(1))) {
                        deleteOrder(order.getId());
                    }
                })
                .collect(Collectors.toList());
        return orders.stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<OrderServiceModel> getAllOrdersByCustomer(String username) {
        List<Order> orders = this.orderRepository.findAllOrdersByCustomer_UsernameOrderByOrderedOnDesc(username);
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }
        orders = orders.stream()
                .peek(order -> {
                    if (LocalDate.now().isAfter(order.getOrderedOn().plusMonths(1))) {
                        deleteOrder(order.getId());
                    }
                })
                .collect(Collectors.toList());
        return orders.stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public OrderServiceModel getOrderById(String id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order was not found!"));
        if (LocalDate.now().isAfter(order.getOrderedOn().plusMonths(1))) {
            deleteOrder(order.getId());
            throw new IllegalArgumentException("The order had expired and was deleted!");
        }
        return this.modelMapper.map(order, OrderServiceModel.class);
    }
}
