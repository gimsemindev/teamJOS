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

/**
 * <h2>EmpDAOImpl (사원 관리 데이터 접근 구현체)</h2>
 *
 * <p>EmpDAO 인터페이스를 구현한 클래스로, 사원 관리 기능(등록, 수정, 조회) 및 인사 이동(부서 이동, 진급),
 * 이력 관리(경력, 자격증, 포상), 퇴직 신청/승인 기능을 실제 데이터베이스와 연동하여 처리합니다.</p>
 *
 * <ul>
 * <li>사원 등록 및 기본 정보 수정</li>
 * <li>부서 이동 및 진급 이력 관리</li>
 * <li>경력, 자격증, 포상 이력 등록 및 조회</li>
 * <li>퇴직 신청 및 관리자 승인 (프로시저 호출)</li>
 * <li>코드 유효성 검증 및 CSV 파일 로드</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> EMP_INS_001 ~ EMP_LIS_019</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 이지영</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class EmpDAOImpl implements EmpDAO{
	private Connection conn = DBConn.getConnection();
	private LoginInfo loginInfo;
	
	public EmpDAOImpl() {
		
	}
	
	public EmpDAOImpl(LoginInfo loginInfo) {
		this.conn = DBConn.getConnection();
		this.loginInfo = loginInfo;
	}
	
	/**
	 * EMP_INS_001 : 신규 사원 정보를 등록하고, 동시에 직급 이력 테이블(TB_EMP_GRADE_HIST)에 초기 직급 정보를 추가합니다.
	 *
	 * @param emp 등록할 사원 정보 DTO
	 * @return 등록된 레코드 수 (2건)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int insertEmployee(EmployeeDTO emp) throws SQLException{
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql;
		String sqlGd;
		int result = 0;
		
		try {
			sql = "INSERT INTO /*EMP_INS_001*/ TB_EMP(EMP_NO, EMP_NM, RRN, EMP_ADDR, HIRE_DT, DEPT_CD, GRADE_CD, EMP_STAT_CD, CONTRACT_TP_CD, EMAIL, PWD, REG_DT, LEVEL_CODE) "
		               + " VALUES(?, ?, ?, ?, SYSDATE, ?, ?, 'A', ?, ?, ?, SYSDATE, ?)";
		         
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
			
			sqlGd = "INSERT INTO /* EMP_INS_001 */ TB_EMP_GRADE_HIST "
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
	
	/**
	 * EMP_UPD_002 : 사원 정보의 특정 컬럼을 수정합니다.
	 *
	 * @param empNo 수정 대상 사원 번호
	 * @param col 수정할 컬럼명
	 * @param con 수정할 내용
	 * @return 수정된 레코드 수
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int updateEmployee(String empNo, String col, String con) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE /* EMP_UPD_002 */ TB_EMP SET " + col + " = ? WHERE EMP_NO = ?";
		
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, con);
			pstmt.setString(2, empNo);
		
			pstmt.executeUpdate();	
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return 0; // 메서드 구현부에서 0을 반환하므로 0 반환
	}

	/**
	 * EMP_UPD_003 : 사원의 현재 부서 코드(DEPT_CD)를 변경(부서 이동)합니다.
	 *
	 * @param move 부서 이동 정보 DTO (새로운 부서 코드 포함)
	 * @return 수정된 레코드 수
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int updateDeptMove(DeptMoveDTO move) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE /* EMP_UPD_003 */ TB_EMP SET DEPT_CD = ? WHERE EMP_NO = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, move.getNewDeptCd());
			pstmt.setString(2, move.getEmpNo());
		
			pstmt.executeUpdate();	
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return 0; // 메서드 구현부에서 0을 반환하므로 0 반환
	}

	/**
	 * EMP_UPD_004 : 사원의 직급 정보를 변경하고, 직급 이력 테이블(TB_EMP_GRADE_HIST)에 새로운 이력을 추가합니다.
	 * <p>이전 직급 이력의 VALID_END_DT를 현재 날짜-1로 업데이트하고, TB_EMP의 GRADE_CD를 업데이트합니다.</p>
	 *
	 * @param promotion 진급 정보 DTO
	 * @return 업데이트 및 등록된 레코드 총 수
	 * @throws SQLException SQL 실행 실패 시
	 */
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
			
			sqlup = "UPDATE /* EMP_UPD_004 */ TB_EMP_GRADE_HIST SET VALID_END_DT = SYSDATE-1 "
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
			
			sqlupm = "UPDATE /* EMP_UPD_004 */ TB_EMP SET GRADE_CD = ? WHERE EMP_NO = ?";
			
			pstmt3 = conn.prepareStatement(sqlupm);
			pstmt3.setString(1, promotion.getNewGradeCd());
			pstmt3.setString(2, promotion.getEmpNo());
		
			result += pstmt3.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(pstmt2);
			DBUtil.close(pstmt3);
		}
		return result;
	}
	
	/**
	 * EMP_UPD_008 : 특정 퇴직 신청 건을 승인 처리(결재)하고 사원 정보를 업데이트하는 프로시저를 호출합니다.
	 *
	 * @param retireSeq 퇴직 신청 일련번호
	 * @return 처리 결과 (1)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int updateRetireApproval(int retireSeq) throws SQLException{
		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		
		try {
			sql = """
					CALL /* EMP_UPD_008 */ SP_APPROVE_RETIRE_SUSI(?)
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
	
	/**
	 * EMP_INS_009 : 사원의 경력 정보를 등록합니다.
	 *
	 * @param career 등록할 경력 정보 DTO
	 * @return 등록된 레코드 수
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int insertCareer(CareerDTO career) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO /* EMP_INS_009 */ TB_EMP_CAREER_HIST(CAREER_SEQ, EMP_NO, PREV_COMP_NM, CAREER_STRT_DT, CAREER_END_DT, DETAILS) "
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
		return 0; // 메서드 구현부에서 0을 반환하므로 0 반환
	}

	/**
	 * EMP_INS_010 : 사원의 자격증 또는 포상 정보를 등록합니다.
	 *
	 * @param reward 등록할 자격증/포상 정보 DTO
	 * @return 등록된 레코드 수
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int insertLicense(RewardDTO reward) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO /* EMP_INS_010 */ TB_EMP_CERT(CERT_SEQ, EMP_NO, CERT_NM, ISSUE_ORG_NM, ISSUE_DT) "
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
		return 0; // 메서드 구현부에서 0을 반환하므로 0 반환
	}

	/**
	 * EMP_SEL_005 : 사원 번호를 기준으로 단일 사원의 상세 정보를 조회합니다.
	 *
	 * @param empNo 조회할 사원 번호
	 * @return 조회된 사원 정보 DTO (없으면 null)
	 */
	@Override
	public EmployeeDTO selectByEmpNo(String empNo) {
		EmployeeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = " SELECT /* EMP_SEL_005 */ e.EMP_NO, e.EMP_NM, e.RRN, e.EMP_ADDR, TO_CHAR(e.HIRE_DT, 'YYYY-MM-DD') HIRE_DT, "
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

	/**
	 * EMP_SEL_006 : 이름을 기준으로 사원 목록을 조회합니다.
	 *
	 * @param name 조회할 사원 이름
	 * @return 이름이 포함된 사원 정보 리스트
	 */
	@Override
	public List<EmployeeDTO> selectByName(String name) {
		List<EmployeeDTO> list = new ArrayList<EmployeeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT /* EMP_SEL_006 */ e.EMP_NO, e.EMP_NM, e.RRN, e.EMP_ADDR, TO_CHAR(e.HIRE_DT, 'YYYY-MM-DD') HIRE_DT, "
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

	/**
	 * EMP_SEL_007 : 전체 사원 목록을 조회합니다.
	 *
	 * @return 전체 사원 정보 리스트
	 */
	@Override
	public List<EmployeeDTO> selectAll() {
		List<EmployeeDTO> list = new ArrayList<EmployeeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT /* EMP_SEL_007 */ e.EMP_NO, e.EMP_NM, e.RRN, e.EMP_ADDR, TO_CHAR(e.HIRE_DT, 'YYYY-MM-DD') HIRE_DT, "
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


	
	/**
	 * EMP_SEL_012 : 특정 사원의 경력 이력 목록을 조회합니다.
	 *
	 * @param empNo 조회할 사원 번호
	 * @return 경력 이력 정보 리스트
	 */
	@Override
	public List<HistoryDTO> selectCareerHis(String empNo) {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT /* EMP_SEL_012 */ e.EMP_NO, e.EMP_NM, c.PREV_COMP_NM, TO_CHAR(c.CAREER_STRT_DT, 'YYYY-MM-DD') CAREER_STRT_DT, TO_CHAR(c.CAREER_END_DT, 'YYYY-MM-DD') CAREER_END_DT, c.DETAILS, TO_CHAR(c.REG_DT, 'YYYY-MM-DD') REG_DT, c.APPRV_D "
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
	
	/**
	 * EMP_SEL_013 : 특정 사원의 자격증 및 포상 이력 목록을 조회합니다.
	 *
	 * @param empNo 조회할 사원 번호
	 * @return 자격증 및 포상 이력 정보 리스트
	 */
	@Override
	public List<HistoryDTO> selectCertHis(String empNo) {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT /* EMP_SEL_013 */ e.EMP_NO, e.EMP_NM, c.CERT_NM, c.ISSUE_ORG_NM, TO_CHAR(c.ISSUE_DT, 'YYYY-MM-DD') ISSUE_DT, TO_CHAR(c.REG_DT, 'YYYY-MM-DD') REG_DT "
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
	
	/**
	 * EMP_SEL_011 : 특정 사원의 직급 변동 이력 목록을 조회합니다.
	 *
	 * @param empNo 조회할 사원 번호
	 * @return 직급 이력 정보 리스트
	 */
	@Override
	public List<HistoryDTO> selectGradeHis(String empNo) {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT /* EMP_SEL_011 */ TO_CHAR(VALID_STRT_DT, 'YYYY-MM-DD') VALID_STRT_DT, e.EMP_NO, e.EMP_NM, g.GRADE_NM, TO_CHAR(VALID_END_DT, 'YYYY-MM-DD') VALID_END_DT, DETAILS, TO_CHAR(gh.REG_DT, 'YYYY-MM-DD') REG_DT, d.DEPT_NM "
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
	
	/**
	 * EMP_SEL_013 : 전체 사원의 자격증 및 포상 이력 목록을 조회합니다.
	 *
	 * @return 전체 자격증 및 포상 이력 정보 리스트
	 */
	@Override
	public List<HistoryDTO> selectCertHisAll() {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT /* EMP_SEL_013 */ e.EMP_NO, e.EMP_NM, c.CERT_NM, c.ISSUE_ORG_NM, TO_CHAR(c.ISSUE_DT, 'YYYY-MM-DD') ISSUE_DT, TO_CHAR(c.REG_DT, 'YYYY-MM-DD') REG_DT "
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
	
	/**
	 * EMP_SEL_011 : 전체 사원의 직급 변동 이력 목록을 조회합니다.
	 *
	 * @return 전체 직급 이력 정보 리스트
	 */
	@Override
	public List<HistoryDTO> selectGradeHisAll() {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT /* EMP_SEL_011 */ TO_CHAR(VALID_STRT_DT, 'YYYY-MM-DD') VALID_STRT_DT, e.EMP_NO, e.EMP_NM, g.GRADE_NM, TO_CHAR(VALID_END_DT, 'YYYY-MM-DD') VALID_END_DT, DETAILS, TO_CHAR(gh.REG_DT, 'YYYY-MM-DD') REG_DT, d.DEPT_NM "
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
	
	/**
	 * EMP_SEL_012 : 전체 사원의 경력 이력 목록을 조회합니다.
	 *
	 * @return 전체 경력 이력 정보 리스트
	 */
	@Override
	public List<HistoryDTO> selectCareerHisAll() {
		List<HistoryDTO> list = new ArrayList<HistoryDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT /* EMP_SEL_012 */ e.EMP_NO, e.EMP_NM, c.PREV_COMP_NM, TO_CHAR(c.CAREER_STRT_DT, 'YYYY-MM-DD') CAREER_STRT_DT, TO_CHAR(c.CAREER_END_DT, 'YYYY-MM-DD') CAREER_END_DT, c.DETAILS, TO_CHAR(c.REG_DT, 'YYYY-MM-DD') REG_DT, c.APPRV_D "
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
	
	/**
	 * EMP_SEL_014 : 사원 번호를 기준으로 해당 사원의 부서 정보(코드 및 이름)를 조회합니다.
	 *
	 * @param empNo 조회할 사원 번호
	 * @return 부서 정보가 포함된 EmployeeDTO
	 */
	@Override
	public EmployeeDTO selectDeptName(String empNo) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		EmployeeDTO dto = new EmployeeDTO();
		try {
			sql = "SELECT /* EMP_SEL_014 */ e.DEPT_CD, d.DEPT_NM "
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

	/**
	 * EMP_SEL_015 : 사원 번호를 기준으로 해당 사원의 직급 정보(코드 및 이름)를 조회합니다.
	 *
	 * @param empNo 조회할 사원 번호
	 * @return 직급 정보가 포함된 EmployeeDTO
	 */
	@Override
	public EmployeeDTO selectGradeName(String empNo) {
		PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;
	    EmployeeDTO dto = new EmployeeDTO();

	    try {
	        sql = " SELECT /* EMP_SEL_015 */ e.DEPT_CD, d.DEPT_NM, e.GRADE_CD, e.EMP_NM "
	        		+ " FROM TB_EMP e JOIN TB_DEPT d ON e.DEPT_CD = d.DEPT_CD"
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
	   
	/**
	 * EMP_BOL_015 : 주어진 부서 코드의 유효성을 검증합니다.
	 *
	 * @param deptCd 검증할 부서 코드
	 * @return 유효한 코드이면 true, 아니면 false
	 */
	@Override
	public boolean isValidDeptCd(String deptCd) {
		String sql = "SELECT /* EMP_BOL_015 */ COUNT(*) FROM TB_DEPT WHERE DEPT_CD = ?";
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
	
	/**
	 * EMP_BOL_016 : 주어진 직급 코드의 유효성을 검증합니다.
	 *
	 * @param gradeCd 검증할 직급 코드
	 * @return 유효한 코드이면 true, 아니면 false
	 */
	@Override
	public boolean isValidGradeCd(String gradeCd) {
		String sql = "SELECT /* EMP_BOL_016 */ COUNT(*) FROM TB_GRADE WHERE GRADE_CD = ?";
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

	/**
	 * EMP_BOL_020 : 주어진 계약 구분 코드의 유효성을 검증합니다.
	 *
	 * @param contractTpCd 검증할 계약 구분 코드
	 * @return 유효한 코드이면 true, 아니면 false
	 */
	@Override
	public boolean isValidContractTpCd(String contractTpCd) {
		String sql = "SELECT /* EMP_BOL_020 */ COUNT(*) FROM TB_EMP_CNTRT_TYPE WHERE CONTRACT_TP_CD = ?";
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

	/**
	 * EMP_BOL_021 : 주어진 권한 레벨 코드의 유효성을 검증합니다.
	 *
	 * @param levelCode 검증할 권한 레벨 코드
	 * @return 유효한 코드이면 true, 아니면 false
	 */
	@Override
	public boolean isValidLevelCode(String levelCode) {
		String sql = "SELECT /* EMP_BOL_021 */ COUNT(*) FROM TB_ROLE WHERE LEVEL_CODE = ?";
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

	/**
	 * EMP_BOL_017 : 주어진 이메일의 중복 여부를 검증합니다.
	 *
	 * @param email 검증할 이메일 주소
	 * @return 이미 존재하는 이메일이면 true, 아니면 false
	 */
	@Override
	public boolean isEmailExists(String email) {
	    String sql = "SELECT /* EMP_BOL_017 */ COUNT(*) FROM TB_EMP WHERE EMAIL = ?";
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

	/**
	 * EMP_SEL_018 : CSV 파일에서 사원 정보를 읽어와 데이터베이스에 일괄 삽입합니다.
	 *
	 * <p>이 메소드는 트랜잭션 관리를 포함하여 대량 데이터 로드를 처리합니다.</p>
	 */
	@Override
	public void loadEmployeeInfo() {
        PreparedStatement pstmt = null;
        
        String csvPath = "LOAD_TB_EMP.csv";
        
	    String SQL = """
	            INSERT /* EMP_SEL_018 */ INTO TB_EMP (
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

	                // 첫줄은 컬럼명으로 스킵
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
   
	                    // 500건마다 커밋
	                    if (count % 500 == 0) {
	                        pstmt.executeBatch();
	                        conn.commit();
	                        System.out.println(count + "건 커밋 완료...");
	                    }
	                }

	                pstmt.executeBatch();
	                conn.commit();
	                System.out.println(" 총 " + count + "건 INSERT 완료!");

	            } catch (Exception e) {
	                e.printStackTrace();
	                try { 
	                	if (conn != null) conn.rollback(); 
	                } catch (Exception ex) {}
	            } finally {
	            	try {
	            	    conn.setAutoCommit(true);
	            	    } catch(Exception e) {  	
	            	    }
	                try { if (pstmt != null) 
	                	  pstmt.close(); 
	                    } catch (Exception e) {	
	                    }
	                try { if (conn != null) ; 
	                    } catch (Exception e) {    	
	                    }
	            }
	}

	/**
	 * EMP_LIS_019 : 승인 대기 중인 퇴직 신청 리스트를 조회합니다.
	 *
	 * @return 퇴직 신청 정보 DTO 리스트
	 */
	@Override
	public List<RetireDTO> listRetire() {
		List<RetireDTO> list = new ArrayList<RetireDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = """
					SELECT /* EMP_LIS_019 */ RETIRE_SEQ, EMP_NO, REG_DT, APPROVER_YN, RETIRE_MEMO 
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

	/**
	 * EMP_INS_013 : 신규 퇴직 신청 정보를 등록합니다.
	 *
	 * @param dto 퇴직 신청 정보 DTO
	 * @return 등록된 레코드 수 (1)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int insertRetire(RetireDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = """
					INSERT INTO /* EMP_INS_013 */ TB_EMP_RETIRE(RETIRE_SEQ, EMP_NO, REG_DT, APPROVER_YN, RETIRE_MEMO) 
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