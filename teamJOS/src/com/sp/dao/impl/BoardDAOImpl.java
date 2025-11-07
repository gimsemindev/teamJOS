package com.sp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sp.dao.BoardDAO;
import com.sp.model.BoardDTO;
import com.sp.util.DBConn;

public class BoardDAOImpl implements BoardDAO{

	@Override
	public int insertPost(BoardDTO board) throws SQLException{
		/*
		Connection conn = DBConn.getConnection();
		BoardDTO 
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql ="INSERT INTO tb_board(BOARD_SEQ, EMP_NO  ,TITLE ,CONTENT , REG_DTM ) VALUES (?,?, ?, ?,SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.get());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getPwd());

		} catch (Exception e) {
			// TODO: handle exception
		}

		
	*/return 0;}

	@Override
	public int updatePost(BoardDTO board) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deletePost(int postNo) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

}
