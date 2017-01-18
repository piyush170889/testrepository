package com.repleteinc.motherspromise.service;

import javax.servlet.http.HttpServletRequest;

import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.beans.entity.OtpDetails;
import com.repleteinc.motherspromise.exception.ServicesException;

public interface LoginNRegistrationService {

	BaseWrapper doSendOTP(String cellNumber, String otp, String string) throws ServicesException, Exception;

	BaseWrapper doVerifyOTP(OtpDetails request) throws ServicesException, Exception;

	BaseWrapper doSendForgotPasswordLink(String phnNo, HttpServletRequest servletRequest);

}
