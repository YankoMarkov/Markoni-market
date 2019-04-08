package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.models.services.OrderServiceModel;

import java.util.List;

public interface OrderService {

    OrderServiceModel saveOrder(OrderServiceModel orderService);

    List<OrderServiceModel> getAllOrders();

    List<OrderServiceModel> getAllOrdersByCustomer(String username);

    OrderServiceModel getOrderById(String id);
}
