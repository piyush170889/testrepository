package com.repleteinc.motherspromise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repleteinc.motherspromise.service.TestInterface;

@RestController
public class TestController {

	@Autowired
	private TestInterface testInterface;
	
	@RequestMapping(value="dotest", method=RequestMethod.GET)
	public TestClass dotest(@RequestParam(value="varone") String varOne, @RequestParam(value="vartwo") String varTwo) {
		
		return testInterface.doTest(varOne, varTwo);
	}
	
	@RequestMapping(value="add", method=RequestMethod.GET)
	public int add(@RequestParam(value="a") int a, @RequestParam(value="b") int b) {
		
		int c = a+b;
		
		return c;
	}
}
