package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Comment;
import org.yanmark.markoni.domain.models.bindings.comments.CommentCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CommentServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.CommentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              ProductService productService,
                              ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentServiceModel saveComment(CommentServiceModel commentService,
                                           CommentCreateBindingModel commentCreate,
                                           UserServiceModel userService,
                                           ProductServiceModel productService) {
        commentService.setTime(LocalDateTime.now());
        commentService.setUser(userService);
        productService.setRating(productService.getRating() + commentCreate.getRating());
        this.productService.editProduct(productService);
        commentService.setProduct(productService);
        Comment comment = this.modelMapper.map(commentService, Comment.class);
        try {
            comment = this.commentRepository.saveAndFlush(comment);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(comment, CommentServiceModel.class);
    }

    @Override
    public List<CommentServiceModel> getAllComments() {
        List<Comment> comments = this.commentRepository.findAllByOrderByTimeDesc();
        if (comments == null) {
            throw new IllegalArgumentException("comments was not found!");
        }
        return comments.stream()
                .map(comment -> this.modelMapper.map(comment, CommentServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
