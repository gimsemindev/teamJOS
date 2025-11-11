package com.sp.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sp.dao.AttDAO;
import com.sp.model.AttendanceDTO;
import com.sp.model.VacationDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;

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
	public int updateVacationApprove(int vacationSeq) throws SQLException{
		Connection conn = DBConn.getConnection();
		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		
		try {
			sql = """
					CALL SP_APPROVE_VACATION_SUSI(?)
					""";
			cstmt = conn.prepareCall(sql);
			
			cstmt.setInt(1, vacationSeq);
			
			cstmt.execute();
			
			result = 1;
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(cstmt);
		}
		
		return result;
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

	@Override
	public List<VacationDTO> listVaction() {
		List<VacationDTO> list = new ArrayList<VacationDTO>();
		Connection conn = DBConn.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = """
					SELECT VACATION_SEQ, V.EMP_NO,TO_CHAR(START_DT, 'YYYY-MM-DD')AS START_DT,TO_CHAR(END_DT, 'YYYY-MM-DD') AS END_DT, VACATION_MEMO, APPROVER_YN
					FROM TB_VACATION V
					LEFT JOIN TB_EMP E ON V.EMP_NO = E.EMP_NO 
					WHERE APPROVER_YN = 'N'
					ORDER BY VACATION_SEQ DESC

					""";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				VacationDTO dto = new VacationDTO();
				dto.setVacationSeq(rs.getInt("VACATION_SEQ"));
				dto.setEmpNo(rs.getString("EMP_NO"));
				dto.setStartDt(rs.getString("START_DT"));
				dto.setEndDt(rs.getString("END_DT"));
				dto.setVacationMemo(rs.getString("VACATION_MEMO"));
				dto.setApproverYn(rs.getString("APPROVER_YN"));
				
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

}
