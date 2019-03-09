package markoni.models.services;

import markoni.entities.Role;

import java.util.Set;

public class UserServiceModel {

    private String id;
    private String username;
    private String password;
    private String email;
    private String address;
    private Role role;
    private Set<PackageServiceModel> packages;
    private Set<ReceiptServiceModel> receipts;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<PackageServiceModel> getPackages() {
        return this.packages;
    }

    public void setPackages(Set<PackageServiceModel> packages) {
        this.packages = packages;
    }

    public Set<ReceiptServiceModel> getReceipts() {
        return this.receipts;
    }

    public void setReceipts(Set<ReceiptServiceModel> receipts) {
        this.receipts = receipts;
    }
}
