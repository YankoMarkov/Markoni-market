package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.models.bindings.users.UserEditBindingModel;
import org.yanmark.markoni.domain.models.bindings.users.UserLoginBindingModel;
import org.yanmark.markoni.domain.models.bindings.users.UserRegisterBindingModel;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.services.UserRoleServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.domain.models.views.products.ProductHomeViewModel;
import org.yanmark.markoni.domain.models.views.users.RoleViewModel;
import org.yanmark.markoni.domain.models.views.users.UserAllViewModel;
import org.yanmark.markoni.domain.models.views.users.UserProfileViewModel;
import org.yanmark.markoni.services.PackageService;
import org.yanmark.markoni.services.UserRoleService;
import org.yanmark.markoni.services.UserService;
import org.yanmark.markoni.web.annotations.PageTitle;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private static final String USERS_REGISTER = "/users/register";
    private static final String USERS_LOGIN = "/users/login";
    private static final String USERS_PROFILE = "/users/profile";
    private static final String USERS_EDIT_PROFILE = "/users/edit-profile";
    private static final String USERS_ALL_USERS = "/users/all-users";
    private static final String USERS_ALL = "/users/all";
    private static final String USERS_USER_STORAGE = "/users/user-storage";

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final PackageService packageService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService,
                          UserRoleService userRoleService,
                          PackageService packageService,
                          ModelMapper modelMapper) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.packageService = packageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("\uD835\uDCB0\uD835\uDCC8\uD835\uDC52\uD835\uDCC7 \uD835\uDC45\uD835\uDC52\uD835\uDC54\uD835\uDCBE\uD835\uDCC8\uD835\uDCC9\uD835\uDC52\uD835\uDCC7")
    public ModelAndView register(@ModelAttribute("userRegister") UserRegisterBindingModel userRegister) {
        return this.view(USERS_REGISTER);
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute("userRegister") UserRegisterBindingModel userRegister,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view(USERS_REGISTER);
        }
        if (!userRegister.getPassword().equals(userRegister.getConfirmPassword())) {
            return this.view(USERS_REGISTER);
        }
        UserServiceModel userServiceModel = this.modelMapper.map(userRegister, UserServiceModel.class);
        this.userService.saveUser(userServiceModel);
        return this.redirect(USERS_LOGIN);
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("\uD835\uDCB0\uD835\uDCC8\uD835\uDC52\uD835\uDCC7 \uD835\uDC3F\uD835\uDC5C\uD835\uDC54\uD835\uDCBE\uD835\uDCC3")
    public ModelAndView login(@ModelAttribute("userLogin") UserLoginBindingModel userLogin) {
        return this.view(USERS_LOGIN);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("\uD835\uDCB0\uD835\uDCC8\uD835\uDC52\uD835\uDCC7 \uD835\uDCAB\uD835\uDCC7\uD835\uDC5C\uD835\uDCBB\uD835\uDCBE\uD835\uDCC1\uD835\uDC52")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        UserProfileViewModel userProfileViewModel = this.modelMapper.map(userServiceModel, UserProfileViewModel.class);
        modelAndView.addObject("userProfile", userProfileViewModel);
        return this.view(USERS_PROFILE, modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("\uD835\uDCB0\uD835\uDCC8\uD835\uDC52\uD835\uDCC7 \uD835\uDC38\uD835\uDCB9\uD835\uDCBE\uD835\uDCC9")
    public ModelAndView edit(Principal principal,
                             @ModelAttribute("userEdit") UserEditBindingModel userEdit,
                             ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        UserProfileViewModel userProfileViewModel = this.modelMapper.map(userServiceModel, UserProfileViewModel.class);
        modelAndView.addObject("user", userProfileViewModel);
        return this.view(USERS_EDIT_PROFILE, modelAndView);
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editConfirm(@Valid @ModelAttribute("userEdit") UserEditBindingModel userEdit,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view(USERS_EDIT_PROFILE);
        }
        if (!userEdit.getNewPassword().equals(userEdit.getConfirmNewPassword())) {
            return this.view(USERS_EDIT_PROFILE);
        }
        UserServiceModel userServiceModel = this.modelMapper.map(userEdit, UserServiceModel.class);
        this.userService.updateUser(userServiceModel, userEdit);
        return this.redirect(USERS_PROFILE);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROOT')")
    @PageTitle("\uD835\uDC9C\uD835\uDCC1\uD835\uDCC1 \uD835\uDCB0\uD835\uDCC8\uD835\uDC52\uD835\uDCC7\uD835\uDCC8")
    public ModelAndView usersAll(@ModelAttribute("allUsers") UserAllViewModel allUsers,
                                 ModelAndView modelAndView) {
        List<UserServiceModel> userServiceModels = this.userService.getAllUsers();
        List<UserAllViewModel> usersViewModels = userServiceModels.stream()
                .map(user -> {
                    UserAllViewModel usersViewModel = this.modelMapper.map(user, UserAllViewModel.class);
                    Set<String> authorities = user.getAuthorities().stream()
                            .map(UserRoleServiceModel::getAuthority)
                            .collect(Collectors.toSet());
                    usersViewModel.setAuthorities(authorities);
                    return usersViewModel;
                })
                .collect(Collectors.toList());

        modelAndView.addObject("allUsers", usersViewModels);
        return this.view(USERS_ALL_USERS, modelAndView);
    }

    @PostMapping("/changeRole")
    @PreAuthorize("hasAuthority('ROOT')")
    public ModelAndView changeRole(@RequestParam("id") String id,
                                   @Valid @ModelAttribute("role") RoleViewModel role,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view(USERS_ALL_USERS);
        }
        UserServiceModel userServiceModel = this.userService.getUserById(id);
        if (userServiceModel.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROOT"))) {
            return this.redirect(USERS_ALL);
        }
        UserRoleServiceModel userRoleServiceModel = this.userRoleService.getRoleByName(role.getAuthority());
        this.userService.updateUsersRole(userServiceModel, userRoleServiceModel);
        return this.redirect(USERS_ALL);
    }

    @GetMapping("/storage")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("\uD835\uDCB0\uD835\uDCC8\uD835\uDC52\uD835\uDCC7 \uD835\uDCAE\uD835\uDCC9\uD835\uDC5C\uD835\uDCC7\uD835\uDCB6\uD835\uDC54\uD835\uDC52")
    public ModelAndView userAccount(Principal principal, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        List<ProductHomeViewModel> pendingHomeModels = getProductHomeViewModels(userServiceModel, Status.PENDING);
        List<ProductHomeViewModel> shippedHomeModels = getProductHomeViewModels(userServiceModel, Status.SHIPPED);
        List<ProductHomeViewModel> deliveredHomeModels = getProductHomeViewModels(userServiceModel, Status.DELIVERED);
        modelAndView.addObject("pendingModels", pendingHomeModels);
        modelAndView.addObject("shippedModels", shippedHomeModels);
        modelAndView.addObject("deliveredModels", deliveredHomeModels);
        List<String> roles = userServiceModel.getAuthorities().stream()
                .map(UserRoleServiceModel::getAuthority)
                .collect(Collectors.toList());
        modelAndView.addObject("roles", roles);
        return this.view(USERS_USER_STORAGE, modelAndView);
    }

    private List<ProductHomeViewModel> getProductHomeViewModels(UserServiceModel userServiceModel, Status status) {
        List<PackageServiceModel> packageServiceModels =
                this.packageService.getAllPackagesByUserAndStatus(userServiceModel, status);
        return packageServiceModels.stream()
                .map(pack -> this.modelMapper.map(pack, ProductHomeViewModel.class))
                .collect(Collectors.toList());
    }
}
