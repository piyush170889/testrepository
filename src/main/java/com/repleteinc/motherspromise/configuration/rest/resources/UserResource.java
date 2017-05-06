package com.repleteinc.motherspromise.configuration.rest.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.repleteinc.motherspromise.beans.LoginResponseWrapper;
import com.repleteinc.motherspromise.beans.ResponseMessage;
import com.repleteinc.motherspromise.beans.entity.UserLoginDtl;
import com.repleteinc.motherspromise.configuration.rest.transfer.UserTransfer;
import com.repleteinc.motherspromise.exception.ServicesException;
import com.repleteinc.motherspromise.exception.UnAuthorizedException;

@Controller
public class UserResource {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserDetailsService ptntUserDetailsService;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@Autowired
	@Qualifier("patientAuthenticationManager")
	private AuthenticationManager ptntAuthManager;

	@Autowired
	Properties configProperties;

	@Autowired
	Properties responseMessageProperties;

	static final Logger logger = LoggerFactory.getLogger(UserResource.class);

	/**
	 * Retrieves the currently logged in user.
	 * 
	 * @return A transfer containing the username and the roles.
	 * @throws ServicesException
	 */

	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public UserTransfer getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
			logger.info("user not found exception");
			throw new UnAuthorizedException();
		}
		UserDetails userDetails = (UserDetails) principal;

		System.out.println(" User details " + userDetails.toString());
		return new UserTransfer(userDetails.getUsername(), this.createRoleMap(userDetails));
	}

	/**
	 * Authenticates a user and creates an authentication token.
	 * 
	 * @param username
	 *            The name of the user.
	 * @param password
	 *            The password of the user.
	 * @return A transfer containing the authentication token.
	 * @throws ServicesException
	 */

	@RequestMapping(value = "security/authenticate", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public @ResponseBody Object authenticate(@RequestParam("username") String username,
			@RequestParam("password") String password) throws ServicesException {
		logger.info("User detail .." + username + ".." + password);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		Authentication authentication = this.authManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		/*
		 * Reload user as password of authentication principal will be null
		 * after authorization and password is needed for token generation
		 */
		// UserDetails userDetails =
		// this.userDetailsService.loadUserByUsername(username);
		UserLoginDtl userLoginDtl = (UserLoginDtl) this.userDetailsService.loadUserByUsername(username);
		if (null == userLoginDtl) {
			//TODO: Send proper Authentication Message
			throw new ServicesException("608");
		} 
		
		ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK.toString(),
				responseMessageProperties.getProperty("615"), configProperties.getProperty("api.version"));
		LoginResponseWrapper loginResponse = new LoginResponseWrapper(responseMessage);
		return loginResponse;
		// return new TokenTransfer(TokenUtils.createToken(userDetails));
	}

	@RequestMapping(value = "authenticate-patient", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public @ResponseBody Object authenticatePatient(@RequestParam("username") String username,
			@RequestParam("password") String password) throws ServicesException {
		logger.info("User detail .." + username + ".." + password);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		Authentication authentication = this.ptntAuthManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		/*
		 * Reload user as password of authentication principal will be null
		 * after authorization and password is needed for token generation
		 */
		UserLoginDtl userLoginDtl = (UserLoginDtl) this.ptntUserDetailsService.loadUserByUsername(username);
		if (null == userLoginDtl) {
			throw new ServicesException(" ");
		}

//		this.ptntUserDetailsService
		ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK.toString(),
				responseMessageProperties.getProperty("615"), configProperties.getProperty("api.version"));
		LoginResponseWrapper loginResponse = new LoginResponseWrapper(responseMessage);
		return loginResponse;
		// return new TokenTransfer(TokenUtils.createToken(userDetails));
	}

	private Map<String, Boolean> createRoleMap(UserDetails userDetails) {
		Map<String, Boolean> roles = new HashMap<String, Boolean>();
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			roles.put(authority.getAuthority(), Boolean.TRUE);
		}

		return roles;
	}

}