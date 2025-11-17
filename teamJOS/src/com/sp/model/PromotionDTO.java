package com.sp.model;

/**
 * <h2>PromotionDTO (진급 발령 정보 DTO)</h2>
 *
 * <p>사원의 진급 발령 기록 및 관련 정보를 담는 Data Transfer Object 입니다.
 * TB_PROMOTION_HISTORY 테이블과 매핑될 수 있습니다.</p>
 *
 * <ul>
 * <li>인사이동 대상 사원 번호</li>
 * <li>현재 직급 코드 및 신규 진급 직급 코드</li>
 * <li>발령 기간 및 상세 내역</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_EMP, TB_GRADE</p>
 * <p><b>작성자:</b> 이지영</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class PromotionDTO {
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
	private String empNo;
    
    /** 현 직급 코드 (CURRENT_GRADE_CD CHAR(2)) */
	private String currentGradeCd;
    
    /** 신규 진급 직급 코드 (NEW_GRADE_CD CHAR(2)) */
	private String newGradeCd;
    
    /** 종료일 (END_DT DATE - 발령 이력 관리 시 사용) */
	private String endDt;
    
    /** 시작일 (START_DT DATE - 진급 발령일) */
	private String startDt;
    
    /** 진급 발령 상세 내역 (DETAILS VARCHAR2(500)) */
	private String details;
	
	
    /** @return 사원 번호 */
	public String getEmpNo() {
		return empNo;
	}
    
    /** @param empNo 사원 번호 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
    
    /** @return 현 직급 코드 */
	public String getCurrentGradeCd() {
		return currentGradeCd;
	}
    
    /** @param currentGradeCd 현 직급 코드 */
	public void setCurrentGradeCd(String currentGradeCd) {
		this.currentGradeCd = currentGradeCd;
	}
    
    /** @return 신규 진급 직급 코드 */
	public String getNewGradeCd() {
		return newGradeCd;
	}
    
    /** @param newGradeCd 신규 진급 직급 코드 */
	public void setNewGradeCd(String newGradeCd) {
		this.newGradeCd = newGradeCd;
	}
    
    /** @return 종료일 */
	public String getEndDt() {
		return endDt;
	}
    
    /** @param endDt 종료일 */
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
    
    /** @return 시작일 */
	public String getStartDt() {
		return startDt;
	}
    
    /** @param startDt 시작일 */
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
    
    /** @return 진급 발령 상세 내역 */
	public String getDetails() {
		return details;
	}
    
    /** @param details 진급 발령 상세 내역 */
	public void setDetails(String details) {
		this.details = details;
	}
	
	
}