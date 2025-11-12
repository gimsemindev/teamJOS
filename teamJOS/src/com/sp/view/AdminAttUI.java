package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.sp.dao.AttDAO;
import com.sp.model.VacationDTO;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;
import com.sp.view.common.DeptCommonUI;


public class AdminAttUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private AttDAO attDao;
    private LoginInfo loginInfo;
    private DeptCommonUI deptCommonUI;
    
    public AdminAttUI(AttDAO attDao, LoginInfo loginInfo) {
        this.attDao = attDao;
        this.loginInfo = loginInfo;
        this.deptCommonUI = new DeptCommonUI(loginInfo);
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
        		case 4: deptCommonUI.selectAllAnnualLeave(); break; // 4.연차조회 (전체조회) // ATT_SEL_006
        		}
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    protected void updateAttendanceInfo() {
    	
    }
    
    protected void updateVacationApproveInfo() {
		// ANSI Escape Codes (색상 상수)
		final String RESET  = "\u001B[0m";
		final String GREEN  = "\u001B[32m";
		final String YELLOW = "\u001B[33m";
		final String CYAN   = "\u001B[36m";
		final String RED    = "\u001B[31m";
		final String GRAY   = "\u001B[90m";

		System.out.println(CYAN + "\n╔════════════════════════════════════════╗" + RESET);
		System.out.println(CYAN + "║       🗓️  관리자 - 휴가 승인 관리            ║" + RESET);
		System.out.println(CYAN + "╚════════════════════════════════════════╝" + RESET);

		String input;
		int vacationSeq;

		try {
			// 1. 미승인 휴가 목록 조회 및 출력
			List<VacationDTO> list = attDao.listVaction();
			
			
			PrintUtil.printLine('─', 100);
			System.out.println(YELLOW + " 미승인 휴가 신청 (총 " + list.size() + "건)" + RESET);
			PrintUtil.printLine('─', 100);
            // 헤더 출력
            System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t | %s\t\n",
            		PrintUtil.padCenter("번호", 12),
            		PrintUtil.padCenter("사번", 8),
            		PrintUtil.padCenter("시작일", 12),
            		PrintUtil.padCenter("종료일", 12),
            		PrintUtil.padCenter("신청사유", 8),
            		PrintUtil.padCenter("승인상태", 8)
            		
            		);
            
			PrintUtil.printLine('-', 100);


			if (list.isEmpty()) {
				System.out.println(CYAN + "👉 현재 미승인된 휴가 신청이 없습니다." + RESET);
				PrintUtil.printLine('-', 100);
				return;
			}
            
			// 목록 출력
			for(VacationDTO dto : list) {
				System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t | %s\t\n",
						PrintUtil.padCenter(Integer.toString(dto.getVacationSeq()), 12),
	            		PrintUtil.padCenter(dto.getEmpNo(), 8),
	            		PrintUtil.padCenter(dto.getStartDt(),  12),
	            		PrintUtil.padCenter(dto.getEndDt(),12),
	            		PrintUtil.padCenter(dto.getVacationMemo() != null && dto.getVacationMemo().length() > 18 ? dto.getVacationMemo().substring(0, 15) + "..." : dto.getVacationMemo(), 8),
	            		PrintUtil.padCenter(dto.getApproverYn(), 8));
			}
			PrintUtil.printLine('-', 100);
			
			// 2. 승인 번호 입력
			System.out.print(GREEN + "👉 승인하실 휴가 신청 번호를 입력하세요 (취소: Enter) : " + RESET);
			input = br.readLine();
            
            if (input == null || input.trim().isEmpty()) {
                System.out.println(GRAY + "취소되었습니다." + RESET);
                return;
            }

            // NumberFormatException 처리
			vacationSeq = Integer.parseInt(input.trim());
			
			// 3. DAO 호출 (updateVacationApprove: 프로시저 호출)
			attDao.updateVacationApprove(vacationSeq); // ⚠️ DAO 메서드명을 approveVacation으로 통일하여 사용합니다.
			
			System.out.println(GREEN + "\n✅ 휴가 신청 번호 " + vacationSeq + " 승인 및 연차 차감 완료." + RESET);
			
		} catch (NumberFormatException e) {
			System.out.println(RED + "❌ 입력 오류: 휴가 번호는 숫자로만 입력해야 합니다." + RESET);
		} catch (SQLException e) {
			// PL/SQL 프로시저에서 발생한 에러 코드 처리 (-20000 대 오류)
			if(e.getErrorCode() == 20001) {
				System.out.println(RED + "❌ 승인 실패: 입력하신 번호에 해당하는 휴가 신청번호가 없거나 연차 정보가 없습니다." + RESET);
			} else if (e.getErrorCode() == 20003) {
                // 잔여 연차 부족 상세 메시지 출력
                String errorDetail = e.getMessage().substring(e.getMessage().indexOf(":") + 1).trim();
				System.out.println(RED + "❌ 승인 실패: 잔여 연차가 부족합니다. (" + errorDetail + ")" + RESET);
			} else if (e.getErrorCode() == 20099) {
				System.out.println(RED + "❌ 승인 실패: 시스템 오류로 승인 중 오류가 발생했습니다." + RESET);
			} else {
				System.out.println(RED + "❌ DB 오류 발생 (코드: " + e.getErrorCode() + "): " + e.getMessage() + RESET);
			}	
		} catch (IOException e) {
			System.out.println(RED + "❌ 입출력 오류가 발생했습니다." + RESET);
		} catch (Exception e) {
			System.out.println(RED + "❌ 알 수 없는 오류가 발생했습니다: " + e.getMessage() + RESET);
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

}