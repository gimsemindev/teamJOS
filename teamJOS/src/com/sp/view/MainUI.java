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
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;

/**
 * <h2>MainUI (프로그램 메인 UI)</h2>
 *
 * <p>로그인 상태에 따라 게스트/사원/관리자 메뉴를 구분하여 보여주는  
 * 프로그램 진입점 역할의 UI 클래스입니다.</p>
 *
 * <ul>
 *   <li>게스트 모드: 로그인 / 종료</li>
 *   <li>사원 모드: 사원/부서/근태/게시판 기능</li>
 *   <li>관리자 모드: 사원/부서/근태/권한/게시판 관리 기능</li>
 * </ul>
 *
 * <p>필요한 DAO 객체들을 초기화하고  
 * 각 화면 기능을 AdminUI / EmployeeUI 에 위임합니다.</p>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
  * <p><b>작성자:</b> 황선호</p>
 * <p><b>작성일:</b> 2025-11-16</p>
 * <p><b>버전:</b> 0.9</p> 
 */
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

    // UI 초기화 (DI)
    public AdminUI adminUI = null;
    public EmployeeUI employeeUI = null;
    
    // 권한 레벨 상수
    private static final int AUTH_LEVEL_ADMIN = 3;
    private static final int AUTH_LEVEL_EMPLOYEE = 1;
    
    /**
     * <h3>프로그램 실행 메인 루프</h3>
     *
     * <p>로그인된 사용자의 권한(role)에 따라  
     * 게스트 → 사원메뉴 → 관리자메뉴 순으로 분기합니다.</p>
     */
    public void menu() {
        while (true) {

            LoginDTO member = login.loginMember();

            if (member == null) {
                menuGuest();
                continue;
            }

            adminUI = new AdminUI(empDao, deptDao, attDao, authDao, boardDao, login);
            employeeUI = new EmployeeUI(empDao, deptDao, attDao, boardDao, login);

            int authLevel;
            try {
                authLevel = Integer.parseInt(member.getRole());
            } catch (NumberFormatException e) {
                System.out.println(GRAY + "경고: 알 수 없는 권한 정보입니다. 로그아웃 처리합니다." + RESET);
                login.logout();
                continue;
            }

            if (authLevel == AUTH_LEVEL_ADMIN) {
                menuAdmin();
            } else if (authLevel == AUTH_LEVEL_EMPLOYEE) {
                menuEmployee();
            } else {
                System.out.println(GRAY + "정의되지 않은 권한 레벨(" + member.getRole() + ")입니다. 로그아웃 처리합니다." + RESET);
                login.logout();
            }
        }
    }

    /**
     * <h3>게스트(비로그인) 메뉴</h3>
     *
     * <p>로그인 전 사용자에게 보여지는 초기 화면입니다.</p>
     * <ul>
     *   <li>1. 로그인</li>
     *   <li>2. 종료</li>
     * </ul>
     */
    private void menuGuest() {
        int ch = 0;
        String input;

        do {
            try {
                System.out.println();
                System.out.println(CYAN + "════════════════════════════════════════" + RESET);
                System.out.println(CYAN + "                                        " + RESET);
                System.out.println(CYAN + "   🏢  " + YELLOW + " teamJOS 인사관리 시스템" + CYAN + RESET);
                System.out.println(CYAN + "                                        " + RESET);
                System.out.println(CYAN + "════════════════════════════════════════" + RESET);

                System.out.println(GRAY + "──────────────────────────────────────────" + RESET);
                System.out.println(YELLOW + "   ① 로그인" + RESET);
                System.out.println(YELLOW + "   ② 종료" + RESET);
                System.out.println(GRAY + "──────────────────────────────────────────" + RESET);
                System.out.print(GREEN + "👉 메뉴 선택 : " + RESET);
                
                input = br.readLine();
                
                if (input == null || input.trim().isEmpty()) {
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
                loginProcess();
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
     * <h3>로그인 처리</h3>
     *
     * <p>사번(ID), 비밀번호 입력 후  
     * loginDao.login() 을 통해 인증을 수행합니다.</p>
     */
    private void loginProcess() {
    	try {
			System.out.print(GREEN + "👉 사번(아이디) : ");
			String empNo = br.readLine();
			System.out.print(GREEN + "👉 비밀번호: ");
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
     * <h3>일반 사원 메뉴</h3>
     *
     * <p>사원 모드에서 접근 가능한 기능:</p>
     * <ul>
     *   <li>사원관리</li>
     *   <li>부서관리</li>
     *   <li>근태관리</li>
     *   <li>게시판</li>
     *   <li>로그아웃</li>
     * </ul>
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
        			PrintUtil.printMenu(YELLOW, "① 사원 관리", "② 부서 관리", "③ 근태 관리", "④ 게시판", "⑤ 로그아웃");
        			
        			input = br.readLine();
        			InputValidator.isUserExit(input);
        			
                    if (input == null || input.trim().isEmpty()) {
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
        			System.out.println(GREEN + "로그아웃 되었습니다.\n" + RESET);
        			return;
        		}
        	} catch (IOException e) {
        		System.err.println("입력 오류: " + e.getMessage());
        	} catch (Exception e) {
        		ch = 0;
        	}
        }
        
    }

    /**
     * <h3>관리자 메뉴</h3>
     *
     * <p>관리자가 사용할 수 있는 기능:</p>
     * <ul>
     *   <li>사원관리</li>
     *   <li>부서관리</li>
     *   <li>근태관리</li>
     *   <li>권한관리</li>
     *   <li>게시판관리</li>
     *   <li>로그아웃</li>
     * </ul>
     */
    private void menuAdmin() {
        int ch = 0;
        String input;
        
        while(true) {
        	
        	try {
        		PrintUtil.printTitle("[관리자 모드]");
        		
        		do {
        			PrintUtil.printMenu(YELLOW, "① 사원 관리", "② 부서 관리", "③ 근태 관리","④ 권한 관리", "⑤ 게시판 관리", "⑥ 로그아웃");

        			input = br.readLine();
        			InputValidator.isUserExit(input);
                    
                    if (input == null || input.trim().isEmpty()) {
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
        			System.out.println(GREEN + "로그아웃 되었습니다.\n" + RESET);
        			return;
        		}
        	} catch (IOException e) {
        		System.err.println("입력 오류: " + e.getMessage());
        	}  catch (Exception e) {
        		ch = 0;
        	}
        }
    }
}
