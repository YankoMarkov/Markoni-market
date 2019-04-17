package org.yanmark.markoni.unitTest.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.yanmark.markoni.domain.entities.Comment;
import org.yanmark.markoni.domain.entities.Product;
import org.yanmark.markoni.domain.entities.User;
import org.yanmark.markoni.domain.models.bindings.comments.CommentCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CommentServiceModel;
import org.yanmark.markoni.repositories.CommentRepository;
import org.yanmark.markoni.repositories.ProductRepository;
import org.yanmark.markoni.repositories.UserRepository;
import org.yanmark.markoni.services.CommentService;
import org.yanmark.markoni.utils.TestUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {

    @MockBean
    private CommentRepository mockCommentRepository;

    @Autowired
    private CommentService commentService;

    @MockBean
    private Principal mockPrincipal;

    @MockBean
    private UserRepository mockUserRepository;

    @MockBean
    private ProductRepository mockProductRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void saveComment_whenCommentSave_returnComment() {
        Comment testComment = TestUtils.getTestComment();
        Product testProduct = TestUtils.getTestProduct();
        User testUser = TestUtils.getTestUser();
        when(mockProductRepository.findById(anyString()))
                .thenReturn(Optional.of(testProduct));
        when(mockProductRepository.saveAndFlush(any(Product.class)))
                .thenReturn(testProduct);
        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(testUser));
        when(mockPrincipal.getName())
                .thenReturn(testUser.getUsername());
        when(mockCommentRepository.saveAndFlush(any(Comment.class)))
                .thenReturn(testComment);

        CommentServiceModel commentServiceModel = modelMapper.map(testComment, CommentServiceModel.class);
        CommentCreateBindingModel commentCreateBindingModel = modelMapper.map(testComment, CommentCreateBindingModel.class);

        CommentServiceModel result = commentService.saveComment(commentServiceModel, commentCreateBindingModel, mockPrincipal, testProduct.getId());

        assertEquals(testComment.getId(), result.getId());
    }

    @Test
    public void getAllCommentsByProduct_when2Comments_return2Comments() {
        when(mockCommentRepository.findAllCommentsByProduct_IdOrderByTimeDesc(anyString()))
                .thenReturn(TestUtils.getTestComments(2));

        List<CommentServiceModel> commentServiceModels = commentService.getAllCommentsByProduct(anyString());

        assertEquals(2, commentServiceModels.size());
    }

    @Test
    public void getAllCommentsByProduct_whenNoComments_returnNoComments() {
        when(mockCommentRepository.findAllCommentsByProduct_IdOrderByTimeDesc(anyString()))
                .thenReturn(new ArrayList<>());

        List<CommentServiceModel> commentServiceModels = commentService.getAllCommentsByProduct(anyString());

        assertEquals(0, commentServiceModels.size());
    }
}
