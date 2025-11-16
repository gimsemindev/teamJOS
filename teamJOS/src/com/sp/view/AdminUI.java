package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.AttDAO;
import com.sp.dao.AuthDAO;
import com.sp.dao.BoardDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.EmpDAO;
import com.sp.util.LoginInfo;

/**
 * <h2>AdminUI (관리자 메인 UI)</h2>
 *
 * <p>관리자 계정으로 접근 가능한 모든 기능(사원 관리, 부서 관리, 근태, 권한, 게시판)을  
 * 개별 UI 클래스로 분할하여 관리하는 최상위 UI 컨트롤러입니다.</p>
 *
 * <ul>
 *   <li>사원관리 메뉴(AdminEmpUI)</li>
 *   <li>부서관리 메뉴(AdminDeptUI)</li>
 *   <li>근태관리 메뉴(AdminAttUI)</li>
 *   <li>권한관리 메뉴(AdminAuthUI)</li>
 *   <li>게시판관리 메뉴(AdminBoardUI)</li>
 * </ul>
 *
 * <p>각 기능은 의존성 주입(DI)을 통해 DAO 객체를 전달받아 초기화됩니다.</p>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 시스템</p>
  * <p><b>작성자:</b> 황선호</p>
 * <p><b>작성일:</b> 2025-11-16</p>
 * <p><b>버전:</b> 0.9</p> 
 */
public class AdminUI {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // 분할된 개별 UI 클래스 선언
    private AdminEmpUI adminEmpUI;
    private AdminDeptUI adminDeptUI;
    private AdminAttUI adminAttUI;
    private AdminAuthUI adminAuthUI;
    private AdminBoardUI adminBoardUI;
    
    private LoginInfo loginInfo;
    
    /**
     * AdminUI 생성자
     *
     * <p>각 UI 클래스(AdminEmpUI, AdminDeptUI, …)는  
     * 각각의 DAO + LoginInfo 로 초기화됩니다.</p>
     *
     * @param empDao   사원 DAO
     * @param deptDao  부서 DAO
     * @param attDao   근태 DAO
     * @param authDao  권한 DAO
     * @param boardDao 게시판 DAO
     * @param loginInfo 로그인 사용자 정보
     */
    public AdminUI(EmpDAO empDao, DeptDAO deptDao, AttDAO attDao, AuthDAO authDao, BoardDAO boardDao, LoginInfo loginInfo) {
    	this.loginInfo = loginInfo;
        this.adminEmpUI = new AdminEmpUI(empDao, this.loginInfo);
        this.adminDeptUI = new AdminDeptUI(deptDao, this.loginInfo);
        this.adminAttUI = new AdminAttUI(attDao, this.loginInfo);
        this.adminAuthUI = new AdminAuthUI(authDao, this.loginInfo);
        this.adminBoardUI = new AdminBoardUI(boardDao, this.loginInfo);
    }
    
    // 각 기능을 분할된 UI 클래스의 menu() 메서드로 위임

    /**
     * 사원관리 메뉴 실행  
     * (AdminEmpUI.menu() 호출)
     */
    public void manageEmployee() {
        adminEmpUI.menu();
    }

    /**
     * 부서관리 메뉴 실행  
     * (AdminDeptUI.menu() 호출)
     */
    public void manageDepartment() {
        adminDeptUI.menu();
    }
    
    /**
     * 근태관리 메뉴 실행  
     * (AdminAttUI.menu() 호출)
     */
    public void manageAttendance() {
        adminAttUI.menu();
    }
    
    /**
     * 권한관리 메뉴 실행  
     * (AdminAuthUI.menu() 호출)
     */
    public void manageAuth() {
    	adminAuthUI.menu();
    }

    /**
     * 게시판관리 메뉴 실행  
     * (AdminBoardUI.menu() 호출)
     */
    public void manageBoard() {
        adminBoardUI.menu();
    }
}
