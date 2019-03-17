package markoni.models.bindings;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PackageCreateBindingModel {
	
	private String description;
	private Double weight;
	private String shippingAddress;
	private String recipient;
	
	@NotNull(message = "Description cannot be null.")
	@Size(min = 3, max = 200, message = "Description must be in range [3 - 200] symbols.")
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@NotNull(message = "Weight cannot be null.")
	@DecimalMin(value = "1", message = "Weight must be minimum 1 kilo.")
	public Double getWeight() {
		return this.weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	@NotNull(message = "Password cannot be null.")
	@Size(min = 3, max = 30, message = "Description must be in range [3 - 30] symbols.")
	public String getShippingAddress() {
		return this.shippingAddress;
	}
	
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	
	@NotNull(message = "Recipient cannot be null.")
	@NotBlank(message = "Recipient cannot be empty.")
	public String getRecipient() {
		return this.recipient;
	}
	
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
}
