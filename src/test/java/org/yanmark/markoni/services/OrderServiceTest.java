package org.yanmark.markoni.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.yanmark.markoni.domain.entities.Category;
import org.yanmark.markoni.domain.entities.Order;
import org.yanmark.markoni.domain.entities.Product;
import org.yanmark.markoni.domain.entities.User;
import org.yanmark.markoni.domain.models.bindings.orders.OrderBindingModel;
import org.yanmark.markoni.domain.models.services.OrderServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.OrderRepository;
import org.yanmark.markoni.repositories.ProductRepository;
import org.yanmark.markoni.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private ProductRepository mockProductRepository;

    @MockBean
    private OrderRepository mockOrderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void saveOrder_whenValidOrder_returnOrder() {
        Order testOrder = TestUtils.getTestOrder();
        Product testProduct = TestUtils.getTestProduct();
        User testUser = TestUtils.getTestUser();
        Integer quantity = 1;
        when(mockProductRepository.findById(anyString()))
                .thenReturn(Optional.of(testProduct));
        when(mockOrderRepository.saveAndFlush(any(Order.class)))
                .thenReturn(testOrder);
        UserServiceModel userServiceModel = modelMapper.map(testUser, UserServiceModel.class);
        OrderBindingModel orderBindingModel = modelMapper.map(testOrder, OrderBindingModel.class);

        OrderServiceModel orderServiceModel = orderService.saveOrder(orderBindingModel, userServiceModel, quantity);

        assertEquals(testOrder.getId(), orderServiceModel.getId());
    }

    @Test
    public void updateOrder_whenValidOrder_returnOrder() {
        Order testOrder = TestUtils.getTestOrder();
        when(mockOrderRepository.saveAndFlush(any(Order.class)))
                .thenReturn(testOrder);
        OrderServiceModel orderServiceModel = modelMapper.map(testOrder, OrderServiceModel.class);

        OrderServiceModel result = orderService.updateOrder(orderServiceModel);

        assertEquals(testOrder.getId(), result.getId());
    }

    @Test
    public void deleteOrder_whenDeleteOrder_void() {
        Order testOrder = TestUtils.getTestOrder();

        orderService.deleteOrder(testOrder.getId());

        verify(mockOrderRepository).deleteById(testOrder.getId());
    }

    @Test
    public void getAllOrders_when2Orders_return2Orders() {
        when(mockOrderRepository.findAll())
                .thenReturn(TestUtils.getTestOrders(2));

        List<OrderServiceModel> orderServiceModels = orderService.getAllOrders();

        assertEquals(2, orderServiceModels.size());
    }

    @Test
    public void getAllOrders_whenNoOrders_returnNoOrders() {
        when(mockOrderRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<OrderServiceModel> orderServiceModels = orderService.getAllOrders();

        assertEquals(0, orderServiceModels.size());
    }
}
