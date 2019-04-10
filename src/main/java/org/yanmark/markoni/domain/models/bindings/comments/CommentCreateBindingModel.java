package org.yanmark.markoni.domain.models.bindings.comments;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentCreateBindingModel {

    private int rating;
    private String comment;

    @NotNull(message = "Rating cannot be null.")
    @Min(value = 1, message = "Rating must be minimum 1.")
    @Max(value = 5, message = "Rating must be maximum 1.")
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
