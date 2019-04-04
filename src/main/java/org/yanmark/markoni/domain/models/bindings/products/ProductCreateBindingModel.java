package org.yanmark.markoni.domain.models.bindings.products;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

public class ProductCreateBindingModel {

    private String name;
    private MultipartFile image;
    private double weight;
    private String description;
    private BigDecimal price;
    private Set<String> categories;

    @NotNull(message = "Name cannot be null.")
    @Size(min = 3, max = 30, message = "Name must be in range [3 - 30] symbols.")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Image cannot be null.")
    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    @NotNull(message = "Weight cannot be null.")
    @DecimalMin(value = "0.1", message = "Weight must be greater than 0")
    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @NotNull(message = "Description cannot be null.")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull(message = "Price cannot be null.")
    @DecimalMin(value = "0.001", message = "Price must be greater than 0")
    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
