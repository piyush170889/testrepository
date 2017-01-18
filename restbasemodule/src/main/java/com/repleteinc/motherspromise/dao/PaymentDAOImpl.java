package com.repleteinc.motherspromise.dao;

import org.springframework.stereotype.Repository;

import com.repleteinc.motherspromise.utils.UDValues;

@Repository
public class PaymentDAOImpl extends BaseDAOImpl implements PaymentDAO {

	@Override
	public int updatePackagePaymentDetails(String udf1) {
		
		return jdbcTemplate.update("update patient_package_dtls set PAYMENT_STATUS=? where PATIENT_PACKAGE_DTLS_ID=?", UDValues.STATUS_PAID.toString(), udf1);
	}
	

}
