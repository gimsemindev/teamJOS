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

/**
 * <h2>AttDAOImpl (근태 및 휴가/연차 데이터 접근 구현체)</h2>
 *
 * <p>AttDAO 인터페이스를 구현한 클래스로, 출퇴근 기록, 휴가 신청, 연차 정보 등
 * 근태 관련 기능을 데이터베이스와 연동하여 처리합니다. Oracle Stored Procedure를 활용하여
 * 출퇴근 기록 및 근태 상태 관리를 수행합니다.</p>
 *
 * <ul>
 * <li>출근(CHECK_IN) 및 퇴근(CHECK_OUT) 시간 기록</li>
 * <li>휴가 신청 및 승인 처리</li>
 * <li>근태 정보 조회 및 관리자 수정 기능</li>
 * <li>사원별 연차 정보 조회 및 관리</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> ATT_INS_001 ~ ATT_SEL_008 (프로시저 호출 포함)</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 오다은</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class AttDAOImpl implements AttDAO{
	private Connection conn = DBConn.getConnection();
	private LoginInfo loginInfo;
	
	public AttDAOImpl() {
		
	}
	
	public AttDAOImpl(LoginInfo loginInfo){
		this.loginInfo = loginInfo;
	}
	
	
	/**
	 * ATT_INS_001 : 출근 시간을 기록합니다. (Oracle Stored Procedure: SP_INSERT_CHECKIN 호출)
	 *
	 * <p>당일 CHECK_IN이 NULL인 경우에만 SYSDATE로 기록하며, 9시 이후 기록 시 지각 처리됩니다.</p>
	 *
	 * @param att 출근 시간 기록할 사원의 정보 (사원번호 포함)
	 * @return 프로시저에서 반환된 처리 결과 메시지
	 * @throws SQLException SQL 실행 실패 시
	 */
	// 출근 시간 입력 메소드
		@Override
		public String insertAttendanceIn(AttendanceDTO att) throws SQLException{
			CallableStatement cstmt = null;
			String msg = null;
			String sql;
			
			try {
				// CHECK_IN이 NULL이면 SYSDATE 입력하는 프로시저 호출
				// 오전 9시 이후로 입력되는 경우 지각으로 처리됨.
				// CHECK_IN 내용이 이미 있는 경우 입력되지 않음.
				sql = "CALL /* ATT_INS_001 */ SP_INSERT_CHECKIN(?, ?) ";
				
				cstmt= conn.prepareCall(sql);
				cstmt.setString(1, att.getEmpNo()); // 사원번호
				cstmt.registerOutParameter(2, OracleTypes.VARCHAR); // 리턴메시지
				cstmt.execute();
				
				// 프로시저 실행 후 리턴메시지 받아옴
				msg = cstmt.getString(2);
				
			} catch (Exception e) {
				throw e;
			} finally {
				DBUtil.close(cstmt);
			}
			// 프로시저에서 받아온 리턴메시지 UI로 리턴
			return msg;
		}


		/**
		 * ATT_INS_002 : 퇴근 시간을 기록하고 근무 시간을 계산합니다. (Oracle Stored Procedure: SP_INSERT_CHECKOUT 호출)
		 *
		 * <p>CHECK_OUT에 SYSDATE를 입력하고 근무 시간을 계산합니다. CHECK_OUT은 계속 갱신 가능합니다.
		 * CHECK_IN이 NULL인 경우에는 입력되지 않습니다.</p>
		 *
		 * @param att 퇴근 시간 기록할 사원의 정보 (사원번호 포함)
		 * @return 프로시저에서 반환된 처리 결과 메시지
		 * @throws SQLException SQL 실행 실패 시
		 */
		// 퇴근 시간 입력 메소드
		@Override
		public String insertAttendanceOut(AttendanceDTO att) throws SQLException{
			CallableStatement cstmt = null;
		    String msg = null;
		    String sql;
		    
		    try {
		    	// CHECK_OUT에 SYSDATE 입력 후 근무시간 계산하여 입력하는 프로시저 호출
		    	// CHECK_OUT은 계속 갱신 가능하며, 새로 입력 시 근무시간도 새로 계산됨
		    	// CHECK_IN이 NULL일 경우에는 입력되지 않음.
		        sql = "CALL /* ATT_INS_002 */ SP_INSERT_CHECKOUT(?, ?)";
		        cstmt = conn.prepareCall(sql);
		        cstmt.setString(1, att.getEmpNo()); // 사원번호
		        cstmt.registerOutParameter(2, OracleTypes.VARCHAR); // 리턴메시지
		        cstmt.execute();

		        // 프로시저 실행 후 리턴메시지 받아옴
		        msg = cstmt.getString(2);

		    } catch (Exception e) {
		        throw e;
		    } finally {
		        DBUtil.close(cstmt);
		    }
		    // 프로시저에서 받아온 리턴메시지 UI로 리턴
			return msg;
		}


	/**
	 * ATT_INS_008 : 휴가/부재 신청 정보를 등록합니다.
	 *
	 * @param vacation 등록할 휴가 정보 DTO
	 * @return 등록된 레코드 수 (1: 성공)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int insertVacation(VacationDTO vacation) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = """
					INSERT INTO /* ATT_INS_008 */ TB_VACATION(SQ_TB_VACATION, EMP_NO, START_DT, END_DT, VACATION_MEMO, APPROVER_YN) VALUES(RETIRE_SEQ.NEXTVAL, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), ?, 'N')
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
	
	/**
	 * ATT_UPD_003 : 특정 휴가 신청을 승인 처리합니다. (Oracle Stored Procedure: SP_APPROVE_VACATION_SUSI 호출)
	 *
	 * @param vacationSeq 승인할 휴가 신청 번호
	 * @return 처리 결과 (1: 성공)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int updateVacationApprove(int vacationSeq) throws SQLException{

		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		
		try {
			sql = """
					CALL /* ATT_UPD_003 */ SP_APPROVE_VACATION_SUSI(?)
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

	/**
	 * ATT_UPD_009 : 휴가 신청 내용을 수정합니다. (현재 미사용)
	 *
	 * @param vacation 수정할 휴가 정보 DTO
	 * @return 수정된 레코드 수
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int updateVacation(VacationDTO vacation) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = """
					UPDATE /* ATT_UPD_009 */ TB_VACATION
					SET
						START_DT = ?,         
						END_DT = ?,           
						VACATION_MEMO = ?    
					WHERE
						VACATION_SEQ = ?      
						AND EMP_NO = ?       
						AND APPROVER_YN = 'N';
					""";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, vacation.getStartDt());
			pstmt.setString(2, vacation.getEndDt());
			pstmt.setString(3, vacation.getVacationMemo());
			pstmt.setInt(4, vacation.getVacationSeq());			
			pstmt.setString(5, loginInfo.loginMember().getMemberId());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return result;
	}

	/**
	 * ATT_UPD_010 : 관리자가 특정 일자의 근태 기록을 수정합니다. (Oracle Stored Procedure: SP_UPDATE_ATD_COLUMN 호출)
	 *
	 * <p>수정할 사원번호, 날짜, 컬럼(CHECK_IN/CHECK_OUT), 새로운 값을 전달하여 근태 정보를 갱신합니다.</p>
	 *
	 * @param att 수정할 근태 정보 DTO (사원번호, 날짜, 컬럼명, 새 값 포함)
	 * @return 프로시저에서 반환된 처리 결과 메시지
	 * @throws SQLException SQL 실행 실패 시
	 */
	// 근태 수정 메소드
		@Override
		public String updateAttendance(AttendanceDTO att) throws SQLException{
			CallableStatement cstmt = null;
			String sql;
			String msg = null;
			
			try {
				// 프로시저 호출
				sql = "CALL /* ATT_UPD_010 */ SP_UPDATE_ATD_COLUMN(?, ?, ?, ?, ?)";
				cstmt = conn.prepareCall(sql);
				
				cstmt.setString(1, att.getEmpNo()); // 사원번호
				cstmt.setString(2, att.getRegDt()); // 변경할 날짜 
				cstmt.setString(3, att.getAtdNo()); // 변경할 컬럼 명
				cstmt.setString(4, att.getAtdStatusCd()); // 새로운 값
				cstmt.registerOutParameter(5, OracleTypes.VARCHAR); // 리턴메시지
				cstmt.execute();
				msg = cstmt.getString(5);
			} catch (Exception e) {
				throw e;
			} finally {
				DBUtil.close(cstmt);
			}
			// 프로시저에서 받아온 리턴메시지 UI로 리턴
			return msg;
		}
		
		/**
		 * ATT_SEL_012 : 특정 날짜의 CHECK_IN 또는 CHECK_OUT 컬럼 값이 NULL인지 확인합니다.
		 *
		 * <p>근태 수정 전에 해당 컬럼이 비어있는지 확인하여 수정 가능 여부를 판단하는 데 사용됩니다.</p>
		 *
		 * @param att 확인할 근태 정보 DTO (사원번호, 날짜, 컬럼명 포함)
		 * @return 해당 컬럼 값이 NULL이면 true, 아니면 false
		 * @throws SQLException SQL 실행 실패 시
		 */
		// 근무시간 수정 시 수정이 가능한지 확인하는 메소드
		@Override
		public boolean checkAtdColumnIsNull(AttendanceDTO att) throws SQLException {
			PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    String sql;
		    try {
		    	// CHECK_IN 또는 CHECK_OUT이 NULL이 맞는지 확인하는 sql
		    	sql = "SELECT /* ATT_SEL_012 */ COUNT(*) FROM TB_ATD WHERE EMP_NO = ? AND REG_DT = ? AND " + att.getAtdNo() + " IS NULL";
		        pstmt = conn.prepareStatement(sql);
		        pstmt.setString(1, att.getEmpNo()); // 사원번호
		        pstmt.setString(2, att.getRegDt()); // 수정을 원하는 날짜

		        rs = pstmt.executeQuery();
		        rs.next();

		        return rs.getInt(1) > 0;   // NULL이면 true, 아니면 false
		    } finally {
		        DBUtil.close(rs);
		        DBUtil.close(pstmt);
		    }
		}
		
		/**
		 * ATT_SEL_013 : 특정 날짜의 전체 사원 근태 정보를 조회합니다. (관리자용)
		 *
		 * <p>Oracle Stored Procedure: SP_SELECT_ATD_BY_DATE_ALL 호출하여 결과를 CURSOR로 받습니다.</p>
		 *
		 * @param date 조회할 날짜 (YYYYMMDD 형식)
		 * @return 해당 날짜의 전체 사원 근태 정보 리스트
		 * @throws SQLException SQL 실행 실패 시
		 */
		// 근태 정보 조회 메소드(관리자)
		@Override
		public List<AttendanceDTO> selectAttendanceAll(String date) throws SQLException {
			List<AttendanceDTO> list = new ArrayList<>();
			AttendanceDTO att = null;
			CallableStatement cstmt = null;
			ResultSet rs = null;
			String sql;
			try {
				// 원하는 날짜의 전체 사원 근태 정보를 조회하는 프로시저 호출
				sql = "CALL /* ATT_SEL_013 */ SP_SELECT_ATD_BY_DATE_ALL(?, ?) ";
				
				cstmt = conn.prepareCall(sql);
				
				cstmt.setString(1, date); // 조회 날짜
				cstmt.registerOutParameter(2, OracleTypes.CURSOR); // 조회 결과
				cstmt.execute();
				
				rs = (ResultSet) cstmt.getObject(2);
				
				while (rs.next()) {
					
					att = new AttendanceDTO();
					att.setEmpNo(rs.getString("EMP_NO")); // 사원번호
					att.setAtdNo(rs.getString("EMP_NM")); // 사원이름
					att.setCheckIn(rs.getString("CHECK_IN")); // 출근시간
					att.setCheckOut(rs.getString("CHECK_OUT")); // 퇴근시간
					att.setWorkHours(rs.getString("WORK_HOURS")); // 근무시간
					att.setAtdStatusCd(rs.getString("STATUS_NM")); // 근태상태
					att.setRegDt(rs.getString("REG_DT")); // 등록일자

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

	/**
	 * ATT_SEL_014 : 특정 날짜의 본인 근태 정보를 조회합니다. (일반 사용자용)
	 *
	 * <p>Oracle Stored Procedure를 호출하여 해당 사원번호와 날짜에 일치하는 근태 정보를 CURSOR로 받습니다.</p>
	 *
	 * @param att 조회할 근태 정보 DTO (사원번호, 날짜 포함)
	 * @return 해당 날짜의 근태 정보 DTO (없으면 null)
	 * @throws SQLException SQL 실행 실패 시
	 */
	// 근태 정보 조회 메소드(일반사용자)
	@Override
	public AttendanceDTO selectAttendance(AttendanceDTO att) throws SQLException {
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			// 원하는 날짜의 자신의 근태 정보를 조회하는 프로시저 호출
			sql = "CALL /* ATT_SEL_014 */ SP_SELECT_ATD_BY_DATE(?, ?, ?) ";
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, att.getEmpNo()); // 사원번호
			cstmt.setString(2, att.getRegDt()); // 날짜
			cstmt.registerOutParameter(3, OracleTypes.CURSOR);
			cstmt.execute();
			
			rs = (ResultSet) cstmt.getObject(3);
			
			if (rs.next()) {
				att = new AttendanceDTO();
				att.setEmpNo(rs.getString("EMP_NO")); // 사원번호
				att.setAtdNo(rs.getString("EMP_NM")); // 사원이름
				att.setCheckIn(rs.getString("CHECK_IN")); // 출근일시
				att.setCheckOut(rs.getString("CHECK_OUT")); // 퇴근일시
				att.setWorkHours(rs.getString("WORK_HOURS")); // 근무시간
				att.setAtdStatusCd(rs.getString("STATUS_NM")); // 근태상태
				att.setRegDt(rs.getString("REG_DT")); // 등록일자
			}
		} catch (Exception e) {
		}
		return att;
	}

	/**
	 * ATT_SEL_006 : 전체 사원의 연차 사용 현황을 페이징하여 조회합니다. (권한에 따라 본인 연차만 조회)
	 *
	 * <p>사용자 권한(Role)이 '03'이 아니면 본인의 연차 정보만 조회합니다.</p>
	 *
	 * @param start 시작 행 번호
	 * @param end 끝 행 번호
	 * @return 연차 정보 DTO 리스트
	 */
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
	              SELECT *  /* ATT_SEL_006 */FROM (
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

	/**
	 * ATT_SEL_008 : 전체 사원(또는 본인)의 연차 사용 현황 개수를 조회합니다.
	 *
	 * <p>사용자 권한(Role)이 '03'이 아니면 본인의 연차 정보 개수만 조회합니다.</p>
	 *
	 * @return 연차 사용 현황 전체 개수
	 */
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
	
	
	/**
	 * ATT_SEL_007 : 특정 사원의 휴가 신청 목록을 조회합니다. (현재 미사용)
	 *
	 * @param empNo 조회할 사원 번호
	 * @return 휴가 정보 DTO 리스트
	 */
	@Override
	public List<VacationDTO> selectAnnualLeaveByEmp(int empNo) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * ATT_SEL_011 : 승인 대기 중인 모든 휴가 신청 목록을 조회합니다. (관리자용)
	 *
	 * <p>APPROVER_YN이 'N'인 레코드만 조회합니다.</p>
	 *
	 * @return 승인 대기 중인 휴가 정보 DTO 리스트
	 */
	@Override
	public List<VacationDTO> listVaction() {
		List<VacationDTO> list = new ArrayList<VacationDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = """
					SELECT /* ATT_SEL_011 */ VACATION_SEQ, V.EMP_NO,TO_CHAR(START_DT, 'YYYY-MM-DD')AS START_DT,TO_CHAR(END_DT, 'YYYY-MM-DD') AS END_DT, VACTION_MEMO, APPROVER_YN
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
				dto.setVacationMemo(rs.getString("VACTION_MEMO"));
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
	/** ATT_SEL_015 */
	@Override
	public List<VacationDTO> listVaction(String empNo) {
		List<VacationDTO> list = new ArrayList<VacationDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = """
					SELECT /* ATT_SEL_015 */ VACATION_SEQ, V.EMP_NO,TO_CHAR(START_DT, 'YYYY-MM-DD')AS START_DT,TO_CHAR(END_DT, 'YYYY-MM-DD') AS END_DT, VACTION_MEMO, APPROVER_YN
					FROM TB_VACATION V
					LEFT JOIN TB_EMP E ON V.EMP_NO = E.EMP_NO 
					WHERE APPROVER_YN = 'N' AND V.EMP_NO = ?
					ORDER BY VACATION_SEQ ASC

					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, loginInfo.loginMember().getMemberId());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				VacationDTO dto = new VacationDTO();
				dto.setVacationSeq(rs.getInt("VACATION_SEQ"));
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setStartDt(rs.getString("START_DT"));
				dto.setEndDt(rs.getString("END_DT"));
				dto.setVacationMemo(rs.getString("VACTION_MEMO"));
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