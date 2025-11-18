package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.sp.dao.AuthDAO;
import com.sp.exception.UserQuitException;
import com.sp.model.LoginDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;

import static com.sp.util.PrintUtil.*;

/**
 * <h2>AdminAuthUI (관리자 - 권한 관리 UI)</h2>
 *
 * <p>관리자 모드에서 사원의 권한 레벨을 수정(승격/강등)하는 기능을 제공하는 UI 클래스입니다.
 * 주로 일반 사원({@code 01})과 관리자({@code 03}) 간의 권한 변경을 처리합니다.</p>
 *
 * <h3>📌 관련 서비스 번호(Service ID)</h3>
 * <ul>
 *   <li><b>AUTH_UPD_002</b> — 관리자 정보 수정 (관리자 → 일반사원 강등)</li>
 *   <li><b>AUTH_INS_001</b> — 관리자 계정 등록 (일반사원 → 관리자 승격)</li>
 * </ul>
 *
 * <h3>주요 기능</h3>
 * <ul>
 *   <li>관리자 계정 등록 (일반 사원 → 관리자 승격)</li>
 *   <li>관리자 계정 수정 (관리자 → 일반 사원 강등)</li>
 *   <li>현재 로그인한 관리자 본인의 권한 강등 방지 로직 포함</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 황선호</p>
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class AdminAuthUI {
    /** ANSI 이스케이프 코드: 색상 리셋 */
	final String RESET  = "\u001B[0m";
    /** ANSI 이스케이프 코드: 초록색 */
    final String GREEN  = "\u001B[32m";
    /** ANSI 이스케이프 코드: 노란색 */
    final String YELLOW = "\u001B[33m";
	
    /** 콘솔 입력을 위한 {@code BufferedReader} 객체 */
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    /** 권한 관리를 위한 데이터 접근 객체 (DAO) */
    private AuthDAO authDao;
    /** 현재 로그인 정보를 담는 유틸리티 객체 */
    private LoginInfo loginInfo;
    
    /** 관리자 권한 레벨 코드 상수 ("03") */
    private static final String ADMIN_LEVEL_CODE = "03";
    /** 일반 사원 권한 레벨 코드 상수 ("01") */
    private static final String EMPLOYEE_LEVEL_CODE = "01";
    
    /**
     * AdminAuthUI의 생성자입니다.
     *
     * @param authDao 권한 관리를 위한 DAO 객체
     * @param loginInfo 로그인 정보를 담는 유틸리티 객체
     */
    public AdminAuthUI(AuthDAO authDao, LoginInfo loginInfo) {
        this.authDao = authDao;
        this.loginInfo = loginInfo;
    }
    
    /**
     * <h3>권한 관리 메인 메뉴 루프</h3>
     *
     * <p>관리자에게 '관리자 정보 수정(강등)' 또는 '관리자 계정 등록(승격)' 메뉴를 보여주고
     * 사용자 입력을 받아 해당 기능으로 분기합니다. 'q' 입력 시 이전 화면으로 돌아갑니다.</p>
     *
     * <p><b>관련 Service ID</b></p>
     * <ul>
     *   <li>AUTH_UPD_002 — 관리자 강등</li>
     *   <li>AUTH_INS_001 — 관리자 승격</li>
     * </ul>
     */
    public void menu() {
        int ch;
        String input;
        
        while(true) {
        	try {
        		do {
        			printTitle("🏢 [관리자 - 권한관리] ");
        			printMenu(YELLOW, "① 관리자 정보 수정", "② 관리자 계정 등록");
        			
        			input = br.readLine();
        			InputValidator.isUserExit(input);
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 2);
        		
        		switch(ch) {
        		case 1: updateAdmin(); break; // AUTH_UPD_002
        		case 2: insertAdmin(); break; // AUTH_INS_001
        		}
        		
        	} catch (NumberFormatException e) {
				printLineln(MAGENTA, "📢 1 ~ 2 사이의 숫자만 입력 가능합니다.");
			} catch (UserQuitException e) {
				printLineln(MAGENTA, "📢 작업을 취소하였습니다.");
				return;
		    } catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
    /**
     * <h3>관리자 계정 수정 (권한 강등)</h3>
     *
     * <p>특정 사번의 관리자 권한을 일반 사원({@code EMPLOYEE_LEVEL_CODE})으로 강등 처리합니다.
     * 단, 현재 로그인한 관리자 본인의 권한은 강등할 수 없습니다.</p>
     *
     * <p><b>Service ID:</b> AUTH_UPD_002</p>
     */
    public void updateAdmin() {
    	printTitle("🏢 [관리자 계정 수정]");
    	String empNo = null;
    	try {
    		printLine(GREEN, "👉 수정할 사번 (관리자 ID): ");
            empNo = br.readLine();
            
            LoginDTO currentUser = loginInfo.loginMember();
            
            if (currentUser != null && currentUser.getMemberId().equals(empNo)) {
            	printLineln(MAGENTA, "❌ 권한 강등 실패: 현재 로그인한 관리자 본인의 권한은 강등할 수 없습니다.");
                return; 
            }
            
            int result = authDao.insertAdmin(empNo, EMPLOYEE_LEVEL_CODE);
            
            if (result > 0) {
            	printLineln(MAGENTA, "✅ 권한 수정 완료! 사번 " + empNo + "이(가) 일반사원으로 변경되었습니다.");
            } else {
            	printLineln(MAGENTA, "❌ 권한 수정 실패: 해당 사번이 존재하지 않거나, 이미 일반사원입니다.");
            }
            
        } catch (IOException e) {
            System.err.println("입력 중 오류 발생.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    		
    }
    
    /**
     * <h3>관리자 계정 등록 (권한 승격)</h3>
     *
     * <p>특정 사번의 일반 사원 권한을 관리자({@code ADMIN_LEVEL_CODE})로 승격 처리합니다.
     * 해당 사번이 존재하지 않거나 이미 관리자인 경우 실패 처리됩니다.</p>
     *
     * <p><b>Service ID:</b> AUTH_INS_001</p>
     */
    public void insertAdmin() {
    	printTitle("🏢 [관리자 계정 등록]");
    	String empNo = null;
    	
    	try {
    		printLine(GREEN, "👉 등록할 사번 (관리자 ID): ");
            empNo = br.readLine();
            
            int result = authDao.insertAdmin(empNo, ADMIN_LEVEL_CODE);
            
            if (result > 0) {
            	printLineln(MAGENTA, "✅ 권한 등록 완료! 사번 " + empNo + "이(가) 관리자로 승급되었습니다.");
            } else {
            	printLineln(MAGENTA, "❌ 권한 등록 실패: 해당 사번이 존재하지 않거나, 이미 관리자입니다.");
            }
            
        } catch (IOException e) {
            System.err.println("입력 중 오류 발생.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
