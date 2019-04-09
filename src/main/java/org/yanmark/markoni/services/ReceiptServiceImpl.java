package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Receipt;
import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.services.ReceiptServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.ReceiptRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final PackageService packageService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReceiptServiceImpl(ReceiptRepository receiptRepository,
                              PackageService packageService,
                              ModelMapper modelMapper) {
        this.receiptRepository = receiptRepository;
        this.packageService = packageService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReceiptServiceModel saveReceipt(ReceiptServiceModel receiptService,
                                           PackageServiceModel packageService,
                                           UserServiceModel userService) {
        packageService.setStatus(Status.ACQUIRED);
        packageService = this.packageService.savePackage(packageService);
        receiptService.setFee(BigDecimal.valueOf(packageService.getWeight()).multiply(BigDecimal.valueOf(2.67)));
        receiptService.setIssuedOn(LocalDateTime.now());
        receiptService.setRecipient(userService);
        receiptService.setPakage(packageService);
        Receipt receipt = this.modelMapper.map(receiptService, Receipt.class);
        try {
            receipt = this.receiptRepository.saveAndFlush(receipt);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(receipt, ReceiptServiceModel.class);
    }

    @Override
    public ReceiptServiceModel getReceiptById(String id) {
        Receipt receipt = this.receiptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receipt was not found!"));
        return this.modelMapper.map(receipt, ReceiptServiceModel.class);
    }

    @Override
    public ReceiptServiceModel getReceiptByPackage(String packageId) {
        Receipt receipt = this.receiptRepository.findByPakage_Id(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Receipt was not found!"));
        return this.modelMapper.map(receipt, ReceiptServiceModel.class);
    }

    @Override
    public List<ReceiptServiceModel> getAllReceiptByUser(String username) {
        return this.receiptRepository.findAllByRecipient_Username(username).stream()
                .map(receipt -> this.modelMapper.map(receipt, ReceiptServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
