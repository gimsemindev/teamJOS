package com.sp.model;

/**
 * <h2>LoginDTO (로그인 세션 정보 DTO)</h2>
 *
 * <p>사용자가 시스템에 로그인했을 때 세션에 저장되거나 전송되는 핵심 인증 및 식별 정보를 담는
 * Data Transfer Object 입니다. 사원 또는 관리자 정보가 매핑됩니다.</p>
 *
 * <ul>
 * <li>로그인 아이디 (사원 번호 또는 관리자 ID)</li>
 * <li>사용자 이름 및 권한 레벨</li>
 * <li>직급명 및 소속 부서 코드</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_EMP, TB_ROLE 등 (인증 정보 조합)</p>
 * <p><b>작성자:</b> 황선호</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class LoginDTO {
    
    /** 로그인 아이디 (EMP_NO 또는 ADMIN_ID) */
	private String memberId;

    /** 사용자 이름 */
    private String memberName;

    /** 사용자 권한 (role: admin, employee 등) */
    private String role;
    
    /** 직급명 */
    private String gradeNm;
    
    /** 부서 코드 (DEPT_CD) */
    private String deptCd;
    
    // 기본 생성자
    /** 기본 생성자 */
    public LoginDTO() {}

    /**
     * 모든 필드를 받는 생성자
     *
     * @param memberId 로그인 아이디
     * @param memberName 사용자 이름
     * @param gradeNm 직급명
     * @param deptCd 부서 코드
     * @param role 사용자 권한
     */
    public LoginDTO(String memberId, String memberName, String gradeNm, String deptCd,String role) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.gradeNm = gradeNm;
        this.deptCd = deptCd;
        this.role = role;
    }

    // Getter / Setter
    /** @return 로그인 아이디 */
    public String getMemberId() {
        return memberId;
    }

    /** @param memberId 로그인 아이디 */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /** @return 사용자 이름 */
    public String getMemberName() {
        return memberName;
    }

    /** @param memberName 사용자 이름 */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    
    /** @return 직급명 */
    public String getGradeNm() {
        return gradeNm;
    }
    
    /** @param gradeNm 직급명 */
    public void setGradeNm(String gradeNm) {
        this.gradeNm = gradeNm;
    }
    
    /** @return 부서 코드 */
    public String getDeptCd() {
		return deptCd;
	}

    /** @param deptCd 부서 코드 */
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}

    /** @return 사용자 권한 */
	public String getRole() {
        return role;
    }

    /** @param role 사용자 권한 */
    public void setRole(String role) {
        this.role = role;
    }

    // 로그인 정보 요약 출력용
    @Override
    public String toString() {
    	return String.format("[ID: %s, 이름: %s, 직급: %s, 권한: %s]", memberId, memberName, gradeNm, role);
    }
}