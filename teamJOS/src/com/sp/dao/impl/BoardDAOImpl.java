package com.sp.dao.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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



public class BoardDAOImpl implements BoardDAO{
	private Connection conn = DBConn.getConnection();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	BoardDTO dto;
	@Override
	public int insertPost(BoardDTO board) throws SQLException{
		
		
		
		 
		PreparedStatement pstmt = null;
		String sql;
		 dto =new BoardDTO();
		try {
			System.out.println("게시글 작성!");
			
			
			System.out.println("사번 ?");
			dto.setEmpNo(br.readLine());
			System.out.println("제목");
			dto.setTitle(br.readLine());
			System.out.println("내용");
			dto.setContent(br.readLine());
			
			
			sql ="INSERT INTO tb_board(BOARD_SEQ, EMP_NO  ,TITLE ,CONTENT , REG_DTM ) VALUES (?,?, ?, ?,SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1123);
			pstmt.setString(2, dto.getEmpNo());
			pstmt.setString(3, dto.getTitle());
			pstmt.setString(4, dto.getContent());
			System.out.println("작성 완료!");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(pstmt);
		}

		
	return 0;}

	@Override
	public int updatePost(BoardDTO board) throws SQLException{
		int result = 0;
        String sql;
        
        sql = "UPDATE tb_board SET TITLE = ?, CONTENT = ?, UPDATE_DTM = SYSTIMESTAMP "
            + " WHERE BOARD_SEQ = ? AND EMP_NO = ?"; 
        
        try (Connection conn = DBConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContent());
            pstmt.setInt(3, 1123); 
            pstmt.setString(4, board.getEmpNo());   

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
	}

	@Override
	public int deletePost(int postNo) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deletePost(int boardSeq, String empNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BoardDTO getPost(int boardSeq) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BoardDTO> listPosts() throws SQLException {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		BoardDTO dto = new BoardDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql ="SELECT * FROM tb_board";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				 

				dto.setBoardNo(rs.getInt("BOARD_SEQ"));
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setTitle(rs.getString("TITLE"));
				dto.setContent(rs.getString("CONTENT"));
				dto.setRegDtm(rs.getString("REG_DTM"));
				dto.setUpdateDtm(rs.getString("UPDATE_DTM"));

				list.add(dto);
				
				System.out.println("-----------------------------------------");
				System.out.println("제목: "+dto.getTitle()+"-------------------");
				System.out.println("-------글쓴이:"+dto.getEmpNo()+"--글번호: "+dto.getBoardNo());
				System.out.println("작성시간: "+dto.getRegDtm());
				System.out.println("글 내용 :"+dto.getContent());
			}


		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBConn.close();
			
		}

		return list;
	}

	@Override
	public void updateBoard() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("\n--- [ 2. 게시글 수정 ] ---");
        

        try {
            System.out.print("수정할 글번호 ? ");
            String input = br.readLine();
             dto.setBoardNo(Integer.parseInt(input));

            
            if (dto == null) {
                System.out.println("! 존재하지 않는 게시글입니다.");
                return;
            }
            
            // 본인 글인지 확인
            

            System.out.println(" [ " + dto.getTitle() + " ] 글을 수정합니다.");
            System.out.print("새 제목 ? ");
            String newTitle = br.readLine();
            if (newTitle == null || newTitle.trim().isEmpty()) {
                System.out.println("! 제목은 필수 입력 사항입니다.");
                return;
            }
            
            System.out.print("새 내용 ? ");
            String newContent = br.readLine();

            dto.setTitle(newTitle);
            dto.setContent(newContent);
            

            int result = updatePost(dto);
            if(result > 0) {
                System.out.println("✓ 글 수정 성공 !!!");
            } else {
                System.out.println("! 글 수정 실패.");
            }

        } catch (NumberFormatException e) {
            System.out.println("! 글번호는 숫자로 입력해야 합니다.");
        } catch (SQLException e) {
            System.out.println("! 데이터베이스 오류: " + e.getMessage());
        }	
	}
		

}
