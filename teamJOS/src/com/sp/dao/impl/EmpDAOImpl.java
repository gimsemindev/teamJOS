package com.sp.dao.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.CallableStatement;
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
import com.sp.model.RetireDTO;
import com.sp.model.RewardDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;
import com.sp.util.LoginInfo;

public class EmpDAOImpl implements EmpDAO{
	private Connection conn = DBConn.getConnection();
	private LoginInfo loginInfo;
	
	public EmpDAOImpl() {
		
	}
	
	public EmpDAOImpl(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
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

	// 직급 변경(TB_EMP) 및 직급 변경 이력(TB_EMP_GRADE_HIST) 추가 메소드
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
	public int updateRetireApproval(int retireSeq) throws SQLException{
		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		
		try {
			sql = """
					CALL SP_APPROVE_RETIRE_SUSI(?)
					""";
			
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, retireSeq);
			cstmt.execute();
			
			result = 1;
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(cstmt);
		}
		return result;
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
					+ " VALUES(SQ_TB_EMP_CERT.NEXTVAL, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reward.getEmpNo());
			pstmt.setString(2, reward.getRewardName());
			pstmt.setString(3, reward.getIssuer());
			pstmt.setString(4, reward.getDate());
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


	
	// 경력 조회
	@Override
	public List<HistoryDTO> selectCareerHis(String empNo) {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT e.EMP_NO, e.EMP_NM, c.PREV_COMP_NM, TO_CHAR(c.CAREER_STRT_DT, 'YYYY-MM-DD') CAREER_STRT_DT, TO_CHAR(c.CAREER_END_DT, 'YYYY-MM-DD') CAREER_END_DT, c.DETAILS, TO_CHAR(c.REG_DT, 'YYYY-MM-DD') REG_DT, c.APPRV_D "
					+ " FROM TB_EMP_CAREER_HIST c "
					+ " JOIN TB_EMP e ON c.EMP_NO = e.EMP_NO "
					+ " WHERE c.EMP_NO = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, empNo);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				HistoryDTO dto = new HistoryDTO();
				
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setEmpNm(rs.getString("EMP_NM"));
				dto.setPrevCompNm(rs.getString("PREV_COMP_NM"));
				dto.setStartDt(rs.getString("CAREER_STRT_DT"));
				dto.setEndDt(rs.getString("CAREER_END_DT"));
				dto.setDetails(rs.getString("DETAILS"));
				dto.setRegDt(rs.getString("REG_DT"));
				dto.setApprvD(rs.getString("APPRV_D"));
				
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
	
	// 자격증 및 포상 이력 조회
	@Override
	public List<HistoryDTO> selectCertHis(String empNo) {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT e.EMP_NO, e.EMP_NM, c.CERT_NM, c.ISSUE_ORG_NM, TO_CHAR(c.ISSUE_DT, 'YYYY-MM-DD') ISSUE_DT, TO_CHAR(c.REG_DT, 'YYYY-MM-DD') REG_DT "
					+ " FROM TB_EMP_CERT c"
					+ " JOIN TB_EMP e ON c.EMP_NO = e.EMP_NO "
					+ " WHERE c.EMP_NO = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, empNo);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				HistoryDTO dto = new HistoryDTO();
				
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setEmpNm(rs.getString("EMP_NM"));
				dto.setCertNm(rs.getString("CERT_NM"));
				dto.setIssueOrgNm(rs.getString("ISSUE_ORG_NM"));
				dto.setIssueDt(rs.getString("ISSUE_DT"));
				dto.setRegDt(rs.getString("REG_DT"));
				
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
	
	// 직급 이력 조회
	@Override
	public List<HistoryDTO> selectGradeHis(String empNo) {
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
					+ " WHERE gh.EMP_NO = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, empNo);
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
	
	// 전체 사원 자격증 및 포상 이력 조회
	@Override
	public List<HistoryDTO> selectCertHisAll() {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT e.EMP_NO, e.EMP_NM, c.CERT_NM, c.ISSUE_ORG_NM, TO_CHAR(c.ISSUE_DT, 'YYYY-MM-DD') ISSUE_DT, TO_CHAR(c.REG_DT, 'YYYY-MM-DD') REG_DT "
					+ " FROM TB_EMP_CERT c"
					+ " JOIN TB_EMP e ON c.EMP_NO = e.EMP_NO ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				HistoryDTO dto = new HistoryDTO();
				
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setEmpNm(rs.getString("EMP_NM"));
				dto.setCertNm(rs.getString("CERT_NM"));
				dto.setIssueOrgNm(rs.getString("ISSUE_ORG_NM"));
				dto.setIssueDt(rs.getString("ISSUE_DT"));
				dto.setRegDt(rs.getString("REG_DT"));
				
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
	// 전체 사원 직급 이력 조회
	@Override
	public List<HistoryDTO> selectGradeHisAll() {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT TO_CHAR(VALID_STRT_DT, 'YYYY-MM-DD') VALID_STRT_DT, e.EMP_NO, e.EMP_NM, g.GRADE_NM, TO_CHAR(VALID_END_DT, 'YYYY-MM-DD') VALID_END_DT, DETAILS, TO_CHAR(gh.REG_DT, 'YYYY-MM-DD') REG_DT, d.DEPT_NM "
					+ " FROM TB_EMP_GRADE_HIST gh "
					+ " LEFT JOIN TB_EMP e ON e.EMP_NO = gh.EMP_NO "
					+ " LEFT JOIN TB_GRADE g ON g.GRADE_CD = gh.GRADE_CD "
					+ " LEFT JOIN TB_DEPT d ON gh.DEPT_CD = d.DEPT_CD ";
			
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
	// 전체 사원 경력 조회
	@Override
	public List<HistoryDTO> selectCareerHisAll() {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT e.EMP_NO, e.EMP_NM, c.PREV_COMP_NM, TO_CHAR(c.CAREER_STRT_DT, 'YYYY-MM-DD') CAREER_STRT_DT, TO_CHAR(c.CAREER_END_DT, 'YYYY-MM-DD') CAREER_END_DT, c.DETAILS, TO_CHAR(c.REG_DT, 'YYYY-MM-DD') REG_DT, c.APPRV_D "
					+ " FROM TB_EMP_CAREER_HIST c "
					+ " JOIN TB_EMP e ON c.EMP_NO = e.EMP_NO ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				HistoryDTO dto = new HistoryDTO();
				
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setEmpNm(rs.getString("EMP_NM"));
				dto.setPrevCompNm(rs.getString("PREV_COMP_NM"));
				dto.setStartDt(rs.getString("CAREER_STRT_DT"));
				dto.setEndDt(rs.getString("CAREER_END_DT"));
				dto.setDetails(rs.getString("DETAILS"));
				dto.setRegDt(rs.getString("REG_DT"));
				dto.setApprvD(rs.getString("APPRV_D"));
				
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
	
	// 부서명 조회
	@Override
	public EmployeeDTO selectDeptName(String empNo) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		EmployeeDTO dto = new EmployeeDTO();
		try {
			sql = "SELECT e.DEPT_CD, d.DEPT_NM "
					+ " FROM TB_EMP e JOIN TB_DEPT d ON e.DEPT_CD = d.DEPT_CD "
					+ " WHERE e.EMP_NO= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, empNo);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto.setDeptCd(rs.getString("DEPT_CD"));
				dto.setDeptNm(rs.getString("DEPT_NM"));
			}
		} catch (Exception e) {
			
		}
		return dto;
	}

	// 직급명 조회
	@Override
	public EmployeeDTO selectGradeName(String empNo) {
		PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;
	    EmployeeDTO dto = new EmployeeDTO();

	    try {
	        sql = " SELECT e.DEPT_CD, d.DEPT_NM, e.GRADE_CD, e.EMP_NM "
	        		+ " FROM TB_EMP e, JOIN TB_DEPT d ON e.DEPT_CD = d.DEPT_CD"
	        		+ " WHERE e.EMP_NO = ?";
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

	@Override
	public boolean isEmailExists(String email) {
	    String sql = "SELECT COUNT(*) FROM TB_EMP WHERE EMAIL = ?";
	    try (Connection conn = DBConn.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, email);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            int count = rs.getInt(1);
	            return count > 0; // count가 1 이상이면 이미 등록된 이메일
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // 예외 발생 시 기본 false 반환
	}

	@Override
	public void loadEmployeeInfo() {
        PreparedStatement pstmt = null;
        
        String csvPath = "LOAD_TB_EMP.csv";
        
	    String SQL = """
	            INSERT INTO TB_EMP (
	                EMP_NO, EMP_NM, RRN, EMP_ADDR, HIRE_DT, DEPT_CD, GRADE_CD,
	                EMP_STAT_CD, CONTRACT_TP_CD, EMAIL, PWD, REG_DT, RETIRE_DT,
	                USE_YN, LEVEL_CODE
	            ) VALUES (
	                ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?, ?, ?,
	                TO_DATE(?, 'YYYY-MM-DD'), NULL, ?, ?
	            )
	        """;

	        /**
	         * CSV 파일을 읽어서 TB_EMP에 일괄 Insert
	         * @param csvPath CSV 파일 전체 경로
	         */

	            try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
	                conn = DBConn.getConnection();
	                conn.setAutoCommit(false);
	                pstmt = conn.prepareStatement(SQL);

	                // 첫줄 헤더임으로 스킵
	                String header = br.readLine();
	                String line;
	                int count = 0;

	                while ((line = br.readLine()) != null) {
	                    String[] data = line.split(",", -1); // 빈값 허용

	                    pstmt.setString(1, data[0]);  // EMP_NO
	                    pstmt.setString(2, data[1]);  // EMP_NM
	                    pstmt.setString(3, data[2]);  // RRN
	                    pstmt.setString(4, data[3]);  // EMP_ADDR
	                    pstmt.setString(5, data[4]);  // HIRE_DT
	                    pstmt.setString(6, data[5]);  // DEPT_CD
	                    pstmt.setString(7, data[6]);  // GRADE_CD
	                    pstmt.setString(8, data[7]);  // EMP_STAT_CD
	                    pstmt.setString(9, data[8]);  // CONTRACT_TP_CD
	                    pstmt.setString(10, data[9]); // EMAIL
	                    pstmt.setString(11, data[10]); // PWD
	                    pstmt.setString(12, data[11]); // REG_DT
	                    pstmt.setString(13, data[13]); // USE_YN
	                    pstmt.setString(14, data[14]); // LEVEL_CODE

	                    pstmt.addBatch();
	                    count++;

	                    if (count % 500 == 0) {
	                        pstmt.executeBatch();
	                        conn.commit();
	                        System.out.println(count + "건 커밋 완료...");
	                    }
	                }

	                pstmt.executeBatch();
	                conn.commit();
	                System.out.println("✅ 총 " + count + "건 INSERT 완료!");

	            } catch (Exception e) {
	                e.printStackTrace();
	                try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
	            } finally {
	                try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	                try { if (conn != null) conn.close(); } catch (Exception e) {}
	            }	
	}

	@Override
	public List<RetireDTO> listRetire() {
		List<RetireDTO> list = new ArrayList<RetireDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = """
					SELECT RETIRE_SEQ, EMP_NO, REG_DT, APPROVER_YN, RETIRE_MEMO 
				      FROM TB_EMP_RETIRE
					 WHERE APPROVER_YN = 'N'
			      ORDER BY RETIRE_SEQ ASC
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RetireDTO dto = new RetireDTO();
				
				dto.setRetireSeq(rs.getInt("RETIRE_SEQ"));
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setRegDt(rs.getString("REG_DT"));
				dto.setApproverYn(rs.getNString("APPROVER_YN"));
				dto.setRetireMemo(rs.getString("RETIRE_MEMO"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		
		
		return list;
	}

	@Override
	public int insertRetire(RetireDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = """
					INSERT INTO TB_EMP_RETIRE(RETIRE_SEQ, EMP_NO, REG_DT, APPROVER_YN, RETIRE_MEMO) 
						VALUES (RETIRE_SEQ_SQ.NEXTVAL, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), 'N', ?)
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, loginInfo.loginMember().getMemberId());
			pstmt.setString(2, dto.getRegDt());
			pstmt.setString(3, dto.getRetireMemo());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return result;
	}

}

