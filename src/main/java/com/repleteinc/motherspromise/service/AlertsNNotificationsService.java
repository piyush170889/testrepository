package com.repleteinc.motherspromise.service;

import java.io.IOException;
import java.util.List;

import com.repleteinc.motherspromise.beans.BaseWrapper;
import com.repleteinc.motherspromise.exception.ServicesException;

public interface AlertsNNotificationsService {

	BaseWrapper doSendMulticastNotification(String message) throws ServicesException, IOException;

	BaseWrapper doSendNotificationToUser(String message, String mrNo) throws ServicesException, IOException;

	void doSendMulticastNotificationFromList(String string, List<String> notificationList);
}
