package com.repleteinc.motherspromise.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AlertsNNotificationsDAOImpl extends BaseDAOImpl implements AlertsNNotificationsDAO {

	
	@Override
	public List<String> selectAllRegIds(int startIdx, int endIdx) {

		return jdbcTemplate.query(sqlProperties.getProperty(""), 
				new Object[] {startIdx, endIdx}, new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						
						return rs.getString("andr_reg_id");
					}
				});
	}
}
