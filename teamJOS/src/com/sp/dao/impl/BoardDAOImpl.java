package com.sp.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sp.dao.BoardDAO;
import com.sp.model.BoardDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;

/**
 * <h2>BoardDAOImpl (게시판 데이터 접근 구현체)</h2>
 *
 * <p>BoardDAO 인터페이스를 구현한 클래스로, 공지사항/게시판의 CRUD(생성, 조회, 수정, 삭제) 기능을
 * 실제 데이터베이스와 연동하여 처리합니다.</p>
 *
 * <ul>
 * <li>게시글 목록 및 상세 조회 (페이징 포함)</li>
 * <li>게시글 등록, 수정, 삭제 기능 제공 (작성자/관리자 권한 분리)</li>
 * <li>전체 게시글 수 조회</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> BOARD_INS_001 ~ BOARD_SEL_007</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 이석준</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class BoardDAOImpl implements BoardDAO{
	private Connection conn = DBConn.getConnection();

	/**
	 * BOARD_SEL_007 : 전체 게시글의 개수를 조회합니다. (페이징 처리를 위해 사용)
	 *
	 * @return 전체 게시글 수
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int listPostsCount() throws SQLException {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        
        try {
        	sql = "SELECT /* BOARD_SEL_007 */ COUNT(*) AS CNT FROM TB_BOARD ";
        	        	
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt("CNT");
            }        	
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
		
        return result;
	}
	
	
	/**
	 * BOARD_INS_001 : 신규 게시글을 등록합니다.
	 *
	 * @param board 등록할 게시글 정보 (작성자 사번, 제목, 내용 포함)
	 * @return 등록된 레코드 수 (1)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int insertPost(BoardDTO board) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
            
            if (board == null) {
                System.out.println("! DAO 오류: 전달된 board 객체가 null입니다.");
                return 0;
            }

            // 2. SQL 쿼리 준비
			sql ="INSERT INTO /* BOARD_INS_001 */ tb_board(BOARD_SEQ, EMP_NO, TITLE, CONTENT, REG_DTM) "
               + " VALUES(SQ_TB_BOARD_SEQ.NEXTVAL, ?, ?, ?, SYSTIMESTAMP)";
            
			pstmt = conn.prepareStatement(sql);
            
            
            
			pstmt.setString(1, board.getEmpNo());   
			pstmt.setString(2, board.getTitle());   
			pstmt.setString(3, board.getContent()); 
			
       
           
			result = pstmt.executeUpdate(); 
			
         
			
		} catch (SQLException e) {
            System.out.println("! DB 오류 (insertPost): " + e.getMessage());
            e.printStackTrace(); 
            throw e; 
		} catch (Exception e) {
            e.printStackTrace();
		} finally {
			DBUtil.close(pstmt); 
            
		}
		
		return result; // 0 (실패) 또는 1 (성공) 반환
	}

	/**
	 * BOARD_UPD_002 : 게시글의 제목과 내용을 수정합니다. (작성자 본인만 가능)
	 *
	 * @param board 수정할 게시글 정보 (글번호, 작성자 사번, 새 제목, 새 내용 포함)
	 * @return 수정된 레코드 수 (1: 성공, 0: 실패/권한 없음)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int updatePost(BoardDTO board) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "UPDATE /* BOARD_UPD_002 */ tb_board SET TITLE = ?, CONTENT = ?, UPDATE_DTM = SYSTIMESTAMP "
		            + " WHERE BOARD_SEQ = ? AND EMP_NO = ?"; 
			pstmt = conn.prepareStatement(sql);
			
			 if (board == null) {
	                System.out.println("! DAO 오류: 전달된 board 객체가 null입니다.");
	                return 0;
	            }
			pstmt.setString(1, board.getTitle());   // 새 제목
	        pstmt.setString(2, board.getContent()); // 새 내용
	        pstmt.setInt(3, board.getBoardNo());    // 수정할 글번호
	        pstmt.setString(4, board.getEmpNo());   // 작성자 사번 (수정 권한 확인용)
	        result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		}finally {
			DBUtil.close(pstmt);
		}
		
		return result;
	}

	/**
	 * BOARD_DEL_003 : 게시글을 관리자 권한으로 삭제합니다. (사번 일치 조건 없음)
	 *
	 * @param board 삭제할 게시글 정보 (글번호 포함)
	 * @return 삭제된 레코드 수 (1)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int deletePost_Admin(BoardDTO board) throws SQLException{
		
				int result = 0;
		        String sql;
		        
		        
		        sql = "DELETE /* BOARD_DEL_003 */ FROM tb_board WHERE BOARD_SEQ = ? ";
		        
		        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		            
		          
		            pstmt.setInt(1, board.getBoardNo());
		            
		            
		            // SQL 실행
		            result = pstmt.executeUpdate();

		        } catch (SQLException e) {
		            System.out.println("! DB 오류 (deletePost): " + e.getMessage());
		            e.printStackTrace();
		            
		        }
		return result;
	}

	/**
	 * BOARD_DEL_004 : 게시글을 작성자 본인의 권한으로 삭제합니다. (글번호 및 사번 일치 조건)
	 *
	 * @param board 삭제할 게시글 정보 (글번호, 작성자 사번 포함)
	 * @return 삭제된 레코드 수 (1: 성공, 0: 실패/권한 없음)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int deletePost(BoardDTO board) throws SQLException {
		// TODO Auto-generated method stub
		int result = 0;
        String sql;
        
        // 본인 글(BOARD_SEQ와 EMP_NO가 일치)만 삭제하도록 SQL 작성
        sql = "DELETE /* BOARD_DEL_004 */ FROM tb_board WHERE BOARD_SEQ = ? AND EMP_NO = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
          
            pstmt.setInt(1, board.getBoardNo());
            pstmt.setString(2,board.getEmpNo());
            
            // SQL 실행
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("! DB 오류 (deletePost): " + e.getMessage());
            e.printStackTrace();
            
        }
        
        return result; 
	}
	

	/**
	 * BOARD_SEL_006 : 특정 게시글 번호에 해당하는 게시글의 상세 정보를 조회합니다.
	 *
	 * @param boardSeq 조회할 게시글 번호
	 * @return 조회된 게시글 정보 DTO (없으면 null)
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public BoardDTO getPost(int boardSeq) throws SQLException {
		BoardDTO dto = null; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = """
				  SELECT /*BOARD_SEL_006 */ B.BOARD_SEQ
				       , E.EMP_NM || '[' || B.EMP_NO || ']' AS EMP_NO
				       , B.TITLE
				       , B.CONTENT
				       , TO_CHAR(B.REG_DTM, 'YYYY-MM-DD HH24:MI:SS') AS REG_DTM
				       , TO_CHAR(B.UPDATE_DTM, 'YYYY-MM-DD HH24:MI:SS') AS UPDATE_DTM 
			        FROM TB_BOARD B
                    LEFT JOIN TB_EMP E
                      ON B.EMP_NO = E.EMP_NO
			       WHERE B.BOARD_SEQ = ? """;
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardSeq);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BoardDTO(); 
				dto.setBoardNo(rs.getInt("BOARD_SEQ"));
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setTitle(rs.getString("TITLE"));
				dto.setContent(rs.getString("CONTENT")); // 내용 포함
				dto.setRegDtm(rs.getString("REG_DTM"));
				dto.setUpdateDtm(rs.getString("UPDATE_DTM"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
            throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto; // 찾으면 DTO, 못 찾으면 null 반환
		
	}

	/**
	 * BOARD_SEL_005 : 전체 게시글 목록을 최신순으로 페이징 처리하여 조회합니다.
	 *
	 * @param start 시작 행 번호
	 * @param end 끝 행 번호
	 * @return 게시글 정보 DTO 리스트
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public List<BoardDTO> listPosts(int start, int end) throws SQLException {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql= """
					SELECT * /* BOARD_SEL_005 */
			          FROM (
			                SELECT ROWNUM rn, A.*
			                  FROM (
			                        SELECT B.BOARD_SEQ
                                         , E.EMP_NM || '[' || B.EMP_NO || ']' AS EMP_NO
                                         , B.TITLE
                                         , B.REG_DTM
                                         , B.UPDATE_DTM
			                          FROM TB_BOARD B
                                      LEFT JOIN TB_EMP E
                                        ON B.EMP_NO = E.EMP_NO
			                         ORDER BY BOARD_SEQ DESC
			                       ) A
			                 WHERE ROWNUM <= ?
			               )
			         WHERE rn >= ? """;
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, end);   //
			pstmt.setInt(2, start); //
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardDTO dto = new BoardDTO(); 

				dto.setBoardNo(rs.getInt("BOARD_SEQ"));
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setTitle(rs.getString("TITLE"));
				dto.setRegDtm(rs.getString("REG_DTM"));
				dto.setUpdateDtm(rs.getString("UPDATE_DTM"));

				list.add(dto);
				
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
            throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

}