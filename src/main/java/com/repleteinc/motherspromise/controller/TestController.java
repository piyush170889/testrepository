package com.repleteinc.motherspromise.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.repleteinc.motherspromise.exception.ServicesException;
import com.repleteinc.motherspromise.service.TestService;

@RestController
public class TestController {

	@Autowired
	private TestService testService;
	
	public static Logger logger = Logger.getLogger(TestController.class);
	
	@RequestMapping(value="test", method=RequestMethod.GET)
	public Object test() throws ServicesException {
		
		logger.info("Inside Test Method");
		return testService.doTest();
	}
}
