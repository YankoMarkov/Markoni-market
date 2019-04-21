package org.yanmark.markoni.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.yanmark.markoni.domain.entities.*;
import org.yanmark.markoni.domain.models.bindings.users.UserEditBindingModel;
import org.yanmark.markoni.domain.models.services.OrderServiceModel;
import org.yanmark.markoni.domain.models.services.UserRoleServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.OrderProductRepository;
import org.yanmark.markoni.repositories.OrderRepository;
import org.yanmark.markoni.repositories.ProductRepository;
import org.yanmark.markoni.repositories.UserRepository;
import org.yanmark.markoni.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository mockUserRepository;

    @Autowired
    private UserRoleService userRoleService;

    @MockBean
    private ProductRepository mockProductRepository;

    @Autowired
    private ProductService productService;

    @MockBean
    private OrderRepository mockOrderRepository;

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderProductRepository mockOrderProductRepository;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void saveUser_whenValidUser_returnUser() {
        User testUser = TestUtils.getTestUser();
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);
        UserServiceModel userServiceModel = modelMapper.map(testUser, UserServiceModel.class);

        UserServiceModel result = userService.saveUser(userServiceModel);

        assertEquals(testUser.getId(), result.getId());
    }

    @Test(expected = Exception.class)
    public void saveUser_whenNoValidUser_throwException() {
        User testUser = TestUtils.getTestUser();
        UserServiceModel userServiceModel = modelMapper.map(testUser, UserServiceModel.class);

        userService.saveUser(userServiceModel);

        verify(mockUserRepository).saveAndFlush(any(User.class));
    }

    @Test(expected = Exception.class)
    public void saveUser_whenUserIsNull_throwException() {
        userService.saveUser(null);

        verify(mockUserRepository).saveAndFlush(any(User.class));
    }

    @Test
    public void updateUserProducts_whenValidUser_returnUser() {
        User testUser = TestUtils.getTestUser();
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);
        UserServiceModel userServiceModel = modelMapper.map(testUser, UserServiceModel.class);

        UserServiceModel result = userService.updateUserProducts(userServiceModel);

        assertEquals(testUser.getId(), result.getId());
    }

    @Test(expected = Exception.class)
    public void updateUserProducts_whenNoValidUser_throwException() {
        User testUser = TestUtils.getTestUser();
        UserServiceModel userServiceModel = modelMapper.map(testUser, UserServiceModel.class);

        userService.updateUserProducts(userServiceModel);

        verify(mockUserRepository).saveAndFlush(any(User.class));
    }

    @Test
    public void updateUsersRole_whenValidUserAndUserRole_returnUser() {
        User testUser = TestUtils.getTestUser();
        UserRole testUserRole = TestUtils.getTestUserRole();
        when(mockUserRepository.findById(anyString()))
                .thenReturn(Optional.of(testUser));
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);
        UserServiceModel userServiceModel = modelMapper.map(testUser, UserServiceModel.class);
        UserRoleServiceModel userRoleServiceModel = modelMapper.map(testUserRole, UserRoleServiceModel.class);

        UserServiceModel result = userService.updateUsersRole(userServiceModel, userRoleServiceModel);

        assertEquals(testUser.getId(), result.getId());
    }

    @Test(expected = Exception.class)
    public void updateUsersRole_whenNoValidUserAndUserRole_throwException() {
        User testUser = TestUtils.getTestUser();
        UserRole testUserRole = TestUtils.getTestUserRole();
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);
        UserServiceModel userServiceModel = modelMapper.map(testUser, UserServiceModel.class);
        UserRoleServiceModel userRoleServiceModel = modelMapper.map(testUserRole, UserRoleServiceModel.class);

        userService.updateUsersRole(userServiceModel, userRoleServiceModel);

        verify(mockUserRepository).findById(anyString());
    }

    @Test
    public void updateUser_whenValidUser_returnUser() {
        User testUser = TestUtils.getTestUser();
        UserEditBindingModel editTestUser = TestUtils.getTestEditUser();
        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(testUser));
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);
        when(passwordEncoder.matches(editTestUser.getPassword(), testUser.getPassword()))
                .thenReturn(true);
        UserServiceModel userServiceModel = modelMapper.map(testUser, UserServiceModel.class);

        UserServiceModel result = userService.updateUser(userServiceModel, editTestUser);

        assertEquals(testUser.getId(), result.getId());
    }

    @Test
    public void getUserById_whenValidUserId_returnUser() {
        User testUser = TestUtils.getTestUser();
        when(mockUserRepository.findById(anyString()))
                .thenReturn(Optional.of(testUser));

        UserServiceModel result = userService.getUserById(testUser.getId());

        assertEquals(testUser.getId(), result.getId());
    }

    @Test(expected = Exception.class)
    public void getUserById_whenNoValidUserId_throwException() {
        User testUser = TestUtils.getTestUser();

        userService.getUserById(testUser.getId());

        verify(mockUserRepository).findById(anyString());
    }

    @Test
    public void getUserByUsername_whenValidUserName_returnUser() {
        User testUser = TestUtils.getTestUser();
        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(testUser));

        UserServiceModel result = userService.getUserByUsername(testUser.getUsername());

        assertEquals(testUser.getId(), result.getId());
    }

    @Test(expected = Exception.class)
    public void getUserByUsername_whenNoValidUserName_throwExceptionr() {
        User testUser = TestUtils.getTestUser();

        userService.getUserByUsername(testUser.getUsername());

        verify(mockUserRepository).findByUsername(anyString());
    }

    @Test
    public void getAllUsers_when2Users_return2Users() {
        List<User> testUsers = TestUtils.getTestUsers(2);
        when(mockUserRepository.findAllOrderByUsername())
                .thenReturn(testUsers);

        List<UserServiceModel> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    public void getAllUsers_whenNoUsers_returnNoUsers() {
        when(mockUserRepository.findAllOrderByUsername())
                .thenReturn(new ArrayList<>());

        List<UserServiceModel> result = userService.getAllUsers();

        assertEquals(0, result.size());
    }

    @Test
    public void buyOrder_whenValidOrderAndUser_returnUser() {
        User testUser = TestUtils.getTestUser();
        Order testOrder = TestUtils.getTestOrder();
        OrderProduct testOrderProduct = TestUtils.getTestOrderProduct();
        Product testProduct = TestUtils.getTestProduct();
        testOrder.setProduct(testProduct);
        testOrder.setCustomer(testUser);
        when(mockOrderRepository.saveAndFlush(any(Order.class)))
                .thenReturn(testOrder);
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);
        when(mockProductRepository.findById(anyString()))
                .thenReturn(Optional.of(testProduct));
        when(mockProductRepository.saveAndFlush(any(Product.class)))
                .thenReturn(testProduct);
        when(mockOrderProductRepository.saveAndFlush(any(OrderProduct.class)))
                .thenReturn(testOrderProduct);
        UserServiceModel userServiceModel = modelMapper.map(testUser, UserServiceModel.class);
        OrderServiceModel orderServiceModel = modelMapper.map(testOrder, OrderServiceModel.class);

        UserServiceModel result = userService.buyOrder(orderServiceModel, userServiceModel);

        verify(mockOrderRepository).deleteById(testOrder.getId());
        assertEquals(testUser.getId(), result.getId());
    }
}
