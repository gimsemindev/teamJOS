package com.sp.model;

/**
 * <h2>RewardDTO (포상 정보 DTO)</h2>
 *
 * <p>TB_REWARD 테이블의 데이터를 담는 Data Transfer Object 입니다.
 * 사원의 포상 내역 관리에 사용됩니다.</p>
 *
 * <ul>
 * <li>포상 대상 사원 번호</li>
 * <li>포상명 및 포상 일자</li>
 * <li>포상을 수여한 기관 또는 발급자 정보</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_EMP_CERT</p>
 * <p><b>작성자:</b> 오다은</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class RewardDTO {
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
	private String empNo;
    
    /** 포상명 (REWARD_NAME VARCHAR2(100)) */
	private String rewardName;
    
    /** 포상 일자 (DATE DATE) */
	private String date;
    
    /** 수여 기관 또는 발급자 (ISSUER VARCHAR2(100)) */
	private String issuer;

    /** @return 사원 번호 */
	public String getEmpNo() {
		return empNo;
	}

    /** @param empNo 사원 번호 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

    /** @return 포상명 */
	public String getRewardName() {
		return rewardName;
	}

    /** @param rewardName 포상명 */
	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}

    /** @return 포상 일자 */
	public String getDate() {
		return date;
	}

    /** @param date 포상 일자 */
	public void setDate(String date) {
		this.date = date;
	}

    /** @return 수여 기관 또는 발급자 */
	public String getIssuer() {
		return issuer;
	}

    /** @param issuer 수여 기관 또는 발급자 */
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

}