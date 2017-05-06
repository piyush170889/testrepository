package com.repleteinc.motherspromise.beans.entity;

public class OtpDetails {

	private int otpId;
	
	private String otp;

	private int numOfAttempts;
	
	private String cellNumber;
	
	public OtpDetails() {}
	
	public String getCellNumber() {
		return cellNumber;
	}

	public void setCellNumber(String cellNumber) {
		this.cellNumber = cellNumber;
	}

	public int getOtpId() {
		return otpId;
	}

	public void setOtpId(int otpId) {
		this.otpId = otpId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public void setNumOfAttempts(int numOfAttempts) {
		this.numOfAttempts = numOfAttempts;
	}

	public int getNumOfAttempts() {
		return this.numOfAttempts;
	}
	
}
