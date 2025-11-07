package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.EmpDAO;

public class AdminEmpUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private EmpDAO empDao;
    
    public AdminEmpUI(EmpDAO empDao) {
        this.empDao = empDao;
    }
    
    public void menu() {
        int ch;
        System.out.println("\n[관리자 - 사원관리]");
        try {
            
            do {
                System.out.print("1.정보등록 2.정보수정 3.부서이동 4.진급관리 5.정보조회 6.퇴직신청결재 7.경력등록 8.자격증및포상 9.이력조회 10.메뉴로돌아가기 => ");
                ch = Integer.parseInt(br.readLine());
            } while(ch < 1 || ch > 10);

            switch (ch) {
            case 1: empDao.insertEmployee(null); break; // EMP_INS_001
            case 2: empDao.updateEmployee(null); break; // EMP_UPD_002
            case 3: empDao.updateDeptMove(null); break; // EMP_UPD_003
            case 4: empDao.updatePromotion(null); break; // EMP_UPD_004
            case 5: manageEmployeeSearch(); break; // 5.정보조회 (하위 메뉴로 위임)
            case 6: empDao.updateRetireApproval(0); break; // EMP_UPD_008
            case 7: empDao.insertCareer(null); break; // EMP_INS_009
            case 8: empDao.insertLicense(null); break; // EMP_INS_010
            case 9: empDao.selectHistory(0); break; // EMP_SEL_011
            case 10: return; // 10.메뉴화면으로
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // WBS의 4레벨 메뉴(5.정보조회) 처리를 위한 별도 메서드
    private void manageEmployeeSearch() {
        int ch;
        System.out.println("\n[관리자 - 사원관리 - 정보조회]");
        try {
            do {
                System.out.print("1.사번조회 2.이름조회 3.전체조회 4.상위메뉴로돌아가기 => ");
                ch = Integer.parseInt(br.readLine());
            } while(ch < 1 || ch > 4);

            switch (ch) {
            case 1: empDao.selectByEmpNo(0); break; // EMP_SEL_005
            case 2: empDao.selectByName(null); break; // EMP_SEL_006
            case 3: empDao.selectAll(); break; // EMP_SEL_007
            case 4: return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}