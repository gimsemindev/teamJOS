package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.AttDAO;
import com.sp.dao.BoardDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.EmpDAO;
import com.sp.util.LoginInfo;

/**
 * <h2>EmployeeUI (일반 사원 - 메인 UI)</h2>
 *
 * <p>일반 사원이 접근할 수 있는 기능을 메뉴 단위로 제공하는 UI 클래스입니다.
 * 개별 기능(UI)은 별도의 Employee*UI 클래스로 분리되어 있으며,
 * 이 클래스는 각 메뉴 선택 시 해당 UI 클래스의 기능을 호출합니다.</p>
 *
 * <ul>
 * <li>사원 관리 (EmployeeEmpUI)</li>
 * <li>부서 관리 (EmployeeDeptUI)</li>
 * <li>출결 관리 (EmployeeAttUI)</li>
 * <li>게시판 관리 (EmployeeBoardUI)</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 황선호</p>
 * <p><b>작성일:</b> 2025-11-18</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class EmployeeUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	// 분할된 개별 UI 클래스 선언 및 초기화
	public EmployeeEmpUI employeeEmpUI; 
	private EmployeeDeptUI employeeDeptUI;
	private EmployeeAttUI employeeAttUI;
	private EmployeeBoardUI employeeBoardUI;
	
	private LoginInfo loginInfo; 
	
	public EmployeeUI(EmpDAO empDao, DeptDAO deptDao, AttDAO attDao, BoardDAO boardDao, LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
		this.employeeEmpUI = new EmployeeEmpUI(empDao, this.loginInfo);
		this.employeeDeptUI = new EmployeeDeptUI(deptDao, this.loginInfo);
		this.employeeAttUI = new EmployeeAttUI(attDao, this.loginInfo);
		this.employeeBoardUI = new EmployeeBoardUI(boardDao, this.loginInfo);
	}
	
	public void manageEmployee() {
		employeeEmpUI.menu();
	}

	public void manageDepartment() {
		employeeDeptUI.menu();
	}
	
	public void manageAttendance() {
		employeeAttUI.menu();
	}
	
	public void manageBoard() {
		employeeBoardUI.menu();
	}
	
}
