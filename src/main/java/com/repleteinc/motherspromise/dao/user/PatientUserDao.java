package com.repleteinc.motherspromise.dao.user;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface PatientUserDao extends UserDetailsService {

	boolean updateUserAndroidRegId(String patientId, String andRegId);
}
