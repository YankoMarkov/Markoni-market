package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.services.OrderServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.domain.models.views.orders.OrderViewModel;
import org.yanmark.markoni.services.OrderService;
import org.yanmark.markoni.services.ProductService;
import org.yanmark.markoni.services.UserService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService,
                           ProductService productService,
                           UserService userService,
                           ModelMapper modelMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView add(@PathVariable String id,
                            Principal principal) {
        ProductServiceModel productServiceModel = this.productService.getProductById(id);
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        OrderServiceModel orderServiceModel = new OrderServiceModel();
        this.orderService.saveOrder(orderServiceModel, productServiceModel, userServiceModel);
        return this.redirect("/orders/my");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView all(ModelAndView modelAndView) {
        List<OrderServiceModel> orderServiceModels = this.orderService.getAllOrders();
        List<OrderViewModel> orderViewModels = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (!orderServiceModels.isEmpty()) {
            orderViewModels = orderServiceModels.stream()
                    .map(order -> {
                        OrderViewModel orderViewModel = this.modelMapper.map(order, OrderViewModel.class);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                        String date = order.getOrderedOn().format(formatter);
                        orderViewModel.setOrderedOn(date);
                        orderViewModel.setImage(order.getProduct().getImage());
                        orderViewModel.setProduct(order.getProduct().getName());
                        orderViewModel.setPrice(order.getProduct().getPrice());
                        return orderViewModel;
                    })
                    .collect(Collectors.toList());
            for (OrderServiceModel orderServiceModel : orderServiceModels) {
                totalPrice = totalPrice.add(orderServiceModel.getProduct().getPrice());
            }
        }
        modelAndView.addObject("role", "ADMIN");
        modelAndView.addObject("orders", orderViewModels);
        modelAndView.addObject("totalPrice", totalPrice);
        return this.view("/orders/all-orders", modelAndView);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView myOrders(Principal principal, ModelAndView modelAndView) {
        List<OrderServiceModel> orderServiceModels = this.orderService.getAllOrdersByCustomer(principal.getName());
        List<OrderViewModel> orderViewModels = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (!orderServiceModels.isEmpty()) {
            orderViewModels = orderServiceModels.stream()
                    .map(order -> {
                        OrderViewModel orderViewModel = this.modelMapper.map(order, OrderViewModel.class);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                        String date = order.getOrderedOn().format(formatter);
                        orderViewModel.setOrderedOn(date);
                        orderViewModel.setImage(order.getProduct().getImage());
                        orderViewModel.setProduct(order.getProduct().getName());
                        orderViewModel.setPrice(order.getProduct().getPrice());
                        return orderViewModel;
                    })
                    .collect(Collectors.toList());
            for (OrderServiceModel orderServiceModel : orderServiceModels) {
                totalPrice = totalPrice.add(orderServiceModel.getProduct().getPrice());
            }
        }
        modelAndView.addObject("role", "USER");
        modelAndView.addObject("orders", orderViewModels);
        modelAndView.addObject("totalPrice", totalPrice);
        return this.view("/orders/all-orders", modelAndView);
    }
}
