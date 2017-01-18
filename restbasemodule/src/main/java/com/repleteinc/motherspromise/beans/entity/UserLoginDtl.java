package com.repleteinc.motherspromise.beans.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserLoginDtl implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1531274718079681771L;

	
	private String userLoginDtlId;
	
	private String userTrackId;
	
	public String getUserLoginDtlId() {
		return userLoginDtlId;
	}

	public void setUserLoginDtlId(String userLoginDtlId) {
		this.userLoginDtlId = userLoginDtlId;
	}

	public String getUserTrackId() {
		return userTrackId;
	}

	public void setUserTrackId(String userTrackId) {
		this.userTrackId = userTrackId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	

}
