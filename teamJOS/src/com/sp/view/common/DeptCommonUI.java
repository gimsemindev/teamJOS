package com.sp.view.common;

import java.util.List;

import com.sp.dao.DeptDAO;
import com.sp.dao.impl.DeptDAOImpl;
import com.sp.model.DeptDTO;
import com.sp.util.PrintUtil;

public class DeptCommonUI {
    private DeptDAO deptDao = new DeptDAOImpl();
    
	public void selectAllDept() {
		System.out.println("\n전체 부서 리스트...");

        List<DeptDTO> list = deptDao.selectAllDept();

        System.out.println("전체 부서수 : " + list.size());    
        System.out.println("============================================================");
        System.out.printf("%s | %s | %s\n",
        		PrintUtil.padCenter("부서코드", 14),
        		PrintUtil.padCenter("부서명", 34),
        		PrintUtil.padCenter("내선번호",10));
        System.out.println("============================================================");
        
        for(DeptDTO dto : list) {            
            System.out.printf("%s | %s \t | %s\n",
            		PrintUtil.padCenter(dto.getDeptCd(), 12),
            		PrintUtil.padRight(dto.getDeptNm(), 32),
            		PrintUtil.padCenter(dto.getExtNo(), 8));
        }
        System.out.println("------------------------------------------------------------");
	}

}
