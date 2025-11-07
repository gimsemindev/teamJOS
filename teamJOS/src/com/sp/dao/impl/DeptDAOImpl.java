package com.sp.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.sp.dao.DeptDAO;
import com.sp.model.DeptDTO;
import com.sp.model.DeptMemberCountDTO;

public class DeptDAOImpl implements DeptDAO{

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
	public DeptDTO selectDeptByNo(int deptNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeptMemberCountDTO> selectDeptMemberCount() {
		// TODO Auto-generated method stub
		return null;
	}

}
