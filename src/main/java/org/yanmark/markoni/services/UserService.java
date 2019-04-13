package org.yanmark.markoni.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.yanmark.markoni.domain.models.bindings.users.UserEditBindingModel;
import org.yanmark.markoni.domain.models.services.OrderServiceModel;
import org.yanmark.markoni.domain.models.services.UserRoleServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserServiceModel saveUser(UserServiceModel userService);

    UserServiceModel updateUserProducts(UserServiceModel userService);

    UserServiceModel updateUsersRole(UserServiceModel userService, UserRoleServiceModel userRoleService);

    UserServiceModel updateUser(UserServiceModel userService, UserEditBindingModel userEdit);

    UserServiceModel getUserById(String id);

    UserServiceModel getUserByUsername(String username);

    List<UserServiceModel> getAllUsers();

    UserServiceModel buyOrder(OrderServiceModel orderService, UserServiceModel userService);
}
