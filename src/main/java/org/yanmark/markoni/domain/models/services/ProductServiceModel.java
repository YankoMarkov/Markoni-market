package org.yanmark.markoni.domain.models.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductServiceModel extends BaseServiceModel {

    private String name;
    private String image;
    private Double weight;
    private String description;
    private BigDecimal price;
    private int rating;
    private Integer quantity;
    private List<CommentServiceModel> comments;
    private Set<CategoryServiceModel> categories;

    public ProductServiceModel() {
        this.comments = new ArrayList<>();
        this.categories = new HashSet<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getWeight() {
        return this.weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<CommentServiceModel> getComments() {
        return this.comments;
    }

    public void setComments(List<CommentServiceModel> comments) {
        this.comments = comments;
    }

    public Set<CategoryServiceModel> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryServiceModel> categories) {
        this.categories = categories;
    }
}
