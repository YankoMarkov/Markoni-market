package markoni.models.services;

import java.util.Set;

public class CategoryServiceModel {

    private String id;
    private String name;
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

    public Set<ProductServiceModel> getProducts() {
        return this.products;
    }

    public void setProducts(Set<ProductServiceModel> products) {
        this.products = products;
    }
}
