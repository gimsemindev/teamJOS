package com.sp.dao;

import java.sql.SQLException;
import java.util.List;

import com.sp.model.CareerDTO;
import com.sp.model.DeptMoveDTO;
import com.sp.model.EmployeeDTO;
import com.sp.model.HistoryDTO;
import com.sp.model.PromotionDTO;
import com.sp.model.RetireDTO;
import com.sp.model.RewardDTO;

/**
 * <h2>EmpDAO (사원 관리 데이터 접근 인터페이스)</h2>
 *
 * <p>사원 관리 기능에 대한 데이터베이스 접근 메서드를 정의하는 인터페이스입니다.</p>
 * * <ul>
 * <li>사원 등록, 수정, 조회 기능 제공</li>
 * <li>부서 이동 및 진급 정보 수정</li>
 * <li>경력, 자격증, 포상 등록 및 이력 조회</li>
 * <li>퇴직 신청 및 승인 관리 기능</li>
 * </ul>
 * * <p><b>Service ID 범위:</b> EMP_INS_001 ~ EMP_LIS_019</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 이지영</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */

public interface EmpDAO {
	
	/** * EMP_INS_001 : 신규 사원 정보를 등록합니다.
	 * @param emp 등록할 사원 정보 DTO
	 * @return 등록된 레코드 수 (1)
	 * @throws SQLException DB 접근 오류 발생 시
	 */
    int insertEmployee(EmployeeDTO emp) throws SQLException;

    /** * EMP_UPD_002 : 사원 정보의 특정 컬럼을 수정합니다.
     * @param empNo 수정 대상 사원 번호
     * @param col 수정할 컬럼명
     * @param con 수정할 내용
     * @return 수정된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int updateEmployee(String empNo, String col, String con) throws SQLException;

    /** * EMP_UPD_003 : 사원의 부서 이동 이력을 등록하고 현재 부서 정보를 수정합니다.
     * @param move 부서 이동 정보 DTO
     * @return 수정된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int updateDeptMove(DeptMoveDTO move) throws SQLException;
    

    /** * EMP_UPD_004 : 사원의 진급 이력을 등록하고 현재 직급 정보를 수정합니다.
     * @param promotion 진급 정보 DTO
     * @return 수정된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int updatePromotion(PromotionDTO promotion) throws SQLException;

    /** * EMP_UPD_008 : 특정 퇴직 신청 건을 승인 처리(결재)합니다.
     * @param retireSeq 퇴직 신청 일련번호
     * @return 수정된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int updateRetireApproval(int retireSeq) throws SQLException;

    /** * EMP_INS_009 : 사원의 경력 정보를 등록합니다.
     * @param career 등록할 경력 정보 DTO
     * @return 등록된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int insertCareer(CareerDTO career) throws SQLException;

    /** * EMP_INS_010 : 사원의 자격증 또는 포상 정보를 등록합니다.
     * @param reward 등록할 자격증/포상 정보 DTO
     * @return 등록된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int insertLicense(RewardDTO reward) throws SQLException;

    /** * EMP_SEL_005 : 사원 번호를 기준으로 단일 사원 정보를 조회합니다.
     * @param empNo 조회할 사원 번호
     * @return 조회된 사원 정보 DTO
     */
    EmployeeDTO selectByEmpNo(String empNo);

    /** * EMP_SEL_006 : 이름을 기준으로 사원 목록을 조회합니다.
     * @param name 조회할 사원 이름
     * @return 이름이 일치하는 사원 정보 리스트
     */
    List<EmployeeDTO> selectByName(String name);

    /** * EMP_SEL_007 : 전체 사원 목록을 조회합니다.
     * @return 전체 사원 정보 리스트
     */
    List<EmployeeDTO> selectAll();

    /** * EMP_SEL_011 : 특정 사원의 진급 이력 목록을 조회합니다.
     * @param empNo 조회할 사원 번호
     * @return 진급 이력 정보 리스트
     */
    List<HistoryDTO> selectGradeHis(String empNo);

