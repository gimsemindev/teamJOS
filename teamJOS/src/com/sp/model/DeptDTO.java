package com.sp.model;

public class DeptDTO {
	private String deptCd;        // DEPT_CD CHAR(6)
    private String deptNm;        // DEPT_NM CHAR(10)
    private String superDeptCd;   // SUPER_DEPT_CD CHAR(6)
    private String useYn;         // USE_YN CHAR(1)
    private String regDt;      // REG_DT DATE
    
    public DeptDTO() {
    }
    
    public DeptDTO(String deptCd, String deptNm, String superDeptCd,
    		String useYn, String regDt) {
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.superDeptCd = superDeptCd;
        this.useYn = useYn;
        this.regDt = regDt;
    }
    
    
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
	public String getSuperDeptCd() {
		return superDeptCd;
	}
	public void setSuperDeptCd(String superDeptCd) {
		this.superDeptCd = superDeptCd;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
    
    
}
