package com.repleteinc.motherspromise.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repleteinc.motherspromise.controller.TestClaass2;
import com.repleteinc.motherspromise.controller.TestClass;

@Service
//@Transactional(rollbackFor=Throwable.class)
public class TestInterfaceImpl implements TestInterface {

	@Override
	public TestClass doTest(String varOne, String varTwo) {
		TestClaass2 testClass2 = getTestClass2Object();
		
		TestClass testClass = new TestClass();
		testClass.setVarOne(varOne);
		testClass.setVarTwo(varTwo);
		testClass.setTsTestClaass2(testClass2);
		
		return testClass;
	}

	private TestClaass2 getTestClass2Object() {
		TestClaass2 testClass2 = new TestClaass2();
		testClass2.setVarFour("Four");
		testClass2.setVarThree("Three");
		return testClass2;
	}

}
