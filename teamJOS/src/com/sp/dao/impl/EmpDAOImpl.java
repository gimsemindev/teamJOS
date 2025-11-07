package com.sp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.sp.dao.EmpDAO;
import com.sp.model.CareerDTO;
import com.sp.model.DeptMoveDTO;
import com.sp.model.EmployeeDTO;
import com.sp.model.HistoryDTO;
import com.sp.model.PromotionDTO;
import com.sp.model.RewardDTO;
import com.sp.util.DBConn;

public class EmpDAOImpl implements EmpDAO{
	private Connection conn = DBConn.getConnection();

	@Override
	public int insertEmployee(EmployeeDTO emp) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateEmployee(String empNo, String col, String con) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE TB_EMP SET " + col + " = ? WHERE EMP_NO = ?";
		
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, con);
			pstmt.setString(2, empNo);
		
			pstmt.executeUpdate();
			
			System.out.println("사원 정보 수정이 완료되었습니다.");
			
		} catch (SQLException e) {
			conn.rollback();
			throw e;
		} finally {
			pstmt.close();
			conn.close();
		}
		
		return 0;
	}


	@Override
	public int updateDeptMove(DeptMoveDTO move) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updatePromotion(PromotionDTO promotion) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateRetireApproval(int empNo) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertCareer(CareerDTO career) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertLicense(RewardDTO reward) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EmployeeDTO selectByEmpNo(int empNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmployeeDTO> selectByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmployeeDTO> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HistoryDTO> selectHistory(int empNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeptMoveDTO> selectDeptMove(int empNo) {
		// TODO Auto-generated method stub
		return null;
	}

}
