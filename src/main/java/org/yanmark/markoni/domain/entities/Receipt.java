package org.yanmark.markoni.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "receipts")
public class Receipt extends BaseEntity {

    private BigDecimal fee;
    private BigDecimal total;
    private LocalDateTime issuedOn;
    private User recipient;
    private Package pakage;

    @Column(name = "fee", nullable = false)
    public BigDecimal getFee() {
        return this.fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Column(name = "total", nullable = false)
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Column(name = "issued_on", nullable = false)
    public LocalDateTime getIssuedOn() {
        return this.issuedOn;
    }

    public void setIssuedOn(LocalDateTime issuedOn) {
        this.issuedOn = issuedOn;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    public User getRecipient() {
        return this.recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    @OneToOne(targetEntity = Package.class)
    @JoinColumn(name = "package_id", referencedColumnName = "id")
    public Package getPakage() {
        return this.pakage;
    }

    public void setPakage(Package pakage) {
        this.pakage = pakage;
    }
}
