package markoni.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity(name = "categories")
public class Category extends BaseEntity {
	
	private String name;
	private Set<Product> products;
	
	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany
	public Set<Product> getProducts() {
		return this.products;
	}
	
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
}
