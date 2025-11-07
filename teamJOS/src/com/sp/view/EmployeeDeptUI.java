package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import com.sp.dao.DeptDAO;
import com.sp.model.DeptDTO;

public class EmployeeDeptUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private DeptDAO deptDao;
    
    public EmployeeDeptUI(DeptDAO deptDao) {
        this.deptDao = deptDao;
    }
    
    // EmployeeUI의 manageDepartment() 기능을 menu()로 변경
    public void menu() {
        int ch;
        System.out.println("\n[부서관리]");
        
        try {
            
            do {
                System.out.print("1.부서조회 2.부서인원현황 3.메뉴로돌아가기 => ");
                ch = Integer.parseInt(br.readLine());
            } while(ch < 1 || ch > 3);
            
            switch(ch) {
            case 1: selectAllDept(); break; // DEPT_SEL_003  (기존 코드의 selectDeptByNo(0)은 selectAllDept로 수정)
            case 2: deptDao.selectDeptMemberCount(); break; // DEPT_SEL_005 
            case 3: return; // 3. 메뉴화면으로 
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public void selectAllDept() {
		System.out.println("\n전체 부서 리스트...");

        List<DeptDTO> list = deptDao.selectAllDept();

        System.out.println("전체 부서수 : " + list.size());
        System.out.println("==================================");
        System.out.println("🏢부서코드 | 부서명");
        System.out.println("==================================");
        
        for(DeptDTO dto : list) {
            System.out.print(dto.getDeptCd() + "\t");
            System.out.print("|");
            System.out.println(dto.getDeptNm() + "\t");
        }
        System.out.println("----------------------------------");
	}
}