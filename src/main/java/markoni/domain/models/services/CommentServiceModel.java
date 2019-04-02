package markoni.domain.models.services;

import java.time.LocalDateTime;

public class CommentServiceModel {
	
	private String id;
	private UserServiceModel user;
	private LocalDateTime time;
	private String comment;
	private ProductServiceModel product;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public UserServiceModel getUser() {
		return this.user;
	}
	
	public void setUser(UserServiceModel user) {
		this.user = user;
	}
	
	public LocalDateTime getTime() {
		return this.time;
	}
	
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	
	public String getComment() {
		return this.comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public ProductServiceModel getProduct() {
		return this.product;
	}
	
	public void setProduct(ProductServiceModel product) {
		this.product = product;
	}
}
