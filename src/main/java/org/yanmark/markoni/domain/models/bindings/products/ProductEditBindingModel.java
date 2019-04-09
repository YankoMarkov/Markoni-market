package org.yanmark.markoni.domain.models.bindings.products;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductEditBindingModel {

    private String name;
    private Double weight;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Set<String> categories;

    public ProductEditBindingModel() {
        this.categories = new HashSet<>();
    }

    @NotNull(message = "Name cannot be null.")
    @Size(min = 3, max = 30, message = "Name must be in range [3 - 30] symbols.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Weight cannot be null.")
    @DecimalMin(value = "0.1", message = "Weight must be greater than 0")
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @NotNull(message = "Description cannot be null.")
    @NotBlank(message = "Description cannot be empty.")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull(message = "Price cannot be null.")
    @DecimalMin(value = "0.001", message = "Price must be greater than 0")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @NotNull(message = "Quantity cannot be null.")
    @Min(1)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @NotNull(message = "Category cannot be null.")
    @NotEmpty(message = "Category cannot be empty.")
    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }
}
