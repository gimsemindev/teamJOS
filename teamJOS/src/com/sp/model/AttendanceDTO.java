package com.sp.model;

public class AttendanceDTO {

    private String atdNo;           // ATD_ NUMBER(10) - 시퀀스
    private String empNo;           // EMP_NO CHAR(5)
    private String checkIn;         // CHECK_IN TIMESTAMP
    private String checkOut;        // CHECK_OUT TIMESTAMP
    private String workHours;       // WORK_HOURS NUMBER(5,2)
    private String atdStatusCd;     // ATD_STATUS_CD CHAR(2)

    
    public AttendanceDTO() {
    }

    // 모든 필드를 초기화하는 생성자
    public AttendanceDTO(String atdNo, String empNo, String checkIn, String checkOut, String workHours, String atdStatusCd) {
        this.atdNo = atdNo;
        this.empNo = empNo;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.workHours = workHours;
        this.atdStatusCd = atdStatusCd;
    }

    // Getter / Setter
    public String getAtdNo() {
        return atdNo;
    }

    public void setAtdNo(String atdNo) {
        this.atdNo = atdNo;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getWorkHours() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    public String getAtdStatusCd() {
        return atdStatusCd;
    }

    public void setAtdStatusCd(String atdStatusCd) {
        this.atdStatusCd = atdStatusCd;
    }

    
    
}

