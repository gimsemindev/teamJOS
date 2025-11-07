package com.sp.view;

import com.sp.dao.AttDAO;
import com.sp.dao.AuthDAO;
import com.sp.dao.BoardDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.EmpDAO;
import com.sp.dao.impl.AttDAOImpl;
import com.sp.dao.impl.BoardDAOImpl;
import com.sp.dao.impl.DeptDAOImpl;
import com.sp.dao.impl.EmpDAOImpl;
import com.sp.util.LoginInfo;

public class EmployeeUI {
	private LoginInfo loginInfo = null;
	private EmpDAO empDao = new EmpDAOImpl();
	private DeptDAO deptDao = new DeptDAOImpl();
	private AttDAO attDao = new AttDAOImpl();
	private BoardDAO boardDao = new BoardDAOImpl();
	
	public  EmployeeUI(LoginInfo loginInfo,EmpDAO empDao,DeptDAO deptDao,AttDAO attDao,BoardDAO boardDao) {
		this.loginInfo = loginInfo;
		this.empDao = empDao;
		this.deptDao = deptDao;
		this.attDao = attDao;
		this.boardDao = boardDao;
	}
	
	public void manageEmployee() {
		
	}
	
	public void manageDepartment() {
		
	}
	
	public void manageAttendance() {
		
	}
	
	public void manageBoard() {
		
	}
	
}
