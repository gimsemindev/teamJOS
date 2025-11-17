package com.sp.model;

/**
 * <h2>BoardDTO (게시판 정보 DTO)</h2>
 *
 * <p>TB_BOARD 테이블의 데이터를 담는 Data Transfer Object 입니다.</p>
 *
 * <ul>
 * <li>게시글 고유 번호 및 제목/내용</li>
 * <li>작성 사원 번호 및 이름</li>
 * <li>등록 및 최종 수정 일시</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>테이블명:</b> TB_BOARD</p>
 * <p><b>작성자:</b> 이석준</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class BoardDTO {
    
    /** 게시글 번호 (BOARD_NO NUMBER(10), 시퀀스) */
    private int boardNo;         // BOARD_ NUMBER(10)
    
    /** 작성 사원 번호 (EMP_NO CHAR(5)) */
    private String empNo;           // EMP_NO CHAR(5)
    
    /** 작성 사원 이름 (EMP_NM VARCHAR2(50)) */
    private String empNm;           // EMP_NM VARCHAR2(50
    
    /** 게시글 제목 (TITLE VARCHAR2(200)) */
    private String title;           // TITLE VARCHAR2(200)
    
    /** 게시글 내용 (CONTENT VARCHAR2(600)) */
    private String content;         // CONTENT VARCHAR2(600)
    
    /** 등록 일시 (REG_DTM TIMESTAMP) */
    private String regDtm;          // REG_DTM TIMESTAMP
    
    /** 최종 수정 일시 (UPDATE_DTM TIMESTAMP) */
    private String updateDtm;       // UPDATE_DTM TIMESTAMP

    /** 기본 생성자 */
    public BoardDTO() {
    }

    /**
     * 모든 필드를 초기화하는 생성자
     *
     * @param boardNo 게시글 번호
     * @param empNo 작성 사원 번호
     * @param empNm 작성 사원 이름
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param regDtm 등록 일시
     * @param updateDtm 최종 수정 일시
     */
    public BoardDTO(int boardNo, String empNo, String empNm, String title, String content, String regDtm, String updateDtm) {
        this.boardNo = boardNo;
        this.empNo = empNo;
        this.setEmpNm(empNm);
        this.title = title;
        this.content = content;
        this.regDtm = regDtm;
        this.updateDtm = updateDtm;
    }

    /** @return 게시글 번호 */
    public int getBoardNo() {
        return boardNo;
    }

    /** @param boardNo 게시글 번호 */
    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    /** @return 작성 사원 번호 */
    public String getEmpNo() {
        return empNo;
    }

    /** @param empNo 작성 사원 번호 */
    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    /** @return 게시글 제목 */
    public String getTitle() {
        return title;
    }

    /** @param title 게시글 제목 */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @return 게시글 내용 */
    public String getContent() {
        return content;
    }

    /** @param content 게시글 내용 */
    public void setContent(String content) {
        this.content = content;
    }

    /** @return 등록 일시 */
    public String getRegDtm() {
        return regDtm;
    }

    /** @param regDtm 등록 일시 */
    public void setRegDtm(String regDtm) {
        this.regDtm = regDtm;
    }

    /** @return 최종 수정 일시 */
    public String getUpdateDtm() {
        return updateDtm;
    }

    /** @param updateDtm 최종 수정 일시 */
    public void setUpdateDtm(String updateDtm) {
        this.updateDtm = updateDtm;
    }

    /** @return 작성 사원 이름 */
	public String getEmpNm() {
		return empNm;
	}

    /** @param empNm 작성 사원 이름 */
	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}

    
}