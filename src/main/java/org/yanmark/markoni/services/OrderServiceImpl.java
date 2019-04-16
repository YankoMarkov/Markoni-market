package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Order;
import org.yanmark.markoni.domain.models.bindings.orders.OrderBindingModel;
import org.yanmark.markoni.domain.models.services.OrderServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(ProductService productService,
                            OrderRepository orderRepository,
                            ModelMapper modelMapper) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderServiceModel saveOrder(OrderBindingModel productOrder,
                                       UserServiceModel userService,
                                       Integer quantity) {
        ProductServiceModel productServiceModel = this.productService.getProductById(productOrder.getId());
        getAllOrdersByCustomer(userService.getUsername())
                .forEach(order -> {
                    if (order.getProduct().getId().equals(productServiceModel.getId()) &&
                            order.getCustomer().getId().equals(userService.getId())) {
                        throw new IllegalArgumentException("Customer already has this order!");
                    }
                });
        OrderServiceModel orderServiceModel = new OrderServiceModel();
        orderServiceModel.setProduct(productServiceModel);
        orderServiceModel.setOrderedOn(LocalDate.now());
        orderServiceModel.setCustomer(userService);
        orderServiceModel.setQuantity(quantity);
        orderServiceModel.setPrice(productServiceModel.getPrice().multiply(BigDecimal.valueOf(quantity)));
        Order order = this.modelMapper.map(orderServiceModel, Order.class);
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
        return this.orderRepository.findAll().stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<OrderServiceModel> getAllOrdersByCustomer(String username) {
        return this.orderRepository.findAllOrdersByCustomer_UsernameOrderByOrderedOnDesc(username).stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public OrderServiceModel getOrderById(String id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order was not found!"));
        return this.modelMapper.map(order, OrderServiceModel.class);
    }

    @Scheduled(fixedRate = 43200000)
    private void deleteExpiredOrders() {
        List<Order> orders = this.orderRepository.findAll();
        for (Order order : orders) {
            if (LocalDate.now().isAfter(order.getOrderedOn().plusMonths(1))) {
                this.orderRepository.deleteById(order.getId());
            }
        }
    }
}
