package com.sp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sp.dao.DeptDAO;
import com.sp.model.DeptDTO;
import com.sp.model.DeptMemberDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;


public class DeptDAOImpl implements DeptDAO{

	private Connection conn = DBConn.getConnection();
	
	@Override	
    public int insertDept(DeptDTO dept) throws SQLException {
        int result = 0;
		PreparedStatement pstmt = null;
		String sql;
				
        try {
            sql = """
            	  INSERT /* DEPT_INS_001 */ 
            	    INTO TB_DEPT
                       ( DEPT_CD
                       , DEPT_NM
                       , EXT_NO
                       , SUPER_DEPT_CD
                       , USE_YN
                       , REG_DT
                       )
                  VALUES (?, ?, ?, ?, ?, SYSDATE) """;

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
            sql = """   
            	  UPDATE /** DEPT_UPD_002 */ TB_DEPT 
            	     SET DEPT_NM       = ? 
            	       , EXT_NO        = ?
            	       , SUPER_DEPT_CD = ? 	
            	       , USE_YN        = ? 
            	   WHERE DEPT_CD       = ? """;
            pstmt= conn.prepareStatement(sql);
            
            pstmt.setString(1, dept.getDeptNm());
            pstmt.setString(2, dept.getExtNo());
            pstmt.setString(3, dept.getSuperDeptCd());
            pstmt.setString(4, dept.getUseYn());
            pstmt.setString(5, dept.getDeptCd());
            
			result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            DBUtil.close(pstmt);
        }
        
        return result;
	}

