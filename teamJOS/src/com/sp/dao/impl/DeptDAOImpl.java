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
import com.sp.util.*;


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
			sql = " SELECT /* DEPT_SEL_003 */ "
					+ "      DEPT_CD "
					+ "    , CASE WHEN CONNECT_BY_ISLEAF = 1 THEN "
					+ "            LPAD(' ', (LEVEL-1)*4, ' ') || '└─ ' || DEPT_NM "
					+ "            ELSE LPAD(' ', (LEVEL-1)*4, ' ') || '├─ ' || DEPT_NM "
					+ "       END AS DEPT_NM "
					+ "    , EXT_NO "
					+ " FROM TB_DEPT "
					+ " START WITH SUPER_DEPT_CD IS NULL "
					+ "CONNECT BY PRIOR DEPT_CD = SUPER_DEPT_CD "
					+ "ORDER SIBLINGS BY DEPT_CD ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DeptDTO dto = new DeptDTO();

				dto.setDeptCd(rs.getString("DEPT_CD"));
				dto.setDeptNm(rs.getString("DEPT_NM"));
				dto.setExtNo(rs.getString("EXT_NO"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	@Override
	public List<DeptMemberCountDTO> selectDeptMemberCount() {
		// TODO Auto-generated method stub
		return null;
	}

}
