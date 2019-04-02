package org.yanmark.markoni.domain.models.services;

import java.time.LocalDateTime;

public class CommentServiceModel extends BaseServiceModel {

    private UserServiceModel user;
    private LocalDateTime time;
    private String comment;
    private ProductServiceModel product;

    public UserServiceModel getUser() {
        return this.user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ProductServiceModel getProduct() {
        return this.product;
    }

    public void setProduct(ProductServiceModel product) {
        this.product = product;
    }
}
