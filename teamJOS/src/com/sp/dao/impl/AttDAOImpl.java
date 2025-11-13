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

import oracle.jdbc.OracleTypes;

public class AttDAOImpl implements AttDAO{
	private Connection conn = DBConn.getConnection();
	private LoginInfo loginInfo;
	
	public AttDAOImpl() {
		
	}
	
	public AttDAOImpl(LoginInfo loginInfo){
		this.loginInfo = loginInfo;
	}
	
	
	// 출근 시간 입력 메소드
		@Override
		public String insertAttendanceIn(AttendanceDTO att) throws SQLException{
			CallableStatement cstmt = null;
			String msg = null;
			String sql;
			
			try {
				sql = "CALL SP_INSERT_CHECKIN(?, ?) ";
				
				cstmt= conn.prepareCall(sql);
				cstmt.setString(1, att.getEmpNo());
				cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
				cstmt.execute();
				
				msg = cstmt.getString(2);
				
			} catch (Exception e) {
				throw e;
			} finally {
				DBUtil.close(cstmt);
			}
			return msg;
		}


		// 퇴근 시간 입력 메소드
		@Override
		public String insertAttendanceOut(AttendanceDTO att) throws SQLException{
			CallableStatement cstmt = null;
		    String msg = null;
		    String sql;
		    
		    try {
		        sql = "CALL SP_INSERT_CHECKOUT(?, ?)";
		        cstmt = conn.prepareCall(sql);
		        cstmt.setString(1, att.getEmpNo());
		        cstmt.registerOutParameter(2, OracleTypes.VARCHAR);
		        cstmt.execute();

		        msg = cstmt.getString(2);

		    } catch (Exception e) {
		        throw e;
		    } finally {
		        DBUtil.close(cstmt);
		    }
			
			return msg;
		}


	@Override
	public int insertVacation(VacationDTO vacation) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = """
					INSERT INTO TB_VACATION(VACATION_SEQ, EMP_NO, START_DT, END_DT, VACATION_MEMO, APPROVER_YN) VALUES(RETIRE_SEQ.NEXTVAL, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), ?, 'N')
					""";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, loginInfo.loginMember().getMemberId());
			pstmt.setString(2, vacation.getStartDt());
			pstmt.setString(3, vacation.getEndDt());
			pstmt.setString(4, vacation.getVacationMemo());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return result;
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

	// 근태 수정 메소드
		@Override
		public String updateAttendance(AttendanceDTO att) throws SQLException{
			CallableStatement cstmt = null;
			String sql;
			String msg = null;
			
			try {
				sql = "CALL SP_UPDATE_ATD_COLUMN(?, ?, ?, ?)";
				cstmt = conn.prepareCall(sql);
				cstmt.setString(1, att.getEmpNo());
				cstmt.setString(2, att.getAtdNo());
				cstmt.setString(3, att.getAtdStatusCd());
				cstmt.registerOutParameter(4, OracleTypes.VARCHAR);
				cstmt.execute();
				
				msg = cstmt.getString(4);
			} catch (Exception e) {
				throw e;
			} finally {
				DBUtil.close(cstmt);
			}
			return msg;
		}
		
		// 근태정보조회 메소드(관리자)
		@Override
		public List<AttendanceDTO> selectAttendanceAll(String date) throws SQLException {
			List<AttendanceDTO> list = new ArrayList<>();
			AttendanceDTO att = null;
			CallableStatement cstmt = null;
			ResultSet rs = null;
			String sql;
			try {
				sql = "CALL SP_SELECT_ATD_BY_DATE_ALL(?, ?) ";
				
				cstmt = conn.prepareCall(sql);
				
				cstmt.setString(1, date);
				cstmt.registerOutParameter(2, OracleTypes.CURSOR);
				cstmt.execute();
				
				rs = (ResultSet) cstmt.getObject(2);
				
				while (rs.next()) {
					
					att = new AttendanceDTO();
					att.setEmpNo(rs.getString("EMP_NO"));
					att.setAtdNo(rs.getString("EMP_NM"));
					att.setCheckIn(rs.getString("CHECK_IN"));
					att.setCheckOut(rs.getString("CHECK_OUT"));
					att.setWorkHours(rs.getString("WORK_HOURS"));
					att.setAtdStatusCd(rs.getString("STATUS_NM"));
					att.setRegDt(rs.getString("REG_DT"));

					list.add(att);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DBUtil.close(rs);
				DBUtil.close(cstmt);
			}
			return list;
		}

	// 근태정보조회 메소드(일반사용자)
	@Override
	public AttendanceDTO selectAttendance(AttendanceDTO att) throws SQLException {
		CallableStatement cstmt = null;
		List<AttendanceDTO> list = new ArrayList<>();
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "CALL SP_SELECT_ATD_BY_DATE_ALL(?, ?, ?) ";
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, att.getEmpNo());
			cstmt.setString(2, att.getRegDt());
			cstmt.registerOutParameter(3, OracleTypes.CURSOR);
			cstmt.execute();
			
			rs = (ResultSet) cstmt.getObject(3);
			
			if (rs.next()) {
				att = new AttendanceDTO();
				att.setEmpNo(rs.getString("EMP_NO"));
				att.setAtdNo(rs.getString("EMP_NM"));
				att.setCheckIn(rs.getString("CHECK_IN"));
				att.setCheckOut(rs.getString("CHECK_OUT"));
				att.setWorkHours(rs.getString("WORK_HOURS"));
				att.setAtdStatusCd(rs.getString("STATUS_NM"));
				att.setRegDt(rs.getString("REG_DT"));
			}
		} catch (Exception e) {
		}
		return att;
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
					ORDER BY VACATION_SEQ ASC

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
