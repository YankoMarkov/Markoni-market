package markoni.domain.models.views;

public class ShippedViewModel {
	
	private String id;
	private String description;
	private Double weight;
	private String estimatedDeliveryDay;
	private String recipient;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
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
