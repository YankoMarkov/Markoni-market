package markoni.models.services;

import markoni.entities.Status;

import java.time.LocalDateTime;
import java.util.List;

public class PackageServiceModel {

    private String id;
    private String description;
    private Double weight;
    private String shippingAddress;
    private Status status;
    private LocalDateTime estimatedDeliveryDay;
    private UserServiceModel recipient;
    private ReceiptServiceModel receipt;
    private List<ProductServiceModel> products;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getWeight() {
        return this.weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getShippingAddress() {
        return this.shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getEstimatedDeliveryDay() {
        return this.estimatedDeliveryDay;
    }

    public void setEstimatedDeliveryDay(LocalDateTime estimatedDeliveryDay) {
        this.estimatedDeliveryDay = estimatedDeliveryDay;
    }

    public UserServiceModel getRecipient() {
        return this.recipient;
    }

    public void setRecipient(UserServiceModel recipient) {
        this.recipient = recipient;
    }

    public ReceiptServiceModel getReceipt() {
        return this.receipt;
    }

    public void setReceipt(ReceiptServiceModel receipt) {
        this.receipt = receipt;
    }

    public List<ProductServiceModel> getProducts() {
        return this.products;
    }

    public void setProducts(List<ProductServiceModel> products) {
        this.products = products;
    }
}
