package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.models.services.OrderProductServiceModel;

public interface OrderProductService {

    OrderProductServiceModel saveOrderProduct(OrderProductServiceModel orderProductService);

    void deleteOrderProduct(String id);
}
