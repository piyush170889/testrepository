package com.repleteinc.motherspromise.constants;

public class Constants {
	
	public static final String APPLICATION_JSON = "application/json";
	
	public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	
	public static final String BASE_MAPPING = "/";
	
	public static final String BASE_POINTCUT = "execution(* com.repleteinc.*.*.*.*(..))";
	
	public static final String EXCEPTION_POINTCUT = "execution(* com.repleteinc.*.controller.*.*(..))";
	
	public static final String CACHE_POINTCUT = "execution(* com.repleteinc.*.cache.*.*(..))";
	
	public static final String SUCCESS_OK = "OK";

	public static final String MAX_LIMIT = "Max Records";

	public static final String EMAIL_VERIFICATION_TEMPLATE = "Email Verification";

	public static final String EMAIL_VERIFICATION_URI = "/verifyemail?ac=";

	public static final String FILE_SEPARATOR = "/";

	public static final String FILE_NAME_SEPARATOR = "_";
	
	public static final String FILE_IMG_EXT_PNG = ".png";
	
	public static final String UPLOAD_DEST_DROPBOX = "Dropbox";
	
	public static final String SMS_GATEWAY_TWILIO = "Twilio";

	public static final String SMS_GATEWAY_MSSG_91 = "Mssg91";

}
