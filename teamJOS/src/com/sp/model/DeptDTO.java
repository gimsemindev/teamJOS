package com.sp.model;

/**
 * <h2>DeptDTO (부서 정보 DTO)</h2>
 *
 * <p>TB_DEPT 테이블의 데이터를 담는 Data Transfer Object 입니다.</p>
 *
 * <ul>
 *   <li>부서 기본 정보(부서코드, 부서명, 내선번호 등)</li>
 *   <li>조직도 관련 정보(상위 부서 코드)</li>
 *   <li>사용 여부 및 등록일시</li>
 *   <li>부서별 인원 수 및 비율(통계 조회 시 사용)</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_DEPT</p>
 * <p><b>작성자:</b> 황선호, 이석준, 김세민</p>
 * <p><b>작성일:</b> 2025-11-16</p>
 * <p><b>버전:</b> 0.9</p> 
 */
public class DeptDTO {

    /** 부서 코드 (DEPT_CD CHAR(6)) */
	private String deptCd;

    /** 부서명 (DEPT_NM CHAR(10)) */
    private String deptNm;

    /** 내선번호 (EXT_NO VARCHAR2(20)) */
    private String extNo;

    /** 상위 부서 코드 (SUPER_DEPT_CD CHAR(6)) */
    private String superDeptCd;

    /** 사용 여부 (USE_YN CHAR(1)) */
    private String useYn;

    /** 등록일시 (REG_DT DATE → 문자열로 매핑됨) */
    private String regDt;

    /** 부서별 인원수 (통계 조회용) */
    private int deptCount;

    /** 부서별 인원 비율(%) (통계 조회용) */
    private int deptCountRatio;
    
    /** 기본 생성자 */
    public DeptDTO() {
    }

    /**
     * 전체 필드 생성자
     *
     * @param deptCd 부서 코드
     * @param deptNm 부서명
     * @param extNo 내선번호
     * @param superDeptCd 상위 부서 코드
     * @param useYn 사용 여부
     * @param regDt 등록일시
     */
    public DeptDTO(String deptCd, String deptNm, String extNo, String superDeptCd,
    		String useYn, String regDt) {
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.extNo = extNo;
        this.superDeptCd = superDeptCd;
        this.useYn = useYn;
        this.regDt = regDt;
    }
    
    /** @return 부서 코드 */
	public String getDeptCd() {
		return deptCd;
	}

    /** @param deptCd 부서 코드 */
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}

    /** @return 부서명 */
	public String getDeptNm() {
		return deptNm;
	}

    /** @param deptNm 부서명 */
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}

    /** @return 내선번호 */
	public String getExtNo() {
		return extNo;
	}

    /** @param extNo 내선번호 */
	public void setExtNo(String extNo) {
		this.extNo = extNo;
	}

    /** @return 상위 부서 코드 */
	public String getSuperDeptCd() {
		return superDeptCd;
	}

    /** @param superDeptCd 상위 부서 코드 */
	public void setSuperDeptCd(String superDeptCd) {
		this.superDeptCd = superDeptCd;
	}

    /** @return 사용 여부 */
	public String getUseYn() {
		return useYn;
	}

    /** @param useYn 사용 여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

    /** @return 등록일시 */
	public String getRegDt() {
		return regDt;
	}

    /** @param regDt 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

    /** @return 부서별 인원수 */
	public int getDeptCount() {
		return deptCount;
	}

    /** @param deptCount 부서별 인원수 */
	public void setDeptCount(int deptCount) {
		this.deptCount = deptCount;
	}

    /** @return 부서별 인원 비율(%) */
	public int getDeptCountRatio() {
		return deptCountRatio;
	}

    /** @param deptCountRatio 부서별 인원 비율(%) */
	public void setDeptCountRatio(int deptCountRatio) {
		this.deptCountRatio = deptCountRatio;
	}
}
