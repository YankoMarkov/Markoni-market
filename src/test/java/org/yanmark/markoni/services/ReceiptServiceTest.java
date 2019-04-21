package org.yanmark.markoni.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.yanmark.markoni.domain.entities.Package;
import org.yanmark.markoni.domain.entities.Receipt;
import org.yanmark.markoni.domain.entities.User;
import org.yanmark.markoni.domain.models.services.ReceiptServiceModel;
import org.yanmark.markoni.repositories.PackageRepository;
import org.yanmark.markoni.repositories.ReceiptRepository;
import org.yanmark.markoni.repositories.UserRepository;
import org.yanmark.markoni.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReceiptServiceTest {

    @MockBean
    private ReceiptRepository mockReceiptRepository;

    @Autowired
    private ReceiptService receiptService;

    @MockBean
    private PackageRepository mockPackageRepository;

    @MockBean
    private UserRepository mockUserRepository;

    @Test
    public void saveReceipt_whenValidPackageIdAndValidUser_returnReceipt() {
        Package testPackage = TestUtils.getTestPackage();
        User testUser = TestUtils.getTestUser();
        Receipt testReceipt = TestUtils.getTestReceipt();
        when(mockPackageRepository.findById(anyString()))
                .thenReturn(Optional.of(testPackage));
        when(mockPackageRepository.saveAndFlush(any(Package.class)))
                .thenReturn(testPackage);
        when(mockReceiptRepository.saveAndFlush(any(Receipt.class)))
                .thenReturn(testReceipt);
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);

        ReceiptServiceModel result = receiptService.saveReceipt(testPackage.getId(), any());

        assertEquals(testReceipt.getId(), result.getId());
    }

    @Test(expected = Exception.class)
    public void saveReceipt_whenNotValidPackageIdAndValidUser_throwException() {
        Package testPackage = TestUtils.getTestPackage();
        User testUser = TestUtils.getTestUser();
        Receipt testReceipt = TestUtils.getTestReceipt();
        when(mockPackageRepository.findById(anyString()))
                .thenReturn(Optional.of(testPackage));
        when(mockPackageRepository.saveAndFlush(any(Package.class)))
                .thenReturn(testPackage);
        when(mockReceiptRepository.saveAndFlush(any(Receipt.class)))
                .thenReturn(testReceipt);
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);

        ReceiptServiceModel result = receiptService.saveReceipt(anyString(), any());

        assertEquals(testReceipt.getId(), result.getId());
    }

    @Test(expected = Exception.class)
    public void saveReceipt_whenNullPackageIdAndNullUser_throwException() {
        receiptService.saveReceipt(null, null);

        verify(mockReceiptRepository).saveAndFlush(any(Receipt.class));
    }

    @Test(expected = Exception.class)
    public void saveReceipt_whenNullPackageIdAndValidUser_throwException() {
        User testUser = TestUtils.getTestUser();
        when(mockUserRepository.saveAndFlush(any(User.class)))
                .thenReturn(testUser);

        receiptService.saveReceipt(null, any());

        verify(mockReceiptRepository).saveAndFlush(any(Receipt.class));
    }

    @Test(expected = Exception.class)
    public void saveReceipt_whenValidPackageIdAndNullUser_throwException() {
        Package testPackage = TestUtils.getTestPackage();
        when(mockPackageRepository.saveAndFlush(any(Package.class)))
                .thenReturn(testPackage);

        receiptService.saveReceipt(testPackage.getId(), null);

        verify(mockReceiptRepository).saveAndFlush(any(Receipt.class));
    }

    @Test
    public void getReceiptById_whenIdIsValid_returnReceipt() {
        Receipt testReceipt = TestUtils.getTestReceipt();
        when(mockReceiptRepository.findById(anyString()))
                .thenReturn(Optional.of(testReceipt));

        ReceiptServiceModel receiptServiceModel = receiptService.getReceiptById(anyString());

        assertEquals(testReceipt.getId(), receiptServiceModel.getId());
    }

    @Test(expected = Exception.class)
    public void getReceiptById_whenIdIsNotValid_throwException() {
        receiptService.getReceiptById(anyString());

        verify(mockReceiptRepository).findById(anyString());
    }

    @Test
    public void getReceiptByPackage_whenPackageIsValid_returnReceipt() {
        Receipt testReceipt = TestUtils.getTestReceipt();
        when(mockReceiptRepository.findByPakage_Id(anyString()))
                .thenReturn(Optional.of(testReceipt));

        ReceiptServiceModel receiptServiceModel = receiptService.getReceiptByPackage(anyString());

        assertEquals(testReceipt.getId(), receiptServiceModel.getId());
    }

    @Test(expected = Exception.class)
    public void getReceiptByPackage_whenPackageIsNotValid_throwException() {
        receiptService.getReceiptByPackage(anyString());

        verify(mockReceiptRepository).findByPakage_Id(anyString());
    }

    @Test
    public void getAllReceiptByUser_whenUserIsValid_returnReceipts() {
        when(mockReceiptRepository.findAllByRecipient_Username(anyString()))
                .thenReturn(TestUtils.getTestReceipts(3));

        List<ReceiptServiceModel> receiptServiceModels = receiptService.getAllReceiptByUser(anyString());

        assertEquals(3, receiptServiceModels.size());
    }

    @Test
    public void getAllReceiptByUser_whenUserIsNotValid_returnNoReceipts() {
        when(mockReceiptRepository.findAllByRecipient_Username(anyString()))
                .thenReturn(new ArrayList<>());

        List<ReceiptServiceModel> receiptServiceModels = receiptService.getAllReceiptByUser(anyString());

        assertEquals(0, receiptServiceModels.size());
    }
}
