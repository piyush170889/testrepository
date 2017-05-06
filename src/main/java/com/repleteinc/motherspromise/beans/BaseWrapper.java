package com.repleteinc.motherspromise.beans;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class BaseWrapper {
	
	@JsonProperty("responseMessage")
	private ResponseMessage responseMessage;
	
	public BaseWrapper() {}

	public BaseWrapper(ResponseMessage responseMessage) {

		this.responseMessage = responseMessage;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
}
