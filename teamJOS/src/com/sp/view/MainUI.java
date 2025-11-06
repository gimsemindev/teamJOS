package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sp.view.AdminUI;
import com.sp.util.DBConn;
import com.sp.util.LoginInfo;


public class MainUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	private LoginInfo login = new LoginInfo();
	private AdminUI AdminUI = new AdminUI();
	//private EmployeeUI EmployeeUI = new EmployeeUI(login);
	
	public void start() {
//        while (true) {
//            try {
//                if (login.loginMember() == null) {
//                    showLoginMenu();
//                } else {
//                    showMainMenu();
//                }
//            } catch (Exception e) {
//                System.err.println("⚠️ 시스템 오류: " + e.getMessage());
//            }
//        }
   }

    /**
     * 로그인 전 메뉴 출력
     */
    private void showLoginMenu() throws IOException {
        System.out.println("\n==============================");
        System.out.println("   [teamJOS 인사관리 시스템]");
        System.out.println("==============================");
        System.out.println("1. 로그인");
        System.out.println("2. 종료");
        System.out.print("선택 >> ");

        String choice = br.readLine();

        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                exitProgram();
                break;
            default:
                System.out.println("⚠️ 잘못된 입력입니다. 다시 선택해주세요.");
        }
    }

    /**
     * 로그인 로직
     */
    private void login() throws IOException {
        System.out.print("아이디: ");
        String id = br.readLine();
        System.out.print("비밀번호: ");
        String pw = br.readLine();

        // 간단한 로그인 예시 (추후 DB 연동 가능)
        if (id.equals("admin") && pw.equals("1234")) {
            System.out.println("✅ 관리자 로그인 성공!");
           // login.login(new MemberDTO(id, "관리자", "admin"));
        } else if (id.equals("user") && pw.equals("1234")) {
            System.out.println("✅ 일반사원 로그인 성공!");
          //  login.login(new MemberDTO(id, "홍길동", "employee"));
        } else {
            System.out.println("❌ 로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
        }
    }

    /**
     * 로그인 후 권한별 메인 메뉴
     */
//    private void showMainMenu() throws IOException {
//        String role = login.loginMember().getRole();
//        if ("admin".equalsIgnoreCase(role)) {
//        	AdminUI.showMenu();
//        } else {
//        	EmployeeUI.showMenu();
//        }
//    }

    /**
     * 프로그램 종료
     */
    private void exitProgram() {
        System.out.println("\n프로그램을 종료합니다.");
        DBConn.close();
        System.exit(0);
    }
}