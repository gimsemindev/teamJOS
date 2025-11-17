package com.sp.dao;

import java.sql.SQLException;
import java.util.List;

import com.sp.model.BoardDTO;

/**
 * <h2>BoardDAO (게시판 관리 데이터 접근 인터페이스)</h2>
 *
 * <p>게시판(Board) 관련 데이터베이스 접근 기능을 정의하는 인터페이스입니다.</p>
 *
 * <ul>
 * <li>게시글 등록, 수정, 삭제 기능 제공</li>
 * <li>게시글 단건 조회 및 목록 페이징 조회</li>
 * <li>전체 게시글 수 조회</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> BOARD_INS_001 ~ BOARD_SEL_007</p>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 이석준</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public interface BoardDAO {

    /** * BOARD_INS_001 : 신규 게시글을 등록합니다.
     * @param board 등록할 게시글 정보 DTO
     * @return 등록된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int insertPost(BoardDTO board) throws SQLException;

    /** * BOARD_UPD_002 : 게시글의 내용을 수정합니다.
     * @param board 수정할 게시글 정보 DTO (boardSeq 및 수정 내용 포함)
     * @return 수정된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int updatePost(BoardDTO board) throws SQLException;

    /** * BOARD_DEL_003 : 관리자 권한으로 게시글을 삭제합니다.
     * <p>관리자 기능이므로 게시글 작성자 권한 확인을 생략합니다.</p>
     * @param board 삭제할 게시글 정보 DTO (boardSeq 포함)
     * @return 삭제된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int deletePost_Admin(BoardDTO board) throws SQLException;
    
    /** * BOARD_DEL_004 : 일반 사용자 권한으로 게시글을 삭제합니다.
     * <p>삭제 전, 게시글 작성자 본인 여부를 반드시 확인해야 합니다.</p>
     * @param board 삭제할 게시글 정보 DTO (boardSeq 포함)
     * @return 삭제된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int deletePost(BoardDTO board) throws SQLException;
    
    /** * BOARD_SEL_005 : 게시글 일련번호를 기준으로 단일 게시글 정보를 조회합니다.
     * @param boardSeq 조회할 게시글 일련번호
     * @return 조회된 게시글 정보 DTO
     * @throws SQLException DB 접근 오류 발생 시
     */
    BoardDTO getPost(int boardSeq) throws SQLException;
    
    /** * BOARD_SEL_006 : 전체 게시글 목록을 페이징하여 조회합니다.
     * @param start 조회 시작 row 번호
     * @param end   조회 종료 row 번호
     * @return 페이징된 게시글 목록 리스트
     * @throws SQLException DB 접근 오류 발생 시
     */
    List<BoardDTO> listPosts(int start, int end) throws SQLException;
      
    /** * BOARD_SEL_007 : 전체 게시글의 총 개수를 조회합니다.
     * @return 전체 게시글의 수
     * @throws SQLException DB 접근 오류 발생 시
     */
    int listPostsCount() throws SQLException; 
     

}