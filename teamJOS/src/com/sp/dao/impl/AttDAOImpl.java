package com.sp.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sp.dao.AttDAO;
import com.sp.model.AnnualLeaveDTO;
import com.sp.model.AttendanceDTO;
import com.sp.model.VacationDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;
import com.sp.util.LoginInfo;

public class AttDAOImpl implements AttDAO{
	private Connection conn = DBConn.getConnection();
	private LoginInfo loginInfo;
	
	public AttDAOImpl() {
		
	}
	
	public AttDAOImpl(LoginInfo loginInfo){
		this.loginInfo = loginInfo;
	}
	
	
	@Override
	public int insertAttendanceIn(AttendanceDTO att) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertAttendanceOut(AttendanceDTO att) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertVacation(VacationDTO vacation) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	@Override
	public int updateVacationApprove(int vacationSeq) throws SQLException{

		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		
		try {
			sql = """
					CALL SP_APPROVE_VACATION_SUSI(?)
					""";
			cstmt = conn.prepareCall(sql);
			
			cstmt.setInt(1, vacationSeq);
			
			cstmt.execute();
			
			result = 1;
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(cstmt);
		}
		
		return result;
	}

	@Override
	public int updateVacation(VacationDTO vacation) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateAttendance(AttendanceDTO att) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<AttendanceDTO> selectAllWorkTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AttendanceDTO> selectWorkTimeByEmp(int empNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AnnualLeaveDTO> selectAllAnnualLeave(int start, int end) {
	    List<AnnualLeaveDTO> list = new ArrayList<>();
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;
	    String addSql="";
	    
		System.out.println(loginInfo.loginMember().getGradeNm());
		System.out.println(loginInfo.loginMember().getDeptCd());
		System.out.println(loginInfo.loginMember().getRole());
		
	    if(!loginInfo.loginMember().getRole().equals("03")) {
	    	addSql = " AND A.EMP_NO = '" + loginInfo.loginMember().getMemberId() + "'";
	    }
		
	    try {
	        sql = """
	              SELECT * 
	                FROM (
	                        SELECT ROWNUM rn, A.*
	                          FROM (
	                                SELECT A.LEAVE_SEQ
	                                     , D.DEPT_NM
	                                     , G.GRADE_NM
	                                     , A.EMP_NO
	                                     , E.EMP_NM
	                                     , TO_CHAR(E.HIRE_DT, 'YYYY/MM/DD') AS HIRE_DT
	                                     , A.TOTAL_DAYS
	                                     , A.USED_DAYS
	                                     , A.REMAIN_DAYS
	                                     , A.USE_YN
	                                  FROM TB_ANNUAL_LEAVE A
	                                  JOIN TB_EMP E
	                                    ON A.EMP_NO = E.EMP_NO
	                                   AND E.USE_YN = 'Y'
	                                  LEFT JOIN TB_DEPT D
	                                    ON E.DEPT_CD = D.DEPT_CD
	                                   AND D.USE_YN = 'Y'
	                                  LEFT JOIN TB_GRADE G
	                                    ON E.GRADE_CD = G.GRADE_CD
	                                   AND G.USE_YN = 'Y'
	                                 WHERE A.USE_YN = 'Y'
	                                 """;
	        sql += addSql;
	        addSql= """
	        		
	        		ORDER BY D.DEPT_NM, G.GRADE_NM, E.EMP_NM
	                               ) A
	                     )
	               WHERE rn BETWEEN ? AND ? """;  
	        sql += addSql;
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, start);
	        pstmt.setInt(2, end);
	        
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            AnnualLeaveDTO dto = new AnnualLeaveDTO();
	            dto.setLeaveSeq(rs.getInt("LEAVE_SEQ"));
	            dto.setDeptNm(rs.getString("DEPT_NM"));
	            dto.setGradeNm(rs.getString("GRADE_NM"));
	            dto.setEmpNo(rs.getString("EMP_NO"));
	            dto.setEmpNm(rs.getString("EMP_NM"));
	            dto.setHireDt(rs.getString("HIRE_DT"));
	            dto.setTotalDays(rs.getInt("TOTAL_DAYS"));
	            dto.setUsedDays(rs.getInt("USED_DAYS"));
	            dto.setRemainDays(rs.getInt("REMAIN_DAYS"));
	            dto.setUseYn(rs.getString("USE_YN"));
	            
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
	public int selectAllAnnualLeaveCount() {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
 
	    String addSql="";
	    
		System.out.println(loginInfo.loginMember().getGradeNm());
		System.out.println(loginInfo.loginMember().getDeptCd());
		System.out.println(loginInfo.loginMember().getRole());
		
	    if(!loginInfo.loginMember().getRole().equals("03")) {
	    	addSql = " AND EMP_NO = '" + loginInfo.loginMember().getMemberId() + "'";
	    }
        
        try {
        	sql = "SELECT /* ATT_SEL_008 */ COUNT(*) AS CNT FROM TB_ANNUAL_LEAVE WHERE USE_YN='Y'";
        	 
        	
	        sql += addSql;
        	
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
	public List<VacationDTO> selectAnnualLeaveByEmp(int empNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VacationDTO> listVaction() {
		List<VacationDTO> list = new ArrayList<VacationDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = """
					SELECT VACATION_SEQ, V.EMP_NO,TO_CHAR(START_DT, 'YYYY-MM-DD')AS START_DT,TO_CHAR(END_DT, 'YYYY-MM-DD') AS END_DT, VACATION_MEMO, APPROVER_YN
					FROM TB_VACATION V
					LEFT JOIN TB_EMP E ON V.EMP_NO = E.EMP_NO 
					WHERE APPROVER_YN = 'N'
					ORDER BY VACATION_SEQ DESC

					""";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				VacationDTO dto = new VacationDTO();
				dto.setVacationSeq(rs.getInt("VACATION_SEQ"));
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setStartDt(rs.getString("START_DT"));
				dto.setEndDt(rs.getString("END_DT"));
				dto.setVacationMemo(rs.getString("VACATION_MEMO"));
				dto.setApproverYn(rs.getString("APPROVER_YN"));
				
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

}
