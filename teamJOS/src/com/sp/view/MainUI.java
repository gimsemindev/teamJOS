package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
import com.sp.model.MemberDTO;
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

    // UI 초기화
    public AdminUI adminUI = new AdminUI(empDao, deptDao, attDao, authDao,boardDao);
    public EmployeeUI employeeUI = new EmployeeUI(empDao, deptDao, attDao, boardDao);

    /**
     * 프로그램 시작점
     */
    public void menu() {
        while (true) {
            MemberDTO member = login.loginMember();

            if (member == null) {           
                menuGuest(); 
            } else if ("admin".equalsIgnoreCase(member.getRole())) {  // 관리자
                menuAdmin();
            } else {                        // 일반 사원
                menuEmployee();
            }
        }
    }

    /**
     * 게스트(비로그인) 메뉴
     */
    private void menuGuest() {
        int ch = 0;

        do {
            try {
                System.out.println();
                System.out.println(CYAN + "╔════════════════════════════════════════╗" + RESET);
                System.out.println(CYAN + "║                                        ║" + RESET);
                System.out.println(CYAN + "║   🏢  " + YELLOW + " teamJOS 인사관리 시스템" + CYAN + "              ║" + RESET);
                System.out.println(CYAN + "║                                        ║" + RESET);
                System.out.println(CYAN + "╚════════════════════════════════════════╝" + RESET);

                System.out.println(GRAY + "──────────────────────────────────────────" + RESET);
                System.out.println(YELLOW + "   ① 로그인" + RESET);
                System.out.println(YELLOW + "   ② 종료" + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────" + RESET);
                System.out.print(GREEN + "👉 메뉴 선택 : " + RESET);

                ch = Integer.parseInt(br.readLine());

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
            System.out.print("아이디: ");
            String id = br.readLine();
            System.out.print("비밀번호: ");
            String pw = br.readLine();

            // 단순 로그인 예시 (실제 구현은 authDao를 활용)
            if (id.equals("admin") && pw.equals("1234")) {
                login.login(new MemberDTO(id, "관리자", "admin"));
                System.out.println("✅ 관리자 로그인 성공!\n");
            } else if (id.equals("user") && pw.equals("1234")) {
                login.login(new MemberDTO(id, "홍길동", "employee"));
                System.out.println("✅ 사원 로그인 성공!\n");
            } else {
                System.out.println("❌ 로그인 실패: 아이디 또는 비밀번호를 확인하세요.\n");
            }

        } catch (IOException e) {
            System.err.println("입력 오류: " + e.getMessage());
        }
    }

    /**
     * 일반 사원 메뉴
     */
    private void menuEmployee() {
        int ch = 0;

        try {
            MemberDTO member = login.loginMember();
            System.out.println("\n[" + member.getMemberName() + "] 님 (사원 권한)");

            do {
                System.out.print("1.사원관리 2.부서관리 3.근태관리 4.게시판관리 5.로그아웃 => ");
                ch = Integer.parseInt(br.readLine());
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
                    break;
            }
        } catch (IOException e) {
            System.err.println("입력 오류: " + e.getMessage());
        }
    }

    /**
     * 관리자 메뉴
     */
    private void menuAdmin() {
        int ch = 0;

        try {
            System.out.println("\n[관리자 모드]");

            do {
                System.out.print("1.사원관리 2.부서관리 3.근태관리 4.권한관리 5.게시판관리 6.로그아웃 => ");
                ch = Integer.parseInt(br.readLine());
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
                    break;
            }
        } catch (IOException e) {
            System.err.println("입력 오류: " + e.getMessage());
        }
    }
}