package com.sp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.sp.dao.LoginDAO; 
import com.sp.model.LoginDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;

public class LoginDAOImpl implements LoginDAO {

    @Override
    public LoginDTO login(String empNo, String pwd) {
        LoginDTO loginInfo = null; 
        Connection conn = DBConn.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        

        try {
        	sql = "SELECT e.EMP_NO, e.EMP_NM, g.GRADE_NM, e.LEVEL_CODE "
        			+ "FROM TB_EMP e "
        			+ "LEFT JOIN TB_GRADE g ON e.GRADE_CD = g.GRADE_CD "
        			+ "WHERE e.EMP_NO = ? AND e.PWD = ? AND e.USE_YN = 'Y'";
        	
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, empNo);
            pstmt.setString(2, pwd);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                loginInfo = new LoginDTO(
                    rs.getString("EMP_NO"),
                    rs.getString("EMP_NM"),
                    rs.getString("GRADE_NM"),
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