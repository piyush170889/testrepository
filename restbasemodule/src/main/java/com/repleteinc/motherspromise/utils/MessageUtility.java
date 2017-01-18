package com.repleteinc.motherspromise.utils;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.repleteinc.motherspromise.constants.Constants;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;

@Component
public class MessageUtility {

	@Autowired
	Properties configProperties;
	
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
	
}
