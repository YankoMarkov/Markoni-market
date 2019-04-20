package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.User;
import org.yanmark.markoni.domain.entities.UserRole;
import org.yanmark.markoni.domain.models.bindings.users.UserEditBindingModel;
import org.yanmark.markoni.domain.models.services.OrderProductServiceModel;
import org.yanmark.markoni.domain.models.services.OrderServiceModel;
import org.yanmark.markoni.domain.models.services.UserRoleServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final ProductService productService;
    private final OrderProductService orderProductService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserRoleService userRoleService,
                           ProductService productService,
                           OrderProductService orderProductService,
                           OrderService orderService,
                           ModelMapper modelMapper,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.productService = productService;
        this.orderProductService = orderProductService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found!"));
    }

    @Override
    public UserServiceModel saveUser(UserServiceModel userService) {
        User user = this.modelMapper.map(giveRolesToUser(userService), User.class);
        user.setPassword(this.passwordEncoder.encode(userService.getPassword()));
        try {
            user = this.userRepository.saveAndFlush(user);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel updateUserProducts(UserServiceModel userService) {
        User user = this.modelMapper.map(userService, User.class);
        try {
            user = this.userRepository.saveAndFlush(user);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel updateUsersRole(UserServiceModel userService, UserRoleServiceModel userRoleService) {
        User user = this.userRepository.findById(userService.getId())
                .orElseThrow(() -> new IllegalArgumentException("User was not found!"));
        try {
            UserRole userRole = this.modelMapper.map(userRoleService, UserRole.class);
            List<String> roles = user.getAuthorities().stream()
                    .map(UserRole::getAuthority)
                    .collect(Collectors.toList());
            if (!roles.contains(userRole.getAuthority())) {
                user.getAuthorities().add(userRole);
            }
            if (roles.contains(userRole.getAuthority())) {
                UserRole role = user.getAuthorities().stream()
                        .filter(r -> r.getAuthority().equals(userRole.getAuthority()))
                        .findFirst().orElse(null);
                user.getAuthorities().remove(role);
            }
            user = this.userRepository.saveAndFlush(user);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel updateUser(UserServiceModel userService, UserEditBindingModel userEdit) {
        if (!userEdit.getNewPassword().equals("") && userEdit.getNewPassword() != null) {
            userService.setPassword(userEdit.getNewPassword());
        }
        User user = this.userRepository.findByUsername(userService.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User was not found!"));
        if (!this.passwordEncoder.matches(userEdit.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password!");
        }
        if (!this.passwordEncoder.matches(userService.getPassword(), user.getPassword())) {
            user.setPassword(this.passwordEncoder.encode(userService.getPassword()));
        }
        user.setEmail(userService.getEmail());
        try {
            user = this.userRepository.saveAndFlush(user);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel getUserById(String id) {
        return this.userRepository.findById(id)
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .orElseThrow(() -> new IllegalArgumentException("User was not found!"));
    }

    @Override
    public UserServiceModel getUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .orElseThrow(() -> new UsernameNotFoundException("User was not found!"));
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        return this.userRepository.findAllOrderByUsername().stream()
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public UserServiceModel buyOrder(OrderServiceModel orderService, UserServiceModel userService) {
        if (orderService.getQuantity() > orderService.getProduct().getQuantity()) {
            throw new IllegalArgumentException("The quantity is bigger than available!");
        }
        OrderProductServiceModel orderProductServiceModel =
                this.modelMapper.map(orderService.getProduct(), OrderProductServiceModel.class);
        orderProductServiceModel.setPrice(orderService.getPrice());
        orderProductServiceModel.setQuantity(orderService.getQuantity());
        orderProductServiceModel = this.orderProductService.saveOrderProduct(orderProductServiceModel);
        userService.getOrderProducts().add(orderProductServiceModel);
        User user = this.modelMapper.map(userService, User.class);
        try {
            user = this.userRepository.saveAndFlush(user);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        orderService.getProduct().setQuantity(orderService.getProduct().getQuantity() - orderService.getQuantity());
        this.productService.editProduct(orderService.getProduct(), orderService.getProduct().getId());
        this.orderService.deleteOrder(orderService.getId());
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    private UserServiceModel giveRolesToUser(UserServiceModel userService) {
        if (this.userRepository.count() == 0) {
            userService.setAuthorities(this.userRoleService.getAllRoles());
        } else {
            userService.getAuthorities().add(this.userRoleService.getRoleByName("USER"));
        }
        return userService;
    }
}
