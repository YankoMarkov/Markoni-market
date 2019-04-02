package org.yanmark.markoni.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment extends BaseEntity {
	
	private User user;
	private LocalDateTime time;
	private String comment;
	private Product product;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "time", nullable = false)
	public LocalDateTime getTime() {
		return this.time;
	}
	
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	
	@Column(name = "comment", nullable = false)
	public String getComment() {
		return this.comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	public Product getProduct() {
		return this.product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
}
