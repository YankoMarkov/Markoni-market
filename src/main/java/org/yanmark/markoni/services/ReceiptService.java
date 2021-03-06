package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.models.services.ReceiptServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;

import java.util.List;

public interface ReceiptService {

    ReceiptServiceModel saveReceipt(String id, UserServiceModel userService);

    ReceiptServiceModel getReceiptById(String id);

    ReceiptServiceModel getReceiptByPackage(String packageId);

    List<ReceiptServiceModel> getAllReceiptByUser(String username);
}
