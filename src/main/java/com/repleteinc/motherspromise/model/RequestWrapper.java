package com.repleteinc.motherspromise.model;

import java.util.List;

import com.repleteinc.motherspromise.beans.TestResponse;

public class RequestWrapper {
	
	String version;
	
	List<TestResponse> testResponsejson;
	
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public List<TestResponse> getTestResponsejson() {
		return testResponsejson;
	}
	
	public void setTestResponsejson(List<TestResponse> testResponsejson) {
		this.testResponsejson = testResponsejson;
	}
	
}
