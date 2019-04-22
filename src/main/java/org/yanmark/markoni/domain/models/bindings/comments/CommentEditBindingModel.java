package org.yanmark.markoni.domain.models.bindings.comments;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentEditBindingModel {

    private String id;
    private String user;
    private String time;
    private String comment;
    private String product;

    @NotNull(message = "Comment id cannot be null.")
    @NotBlank(message = "Comment id cannot be empty.")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull(message = "User cannot be null.")
    @NotBlank(message = "User cannot be empty.")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @NotNull(message = "Time cannot be null.")
    @NotBlank(message = "Time cannot be empty.")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @NotNull(message = "Comment cannot be null.")
    @NotBlank(message = "Comment cannot be empty.")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @NotNull(message = "Product cannot be null.")
    @NotBlank(message = "Product cannot be empty.")
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
