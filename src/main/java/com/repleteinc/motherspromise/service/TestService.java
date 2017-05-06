package com.repleteinc.motherspromise.service;

import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.exception.ServicesException;

public interface TestService {

	BaseWrapper doTest() throws ServicesException;

}
