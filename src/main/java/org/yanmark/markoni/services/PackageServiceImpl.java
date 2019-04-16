package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Package;
import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.entities.User;
import org.yanmark.markoni.domain.models.bindings.packages.PackageCreateBindingModel;
import org.yanmark.markoni.domain.models.services.OrderProductServiceModel;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.PackageRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final OrderProductService orderProductService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository,
                              OrderProductService orderProductService,
                              UserService userService,
                              ModelMapper modelMapper) {
        this.packageRepository = packageRepository;
        this.orderProductService = orderProductService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public PackageServiceModel savePackage(PackageServiceModel packageService) {
        Package pakage = this.modelMapper.map(packageService, Package.class);
        try {
            pakage = this.packageRepository.saveAndFlush(pakage);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        Set<OrderProductServiceModel> orders = packageService.getRecipient().getOrderProducts();
        packageService.getRecipient().setOrderProducts(new HashSet<>());
        this.userService.updateUserProducts(packageService.getRecipient());
        for (OrderProductServiceModel order : orders) {
            this.orderProductService.deleteOrderProduct(order.getId());
        }
        return this.modelMapper.map(pakage, PackageServiceModel.class);
    }

    @Override
    public PackageServiceModel createPackage(PackageServiceModel packageService, PackageCreateBindingModel packageCreate) {
        UserServiceModel userServiceModel = this.userService.getUserById(packageCreate.getRecipient());
        if (userServiceModel.getOrderProducts().isEmpty()) {
            throw new IllegalArgumentException("The user has no products!");
        }
        packageService.setRecipient(userServiceModel);
        packageService.setShippingAddress(userServiceModel.getAddress());
        packageService.setStatus(Status.PENDING);
        packageService.setEstimatedDeliveryDay(LocalDateTime.now());
        double weight = userServiceModel.getOrderProducts().stream()
                .mapToDouble(OrderProductServiceModel::getWeight).sum();
        packageService.setWeight(weight);
        StringBuilder description = new StringBuilder();
        userServiceModel.getOrderProducts()
                .forEach(order -> description.append(order.getName()).append(System.lineSeparator()));
        packageService.setDescription(description.toString());
        return savePackage(packageService);
    }

    @Override
    public List<PackageServiceModel> getAllPackagesByStatus(Status status) {
        return this.packageRepository.findAllByStatus(status).stream()
                .map(pack -> this.modelMapper.map(pack, PackageServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<PackageServiceModel> getAllPackagesByUserAndStatus(UserServiceModel userService, Status status) {
        User user = this.modelMapper.map(userService, User.class);
        return this.packageRepository.findAllByRecipientAndStatus(user, status).stream()
                .map(pack -> this.modelMapper.map(pack, PackageServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public PackageServiceModel getPackageById(String id) {
        Package aPackage = this.packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package was not found!"));
        return this.modelMapper.map(aPackage, PackageServiceModel.class);
    }
}
