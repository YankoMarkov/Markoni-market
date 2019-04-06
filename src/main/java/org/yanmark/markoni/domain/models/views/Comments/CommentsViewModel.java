package org.yanmark.markoni.domain.models.views.Comments;

import java.time.LocalDateTime;

public class CommentsViewModel {

    private String user;
    private LocalDateTime time;
    private String comment;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
