package com.repleteinc.motherspromise.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseMessage {

	@JsonProperty("status")
	private String status;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("apiVersion")
	private String apiVersion;
	
	public ResponseMessage() {}

	public ResponseMessage(String status, String message, String apiVersion) {
		this.status = status;
		this.message = message;
		this.apiVersion = apiVersion;
	}

	public String getApiVersion() {
		return apiVersion;
	}


	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
