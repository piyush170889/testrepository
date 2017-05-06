package com.repleteinc.motherspromise.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.repleteinc.motherspromise.beans.TestResponse;

@Repository
public class TestDAOImpl extends BaseDAOImpl implements TestDAO {

	@Override
	public TestResponse selectTestValues() {

		return jdbcTemplate.query(sqlProperties.getProperty("select.tbl1.all"), 
				new BeanPropertyRowMapper<TestResponse>(TestResponse.class)).get(0);
	}

}
