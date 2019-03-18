package markoni.web.controllers;

import markoni.entities.Status;
import markoni.models.bindings.PackageCreateBindingModel;
import markoni.models.services.PackageServiceModel;
import markoni.models.services.UserServiceModel;
import markoni.models.views.UserAllViewModel;
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

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/packages")
public class PackageController extends BaseController {
	
	private final PackageService packageService;
	private final UserService userService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public PackageController(PackageService packageService, UserService userService, ModelMapper modelMapper) {
		this.packageService = packageService;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/create")
	public ModelAndView create(@ModelAttribute("packageCreate") PackageCreateBindingModel packageCreate,
	                           ModelAndView modelAndView) {
		List<UserAllViewModel> userAllViewModels = this.userService.getAllUsers().stream()
				.map(user -> this.modelMapper.map(user, UserAllViewModel.class))
				.collect(Collectors.toList());
		modelAndView.addObject("users", userAllViewModels);
		return this.view("packageCreate", modelAndView);
	}
	
	@PostMapping("/create")
	public ModelAndView createConfirm(@Valid @ModelAttribute("packageCreate") PackageCreateBindingModel packageCreate,
	                                  BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.view("packageCreate");
		}
		PackageServiceModel packageServiceModel = this.modelMapper.map(packageCreate, PackageServiceModel.class);
		UserServiceModel userServiceModel = this.userService.getUserById(packageCreate.getRecipient());
		packageServiceModel.setRecipient(userServiceModel);
		packageServiceModel.setProducts(userServiceModel.getProducts());
		packageServiceModel.setStatus(Status.PENDING);
		packageServiceModel.setEstimatedDeliveryDay(LocalDateTime.now());
		if (this.packageService.savePackage(packageServiceModel) == null) {
			return this.view("packageCreate");
		}
		return this.redirect("/home");
	}
}
