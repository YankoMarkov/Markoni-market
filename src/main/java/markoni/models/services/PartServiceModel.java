package markoni.models.services;

import java.math.BigDecimal;
import java.util.Set;

public class PartServiceModel {

    private String id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private Set<ProductServiceModel> products;

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

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<ProductServiceModel> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductServiceModel> products) {
        this.products = products;
    }
}
