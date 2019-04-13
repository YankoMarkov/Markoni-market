package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.bindings.orders.OrderBindingModel;
import org.yanmark.markoni.domain.models.services.OrderServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.domain.models.views.orders.OrderProductViewModel;
import org.yanmark.markoni.domain.models.views.orders.OrderViewModel;
import org.yanmark.markoni.services.OrderService;
import org.yanmark.markoni.services.ProductService;
import org.yanmark.markoni.services.UserService;
import org.yanmark.markoni.web.annotations.PageTitle;

import javax.validation.Valid;
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

    @GetMapping("/order/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("\uD835\uDCAA\uD835\uDCC7\uD835\uDCB9\uD835\uDC52\uD835\uDCC7")
    public ModelAndView order(@PathVariable String id,
                              @ModelAttribute("productOrder") OrderBindingModel productOrder,
                              ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.getProductById(id);
        OrderProductViewModel orderProductViewModel = this.modelMapper.map(productServiceModel, OrderProductViewModel.class);
        modelAndView.addObject("product", orderProductViewModel);
        return this.view("/orders/order-product", modelAndView);
    }

    @PostMapping("/order")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView orderConfirm(@Valid @ModelAttribute("productOrder") OrderBindingModel productOrder,
                                     Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view("/orders/order-product");
        }
        ProductServiceModel productServiceModel = this.productService.getProductById(productOrder.getId());
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        OrderServiceModel orderServiceModel = new OrderServiceModel();
        this.orderService.saveOrder(productServiceModel,
                orderServiceModel,
                userServiceModel,
                productOrder.getQuantity());
        return this.redirect("/orders/my");
    }

    @GetMapping("/buy/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView buyConfirm(@PathVariable String id, Principal principal) {
        OrderServiceModel orderServiceModel = this.orderService.getOrderById(id);
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        this.userService.buyOrder(orderServiceModel, userServiceModel);
        return this.redirect("/home");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView delete(@PathVariable String id) {
        this.orderService.deleteOrder(id);
        return this.redirect("/orders/my");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PageTitle("\uD835\uDC9C\uD835\uDCC1\uD835\uDCC1 \uD835\uDCAA\uD835\uDCC7\uD835\uDCB9\uD835\uDC52\uD835\uDCC7\uD835\uDCC8")
    public ModelAndView all(ModelAndView modelAndView) {
        List<OrderServiceModel> orderServiceModels = this.orderService.getAllOrders();
        List<OrderViewModel> orderViewModels = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (!orderServiceModels.isEmpty()) {
            orderViewModels = orderServiceModels.stream()
                    .map(order -> {
                        OrderViewModel orderViewModel = new OrderViewModel();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                        String date = order.getOrderedOn().format(formatter);
                        orderViewModel.setOrderedOn(date);
                        orderViewModel.setId(order.getId());
                        orderViewModel.setImage(order.getProduct().getImage());
                        orderViewModel.setProduct(order.getProduct().getName());
                        orderViewModel.setPrice(order.getPrice());
                        return orderViewModel;
                    })
                    .collect(Collectors.toList());
            for (OrderViewModel orderViewModel : orderViewModels) {
                totalPrice = totalPrice.add(orderViewModel.getPrice());
            }
        }
        modelAndView.addObject("role", "ADMIN");
        modelAndView.addObject("orders", orderViewModels);
        modelAndView.addObject("totalPrice", totalPrice);
        return this.view("/orders/all-orders", modelAndView);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("\uD835\uDC40\uD835\uDCCE \uD835\uDCAA\uD835\uDCC7\uD835\uDCB9\uD835\uDC52\uD835\uDCC7\uD835\uDCC8")
    public ModelAndView myOrders(Principal principal, ModelAndView modelAndView) {
        List<OrderServiceModel> orderServiceModels = this.orderService.getAllOrdersByCustomer(principal.getName());
        List<OrderViewModel> orderViewModels = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (!orderServiceModels.isEmpty()) {
            orderViewModels = orderServiceModels.stream()
                    .map(order -> {
                        OrderViewModel orderViewModel = new OrderViewModel();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                        String date = order.getOrderedOn().format(formatter);
                        orderViewModel.setOrderedOn(date);
                        orderViewModel.setId(order.getId());
                        orderViewModel.setImage(order.getProduct().getImage());
                        orderViewModel.setProduct(order.getProduct().getName());
                        orderViewModel.setPrice(order.getPrice());
                        return orderViewModel;
                    })
                    .collect(Collectors.toList());
            for (OrderViewModel orderViewModel : orderViewModels) {
                totalPrice = totalPrice.add(orderViewModel.getPrice());
            }
        }
        modelAndView.addObject("role", "USER");
        modelAndView.addObject("orders", orderViewModels);
        modelAndView.addObject("totalPrice", totalPrice);
        return this.view("/orders/all-orders", modelAndView);
    }
}
