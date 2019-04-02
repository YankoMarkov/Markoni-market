package org.yanmark.markoni.domain.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
	
	private String id;
	
	@Id
	@GeneratedValue(generator = "uuid-system")
	@GenericGenerator(name = "uuid-system", strategy = "uuid")
	@Column(name = "id", unique = true, nullable = false, updatable = false)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
