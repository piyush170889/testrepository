package com.repleteinc.motherspromise.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.beans.ResponseMessage;
import com.repleteinc.motherspromise.beans.TestResponse;
import com.repleteinc.motherspromise.exception.ServicesException;
import com.repleteinc.motherspromise.model.RequestWrapper;
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
	/*
DELETE RECORD
	*/
	@RequestMapping(value = "testDeletePath/{id}", method = RequestMethod.DELETE)
	public int delete(@PathVariable("id")int id) {
		testService.deleteTest(id);
	  		return id;
	}
	
	/*INSERT RECORD*/
	
	@RequestMapping(value = "testInsert", method = RequestMethod.POST)
	public String InsertTest(@RequestBody TestResponse t1){
		testService.InsertTest(t1);
		return "Insertion Successfull";
	}

   /*UPDATE RECORDS*/ 	
	
	@RequestMapping(value = "testUpdate", method = RequestMethod.POST)
	public String updateTest(@RequestBody TestResponse t1){
		testService.updateTest(t1);
		return "Updation Successfull";
	}
	
	/* FETCH SELECTED RECORD 
*/
	@RequestMapping(value="testSelect/{id}", method=RequestMethod.GET)
	public String testSelect(@PathVariable("id")String id) throws ServicesException {
		System.out.println("ID......."+id);
		logger.info("Inside Test Method");
		testService.doTestSelect(id);
		return "Select REcord successfull";
	}
	
/* PASS JSPN ARRAY 
*/	
	@RequestMapping(value = "testArray", method = RequestMethod.POST)
	public String arrayTest(@RequestBody List<TestResponse> testResponceList){
		for (TestResponse t1: testResponceList ){
			System.out.println(t1.getName());
			testService.InsertTest(t1);
		}
		
		//testService.updateTest(t1);
		return "Array Insertion Successfull";
	}
	
	/*MULTIPLE JSONN OBJECTS*/
	
	@RequestMapping(value = "testMultipleJSONObjects", method = RequestMethod.POST)
	public  ResponseEntity<BaseWrapper> testMultipleJSONObjects(@RequestBody RequestWrapper requestWrapper){
		
		System.out.println("requestWrapper.getVersion() : " +requestWrapper.getVersion());
		for (TestResponse t1: requestWrapper.getTestResponsejson()){
			System.out.println(t1.getName());
			//testService.InsertTest(t1);
		}
		ResponseMessage baseWrapper = new ResponseMessage();
		baseWrapper.setStatus("Done");
		baseWrapper.setApiVersion("18.18.18");
		baseWrapper.setMessage("Thnak YOu for your Response");
		//testService.updateTest(t1);
		return new ResponseEntity<BaseWrapper>(HttpStatus.OK);
	}
	
	
	
/*
	@RequestMapping(value = "testArrayPathVatiable", method = RequestMethod.POST)
	public String arrayTestPathVatiable(@RequestParam String[] a){
		for (int i=0;i<a.length;i++){
			System.out.println(a[i]);
		}
		
		//testService.updateTest(t1);
		return "Array Insertion Successfull";
	}
*/
	
	
	
	
}
