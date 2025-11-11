package com.sp.model;

public class VacationDTO {       
    private String empNo;            // EMP_NO CHAR(5)
    private String startDt;   // START_DT TIMESTAMP
    private String endDt;     // END_DT TIMESTAMP
    private String approverYn;       // APPROVER_YN CHAR(1)
    private String vacationMemo;     // VACATION_MEMO VARCHAR2(600
    private int vacationSeq;	// VACATION_ SEQ(10) 
	
	
	public VacationDTO() {
    }
	
	public VacationDTO(String empNo, String startDt, String endDt, String approverYn,
			String vacationMemo, int vacationSeq) {
        this.empNo = empNo;
        this.startDt = startDt;
        this.endDt = endDt;
        this.approverYn = approverYn;
        this.vacationMemo = vacationMemo;
        this.vacationSeq = vacationSeq;
    }
	
	
	public int getVacationSeq() {
		return vacationSeq;
	}

	public void setVacationSeq(int vacationSeq) {
		this.vacationSeq = vacationSeq;
	}

	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public String getApproverYn() {
		return approverYn;
	}
	public void setApproverYn(String approverYn) {
		this.approverYn = approverYn;
	}
	public String getVacationMemo() {
		return vacationMemo;
	}
	public void setVacationMemo(String vacationMemo) {
		this.vacationMemo = vacationMemo;
	}

}
