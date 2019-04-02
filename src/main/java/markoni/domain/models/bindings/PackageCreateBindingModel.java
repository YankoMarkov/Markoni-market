package markoni.domain.models.bindings;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PackageCreateBindingModel {
	
	private String recipient;
	
	@NotNull(message = "Recipient cannot be null.")
	@NotBlank(message = "Recipient cannot be empty.")
	public String getRecipient() {
		return this.recipient;
	}
	
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
}
