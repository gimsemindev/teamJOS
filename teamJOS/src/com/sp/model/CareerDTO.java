package com.sp.model;

/**
 * <h2>CareerDTO (경력 정보 DTO)</h2>
 *
 * <p>TB_CAREER 테이블의 데이터를 담는 Data Transfer Object 입니다.</p>
 *
 * <ul>
 * <li>사원 번호</li>
 * <li>경력 근무지 (회사명)</li>
 * <li>경력 시작일 및 종료일</li>
 * <li>경력 상세 내용</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_EMP_CAREER_HIST</p>
 * <p><b>작성자:</b> 이지영</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class CareerDTO {
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
	private String empNo;
    
    /** 회사명 (COMPANY_NAME VARCHAR2(100)) */
    private String companyName;
    
    /** 경력 시작 일자 (START_DT DATE) */
    private String startDt; // 경력시작일자
    
    /** 경력 종료 일자 (END_DT DATE) */
    private String endDt; // 경력종료일자
    
    /** 경력 상세 내용 (DETAILS VARCHAR2(500)) */
    private String details; // 상세
    
    /** @return 사원 번호 */
	public String getEmpNo() {
		return empNo;
	}
    
    /** @param empNo 사원 번호 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
    
    /** @return 회사명 */
	public String getCompanyName() {
		return companyName;
	}
    
    /** @param companyName 회사명 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

    /** @return 경력 시작 일자 */
	public String getStartDt() {
		return startDt;
	}
    
    /** @param startDt 경력 시작 일자 */
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
    
    /** @return 경력 종료 일자 */
	public String getEndDt() {
		return endDt;
	}
    
    /** @param endDt 경력 종료 일자 */
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
    
    /** @return 경력 상세 내용 */
	public String getDetails() {
		return details;
	}
    
    /** @param details 경력 상세 내용 */
	public void setDetails(String details) {
		this.details = details;
	}
    
}