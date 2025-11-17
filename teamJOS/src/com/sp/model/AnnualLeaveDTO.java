package com.sp.model;

/**
 * <h2>AnnualLeaveDTO (연차 정보 DTO)</h2>
 *
 * <p>사원의 연차 사용 현황 및 정보를 담는 Data Transfer Object 입니다.
 * 연차 정보는 TB_ANNUAL_LEAVE 테이블과 매핑됩니다.</p>
 *
 * <ul>
 * <li>연차 부여 일련번호 및 사원 기본 정보</li>
 * <li>총 연차 일수, 사용 연차 일수, 잔여 연차 일수 현황</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_ANNUAL_LEAVE</p>
 * <p><b>작성자:</b> 오다은</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class AnnualLeaveDTO {
    
    /** 연차 부여 일련번호 (LEAVE_SEQ NUMBER(10)) */
	private int leaveSeq;	 // LEAVE_SEQ(10)
    
    /** 부서명 (DEPT_NM VARCHAR2(50)) */
	private String deptNm;   // DEPT_NM VARCHAR2(50) 
    
    /** 직급명 (GRADE_NM VARCHAR2(50)) */
	private String gradeNm;  // GRADE_NM(50)
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
	private String empNo;    // EMP_NO CHAR(5)
    
    /** 사원 이름 (EMP_NM VARCHAR2(50)) */
	private String empNm;    // EMP_NM VARCHAR2(50)
    
    /** 입사일 (HIRE_DT DATE) */
	private String hireDt;   // HIRE_DT DATE 
    
    /** 총 연차 일수 (TOTAL_DAYS NUMBER(3,1)) */
	private int totalDays;	 // TOTAL_DAYS NUMBER(3,1) 
    
    /** 사용 연차 일수 (USED_DAYS NUMBER(3,1)) */
	private int usedDays;	 // USED_DAYS NUMBER(3,1) 
    
    /** 잔여 연차 일수 (REMAIN_DAYS NUMBER(3,1)) */
	private int remainDays;	 // REMAIN_DAYS NUMBER(3,1)
    
    /** 사용 여부 (USE_YN CHAR(1)) */
	private String useYn;    // USE_YN CHAR(1)              
	
    /** @return 연차 부여 일련번호 */
	public int getLeaveSeq() {
		return leaveSeq;
	}
    
    /** @param leaveSeq 연차 부여 일련번호 */
	public void setLeaveSeq(int leaveSeq) {
		this.leaveSeq = leaveSeq;
	}
    
    /** @return 부서명 */
	public String getDeptNm() {
		return deptNm;
	}
    
    /** @param deptNm 부서명 */
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
    
    /** @return 직급명 */
	public String getGradeNm() {
		return gradeNm;
	}
    
    /** @param gradeNm 직급명 */
	public void setGradeNm(String gradeNm) {
		this.gradeNm = gradeNm;
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
    
    /** @return 총 연차 일수 */
	public int getTotalDays() {
		return totalDays;
	}
    
    /** @param totalDays 총 연차 일수 */
	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}
    
    /** @return 사용 연차 일수 */
	public int getUsedDays() {
		return usedDays;
	}
    
    /** @param usedDays 사용 연차 일수 */
	public void setUsedDays(int usedDays) {
		this.usedDays = usedDays;
	}
    
    /** @return 잔여 연차 일수 */
	public int getRemainDays() {
		return remainDays;
	}
    
    /** @param remainDays 잔여 연차 일수 */
	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}
    
    /** @return 사용 여부 */
	public String getUseYn() {
		return useYn;
	}
    
    /** @param useYn 사용 여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
    
    /** @return 입사일 */
	public String getHireDt() {
		return hireDt;
	}
    
    /** @param hireDt 입사일 */
	public void setHireDt(String hireDt) {
		this.hireDt = hireDt;
	}
	
	
}