package com.sp.view;

import com.sp.dao.AttDAO;
import com.sp.dao.AuthDAO;
import com.sp.dao.BoardDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.EmpDAO;
import com.sp.dao.impl.AttDAOImpl;
import com.sp.dao.impl.AuthDAOImpl;
import com.sp.dao.impl.BoardDAOImpl;
import com.sp.dao.impl.DeptDAOImpl;
import com.sp.dao.impl.EmpDAOImpl;
import com.sp.util.LoginInfo;

public class AdminUI {
	private LoginInfo loginInfo = null;
	private EmpDAO empDao = new EmpDAOImpl();
	private DeptDAO deptDao = new DeptDAOImpl();
	private AttDAO attDao = new AttDAOImpl();
	private AuthDAO authDao = new AuthDAOImpl();
	private BoardDAO boardDao = new BoardDAOImpl();
	
	public  AdminUI(LoginInfo loginInfo,EmpDAO empDao,DeptDAO deptDao,AttDAO attDao,AuthDAO authDao,BoardDAO boardDao) {
		this.loginInfo = loginInfo;
		this.empDao = empDao;
		this.deptDao = deptDao;
		this.attDao = attDao;
		this.authDao = authDao;
		this.boardDao = boardDao;
	}
	
    public void manageEmployee() {
		
	}
	
	public void manageDepartment() {
		
	}
	
	public void manageAttendance() {
		
	}
	
	public void manageAuth() {
		
	}

	public void manageBoard() {
		
	}
}
