package markoni.domain.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "categories")
public class Category extends BaseEntity {
	
	private String name;
	private Set<Product> products;
	
	public Category() {
		this.products = new HashSet<>();
	}
	
	@Column(name = "name", nullable = false, unique = true)
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(targetEntity = Product.class,
			mappedBy = "category",
			fetch = FetchType.EAGER)
	public Set<Product> getProducts() {
		return this.products;
	}
	
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
}
