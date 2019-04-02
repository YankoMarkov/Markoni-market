package markoni.domain.models.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceModel {
	
	private String id;
	private String name;
	private String image;
	private Double weight;
	private String description;
	private BigDecimal price;
	private List<CommentServiceModel> comments;
	private CategoryServiceModel category;
	
	public ProductServiceModel() {
		this.comments = new ArrayList<>();
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImage() {
		return this.image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public Double getWeight() {
		return this.weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public BigDecimal getPrice() {
		return this.price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public List<CommentServiceModel> getComments() {
		return this.comments;
	}
	
	public void setComments(List<CommentServiceModel> comments) {
		this.comments = comments;
	}
	
	public CategoryServiceModel getCategory() {
		return this.category;
	}
	
	public void setCategory(CategoryServiceModel category) {
		this.category = category;
	}
}
