package com.repleteinc.motherspromise.utils;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Column;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.repleteinc.motherspromise.constants.Constants;
import com.repleteinc.motherspromise.exception.ServicesException;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;

@Component
public class CommonUtility {

	@Autowired
	Properties responseMessageProperties;
	
	@Autowired
	Properties configProperties;
	

	public Date getParsedDate(String dateToParse) throws ParseException{
		 
		String[] formatStrings = new String[] {"M/y", "M/d/y", "M-d-y","dd-mm-yy","dd/mm/yyyy","dd.mm.yyyy","d month yyyy","yyyy-mm-dd"," MMM/DD/YYYY","yyyymmdd","dd-mm-yyyy","DD-MM-YY ","YYYYMMDD","dd-mmm-yyyy HH:MM:SS","yyyy-mm-dd HH:MM:SS",
				"mmm.dd.yyyy HH:MM:SS", "yyyy-MM-DD'T'HH:mm:ss'Z'"};

		System.out.println("date To Parse : " + dateToParse);
		    return DateUtils.parseDateStrictly(dateToParse, formatStrings);
	}
	
	/**
	 * Description : Validates the input parameters for null check and empty
	 * @param Map<Object, Object>
	 * @return Object
	 */
	public Object isInputValid(Map<Object, Object> inputParams) {
		
		Object returnVal = null;
		
		Set<Object> keySet = inputParams.keySet();
		for(Object key : keySet) {
			Object inputParam = inputParams.get(key);
			if(null == inputParam) {
				returnVal = key;
				break;
			} else if ((inputParam instanceof String) && ((String) inputParam).isEmpty()) {
				returnVal = key;
				break;
			}
		}
		
		return returnVal;
	}
	
