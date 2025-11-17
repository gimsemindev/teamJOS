package com.sp.model;

/**
 * <h2>VacationDTO (휴가 신청 정보 DTO)</h2>
 *
 * <p>TB_VACATION 테이블의 데이터를 담는 Data Transfer Object 입니다.
 * 사원의 휴가 신청 및 승인 관련 정보를 관리합니다.</p>
 *
 * <ul>
 * <li>휴가 신청 고유 일련번호</li>
 * <li>신청 사원 번호</li>
 * <li>휴가 시작일 및 종료일</li>
 * <li>승인 여부 및 휴가 사유(메모)</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_VACATION</p>
 * <p><b>작성자:</b> 황선호</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class VacationDTO {       
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
    private String empNo;            // EMP_NO CHAR(5)
    
    /** 휴가 시작일 (START_DT DATE/TIMESTAMP) */
    private String startDt;   // START_DT TIMESTAMP
    
    /** 휴가 종료일 (END_DT DATE/TIMESTAMP) */
    private String endDt;     // END_DT TIMESTAMP
    
    /** 승인 여부 (APPROVER_YN CHAR(1) - Y: 승인, N: 대기) */
    private String approverYn;       // APPROVER_YN CHAR(1)
    
    /** 휴가 사유/메모 (VACATION_MEMO VARCHAR2(600)) */
    private String vacationMemo;     // VACATION_MEMO VARCHAR2(600
    
    /** 휴가 신청 일련번호 (VACATION_SEQ NUMBER(10), 시퀀스) */
    private int vacationSeq;	// VACATION_ SEQ(10) 
	
	/** 기본 생성자 */
	public VacationDTO() {
    }
	
    /**
     * 모든 필드를 초기화하는 생성자
     *
     * @param empNo 사원 번호
     * @param startDt 휴가 시작일
     * @param endDt 휴가 종료일
     * @param approverYn 승인 여부
     * @param vacationMemo 휴가 사유/메모
     * @param vacationSeq 휴가 신청 일련번호
     */
	public VacationDTO(String empNo, String startDt, String endDt, String approverYn,
			String vacationMemo, int vacationSeq) {
        this.empNo = empNo;
        this.startDt = startDt;
        this.endDt = endDt;
        this.approverYn = approverYn;
        this.vacationMemo = vacationMemo;
        this.vacationSeq = vacationSeq;
    }
	
	
    /** @return 휴가 신청 일련번호 */
	public int getVacationSeq() {
		return vacationSeq;
	}

    /** @param vacationSeq 휴가 신청 일련번호 */
	public void setVacationSeq(int vacationSeq) {
		this.vacationSeq = vacationSeq;
	}

    /** @return 사원 번호 */
	public String getEmpNo() {
		return empNo;
	}
    
    /** @param empNo 사원 번호 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
    
    /** @return 휴가 시작일 */
	public String getStartDt() {
		return startDt;
	}
    
    /** @param startDt 휴가 시작일 */
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
    
    /** @return 휴가 종료일 */
	public String getEndDt() {
		return endDt;
	}
    
    /** @param endDt 휴가 종료일 */
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
    
    /** @return 승인 여부 */
	public String getApproverYn() {
		return approverYn;
	}
    
    /** @param approverYn 승인 여부 */
	public void setApproverYn(String approverYn) {
		this.approverYn = approverYn;
	}
    
    /** @return 휴가 사유/메모 */
	public String getVacationMemo() {
		return vacationMemo;
	}
    
    /** @param vacationMemo 휴가 사유/메모 */
	public void setVacationMemo(String vacationMemo) {
		this.vacationMemo = vacationMemo;
	}

}