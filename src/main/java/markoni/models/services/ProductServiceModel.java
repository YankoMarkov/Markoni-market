package markoni.models.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class ProductServiceModel {

    private String id;
    private String name;
    private String image;
    private BigDecimal price;
    private List<PartServiceModel> parts;
    private CategoryServiceModel category;
    private Set<PackageServiceModel> packages;

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

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<PartServiceModel> getParts() {
        return this.parts;
    }

    public void setParts(List<PartServiceModel> parts) {
        this.parts = parts;
    }

    public CategoryServiceModel getCategory() {
        return this.category;
    }

    public void setCategory(CategoryServiceModel category) {
        this.category = category;
    }

    public Set<PackageServiceModel> getPackages() {
        return this.packages;
    }

    public void setPackages(Set<PackageServiceModel> packages) {
        this.packages = packages;
    }
}
