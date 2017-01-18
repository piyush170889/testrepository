package com.repleteinc.motherspromise.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.beans.entity.OtpDetails;
import com.repleteinc.motherspromise.exception.ServicesException;
import com.repleteinc.motherspromise.service.LoginNRegistrationService;

@RestController
public class LoginNRegistrationController {

	private final static Logger logger = Logger.getLogger(LoginNRegistrationController.class);

	@Autowired
	private LoginNRegistrationService loginNRegistrationService;


	/**
	 * Description : Webservice to send otp to a number
	 * 
	 * @param {@link
	 * 			OTPRequest}
	 * @return {@link ServiceResponse}
	 * @throws {@link
	 *             ServicesException}
	 */
	@RequestMapping(value = "/sendotp", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Object sendOTP(@RequestBody OtpDetails request) throws Exception {

		return loginNRegistrationService.doSendOTP(request.getCellNumber(), request.getOtp(), "");
	}

	/**
	 * Description : Verified the OTP submitted with the otp details against the
	 * device info and cell number
	 * 
	 * @param {@link OTPRequest}
	 * @return {@link ServiceResponse}
	 * @throws {@link
	 *             ServicesException}
	 */
	@RequestMapping(value = "/verifyotp", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Object verifyOTP(@RequestBody OtpDetails request) throws Exception {

		return loginNRegistrationService.doVerifyOTP(request);
	}

	@RequestMapping(value = "/forgotpassword", method = RequestMethod.GET)
	public Object sendForgotPasswordLink(@RequestParam("phnno") String phnNo, HttpServletRequest servletRequest)
			throws ServicesException, Exception {

		return loginNRegistrationService.doSendForgotPasswordLink(phnNo, servletRequest);
	}
	
}