package markoni.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Part extends BaseEntity {
	
	private String name;
	private BigDecimal price;
	private Integer quantity;
	private Set<Product> products;
	
	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "price", nullable = false)
	public BigDecimal getPrice() {
		return this.price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@Column(name = "quantity", nullable = false)
	public Integer getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@ManyToMany
	public Set<Product> getProducts() {
		return this.products;
	}
	
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
}
