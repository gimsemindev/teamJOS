package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.DeptDAO;
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
        printTitle("[🏢 부서관리]");
        
        while(true) {
        	
        	try {
        		
        		do {
        			System.out.print("1.부서조회 2.부서인원현황 3.메뉴로돌아가기 => ");

        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 3);
        		
        		switch(ch) {
        		case 1: deptCommonUI.selectAllDept(); break; // DEPT_SEL_003  (기존 코드의 selectDeptByNo(0)은 selectAllDept로 수정)
        		case 2: deptCommonUI.selectDeptMember(); break; // DEPT_SEL_005 
        		case 3: return; // 3. 메뉴화면으로 
        		}
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
        
    }

}