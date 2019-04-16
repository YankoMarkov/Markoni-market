package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.bindings.comments.CommentCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CommentServiceModel;
import org.yanmark.markoni.domain.models.views.comments.CommentsViewModel;
import org.yanmark.markoni.services.CommentService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comments")
public class CommentController extends BaseController {

    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentController(CommentService commentService,
                             ModelMapper modelMapper) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addConfirm(@Valid @ModelAttribute("commentCreate") CommentCreateBindingModel commentCreate,
                                   @RequestParam String productId,
                                   BindingResult bindingResult,
                                   Principal principal) {
        if (bindingResult.hasErrors()) {
            return this.redirect("/products/details/" + productId);
        }
        CommentServiceModel commentServiceModel = this.modelMapper.map(commentCreate, CommentServiceModel.class);
        this.commentService.saveComment(commentServiceModel, commentCreate, principal, productId);
        return this.redirect("/products/details/" + productId);
    }

    @GetMapping("/fetch/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public List<CommentsViewModel> fetch(@PathVariable String id) {
        return this.commentService.getAllCommentsByProduct(id).stream()
                .map(comment -> {
                    CommentsViewModel commentsViewModel = this.modelMapper.map(comment, CommentsViewModel.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
                    String date = comment.getTime().format(formatter);
                    commentsViewModel.setTime(date);
                    commentsViewModel.setUser(comment.getUser().getUsername());
                    return commentsViewModel;
                })
                .collect(Collectors.toList());
    }
}
