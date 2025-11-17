package com.sp.model;

/**
 * <h2>DeptMoveDTO (부서 이동 정보 DTO)</h2>
 *
 * <p>사원의 부서 이동 이력 처리를 위해 필요한 정보를 담는 Data Transfer Object 입니다.</p>
 *
 * <ul>
 * <li>인사이동 대상 사원 번호</li>
 * <li>새롭게 배정될 부서 코드</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_EMP (업데이트 용)</p>
 * <p><b>작성자:</b> 이지영</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class DeptMoveDTO {
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
	private String empNo;
    
    /** 신규 부서 코드 (NEW_DEPT_CD CHAR(6)) */
	private String newDeptCd;
	
    /** @return 사원 번호 */
	public String getEmpNo() {
		return empNo;
	}
    
    /** @param empNo 사원 번호 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
    
    /** @return 신규 부서 코드 */
	public String getNewDeptCd() {
		return newDeptCd;
	}
    
    /** @param newDeptCd 신규 부서 코드 */
	public void setNewDeptCd(String newDeptCd) {
		this.newDeptCd = newDeptCd;
	}

	
	
}