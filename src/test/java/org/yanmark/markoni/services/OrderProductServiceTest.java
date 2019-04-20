package org.yanmark.markoni.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.yanmark.markoni.domain.entities.OrderProduct;
import org.yanmark.markoni.domain.models.services.OrderProductServiceModel;
import org.yanmark.markoni.repositories.OrderProductRepository;
import org.yanmark.markoni.utils.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderProductServiceTest {

    @Mock
    private OrderProductRepository mockOrderProductRepository;

    private OrderProductService orderProductService;

    private ModelMapper modelMapper;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        orderProductService = new OrderProductServiceImpl(mockOrderProductRepository, modelMapper);
    }

    @Test
    public void saveOrderProduct_whenValidOrderProduct_returnOrderProduct() {
        OrderProduct testOrderProduct = TestUtils.getTestOrderProduct();
        when(mockOrderProductRepository.saveAndFlush(any(OrderProduct.class)))
                .thenReturn(testOrderProduct);
        OrderProductServiceModel orderProductServiceModel = modelMapper.map(testOrderProduct, OrderProductServiceModel.class);

        OrderProductServiceModel result = orderProductService.saveOrderProduct(orderProductServiceModel);

        assertEquals(orderProductServiceModel.getId(), result.getId());
    }

    @Test(expected = Exception.class)
    public void saveOrderProduct_whenOrderProductIsNull_throwException() {
        orderProductService.saveOrderProduct(null);

        verify(mockOrderProductRepository).saveAndFlush(any(OrderProduct.class));
    }

    @Test
    public void deleteOrderProduct_whenDeleteOrderProduct_void() {
        OrderProduct testOrderProduct = TestUtils.getTestOrderProduct();

        orderProductService.deleteOrderProduct(testOrderProduct.getId());

        verify(mockOrderProductRepository).deleteById(testOrderProduct.getId());
    }
}
