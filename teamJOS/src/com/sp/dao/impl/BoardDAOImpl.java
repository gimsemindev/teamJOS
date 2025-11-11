package com.sp.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sp.dao.BoardDAO;
import com.sp.dao.LoginDAO;
import com.sp.model.BoardDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;



public class BoardDAOImpl implements BoardDAO{
	private Connection conn = DBConn.getConnection();

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
			sql ="INSERT INTO tb_board(BOARD_SEQ, EMP_NO, TITLE, CONTENT, REG_DTM) "
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

	@Override
	public int updatePost(BoardDTO board) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "UPDATE tb_board SET TITLE = ?, CONTENT = ?, UPDATE_DTM = SYSTIMESTAMP "
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
			e.printStackTrace();
		}finally {
			DBUtil.close(pstmt);
		}
		
		return result;
	}

	@Override
	public int deletePost(int postNo) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deletePost(BoardDTO board, LoginDAO info) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BoardDTO getPost(int boardSeq) throws SQLException {
		BoardDTO dto = null; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT BOARD_SEQ, EMP_NO, TITLE, CONTENT, REG_DTM, UPDATE_DTM "
			    + " FROM tb_board WHERE BOARD_SEQ = ?";
			
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

	@Override
	public List<BoardDTO> listPosts() throws SQLException {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql ="SELECT BOARD_SEQ, EMP_NO, TITLE, REG_DTM, UPDATE_DTM "
               + " FROM tb_board ORDER BY BOARD_SEQ DESC";
			
			pstmt = conn.prepareStatement(sql);
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
