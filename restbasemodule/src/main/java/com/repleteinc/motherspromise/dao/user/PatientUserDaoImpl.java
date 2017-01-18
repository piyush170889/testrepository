package com.repleteinc.motherspromise.dao.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.repleteinc.motherspromise.beans.entity.UserLoginDtl;
import com.repleteinc.motherspromise.dao.BaseDAOImpl;

public class PatientUserDaoImpl extends BaseDAOImpl implements PatientUserDao {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(" in patient dtls service");
		String sql = "SELECT u.EMAIL_ID name,u.password pass,u.TRACK_ID, md.DISPLAY_TEXT role FROM patient_login_dtls u INNER JOIN master_data md on md.MASTER_DATA_ID=u.ROLES_CD WHERE u.EMAIL_ID = ?";
		List<UserLoginDtl> userLogin=jdbcTemplate.query(sql, new Object[]{username},
		new RowMapper<UserLoginDtl>() {
			@Override
			public UserLoginDtl mapRow(ResultSet rs, int rowNum) throws SQLException {
				 UserLoginDtl user = new UserLoginDtl();
	             return user;
			}
		});
		if (userLogin.isEmpty()) {
			throw new UsernameNotFoundException("The user with name "
					+ username + " was not found");
		}else{
			return userLogin.get(0);
		}
	
	}
	
	@Override
	public boolean updateUserAndroidRegId(String patientId, String andRegId) {
		String sql = "update patientdtls set andr_reg_id=? where patient_id=?";
		int rowAffected = jdbcTemplate.update(sql, andRegId, patientId);
		if(rowAffected != 1) {
			return false;
		} else {
			return true;
		}
	}

}
