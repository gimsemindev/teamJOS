package com.sp.model;

/**
 * <h2>AuthDTO (관리자 인증 및 권한 DTO)</h2>
 *
 * <p>관리자 로그인 인증 및 권한 레벨 정보를 담는 Data Transfer Object 입니다.</p>
 *
 * <ul>
 * <li>관리자 식별 ID 및 비밀번호</li>
 * <li>관리자 권한 레벨 코드</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_ROLE</p>
 * <p><b>작성자:</b> 황선호</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class AuthDTO {
    
    /** 관리자 아이디 (ADMIN_ID) */
	private String adminId;
    
    /** 비밀번호 (PWD) */
	private String pwd;
    
    /** 권한 레벨 코드 (LEVEL_CODE - 예: 10, 20 등) */
	private String levelCode;
	
    /**
     * 전체 필드 생성자
     *
     * @param adminId 관리자 아이디
     * @param pwd 비밀번호
     * @param levelCode 권한 레벨 코드
     */
	public AuthDTO(String adminId, String pwd, String levelCode) {
        this.adminId = adminId;
        this.pwd = pwd;
        this.levelCode = levelCode;
    }
	
    /** @return 관리자 아이디 */
	public String getAdminId() {
		return adminId;
	}
	
    /** @param adminId 관리자 아이디 */
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
    
    /** @return 비밀번호 */
	public String getPwd() {
		return pwd;
	}
    
    /** @param pwd 비밀번호 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
    
    /** @return 권한 레벨 코드 */
	public String getLevelCode() {
		return levelCode;
	}
    
    /** @param levelCode 권한 레벨 코드 */
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	
}