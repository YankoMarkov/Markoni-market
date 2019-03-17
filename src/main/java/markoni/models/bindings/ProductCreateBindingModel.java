package markoni.models.bindings;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductCreateBindingModel {
	
	private String name;
	private String image;
	private String description;
	private BigDecimal price;
	private String category;
	
	@NotNull(message = "Name cannot be null.")
	@Size(min = 3, max = 30, message = "Name must be in range [3 - 30] symbols.")
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message = "Image cannot be null.")
	@NotBlank(message = "Image cannot be empty.")
	@Size(max = 250, message = "Image is too long.")
	public String getImage() {
		return this.image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	@NotNull(message = "Description cannot be null.")
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@NotNull(message = "Price cannot be null.")
	@DecimalMin(value = "0.001", message = "Price must be greater than 0")
	public BigDecimal getPrice() {
		return this.price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@NotNull(message = "Category cannot be null.")
	@NotBlank(message = "Category cannot be empty.")
	public String getCategory() {
		return this.category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
}
