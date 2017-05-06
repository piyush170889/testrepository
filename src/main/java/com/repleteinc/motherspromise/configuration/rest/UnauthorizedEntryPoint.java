package com.repleteinc.motherspromise.configuration.rest;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	Properties configProperties;
	
	@Autowired
	Properties responseMessageProperties;
	
	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		String errorJson = "{ "
				+ "\"responseMessage\": { "
				+ "\"status\": \""+ HttpStatus.UNAUTHORIZED.toString() + "\""
				+ ",\"message\": \"" + responseMessageProperties.getProperty("616") + "\""
				+ ",\"apiVersion\": \"" + configProperties.getProperty("api.version") 
				+ "\"} "
				+ "}";
		  
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    response.getOutputStream().println(errorJson);
	}

}