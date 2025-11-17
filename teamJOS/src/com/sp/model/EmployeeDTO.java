package com.sp.model;

/**
 * <h2>EmployeeDTO (사원 기본 정보 DTO)</h2>
 *
 * <p>TB_EMP 테이블의 데이터를 담는 핵심 Data Transfer Object 입니다.
 * 사원의 인사 기록, 개인 정보 및 로그인 관련 정보를 포함합니다.</p>
 *
 * <ul>
 * <li>사원 번호, 이름, 주민등록번호, 주소, 입사일</li>
 * <li>소속 부서, 직급, 재직 상태, 계약 형태 등 코드 정보</li>
 * <li>이메일, 비밀번호, 권한 레벨 등 시스템 접근 정보</li>
 * <li>코드명(Name) 필드는 조회 시 JOIN을 통해 추가적으로 매핑되는 정보입니다.</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_EMP</p>
 * <p><b>작성자:</b>  이지영</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class EmployeeDTO {
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
	private String empNo;         // EMP_NO CHAR(5)
    
    /** 사원 이름 (EMP_NM VARCHAR2(50)) */
    private String empNm;         // EMP_NM VARCHAR2(50)
    
    /** 주민등록번호 (RRN CHAR(13)) */
    private String rrn;           // RRN CHAR(13)
    
    /** 사원 주소 (EMP_ADDR VARCHAR2(100)) */
    private String empAddr;       // EMP_ADDR VARCHAR2(100)
    
    /** 입사일 (HIRE_DT DATE) */
    private String hireDt;        // HIRE_DT DATE
    
    /** 부서 코드 (DEPT_CD CHAR(6)) */
    private String deptCd;        // DEPT_CD CHAR(6)
    
    /** 직급 코드 (GRADE_CD CHAR(2)) */
    private String gradeCd;       // GRADE_CD CHAR(2)
    
    /** 재직 상태 코드 (EMP_STAT_CD CHAR(1)) */
    private String empStatCd;     // EMP_STAT_CD CHAR(1)
    
    /** 계약 형태 코드 (CONTRACT_TP_CD CHAR(1)) */
    private String contractTpCd;  // CONTRACT_TP_CD CHAR(1)
    
    /** 이메일 (EMAIL VARCHAR2(50)) */
    private String email;         // EMAIL VARCHAR2(50)
    
    /** 비밀번호 (PWD VARCHAR2(100)) */
    private String pwd;           // PWD VARCHAR2(100)
    
    /** 등록일시 (REG_DT DATE) */
    private String regDt;         // REG_DT DATE
    
    /** 퇴사일 (RETIRE_DT DATE) */
    private String retireDt;      // RETIRE_DT DATE
    
    /** 사용 여부 (USE_YN CHAR(1)) */
    private String useYn;         // USE_YN CHAR(1)
    
    /** 권한 레벨 코드 (LEVEL_CODE CHAR(2)) */
    private String levelCode;     // LEVEL_CODE CHAR(2)
    
    /** 부서명 (조회 시 매핑) */
    private String deptNm;
    
    /** 직급명 (조회 시 매핑) */
    private String gradeNm;
    
    /** 재직 상태명 (조회 시 매핑) */
    private String empStatNm;
    
    /** 계약 형태명 (조회 시 매핑) */
    private String contractTpNm;
    
    /** 기본 생성자 */
    public EmployeeDTO() {
    	
    }
    
    /**
     * 전체 필드 생성자 (코드명 제외)
     *
     * @param empNo 사원 번호
     * @param empNm 사원 이름
     * @param rrn 주민등록번호
     * @param empAddr 사원 주소
     * @param hireDt 입사일
     * @param deptCd 부서 코드
     * @param gradeCd 직급 코드
     * @param empStatCd 재직 상태 코드
     * @param contractTpCd 계약 형태 코드
     * @param email 이메일
     * @param pwd 비밀번호
     * @param regDt 등록일시
     * @param retireDt 퇴사일
     * @param useYn 사용 여부
     * @param levelCode 권한 레벨 코드
     */
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
    
    
    /** @return 사원 번호 */
	public String getEmpNo() {
		return empNo;
	}
    
    /** @param empNo 사원 번호 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
    
    /** @return 사원 이름 */
	public String getEmpNm() {
		return empNm;
	}
    
    /** @param empNm 사원 이름 */
	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}
    
    /** @return 주민등록번호 */
	public String getRrn() {
		return rrn;
	}
    
    /** @param rrn 주민등록번호 */
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
    
    /** @return 사원 주소 */
	public String getEmpAddr() {
		return empAddr;
	}
    
    /** @param empAddr 사원 주소 */
	public void setEmpAddr(String empAddr) {
		this.empAddr = empAddr;
	}
    
    /** @return 입사일 */
	public String getHireDt() {
		return hireDt;
	}
    
    /** @param hireDt 입사일 */
	public void setHireDt(String hireDt) {
		this.hireDt = hireDt;
	}
    
    /** @return 부서 코드 */
	public String getDeptCd() {
		return deptCd;
	}
    
    /** @param deptCd 부서 코드 */
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
    
    /** @return 직급 코드 */
	public String getGradeCd() {
		return gradeCd;
	}
    
    /** @param gradeCd 직급 코드 */
	public void setGradeCd(String gradeCd) {
		this.gradeCd = gradeCd;
	}
    
    /** @return 재직 상태 코드 */
	public String getEmpStatCd() {
		return empStatCd;
	}
    
    /** @param empStatCd 재직 상태 코드 */
	public void setEmpStatCd(String empStatCd) {
		this.empStatCd = empStatCd;
	}
    
    /** @return 계약 형태 코드 */
	public String getContractTpCd() {
		return contractTpCd;
	}
    
    /** @param contractTpCd 계약 형태 코드 */
	public void setContractTpCd(String contractTpCd) {
		this.contractTpCd = contractTpCd;
	}
    
    /** @return 이메일 */
	public String getEmail() {
		return email;
	}
    
    /** @param email 이메일 */
	public void setEmail(String email) {
		this.email = email;
	}
    
    /** @return 비밀번호 */
	public String getPwd() {
		return pwd;
	}
    
    /** @param pwd 비밀번호 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
    
    /** @return 등록일시 */
	public String getRegDt() {
		return regDt;
	}
    
    /** @param regDt 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
    
    /** @return 퇴사일 */
	public String getRetireDt() {
		return retireDt;
	}
    
    /** @param retireDt 퇴사일 */
	public void setRetireDt(String retireDt) {
		this.retireDt = retireDt;
	}
    
    /** @return 사용 여부 */
	public String getUseYn() {
		return useYn;
	}
    
    /** @param useYn 사용 여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
    
    /** @return 권한 레벨 코드 */
	public String getLevelCode() {
		return levelCode;
	}
    
    /** @param levelCode 권한 레벨 코드 */
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
    
    /** @return 부서명 */
	public String getDeptNm() {
		return deptNm;
	}
    
    /** @param deptNm 부서명 */
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
    
    /** @return 직급명 */
	public String getGradeNm() {
		return gradeNm;
	}
    
    /** @param gradeNm 직급명 */
	public void setGradeNm(String gradeNm) {
		this.gradeNm = gradeNm;
	}
    
    /** @return 재직 상태명 */
	public String getEmpStatNm() {
		return empStatNm;
	}
    
    /** @param empStatNm 재직 상태명 */
	public void setEmpStatNm(String empstatNm) {
		this.empStatNm = empstatNm;
	}
    
    /** @return 계약 형태명 */
	public String getContractTpNm() {
		return contractTpNm;
	}
    
    /** @param contractTpNm 계약 형태명 */
	public void setContractTpNm(String contractTpNm) {
		this.contractTpNm = contractTpNm;
	}
	
    
}