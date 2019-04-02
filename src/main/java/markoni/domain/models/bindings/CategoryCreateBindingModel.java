package markoni.domain.models.bindings;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryCreateBindingModel {
	
	private String name;
	
	@NotNull(message = "Name cannot be null.")
	@Size(min = 3, max = 15, message = "Name must be in range [3 - 15] symbols.")
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
