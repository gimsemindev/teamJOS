package com.sp.util;

import com.sp.model.LoginDTO;

/**
 * <h2>LoginInfo (로그인 세션 정보 관리 싱글턴)</h2>
 *
 * <p>현재 로그인된 사용자(사원 또는 관리자)의 정보를 시스템 전반에서
 * 접근하고 관리하기 위한 싱글턴(Singleton) 클래스입니다.
 * 세션 관리 역할을 수행하며, 로그인 DTO를 저장, 조회, 삭제(로그아웃)하는 기능을 제공합니다.</p>
 *
 * <ul>
 * <li>로그인된 사용자 정보 ({@code LoginDTO})를 저장합니다.</li>
 * <li>저장된 로그인 정보를 조회합니다.</li>
 * <li>로그아웃 시 저장된 정보를 초기화합니다.</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 황선호</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class LoginInfo {
    
    /** 현재 로그인된 사용자 정보를 담는 필드 */
	private LoginDTO loginMember = null;
	
    /**
     * 현재 로그인된 사용자 정보를 반환합니다.
     *
     * @return 로그인된 사용자 정보 DTO (로그인 상태가 아니면 {@code null})
     */
	public LoginDTO loginMember() {
		return loginMember;
	}
	
    /**
     * 사용자를 시스템에 로그인시키고, 해당 정보를 {@code LoginInfo}에 저장합니다.
     *
     * @param loginMember 로그인 처리에 사용될 {@code LoginDTO} 객체
     */
	public void login(LoginDTO loginMember) {
		this.loginMember = loginMember;
	}
	
    /**
     * 현재 사용자를 로그아웃 처리하고, 저장된 로그인 정보를 초기화합니다.
     */
	public void logout() {
		loginMember = null;
	}
}