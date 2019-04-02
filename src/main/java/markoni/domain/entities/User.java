package markoni.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
public class User extends BaseEntity {
	
	private String username;
	private String password;
	private String email;
	private String address;
	private Role role;
	private Set<Package> packages;
	private Set<Receipt> receipts;
	private List<Product> products;
	private Set<Comment> comments;
	
	public User() {
		this.packages = new HashSet<>();
		this.receipts = new HashSet<>();
		this.products = new ArrayList<>();
		this.comments = new HashSet<>();
	}
	
	@Column(name = "username", nullable = false, unique = true)
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "password", nullable = false)
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "email", nullable = false, unique = true)
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "address", nullable = false)
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	public Role getRole() {
		return this.role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	@OneToMany(targetEntity = Package.class,
			mappedBy = "recipient",
			fetch = FetchType.EAGER)
	public Set<Package> getPackages() {
		return this.packages;
	}
	
	public void setPackages(Set<Package> packages) {
		this.packages = packages;
	}
	
	@OneToMany(targetEntity = Receipt.class,
			mappedBy = "recipient",
			fetch = FetchType.EAGER)
	public Set<Receipt> getReceipts() {
		return this.receipts;
	}
	
	public void setReceipts(Set<Receipt> receipts) {
		this.receipts = receipts;
	}
	
	@ManyToMany(targetEntity = Product.class,
			fetch = FetchType.EAGER)
	@JoinTable(name = "users_products",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
	public List<Product> getProducts() {
		return this.products;
	}
	
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	@OneToMany(targetEntity = Comment.class,
			mappedBy = "user",
			fetch = FetchType.EAGER)
	public Set<Comment> getComments() {
		return this.comments;
	}
	
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
}
