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
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.domain.models.views.Comments.CommentsViewModel;
import org.yanmark.markoni.services.CommentService;
import org.yanmark.markoni.services.ProductService;
import org.yanmark.markoni.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comments")
public class CommentController extends BaseController {

    private final CommentService commentService;
    private final ProductService productService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentController(CommentService commentService,
                             ProductService productService,
                             UserService userService,
                             ModelMapper modelMapper) {
        this.commentService = commentService;
        this.productService = productService;
        this.userService = userService;
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
        ProductServiceModel productServiceModel = this.productService.getProductById(productId);
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        CommentServiceModel commentServiceModel = this.modelMapper.map(commentCreate, CommentServiceModel.class);
        this.commentService.saveComment(commentServiceModel, commentCreate, userServiceModel, productServiceModel);
        return this.redirect("/products/details/" + productId);
    }

    @GetMapping("/fetch")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public List<CommentsViewModel> fetch() {
        return this.commentService.getAllComments().stream()
                .map(comment -> this.modelMapper.map(comment, CommentsViewModel.class))
                .collect(Collectors.toList());
    }
}
