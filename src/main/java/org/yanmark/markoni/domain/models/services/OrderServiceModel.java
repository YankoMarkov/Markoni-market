package org.yanmark.markoni.domain.models.services;

import java.time.LocalDate;

public class OrderServiceModel extends BaseServiceModel {

    private ProductServiceModel product;
    private UserServiceModel customer;
    private LocalDate orderedOn;
    private Integer quantity;

    public ProductServiceModel getProduct() {
        return product;
    }

    public void setProduct(ProductServiceModel product) {
        this.product = product;
    }

    public UserServiceModel getCustomer() {
        return customer;
    }

    public void setCustomer(UserServiceModel customer) {
        this.customer = customer;
    }

    public LocalDate getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(LocalDate orderedOn) {
        this.orderedOn = orderedOn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
