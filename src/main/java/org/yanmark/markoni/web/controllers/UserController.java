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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

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
    public ModelAndView register(@ModelAttribute("userRegister") UserRegisterBindingModel userRegister) {
        return this.view("/users/register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute("userRegister") UserRegisterBindingModel userRegister,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view("/users/register");
        }
        if (!userRegister.getPassword().equals(userRegister.getConfirmPassword())) {
            return this.view("/users/register");
        }
        UserServiceModel userServiceModel = this.modelMapper.map(userRegister, UserServiceModel.class);
        if (this.userService.saveUser(userServiceModel) == null) {
            return this.view("/users/register");
        }
        return this.redirect("/users/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(@ModelAttribute("userLogin") UserLoginBindingModel userLogin) {
        return this.view("/users/login");
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        UserProfileViewModel userProfileViewModel = this.modelMapper.map(userServiceModel, UserProfileViewModel.class);
        modelAndView.addObject("userProfile", userProfileViewModel);
        return this.view("profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView edit(Principal principal, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        UserProfileViewModel userProfileViewModel = this.modelMapper.map(userServiceModel, UserProfileViewModel.class);
        modelAndView.addObject("user", userProfileViewModel);
        return this.view("edit-profile", modelAndView);
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editConfirm(@Valid @ModelAttribute("userEdit") UserEditBindingModel userEdit,
                                    BindingResult bindingResult) {
        System.out.println();
        if (bindingResult.hasErrors()) {
            return this.view("edit-profile");
        }
        if (!userEdit.getNewPassword().equals(userEdit.getConfirmNewPassword())) {
            return this.view("edit-profile");
        }
        UserServiceModel userServiceModel = this.modelMapper.map(userEdit, UserServiceModel.class);
        if (!userEdit.getNewPassword().equals("") && userEdit.getNewPassword() != null) {
            userServiceModel.setPassword(userEdit.getNewPassword());
        }
        this.userService.updateUser(userServiceModel, userEdit.getPassword());
        return this.redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROOT')")
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
        return this.view("all-users", modelAndView);
    }

    @PostMapping("/changeRole")
    @PreAuthorize("hasAuthority('ROOT')")
    public ModelAndView changeRole(@RequestParam("id") String id,
                                   @Valid @ModelAttribute("role") RoleViewModel role,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view("all-users");
        }
        UserServiceModel userServiceModel = this.userService.getUserById(id);
        if (userServiceModel.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROOT"))) {
            return this.redirect("/users/all");
        }
        UserRoleServiceModel userRoleServiceModel = this.userRoleService.getRoleByName(role.getAuthority());

        this.userService.updateUsersRole(userServiceModel, userRoleServiceModel);
        return this.redirect("/users/all");
    }

    @GetMapping("/account")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView userAccount(HttpSession session, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.getUserByUsername(session.getAttribute("username").toString());
        List<ProductHomeViewModel> pendingHomeModels = getProductHomeViewModels(userServiceModel, Status.PENDING);
        List<ProductHomeViewModel> shippedHomeModels = getProductHomeViewModels(userServiceModel, Status.SHIPPED);
        List<ProductHomeViewModel> deliveredHomeModels = getProductHomeViewModels(userServiceModel, Status.DELIVERED);
        modelAndView.addObject("pendingModels", pendingHomeModels);
        modelAndView.addObject("shippedModels", shippedHomeModels);
        modelAndView.addObject("deliveredModels", deliveredHomeModels);
        return this.view("/users/user-account", modelAndView);
    }

    private List<ProductHomeViewModel> getProductHomeViewModels(UserServiceModel userServiceModel, Status status) {
        List<PackageServiceModel> packageServiceModels =
                this.packageService.getAllPackagesByUserAndStatus(userServiceModel.getUsername(), status);
        return packageServiceModels.stream()
                .map(pack -> this.modelMapper.map(pack, ProductHomeViewModel.class))
                .collect(Collectors.toList());
    }
}
