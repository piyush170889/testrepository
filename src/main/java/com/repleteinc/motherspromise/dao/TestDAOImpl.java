package com.repleteinc.motherspromise.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;
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
		String sqlInsert = "insert into tbl1 values(?,?,?,?)";
		
		
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, t1.getId());
				ps.setString(2, t1.getName());
				ps.setString(3, t1.getLastname());
				ps.setString(4, t1.getEmail());
				
				return ps;
			}
		};
		
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		
		int n = jdbcTemplate.update(psc, holder);
		
		int generatedPrimaryKey = holder.getKey().intValue();
		System.out.println("generatedPrimaryKey : " +generatedPrimaryKey);
		
		
		//int n = jdbcTemplate.update(sqlInsert, t1.getId(),t1.getName(),t1.getLastname(), t1.getEmail());
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
	@Override
	public  TestResponse testSelectValues(String id) {
		/*String sql = "select * from tbl1 where id=?";
		return jdbcTemplate.execute(sql, id);*/
		
		TestResponse res = jdbcTemplate.query(sqlProperties.getProperty("select.tbl.selected"), new Object[] {id},  
				new RowMapper<TestResponse>() {
					@Override
					public TestResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
						TestResponse testResponse = new TestResponse();
						testResponse.setId(rs.getString("id"));
						testResponse.setName(rs.getString("name"));
						testResponse.setEmail(rs.getString("email"));
						testResponse.setLastname(rs.getString("lastname"));
						
						return testResponse;
					}
				}).get(0);
		System.out.println("id : "+res.getId());
		System.out.println("name : "+res.getName());
		return res;
		
	}
}
