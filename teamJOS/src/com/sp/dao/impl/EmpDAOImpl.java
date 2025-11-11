package com.sp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sp.dao.EmpDAO;
import com.sp.model.CareerDTO;
import com.sp.model.DeptMoveDTO;
import com.sp.model.EmployeeDTO;
import com.sp.model.HistoryDTO;
import com.sp.model.PromotionDTO;
import com.sp.model.RewardDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;

public class EmpDAOImpl implements EmpDAO{
	private Connection conn = DBConn.getConnection();

	// 사원 등록 메소드
	@Override
	public int insertEmployee(EmployeeDTO emp) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		int result = 0;
		
		try {
			sql = "INSERT INTO TB_EMP(EMP_NO, EMP_NM, RRN, EMP_ADDR, HIRE_DT, DEPT_CD, GRADE_CD, EMP_STAT_CD, CONTRACT_TP_CD, EMAIL, PWD, LEVEL_CODE) "
					+ " VALUES(?, ?, ?, ?, TO_DATE(sysdate, 'YYYY-MM-DD'), ?, ?, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, emp.getEmpNo());
			pstmt.setString(2, emp.getEmpNm());
			pstmt.setString(3, emp.getRrn());
			pstmt.setString(4, emp.getEmpAddr());
			pstmt.setString(5, emp.getDeptCd());
			pstmt.setString(6, emp.getGradeCd());
			pstmt.setString(7, emp.getEmpStatCd());
			pstmt.setString(8, emp.getContractTpCd());
			pstmt.setString(9, emp.getEmail());
			pstmt.setString(10, emp.getPwd());
			pstmt.setString(11, emp.getLevelCode());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return result;
	}
	
