package com.repleteinc.motherspromise.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.beans.TestResponse;
import com.repleteinc.motherspromise.dao.TestDAO;
import com.repleteinc.motherspromise.exception.ServicesException;

@Service
@Transactional(rollbackFor=Throwable.class)
public class TestServiceImpl implements TestService {

	@Autowired
	private TestDAO testDAO;
	
	public static Logger logger = Logger.getLogger(TestService.class);
	
	@Override
	public BaseWrapper doTest() throws ServicesException {
		/*TestResponse res = new TestResponse("1", "Asmita"); 
		return res;*/
//		throw new ServicesException("888");
		TestResponse res = testDAO.selectTestValues();
		return res;
	}
	public int deleteTest(int id){
		int result = testDAO.deleteTestValues(id);
//		return null;
		return result;
		
	}
	
	@Override
	public void InsertTest(TestResponse t1) {
		
		boolean b = testDAO.InsertTest(t1);
		
	}
	@Override
	public int updateTest(TestResponse t1) {
		// TODO Auto-generated method stub
		 int res = testDAO.updateTestValue( t1);
		 return res;
		
	}
	
	
	
}
