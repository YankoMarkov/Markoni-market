package org.yanmark.markoni.web.controllers;

import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.views.packages.ShippedViewModel;
import org.yanmark.markoni.services.PackageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/packages")
public class ShippedController extends BaseController {
	
	private final PackageService packageService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public ShippedController(PackageService packageService, ModelMapper modelMapper) {
		this.packageService = packageService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/shipped")
	public ModelAndView shippedPackages(ModelAndView modelAndView) {
		List<ShippedViewModel> shippedViewModels = this.packageService.getAllPackagesByStatus(Status.SHIPPED).stream()
				.map(pack -> {
					ShippedViewModel shippedViewModel = this.modelMapper.map(pack, ShippedViewModel.class);
					shippedViewModel.setRecipient(pack.getRecipient().getUsername());
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
					String date = pack.getEstimatedDeliveryDay().format(formatter);
					shippedViewModel.setEstimatedDeliveryDay(date);
					return shippedViewModel;
				})
				.collect(Collectors.toList());
		modelAndView.addObject("shipping", shippedViewModels);
		return this.view("shipped", modelAndView);
	}
	
	@PostMapping("/shipped")
	public ModelAndView deliverPackage(@RequestParam String shippedId) {
		PackageServiceModel packageServiceModel = this.packageService.getPackageById(shippedId);
		packageServiceModel.setStatus(Status.DELIVERED);
		this.packageService.savePackage(packageServiceModel);
		return this.redirect("/packages/shipped");
	}
}
