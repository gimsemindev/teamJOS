package com.sp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.sp.dao.LoginDAO; 
import com.sp.model.LoginDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;

/**
 * <h2>LoginDAOImpl (로그인 데이터 접근 구현체)</h2>
 *
 * <p>LoginDAO 인터페이스를 구현한 클래스로, 사원 인증 및 로그인 정보 조회 기능을
 * 실제 데이터베이스와 연동하여 처리합니다.</p>
 *
 * <ul>
 * <li>사원 번호와 비밀번호를 이용한 로그인 인증</li>
 * <li>로그인 성공 시 사원 기본 정보(이름, 직급, 부서, 권한 레벨) 조회</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> LOGIN_SER_001</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 황선호</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class LoginDAOImpl implements LoginDAO {

    /**
     * LOGIN_SER_001 : 사원 번호와 비밀번호를 이용하여 로그인 유효성을 검증하고, 유효한 경우 로그인 정보를 조회합니다.
     *
     * @param empNo 사원 번호
     * @param pwd 비밀번호
     * @return 로그인 성공 시 LoginDTO 객체, 실패 시 null
     */
    @Override
    public LoginDTO login(String empNo, String pwd) {
        LoginDTO loginInfo = null; 
        Connection conn = DBConn.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        

        try {
        	sql = """
        			SELECT /* LOGIN_SEL_001 */ e.EMP_NO, e.EMP_NM, g.GRADE_NM, e.LEVEL_CODE, DEPT_CD
        			FROM TB_EMP e 
        			LEFT JOIN TB_GRADE g ON e.GRADE_CD = g.GRADE_CD 
        			WHERE e.EMP_NO = ? AND e.PWD = ?  AND e.USE_YN = 'Y'
        			""";
        	
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, empNo);
            pstmt.setString(2, pwd);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                loginInfo = new LoginDTO(
                    rs.getString("EMP_NO"),
                    rs.getString("EMP_NM"),
                    rs.getString("GRADE_NM"),
                    rs.getString("DEPT_CD"),
                    rs.getString("LEVEL_CODE")
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return loginInfo;
    }
}