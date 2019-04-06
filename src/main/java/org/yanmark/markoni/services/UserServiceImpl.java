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
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserRoleServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserRoleService userRoleService,
                           ModelMapper modelMapper,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    public UserServiceModel saveUser(UserServiceModel userService) {
        User user = this.modelMapper.map(giveRolesToUser(userService), User.class);
        user.setPassword(this.passwordEncoder.encode(userService.getPassword()));
        try {
            user = this.userRepository.saveAndFlush(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel updateUsersRole(UserServiceModel userService, UserRoleServiceModel userRoleService) {
        User user = this.userRepository.findById(userService.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        try {
            UserRole userRole = this.modelMapper.map(userRoleService, UserRole.class);
            List<String> roles = user.getAuthorities().stream()
                    .map(UserRole::getAuthority)
                    .collect(Collectors.toList());
            if (!roles.contains(userRole.getAuthority())) {
                user.getAuthorities().add(userRole);
            }
            if (roles.contains(userRole.getAuthority()) &&
                    !userRole.getAuthority().equals("USER")) {
                UserRole role = user.getAuthorities().stream()
                        .filter(r -> r.getAuthority().equals(userRole.getAuthority()))
                        .findFirst().orElse(null);
                user.getAuthorities().remove(role);
            }
            user = this.userRepository.saveAndFlush(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel updateUser(UserServiceModel userService, UserEditBindingModel userEdit) {
        if (!userEdit.getNewPassword().equals("") && userEdit.getNewPassword() != null) {
            userService.setPassword(userEdit.getNewPassword());
        }
        User user = this.userRepository.findByUsername(userService.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
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
            e.printStackTrace();
            return null;
        }
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel getUserById(String id) {
        return this.userRepository.findById(id)
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    @Override
    public UserServiceModel getUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        List<User> users = this.userRepository.findAllOrderByUsername();
        if (users == null) {
            return new ArrayList<>();
        }
        return users.stream()
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public UserServiceModel userBuyProduct(ProductServiceModel productService, UserServiceModel userService) {
        userService.getProducts().add(productService);
        User user = this.modelMapper.map(userService, User.class);
        try {
            user = this.userRepository.saveAndFlush(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
