package com.repleteinc.motherspromise.dao.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.repleteinc.motherspromise.beans.entity.UserLoginDtl;
import com.repleteinc.motherspromise.dao.BaseDAOImpl;

@Repository
public class JpaUserDao extends BaseDAOImpl implements UserDao {

	private static Logger logger = LoggerFactory.getLogger(JpaUserDao.class);

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("In loadUserByUsername");
		UserLoginDtl user = this.findByName(username);
		if (null == user) {
			throw new UsernameNotFoundException("The user with name " + username + " was not found");
		}

		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public UserLoginDtl findByName(String name) {
		System.out.println(" in find by name " + name);
		String sql = "SELECT u.patient_id,u.email_id name,u.mobile_password pass,u.role,u.mr_no mrNo,u.patient_name,u.patient_phone FROM patientdtls u WHERE u.email_id = ?";
		List<UserLoginDtl> UserLoginDtl = jdbcTemplate.query(sql, new Object[] { name }, new RowMapper<UserLoginDtl>() {
			@Override
			public UserLoginDtl mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserLoginDtl user = new UserLoginDtl();
				
				return user;
			}
		});
		if (UserLoginDtl.isEmpty()) {
			throw new UsernameNotFoundException("The user with name " + name + " was not found");
		} else {
			return UserLoginDtl.get(0);
		}
	}

	public UserLoginDtl getloggedUser() {
		return (UserLoginDtl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
