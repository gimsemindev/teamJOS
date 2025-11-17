package com.sp.model;

/**
 * <h2>HistoryDTO (통합 이력 정보 DTO)</h2>
 *
 * <p>사원의 다양한 이력(인사이동, 경력, 자격증 등)을 통합적으로 조회하거나 전달하기 위해
 * 여러 테이블의 필드를 조합한 Data Transfer Object 입니다.</p>
 *
 * <ul>
 * <li>사원 기본 정보 (사번, 이름)</li>
 * <li>기간 정보 (시작일, 종료일, 등록일)</li>
 * <li>경력, 발령, 자격증 등 이력 종류별 상세 정보 필드</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_EMP_GRADE_HIST, TB_CAREER, TB_EMP_CAREER_HIST 등 조합</p>
 * <p><b>작성자:</b> 이지영</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class HistoryDTO {
    
    /** 시작일 (경력 시작일, 발령 시작일 등) */
	private String startDt;
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
	private String empNo;
    
    /** 사원 이름 (EMP_NM) */
	private String empNm;
    
    /** 직급명 (GRADE_NM) */
	private String gradeNm;
    
    /** 종료일 (경력 종료일, 발령 종료일 등) */
	private String endDt;
    
    /** 상세 내용 (발령 사유, 경력 내용, 교육 상세 등) */
	private String details;
    
    /** 등록일시 (REG_DT) */
	private String regDt;
    
    /** 부서명 (DEPT_NM) */
	private String deptNm;
    
    /** 이전 회사명 (경력 정보용) */
	private String prevCompNm;
    
    /** 승인일 (APPROVAL_DT) */
	private String apprvD;
    
    /** 자격증명 (CERT_NM) */
	private String certNm;
    
    /** 발급 기관명 (ISSUE_ORG_NM) */
	private String issueOrgNm;
    
    /** 발급일 (ISSUE_DT) */
	private String issueDt;
	
    /** @return 시작일 */
	public String getStartDt() {
		return startDt;
	}
    
    /** @param startDt 시작일 */
	public void setStartDt(String startDt) {
		this.startDt = startDt;
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
    
    /** @return 직급명 */
	public String getGradeNm() {
		return gradeNm;
	}
    
    /** @param gradeNm 직급명 */
	public void setGradeNm(String gradeNm) {
		this.gradeNm = gradeNm;
	}
    
    /** @return 종료일 */
	public String getEndDt() {
		return endDt;
	}
    
    /** @param endDt 종료일 */
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
    
    /** @return 상세 내용 */
	public String getDetails() {
		return details;
	}
    
    /** @param details 상세 내용 */
	public void setDetails(String details) {
		this.details = details;
	}
    
    /** @return 등록일시 */
	public String getRegDt() {
		return regDt;
	}
    
    /** @param regDt 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
    
    /** @return 부서명 */
	public String getDeptNm() {
		return deptNm;
	}
    
    /** @param deptNm 부서명 */
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
    
    /** @return 이전 회사명 */
	public String getPrevCompNm() {
		return prevCompNm;
	}
    
    /** @param prevCompNm 이전 회사명 */
	public void setPrevCompNm(String prevCompNm) {
		this.prevCompNm = prevCompNm;
	}
    
    /** @return 승인일 */
	public String getApprvD() {
		return apprvD;
	}
    
    /** @param apprvD 승인일 */
	public void setApprvD(String apprvD) {
		this.apprvD = apprvD;
	}
    
    /** @return 자격증명 */
	public String getCertNm() {
		return certNm;
	}
    
    /** @param certNm 자격증명 */
	public void setCertNm(String certNm) {
		this.certNm = certNm;
	}
    
    /** @return 발급 기관명 */
	public String getIssueOrgNm() {
		return issueOrgNm;
	}
    
    /** @param issueOrgNm 발급 기관명 */
	public void setIssueOrgNm(String issueOrgNm) {
		this.issueOrgNm = issueOrgNm;
	}
    
    /** @return 발급일 */
	public String getIssueDt() {
		return issueDt;
	}
    
    /** @param issueDt 발급일 */
	public void setIssueDt(String issueDt) {
		this.issueDt = issueDt;
	}
	
	
}