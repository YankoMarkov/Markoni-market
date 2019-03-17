package markoni.models.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReceiptServiceModel {

    private String id;
    private BigDecimal fee;
    private LocalDateTime issuedOn;
    private UserServiceModel recipient;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
