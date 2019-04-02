package markoni.web.controllers;

import markoni.domain.entities.Status;
import markoni.domain.models.services.PackageServiceModel;
import markoni.domain.models.views.PendingAndDeliveredViewModel;
import markoni.services.PackageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/packages")
public class PendingController extends BaseController {
	
	private final PackageService packageService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public PendingController(PackageService packageService, ModelMapper modelMapper) {
		this.packageService = packageService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/pending")
	public ModelAndView pendingPackages(ModelAndView modelAndView) {
		List<PendingAndDeliveredViewModel> pendingAndDeliveredViewModels = this.packageService.getAllPackagesByStatus(Status.PENDING).stream()
				.map(pack -> {
					PendingAndDeliveredViewModel pendingAndDeliveredViewModel = this.modelMapper.map(pack, PendingAndDeliveredViewModel.class);
					pendingAndDeliveredViewModel.setRecipient(pack.getRecipient().getUsername());
					return pendingAndDeliveredViewModel;
				})
				.collect(Collectors.toList());
		modelAndView.addObject("pendings", pendingAndDeliveredViewModels);
		return this.view("pending", modelAndView);
	}
	
	@PostMapping("/pending")
	public ModelAndView shipPackage(@RequestParam String pendingId) {
		PackageServiceModel packageServiceModel = this.packageService.getPackageById(pendingId);
		packageServiceModel.setStatus(Status.SHIPPED);
		packageServiceModel.setEstimatedDeliveryDay(LocalDateTime.now().plusDays(getRandomDays()));
		this.packageService.savePackage(packageServiceModel);
		return this.redirect("/packages/pending");
	}
	
	private int getRandomDays() {
		Random random = new Random();
		return random.nextInt(20 - 5 + 1) + 5;
	}
}