	@Override
	public List<DeptDTO> selectAllDept() {
		List<DeptDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		// member1과 member2를 id를 기준으로 LEFT OUTER JOIN 하여 전체 레코드 반환
		
		try {
			sql = """
				  SELECT /* DEPT_SEL_003 */ 
					     DEPT_CD 
					   , CASE WHEN CONNECT_BY_ISLEAF = 1 THEN 
					            LPAD(' ', (LEVEL-1)*4, ' ') || '└─ ' || DEPT_NM 
					            ELSE LPAD(' ', (LEVEL-1)*4, ' ') || '├─ ' || DEPT_NM 
					      END AS DEPT_NM 
				       , EXT_NO 
					FROM TB_DEPT 
				   WHERE USE_YN = 'Y' 
				   START WITH SUPER_DEPT_CD IS NULL 
				  CONNECT BY PRIOR DEPT_CD = SUPER_DEPT_CD 
				   ORDER SIBLINGS BY DEPT_CD """;

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
        	sql = """
        		  SELECT /* DEPT_SEL_006 */ 
        		         DEPT_CD
        		       , DEPT_NM
        		       , EXT_NO
        		       , SUPER_DEPT_CD
        		       , NVL(USE_YN,'') AS USE_YN
        		       , TO_CHAR(REG_DT, 'YYYY/MM/DD HH24:MI:SS') AS REG_DT
        			FROM TB_DEPT 
        		   WHERE DEPT_CD = ? """;

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
	public int selectDeptMemberCount() {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        
        try {
        	sql = "SELECT /* DEPT_SEL_008 */ COUNT(*) AS CNT FROM TB_EMP WHERE USE_YN='Y'";
        	        	
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt("CNT");
            }
            
            
        	
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
		
        return result;
	}
	
	
	@Override
	public List<DeptMemberDTO> selectDeptMember(int start, int end) {
	    
		List<DeptMemberDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
	    
		try {
	    	sql = """
	                SELECT *
	                FROM (
	                    SELECT ROWNUM rn, A.*
	                    FROM (
	                        SELECT D.DEPT_CD, D.DEPT_NM, G.GRADE_NM, C.CONTRACT_TP_NM,
	                               S.EMP_STAT_NM, E.EMP_NO, E.EMP_NM,
	                               TO_CHAR(E.HIRE_DT, 'YYYY/MM/DD') AS HIRE_DT,
	                               NVL(P.CONTACT_NO,' ') AS CONTACT_NO, E.EMAIL
	                        FROM TB_EMP E
	                        LEFT JOIN TB_DEPT D ON E.DEPT_CD = D.DEPT_CD AND D.USE_YN='Y'
	                        LEFT JOIN TB_GRADE G ON E.GRADE_CD = G.GRADE_CD AND G.USE_YN='Y'
	                        LEFT JOIN TB_EMP_CNTRT_TYPE C ON E.CONTRACT_TP_CD=C.CONTRACT_TP_CD AND C.USE_YN='Y'
	                        LEFT JOIN TB_EMP_STATUS S ON E.EMP_STAT_CD=S.EMP_STAT_CD AND S.USE_YN='Y'
	                        LEFT JOIN TB_EMP_CNTCT P ON E.EMP_NO=P.EMP_NO AND P.CONTACT_TP_CD='1' AND P.USE_YN='Y'
	                        WHERE E.USE_YN='Y'
	                        ORDER BY D.DEPT_CD, E.GRADE_CD DESC
	                    ) A
	                )
	                WHERE rn BETWEEN ? AND ? """;
	    	
	    	pstmt = conn.prepareStatement(sql);
            
	    	pstmt.setInt(1, start);
	    	pstmt.setInt(2, end);
	    	
	        rs = pstmt.executeQuery();

	    	while (rs.next()) {
	    		DeptMemberDTO dto = new DeptMemberDTO();
	            dto.setDeptCd(rs.getString("DEPT_CD"));
	            dto.setDeptNm(rs.getString("DEPT_NM"));
	            dto.setGradeNM(rs.getString("GRADE_NM"));
	            dto.setCotractTpNM(rs.getString("CONTRACT_TP_NM"));
	            dto.setEmpStatNM(rs.getString("EMP_STAT_NM"));
	            dto.setEmpNo(rs.getString("EMP_NO"));
	            dto.setEmpNm(rs.getString("EMP_NM"));
	            dto.setHireDt(rs.getString("HIRE_DT"));
	            dto.setContactNo(rs.getString("CONTACT_NO"));
	            dto.setEmail(rs.getString("EMAIL"));
	            list.add(dto);
	        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
	    
	    return list;		
	}

	@Override
	public List<DeptDTO> selectDeptWithAllChildren(String deptCd) throws SQLException {
		List<DeptDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
	    try {
	    	sql = """
	    		  SELECT /* DEPT_SEL_007 */ 
	    		         DEPT_CD
				       , CASE WHEN CONNECT_BY_ISLEAF = 1 THEN 
				                   LPAD(' ', (LEVEL-1)*4, ' ') || '└─ ' || DEPT_NM 
				              ELSE LPAD(' ', (LEVEL-1)*4, ' ') || '├─ ' || DEPT_NM 
				          END AS DEPT_NM 
	    		       , SUPER_DEPT_CD, USE_YN  
	    	        FROM TB_DEPT  
	               START WITH DEPT_CD = ? 
	              CONNECT BY PRIOR DEPT_CD = SUPER_DEPT_CD """;	
	    	
	    	pstmt = conn.prepareStatement(sql);
		 
	        pstmt.setString(1, deptCd);
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            DeptDTO dto = new DeptDTO();
	            dto.setDeptCd(rs.getString("DEPT_CD"));
	            dto.setDeptNm(rs.getString("DEPT_NM"));
	            dto.setSuperDeptCd(rs.getString("SUPER_DEPT_CD"));
	            dto.setUseYn(rs.getString("USE_YN"));
	            list.add(dto);
	        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
	    
	    return list;
	}
	
	@Override
	public int deleteDept(String deptCd) throws SQLException{
        int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = """
				  UPDATE /* DEPT_DEL_004 */ TB_DEPT " 
                     SET USE_YN = 'N' " 
                   WHERE DEPT_CD IN ( " 
                                     SELECT DEPT_CD " 
                                       FROM TB_DEPT " 
                                      START WITH DEPT_CD = ? " 
                                    CONNECT BY PRIOR DEPT_CD = SUPER_DEPT_CD) """;
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, deptCd);
			result = pstmt.executeUpdate();
			} catch (Exception e) {
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		return result;
		}	
}
