package com.sp.dao;

import java.sql.SQLException;
import java.util.List;

import com.sp.model.CareerDTO;
import com.sp.model.DeptMoveDTO;
import com.sp.model.EmployeeDTO;
import com.sp.model.HistoryDTO;
import com.sp.model.PromotionDTO;
import com.sp.model.RewardDTO;

/**
 * <h2>EmpDAO (사원 관리 데이터 접근 인터페이스)</h2>
 * 
 * <p>사원 관리 기능에 대한 데이터베이스 접근 메서드를 정의하는 인터페이스입니다.</p>
 * <ul>
 *   <li>사원 등록, 수정, 조회</li>
 *   <li>부서 이동 및 진급 관리</li>
 *   <li>경력, 자격증, 포상 등록</li>
 *   <li>퇴직 승인 및 이력 조회</li>
 * </ul>
 * 
 * <p><b>Service ID 범위:</b> EMP_INS_001 ~ EMP_SEL_012</p>
 * 
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 홍길동</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */

public interface EmpDAO {
	
	 /** EMP_INS_001 : 사원정보등록 */
    int insertEmployee(EmployeeDTO emp) throws SQLException;

    /** EMP_UPD_002 : 사원정보수정 */
    int updateEmployee(String empNo, String col, String con) throws SQLException;

    /** EMP_UPD_003 : 부서이동 */
    int updateDeptMove(DeptMoveDTO move) throws SQLException;
    

    /** EMP_UPD_004 : 진급관리 */
    int updatePromotion(PromotionDTO promotion) throws SQLException;

    /** EMP_UPD_008 : 퇴직신청결재 */
    int updateRetireApproval(String empNo, String status) throws SQLException;

    /** EMP_INS_009 : 경력등록 */
    int insertCareer(CareerDTO career) throws SQLException;

    /** EMP_INS_010 : 자격증 및 포상등록 */
    int insertLicense(RewardDTO reward) throws SQLException;

    /** EMP_SEL_005 : 사번조회 */
    EmployeeDTO selectByEmpNo(String empNo);

    /** EMP_SEL_006 : 이름조회 */
    List<EmployeeDTO> selectByName(String name);

    /** EMP_SEL_007 : 전체조회 */
    List<EmployeeDTO> selectAll();

    /** EMP_SEL_011 : 이력조회 */
    List<HistoryDTO> selectHistory(String empNo);

    /** EMP_SEL_012 : 부서이동이력조회 */
    List<DeptMoveDTO> selectDeptMove(String empNo);
    
    /** : 부서조회 */
    EmployeeDTO selectDeptName(String empNo);
    
    /** : 직급조회 */
    EmployeeDTO selectGradeName(String empNo);
    
    /** 부서코드 유효성 검증 */
    boolean isValidDeptCd(String deptCd);

    /** 직급코드 유효성 검증 */
    boolean isValidGradeCd(String gradeCd);

    /** 계약구분코드 유효성 검증 */
    boolean isValidContractTpCd(String contractTpCd);

    /** 권한레벨코드 유효성 검증 */
    boolean isValidLevelCode(String levelCode);
}

