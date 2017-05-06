package com.repleteinc.motherspromise.beans;

public class UploadFileResponse {

	private String sharedUrl;
	
	private String contentUrl;
	
	public UploadFileResponse(String sharedUrl, String contentUrl) {
		this.sharedUrl = sharedUrl;
		this.contentUrl = contentUrl;
	}

	public String getSharedUrl() {
		return sharedUrl;
	}

	public void setSharedUrl(String sharedUrl) {
		this.sharedUrl = sharedUrl;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

}
