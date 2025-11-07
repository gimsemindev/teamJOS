package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.AttDAO;
import com.sp.dao.BoardDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.EmpDAO;


public class EmployeeUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	// 분할된 개별 UI 클래스 선언 및 초기화
	public EmployeeEmpUI employeeEmpUI; // 기존 선언 활용
	private EmployeeDeptUI employeeDeptUI;
	private EmployeeAttUI employeeAttUI;
	private EmployeeBoardUI employeeBoardUI;
	
	public  EmployeeUI(EmpDAO empDao,DeptDAO deptDao,AttDAO attDao,BoardDAO boardDao) {
	
		// 주입받은 DAO를 사용하여 분할된 UI 클래스 인스턴스 초기화
		this.employeeEmpUI = new EmployeeEmpUI(empDao);
		this.employeeDeptUI = new EmployeeDeptUI(deptDao);
		this.employeeAttUI = new EmployeeAttUI(attDao);
		this.employeeBoardUI = new EmployeeBoardUI(boardDao);
	}
	
	public void manageEmployee() {
		// 기능 위임
		employeeEmpUI.menu();
	}

	
	public void manageDepartment() {
		// 기능 위임
		employeeDeptUI.menu();
	}
	
	public void manageAttendance() {
		// 기능 위임
		employeeAttUI.menu();
	}
	
	public void manageBoard() {
		// 기능 위임
		employeeBoardUI.menu();
	}
	
}