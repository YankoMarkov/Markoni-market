package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Receipt;
import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.models.services.OrderProductServiceModel;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.services.ReceiptServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.ReceiptRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final PackageService packageService;
    private final UserService userService;
    private final OrderProductService orderProductService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReceiptServiceImpl(ReceiptRepository receiptRepository,
                              PackageService packageService,
                              UserService userService,
                              OrderProductService orderProductService,
                              ModelMapper modelMapper) {
        this.receiptRepository = receiptRepository;
        this.packageService = packageService;
        this.userService = userService;
        this.orderProductService = orderProductService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReceiptServiceModel saveReceipt(String id, UserServiceModel userService) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderProductServiceModel orderProduct : userService.getOrderProducts()) {
            total = total.add(orderProduct.getPrice());
        }
        ReceiptServiceModel receiptServiceModel = new ReceiptServiceModel();
        PackageServiceModel packageServiceModel = this.packageService.getPackageById(id);
        for (PackageServiceModel pakage : userService.getPackages()) {
            if (pakage.getId().equals(packageServiceModel.getId())) {
                pakage.setStatus(Status.ACQUIRED);
                packageServiceModel = this.packageService.savePackage(pakage);
            }
        }
        receiptServiceModel.setFee(BigDecimal.valueOf(packageServiceModel.getWeight()).multiply(BigDecimal.valueOf(2.67)));
        receiptServiceModel.setTotal(total.add(receiptServiceModel.getFee()));
        receiptServiceModel.setIssuedOn(LocalDateTime.now());
        receiptServiceModel.setPakage(packageServiceModel);

        Set<OrderProductServiceModel> orders = userService.getOrderProducts();
        userService.setOrderProducts(new HashSet<>());
        this.userService.updateUserProducts(userService);
        for (OrderProductServiceModel order : orders) {
            this.orderProductService.deleteOrderProduct(order.getId());
        }
        receiptServiceModel.setRecipient(userService);

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

    @Scheduled(fixedRate = 43200000)
    private void deleteExpiredReceipt() {
        List<Receipt> receipts = this.receiptRepository.findAll();
        for (Receipt receipt : receipts) {
            if (LocalDateTime.now().isAfter(receipt.getIssuedOn().plusMonths(1))) {
                this.receiptRepository.deleteById(receipt.getId());
            }
        }
    }
}
