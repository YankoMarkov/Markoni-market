package org.yanmark.markoni.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "packages")
public class Package extends BaseEntity {
	
	private String description;
	private Double weight;
	private String shippingAddress;
	private Status status;
	private LocalDateTime estimatedDeliveryDay;
	private User recipient;
	
	@Column(name = "description", nullable = false)
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "weight", nullable = false)
	public Double getWeight() {
		return this.weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	@Column(name = "shipping_address", nullable = false)
	public String getShippingAddress() {
		return this.shippingAddress;
	}
	
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	public Status getStatus() {
		return this.status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Column(name = "estimated_delivery_day", nullable = false)
	public LocalDateTime getEstimatedDeliveryDay() {
		return this.estimatedDeliveryDay;
	}
	
	public void setEstimatedDeliveryDay(LocalDateTime estimatedDeliveryDay) {
		this.estimatedDeliveryDay = estimatedDeliveryDay;
	}
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "recipient_id", referencedColumnName = "id")
	public User getRecipient() {
		return this.recipient;
	}
	
	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}
}