	// 사원 정보 수정 메소드
	@Override
	public int updateEmployee(String empNo, String col, String con) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE TB_EMP SET " + col + " = ? WHERE EMP_NO = ?";
		
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, con);
			pstmt.setString(2, empNo);
		
			pstmt.executeUpdate();	
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return 0;
	}

	// 부서 이동 메소드
	@Override
	public int updateDeptMove(DeptMoveDTO move) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE TB_EMP SET DEPT_CD = ? WHERE EMP_NO = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, move.getNewDeptCd());
			pstmt.setString(2, move.getEmpNo());
		
			pstmt.executeUpdate();	
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return 0;
	}

	// 직급 변경 및 직급 변경 이력 업데이트 메소드
	// 수정 중
	@Override
	public int updatePromotion(PromotionDTO promotion) throws SQLException{
		PreparedStatement pstmt = null;
		String sqlup;
		String sqlinst;
		int result =0;
		try {
			EmployeeDTO empDto = selectdeptName(promotion.getEmpNo());
			// EmployeeDTO empDto = selectDeptName(promotion.getEmpNo());
	        String deptCd = empDto.getDeptCd();
			
			sqlup = "UPDATE TB_GRADE_HISTORY SET VALID_END_DT = SYSDATE-1 "
					+ "WHERE EMP_NO = ? AND VALID_END_DT IS NULL";
			
			pstmt = conn.prepareStatement(sqlup);
	        pstmt.setString(1, promotion.getEmpNo());
	        pstmt.executeUpdate();
	        pstmt = null;
			
	        sqlinst = "INSERT INTO TB_GRADE_HISTORY "
	        		+ " (EMP_NO, GRADE_CD, VALID_STRT_DT, VALID_END_DT, DETAILS, REG_DT, DEPT_CD) "
	                  + " VALUES (?, ?, SYSDATE, NULL, ?, SYSDATE, ?)";
			
			pstmt = conn.prepareStatement(sqlinst);
			pstmt.setString(1, promotion.getEmpNo());
			pstmt.setString(2, promotion.getNewGradeCd());
			pstmt.setString(3, promotion.getDetails());
			pstmt.setString(4, deptCd);
			
		
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return result;
	}
	
	// 퇴직 신청 결재 메소드
	@Override
	public int updateRetireApproval(String empNo, String status) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE TB_EMP SET EMP_STAT_CD = ? WHERE EMP_NO = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, status);
			pstmt.setString(2, empNo);
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return 0;
	}

	// 경력 추가 메소드
	@Override
	public int insertCareer(CareerDTO career) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO TB_EMP_CAREER_HIST(CAREER_SEQ, EMP_NO, PREV_COMP_NM, CAREER_STRT_DT, CAREER_END_DT, DETAILS) "
					+ " VALUES(SQ_TB_EMP_CAREER_HIST.NEXTVAL, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, career.getEmpNo());
			pstmt.setString(2, career.getCompanyName());
			pstmt.setString(3, career.getStartDt());
			pstmt.setString(4, career.getEndDt());
			pstmt.setString(5, career.getDetails());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return 0;
	}

	// 자격증 및 포상 추가 메소드
	@Override
	public int insertLicense(RewardDTO reward) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO TB_EMP_CERT(CERT_SEQ, EMP_NO, CERT_NM, ISSUE_ORG_NM, ISSUE_DT) "
					+ " VALUES(SQ_TB_EMP_CERT.NEXTVAL, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reward.getEmpNo());
			pstmt.setString(2, reward.getRewardName());
			pstmt.setString(3, reward.getDate());
			pstmt.setString(4, reward.getIssuer());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {	
			DBUtil.close(pstmt);
		}
		return 0;
	}

	// 사원번호 조회 메소드
	@Override
	public EmployeeDTO selectByEmpNo(String empNo) {
		EmployeeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = " SELECT e.EMP_NO, e.EMP_NM, e.RRN, e.EMP_ADDR, TO_CHAR(e.HIRE_DT, 'YYYY-MM-DD') HIRE_DT, "
					+ " d.DEPT_NM, g.GRADE_NM, s.EMP_STAT_NM, c.CONTRACT_TP_NM, "
					+ " e.EMAIL, e.PWD, TO_CHAR(e.REG_DT, 'YYYY-MM-DD') REG_DT, TO_CHAR(e.RETIRE_DT, 'YYYY-MM-DD') RETIRE_DT, r.LEVEL_NM "
					+ " FROM TB_EMP e "
					+ " JOIN TB_DEPT d ON e.DEPT_CD = d.DEPT_CD "
					+ " JOIN TB_GRADE g ON e.GRADE_CD = g.GRADE_CD "
					+ " JOIN TB_EMP_STATUS s ON e.EMP_STAT_CD = s.EMP_STAT_CD "
					+ " JOIN TB_EMP_CNTRT_TYPE c ON e.CONTRACT_TP_CD = c.CONTRACT_TP_CD "
					+ " JOIN TB_ROLE r ON e.LEVEL_CODE = r.LEVEL_CODE "
					+ " WHERE e.EMP_NO = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, empNo);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new EmployeeDTO();
				
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setEmpNm(rs.getString("EMP_NM"));
				dto.setRrn(rs.getString("RRN"));
				dto.setEmpAddr(rs.getString("EMP_ADDR"));
				dto.setHireDt(rs.getString("HIRE_DT"));
				dto.setDeptNm(rs.getString("DEPT_NM"));
				dto.setGradeNm(rs.getString("GRADE_NM"));
				dto.setEmpStatNm(rs.getString("EMP_STAT_NM"));
				dto.setContractTpNm(rs.getString("CONTRACT_TP_NM"));
				dto.setEmail(rs.getString("EMAIL"));
				dto.setPwd(rs.getString("PWD"));
				dto.setRegDt(rs.getString("REG_DT"));;
				dto.setRetireDt(rs.getString("RETIRE_DT"));
				dto.setLevelCode(rs.getString("LEVEL_NM"));
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return dto;
	}

	// 사원이름 검색
	@Override
	public List<EmployeeDTO> selectByName(String name) {
		List<EmployeeDTO> list = new ArrayList<EmployeeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT e.EMP_NO, e.EMP_NM, e.RRN, e.EMP_ADDR, TO_CHAR(e.HIRE_DT, 'YYYY-MM-DD') HIRE_DT, "
					+ " d.DEPT_NM, g.GRADE_NM, s.EMP_STAT_NM, c.CONTRACT_TP_NM, "
					+ " e.EMAIL, e.PWD, TO_CHAR(e.REG_DT, 'YYYY-MM-DD') REG_DT, TO_CHAR(e.RETIRE_DT, 'YYYY-MM-DD') RETIRE_DT, r.LEVEL_NM "
					+ " FROM TB_EMP e "
					+ " JOIN TB_DEPT d ON e.DEPT_CD = d.DEPT_CD "
					+ " JOIN TB_GRADE g ON e.GRADE_CD = g.GRADE_CD "
					+ " JOIN TB_EMP_STATUS s ON e.EMP_STAT_CD = s.EMP_STAT_CD "
					+ " JOIN TB_EMP_CNTRT_TYPE c ON e.CONTRACT_TP_CD = c.CONTRACT_TP_CD "
					+ " JOIN TB_ROLE r ON e.LEVEL_CODE = r.LEVEL_CODE "
					+ " WHERE INSTR(e.EMP_NM, ?) >= 1";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EmployeeDTO dto = new EmployeeDTO();
				
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setEmpNm(rs.getString("EMP_NM"));
				dto.setRrn(rs.getString("RRN"));
				dto.setEmpAddr(rs.getString("EMP_ADDR"));
				dto.setHireDt(rs.getString("HIRE_DT"));
				dto.setDeptNm(rs.getString("DEPT_NM"));
				dto.setGradeNm(rs.getString("GRADE_NM"));
				dto.setEmpStatNm(rs.getString("EMP_STAT_NM"));
				dto.setContractTpNm(rs.getString("CONTRACT_TP_NM"));
				dto.setEmail(rs.getString("EMAIL"));
				dto.setPwd(rs.getString("PWD"));
				dto.setRegDt(rs.getString("REG_DT"));;
				dto.setRetireDt(rs.getString("RETIRE_DT"));
				dto.setLevelCode(rs.getString("LEVEL_NM"));
				
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

	// 전체 리스트
	@Override
	public List<EmployeeDTO> selectAll() {
		List<EmployeeDTO> list = new ArrayList<EmployeeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT e.EMP_NO, e.EMP_NM, e.RRN, e.EMP_ADDR, TO_CHAR(e.HIRE_DT, 'YYYY-MM-DD') HIRE_DT, "
					+ " d.DEPT_NM, g.GRADE_NM, s.EMP_STAT_NM, c.CONTRACT_TP_NM, "
					+ " e.EMAIL, e.PWD, TO_CHAR(e.REG_DT, 'YYYY-MM-DD') REG_DT, TO_CHAR(e.RETIRE_DT, 'YYYY-MM-DD') RETIRE_DT, e.USE_YN, r.LEVEL_NM "
					+ " FROM TB_EMP e "
					+ " JOIN TB_DEPT d ON e.DEPT_CD = d.DEPT_CD "
					+ " JOIN TB_GRADE g ON e.GRADE_CD = g.GRADE_CD "
					+ " JOIN TB_EMP_STATUS s ON e.EMP_STAT_CD = s.EMP_STAT_CD "
					+ " JOIN TB_EMP_CNTRT_TYPE c ON e.CONTRACT_TP_CD = c.CONTRACT_TP_CD "
					+ " JOIN TB_ROLE r ON e.LEVEL_CODE = r.LEVEL_CODE ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EmployeeDTO dto = new EmployeeDTO();
				
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setEmpNm(rs.getString("EMP_NM"));
				dto.setRrn(rs.getString("RRN"));
				dto.setEmpAddr(rs.getString("EMP_ADDR"));
				dto.setHireDt(rs.getString("HIRE_DT"));
				dto.setDeptNm(rs.getString("DEPT_NM"));
				dto.setGradeNm(rs.getString("GRADE_NM"));
				dto.setEmpStatNm(rs.getString("EMP_STAT_NM"));
				dto.setContractTpNm(rs.getString("CONTRACT_TP_NM"));
				dto.setEmail(rs.getString("EMAIL"));
				dto.setPwd(rs.getString("PWD"));
				dto.setRegDt(rs.getString("REG_DT"));;
				dto.setRetireDt(rs.getString("RETIRE_DT"));
				dto.setUseYn(rs.getString("USE_YN"));
				dto.setLevelCode(rs.getString("LEVEL_NM"));
				
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

	// 이력 조회
	@Override
	public List<HistoryDTO> selectHistory(String empNo) {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT TO_CHAR(VALID_STRT_DT, 'YYYY-MM-DD') VALID_STRT_DT, e.EMP_NO, e.EMP_NM, g.GRADE_NM, TO_CHAR(VALID_END_DT, 'YYYY-MM-DD') VALID_END_DT, DETAILS, TO_CHAR(gh.REG_DT, 'YYYY-MM-DD') REG_DT, d.DEPT_NM "
					+ " FROM TB_EMP_GRADE_HIST gh "
					+ " LEFT JOIN TB_EMP e ON e.EMP_NO = gh.EMP_NO "
					+ " LEFT JOIN TB_GRADE g ON g.GRADE_CD = gh.GRADE_CD "
					+ " LEFT JOIN TB_DEPT d ON gh.DEPT_CD = d.DEPT_CD "
					+ " WHERE gh.EMP_NO = '00001' ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				HistoryDTO dto = new HistoryDTO();
				
				 dto.setStartDt(rs.getString("VALID_STRT_DT"));
				 dto.setEmpNo(rs.getString("EMP_NO"));
				 dto.setEmpNm(rs.getString("EMP_NM"));
				 dto.setGradeNm(rs.getString("GRADE_NM"));
				 dto.setEndDt(rs.getString("VALID_END_DT"));
				 dto.setDetails(rs.getString("DETAILS"));
				 dto.setRegDt(rs.getString("REG_DT"));
				 dto.setDeptNm(rs.getString("DEPT_NM"));

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
	public EmployeeDTO selectdeptName(String empNo) {
//	public EmployeeDTO selectDeptName(String empNo) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		EmployeeDTO dto = new EmployeeDTO();
		try {
			sql = "SELECT e.DEPT_CD, d.DEPT_NM FROM TB_EMP e JOIN TB_DEPT d ON e.DEPT_CD = d.DEPT_CD WHERE e.EMP_NO= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, empNo);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto.setDeptCd(rs.getString("DEPT_CD"));
				dto.setDeptNm(rs.getString("DEPT_NM"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dto;
	}
	
	@Override
	public List<DeptMoveDTO> selectDeptMove(String empNo) {
		
		return null;
	}

	   @Override
	   public EmployeeDTO selectGradeName(String empNo) {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;
	    EmployeeDTO dto = new EmployeeDTO();

	    try {
	        sql = " SELECT e.DEPT_CD, d.DEPT_NM, e.GRADE_CD, e.EMP_NM, FROM TB_EMP e, JOIN TB_DEPT d ON e.DEPT_CD = d.DEPT_CD, WHERE e.EMP_NO = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, empNo);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            dto = new EmployeeDTO();
	            dto.setDeptCd(rs.getString("DEPT_CD"));
	            dto.setDeptNm(rs.getString("DEPT_NM"));
	            dto.setGradeCd(rs.getString("GRADE_CD"));
	            dto.setEmpNm(rs.getString("EMP_NM"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 
	    return dto;
	}



	
	

}
