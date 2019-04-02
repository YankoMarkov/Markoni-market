package org.yanmark.markoni.domain.models.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReceiptServiceModel extends BaseServiceModel {

    private BigDecimal fee;
    private LocalDateTime issuedOn;
    private UserServiceModel recipient;
    private PackageServiceModel aPackage;

    public BigDecimal getFee() {
        return this.fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public LocalDateTime getIssuedOn() {
        return this.issuedOn;
    }

    public void setIssuedOn(LocalDateTime issuedOn) {
        this.issuedOn = issuedOn;
    }

    public UserServiceModel getRecipient() {
        return this.recipient;
    }

    public void setRecipient(UserServiceModel recipient) {
        this.recipient = recipient;
    }

    public PackageServiceModel getaPackage() {
        return this.aPackage;
    }

    public void setaPackage(PackageServiceModel aPackage) {
        this.aPackage = aPackage;
    }
}
