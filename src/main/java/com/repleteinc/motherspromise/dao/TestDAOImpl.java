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
	public int deleteTestValues(int id) {
		String sql = "delete from tbl1 where id=?";
		return jdbcTemplate.update(sql, id);
	}
	
	@Override
	public boolean InsertTest(TestResponse t1) {
		String SqlInsert = "insert into tbl1 values(?,?,?,?)";
		int n = jdbcTemplate.update(SqlInsert, t1.getId(),t1.getName(),t1.getLastname(), t1.getEmail());
		if(n>0)
		{
			return true;
		}
		else
			return false;
	}
	@Override
	public int updateTestValue(TestResponse t1) {
		String sqlUpdate = "update tbl1 SET name = ?, lastname = ?, email =? where id =?";
		int update = jdbcTemplate.update(sqlUpdate,t1.getName(),t1.getLastname(),t1.getEmail(),t1.getId());  
		return update;
	}
}
