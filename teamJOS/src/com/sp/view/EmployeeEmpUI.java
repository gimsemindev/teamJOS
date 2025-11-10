package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.EmpDAO;
import com.sp.util.LoginInfo;

public class EmployeeEmpUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private EmpDAO empDao;
    private LoginInfo loginInfo;
    
    public EmployeeEmpUI(EmpDAO empDao, LoginInfo loginInfo) {
        this.empDao = empDao;
        this.loginInfo = loginInfo;
;
    }
    
    // EmployeeUI의 manageEmployee() 기능을 menu()로 변경
    public void menu() {
        int ch;
        String input;
        System.out.println("\n[사원관리]");
      
        
        while(true){
        	
        	try {    
        		
        		do {
        			System.out.print("1.정보등록 2.정보수정 3.정보조회 4.부서이동이력조회 5.이력조회  6.메뉴로돌아가기 => ");

        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 6);
        		
        		switch (ch) {
        		case 1: empDao.insertEmployee(null); break; // EMP_INS_001 
        		//case 2: empDao.updateEmployee(null); break; // EMP_UPD_002 
        		//case 3: empDao.selectByEmpNo(); break; // EMP_SEL_005 
//        		case 4: empDao.selectDeptMove(); break; // EMP_SEL_012 
//        		case 5: empDao.selectHistory(); break; // EMP_SEL_011 
        		case 6: return; // 6. 메뉴화면으로 
        		}
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
}