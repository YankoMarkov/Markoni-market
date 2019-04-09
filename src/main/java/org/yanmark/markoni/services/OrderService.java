package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.models.services.OrderServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;

import java.util.List;

public interface OrderService {

    OrderServiceModel saveOrder(OrderServiceModel orderService,
                                ProductServiceModel productService,
                                UserServiceModel userService,
                                Integer quantity);

    OrderServiceModel updateOrder(OrderServiceModel orderService);

    void deleteOrder(String id);

    List<OrderServiceModel> getAllOrders();

    List<OrderServiceModel> getAllOrdersByCustomer(String username);

    OrderServiceModel getOrderById(String id);
}