    /** * EMP_SEL_012 : 특정 사원의 경력 이력 목록을 조회합니다.
     * @param empNo 조회할 사원 번호
     * @return 경력 이력 정보 리스트
     * @throws SQLException DB 접근 오류 발생 시
     */
    List<HistoryDTO> selectCareerHis(String empNo) throws SQLException;
    
    /** * EMP_SEL_013 : 특정 사원의 자격증 및 포상 이력 목록을 조회합니다.
     * @param empNo 조회할 사원 번호
     * @return 자격증 및 포상 이력 정보 리스트
     */
    List<HistoryDTO> selectCertHis(String empNo);
    
    /** * EMP_SEL_013 : 전체 사원의 자격증 및 포상 이력 목록을 조회합니다.
     * @return 전체 자격증 및 포상 이력 정보 리스트
     */
    List<HistoryDTO> selectCertHisAll();
    
    /** * EMP_SEL_011 : 전체 사원의 진급 이력 목록을 조회합니다.
     * @return 전체 진급 이력 정보 리스트
     */
    List<HistoryDTO> selectGradeHisAll();

    /** * EMP_SEL_012 : 전체 사원의 경력 이력 목록을 조회합니다.
     * @return 전체 경력 이력 정보 리스트
     */
    List<HistoryDTO> selectCareerHisAll();
    
    /** * EMP_SEL_014 : 사원 번호로 해당 사원의 부서 이름을 조회합니다.
     * @param empNo 조회할 사원 번호
     * @return 부서 이름 정보가 포함된 사원 DTO
     */
    EmployeeDTO selectDeptName(String empNo);
    
    /** * EMP_SEL_015 : 사원 번호로 해당 사원의 직급 이름을 조회합니다.
     * @param empNo 조회할 사원 번호
     * @return 직급 이름 정보가 포함된 사원 DTO
     */
    EmployeeDTO selectGradeName(String empNo);
    
    /** * EMP_BOL_015 : 주어진 부서 코드의 유효성을 검증합니다.
     * @param deptCd 검증할 부서 코드
     * @return 유효한 코드이면 true, 아니면 false
     */
    boolean isValidDeptCd(String deptCd);

    /** * EMP_BOL_016 : 주어진 직급 코드의 유효성을 검증합니다.
     * @param gradeCd 검증할 직급 코드
     * @return 유효한 코드이면 true, 아니면 false
     */
    boolean isValidGradeCd(String gradeCd);

    /** * EMP_BOL_020 : 주어진 계약 구분 코드의 유효성을 검증합니다.
     * @param contractTpCd 검증할 계약 구분 코드
     * @return 유효한 코드이면 true, 아니면 false
     */
    boolean isValidContractTpCd(String contractTpCd);

    /** * EMP_BOL_021 : 주어진 권한 레벨 코드의 유효성을 검증합니다.
     * @param levelCode 검증할 권한 레벨 코드
     * @return 유효한 코드이면 true, 아니면 false
     */
    boolean isValidLevelCode(String levelCode);

    /** * EMP_BOL_017 : 주어진 이메일의 중복 여부를 검증합니다.
     * @param email 검증할 이메일 주소
     * @return 이미 존재하는 이메일이면 true, 아니면 false
     */
	boolean isEmailExists(String email);
	
	/** * EMP_SEL_018 : CSV 파일에서 사원 정보를 로드(불러오기)합니다.
	 */
	void loadEmployeeInfo();
	
	/** * EMP_LIS_019 : 퇴직 신청 대기 리스트를 조회합니다.
	 * @return 퇴직 신청 정보 DTO 리스트
	 */
	List <RetireDTO> listRetire();
	
	/** * EMP_INS_013 : 신규 퇴직 신청 정보를 등록합니다.
	 * @param dto 퇴직 신청 정보 DTO
	 * @return 등록된 레코드 수 (1)
	 * @throws SQLException DB 접근 오류 발생 시
	 */
	int insertRetire(RetireDTO dto) throws SQLException;
}