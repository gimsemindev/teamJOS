package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.DeptDAO;
import com.sp.exception.UserQuitException;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.view.common.DeptCommonUI;

import static com.sp.util.PrintUtil.*;

public class EmployeeDeptUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private DeptDAO deptDao;
    private DeptCommonUI deptCommonUI = null;
    private LoginInfo loginInfo;
    
    public EmployeeDeptUI(DeptDAO deptDao, LoginInfo loginInfo) {
        this.deptDao = deptDao;
        this.loginInfo = loginInfo;
		this.deptCommonUI = new DeptCommonUI(loginInfo);        
    }
    
    // EmployeeUI의 manageDepartment() 기능을 menu()로 변경
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
        		case 1: deptCommonUI.selectAllDept(); break; // DEPT_SEL_003  (기존 코드의 selectDeptByNo(0)은 selectAllDept로 수정)
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