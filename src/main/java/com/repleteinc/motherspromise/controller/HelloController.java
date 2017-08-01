package com.repleteinc.motherspromise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

	
	
	@RequestMapping("hello1")
	String hello(){
		return "hello";
	}
	
	@RequestMapping(value="/")
	public Object defaultTemplate() {
		
		return "default/defaultTemplate";
	}

}
