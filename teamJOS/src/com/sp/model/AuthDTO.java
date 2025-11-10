package com.sp.model;

public class AuthDTO {
	private String adminId;
	private String pwd;
	private String levelCode;
	
	public AuthDTO(String adminId, String pwd, String levelCode) {
        this.adminId = adminId;
        this.pwd = pwd;
        this.levelCode = levelCode;
    }
	
	public String getAdminId() {
		return adminId;
	}
	
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	
}
