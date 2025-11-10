package com.sp.dao;

import java.sql.SQLException;

import com.sp.model.AuthDTO;

/**
 * <h2>AuthDAO (권한 관리 데이터 접근 인터페이스)</h2>
 *
 * <p>관리자 계정 및 권한(Role) 관련 데이터베이스 접근 기능을 정의하는 인터페이스입니다.</p>
 * 
 * <ul>
 *   <li>관리자 계정 등록, 수정, 삭제</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> AUTH_UPD_001 ~ AUTH_DEL_003</p>
 * 
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 홍길동</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public interface AuthDAO {

    /** AUTH_UPD_001 */
    int insertAdmin(String empNo, String levelCode) throws SQLException;

    /** AUTH_UPD_002 */
    int updateAdmin(AuthDTO admin) throws SQLException;

    /** AUTH_DEL_003 */
    int deleteAdmin(String adminId) throws SQLException;

}
