package org.yanmark.markoni.domain.models.services;

public class UserRoleServiceModel extends BaseServiceModel {

    private String authority;

    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
