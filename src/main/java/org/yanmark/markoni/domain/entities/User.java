package org.yanmark.markoni.domain.entities;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
public class User extends BaseEntity implements UserDetails {

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    private String username;
    private String password;
    private String email;
    private String address;
    private Set<UserRole> authorities;
    private Set<Package> packages;
    private Set<Receipt> receipts;
    private Set<Order> orders;
    private Set<Comment> comments;

    public User() {
        this.authorities = new HashSet<>();
        this.packages = new HashSet<>();
        this.receipts = new HashSet<>();
        this.orders = new HashSet<>();
        this.comments = new HashSet<>();
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }

    @Column(name = "username", nullable = false, unique = true)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "address", nullable = false)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @OneToMany(targetEntity = Package.class, mappedBy = "recipient", cascade = CascadeType.ALL)
    public Set<Package> getPackages() {
        return this.packages;
    }

    public void setPackages(Set<Package> packages) {
        this.packages = packages;
    }

    @OneToMany(targetEntity = Receipt.class, mappedBy = "recipient", cascade = CascadeType.ALL)
    public Set<Receipt> getReceipts() {
        return this.receipts;
    }

    public void setReceipts(Set<Receipt> receipts) {
        this.receipts = receipts;
    }

    @OneToMany(targetEntity = Order.class, mappedBy = "customer", cascade = CascadeType.ALL)
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @OneToMany(targetEntity = Comment.class, mappedBy = "user", cascade = CascadeType.ALL)
    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    @ManyToMany(targetEntity = UserRole.class, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    public Set<UserRole> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
    }
}
