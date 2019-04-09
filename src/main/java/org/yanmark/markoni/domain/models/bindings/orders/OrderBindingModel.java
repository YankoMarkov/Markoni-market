package org.yanmark.markoni.domain.models.bindings.orders;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OrderBindingModel {

    private String id;
    private Integer quantity;

    @NotNull(message = "Product id cannot be null.")
    @NotBlank(message = "Product id cannot be empty.")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 1, message = "Quantity must be minimum 1.")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
