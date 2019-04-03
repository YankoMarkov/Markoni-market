package org.yanmark.markoni.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.models.bindings.packages.PackageCreateBindingModel;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.domain.models.views.packages.PackageDetailsViewModel;
import org.yanmark.markoni.domain.models.views.users.UserViewModel;
import org.yanmark.markoni.services.PackageService;
import org.yanmark.markoni.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
		List<UserViewModel> userViewModels = this.userService.getAllUsers().stream()
				.map(user -> this.modelMapper.map(user, UserViewModel.class))
				.collect(Collectors.toList());
		modelAndView.addObject("users", userViewModels);
		return this.view("packageCreate", modelAndView);
	}
	
	@PostMapping("/create")
	public ModelAndView createConfirm(@Valid @ModelAttribute("packageCreate") PackageCreateBindingModel packageCreate,
	                                  BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.view("packageCreate");
		}
		UserServiceModel userServiceModel = this.userService.getUserById(packageCreate.getRecipient());
		if (userServiceModel.getProducts().isEmpty()) {
			return this.view("packageCreate");
		}
		PackageServiceModel packageServiceModel = this.modelMapper.map(packageCreate, PackageServiceModel.class);
		packageServiceModel.setRecipient(userServiceModel);
		packageServiceModel.setShippingAddress(userServiceModel.getAddress());
		packageServiceModel.setStatus(Status.PENDING);
		packageServiceModel.setEstimatedDeliveryDay(LocalDateTime.now());
		double weight = userServiceModel.getProducts().stream()
				.mapToDouble(ProductServiceModel::getWeight).sum();
		packageServiceModel.setWeight(weight);
		StringBuilder description = new StringBuilder();
		userServiceModel.getProducts().forEach(product -> description.append(product.getName()).append(System.lineSeparator()));
		packageServiceModel.setDescription(description.toString());
		if (this.packageService.savePackage(packageServiceModel) == null) {
			return this.view("packageCreate");
		}
		return this.redirect("/home");
	}
	
	@GetMapping("/details")
	public ModelAndView details(@RequestParam("id") String id, ModelAndView modelAndView) {
		PackageServiceModel packageServiceModel = this.packageService.getPackageById(id);
		PackageDetailsViewModel packageDetailsViewModel = this.modelMapper.map(packageServiceModel, PackageDetailsViewModel.class);
		packageDetailsViewModel.setRecipient(packageServiceModel.getRecipient().getUsername());
		if (packageServiceModel.getStatus().equals(Status.PENDING)) {
			packageDetailsViewModel.setEstimatedDeliveryDay("N/A");
		} else {
			if (packageServiceModel.getStatus().equals(Status.DELIVERED) || packageServiceModel.getStatus().equals(Status.ACQUIRED)) {
				packageDetailsViewModel.setEstimatedDeliveryDay("Delivered");
			} else {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
				String date = packageServiceModel.getEstimatedDeliveryDay().format(formatter);
				packageDetailsViewModel.setEstimatedDeliveryDay(date);
			}
		}
		modelAndView.addObject("package", packageDetailsViewModel);
		return this.view("packageDetails", modelAndView);
	}
}
