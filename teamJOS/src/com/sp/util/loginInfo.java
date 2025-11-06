package com.sp.util;

import db.user.MemberDTO;

public class LoginInfo {
	private MemberDTO loginMember = null;
	
	public MemberDTO loginMember() {
		return loginMember;
	}
	
	public void login(MemberDTO loginMember) {
		this.loginMember = loginMember;
	}
	
	public void logout() {
		loginMember = null;
	}
}