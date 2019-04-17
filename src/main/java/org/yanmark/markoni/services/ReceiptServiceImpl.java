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
    public ReceiptServiceModel saveReceipt(String id, UserServiceModel userService) {
        PackageServiceModel packageServiceModel = this.packageService.getPackageById(id);
        ReceiptServiceModel receiptServiceModel = new ReceiptServiceModel();
        packageServiceModel.setStatus(Status.ACQUIRED);
        packageServiceModel = this.packageService.savePackage(packageServiceModel);
        receiptServiceModel.setFee(BigDecimal.valueOf(packageServiceModel.getWeight()).multiply(BigDecimal.valueOf(2.67)));
        receiptServiceModel.setIssuedOn(LocalDateTime.now());
        receiptServiceModel.setRecipient(userService);
        receiptServiceModel.setPakage(packageServiceModel);
        Receipt receipt = this.modelMapper.map(receiptServiceModel, Receipt.class);
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
