package com.sp.model;

/**
 * <h2>AttendanceDTO (근태 정보 DTO)</h2>
 *
 * <p>TB_ATTENDANCE 테이블의 데이터를 담는 Data Transfer Object 입니다.</p>
 *
 * <ul>
 * <li>사원의 출퇴근 기록 및 근무 시간 정보</li>
 * <li>근태 상태를 나타내는 코드</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_ATD</p>
 * <p><b>작성자:</b> 오다은</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class AttendanceDTO {

    /** 근태 번호 (ATD_NO NUMBER(10), 시퀀스) */
    private String atdNo;           // ATD_ NUMBER(10) - 시퀀스
    
    /** 사원 번호 (EMP_NO CHAR(5)) */
    private String empNo;           // EMP_NO CHAR(5)
    
    /** 출근 시간 (CHECK_IN TIMESTAMP) */
    private String checkIn;         // CHECK_IN TIMESTAMP
    
    /** 퇴근 시간 (CHECK_OUT TIMESTAMP) */
    private String checkOut;        // CHECK_OUT TIMESTAMP
    
    /** 총 근무 시간 (WORK_HOURS NUMBER(5,2)) */
    private String workHours;       // WORK_HOURS NUMBER(5,2)
    
    /** 근태 상태 코드 (ATD_STATUS_CD CHAR(2) - 정상, 지각, 조퇴 등) */
    private String atdStatusCd;     // ATD_STATUS_CD CHAR(2)
    
    /** 등록일시 (REG_DT DATE/TIMESTAMP) */
    private String regDt;

    
    /** 기본 생성자 */
    public AttendanceDTO() {
    }

    /**
     * 모든 필드를 초기화하는 생성자
     *
     * @param atdNo 근태 번호
     * @param empNo 사원 번호
     * @param checkIn 출근 시간
     * @param checkOut 퇴근 시간
     * @param workHours 총 근무 시간
     * @param atdStatusCd 근태 상태 코드
     * @param regDt 등록일시
     */
    public AttendanceDTO(String atdNo, String empNo, String checkIn, String checkOut, String workHours, String atdStatusCd, String regDt) {
        this.atdNo = atdNo;
        this.empNo = empNo;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.workHours = workHours;
        this.atdStatusCd = atdStatusCd;
        this.regDt = regDt;
    }

    /** @return 근태 번호 */
    public String getAtdNo() {
        return atdNo;
    }

    /** @param atdNo 근태 번호 */
    public void setAtdNo(String atdNo) {
        this.atdNo = atdNo;
    }

    /** @return 사원 번호 */
    public String getEmpNo() {
        return empNo;
    }

    /** @param empNo 사원 번호 */
    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    /** @return 출근 시간 */
    public String getCheckIn() {
        return checkIn;
    }

    /** @param checkIn 출근 시간 */
    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    /** @return 퇴근 시간 */
    public String getCheckOut() {
        return checkOut;
    }

    /** @param checkOut 퇴근 시간 */
    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    /** @return 총 근무 시간 */
    public String getWorkHours() {
        return workHours;
    }

    /** @param workHours 총 근무 시간 */
    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    /** @return 근태 상태 코드 */
    public String getAtdStatusCd() {
        return atdStatusCd;
    }

    /** @param atdStatusCd 근태 상태 코드 */
    public void setAtdStatusCd(String atdStatusCd) {
        this.atdStatusCd = atdStatusCd;
    }

    /** @return 등록일시 */
	public String getRegDt() {
		return regDt;
	}

    /** @param regDt 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

    
}