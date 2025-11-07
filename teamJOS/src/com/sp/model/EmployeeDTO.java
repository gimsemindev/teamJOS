package com.sp.model;

public class EmployeeDTO {
	private String empNo;         // EMP_NO CHAR(5)
    private String empNm;         // EMP_NM VARCHAR2(50)
    private String rrn;           // RRN CHAR(13)
    private String empAddr;       // EMP_ADDR VARCHAR2(100)
    private String hireDt;        // HIRE_DT DATE
    private String deptCd;        // DEPT_CD CHAR(6)
    private String gradeCd;       // GRADE_CD CHAR(2)
    private String empStatCd;     // EMP_STAT_CD CHAR(1)
    private String contractTpCd;  // CONTRACT_TP_CD CHAR(1)
    private String email;         // EMAIL VARCHAR2(50)
    private String pwd;           // PWD VARCHAR2(100)
    private String regDt;         // REG_DT DATE
    private String retireDt;      // RETIRE_DT DATE
    private String useYn;         // USE_YN CHAR(1)
    private String levelCode;     // LEVEL_CODE CHAR(2)
    
    public EmployeeDTO() {
    	
    }
    //전체 생성자
    public EmployeeDTO(String empNo, String empNm, String rrn,
    		String empAddr, String hireDt, String deptCd,
    		String gradeCd, String empStatCd, 
    		String contractTpCd, String email, String pwd, String regDt,
    		String retireDt, String useYn, String levelCode) {
        this.empNo = empNo;
        this.empNm = empNm;
        this.rrn = rrn;
        this.empAddr = empAddr;
        this.hireDt = hireDt;
        this.deptCd = deptCd;
        this.gradeCd = gradeCd;
        this.empStatCd = empStatCd;
        this.contractTpCd = contractTpCd;
        this.email = email;
        this.pwd = pwd;
        this.regDt = regDt;
        this.retireDt = retireDt;
        this.useYn = useYn;
        this.levelCode = levelCode;
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
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	public String getEmpAddr() {
		return empAddr;
	}
	public void setEmpAddr(String empAddr) {
		this.empAddr = empAddr;
	}
	public String getHireDt() {
		return hireDt;
	}
	public void setHireDt(String hireDt) {
		this.hireDt = hireDt;
	}
	public String getDeptCd() {
		return deptCd;
	}
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	public String getGradeCd() {
		return gradeCd;
	}
	public void setGradeCd(String gradeCd) {
		this.gradeCd = gradeCd;
	}
	public String getEmpStatCd() {
		return empStatCd;
	}
	public void setEmpStatCd(String empStatCd) {
		this.empStatCd = empStatCd;
	}
	public String getContractTpCd() {
		return contractTpCd;
	}
	public void setContractTpCd(String contractTpCd) {
		this.contractTpCd = contractTpCd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getRetireDt() {
		return retireDt;
	}
	public void setRetireDt(String retireDt) {
		this.retireDt = retireDt;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
    
}
