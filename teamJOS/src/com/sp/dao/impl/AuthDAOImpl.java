package com.sp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sp.dao.AuthDAO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;

/**
 * <h2>AuthDAOImpl (권한 관리 데이터 접근 구현체)</h2>
 *
 * <p>AuthDAO 인터페이스를 구현한 클래스로, 관리자 계정 권한 부여 및 수정 기능을
 * 실제 데이터베이스와 연동하여 처리합니다.</p>
 *
 * <ul>
 * <li>사원에게 관리자 권한 레벨 부여</li>
 * <li>관리자 권한 레벨 수정</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> AUTH_INS_001 ~ AUTH_UPD_002</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 황선호</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class AuthDAOImpl implements AuthDAO{
	private Connection conn = DBConn.getConnection();

	/**
	 * AUTH_INS_001 : 특정 사원에게 관리자 권한 레벨을 부여합니다.
	 * <p>실제 구현은 사원 테이블(TB_EMP)의 LEVEL_CODE 컬럼을 업데이트하여 권한을 설정합니다.</p>
	 *
	 * @param empNo 권한을 부여할 사원 번호
	 * @param levelCode 부여할 권한 레벨 코드
	 * @return 업데이트된 레코드 수 (1)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int insertAdmin(String empNo, String levelCode) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE /* AUTH_INS_001 */ TB_EMP SET LEVEL_CODE = ? WHERE EMP_NO = ? AND USE_YN = 'Y'";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, levelCode);
			pstmt.setString(2, empNo);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}

		return result;
	}

	/**
	 * AUTH_UPD_002 : 기존 관리자 계정의 권한 레벨을 수정합니다.
	 *
	 * @param levelCode 새로 부여할 권한 레벨 코드
	 * @param empNo 권한을 수정할 사원 번호
	 * @return 업데이트된 레코드 수 (1)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int updateAdmin(String levelCode, String empNo) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = """
					UPDATE /* AUTH_UPD_002 */ TB_EMP SET LEVEL_CODE = ?, EMP_NO = ? AND USE_YN = 'Y'"
					""";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, levelCode);
			pstmt.setString(2, empNo);

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}

		return result;
	}


}