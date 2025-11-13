package com.sp.model;

public class RetireDTO {
	private int retireSeq;
	private String empNo;
	private String regDt;
	private String approverYn;
	private String retireMemo;
	
	public RetireDTO() {
		
	}
	
	public RetireDTO(int retireSeq, String empNo, String regDt, String approverYn, String retireMemo) {
		this.retireSeq = retireSeq;
		this.empNo = empNo;
		this.regDt = regDt;
		this.approverYn = approverYn;
		this.retireMemo = retireMemo;
	}
	
	public int getRetireSeq() {
		return retireSeq;
	}
	public void setRetireSeq(int retire_seq) {
		this.retireSeq = retire_seq;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getApproverYn() {
		return approverYn;
	}
	public void setApproverYn(String approverYn) {
		this.approverYn = approverYn;
	}
	public String getRetireMemo() {
		return retireMemo;
	}
	public void setRetireMemo(String retireMemo) {
		this.retireMemo = retireMemo;
	}
	
	
}
