package com.sp.model;

/**
 * <h2>RetireDTO (퇴직 신청 정보 DTO)</h2>
 *
 * <p>사원의 퇴직 신청 및 승인 정보를 담는 Data Transfer Object 입니다.
 * TB_RETIRE 테이블과 매핑됩니다.</p>
 *
 * <ul>
 * <li>퇴직 신청 고유 일련번호 및 신청 사원 번호</li>
 * <li>신청일, 승인 여부, 퇴직 사유(메모)</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_RETIRE</p>
 * <p><b>작성자:</b> 황선호</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class RetireDTO {
    
    /** 퇴직 신청 일련번호 (RETIRE_SEQ NUMBER(10), 시퀀스) */
	private int retireSeq;
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
	private String empNo;
    
    /** 신청일/등록일 (REG_DT DATE) */
	private String regDt;
    
    /** 승인 여부 (APPROVER_YN CHAR(1) - Y: 승인, N: 대기) */
	private String approverYn;
    
    /** 퇴직 사유/메모 (RETIRE_MEMO VARCHAR2(500)) */
	private String retireMemo;
	
    /** 기본 생성자 */
	public RetireDTO() {
		
	}
	
    /**
     * 모든 필드를 초기화하는 생성자
     *
     * @param retireSeq 퇴직 신청 일련번호
     * @param empNo 사원 번호
     * @param regDt 신청일/등록일
     * @param approverYn 승인 여부
     * @param retireMemo 퇴직 사유/메모
     */
	public RetireDTO(int retireSeq, String empNo, String regDt, String approverYn, String retireMemo) {
		this.retireSeq = retireSeq;
		this.empNo = empNo;
		this.regDt = regDt;
		this.approverYn = approverYn;
		this.retireMemo = retireMemo;
	}
	
    /** @return 퇴직 신청 일련번호 */
	public int getRetireSeq() {
		return retireSeq;
	}
    
    /** @param retire_seq 퇴직 신청 일련번호 */
	public void setRetireSeq(int retire_seq) {
		this.retireSeq = retire_seq;
	}
    
    /** @return 사원 번호 */
	public String getEmpNo() {
		return empNo;
	}
    
    /** @param empNo 사원 번호 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
    
    /** @return 신청일/등록일 */
	public String getRegDt() {
		return regDt;
	}
    
    /** @param regDt 신청일/등록일 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
    
    /** @return 승인 여부 */
	public String getApproverYn() {
		return approverYn;
	}
    
    /** @param approverYn 승인 여부 */
	public void setApproverYn(String approverYn) {
		this.approverYn = approverYn;
	}
    
    /** @return 퇴직 사유/메모 */
	public String getRetireMemo() {
		return retireMemo;
	}
    
    /** @param retireMemo 퇴직 사유/메모 */
	public void setRetireMemo(String retireMemo) {
		this.retireMemo = retireMemo;
	}
	
	
}