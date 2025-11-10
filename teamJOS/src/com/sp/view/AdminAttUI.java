package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.AttDAO;
import com.sp.util.LoginInfo;

public class AdminAttUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private AttDAO attDao;
    private LoginInfo loginInfo;
    
    public AdminAttUI(AttDAO attDao, LoginInfo loginInfo) {
        this.attDao = attDao;
        this.loginInfo = loginInfo;
    }
    
    public void menu() {
        int ch;
        String input;
        
        System.out.println("\n[관리자 - 근태관리]");
        while(true) {
        	
        	try {
        		
        		do {
        			System.out.print("1.근태정보수정 2.휴가승인 3.근무시간조회 4.연차조회 5.메뉴로돌아가기 => ");
        			
        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 5);
        		
        		if(ch==5) return; // 5.메뉴화면으로
        		
        		switch(ch) {
        		case 1: updateAttendanceInfo(); break; // ATT_UPD_010
        		case 2: updateVacationApproveInfo(); break; // ATT_UPD_003
        		case 3: manageWorkTimeSearch(); break; // 3.근무시간조회 (하위 메뉴로 위임)
        		case 4: manageVacationSearch(); break; // 4.연차조회 (하위 메뉴로 위임)
        		}
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
    private void updateAttendanceInfo() {
    	System.out.println("\n[관리자 - 근태관리 - 근태정보수정]");
    	int ch;
    	String str, empNo;
    	try {
    		// 수정하려는 사원의 사원번호 입력
			System.out.print("수정을 원하는 사원의 사원번호를 입력하세요. => ");
			empNo = br.readLine();
			
			// 사원번호를 제대로 입력했는지 확인
			if(empNo==null || empNo.trim().isEmpty()) {
				System.out.println("사원번호를 입력하여 주세요.");
			} else {
				// 사원번호 검색 메소드 수정 중
			}
			
			do {
				System.out.println("1.출근일시수정 2.퇴근일시 수정 3.이전메뉴로돌아가기 => ");
				ch = Integer.parseInt(br.readLine());
			}while(ch < 1 || ch > 3);
			
			if(ch==3) return;
			
			System.out.println("수정할 일시 입력 (YYYY-MM-DD 24HH:MI) => ");
			str = br.readLine();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	protected void updateVacationApproveInfo() {
		System.out.println("\n[관리자 - 근태관리 - 휴가승인]");
		
	}

	// WBS의 4레벨 메뉴(3.근무시간조회) 처리를 위한 별도 메서드
    private void manageWorkTimeSearch() {
        int ch;
        System.out.println("\n[관리자 - 근태관리 - 근무시간조회]");
        try {
            do {
                System.out.print("1.전체조회 2.사번조회 3.상위메뉴로돌아가기 => ");
                ch = Integer.parseInt(br.readLine());
            } while(ch < 1 || ch > 3);

            switch (ch) {
            case 1: attDao.selectAllWorkTime(); break; // ATT_SEL_004
            case 2: attDao.selectWorkTimeByEmp(0); break; // ATT_SEL_005
            case 3: return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // WBS의 4레벨 메뉴(4.연차조회) 처리를 위한 별도 메서드
    private void manageVacationSearch() {
        int ch;
        System.out.println("\n[관리자 - 근태관리 - 연차조회]");
        try {
            do {
                System.out.print("1.전체조회 2.사번조회 3.상위메뉴로돌아가기 => ");
                ch = Integer.parseInt(br.readLine());
            } while(ch < 1 || ch > 3);

            switch (ch) {
            case 1: attDao.selectAllVacation(); break; // ATT_SEL_006
            case 2: attDao.selectVacationByEmp(0); break; // ATT_SEL_007
            case 3: return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}