package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.AttDAO;
import com.sp.dao.AuthDAO;
import com.sp.dao.BoardDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.EmpDAO;


public class AdminUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // 분할된 개별 UI 클래스 선언
    private AdminEmpUI adminEmpUI;
    private AdminDeptUI adminDeptUI;
    private AdminAttUI adminAttUI;
    private AdminAuthUI adminAuthUI;
    private AdminBoardUI adminBoardUI;

    public AdminUI(EmpDAO empDao, DeptDAO deptDao, AttDAO attDao, AuthDAO authDao, BoardDAO boardDao) {
        // 주입받은 DAO를 사용하여 분할된 UI 클래스 인스턴스 초기화
        this.adminEmpUI = new AdminEmpUI(empDao);
        this.adminDeptUI = new AdminDeptUI(deptDao);
        this.adminAttUI = new AdminAttUI(attDao);
        this.adminAuthUI = new AdminAuthUI(authDao);
        this.adminBoardUI = new AdminBoardUI(boardDao);
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