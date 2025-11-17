package com.sp.dao;

import com.sp.model.LoginDTO;

/**
 * <h2>LoginDAO (로그인 데이터 접근 인터페이스)</h2>
 *
 * <p>사원 로그인 기능과 관련된 데이터베이스 접근 기능을 정의하는 인터페이스입니다.</p>
 *
 * <ul>
 * <li>사원 로그인 정보 검증</li>
 * <li>로그인 성공 시 사원 정보 조회</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> LOGIN_SER_001</p>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 황선호</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public interface LoginDAO {
	/** * LOGIN_SER_001 : 사원 번호와 비밀번호를 이용하여 로그인 유효성을 검증하고, 유효한 경우 로그인 정보를 조회합니다.
	 * @param empNo 사원 번호
	 * @param pwd 비밀번호
	 * @return 로그인에 성공하면 LoginDTO 객체, 실패하면 null
	 */
	public LoginDTO login(String empNo, String pwd);
}