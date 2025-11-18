package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.DeptDAO;
import com.sp.exception.UserQuitException;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.view.common.DeptCommonUI;

import static com.sp.util.PrintUtil.*;

/**
 * <h2>EmployeeDeptUI (일반 사용자 부서 관리 UI)</h2>
 *
 * <p>일반 사원/인사 담당자가 부서 정보 및 부서별 인원 현황을 조회하는 콘솔 기반 UI 클래스입니다.</p>
 *
 * <h3>주요 기능 (유스케이스 ID)</h3>
 * <ul>
 * <li>전체 부서 조회 (DEPT_SEL_003) - 등록된 모든 부서 목록을 조회합니다.</li>
 * <li>부서 인원 현황 조회 (DEPT_SEL_005) - 각 부서별 소속 인원 현황을 조회합니다.</li>
 * </ul>
 *
 * <p>이 클래스는 {@code DeptDAO} 및 공통 UI 로직을 담고 있는 {@code DeptCommonUI}를 활용하여
 * 데이터를 출력합니다.</p>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 김세민, 황선호</p>
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class EmployeeDeptUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private DeptDAO deptDao;
    private DeptCommonUI deptCommonUI = null;
    private LoginInfo loginInfo;
    
    /**
     * EmployeeDeptUI 생성자
     *
     * @param deptDao 부서 DAO (데이터 접근 객체)
     * @param loginInfo 로그인 사용자 정보 객체
     */
    public EmployeeDeptUI(DeptDAO deptDao, LoginInfo loginInfo) {
        this.deptDao = deptDao;
        this.loginInfo = loginInfo;
		this.deptCommonUI = new DeptCommonUI(loginInfo);        
    }
    
    /**
     * 부서 관리 메인 메뉴 화면을 출력하고 사용자 입력을 처리합니다.
     *
     * <p>1. 부서 조회 (DEPT_SEL_003), 2. 부서 인원 현황 (DEPT_SEL_005) 기능을 제공합니다.</p>
     * <p>사용자 입력 'q' 또는 'Q' 입력 시 상위 메뉴로 돌아갑니다.</p>
     */
    public void menu() {
        int ch;
        String input;

        while(true) {
        	try {
        		do {
        			printTitle("🏢 [부서 관리]");
        			printMenu(YELLOW, "① 부서 조회", "② 부서 인원 현황");

        			input = br.readLine();
        			InputValidator.isUserExit(input);
        			
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 2);
        		
        		switch(ch) {
        		case 1: deptCommonUI.selectAllDept(); break; // DEPT_SEL_003
        		case 2: deptCommonUI.selectDeptMember(); break; // DEPT_SEL_005 
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
}