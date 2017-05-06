package com.repleteinc.motherspromise.dao;

import com.repleteinc.motherspromise.beans.TestResponse;

public interface TestDAO extends BaseDAO {

	TestResponse selectTestValues();
	
}
