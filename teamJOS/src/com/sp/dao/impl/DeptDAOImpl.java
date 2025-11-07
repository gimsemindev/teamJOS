package com.sp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sp.dao.DeptDAO;
import com.sp.model.DeptDTO;
import com.sp.model.DeptMemberCountDTO;
import com.sp.util.DBConn;

public class DeptDAOImpl implements DeptDAO{
	private Connection conn = DBConn.getConnection();
	
	@Override
	public int insertDept(DeptDTO dept) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateDept(DeptDTO dept) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteDept(int deptNo) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<DeptDTO> selectAllDept() {
		List<DeptDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		// member1과 member2를 id를 기준으로 LEFT OUTER JOIN 하여 전체 레코드 반환
		
		try {
			sql = "SELECT m1.id, pwd, name, birth, email, tel "
					+ " FROM member1 m1 "
					+ " LEFT OUTER JOIN member2 m2 ON m1.id = m2.id";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DeptDTO dto = new DeptDTO();

				//dto.setId(rs.getString("id"));
				//dto.setPwd(rs.getString("pwd"));
				//dto.setName(rs.getString("name"));
				//dto.setBirth(rs.getDate("birth") == null ? "" : rs.getDate("birth").toString());
				//dto.setEmail(rs.getString("email"));
				//dto.setTel(rs.getString("tel"));

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//DBUtil.close(rs);
			//DBUtil.close(pstmt);
		}

		return list;
	}

	@Override
	public List<DeptMemberCountDTO> selectDeptMemberCount() {
		// TODO Auto-generated method stub
		return null;
	}

}
