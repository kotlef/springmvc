package com.ynswet.system.sc.model;

import java.io.Serializable;
import java.util.List;

public class UserAuthorizationInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 角色
	 */
	private List<String> roles;
	/*
	 * 权限 
	 */
	private List<String>  perms;
	
	
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public List<String> getPerms() {
		return perms;
	}
	public void setPerms(List<String> perms) {
		this.perms = perms;
	}

	

}
