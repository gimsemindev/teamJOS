package com.sp.model;

/**
 * <h2>DeptMemberDTO (부서 소속 사원 정보 DTO)</h2>
 *
 * <p>특정 부서에 소속된 사원들의 핵심 정보를 담는 Data Transfer Object 입니다.
 * 조직도 또는 부서원 목록 조회 시 사용되는 조합 DTO입니다.</p>
 *
 * <ul>
 * <li>부서명, 직급명, 계약 구분명 등 코드명이 풀네임으로 매핑된 정보</li>
 * <li>사원 번호, 이름, 입사일, 연락처, 이메일 등 기본 정보</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_DEPT, TB_EMP, TB_CODE 등 여러 테이블 조합</p>
 * <p><b>작성자:</b> 김세민</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class DeptMemberDTO {

    /** 부서 코드 (DEPT_CD CHAR(6)) */
	private String deptCd;        // DEPT_CD CHAR(6)
    
    /** 부서명 (DEPT_NM CHAR(10)) */
    private String deptNm;        // DEPT_NM CHAR(10)
    
    /** 직급 이름 (GRADE_NM VARCHAR2(50)) */
    private String gradeNM;       // GRADE_NM VHARCHR2(50)
    
    /** 계약 형태 이름 (CONTRACT_TP_NM VARCHAR2(50)) */
    private String cotractTpNM;   // CONTRACT_TP_NM VHARCHR2(50)
    
    /** 재직 상태 이름 (EMP_STAT_NM VARCHAR2(50)) */
    private String empStatNM;     // EMP_STAT_NM VHARCHR2(50)
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
	private String empNo;         // EMP_NO CHAR(5)
    
    /** 사원 이름 (EMP_NM VARCHAR2(50)) */
    private String empNm;         // EMP_NM VARCHAR2(50)   
    
    /** 입사일 (HIRE_DT DATE) */
    private String hireDt;        // HIRE_DT DATE
    
    /** 연락처 (CONTACT_NO VARCHAR2(20)) */
    private String contactNo;      // CONTACT_NO VARCHAR2(20)
    
    /** 이메일 (EMAIL VARCHAR2(50)) */
    private String email;         // EMAIL VARCHAR2(50)
	
    /** @return 부서 코드 */
    public String getDeptCd() {
		return deptCd;
	}
    
    /** @param deptCd 부서 코드 */
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
    
    /** @return 부서명 */
	public String getDeptNm() {
		return deptNm;
	}
    
    /** @param deptNm 부서명 */
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
    
    /** @return 직급 이름 */
	public String getGradeNM() {
		return gradeNM;
	}
    
    /** @param gradeNM 직급 이름 */
	public void setGradeNM(String gradeNM) {
		this.gradeNM = gradeNM;
	}
    
    /** @return 계약 형태 이름 */
	public String getCotractTpNM() {
		return cotractTpNM;
	}
    
    /** @param cotractTpNM 계약 형태 이름 */
	public void setCotractTpNM(String cotractTpNM) {
		this.cotractTpNM = cotractTpNM;
	}
    
    /** @return 재직 상태 이름 */
	public String getEmpStatNM() {
		return empStatNM;
	}
    
    /** @param empStatNM 재직 상태 이름 */
	public void setEmpStatNM(String empStatNM) {
		this.empStatNM = empStatNM;
	}
    
    /** @return 사원 번호 */
	public String getEmpNo() {
		return empNo;
	}
    
    /** @param empNo 사원 번호 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
    
    /** @return 사원 이름 */
	public String getEmpNm() {
		return empNm;
	}
    
    /** @param empNm 사원 이름 */
	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}
    
    /** @return 입사일 */
	public String getHireDt() {
		return hireDt;
	}
    
    /** @param hireDt 입사일 */
	public void setHireDt(String hireDt) {
		this.hireDt = hireDt;
	}
    
    /** @return 연락처 */
	public String getContactNo() {
		return contactNo;
	}
    
    /** @param contactNo 연락처 */
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
    
    /** @return 이메일 */
	public String getEmail() {
		return email;
	}
    
    /** @param email 이메일 */
	public void setEmail(String email) {
		this.email = email;
	}
    
}