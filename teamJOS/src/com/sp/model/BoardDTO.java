package com.sp.model;

public class BoardDTO {
    
    private int boardNo;         // BOARD_ NUMBER(10)
    private String empNo;           // EMP_NO CHAR(5)
    private String empNm;           // EMP_NM VARCHAR2(50
    private String title;           // TITLE VARCHAR2(200)
    private String content;         // CONTENT VARCHAR2(600)
    private String regDtm;          // REG_DTM TIMESTAMP
    private String updateDtm;       // UPDATE_DTM TIMESTAMP

    // 기본 생성자
    public BoardDTO() {
    }

    // 모든 필드를 초기화하는 생성자
    public BoardDTO(int boardNo, String empNo, String empNm, String title, String content, String regDtm, String updateDtm) {
        this.boardNo = boardNo;
        this.empNo = empNo;
        this.setEmpNm(empNm);
        this.title = title;
        this.content = content;
        this.regDtm = regDtm;
        this.updateDtm = updateDtm;
    }

    // Getter / Setter
    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegDtm() {
        return regDtm;
    }

    public void setRegDtm(String regDtm) {
        this.regDtm = regDtm;
    }

    public String getUpdateDtm() {
        return updateDtm;
    }

    public void setUpdateDtm(String updateDtm) {
        this.updateDtm = updateDtm;
    }

	public String getEmpNm() {
		return empNm;
	}

	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}

    
}