package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.bindings.comments.CommentCreateBindingModel;
import org.yanmark.markoni.domain.models.bindings.comments.CommentEditBindingModel;
import org.yanmark.markoni.domain.models.services.CommentServiceModel;
import org.yanmark.markoni.domain.models.views.comments.CommentsViewModel;
import org.yanmark.markoni.domain.models.views.comments.CommetnAllViewModel;
import org.yanmark.markoni.services.CommentService;
import org.yanmark.markoni.web.annotations.PageTitle;

import javax.validation.Valid;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comments")
public class CommentController extends BaseController {

    private static final String PRODUCTS_DETAILS = "/products/details/";
    private static final String COMMENTS_ALL_COMMENTS = "/comments/all-comments";
    private static final String COMMENTS_EDIT_COMMENT = "/comments/edit-comment";
    private static final String COMMENTS_ALL = "/comments/all";
    private static final String COMMENTS_DELETE_COMMENT = "/comments/delete-comment";

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
            return this.redirect(PRODUCTS_DETAILS + productId);
        }
        CommentServiceModel commentServiceModel = this.modelMapper.map(commentCreate, CommentServiceModel.class);
        this.commentService.saveComment(commentServiceModel, commentCreate, principal, productId);
        return this.redirect(PRODUCTS_DETAILS + productId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PageTitle("\uD835\uDC9C\uD835\uDCC1\uD835\uDCC1 \uD835\uDC9E\uD835\uDC5C\uD835\uDCC2\uD835\uDCC2\uD835\uDC52\uD835\uDCC3\uD835\uDCC9\uD835\uDCC8")
    public ModelAndView all(ModelAndView modelAndView) {
        List<CommetnAllViewModel> commetnAllViewModels = this.commentService.getAllComments().stream()
                .map(comment -> {
                    CommetnAllViewModel commetnAllViewModel = this.modelMapper.map(comment, CommetnAllViewModel.class);
                    commetnAllViewModel.setUser(comment.getUser().getUsername());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
                    String date = comment.getTime().format(formatter);
                    commetnAllViewModel.setTime(date);
                    commetnAllViewModel.setProduct(comment.getProduct().getId());
                    return commetnAllViewModel;
                })
                .collect(Collectors.toList());
        modelAndView.addObject("comments", commetnAllViewModels);
        return this.view(COMMENTS_ALL_COMMENTS, modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PageTitle("\uD835\uDC9E\uD835\uDC5C\uD835\uDCC2\uD835\uDCC2\uD835\uDC52\uD835\uDCC3\uD835\uDCC9 \uD835\uDC38\uD835\uDCB9\uD835\uDCBE\uD835\uDCC9")
    public ModelAndView edit(@PathVariable String id,
                             @ModelAttribute("commentEdit") CommentEditBindingModel commentEdit,
                             ModelAndView modelAndView) {
        CommentServiceModel commentServiceModel = this.commentService.getCommentById(id);
        CommetnAllViewModel commentViewModel = this.modelMapper.map(commentServiceModel, CommetnAllViewModel.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        String date = commentServiceModel.getTime().format(formatter);
        commentViewModel.setTime(date);
        commentViewModel.setUser(commentServiceModel.getUser().getUsername());
        commentViewModel.setProduct(commentServiceModel.getProduct().getId());
        modelAndView.addObject("comment", commentViewModel);
        return this.view(COMMENTS_EDIT_COMMENT, modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView editConfirm(@PathVariable String id,
                                    @Valid @ModelAttribute("commentEdit") CommentEditBindingModel commentEdit,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view(COMMENTS_EDIT_COMMENT);
        }
        CommentServiceModel commentServiceModel = this.modelMapper.map(commentEdit, CommentServiceModel.class);
        this.commentService.updateComment(commentServiceModel, commentEdit);
        return this.redirect(COMMENTS_ALL);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PageTitle("\uD835\uDC9E\uD835\uDC5C\uD835\uDCC2\uD835\uDCC2\uD835\uDC52\uD835\uDCC3\uD835\uDCC9 \uD835\uDC9F\uD835\uDC52\uD835\uDCC1\uD835\uDC52\uD835\uDCC9\uD835\uDC52")
    public ModelAndView delete(@PathVariable String id,
                               ModelAndView modelAndView) {
        CommentServiceModel commentServiceModel = this.commentService.getCommentById(id);
        CommetnAllViewModel commentViewModel = this.modelMapper.map(commentServiceModel, CommetnAllViewModel.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        String date = commentServiceModel.getTime().format(formatter);
        commentViewModel.setTime(date);
        commentViewModel.setUser(commentServiceModel.getUser().getUsername());
        commentViewModel.setProduct(commentServiceModel.getProduct().getId());
        modelAndView.addObject("comment", commentViewModel);
        return this.view(COMMENTS_DELETE_COMMENT, modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id) {
        this.commentService.deleteComment(id);
        return this.redirect(COMMENTS_ALL);
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
