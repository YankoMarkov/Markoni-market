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
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentServiceModel saveComment(CommentServiceModel commentService,
                                           CommentCreateBindingModel commentCreate,
                                           UserServiceModel userService,
                                           ProductServiceModel productService) {
        commentService.setTime(LocalDateTime.now());
        commentService.setUser(userService);
        commentService.setProduct(productService);
        Comment comment = this.modelMapper.map(commentService, Comment.class);
        try {
            comment = this.commentRepository.saveAndFlush(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return this.modelMapper.map(comment, CommentServiceModel.class);
    }

    @Override
    public List<CommentServiceModel> getAllComments() {
        List<Comment> comments = this.commentRepository.findAllByOrderByTimeAsc();
        if (comments == null) {
            return new ArrayList<>();
        }
        return comments.stream()
                .map(comment -> this.modelMapper.map(comment, CommentServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
