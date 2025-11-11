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
		PreparedStatement pstmt2 = null;
		String sql;
		String sqlGd;
		int result = 0;
		
		try {
			sql = "INSERT INTO TB_EMP(EMP_NO, EMP_NM, RRN, EMP_ADDR, HIRE_DT, DEPT_CD, GRADE_CD, EMP_STAT_CD, CONTRACT_TP_CD, EMAIL, PWD, LEVEL_CODE) "
		               + " VALUES(?, ?, ?, ?, SYSDATE, ?, ?, 'A', ?, ?, ?, ?)";
		         
		         pstmt = conn.prepareStatement(sql);
		         pstmt.setString(1, emp.getEmpNo());
		         pstmt.setString(2, emp.getEmpNm());
		         pstmt.setString(3, emp.getRrn());
		         pstmt.setString(4, emp.getEmpAddr());
		         pstmt.setString(5, emp.getDeptCd());
		         pstmt.setString(6, emp.getGradeCd());
		         pstmt.setString(7, emp.getContractTpCd());
		         pstmt.setString(8, emp.getEmail());
		         pstmt.setString(9, emp.getPwd());
		         pstmt.setString(10, emp.getLevelCode());

			
			result += pstmt.executeUpdate();
			DBUtil.close(pstmt);
			
			sqlGd = "INSERT INTO TB_EMP_GRADE_HIST "
	        		+ " (EMP_NO, GRADE_CD, VALID_STRT_DT, VALID_END_DT, DETAILS, REG_DT, DEPT_CD) "
	                  + " VALUES (?, ?, SYSDATE, NULL, ?, SYSDATE, ?)";
			
			pstmt2 = conn.prepareStatement(sqlGd);
			pstmt2.setString(1, emp.getEmpNo());
			pstmt2.setString(2, emp.getGradeCd());
			pstmt2.setString(3, "입사 시 기본 직급 부여");
			pstmt2.setString(4, emp.getDeptCd());
			
			result += pstmt2.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(pstmt2);
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
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		
		String sqlup;
		String sqlinst;
		String sqlupm;
		
		int result =0;
		try {
			EmployeeDTO empDto = selectDeptName(promotion.getEmpNo());
	        String deptCd = empDto.getDeptCd();
			
			sqlup = "UPDATE TB_EMP_GRADE_HIST SET VALID_END_DT = SYSDATE-1 "
					+ " WHERE EMP_NO = ? AND VALID_END_DT IS NULL";
			
			pstmt = conn.prepareStatement(sqlup);
	        pstmt.setString(1, promotion.getEmpNo());
	        result += pstmt.executeUpdate();
			
	        sqlinst = "INSERT INTO TB_EMP_GRADE_HIST "
	        		+ " (EMP_NO, GRADE_CD, VALID_STRT_DT, VALID_END_DT, DETAILS, REG_DT, DEPT_CD) "
	                  + " VALUES (?, ?, SYSDATE, NULL, ?, SYSDATE, ?)";
			
			pstmt2 = conn.prepareStatement(sqlinst);
			pstmt2.setString(1, promotion.getEmpNo());
			pstmt2.setString(2, promotion.getNewGradeCd());
			pstmt2.setString(3, promotion.getDetails());
			pstmt2.setString(4, deptCd);
		
			result += pstmt2.executeUpdate();
			
			sqlupm = "UPDATE TB_EMP SET GRADE_CD = ? WHERE EMP_NO = ?";
			
			pstmt3 = conn.prepareStatement(sqlupm);
			pstmt3.setString(1, promotion.getNewGradeCd());
			pstmt3.setString(2, promotion.getEmpNo());
		
			result += pstmt3.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(pstmt2);
		}
		return result;
	}
	
	// 퇴직 신청 결재 메소드
	@Override
	public int updateRetireApproval(String empNo, String status) throws SQLException{
		PreparedStatement pstmt = null;
//		String sqlselect;
		String sqlupdate;
		
		try {
//			sqlselect = " SELECT * FROM 퇴직신청테이블 WHERE 조건 ";
			
//			pstmt = conn.prepareStatement(sqlselect);
			
//			pstmt.executeQuery();
			
			sqlupdate = " UPDATE TB_EMP SET EMP_STAT_CD = ? WHERE EMP_NO = ?";
			
			pstmt = conn.prepareStatement(sqlupdate);
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
	public EmployeeDTO selectDeptName(String empNo) {
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

	@Override
	public boolean isValidDeptCd(String deptCd) {
		String sql = "SELECT COUNT(*) FROM TB_DEPT WHERE DEPT_CD = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, deptCd);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isValidGradeCd(String gradeCd) {
		String sql = "SELECT COUNT(*) FROM TB_GRADE WHERE GRADE_CD = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, gradeCd);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isValidContractTpCd(String contractTpCd) {
		String sql = "SELECT COUNT(*) FROM TB_EMP_CNTRT_TYPE WHERE CONTRACT_TP_CD = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, contractTpCd);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isValidLevelCode(String levelCode) {
		String sql = "SELECT COUNT(*) FROM TB_ROLE WHERE LEVEL_CODE = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, levelCode);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	   



	
	

}
