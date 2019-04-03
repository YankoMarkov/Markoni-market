package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.UserRole;
import org.yanmark.markoni.domain.models.services.UserRoleServiceModel;
import org.yanmark.markoni.repositories.UserRoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, ModelMapper modelMapper) {
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserRoleServiceModel getRoleByName(String name) {
        UserRole role = this.userRoleRepository.findByAuthority(name)
                .orElseThrow(() -> new IllegalArgumentException("Role not found!"));
        return this.modelMapper.map(role, UserRoleServiceModel.class);
    }

    @Override
    public Set<UserRoleServiceModel> getAllRoles() {
        List<UserRole> roles = this.userRoleRepository.findAll();
        if (roles == null) {
            return new HashSet<>();
        }
        return roles.stream()
                .map(role -> this.modelMapper.map(role, UserRoleServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }
}