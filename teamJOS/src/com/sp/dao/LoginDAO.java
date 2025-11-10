package com.sp.dao;

import com.sp.model.LoginDTO;

/**
 * <h2>LoginDAO (로그인 데이터 접근 인터페이스)</h2>
 *
 * <p>사원 로그인 기능과 관련된 데이터베이스 접근 기능을 정의하는 인터페이스입니다.</p>
 * 
 * <ul>
 *   <li>사원 로그인 검증</li>
 *   <li>로그인 정보 조회</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> LOGIN_SER_001</p>
 * 
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 홍길동</p>
 * <p><b>작성일:</b> 2025-11-10</p>
 * <p><b>버전:</b> 1.0</p>
 */
public interface LoginDAO {
	/** LOGIN_SER_001 */
	public LoginDTO login(String empNo, String pwd);
}
