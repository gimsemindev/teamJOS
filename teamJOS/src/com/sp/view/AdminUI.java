package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.AttDAO;
import com.sp.dao.AuthDAO;
import com.sp.dao.BoardDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.EmpDAO;
import com.sp.util.LoginInfo;


public class AdminUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // 분할된 개별 UI 클래스 선언
    private AdminEmpUI adminEmpUI;
    private AdminDeptUI adminDeptUI;
    private AdminAttUI adminAttUI;
    private AdminAuthUI adminAuthUI;
    private AdminBoardUI adminBoardUI;
    
    private LoginInfo loginInfo;
    
    public AdminUI(EmpDAO empDao, DeptDAO deptDao, AttDAO attDao, AuthDAO authDao, BoardDAO boardDao, LoginInfo logininfo) {
        // 주입받은 DAO를 사용하여 분할된 UI 클래스 인스턴스 초기화
    	this.loginInfo = loginInfo;
        this.adminEmpUI = new AdminEmpUI(empDao, loginInfo);
        this.adminDeptUI = new AdminDeptUI(deptDao, loginInfo);
        this.adminAttUI = new AdminAttUI(attDao, loginInfo);
        this.adminAuthUI = new AdminAuthUI(authDao, loginInfo);
        this.adminBoardUI = new AdminBoardUI(boardDao, loginInfo);
    }
    
    // 각 기능을 분할된 UI 클래스의 menu() 메서드로 위임
    
    public void manageEmployee() {
        adminEmpUI.menu();
    }

    public void manageDepartment() {
        adminDeptUI.menu();
    }
    
    public void manageAttendance() {
        adminAttUI.menu();
    }
    
    public void manageAuth() {
        adminAuthUI.menu();
    }

    public void manageBoard() {
        adminBoardUI.menu();
    }
}