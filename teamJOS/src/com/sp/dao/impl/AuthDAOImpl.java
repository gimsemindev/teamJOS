package com.sp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sp.dao.AuthDAO;
import com.sp.model.AuthDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;

public class AuthDAOImpl implements AuthDAO{
	private Connection conn = DBConn.getConnection();
	
	@Override
	public int insertAdmin(String empNo, String levelCode) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE TB_EMP SET LEVEL_CODE = ? WHERE EMP_NO = ? AND USE_YN = 'Y'";
			
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

	@Override
	public int updateAdmin(AuthDTO admin) throws SQLException{
		
		return 0;
	}

	@Override
	public int deleteAdmin(String adminId) throws SQLException{
		
		return 0;
	}

}
