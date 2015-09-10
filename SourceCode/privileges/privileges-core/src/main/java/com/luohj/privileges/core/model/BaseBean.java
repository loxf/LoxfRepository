package com.luohj.privileges.core.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseBean implements Serializable {
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
