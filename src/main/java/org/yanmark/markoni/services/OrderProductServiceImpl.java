package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.OrderProduct;
import org.yanmark.markoni.domain.models.services.OrderProductServiceModel;
import org.yanmark.markoni.repositories.OrderProductRepository;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderProductServiceImpl(OrderProductRepository orderProductRepository,
                                   ModelMapper modelMapper) {
        this.orderProductRepository = orderProductRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderProductServiceModel saveOrderProduct(OrderProductServiceModel orderProductService) {
        OrderProduct orderProduct = this.modelMapper.map(orderProductService, OrderProduct.class);
        try {
            orderProduct = this.orderProductRepository.saveAndFlush(orderProduct);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(orderProduct, OrderProductServiceModel.class);
    }

    @Override
    public void deleteOrderProduct(String id) {
        try {
            this.orderProductRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
