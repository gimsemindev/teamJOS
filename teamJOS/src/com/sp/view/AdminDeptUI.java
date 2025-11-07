package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.DeptDAO;

public class AdminDeptUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private DeptDAO deptDao;
    
    public AdminDeptUI(DeptDAO deptDao) {
        this.deptDao = deptDao;
    }
    
    public void menu() {
        int ch;
        System.out.println("\n[관리자 - 부서관리]");
        
        try {
            
            do {
                System.out.print("1.부서등록 2.부서수정 3.부서조회 4.부서삭제 5.부서인원현황 6.메뉴로돌아가기 => ");
                ch = Integer.parseInt(br.readLine());
            } while(ch < 1 || ch > 6);
            
            switch(ch) {
            case 1: deptDao.insertDept(null); break; // DEPT_INS_001
            case 2: deptDao.updateDept(null); break; // DEPT_UPD_002
            case 3: deptDao.selectAllDept(); break; // DEPT_SEL_003
            case 4: deptDao.deleteDept(0); break; // DEPT_DEL_004
            case 5: deptDao.selectDeptMemberCount(); break; // DEPT_SEL_005
            case 6: return; // 6.메뉴화면으로
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}