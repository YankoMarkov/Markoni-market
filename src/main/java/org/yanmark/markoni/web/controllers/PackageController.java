package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.models.bindings.packages.PackageCreateBindingModel;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.domain.models.views.packages.PackageDetailsViewModel;
import org.yanmark.markoni.domain.models.views.packages.PendingAndDeliveredViewModel;
import org.yanmark.markoni.domain.models.views.packages.ShippedViewModel;
import org.yanmark.markoni.domain.models.views.users.UserViewModel;
import org.yanmark.markoni.services.PackageService;
import org.yanmark.markoni.services.ReceiptService;
import org.yanmark.markoni.services.UserService;
import org.yanmark.markoni.web.annotations.PageTitle;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/packages")
public class PackageController extends BaseController {

    private static final String PACKAGES_PACKAGE_CREATE = "/packages/package-create";
    private static final String PACKAGES_PENDING = "/packages/pending";
    private static final String PACKAGES_PACKAGE_DETAILS = "/packages/package-details";
    private static final String PACKAGES_SHIPPED = "/packages/shipped";
    private static final String PACKAGES_DELIVERED = "/packages/delivered";

    private final PackageService packageService;
    private final UserService userService;
    private final ReceiptService receiptService;
    private final ModelMapper modelMapper;

    @Autowired
    public PackageController(PackageService packageService,
                             UserService userService,
                             ReceiptService receiptService,
                             ModelMapper modelMapper) {
        this.packageService = packageService;
        this.userService = userService;
        this.receiptService = receiptService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PageTitle("\uD835\uDCAB\uD835\uDCB6\uD835\uDCB8\uD835\uDCC0\uD835\uDCB6\uD835\uDC54\uD835\uDC52 \uD835\uDC9E\uD835\uDCC7\uD835\uDC52\uD835\uDCB6\uD835\uDCC9\uD835\uDC52")
    public ModelAndView create(@ModelAttribute("packageCreate") PackageCreateBindingModel packageCreate,
                               ModelAndView modelAndView) {
        List<UserViewModel> userViewModels = this.userService.getAllUsers().stream()
                .map(user -> this.modelMapper.map(user, UserViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("users", userViewModels);
        return this.view(PACKAGES_PACKAGE_CREATE, modelAndView);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView createConfirm(@Valid @ModelAttribute("packageCreate") PackageCreateBindingModel packageCreate,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view(PACKAGES_PACKAGE_CREATE);
        }
        PackageServiceModel packageServiceModel = this.modelMapper.map(packageCreate, PackageServiceModel.class);
        this.packageService.createPackage(packageServiceModel, packageCreate);
        return this.redirect(PACKAGES_PENDING);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("\uD835\uDCAB\uD835\uDCB6\uD835\uDCB8\uD835\uDCC0\uD835\uDCB6\uD835\uDC54\uD835\uDC52 \uD835\uDC9F\uD835\uDC52\uD835\uDCC9\uD835\uDCB6\uD835\uDCBE\uD835\uDCC1\uD835\uDCC8")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {
        PackageServiceModel packageServiceModel = this.packageService.getPackageById(id);
        PackageDetailsViewModel packageDetailsViewModel =
                this.modelMapper.map(packageServiceModel, PackageDetailsViewModel.class);
        packageDetailsViewModel.setRecipient(packageServiceModel.getRecipient().getUsername());
        if (packageServiceModel.getStatus().equals(Status.PENDING)) {
            packageDetailsViewModel.setEstimatedDeliveryDay("N/A");
        } else {
            if (packageServiceModel.getStatus().equals(Status.DELIVERED) ||
                    packageServiceModel.getStatus().equals(Status.ACQUIRED)) {
                packageDetailsViewModel.setEstimatedDeliveryDay("Delivered");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                String date = packageServiceModel.getEstimatedDeliveryDay().format(formatter);
                packageDetailsViewModel.setEstimatedDeliveryDay(date);
            }
        }
        modelAndView.addObject("package", packageDetailsViewModel);
        return this.view(PACKAGES_PACKAGE_DETAILS, modelAndView);
    }

    @GetMapping("/shipped")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PageTitle("\uD835\uDCAB\uD835\uDCB6\uD835\uDCB8\uD835\uDCC0\uD835\uDCB6\uD835\uDC54\uD835\uDC52 \uD835\uDCAE\uD835\uDCBD\uD835\uDCBE\uD835\uDCC5\uD835\uDCC5\uD835\uDC52\uD835\uDCB9")
    public ModelAndView shippedPackages(ModelAndView modelAndView) {
        List<ShippedViewModel> shippedViewModels =
                this.packageService.getAllPackagesByStatus(Status.SHIPPED).stream()
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
        return this.view(PACKAGES_SHIPPED, modelAndView);
    }

    @PostMapping("/shipped")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView deliverPackage(@RequestParam String shippedId) {
        PackageServiceModel packageServiceModel = this.packageService.getPackageById(shippedId);
        packageServiceModel.setStatus(Status.DELIVERED);
        this.packageService.savePackage(packageServiceModel);
        return this.redirect(PACKAGES_DELIVERED);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PageTitle("\uD835\uDCAB\uD835\uDCB6\uD835\uDCB8\uD835\uDCC0\uD835\uDCB6\uD835\uDC54\uD835\uDC52 \uD835\uDCAB\uD835\uDC52\uD835\uDCB9\uD835\uDCC3\uD835\uDCB9\uD835\uDCBE\uD835\uDCC3\uD835\uDC54")
    public ModelAndView pendingPackages(ModelAndView modelAndView) {
        List<PendingAndDeliveredViewModel> pendingAndDeliveredViewModels =
                this.packageService.getAllPackagesByStatus(Status.PENDING).stream()
                        .map(pack -> {
                            PendingAndDeliveredViewModel pendingAndDeliveredViewModel =
                                    this.modelMapper.map(pack, PendingAndDeliveredViewModel.class);
                            pendingAndDeliveredViewModel.setRecipient(pack.getRecipient().getUsername());
                            return pendingAndDeliveredViewModel;
                        })
                        .collect(Collectors.toList());
        modelAndView.addObject("pendings", pendingAndDeliveredViewModels);
        return this.view(PACKAGES_PENDING, modelAndView);
    }

    @PostMapping("/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView shipPackage(@RequestParam String pendingId) {
        PackageServiceModel packageServiceModel = this.packageService.getPackageById(pendingId);
        packageServiceModel.setStatus(Status.SHIPPED);
        packageServiceModel.setEstimatedDeliveryDay(LocalDateTime.now().plusDays(getRandomDays()));
        this.packageService.savePackage(packageServiceModel);
        return this.redirect(PACKAGES_SHIPPED);
    }

    @GetMapping("/delivered")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PageTitle("\uD835\uDCAB\uD835\uDCB6\uD835\uDCB8\uD835\uDCC0\uD835\uDCB6\uD835\uDC54\uD835\uDC52 \uD835\uDC9F\uD835\uDC52\uD835\uDCC1\uD835\uDCBE\uD835\uDCCB\uD835\uDC52\uD835\uDCC7\uD835\uDC52\uD835\uDCB9")
    public ModelAndView deliveredPackages(ModelAndView modelAndView) {
        List<PendingAndDeliveredViewModel> pendingAndDeliveredViewModels =
                this.packageService.getAllPackagesByStatus(Status.DELIVERED).stream()
                        .map(pack -> {
                            PendingAndDeliveredViewModel pendingAndDeliveredViewModel =
                                    this.modelMapper.map(pack, PendingAndDeliveredViewModel.class);
                            pendingAndDeliveredViewModel.setRecipient(pack.getRecipient().getUsername());
                            return pendingAndDeliveredViewModel;
                        })
                        .collect(Collectors.toList());
        modelAndView.addObject("delivering", pendingAndDeliveredViewModels);
        return this.view(PACKAGES_DELIVERED, modelAndView);
    }

    @GetMapping("/acquire/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView acquirePackage(@PathVariable String id, Principal principal) {
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        this.receiptService.saveReceipt(id, userServiceModel);
        return this.redirect("/home");
    }

    private int getRandomDays() {
        Random random = new Random();
        return random.nextInt(20 - 5 + 1) + 5;
    }
}
