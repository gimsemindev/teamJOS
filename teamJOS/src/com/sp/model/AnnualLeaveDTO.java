package com.sp.model;

public class AnnualLeaveDTO {
    
	private int leaveSeq;	 // LEAVE_SEQ(10)
	private String deptNm;   // DEPT_NM VARCHAR2(50) 
	private String gradeNm;  // GRADE_NM(50)
	private String empNo;    // EMP_NO CHAR(5)
	private String empNm;    // EMP_NM VARCHAR2(50)
	private String hireDt;   // HIRE_DT DATE 
	private int totalDays;	 // TOTAL_DAYS NUMBER(3,1) 
	private int usedDays;	 // USED_DAYS NUMBER(3,1) 
	private int remainDays;	 // REMAIN_DAYS NUMBER(3,1)
	private String useYn;    // USE_YN CHAR(1)              
	
	public int getLeaveSeq() {
		return leaveSeq;
	}
	public void setLeaveSeq(int leaveSeq) {
		this.leaveSeq = leaveSeq;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getGradeNm() {
		return gradeNm;
	}
	public void setGradeNm(String gradeNm) {
		this.gradeNm = gradeNm;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getEmpNm() {
		return empNm;
	}
	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}
	public int getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}
	public int getUsedDays() {
		return usedDays;
	}
	public void setUsedDays(int usedDays) {
		this.usedDays = usedDays;
	}
	public int getRemainDays() {
		return remainDays;
	}
	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getHireDt() {
		return hireDt;
	}
	public void setHireDt(String hireDt) {
		this.hireDt = hireDt;
	}
	
	
}
