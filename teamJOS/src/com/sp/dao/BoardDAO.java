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
 *   <li>게시글 등록, 수정, 삭제</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> BOARD_INS_001 ~ BOARD_DEL_003</p>
 * 
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 홍길동</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public interface BoardDAO {

    /** BOARD_INS_001 */
    int insertPost(BoardDTO board) throws SQLException;

    /** BOARD_UPD_002 */
    int updatePost(BoardDTO board) throws SQLException;

    /** BOARD_DEL_003 */
    int deletePost_Admin(BoardDTO board) throws SQLException;
    
    int deletePost(BoardDTO board) throws SQLException;
    
    BoardDTO getPost(int boardSeq) throws SQLException;
    
    /** BOARD_SEL_006 **/
    List<BoardDTO> listPosts(int start, int end) throws SQLException;
      
    /** BOARD_SEL_007 **/
    int listPostsCount() throws SQLException; 
     

}
