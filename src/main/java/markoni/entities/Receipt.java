package markoni.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Receipt extends BaseEntity {

    private BigDecimal fee;
    private LocalDateTime issuedOn;
    private User recipient;
    private Package aPackage;

    @Column(name = "fee", nullable = false)
    public BigDecimal getFee() {
        return this.fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Column(name = "issued_on", nullable = false)
    public LocalDateTime getIssuedOn() {
        return this.issuedOn;
    }

    public void setIssuedOn(LocalDateTime issuedOn) {
        this.issuedOn = issuedOn;
    }

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    public User getRecipient() {
        return this.recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    @OneToOne(mappedBy = "receipt")
    public Package getaPackage() {
        return this.aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }
}