	/**
	 * Sends specified OTP to the specified contact number
	 * 
	 * @param		contactNumber		The contact number provided on registration form
	 * @param 		otp					The generated otp for the contact number 
	 * @return		isMessageSent		true if message sent successfully. false if message sending failed.
	 */
	public boolean sendOTP(String contactNumber, String otp) throws Exception{
		final String ACCOUNT_SID = "AC151401698d9f4c3ebf63aad80d317bd7";
		final String AUTH_TOKEN = "9319d868794a97d54b389c9238d9e369";
		final String EXT = "+91";												// Exension to be appended to the mmobile number	
		boolean isMessageSent = false;
		final String FROM = "+12052897085";
		try{
			// Create a rest client
		    final TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

		    // Get the main account (The one we used to authenticate the client)
		    final Account mainAccount = client.getAccount();

		    // Send an SMS (Requires version 3.4+)
		    final SmsFactory messageFactory = mainAccount.getSmsFactory();
		    final List<NameValuePair> messageParams = new ArrayList<NameValuePair>();
		    messageParams.add(new BasicNameValuePair("To", EXT + contactNumber.trim()));
		    messageParams.add(new BasicNameValuePair("From", FROM));
		    messageParams.add(new BasicNameValuePair("Body", otp));
		    messageFactory.create(messageParams);		//Send Messsage
		    isMessageSent = true;
		    return isMessageSent;
		}catch (TwilioRestException e) {
			e.printStackTrace();
			throw new Exception(responseMessageProperties.getProperty("error.sms"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(responseMessageProperties.getProperty("error.sms"));
		}
		
	}
	
	
	private final String ACCOUNT_SID = "AC151401698d9f4c3ebf63aad80d317bd7";
	private final String AUTH_TOKEN = "9319d868794a97d54b389c9238d9e369";
	private final String EXT = "+91";
	private final String FROM = "+12052897085";
	
	public boolean sendMessage(String contactNumber, String message) throws Exception {
		String smsGateway = configProperties.getProperty("sms.gateway");
		System.out.println("SMS GATEWAY : " + smsGateway + " SMS Gateway URl P1 : " + configProperties.getProperty("sms.url.part1") + 
				" SMS Gateway URl P2 : " + configProperties.getProperty("sms.url.part2"));
		boolean isMessageSent = false;
		
		switch (smsGateway) {
		case Constants.SMS_GATEWAY_TWILIO :
			try{
				// Create a rest client
			    final TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

			    // Get the main account (The one we used to authenticate the client)
			    final Account mainAccount = client.getAccount();

			    // Send an SMS (Requires version 3.4+)
			    final SmsFactory messageFactory = mainAccount.getSmsFactory();
			    final List<NameValuePair> messageParams = new ArrayList<NameValuePair>();
			    messageParams.add(new BasicNameValuePair("To", EXT + contactNumber.trim()));
//			    messageParams.add(new BasicNameValuePair("From", "(720) 606-4615")); 
			    messageParams.add(new BasicNameValuePair("From", FROM));
			    messageParams.add(new BasicNameValuePair("Body", message));
			    messageFactory.create(messageParams);		//Send Messsage
			    isMessageSent = true;
			    return isMessageSent;
			}catch (TwilioRestException e) {
				e.printStackTrace();
				throw new Exception("Error Occured Sending Message");
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Error Occured Sending Message");
			}
		    
		case Constants.SMS_GATEWAY_MSSG_91 :
			try {
				String finalSMS = configProperties.getProperty("sms.url.part1") + URLEncoder.encode(message,"UTF-8") + 
						configProperties.getProperty("sms.url.part2") + URLEncoder.encode(contactNumber,"UTF-8");
				System.out.println("FINAL SMS : " + finalSMS);
				URL smsURL = new URL(finalSMS);
				InputStreamReader in = new InputStreamReader(smsURL.openStream());
				in.close();
				/*BufferedReader br = new BufferedReader(in);
//				String response = "";
				while(br.readLine() != null) {
					System.out.println(br.readLine());
				}*/
				return true;
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
			
		default:
			return false;
		}
		
	}
	
	/**
	 * Sends email to the toAddress specified, with message body containing the baseURL appended with activate user account page. This forms the activation link for the user's account
	 * 
	 * @param 		toAddress		The email address to which mail is to be sent
	 * @param 		baseURL			The base URl of the application
	 * @return		isEmailSent		true if mail is sent successfully.false if mail sending fails
	 */
	public boolean sendEmail(String toAddress, String message, String subject){
	    final String userName =configProperties.getProperty("email.username");
		
		final String password = configProperties.getProperty("email.password");
		boolean isEmailSent = false;
		try{	
			// sets SMTP server properties
			Properties properties = new Properties();
			properties.put(configProperties.getProperty("smtp.server.host"),configProperties.getProperty("smtp.server.gmail"));
			properties.put("mail.smtp.port",587);
			properties.put("mail.smtp.auth", "true");
			properties.put(configProperties.getProperty("smtp.server.starttls"), configProperties.getProperty("smtp.server.starttls.value"));
			System.out.println(configProperties.getProperty("smtp.server.gmail"));
			// creates a new session with an authenticator
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			};
			Session session = Session.getInstance(properties, auth);
			// creates a new e-mail message
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(userName));
			InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
			mimeMessage.setRecipients(Message.RecipientType.TO, toAddresses);
			mimeMessage.setSubject(subject);
			mimeMessage.setSentDate(new Date());
			
			/*// Create the message part 
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setContent(message,"text/html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
*/
			// Put parts in message
			mimeMessage.setContent(message,"text/html");
			
//			mimeMessage.setText(message);
			
			// sends the e-mail
			Transport.send(mimeMessage);
			isEmailSent = true;
			return isEmailSent;
		}catch(Exception e){
			e.printStackTrace();
			return isEmailSent;
		}
	}
	
	public boolean isUpdated(int rowAffected) {
		if(rowAffected != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isBatchUpdated(int[] rowsAffected) {
		if(rowsAffected.length != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<String> getAnnotatedFieldWithValues(Object objectToCheckValuesFrom) {
		List<String> properties  = new ArrayList<String>();

		try {
			Class<?> clazz = objectToCheckValuesFrom.getClass();
			Field[] fields =  clazz.getDeclaredFields();
			for(Field field : fields) {
				field.setAccessible(true);
				if(field.isAnnotationPresent(Column.class)) {
					String fieldValue = (String) field.get(objectToCheckValuesFrom);
					if(null != fieldValue) {
						properties.add(field.getAnnotation(Column.class).name());
					}
				}
			}
			
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
				
		return properties;
	}
	
	/**
	 * 
	 * @param firstName
	 * @return
	 */
	public String getUniqueIdFromName(String firstName)
	{
		String firstThreeLetters = firstName.trim().substring(0, 3).toUpperCase();
		String uniqueId = firstThreeLetters + ThreadLocalRandom.current().nextInt(999999);
		return uniqueId;
	}
	
	
	public String convertUTCToIST(String utcDateToParse) throws ParseException {
		
		DateFormat sdfUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdfUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		DateFormat sdfIst = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdfIst.setTimeZone(TimeZone.getTimeZone("IST"));
		
		Date utcDate = sdfUtc.parse(utcDateToParse);
//		System.out.println(sdfIst.format(utcDate));
		return sdfIst.format(utcDate);
	}
	
	public String convertISTToUTC(String istDateToParse) throws ParseException {
		
		DateFormat sdfUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdfUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		DateFormat sdfIst = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdfIst.setTimeZone(TimeZone.getTimeZone("IST"));
		
		Date istDate = sdfIst.parse(istDateToParse);
//		System.out.println(sdfIst.format(utcDate));
		return sdfUtc.format(istDate);
	}
	
	public boolean isValidTime(String istTimeToCheck) {
		
		return false;
	}

	public void checkRowUpdated(int rowAffected) {
		if(rowAffected !=1) {
			throw new ServiceException("608");
		}
		
	}

	public void checkUpdate(int otpInsertedRowsAffected) {
		if (otpInsertedRowsAffected != 1) {
			throw new ServiceException("608");
		}
	}
	
	public String getOTP() {
		Random random = new Random() ;
		int otp = random.nextInt(10000);
		return Integer.toString(otp);
	}

	public void checkBatchUpdate(int[] batchUpdatedRows, int batchSize) throws ServicesException {
		for (int batchUpdatedRow : batchUpdatedRows) {
			if(batchUpdatedRow != 1) {
				throw new ServicesException("608");
			}
		}
	}
	
}
