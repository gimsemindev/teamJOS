package com.sp.util;

import com.sp.model.LoginDTO;

public class LoginInfo {
	private LoginDTO loginMember = null;
	
	public LoginDTO loginMember() {
		return loginMember;
	}
	
	public void login(LoginDTO loginMember) {
		this.loginMember = loginMember;
	}
	
	public void logout() {
		loginMember = null;
	}
}