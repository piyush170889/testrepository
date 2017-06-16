package com.repleteinc.motherspromise.dao;

import com.repleteinc.motherspromise.beans.TestResponse;

public interface TestDAO extends BaseDAO {

	TestResponse selectTestValues();
	int deleteTestValues(int id);
	boolean InsertTest(TestResponse t1);
	int updateTestValue(TestResponse t1);
	
}
