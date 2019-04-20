package org.yanmark.markoni.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.yanmark.markoni.domain.entities.UserRole;
import org.yanmark.markoni.domain.models.services.UserRoleServiceModel;
import org.yanmark.markoni.repositories.UserRoleRepository;
import org.yanmark.markoni.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRoleServiceTest {

    private UserRoleService userRoleService;

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    private ModelMapper modelMapper;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        userRoleService = new UserRoleServiceImpl(mockUserRoleRepository, modelMapper);
    }

    @Test
    public void getRoleByName_whenValidName_returnUserRole() {
        UserRole testUserRole = TestUtils.getTestUserRole();
        when(mockUserRoleRepository.findByAuthority(anyString()))
                .thenReturn(Optional.of(testUserRole));

        UserRoleServiceModel result = userRoleService.getRoleByName(testUserRole.getAuthority());

        assertEquals(testUserRole.getId(), result.getId());
    }

    @Test(expected = Exception.class)
    public void getRoleByName_whenNoValidName_returnUserRole() {
        UserRole testUserRole = TestUtils.getTestUserRole();
        when(mockUserRoleRepository.findByAuthority(anyString()))
                .thenReturn(any());

        UserRoleServiceModel result = userRoleService.getRoleByName(testUserRole.getAuthority());

        assertEquals(testUserRole.getId(), result.getId());
    }

    @Test
    public void getAllRoles_whenHasRoles_returnRoles() {
        List<UserRole> testRoles = TestUtils.getTestUserRoles(2);
        when(mockUserRoleRepository.findAll())
                .thenReturn(testRoles);

        Set<UserRoleServiceModel> result = userRoleService.getAllRoles();

        assertEquals(2, result.size());
    }

    @Test
    public void getAllRoles_whenNoHasRoles_returnRoles() {
        when(mockUserRoleRepository.findAll())
                .thenReturn(new ArrayList<>());

        Set<UserRoleServiceModel> result = userRoleService.getAllRoles();

        assertEquals(0, result.size());
    }
}
