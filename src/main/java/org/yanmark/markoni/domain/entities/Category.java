package org.yanmark.markoni.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "categories")
public class Category extends BaseEntity {

    private String name;

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
