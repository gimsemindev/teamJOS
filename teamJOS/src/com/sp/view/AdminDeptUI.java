package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.sp.dao.DeptDAO;
import com.sp.dao.impl.DeptDAOImpl;
import com.sp.exception.UserQuitException;
import com.sp.model.DeptDTO;
import com.sp.util.InputValidator;
import com.sp.view.common.DeptCommonUI;



public class AdminDeptUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private DeptDAO deptDao = new DeptDAOImpl();
    private DeptCommonUI deptCommonUI = new DeptCommonUI();
    
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
            case 1: insertDept(); break; // DEPT_INS_001
            case 2: deptDao.updateDept(null); break; // DEPT_UPD_002
            case 3: deptCommonUI.selectAllDept(); break; // DEPT_SEL_003  (기존 코드의 selectDeptByNo(0)은 selectAllDept로 수정)
            case 4: deptDao.deleteDept(0); break; // DEPT_DEL_004
            case 5: deptDao.selectDeptMemberCount(); break; // DEPT_SEL_005
            case 6: return; // 6.메뉴화면으로
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void insertDept() {
        System.out.println("\n[부서 등록]");       
		
		DeptDTO dto = new DeptDTO();

        try {     
            while (true) {
                System.out.print("부서코드를 입력 [예: D10000, 입력중단:q]: ");
                
                String deptCd = br.readLine().trim(); 

                // 부서코드 필수 값 : null 이나 공백은 허용안됨
                if (deptCd.isEmpty()) {
                    System.out.println("부서코드는 필수 입력사항입니다. 다시 입력하세요.\n");
                    continue;
                }
                
                // 사용자가 q를 입력하면 루프를 빠져나간다.
                InputValidator.isUserExit(deptCd);
                
                // 부서코드는 영문 D로 시작하는 5자리 문자여야함
                if (!InputValidator.isValidDeptCode(deptCd)) {
                    continue;
                }
                    
                
                System.out.print("부서명을 입력 [예: 마케팅부, 입력중단:q]: ");
                String deptNm = br.readLine().trim();
                                
                if (deptNm.isEmpty()) {
                    System.out.println("부서명은 필수 입력사항입니다. 다시 입력하세요.\n");
                    continue;
                }
                
                // 사용자가 q를 입력하면 루프를 빠져나간다.
                InputValidator.isUserExit(deptNm);
                
                
                System.out.print("내선번호를 입력(미배정시 엔터) [예: 1111, 입력중단: q]: ");
                String extNo = br.readLine().trim();
                
                // 사용자가 q를 입력하면 루프를 빠져나간다.
                InputValidator.isUserExit(extNo);
                             
                System.out.print("상위부서코드 입력(미배정시 엔터) [예: D10000, 입력중단:q]: ");
                String superDeptCd = br.readLine().trim(); 
         
                // 사용자가 q를 입력하면 루프를 빠져나간다.
                InputValidator.isUserExit(superDeptCd);
                                   
                System.out.print("사용여부 입력 [예: Y 또는 N, 입력중단:q] : ");
                String useYn = br.readLine().trim();
                if (useYn.isEmpty()) useYn = "Y";
                
                // 사용자가 q를 입력하면 루프를 빠져나간다.
                InputValidator.isUserExit(useYn);
                             
                dto.setDeptCd(deptCd);
                dto.setDeptNm(deptNm);
                dto.setExtNo(extNo);
                dto.setSuperDeptCd(superDeptCd);
                dto.setUseYn(useYn);
                
                break;
            }            
            
            deptDao.insertDept(dto);

            System.out.println("데이터 등록이 완료 되었습니다.");
            
        } catch (UserQuitException e) {
            System.out.println(e.getMessage());
        }
          catch (SQLIntegrityConstraintViolationException e) {
                // 기본키 중복, NOT NULL 제약 위반등
            if(e.getErrorCode() == 1) {
                System.out.println("에러-부서코드 중복으로 추가가 불가능합니다.");
            } else if (e.getErrorCode() == 1400) {
                // INSERT - NOT NULL 제약 위반
                System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
            } else {
                System.out.println(e.toString());
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }	
	}    
    
}