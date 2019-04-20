package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.models.bindings.comments.CommentCreateBindingModel;
import org.yanmark.markoni.domain.models.bindings.comments.CommentEditBindingModel;
import org.yanmark.markoni.domain.models.services.CommentServiceModel;

import java.security.Principal;
import java.util.List;

public interface CommentService {

    CommentServiceModel saveComment(CommentServiceModel commentService,
                                    CommentCreateBindingModel commentCreate,
                                    Principal principal,
                                    String productId);

    CommentServiceModel updateComment(CommentServiceModel commentService, CommentEditBindingModel commentEdit);

    void deleteComment(String id);

    CommentServiceModel getCommentById(String id);

    List<CommentServiceModel> getAllCommentsByProduct(String productId);

    List<CommentServiceModel> getAllComments();
}
