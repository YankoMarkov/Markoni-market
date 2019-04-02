package markoni.web.controllers;

import markoni.domain.entities.Status;
import markoni.domain.models.bindings.UserLoginBindingModel;
import markoni.domain.models.bindings.UserRegisterBindingModel;
import markoni.domain.models.services.PackageServiceModel;
import markoni.domain.models.services.UserServiceModel;
import markoni.domain.models.views.ProductHomeViewModel;
import markoni.services.PackageService;
import markoni.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	
	private final UserService userService;
	private final PackageService packageService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public UserController(UserService userService, PackageService packageService, ModelMapper modelMapper) {
		this.userService = userService;
		this.packageService = packageService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/register")
	public ModelAndView register(@ModelAttribute("userRegister") UserRegisterBindingModel userRegister) {
		return this.view("register");
	}
	
	@PostMapping("register")
	public ModelAndView registerConfirm(@Valid @ModelAttribute("userRegister") UserRegisterBindingModel userRegister,
	                                    BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.view("register");
		}
		UserServiceModel userServiceModel = this.modelMapper.map(userRegister, UserServiceModel.class);
		
		if (this.userService.saveUser(userServiceModel) == null) {
			return this.view("register");
		}
		return this.redirect("/user/login");
	}
	
	@GetMapping("/login")
	public ModelAndView login(@ModelAttribute("userLogin") UserLoginBindingModel userLogin) {
		return this.view("login");
	}
	
	@PostMapping("/login")
	public ModelAndView loginConfirm(@Valid @ModelAttribute("userLogin") UserLoginBindingModel userLogin,
	                                 BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.view("login");
		}
		return this.redirect("/home");
	}
	
	@GetMapping("/account")
	public ModelAndView userAccount(HttpSession session, ModelAndView modelAndView) {
		UserServiceModel userServiceModel = this.userService.getUserByUsername(session.getAttribute("username").toString());
		List<ProductHomeViewModel> pendingHomeModels = getProductHomeViewModels(userServiceModel, Status.PENDING);
		List<ProductHomeViewModel> shippedHomeModels = getProductHomeViewModels(userServiceModel, Status.SHIPPED);
		List<ProductHomeViewModel> deliveredHomeModels = getProductHomeViewModels(userServiceModel, Status.DELIVERED);
		modelAndView.addObject("pendingModels", pendingHomeModels);
		modelAndView.addObject("shippedModels", shippedHomeModels);
		modelAndView.addObject("deliveredModels", deliveredHomeModels);
		return this.view("userAccount", modelAndView);
	}
	
	@GetMapping("/logout")
	public ModelAndView logout(HttpSession session) {
		if (session.getAttribute("username") == null) {
			this.redirect("/user/login");
		}
		session.invalidate();
		return this.redirect("/");
	}
	
	private List<ProductHomeViewModel> getProductHomeViewModels(UserServiceModel userServiceModel, Status status) {
		List<PackageServiceModel> packageServiceModels = this.packageService.getAllPackagesByUserAndStatus(userServiceModel.getUsername(), status);
		return packageServiceModels.stream()
				.map(pack -> this.modelMapper.map(pack, ProductHomeViewModel.class))
				.collect(Collectors.toList());
	}
}
