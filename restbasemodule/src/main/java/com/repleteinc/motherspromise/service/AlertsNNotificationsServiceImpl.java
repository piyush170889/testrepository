package com.repleteinc.motherspromise.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.dao.AlertsNNotificationsDAO;
import com.repleteinc.motherspromise.dao.LoginNRegistrationDAO;
import com.repleteinc.motherspromise.exception.ServicesException;

@Service
@Transactional(rollbackFor=Throwable.class)
public class AlertsNNotificationsServiceImpl implements AlertsNNotificationsService {

	@Autowired
	LoginNRegistrationDAO loginDAO;

	@Autowired
	AlertsNNotificationsDAO alertsNNotificationsDAO;

	@Autowired
	Properties configProperties;

	private final static Logger logger = Logger.getLogger(LoginNRegistrationServiceImpl.class);
	
	@Override
	public BaseWrapper doSendMulticastNotification(String message) throws ServicesException, IOException {
		//TODO : Take start index and end index and server key from configuration table
		String startInx = "0";
		String endIdx = "500";
		List<String> regIdsList = alertsNNotificationsDAO.selectAllRegIds(Integer.parseInt(startInx), Integer.parseInt(endIdx));
		
		if(regIdsList.size() > 0) {
			sendMulticastNotification(regIdsList, message);
			return new BaseWrapper();
		} else {
			throw new ServicesException("724");
		}
	}
	
	@Override
	public BaseWrapper doSendNotificationToUser(String message, String mrNo) throws ServicesException, IOException {
		
		/*List<PatientDtls> patientDtlsList = loginDAO.selectPatientDetailsByMrNo(mrNo);  //Get the Android Reg Id of the patient by mrno
		if(patientDtlsList.size() != 1) {
			throw new ServicesException("606");
		} else {
			PatientDtls patientDtls = patientDtlsList.get(0);
			
			 final String REG_ID = patientDtls.getAndr_reg_id().trim();
			
			 logger.info("REG ID : " + REG_ID);
			if(null != REG_ID && !REG_ID.isEmpty()) {
				ArrayList<String> devicesList = new ArrayList<String>();
		        devicesList.add(REG_ID);
		        sendMulticastNotification(devicesList, message);
			}
			*/
			return new BaseWrapper();
//		}
	}
	
	@Override
	public void doSendMulticastNotificationFromList(String message, List<String> biList) {
		
		sendMulticastNotification(biList, message);
	}
	
	/* HELPER METHOD */
	private void sendMulticastNotification(List<String> devicesList, String message) {
		
		try {
		 final String GCM_API_KEY = configProperties.getProperty("gcm.server.apikey");
		 final String MESSAGE_KEY = "message";
		 System.out.println("REG ID : " + devicesList.get(0) + " API KEY : "+ GCM_API_KEY);
		 Sender sender = new Sender(GCM_API_KEY);
		 Message messageToSend = new Message.Builder().timeToLive(30)
	                .delayWhileIdle(true).addData(MESSAGE_KEY, message).build();

       MulticastResult result = sender.send(messageToSend, devicesList, 1);
       System.out.println(result.toString());
		} catch (Exception e) {
			System.out.println("Exception Occured in sendeing notification");
		}
	}
	
}
