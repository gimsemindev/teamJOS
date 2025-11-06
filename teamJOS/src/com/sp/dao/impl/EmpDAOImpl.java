package com.sp.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.sp.dao.EmpDAO;
import com.sp.model.CareerDTO;
import com.sp.model.DeptMoveDTO;
import com.sp.model.EmployeeDTO;
import com.sp.model.HistoryDTO;
import com.sp.model.PromotionDTO;
import com.sp.model.RewardDTO;

public class EmpDAOImpl implements EmpDAO{

	@Override
	public int insertEmployee(EmployeeDTO emp) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateEmployee(EmployeeDTO emp) throws SQLException{
		// TODO Auto-generated method stub
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
