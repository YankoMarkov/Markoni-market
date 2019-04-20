package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Comment;
import org.yanmark.markoni.domain.models.bindings.comments.CommentCreateBindingModel;
import org.yanmark.markoni.domain.models.bindings.comments.CommentEditBindingModel;
import org.yanmark.markoni.domain.models.services.CommentServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.CommentRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              UserService userService,
                              ProductService productService,
                              ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentServiceModel saveComment(CommentServiceModel commentService,
                                           CommentCreateBindingModel commentCreate,
                                           Principal principal,
                                           String productId) {
        ProductServiceModel productServiceModel = this.productService.getProductById(productId);
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        commentService.setTime(LocalDateTime.now());
        commentService.setUser(userServiceModel);
        productServiceModel.setRating(productServiceModel.getRating() + commentCreate.getRating());
        productServiceModel = this.productService.editProduct(productServiceModel, productId);
        commentService.setProduct(productServiceModel);
        Comment comment = this.modelMapper.map(commentService, Comment.class);
        try {
            comment = this.commentRepository.saveAndFlush(comment);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(comment, CommentServiceModel.class);
    }

    @Override
    public CommentServiceModel updateComment(CommentServiceModel commentService,
                                             CommentEditBindingModel commentEdit) {
        UserServiceModel userServiceModel = this.userService.getUserByUsername(commentEdit.getUser());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        LocalDateTime time = LocalDateTime.parse(commentEdit.getTime(), formatter);
        commentService.setTime(time);
        commentService.setUser(userServiceModel);
        Comment comment = this.modelMapper.map(commentService, Comment.class);
        try {
            comment = this.commentRepository.saveAndFlush(comment);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(comment, CommentServiceModel.class);
    }

    @Override
    public void deleteComment(String id) {
        try {
            this.commentRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public CommentServiceModel getCommentById(String id) {
        Comment comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment was not fund!"));
        return this.modelMapper.map(comment, CommentServiceModel.class);
    }

    @Override
    public List<CommentServiceModel> getAllCommentsByProduct(String productId) {
        return this.commentRepository.findAllCommentsByProduct_IdOrderByTimeDesc(productId).stream()
                .map(comment -> this.modelMapper.map(comment, CommentServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<CommentServiceModel> getAllComments() {
        return this.commentRepository.findAllByOrderByTimeDesc().stream()
                .map(comment -> this.modelMapper.map(comment, CommentServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Scheduled(fixedRate = 43200000)
    private void deleteExpiredComments() {
        List<Comment> comments = this.commentRepository.findAll();
        for (Comment comment : comments) {
            if (LocalDateTime.now().isAfter(comment.getTime().plusMonths(1))) {
                this.commentRepository.deleteById(comment.getId());
            }
        }
    }
}
