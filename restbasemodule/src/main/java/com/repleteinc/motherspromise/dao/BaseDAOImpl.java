package com.repleteinc.motherspromise.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.repleteinc.motherspromise.beans.entity.OtpDetails;
import com.repleteinc.motherspromise.beans.entity.UserLoginDtl;
import com.repleteinc.motherspromise.exception.ServicesException;
import com.repleteinc.motherspromise.utils.CommonUtility;
import com.repleteinc.motherspromise.utils.MessageUtility;


@Repository
public class BaseDAOImpl implements BaseDAO{

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	protected Properties sqlProperties;
	
	@Autowired
	protected CommonUtility commonUtility;
	
	@Autowired
	protected MessageUtility messageUtility;
	
	private static final Logger logger=LoggerFactory.getLogger(BaseDAOImpl.class);
	
	@Override
	public String getUUID() throws DataAccessException {
		
		String uuid = jdbcTemplate.query("select uuid()", new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				return rs.getString(1);
			}
		});
		
		return uuid;
	}
	
	public Date convertToSqlDate(String date) throws ServicesException{
		  String pattern = "yyyy-MM-dd";
		    SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return  format.parse(date);
		} catch (ParseException e) {
			throw new ServicesException("611");
		}
	}
	
	public UserLoginDtl getloggedUser() {
		try{
		logger.info(" spring security "+SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal() );
		
		return (UserLoginDtl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	
	}
	
	public String getLoggedInUserId() {
		UserLoginDtl userDetails = (UserLoginDtl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return userDetails.getUserTrackId();
	} 
	
	@Override
	public boolean sendOTP(String cntcNum, String otp, String deviceInfo) throws Exception {
		List<OtpDetails> otpDetailsList = selectOTPDetailsByCntcNum(cntcNum);
		int otpDetailsSize = otpDetailsList.size();
		if(otpDetailsSize == 0) {
			int otpInsertedRowsAffected = insertOTPRecord(cntcNum, otp, deviceInfo); //Insert OTP Record
			commonUtility.checkUpdate(otpInsertedRowsAffected);
		} else if (otpDetailsSize == 1) {
			OtpDetails otpDetails = otpDetailsList.get(0);
			//TODO: Remove hard coded value
			int maxtAllowedAttempts = (int) selectConfigurationValue("maxNoOfAttempts");
			
			//Check max no of attempts
			if (otpDetails.getNumOfAttempts() < maxtAllowedAttempts) { 		//If max attempts not exceeded update the otp record
				int otpRecordUpdated = updateOTPRecordById(otpDetails.getOtpId(), otp, (otpDetails.getNumOfAttempts() + 1));
				commonUtility.checkUpdate(otpRecordUpdated);
			} else {				//If max attempts exceeded throw exception
				throw new ServicesException("628");
			}
		} else {
			throw new ServicesException("622");
		}
		return messageUtility.sendMessage(cntcNum, otp);
	}
	
	public Object selectConfigurationValue(String configName) {
		
		return jdbcTemplate.queryForObject("select CONFIG_VAL from configuration where CONFIG_NAME=?", Integer.class,
				configName);
	}

	@Override
	public boolean verifyOTP(String cntcNum, String otp, String deviceInfo) throws ServicesException, Exception {
		List<OtpDetails> otpDetailsList = selectOTPDetailsByCntcNumOTPAndDeviceInfo(cntcNum, otp, deviceInfo);
		int otpDetailsSize = otpDetailsList.size();
		if(otpDetailsSize == 0) {
			return false;
		} else if (otpDetailsSize == 1) {
			OtpDetails otpDetails = otpDetailsList.get(0);
			//TODO: Check if OTP is Expired
			if(otpDetails.getOtp().trim().equals(otp)) {
			int otpRecordClearedRowAffected = deleteOTPRecord(otpDetails.getOtpId()); //Clear out the OTP record
			commonUtility.checkUpdate(otpRecordClearedRowAffected);
			return true;
			} else {
				return false;
			}
		} else {
			throw new ServicesException("622");
		}
	}
	
	private List<OtpDetails> selectOTPDetailsByCntcNumOTPAndDeviceInfo(String cntcNum, String otp, String deviceInfo) {
		String sql = "select * from otp_dtls where CELLNUMBER=? and DEVICE_INFO=? and OTP=?";
		return jdbcTemplate.query(sql, new Object[] {cntcNum, deviceInfo, otp}, 
				new BeanPropertyRowMapper<OtpDetails>(OtpDetails.class));
	}

	private int deleteOTPRecord(int otpId) {
		String sql = "delete from otp_dtls where OTP_ID=?";
		return jdbcTemplate.update(sql, otpId);
	}

	@Override
	public List<OtpDetails> selectOTPDetailsByCntcNum(String cntcNum) throws ServicesException {
		
		String sql = "select * from otp_dtls where CELLNUMBER=?";
		return jdbcTemplate.query(sql, new Object[] {cntcNum}, new BeanPropertyRowMapper<OtpDetails>(OtpDetails.class));
	}
	
	@Override
	public int insertOTPRecord(String cntcNum, String otp, String deviceInfo) {
		String sql = "insert into otp_dtls(CELLNUMBER,OTP,NUM_OF_ATTEMPTS,DEVICE_INFO) values(?,?,?,?)";
		return jdbcTemplate.update(sql, cntcNum, otp, 1, deviceInfo);
	}
	
	@Override
	public int updateOTPRecordById(int otpId, String otp, int noOfAttempts) {
		String sql = "update otp_dtls set OTP=?,NUM_OF_ATTEMPTS=? where OTP_ID=?";
		return jdbcTemplate.update(sql, otp, noOfAttempts, otpId);
	}
}
