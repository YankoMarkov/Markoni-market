package markoni.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product extends BaseEntity {
	
	private String name;
	private String image;
	private String description;
	private BigDecimal price;
	private List<Part> parts;
	private Category category;
	
	public Product() {
		this.parts = new ArrayList<>();
	}
	
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
	
	@Column(name = "description", columnDefinition = "TEXT")
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "price", nullable = false)
	public BigDecimal getPrice() {
		return this.price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@ManyToMany(targetEntity = Part.class)
	@JoinTable(name = "products_parts",
			joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id"))
	public List<Part> getParts() {
		return this.parts;
	}
	
	public void setParts(List<Part> parts) {
		this.parts = parts;
	}
	
	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	public Category getCategory() {
		return this.category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
}
