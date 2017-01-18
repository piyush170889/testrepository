package com.repleteinc.motherspromise.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.beans.entity.OtpDetails;
import com.repleteinc.motherspromise.constants.Constants;
import com.repleteinc.motherspromise.dao.LoginNRegistrationDAO;
import com.repleteinc.motherspromise.exception.ServicesException;

@Service
@Transactional(rollbackFor = Throwable.class)
public class LoginNRegistrationServiceImpl extends Constants implements LoginNRegistrationService {

	@Autowired
	LoginNRegistrationDAO loginNRegistrationDAO;

	private final static Logger logger = Logger.getLogger(LoginNRegistrationServiceImpl.class);

	@Override
	public BaseWrapper doSendOTP(String cellNumber, String otp, String deviceInfo) throws ServicesException, Exception {
		
		if (loginNRegistrationDAO.sendOTP(cellNumber, otp, deviceInfo)) {
			return new BaseWrapper();
		} else {
			throw new ServicesException("608");
		}
	}
	
	@Override
	public BaseWrapper doVerifyOTP(OtpDetails request) throws ServicesException, Exception {
		
		if (loginNRegistrationDAO.verifyOTP(request.getCellNumber(), request.getOtp(), "")) {
			return new BaseWrapper();
		} else {
			throw new ServicesException("608");
		}
	}

	@Override
	public BaseWrapper doSendForgotPasswordLink(String phnNo, HttpServletRequest servletRequest) {
		//TODO: Write Code
		return null;
	}
}
