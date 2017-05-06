package com.repleteinc.motherspromise.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.exception.ServicesException;
import com.repleteinc.motherspromise.service.AlertsNNotificationsService;

@RestController
public class AlertsNNotificationsController {

	private final static Logger logger = Logger.getLogger(AlertsNNotificationsController.class);

	@Autowired
	private AlertsNNotificationsService alertsNNotificationsService;

	
	/**
	 * Description : Test API for sending Notification
	 * @param regid
	 * @param serid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/nottest", method=RequestMethod.GET)
	public Object notTest(@RequestParam(value="regid") String regid, @RequestParam(value="serid") String serid) throws Exception {
		try {
			 final String GCM_API_KEY = serid;
			 final String MESSAGE_VALUE = "Test Notification 21";    
			 final String MESSAGE_KEY = "message";
			 final String REG_ID = regid;
			 
//			 loginService.doSendNotificationToUser(message, mrNo);
			Sender sender = new Sender(GCM_API_KEY);
			ArrayList<String> devicesList = new ArrayList<String>();
	        devicesList.add(REG_ID);
			Message message = new Message.Builder().timeToLive(30)
	                .delayWhileIdle(true).addData(MESSAGE_KEY, MESSAGE_VALUE).build();

	        MulticastResult result = sender.send(message, devicesList, 1);
	        System.out.println(result.toString());
	        
			return new BaseWrapper();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
		
	}
	
	@RequestMapping(value="/sendmulticastnotification", method=RequestMethod.GET)
	public Object sendMulticastNotifications(@RequestParam(value="message", required=true) String message) throws ServicesException, IOException {
		
		return alertsNNotificationsService.doSendMulticastNotification(message);
	}

	@RequestMapping(value="/sendnotification", method=RequestMethod.GET)
	public Object sendNotificationsToUser(@RequestParam(value="message", required=true) String message,
			@RequestParam(value="uid", required=true) String uid) throws ServicesException, IOException {
		
		return alertsNNotificationsService.doSendNotificationToUser(message, uid);
	}
	
}
