package com.repleteinc.motherspromise.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.repleteinc.motherspromise.configuration.rest.AuthenticationTokenProcessingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder PasswordEncoder;

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private UserDetailsService ptntUserDetailsService;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(ptntUserDetailsService).passwordEncoder(PasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// handle patient panel http requests
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint).and()
				.addFilterBefore(new AuthenticationTokenProcessingFilter(ptntUserDetailsService),
						UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/rest/*/*").permitAll()
				.and().csrf().disable();
	}

}
