package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sp.dao.AttDAO;
import com.sp.dao.AuthDAO;
import com.sp.dao.BoardDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.EmpDAO;
import com.sp.dao.LoginDAO;
import com.sp.dao.impl.AttDAOImpl;
import com.sp.dao.impl.AuthDAOImpl;
import com.sp.dao.impl.BoardDAOImpl;
import com.sp.dao.impl.DeptDAOImpl;
import com.sp.dao.impl.EmpDAOImpl;
import com.sp.dao.impl.LoginDAOImpl;
import com.sp.model.LoginDTO;
import com.sp.util.DBConn;
import com.sp.util.LoginInfo;


public class MainUI {
    final String RESET  = "\u001B[0m";
    final String CYAN   = "\u001B[36m";
    final String GREEN  = "\u001B[32m";
    final String YELLOW = "\u001B[33m";
    final String GRAY   = "\u001B[90m";
	
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private LoginInfo login = new LoginInfo();


    // DAO 초기화
    private EmpDAO empDao = new EmpDAOImpl();
    private DeptDAO deptDao = new DeptDAOImpl();
    private AttDAO attDao = new AttDAOImpl();
    private AuthDAO authDao = new AuthDAOImpl();
    private BoardDAO boardDao = new BoardDAOImpl();
    private LoginDAO loginDao = new LoginDAOImpl();

    // UI 초기화
    public AdminUI adminUI = null;
    public EmployeeUI employeeUI = null;
    
    
    // MainUI.java 내부 권한 레벨 상수 
    private static final int AUTH_LEVEL_ADMIN = 3; // 관리자 레벨
    private static final int AUTH_LEVEL_EMPLOYEE = 1; // 일반 사원 레벨
    
    /**
     * 프로그램 시작점
     */
    public void menu() {
        while (true) {
            LoginDTO member = login.loginMember();
            
            if (member == null) {
                menuGuest(); 
                continue; 
            }
            adminUI= new AdminUI(empDao, deptDao, attDao, authDao,boardDao, login);
            employeeUI = new EmployeeUI(empDao, deptDao, attDao, boardDao, login);
            int authLevel; 
            try {

                authLevel = Integer.parseInt(member.getRole());
            } catch (NumberFormatException e) {
                System.out.println(GRAY + "경고: 알 수 없는 권한 정보입니다. 로그아웃 처리합니다." + RESET);
                login.logout();
                continue;
            }

            if (authLevel == AUTH_LEVEL_ADMIN) {  // 관리자 (레벨 3)
                menuAdmin();
            } else if (authLevel == AUTH_LEVEL_EMPLOYEE) { // 일반 사원 (레벨 1)
                menuEmployee();
            } else { // 정의되지 않은 권한
                System.out.println(GRAY + "정의되지 않은 권한 레벨(" + member.getRole() + ")입니다. 로그아웃 처리합니다." + RESET);
                login.logout();
            }
        }
    }

    /**
     * 게스트(비로그인) 메뉴
     */
    private void menuGuest() {
        int ch = 0;
        String input;
        do {
            try {
                System.out.println();
                System.out.println(CYAN + "╔════════════════════════════════════════╗" + RESET);
                System.out.println(CYAN + "║                                        ║" + RESET);
                System.out.println(CYAN + "║   🏢  " + YELLOW + " teamJOS 인사관리 시스템" + CYAN + "             ║" + RESET);
                System.out.println(CYAN + "║                                        ║" + RESET);
                System.out.println(CYAN + "╚════════════════════════════════════════╝" + RESET);

                System.out.println(GRAY + "──────────────────────────────────────────" + RESET);
                System.out.println(YELLOW + "   ① 로그인" + RESET);
                System.out.println(YELLOW + "   ② 종료" + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────" + RESET);
                System.out.print(GREEN + "👉 메뉴 선택 : " + RESET);
                
                input = br.readLine();
                
                if(input == null || input.trim().isEmpty()) {
                	ch = 0;
                	continue;
                }
                ch = Integer.parseInt(input);

            } catch (Exception e) {
                ch = 0;
            }
        } while (ch < 1 || ch > 2);

        switch (ch) {
            case 1:
                loginProcess();  // ✅ 로그인 기능을 MainUI 내부에서 수행
                break;
            case 2:
                DBConn.close();
                System.out.println();
                System.out.println(GRAY + "시스템 자원을 정리 중입니다..." + RESET);
                System.out.println(GREEN + "✅ 프로그램을 종료합니다." + RESET);
                System.exit(0);
        }
    }

    /**
     * 로그인 절차 (MainUI 내부)
     */
    private void loginProcess() {
    	try {
			System.out.print("사번(아이디): ");
			String empNo = br.readLine();
			System.out.print("비밀번호: ");
			String pw = br.readLine();

			
			LoginDTO member = loginDao.login(empNo, pw);

			if (member != null) {
				login.login(member);
				System.out.println(GREEN + "✅ 로그인 성공! (" + member.getMemberName() + " " + member.getGradeNm() + ")" + RESET + "\n");
			} else {
				System.out.println(YELLOW + "❌ 로그인 실패: 사번 또는 비밀번호를 확인하세요." + RESET + "\n");
			}

		} catch (IOException e) {
			System.err.println("입력 오류: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("로그인 중 알 수 없는 오류 발생: " + e.getMessage());
		}
	}
    /**
     * 일반 사원 메뉴
     */
    private void menuEmployee() {
        int ch = 0;
        String input;
        
        while(true) {
        	
        	try {
        		LoginDTO member = login.loginMember();

        		String gradeDisplay = member.getGradeNm() != null ? member.getGradeNm() : "직급미정";
                System.out.println("\n[" + member.getMemberName() + " " + gradeDisplay + "] 님");
        		
        		do {
        			System.out.print("1.사원관리 2.부서관리 3.근태관리 4.게시판 5.로그아웃 => ");
        			
        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    
                    ch = Integer.parseInt(input);
                    
        		} while (ch < 1 || ch > 5);
        		
        		switch (ch) {
        		case 1:
        			employeeUI.manageEmployee();
        			break;
        		case 2:
        			employeeUI.manageDepartment();
        			break;
        		case 3:
        			employeeUI.manageAttendance();
        			break; 
        		case 4:
        			employeeUI.manageBoard();
        			break;
        		case 5:	
        			login.logout();
        			System.out.println("로그아웃 되었습니다.\n");
        			return;
        		}
        	} catch (IOException e) {
        		System.err.println("입력 오류: " + e.getMessage());
        	}
        }
        
    }

    /**
     * 관리자 메뉴
     */
    private void menuAdmin() {
        int ch = 0;
        String input;
        
        while(true) {
        	
        	try {
        		System.out.println("\n[관리자 모드]");
        		
        		do {
        			System.out.print("1.사원관리 2.부서관리 3.근태관리 4.권한관리 5.게시판관리 6.로그아웃 => ");

        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while (ch < 1 || ch > 6);
        		
        		switch (ch) {
        		case 1:
        			adminUI.manageEmployee();
        			break;
        		case 2:
        			adminUI.manageDepartment();
        			break;
        		case 3:
        			adminUI.manageAttendance();
        			break;
        		case 4:
        			adminUI.manageAuth();
        			break;    
        		case 5:
        			adminUI.manageBoard();
        			break;
        		case 6:
        			login.logout();
        			System.out.println("로그아웃 되었습니다.\n");
        			return;
        		}
        	} catch (IOException e) {
        		System.err.println("입력 오류: " + e.getMessage());
        	}
        }
    }
}