package com.repleteinc.motherspromise.service;

import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.beans.TestResponse;
import com.repleteinc.motherspromise.exception.ServicesException;

public interface TestService {

	BaseWrapper doTest() throws ServicesException;
	
	int deleteTest(int id);
	
	void InsertTest(TestResponse t1);
	int updateTest(TestResponse t1);
}
