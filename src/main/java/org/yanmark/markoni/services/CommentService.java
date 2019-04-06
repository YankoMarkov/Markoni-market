package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.models.bindings.comments.CommentCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CommentServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;

import java.util.List;

public interface CommentService {

    CommentServiceModel saveComment(CommentServiceModel commentService,
                                    CommentCreateBindingModel commentCreate,
                                    UserServiceModel userService,
                                    ProductServiceModel productService);

    List<CommentServiceModel> getAllComments();
}
