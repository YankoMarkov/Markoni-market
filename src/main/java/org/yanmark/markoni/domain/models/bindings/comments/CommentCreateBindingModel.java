package org.yanmark.markoni.domain.models.bindings.comments;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentCreateBindingModel {

    private int rating;
    private String comment;

    @NotNull(message = "Rating cannot be null.")
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @NotNull(message = "Comment cannot be null.")
    @NotBlank(message = "Comment cannot be empty.")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
