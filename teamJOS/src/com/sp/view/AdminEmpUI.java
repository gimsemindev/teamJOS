package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.EmpDAO;
import com.sp.model.EmployeeDTO;

public class AdminEmpUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private EmpDAO empDao;
    
    public AdminEmpUI(EmpDAO empDao) {
        this.empDao = empDao;
    }
    
    public void menu() {
        int ch;
        String input;
        
        System.out.println("\n[관리자 - 사원관리]");
        
        while(true) {
        	
        	try {
        		
        		do {
        			System.out.print("1.정보등록 2.정보수정 3.부서이동 4.진급관리 5.정보조회 6.퇴직신청결재 7.경력등록 8.자격증및포상 9.이력조회 10.메뉴로돌아가기 => ");
        			
        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 10);
        		
        		if(ch==10) return; // 10.메뉴화면으로
        		
        		switch (ch) {
        		case 1: insertEmployeeInfo(); break;
        		case 2: updateEmployeeinfo(); break; // EMP_UPD_002
        		//case 3: updateDeptMoveInfo(); break; // EMP_UPD_003
        		//case 4: updatePromotionInfo(); break; // EMP_UPD_004
        		case 5: manageEmployeeSearch(); break; // 5.정보조회 (하위 메뉴로 위임)
        		case 6: updateRetireApprovalInfo(); break; // EMP_UPD_008
        		case 7: insertCareerInfo(); break; // EMP_INS_009
        		case 8: insertLicenseInfo(); break; // EMP_INS_010
        		case 9: selectHistoryInfo(); break; // EMP_SEL_011
        		}
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }

	// 사원 등록
	public void insertEmployeeInfo() {
		System.out.println("\n[관리자 - 사원관리 - 사원 정보 등록]");
		EmployeeDTO dto = new EmployeeDTO();

		try {
			System.out.print("사원번호(ex.90001) : ");
			dto.setEmpNo(br.readLine());

			System.out.print("이름 : ");
			dto.setEmpNm(br.readLine());

			System.out.print("주민번호(ex. 000000-0000000) : ");
			dto.setRrn(br.readLine());

			System.out.print("주소 : ");
			dto.setEmpAddr(br.readLine());

			System.out.print("부서코드 : ");
			dto.setDeptCd(br.readLine());

			System.out.print("직급코드 : ");
			dto.setGradeCd(br.readLine());

			System.out.print("사원상태코드(ex. A=재직, R=퇴직 등) : ");
			dto.setEmpStatCd(br.readLine());

			System.out.print("계약구분코드(ex. 1=정규직, 2=계약직) : ");
			dto.setContractTpCd(br.readLine());

			System.out.print("이메일 : ");
			dto.setEmail(br.readLine());

			System.out.print("비밀번호 : ");
			dto.setPwd(br.readLine());

			System.out.print("레벨코드(ex. 01=일반, 02=관리자) : ");
			dto.setLevelCode(br.readLine());

			empDao.insertEmployee(dto);
			System.out.println("사원 정보 등록이 완료 되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();

			System.out.println("사원 정보 등록 중 오류가 발생했습니다.");
		}
	}
	
//	private void updateDeptMoveInfo() {
//	    System.out.println("\n[관리자 - 사원관리 - 부서이동 관리]");
//	    try {
//	        EmployeeDTO dto = new EmployeeDTO();
//
//	        System.out.print("부서이동 할 사원번호(ex.90001) : ");
//	        dto.setEmpNo(br.readLine());
//
//	        System.out.print("현재 부서코드(예: D001): ");
//	        String currentDept = br.readLine();
//
//	        System.out.print("이동할 부서코드(예: D004): ");
//	        dto.setDeptCd(br.readLine());
//
//	        empDao.updateDeptMove(dto);
//
//	        System.out.println("\n부서이동이 완료되었습니다.");
//	        System.out.println("사원번호: " + dto.getEmpNo());
//	        System.out.println("변경된 부서코드: " + dto.getDeptCd());
//
//	    } catch (Exception e) {
//	        System.out.println("\n부서이동 중 오류가 발생했습니다.");
//	        e.printStackTrace();
//	    }
//	}
//
//	private void updatePromotionInfo() {
//	    System.out.println("\n[관리자 - 사원관리 - 진급관리]");
//	    try {
//	        EmployeeDTO dto = new EmployeeDTO();
//
//	        System.out.print("진급할 사원번호(예: E0005): ");
//	        dto.setEmpNo(br.readLine());
//
//	        System.out.print("현재 직급코드(예: 01=사원, 02=대리): ");
//	        String currentGrade = br.readLine();
//
//	        System.out.print("진급할 직급코드(예: 03=과장, 04=차장): ");
//	        dto.setGradeCd(br.readLine());
//
//	        // DAO 호출
//	        empDao.updatePromotion(dto);
//
//	        System.out.println("\n진급 처리가 완료되었습니다.");
//	        System.out.println("사원번호: " + dto.getEmpNo());
//	        System.out.println("변경된 직급코드: " + dto.getGradeCd());
//
//	    } catch (Exception e) {
//	        System.out.println("\n진급 처리 중 오류가 발생했습니다.");
//	        e.printStackTrace();
//	    }
//	}
	
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
            case 1: empDao.selectByEmpNo(null); break; // EMP_SEL_005
            case 2: empDao.selectByName(null); break; // EMP_SEL_006
            case 3: empDao.selectAll(); break; // EMP_SEL_007
            case 4: return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 사원 정보 수정 UI
    protected void updateEmployeeinfo() {
    	System.out.println("\n[관리자 - 사원관리 - 정보수정]");
    	int ch;
    	String empNo, col=null, con;
    	
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
				// 수정할 개인정보 입력
				System.out.println("수정을 원하는 번호를 입력하세요.");
                System.out.print("1.이름 2.주소 3.이메일 4.비밀번호 5.권한레벨코드 6.상위메뉴로돌아가기 => ");
                ch = Integer.parseInt(br.readLine());
                
                // 수정 내용 입력
                System.out.println("변경 내용을 입력하세요. => ");
                con = br.readLine();
                
            } while(ch < 1 || ch > 6);
			
			if(ch==6) return;
			
			switch (ch) {
			case 1: col="EMP_NM"; break;
			case 2: col="EMP_ADDR"; break;
			case 3: col="EMP_EMAIL"; break;
			case 4: col="EMP_PWD"; break;
			case 5: col="LEVEL_CODE"; break;
			}
			// update문에 사용될 사원번호, 컬럼 이름, 수정내용을 메소드로 전달
			empDao.updateEmployee(empNo, col, con);
			
		} catch (Exception e) {
			
		}
	}
    
    protected void updateRetireApprovalInfo() {
    	System.out.println("\n[관리자 - 사원관리 - 퇴직신청결재]");
	}
    
    protected void insertCareerInfo() {
    	System.out.println("\n[관리자 - 사원관리 - 경력등록]");
	}
    
    protected void insertLicenseInfo() {
    	System.out.println("\n[관리자 - 사원관리 - 자격증및포상]");
	}
    
    protected void selectHistoryInfo() {
    	System.out.println("\n[관리자 - 사원관리 - 이력조회]");
	}

}