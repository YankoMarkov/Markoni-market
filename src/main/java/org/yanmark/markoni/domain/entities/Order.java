package org.yanmark.markoni.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity(name = "orders")
public class Order extends BaseEntity {

    private Product product;
    private User customer;
    private LocalDate orderedOn;
    private Integer quantity;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    @Column(name = "ordered_on", nullable = false)
    public LocalDate getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(LocalDate orderedOn) {
        this.orderedOn = orderedOn;
    }

    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
