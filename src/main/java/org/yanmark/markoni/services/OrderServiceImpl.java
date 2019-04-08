package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Order;
import org.yanmark.markoni.domain.models.services.OrderServiceModel;
import org.yanmark.markoni.repositories.OrderRepository;

import java.time.LocalDateTime;
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
    public OrderServiceModel saveOrder(OrderServiceModel orderService) {
        orderService.setOrderedOn(LocalDateTime.now().plusMonths(1));
        Order order = this.modelMapper.map(orderService, Order.class);
        try {
            order = this.orderRepository.saveAndFlush(order);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(order, OrderServiceModel.class);
    }

    @Override
    public List<OrderServiceModel> getAllOrders() {
        List<Order> orders = this.orderRepository.findAll();
        if (orders == null) {
            throw new IllegalArgumentException("Orders was not found!");
        }
        return orders.stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<OrderServiceModel> getAllOrdersByCustomer(String username) {
        List<Order> orders = this.orderRepository.findAllOrdersByCustomer_UsernameOrderByExpiredOnDesc(username);
        if (orders == null) {
            throw new IllegalArgumentException("Orders was not found!");
        }
        return orders.stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public OrderServiceModel getOrderById(String id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order was not found!"));
        return this.modelMapper.map(order, OrderServiceModel.class);
    }
}
