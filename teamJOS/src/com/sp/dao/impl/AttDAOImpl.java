package com.sp.dao.impl;

import java.util.List;

import com.sp.dao.AttDAO;
import com.sp.model.AttendanceDTO;
import com.sp.model.VacationDTO;

public class AttDAOImpl implements AttDAO{

	@Override
	public int insertAttendanceIn(AttendanceDTO att) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertAttendanceOut(AttendanceDTO att) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertVacation(VacationDTO vacation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateVacationApprove(VacationDTO vacation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateVacation(VacationDTO vacation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateAttendance(AttendanceDTO att) {
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
