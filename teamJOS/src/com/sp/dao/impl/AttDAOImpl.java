package com.sp.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.sp.dao.AttDAO;
import com.sp.model.AttendanceDTO;
import com.sp.model.VacationDTO;

public class AttDAOImpl implements AttDAO{

	@Override
	public int insertAttendanceIn(AttendanceDTO att) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertAttendanceOut(AttendanceDTO att) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertVacation(VacationDTO vacation) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateVacationApprove(VacationDTO vacation) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateVacation(VacationDTO vacation) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateAttendance(AttendanceDTO att) throws SQLException{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<AttendanceDTO> selectAllWorkTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AttendanceDTO> selectWorkTimeByEmp(int empNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VacationDTO> selectAllVacation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VacationDTO> selectVacationByEmp(int empNo) {
		// TODO Auto-generated method stub
		return null;
	}

}
