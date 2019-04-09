package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.services.ReceiptServiceModel;
import org.yanmark.markoni.domain.models.views.receipts.ReceiptAllViewModel;
import org.yanmark.markoni.domain.models.views.receipts.ReceiptDetailsViewModel;
import org.yanmark.markoni.services.ReceiptService;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReceiptController extends BaseController {

    private final ReceiptService receiptService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReceiptController(ReceiptService receiptService, ModelMapper modelMapper) {
        this.receiptService = receiptService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/receipts")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView receipt(ModelAndView modelAndView, Principal principal) {
        List<ReceiptAllViewModel> receiptAllViewModels =
                this.receiptService.getAllReceiptByUser(principal.getName()).stream()
                        .map(receipt -> {
                            ReceiptAllViewModel receiptAllViewModel = this.modelMapper.map(receipt, ReceiptAllViewModel.class);
                            receiptAllViewModel.setRecipient(receipt.getRecipient().getUsername());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                            String date = receipt.getIssuedOn().format(formatter);
                            receiptAllViewModel.setIssuedOn(date);
                            return receiptAllViewModel;
                        })
                        .collect(Collectors.toList());
        modelAndView.addObject("receipts", receiptAllViewModels);
        return this.view("/receipts/receipt", modelAndView);
    }

    @GetMapping("/receipts/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {
        ReceiptServiceModel receiptServiceModel = this.receiptService.getReceiptById(id);
        ReceiptDetailsViewModel receiptDetailsViewModel =
                this.modelMapper.map(receiptServiceModel, ReceiptDetailsViewModel.class);
        receiptDetailsViewModel.setRecipient(receiptServiceModel.getRecipient().getUsername());
        receiptDetailsViewModel.setDeliveryAddress(receiptServiceModel.getPakage().getShippingAddress());
        receiptDetailsViewModel.setPackageDescription(receiptServiceModel.getPakage().getDescription());
        receiptDetailsViewModel.setPackageWeight(receiptServiceModel.getPakage().getWeight());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String date = receiptServiceModel.getIssuedOn().format(formatter);
        receiptDetailsViewModel.setIssuedOn(date);
        modelAndView.addObject("receipt", receiptDetailsViewModel);
        return this.view("/receipts/receipt-details", modelAndView);
    }
}
