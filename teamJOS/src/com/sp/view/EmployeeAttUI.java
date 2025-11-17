package com.sp.view;

import static com.sp.util.PrintUtil.GREEN;
import static com.sp.util.PrintUtil.MAGENTA;
import static com.sp.util.PrintUtil.YELLOW;
import static com.sp.util.PrintUtil.printLine;
import static com.sp.util.PrintUtil.printLineln;
import static com.sp.util.PrintUtil.printMenu;
import static com.sp.util.PrintUtil.printSection;
import static com.sp.util.PrintUtil.printTitle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.sp.dao.AttDAO;
import com.sp.dao.impl.AttDAOImpl;
import com.sp.exception.UserQuitException;
import com.sp.model.AttendanceDTO;
import com.sp.model.VacationDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;
import com.sp.view.common.DeptCommonUI;

public class EmployeeAttUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private AttDAO attDao;
    private LoginInfo loginInfo;
    private DeptCommonUI deptCommonUI;
    
    public EmployeeAttUI(AttDAO attDao, LoginInfo loginInfo) {
    	this.loginInfo = loginInfo;
        this.attDao = new AttDAOImpl(this.loginInfo);
        this.deptCommonUI = new DeptCommonUI(loginInfo);
    }
    
    // EmployeeUI의 manageAttendance() 기능을 menu()로 변경
    public void menu() {
        int ch;
        String role = loginInfo.loginMember().getRole();
        String deptCd = loginInfo.loginMember().getDeptCd();
        String input;

        while(true) {
        	try {
        		do {
        			printTitle("📌 [근태관리]");
        			printMenu(YELLOW, "① 출근 등록", "② 퇴근 등록", "③ 휴가 신청", "④ 휴가 수정", "⑤ 연차 조회", "⑥ 근태 조회");
        			
        			input = br.readLine();
        			InputValidator.isUserExit(input);
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 6);
        		
        		switch(ch) {
        		case 1: insertAttendanceInInfo(); break; // ATT_INS_001 
        		case 2: insertAttendanceOutInfo(); break; // ATT_INS_002 
        		case 3: insertVacation(); break; // ATT_INS_008 (기존 코드의 insertVacation을 requestVacation으로 수정) 
        		case 4: updateVacation(); break; // ATT_UPD_009 
        		case 5: deptCommonUI.selectAllAnnualLeave(); break; // ATT_SEL_007 
//        		case 6: attDao.selectWorkTimeByEmp(0); break; // ATT_SEL_005 
        		case 6: selectAttendaceInfo(); break;
        		}
        	} catch (NumberFormatException e) {
        		printLineln(MAGENTA, "📢 1 ~ 6 사이의 숫자만 입력 가능합니다.");
			} catch (UserQuitException e) {
    			printLineln(MAGENTA, "📢 작업을 취소하였습니다.");
    			return;
    	    } catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
	protected void insertAttendanceInInfo() {
		printTitle("⏰ [출근 시간 입력]");
    	AttendanceDTO att = new AttendanceDTO();
    	String empNo = loginInfo.loginMember().getMemberId();
    	att.setEmpNo(empNo);
    	
    	try {
    		printLine(GREEN, "👉 출근 시간을 입력하시겠습니까? [ Y | N ] ");
			String ch = br.readLine();
			ch = ch.toUpperCase();
			
			switch (ch) {
			case "Y": {
				String msg = attDao.insertAttendanceIn(att); 
				printLineln(MAGENTA, "📢 " + msg);
				break;
			}
			case "N": printLineln(GREEN, "👉 출근 입력을 취소했습니다."); return;
			default: printLineln(MAGENTA, "📢 Y | N 만 입력 가능합니다."); break;
			}
		} catch (Exception e) {
		}
	}

	protected void insertAttendanceOutInfo() {
		printTitle("⏰ [퇴근 시간 입력]");
    	AttendanceDTO att = new AttendanceDTO();
    	String empNo = loginInfo.loginMember().getMemberId();
    	att.setEmpNo(empNo);
    	
    	try {
    		printLine(GREEN, "👉 퇴근 시간을 입력하시겠습니까? [ Y | N ] ");
            String ch = br.readLine().toUpperCase();

            switch (ch) {
                case "Y": {
                    String msg = attDao.insertAttendanceOut(att);
                    printLineln(MAGENTA, "📢 " + msg);
                    break;
                }
                case "N":
                	printLineln(MAGENTA, "📢 퇴근 입력을 취소했습니다.");
                    return;
                default:
                	printLineln(MAGENTA, "📢 Y | N 만 입력 가능합니다.");
                    break;
            }
		} catch (Exception e) {
		}
	}
	
    protected void selectAttendaceInfo() {
    	printTitle("⏰ [근태 정보 조회]");
    	AttendanceDTO att = new AttendanceDTO();
    	String empNo = loginInfo.loginMember().getMemberId();
    	att.setEmpNo(empNo);
    	try {
    		printLine(GREEN, "👉 조회할 날짜 ? ex.2025-10-10 ");
			att.setRegDt(br.readLine());
			
			att = attDao.selectAttendance(att);
			
			if(att == null) {
				printLineln(MAGENTA, "📢 등록된 날짜가 아닙니다.\n");
				return;
			}
			
			System.out.print(att.getEmpNo() + "\t");
			System.out.print(att.getAtdNo() + "\t");
			System.out.print(att.getCheckIn() + "\t");
			System.out.print(att.getCheckOut() + "\t");
			System.out.print(att.getWorkHours() + "\t");
			System.out.print(att.getAtdStatusCd() + "\t");
			System.out.println(att.getRegDt());
			
			printLineln(MAGENTA, "📢 조회 완료되었습니다.");
		} catch (Exception e) {
			
		}
		
	}

	public void insertVacation() {
		printTitle("🧳 [휴가 신청]");
    	VacationDTO dto = new VacationDTO();
    	
    	try {
    		printLine(GREEN, "👉 휴가 시작일자 ? ");
			dto.setStartDt(br.readLine()); 
			
			printLine(GREEN, "👉 휴가 종료일자 ? ");
			dto.setEndDt(br.readLine());
			
			printLine(GREEN, "👉 휴가 사유 ? ");
			dto.setVacationMemo(br.readLine());
    		
			attDao.insertVacation(dto);
			
			printLineln(MAGENTA, "📢 휴가 신청 완료!");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
   		} catch (IOException e) {
   			e.printStackTrace();
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
    }
	
	public void updateVacation() {
		printTitle("🧳 [휴가 수정]");
		VacationDTO dto = new VacationDTO();
		String empNo = loginInfo.loginMember().getMemberId();
    	dto.setEmpNo(empNo);
		
    	try {
    		List<VacationDTO> list = attDao.listVaction(dto);
			
			String msg = " 미승인 휴가 신청 (총 " + list.size() + "건)";
			
			printSection(msg);
			
			// 헤더 출력
			System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t | %s\t\n", PrintUtil.padCenter("번호", 12),
					PrintUtil.padCenter("사번", 8), PrintUtil.padCenter("시작일", 12), PrintUtil.padCenter("종료일", 12),
					PrintUtil.padCenter("신청사유", 8), PrintUtil.padCenter("승인상태", 8)

			);

			PrintUtil.printLine('─', 100);

			if (list.isEmpty()) {
				printLine(MAGENTA, "📢 현재 미승인된 휴가 신청이 없습니다.");
				PrintUtil.printLine('─', 100);
				return;
			}

			// 목록 출력
			for (VacationDTO dto1 : list) {
				System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t | %s\t\n",
						PrintUtil.padCenter(Integer.toString(dto1.getVacationSeq()), 12),
						PrintUtil.padCenter(dto1.getEmpNo(), 8), PrintUtil.padCenter(dto1.getStartDt(), 12),
						PrintUtil.padCenter(dto1.getEndDt(), 12),
						PrintUtil.padCenter(dto1.getVacationMemo() != null && dto1.getVacationMemo().length() > 18
								? dto1.getVacationMemo().substring(0, 15) + "..."
								: dto1.getVacationMemo(), 8),
						PrintUtil.padCenter(dto1.getApproverYn(), 8));
			}
			PrintUtil.printLine('-', 100);
    		
			String inputSeq;
	        int vacationSeq;
			while(true) {
	            printLine(GREEN, "👉 수정할 휴가 번호를 입력하세요 (취소: Enter) : ");
	            inputSeq = br.readLine();
	            if(inputSeq.trim().isEmpty()) {
	                printLineln(MAGENTA, "📢 수정을 취소했습니다.");
	                return;
	            }
	            try {
	                vacationSeq = Integer.parseInt(inputSeq);
	                break;
	            } catch (NumberFormatException e) {
	                printLineln(MAGENTA, "❌ 잘못된 입력입니다. 숫자만 입력하세요.");
	            }
	        }
	        dto.setVacationSeq(vacationSeq); 

	        printLine(GREEN, "새 휴가 시작일자 (YYYY-MM-DD) ? ");
	        dto.setStartDt(br.readLine()); 
	        
	        printLine(GREEN, "새 휴가 종료일자 (YYYY-MM-DD) ? ");
	        dto.setEndDt(br.readLine());
	        
	        printLine(GREEN, "새 휴가 사유 ? ");
	        dto.setVacationMemo(br.readLine());

	        int result = attDao.updateVacation(dto); 
	        
	        if (result > 0) {
	            printLineln(GREEN, "✅ 휴가 수정 신청이 완료되었습니다.");
	        } else {
	            printLineln(MAGENTA, "❌ 휴가 수정에 실패했습니다. (이미 승인되었거나 존재하지 않는 번호)");
	        }
    		
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	} catch (IOException e) {
    		e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    
}