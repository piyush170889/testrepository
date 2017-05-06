package com.repleteinc.motherspromise.utils;

public enum UDValues {

	BOOLEAN_TRUE("true"),
	BOOLEAN_FALSE("false"),
	STATUS_ACTIVE("Active"),
	STATUS_INACTIVE("Inactive"),
	STATUS_BOOKED("Booked"),
	STATUS_PENDING("Pending"),
	STATUS_COMPLETED("Completed"),
	STATUS_ORDERED("Ordered"),
	STATUS_DISPATCHED("Dispatched"),
	STATUS_CANCELLED("Cancelled"),
	STATUS_RAISED("Raised"),
	STATUS_ASSIGNED("Assigned"),
	STATUS_RESCHEDULE("Reschedule"), 
	STATUS_REASSIGNED("Reassigned"), 
	STATUS_INITIATED("Initiated"),
	STATUS_YES("Y"),
	STATUS_NO("N"), 
	STATUS_PAID("Paid"),
	STATUS_SUCCESS("Success"), 
	STATUS_CONFIRMED("Confirmed"),
	PAYMENT_MODE_ONLINE("Online"),
	PAYMENT_MODE_COD("COD"),
	PAYMENT_MODE_COA("COA"),
	USER_TYPE_STUDENT("Student"),
	USER_TYPE_TUTOR("Tutor"),
	USER_TYPE_ADMIN("Admin"),
	USER_TYPE_PATIENT("Patient"),
	METHOD_GET("GET"),
	METHOD_POST("POST"),
	APPT_TYPE_CNSLT("Consultation"),
	APPT_TYPE_TEST("Test"),
	APPT_TYPE_SRGRY("Surgery"), 
	PROVIDER_CNSLTNT("ConsultationsProvider"),
	PROVIDER_VITAL_PARAM("VitalParamProvider"),
	PROVIDER_DIAG("DIAGProvider");
	
		
	private String udValue;
	
	UDValues(String udvalue) {
		this.udValue = udvalue;
	}
	
	@Override
	public String toString() {
		
		return this.udValue.toString();
	}
}
