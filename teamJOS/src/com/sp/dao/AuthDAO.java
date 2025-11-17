package com.sp.dao;

import java.sql.SQLException;


/**
 * <h2>AuthDAO (권한 관리 데이터 접근 인터페이스)</h2>
 *
 * <p>관리자 계정 및 권한(Role) 관련 데이터베이스 접근 기능을 정의하는 인터페이스입니다.</p>
 *
 * <ul>
 * <li>관리자 계정 등록 및 권한 부여</li>
 * <li>관리자 계정 권한 수정</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> AUTH_INS_001 ~ AUTH_UPD_002</p>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 황선호</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public interface AuthDAO {

    /** * AUTH_INS_001 : 특정 사원을 관리자로 등록하고 권한 레벨을 부여합니다.
     * @param empNo 관리자로 등록할 사원 번호
     * @param levelCode 부여할 권한 레벨 코드
     * @return 등록된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int insertAdmin(String empNo, String levelCode) throws SQLException;

    /** * AUTH_UPD_002 : 기존 관리자 계정의 권한 레벨을 수정합니다.
     * @param empNo 권한을 수정할 관리자 사원 번호
     * @param levelCode 새로 부여할 권한 레벨 코드
     * @return 수정된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int updateAdmin(String empNo, String levelCode) throws SQLException;


}