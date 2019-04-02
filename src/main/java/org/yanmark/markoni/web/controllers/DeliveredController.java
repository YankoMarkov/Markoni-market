package org.yanmark.markoni.web.controllers;

import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.services.ReceiptServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.domain.models.views.packages.PendingAndDeliveredViewModel;
import org.yanmark.markoni.services.PackageService;
import org.yanmark.markoni.services.ReceiptService;
import org.yanmark.markoni.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/packages")
public class DeliveredController extends BaseController {
	
	private final PackageService packageService;
	private final UserService userService;
	private final ReceiptService receiptService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public DeliveredController(PackageService packageService, UserService userService, ReceiptService receiptService, ModelMapper modelMapper) {
		this.packageService = packageService;
		this.userService = userService;
		this.receiptService = receiptService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/delivered")
	public ModelAndView deliveredPackages(ModelAndView modelAndView) {
		List<PendingAndDeliveredViewModel> pendingAndDeliveredViewModels = this.packageService.getAllPackagesByStatus(Status.DELIVERED).stream()
				.map(pack -> {
					PendingAndDeliveredViewModel pendingAndDeliveredViewModel = this.modelMapper.map(pack, PendingAndDeliveredViewModel.class);
					pendingAndDeliveredViewModel.setRecipient(pack.getRecipient().getUsername());
					return pendingAndDeliveredViewModel;
				})
				.collect(Collectors.toList());
		modelAndView.addObject("delivering", pendingAndDeliveredViewModels);
		return this.view("delivered", modelAndView);
	}
	
	@PostMapping("/acquire")
	public ModelAndView acquirePackage(@RequestParam String deliveredId, HttpSession session) {
		String username = session.getAttribute("username").toString();
		PackageServiceModel packageServiceModel = this.packageService.getPackageById(deliveredId);
		packageServiceModel.setStatus(Status.ACQUIRED);
		ReceiptServiceModel receiptServiceModel = createReceipt(username, packageServiceModel);
		packageServiceModel.setReceipt(receiptServiceModel);
		this.packageService.savePackage(packageServiceModel);
		return this.redirect("/home");
	}
	
	private ReceiptServiceModel createReceipt(String username, PackageServiceModel packageServiceModel) {
		UserServiceModel userServiceModel = this.userService.getUserByUsername(username);
		ReceiptServiceModel receiptServiceModel = new ReceiptServiceModel();
		receiptServiceModel.setFee(BigDecimal.valueOf(packageServiceModel.getWeight()).multiply(BigDecimal.valueOf(2.67)));
		receiptServiceModel.setIssuedOn(LocalDateTime.now());
		receiptServiceModel.setRecipient(userServiceModel);
		receiptServiceModel.setaPackage(packageServiceModel);
		return this.receiptService.saveReceipt(receiptServiceModel);
	}
}
