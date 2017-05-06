package com.repleteinc.motherspromise.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public class PaymentUtility {

	public String getTransactionId() {
		
		   String txnid ="";
	       Random rand = new Random();
	       String rndm = Integer.toString(rand.nextInt())+(System.currentTimeMillis() / 1000L);
	       txnid=hashCal("SHA-256",rndm).substring(0,20);
	       return txnid;
		}
		
	private String hashCal(String type, String str) {
		byte[] hashseq=str.getBytes();
	       StringBuffer hexString = new StringBuffer();
	       try{
	           MessageDigest algorithm = MessageDigest.getInstance(type);
	           algorithm.reset();
	           algorithm.update(hashseq);
	           byte messageDigest[] = algorithm.digest();

	           for (int i=0;i<messageDigest.length;i++) {
	               String hex=Integer.toHexString(0xFF & messageDigest[i]);
	               if(hex.length()==1) hexString.append("0");
	               hexString.append(hex);
	           }

	       }catch(NoSuchAlgorithmException nsae){ }

	       return hexString.toString();
	}
	
	public String calculateHash( MultiValueMap<String, String> params, String salt){
	       String hash="";
	       String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
	       String hashString="";

	       try{
	           if( empty(params.get("key").toString())
	                   || empty(params.get("txnid").get(0).toString())
	                   || empty(params.get("amount").get(0).toString())
	                   /*|| empty(params.get("firstname").get(0).toString())*/
	                   /*|| empty(params.get("email").get(0).toString())*/
	                   || empty(params.get("phone").get(0).toString())
	                   || empty(params.get("productinfo").get(0).toString())
	                   || empty(params.get("surl").get(0).toString())
	                   || empty(params.get("furl").get(0).toString())
	                   /*|| empty(params.get("service_provider").get(0).toString())*/
	                   )
	           {
	               throw new Exception("Required Values are empty");
	           }else{
	               String[] hashVarSeq=hashSequence.split("\\|");

	               for(String part : hashVarSeq)
	               {
	                   hashString= (empty(params.get(part).get(0).toString()))?hashString.concat(""):hashString.concat(params.get(part).get(0).toString());
	                   hashString=hashString.concat("|");
	               }
	               hashString=hashString.concat(salt);

	               hash=hashCal("SHA-512",hashString);
	           }

	       }catch(Exception e){
	           e.printStackTrace();
	       }
	       return hash;
	   }
	 
	 private boolean empty(String s)
	   {
	       if(s== null || s.trim().equals(""))
	           return true;
	       else
	           return false;
	   }
}
