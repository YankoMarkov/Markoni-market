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
    private List<ProductServiceModel> products;
    private List<CommentServiceModel> comments;

    public UserServiceModel() {
        this.authorities = new HashSet<>();
        this.packages = new HashSet<>();
        this.receipts = new HashSet<>();
        this.products = new ArrayList<>();
        this.comments = new ArrayList<>();
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

    public Set<UserRoleServiceModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserRoleServiceModel> authorities) {
        this.authorities = authorities;
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

    public List<ProductServiceModel> getProducts() {
        return this.products;
    }

    public void setProducts(List<ProductServiceModel> products) {
        this.products = products;
    }

    public List<CommentServiceModel> getComments() {
        return this.comments;
    }

    public void setComments(List<CommentServiceModel> comments) {
        this.comments = comments;
    }
}
