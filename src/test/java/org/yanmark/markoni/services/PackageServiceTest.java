package org.yanmark.markoni.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.yanmark.markoni.domain.entities.OrderProduct;
import org.yanmark.markoni.domain.entities.Package;
import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.entities.User;
import org.yanmark.markoni.domain.models.bindings.packages.PackageCreateBindingModel;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.OrderProductRepository;
import org.yanmark.markoni.repositories.PackageRepository;
import org.yanmark.markoni.repositories.UserRepository;
import org.yanmark.markoni.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PackageServiceTest {

    @Autowired
    private PackageService packageService;

    @MockBean
    private PackageRepository mockPackageRepository;

    @MockBean
    private OrderProductRepository mockOrderProductRepository;

    @Autowired
    private OrderProductService orderProductService;

    @MockBean
    private UserRepository mockUserRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void savePackage_whenValidPackage_returnPackage() {
        Package testPackage = TestUtils.getTestPackage();
        User testUser = TestUtils.getTestUser();
        when(mockPackageRepository.saveAndFlush(any(Package.class)))
                .thenReturn(testPackage);
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);
        PackageServiceModel packageServiceModel = modelMapper.map(testPackage, PackageServiceModel.class);

        PackageServiceModel result = packageService.savePackage(packageServiceModel);

        assertEquals(packageServiceModel.getId(), result.getId());
    }

    @Test
    public void createPackage_whenUserHasProduct_throwException() {
        Package testPackage = TestUtils.getTestPackage();
        User testUser = TestUtils.getTestUser();
        OrderProduct testOrderProduct = TestUtils.getTestOrderProduct();
        testUser.setOrderProducts(Set.of(testOrderProduct));
        when(mockUserRepository.findById(anyString()))
                .thenReturn(Optional.of(testUser));
        when(mockPackageRepository.saveAndFlush(any(Package.class)))
                .thenReturn(testPackage);
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);
        PackageServiceModel packageServiceModel = modelMapper.map(testPackage, PackageServiceModel.class);
        PackageCreateBindingModel packageCreateBindingModel = modelMapper.map(testPackage, PackageCreateBindingModel.class);

        PackageServiceModel result = packageService.createPackage(packageServiceModel, packageCreateBindingModel);

        assertNotNull(testUser);
        assertEquals(testPackage.getId(), result.getId());
    }

    @Test(expected = Exception.class)
    public void createPackage_whenUserHasNoProducts_throwException() {
        Package testPackage = TestUtils.getTestPackage();
        User testUser = TestUtils.getTestUser();
        when(mockUserRepository.findById(anyString()))
                .thenReturn(Optional.of(testUser));
        when(mockPackageRepository.saveAndFlush(any(Package.class)))
                .thenReturn(testPackage);
        PackageServiceModel packageServiceModel = modelMapper.map(testPackage, PackageServiceModel.class);
        PackageCreateBindingModel packageCreateBindingModel = modelMapper.map(testPackage, PackageCreateBindingModel.class);

        PackageServiceModel result = packageService.createPackage(packageServiceModel, packageCreateBindingModel);

        assertNotNull(testPackage);
        assertEquals(testPackage.getId(), result.getId());
    }

    @Test
    public void getAllPackagesByStatus_whenValidStatus_return2Packages() {
        List<Package> testPackages = TestUtils.getTestPackages(2);
        when(mockPackageRepository.findAllByStatus(any()))
                .thenReturn(testPackages);

        List<PackageServiceModel> packageServiceModels = packageService.getAllPackagesByStatus(any());

        assertEquals(2, packageServiceModels.size());
    }

    @Test
    public void getAllPackagesByStatus_whenNoValidStatus_returnNoPackages() {
        when(mockPackageRepository.findAllByStatus(any()))
                .thenReturn(new ArrayList<>());

        List<PackageServiceModel> packageServiceModels = packageService.getAllPackagesByStatus(any());

        assertEquals(0, packageServiceModels.size());
    }

//    @Test
//    public void getAllPackagesByUserAndStatus_whenValidUserAndStatus_return2Packages() {
//        List<Package> testPackages = TestUtils.getTestPackages(2);
//        User testUser = TestUtils.getTestUser();
//        UserServiceModel userServiceModel = modelMapper.map(testUser,UserServiceModel.class);
//        when(mockPackageRepository.findAllByRecipientAndStatus(testUser, any()))
//                .thenReturn(testPackages);
//
//        List<PackageServiceModel> packageServiceModels = packageService.getAllPackagesByUserAndStatus(userServiceModel, any());
//
//        assertEquals(2, packageServiceModels.size());
//    }

    @Test
    public void getPackageById_whenValidPackageId_returnPackages() {
        Package testPackage = TestUtils.getTestPackage();
        when(mockPackageRepository.findById(anyString()))
                .thenReturn(Optional.of(testPackage));

        PackageServiceModel packageServiceModel = packageService.getPackageById(anyString());

        assertEquals(testPackage.getId(), packageServiceModel.getId());
    }
}
