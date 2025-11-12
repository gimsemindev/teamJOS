package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.AttDAO;
import com.sp.util.LoginInfo;

public class EmployeeAttUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private AttDAO attDao;
    private LoginInfo loginInfo;
    
    public EmployeeAttUI(AttDAO attDao, LoginInfo loginInfo) {
        this.attDao = attDao;
        this.loginInfo = loginInfo;
    }
    
    // EmployeeUI의 manageAttendance() 기능을 menu()로 변경
    public void menu() {
        int ch;
        String role = loginInfo.loginMember().getRole();
        String deptCd = loginInfo.loginMember().getDeptCd();
        String input;
        System.out.println("\n[근태관리]");
        
        while(true) {
        	
        	try {
        		
        		do {
        			System.out.print("1.출근등록 2.퇴근등록 3.휴가신청 4.휴가수정 5.연차조회 6.근무시간조회 7.메뉴로돌아가기 => ");
        			
        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 7);
        		
        		switch(ch) {
        		case 1: attDao.insertAttendanceIn(null); break; // ATT_INS_001 
        		case 2: attDao.insertAttendanceOut(null); break; // ATT_INS_002 
        		case 3: attDao.insertVacation(null); break; // ATT_INS_008 (기존 코드의 insertVacation을 requestVacation으로 수정) 
        		case 4: attDao.updateVacation(null); break; // ATT_UPD_009 
        		case 5: attDao.selectAllAnnualLeave(1,1); break; // ATT_SEL_007 
        		case 6: attDao.selectWorkTimeByEmp(0); break; // ATT_SEL_005 
        		case 7: return; // 7.메뉴화면으로 
        		}
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
}