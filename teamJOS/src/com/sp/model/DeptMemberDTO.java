package com.sp.model;

public class DeptMemberDTO {

	private String deptCd;        // DEPT_CD CHAR(6)
    private String deptNm;        // DEPT_NM CHAR(10)
    private String gradeNM;       // GRADE_NM VHARCHR2(50)
    private String cotractTpNM;   // CONTRACT_TP_NM VHARCHR2(50)
    private String empStatNM;     // EMP_STAT_NM VHARCHR2(50)
	private String empNo;         // EMP_NO CHAR(5)
    private String empNm;         // EMP_NM VARCHAR2(50)   
    private String hireDt;        // HIRE_DT DATE
    private String contactNo;      // CONTACT_NO VARCHAR2(20)
    private String email;         // EMAIL VARCHAR2(50)
	
    public String getDeptCd() {
		return deptCd;
	}
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getGradeNM() {
		return gradeNM;
	}
	public void setGradeNM(String gradeNM) {
		this.gradeNM = gradeNM;
	}
	public String getCotractTpNM() {
		return cotractTpNM;
	}
	public void setCotractTpNM(String cotractTpNM) {
		this.cotractTpNM = cotractTpNM;
	}
	public String getEmpStatNM() {
		return empStatNM;
	}
	public void setEmpStatNM(String empStatNM) {
		this.empStatNM = empStatNM;
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
	public String getHireDt() {
		return hireDt;
	}
	public void setHireDt(String hireDt) {
		this.hireDt = hireDt;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    
}
