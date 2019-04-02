package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.models.services.UserRoleServiceModel;

import java.util.Set;

public interface UserRoleService {

    UserRoleServiceModel getRoleByName(String name);

    Set<UserRoleServiceModel> getAllRoles();
}