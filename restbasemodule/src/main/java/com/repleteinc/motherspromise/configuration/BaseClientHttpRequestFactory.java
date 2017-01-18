package com.repleteinc.motherspromise.configuration;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class BaseClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

private final HostnameVerifier hostNameVerifier;

public BaseClientHttpRequestFactory (final HostnameVerifier hostNameVerifier) {
    this.hostNameVerifier = hostNameVerifier;
}

@Override
protected void prepareConnection(final HttpURLConnection connection, final String httpMethod)
    throws IOException {
    if (connection instanceof HttpsURLConnection) {
        ((HttpsURLConnection) connection).setHostnameVerifier(hostNameVerifier);
        ((HttpsURLConnection) connection).setSSLSocketFactory(initSSLContext()
            .getSocketFactory());
    }
    super.prepareConnection(connection, httpMethod);
}

private SSLContext initSSLContext() {
    try {
    	 TrustManager[] trustAllCerts = new TrustManager[] { new MyTrustManager() };

 	    // Install the all-trusting trust manager
 	    SSLContext sc = SSLContext.getInstance("SSL");
 	    sc.init(null, trustAllCerts, new java.security.SecureRandom());
 	    HostnameVerifier allHostsValid = new HostnameVerifier() {
 	        public boolean verify(String hostname, SSLSession session) {
 	            return true;
 	        }
 	    };
 	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
 	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        return sc;
    } catch (final Exception ex) {
        return null;
    }
}
}

class MyTrustManager implements X509TrustManager 
{
	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	   return null;
	}
	
	public void checkClientTrusted(X509Certificate[] certs, String authType) {
	}
	
	public void checkServerTrusted(X509Certificate[] certs, String authType) {
	}
	
	@Override
	 public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {}
	
	@Override
	public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {}
}