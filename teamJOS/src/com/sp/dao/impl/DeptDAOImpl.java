package com.sp.dao.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sp.dao.DeptDAO;
import com.sp.model.DeptDTO;
import com.sp.model.DeptMemberCountDTO;
import com.sp.model.EmployeeDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;


public class DeptDAOImpl implements DeptDAO{
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private Connection conn = DBConn.getConnection();
	
	@Override	
    public int insertDept(DeptDTO dept) throws SQLException {
        int result = 0;
		PreparedStatement pstmt = null;
		String sql;
				
        try {
            sql = "INSERT /* DEPT_INS_001 */ INTO TB_DEPT "
                    + "(DEPT_CD, DEPT_NM, EXT_NO, SUPER_DEPT_CD, USE_YN, REG_DT) "
                    + "VALUES (?, ?, ?, ?, ?, SYSDATE)";

     		pstmt = conn.prepareStatement(sql);
        	
            pstmt.setString(1, dept.getDeptCd());
            pstmt.setString(2, dept.getDeptNm());
            pstmt.setString(3, dept.getExtNo());
            pstmt.setString(4, dept.getSuperDeptCd());
            pstmt.setString(5, dept.getUseYn());

            result = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			DBUtil.close(pstmt);			
		}
        return result;
    }	
	
	
	@Override
	public int updateDept(DeptDTO dept) throws SQLException{
        int result = 0;
        PreparedStatement pstmt = null;
        String sql;

        // UPDATE 테이블명 SET 컬럼=값, 컬럼=값 WHERE 조건
        try {                        
            sql = "  UPDATE employee " 
            	  +"    SET name = ? , birth = TO_DATE(?, 'YYYY-MM-DD'), tel = ? "	     
            	  +"		 WHERE sabeon = ? ";
            pstmt= conn.prepareStatement(sql);
            
            pstmt.setString(1, dept.getDeptCd());


            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            DBUtil.close(pstmt);
        }
        
        return result;
	}

	@Override
	public int deleteDept(int deptNo) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<DeptDTO> selectAllDept() {
		List<DeptDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		// member1과 member2를 id를 기준으로 LEFT OUTER JOIN 하여 전체 레코드 반환
		
		try {
			sql = " SELECT /* DEPT_SEL_003 */ "
					+ "      DEPT_CD "
					+ "    , CASE WHEN CONNECT_BY_ISLEAF = 1 THEN "
					+ "            LPAD(' ', (LEVEL-1)*4, ' ') || '└─ ' || DEPT_NM "
					+ "            ELSE LPAD(' ', (LEVEL-1)*4, ' ') || '├─ ' || DEPT_NM "
					+ "       END AS DEPT_NM "
					+ "    , EXT_NO "
					+ " FROM TB_DEPT "
					+ " START WITH SUPER_DEPT_CD IS NULL "
					+ "CONNECT BY PRIOR DEPT_CD = SUPER_DEPT_CD "
					+ "ORDER SIBLINGS BY DEPT_CD ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DeptDTO dto = new DeptDTO();

				dto.setDeptCd(rs.getString("DEPT_CD"));
				dto.setDeptNm(rs.getString("DEPT_NM"));
				dto.setExtNo(rs.getString("EXT_NO"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	@Override
	public DeptDTO selectOneByDeptCd(String deptCd) {
		DeptDTO dto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
       
        // SELECT 컬럼, 컬럼 FROM 테이블 [WHERE 조건][ORDER BY 컬럼 DESC|ASC]
        try {
        	sql = "SELECT DEPT_CD, DEPT_NM, EXT_NO, SUPER_DEPT_CD, NVL(USE_YN,'') AS USE_YN, TO_CHAR(REG_DT, 'YYYY/MM/DD HH24:MI:SS') AS REG_DT "
        			+ "  FROM TB_DEPT "
        			+ " WHERE DEPT_CD = ? ";

            pstmt = conn.prepareStatement(sql);           
            pstmt.setString(1, deptCd);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                dto = new DeptDTO();
                                
                dto.setDeptCd(rs.getString("DEPT_CD"));
                dto.setDeptNm(rs.getString("DEPT_NM"));
                dto.setExtNo(rs.getString("EXT_NO"));
                dto.setSuperDeptCd(rs.getString("SUPER_DEPT_CD"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRegDt(rs.getString("REG_DT"));
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
             
        return dto;
	}
	
	
	
	
	@Override
	public List<DeptMemberCountDTO> selectDeptMemberCount() {
		// TODO Auto-generated method stub
		return null;
	}

}
