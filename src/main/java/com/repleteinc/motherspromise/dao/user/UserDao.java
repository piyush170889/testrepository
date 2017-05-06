package com.repleteinc.motherspromise.dao.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.repleteinc.motherspromise.beans.entity.UserLoginDtl;

public interface UserDao extends UserDetailsService {
	UserLoginDtl findByName(String name);
	
	boolean updateUserAndroidRegId(String patientId, String andRegId);
}