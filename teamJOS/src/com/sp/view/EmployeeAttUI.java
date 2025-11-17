package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.sp.dao.AttDAO;
import com.sp.dao.impl.AttDAOImpl;
import com.sp.exception.UserQuitException;
import com.sp.model.AttendanceDTO;
import com.sp.model.VacationDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.view.common.DeptCommonUI;

import static com.sp.util.PrintUtil.*;

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
        		case 4: attDao.updateVacation(null); break; // ATT_UPD_009 
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
    
    
}