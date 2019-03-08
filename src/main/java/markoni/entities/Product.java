package markoni.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Product extends BaseEntity {
	
	private String name;
	private String image;
	private BigDecimal price;
	private List<Part> parts;
	private Category category;
	
	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "image")
	public String getImage() {
		return this.image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	@Column(name = "price", nullable = false)
	public BigDecimal getPrice() {
		return this.price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@ManyToMany
	public List<Part> getParts() {
		return this.parts;
	}
	
	public void setParts(List<Part> parts) {
		this.parts = parts;
	}
	
	@ManyToOne
	public Category getCategory() {
		return this.category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
}
