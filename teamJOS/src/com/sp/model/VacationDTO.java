package com.sp.model;

public class VacationDTO {
	private String vacationNo;         // VACATION_ SEQ(10) 
    private String empNo;            // EMP_NO CHAR(5)
    private String startDt;   // START_DT TIMESTAMP
    private String endDt;     // END_DT TIMESTAMP
    private String approverYn;       // APPROVER_YN CHAR(1)
    private String vacationMemo;     // VACATION_MEMO VARCHAR2(600
    
	
	
	public VacationDTO() {
    }
	
	public VacationDTO(String vacationNo, String empNo,
			String startDt, String endDt, String approverYn,
			String vacationMemo) {
        this.vacationNo = vacationNo;
        this.empNo = empNo;
        this.startDt = startDt;
        this.endDt = endDt;
        this.approverYn = approverYn;
        this.vacationMemo = vacationMemo;
    }
	
	public String getVacationNo() {
		return vacationNo;
	}
	public void setVacationNo(String vacationNo) {
		this.vacationNo = vacationNo;
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
