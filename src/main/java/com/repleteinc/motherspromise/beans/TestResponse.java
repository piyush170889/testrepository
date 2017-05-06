package com.repleteinc.motherspromise.beans;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class TestResponse extends BaseWrapper {

	@NotNull(message="888")
	@NotEmpty(message="888")
	private String id;
	
	private String name;
	
	public TestResponse(String id, String name) {
		this.id=id;
		this.name=name;
	}
	
	public TestResponse() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
