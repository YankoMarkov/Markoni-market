package markoni.domain.models.views;

import markoni.domain.entities.Status;

public class PackageDetailsViewModel {
	
	private String description;
	private Double weight;
	private String shippingAddress;
	private Status status;
	private String estimatedDeliveryDay;
	private String recipient;
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Double getWeight() {
		return this.weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	public String getShippingAddress() {
		return this.shippingAddress;
	}
	
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getEstimatedDeliveryDay() {
		return this.estimatedDeliveryDay;
	}
	
	public void setEstimatedDeliveryDay(String estimatedDeliveryDay) {
		this.estimatedDeliveryDay = estimatedDeliveryDay;
	}
	
	public String getRecipient() {
		return this.recipient;
	}
	
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
}
