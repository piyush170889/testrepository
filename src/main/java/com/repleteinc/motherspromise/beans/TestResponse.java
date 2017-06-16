package com.repleteinc.motherspromise.beans;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class TestResponse extends BaseWrapper {

	@NotNull(message="701")
	@NotEmpty(message="702")
	private String id;
	
	@NotNull(message="888")
	@NotEmpty(message="888")
	private String name;
	
	private String lastname;
	
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	private String email;
	
	public TestResponse(String id, String name, String lastname, String email) {
		this.id=id;
		this.name=name;
		this.email = email;
		this.lastname = lastname;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
