package com.repleteinc.motherspromise.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.beans.TestResponse;
import com.repleteinc.motherspromise.exception.ServicesException;
import com.repleteinc.motherspromise.service.TestService;

@RestController
public class TestController {

	@Autowired
	private TestService testService;
//	private TestResponse tesst;
	
	public static Logger logger = Logger.getLogger(TestController.class);
	
	@RequestMapping(value="test", method=RequestMethod.GET)
	public Object test() throws ServicesException {
		
		logger.info("Inside Test Method");
		return testService.doTest();
	}
	
	
	@RequestMapping(value="testGetPath/{name}", method=RequestMethod.GET)
	public Object test1(@PathVariable("name") String name) {
		logger.info("Inside hello Method");
		System.out.println("in controller" +name);
		return name;
	}
	
	@RequestMapping(value="testGetParam", method=RequestMethod.GET)
	public Object test3(@RequestParam("name") String name) {
		logger.info("Inside hello Method");
		System.out.println("in controller" +name);
		return name;
	}
	
	
	@RequestMapping(value = "testPost", method=RequestMethod.POST)
	public String test2(@Valid @RequestBody TestResponse t1 ) {
		logger.info("Inside hello Method");
		System.out.println(t1.getId());
		System.out.println(t1.getEmail());
		System.out.println(t1.getName());
	//	System.out.println("in controller" +name);
		return "name";
	}
	

	/*@RequestMapping(value="testDeletePath/{name}", method=RequestMethod.DELETE)
	public Object test4(@PathVariable("name") String name) throws ServicesException {
		logger.info("Inside hello Method");
		
		
		System.out.println("in controller" +name);
		return name;
	}*/
	@RequestMapping(value = "testDeletePath/{id}", method = RequestMethod.DELETE)
	public int delete(@PathVariable("id")int id) {
		testService.deleteTest(id);
	  		return id;
	}
	
	@RequestMapping(value = "testInsert", method = RequestMethod.POST)
	public String InsertTest(@RequestBody TestResponse t1){
		testService.InsertTest(t1);
		return "Insertion Successfull";
	}

	@RequestMapping(value = "testUpdate", method = RequestMethod.POST)
	public String updateTest(@RequestBody TestResponse t1){
		testService.updateTest(t1);
		return "Updation Successfull";
	}

	
}
