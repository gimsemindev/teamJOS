package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.AttDAO;

public class AdminAttUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private AttDAO attDao;
    
    public AdminAttUI(AttDAO attDao) {
        this.attDao = attDao;
    }
    
    public void menu() {
        int ch;
        System.out.println("\n[관리자 - 근태관리]");
        
        try {
            
            do {
                System.out.print("1.근태정보수정 2.휴가승인 3.근무시간조회 4.연차조회 5.메뉴로돌아가기 => ");
                ch = Integer.parseInt(br.readLine());
            } while(ch < 1 || ch > 5);
            
            switch(ch) {
            case 1: attDao.updateAttendance(null); break; // ATT_UPD_010
            case 2: attDao.updateVacationApprove(null); break; // ATT_UPD_003
            case 3: manageWorkTimeSearch(); break; // 3.근무시간조회 (하위 메뉴로 위임)
            case 4: manageVacationSearch(); break; // 4.연차조회 (하위 메뉴로 위임)
            case 5: return; // 5.메뉴화면으로
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
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