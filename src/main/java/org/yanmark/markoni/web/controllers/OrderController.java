package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.views.orders.OrderViewModel;
import org.yanmark.markoni.services.OrderService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView all(ModelAndView modelAndView) {
        List<OrderViewModel> orderViewModels = this.orderService.getAllOrders().stream()
                .map(order -> {
                    OrderViewModel orderViewModel = this.modelMapper.map(order, OrderViewModel.class);
                    List<String> products = order.getProducts().stream()
                            .map(ProductServiceModel::getName)
                            .collect(Collectors.toList());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
                    String date = order.getOrderedOn().format(formatter);
                    orderViewModel.setOrderedOn(date);
                    orderViewModel.setCustomer(order.getCustomer().getUsername());
                    orderViewModel.setProducts(products);
                    return orderViewModel;
                })
                .collect(Collectors.toList());
        modelAndView.addObject("orders", orderViewModels);
        return this.view("/orders/all-orders", modelAndView);
    }
}
