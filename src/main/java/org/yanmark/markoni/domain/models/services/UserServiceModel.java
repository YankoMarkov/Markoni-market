package org.yanmark.markoni.domain.models.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserServiceModel extends BaseServiceModel {

    private String username;
    private String password;
    private String email;
    private String address;
    private Set<UserRoleServiceModel> authorities;
    private Set<PackageServiceModel> packages;
    private Set<ReceiptServiceModel> receipts;
    private Set<ProductServiceModel> products;
    private List<CommentServiceModel> comments;

    public UserServiceModel() {
        this.authorities = new HashSet<>();
        this.packages = new HashSet<>();
        this.receipts = new HashSet<>();
        this.products = new HashSet<>();
        this.comments = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<UserRoleServiceModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserRoleServiceModel> authorities) {
        this.authorities = authorities;
    }

    public Set<PackageServiceModel> getPackages() {
        return packages;
    }

    public void setPackages(Set<PackageServiceModel> packages) {
        this.packages = packages;
    }

    public Set<ReceiptServiceModel> getReceipts() {
        return receipts;
    }

    public void setReceipts(Set<ReceiptServiceModel> receipts) {
        this.receipts = receipts;
    }

    public Set<ProductServiceModel> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductServiceModel> products) {
        this.products = products;
    }

    public List<CommentServiceModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentServiceModel> comments) {
        this.comments = comments;
    }
}
