package org.yanmark.markoni.unitTest.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.yanmark.markoni.domain.entities.Comment;
import org.yanmark.markoni.domain.models.bindings.comments.CommentCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CommentServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.CommentRepository;
import org.yanmark.markoni.repositories.ProductRepository;
import org.yanmark.markoni.services.*;
import org.yanmark.markoni.utils.TestUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {

    @Mock
    private ProductRepository mockProductRepository;

    @Mock
    private CategoryService mockCategoryService;

    @Mock
    private CloudinaryService mockCloudinaryService;

    private ProductService productService;

    @Mock
    private UserService mockUserService;

    @Mock
    private CommentRepository mockCommentRepository;

    @Mock
    private Principal principal;

    private CommentService commentService;

    private ModelMapper modelMapper;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        productService = new ProductServiceImpl(mockProductRepository, mockCategoryService, mockCloudinaryService, modelMapper);
        commentService = new CommentServiceImpl(mockCommentRepository, mockUserService, productService, modelMapper);
    }

//    @Test
//    public void saveComment_whenCommentSave_returnComment() {
//        Comment testcomment = TestUtils.getTestComment();
//        String productId = testcomment.getProduct().getId();
//        when(principal.getName())
//                .thenReturn("testUser");
//        when(mockProductRepository.findById(anyString()))
//                .thenReturn(any());
//        ProductServiceModel productServiceModel = this.productService.getProductById(productId);
//        UserServiceModel userServiceModel = this.mockUserService.getUserByUsername(principal.getName());
//        CommentServiceModel commentService1 = new CommentServiceModel();
//        CommentCreateBindingModel commentCreate = new CommentCreateBindingModel();
//
//        CommentServiceModel commentServiceModel = commentService.saveComment(commentService1, commentCreate, principal, productId);
//    }

    @Test
    public void getAllCommentsByProduct_when2Comments_return2Comments() {
        List<Comment> testComments = TestUtils.getTestComments(2);
        when(mockCommentRepository.findAllCommentsByProduct_IdOrderByTimeDesc(anyString()))
                .thenReturn(testComments);

        List<CommentServiceModel> commentServiceModels = commentService.getAllCommentsByProduct(anyString());

        assertEquals(2, commentServiceModels.size());
    }

    @Test
    public void getAllCommentsByProduct_whenNoComments_returnNoComments() {
        List<Comment> testComments = TestUtils.getTestComments(2);
        when(mockCommentRepository.findAllCommentsByProduct_IdOrderByTimeDesc(anyString()))
                .thenReturn(new ArrayList<>());

        List<CommentServiceModel> commentServiceModels = commentService.getAllCommentsByProduct(anyString());

        assertEquals(0, commentServiceModels.size());
    }
}
