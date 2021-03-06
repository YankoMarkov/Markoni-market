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
    private Set<OrderProductServiceModel> orderProducts;
    private Set<CommentServiceModel> comments;

    public UserServiceModel() {
        this.authorities = new HashSet<>();
        this.packages = new HashSet<>();
        this.receipts = new HashSet<>();
        this.orderProducts = new HashSet<>();
        this.comments = new HashSet<>();
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

    public Set<OrderProductServiceModel> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(Set<OrderProductServiceModel> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Set<CommentServiceModel> getComments() {
        return comments;
    }

    public void setComments(Set<CommentServiceModel> comments) {
        this.comments = comments;
    }
}
