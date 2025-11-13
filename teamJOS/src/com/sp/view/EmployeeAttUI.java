package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.sp.dao.AttDAO;
import com.sp.dao.impl.AttDAOImpl;
import com.sp.model.AttendanceDTO;
import com.sp.model.VacationDTO;
import com.sp.util.LoginInfo;
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
        System.out.println("\n[근태관리]");
        
        while(true) {
        	
        	try {
        		
        		do {
        			System.out.print("1.출근등록 2.퇴근등록 3.휴가신청 4.휴가수정 5.연차조회 6.근태조회 7.메뉴로돌아가기 => ");
        			
        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 7);
        		
        		switch(ch) {
        		case 1: insertAttendanceInInfo(); break; // ATT_INS_001 
        		case 2: insertAttendanceOutInfo(); break; // ATT_INS_002 
        		case 3: insertVacation(); break; // ATT_INS_008 (기존 코드의 insertVacation을 requestVacation으로 수정) 
        		case 4: attDao.updateVacation(null); break; // ATT_UPD_009 
        		case 5: deptCommonUI.selectAllAnnualLeave(); break; // ATT_SEL_007 
//        		case 6: attDao.selectWorkTimeByEmp(0); break; // ATT_SEL_005 
        		case 6: selectAttendaceInfo(); break;
        		case 7: return; // 7.메뉴화면으로 
        		}
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
	protected void insertAttendanceInInfo() {
    	System.out.println("[출근시간입력]");
    	AttendanceDTO att = new AttendanceDTO();
    	String empNo = loginInfo.loginMember().getMemberId();
    	att.setEmpNo(empNo);
    	
    	try {
			System.out.println("출근 시간을 입력하시겠습니까? [ Y | N ] ");
			String ch = br.readLine();
			ch = ch.toUpperCase();
			
			switch (ch) {
			case "Y": {
				String msg = attDao.insertAttendanceIn(att); 
				System.out.println(msg);
				break;
			}
			case "N": System.out.println("출근 입력을 취소했습니다."); return;
			default: System.out.println("Y | N 만 입력 가능합니다."); break;
			}
		} catch (Exception e) {
		}
	}

	protected void insertAttendanceOutInfo() {
		System.out.println("[퇴근시간입력]");
    	AttendanceDTO att = new AttendanceDTO();
    	String empNo = loginInfo.loginMember().getMemberId();
    	att.setEmpNo(empNo);
    	
    	try {
    		System.out.println("퇴근 시간을 입력하시겠습니까? [ Y | N ] ");
            String ch = br.readLine().toUpperCase();

            switch (ch) {
                case "Y": {
                    String msg = attDao.insertAttendanceOut(att);
                    System.out.println(msg);
                    break;
                }
                case "N":
                    System.out.println("퇴근 입력을 취소했습니다.");
                    return;
                default:
                    System.out.println("Y | N 만 입력 가능합니다.");
                    break;
            }
		} catch (Exception e) {
		}
	}
	
    protected void selectAttendaceInfo() {
    	System.out.println("[근태 정보 조회]");
    	AttendanceDTO att = new AttendanceDTO();
    	String empNo = loginInfo.loginMember().getMemberId();
    	att.setEmpNo(empNo);
    	try {
			System.out.println("조회할 날짜 ? ex.2025-10-10 ");
			att.setRegDt(br.readLine());
			
			att = attDao.selectAttendance(att);
			
			if(att == null) {
				System.out.println("등록된 날짜가 아닙니다.\n");
				return;
			}
			
			System.out.print(att.getEmpNo() + "\t");
			System.out.print(att.getAtdNo() + "\t");
			System.out.print(att.getCheckIn() + "\t");
			System.out.print(att.getCheckOut() + "\t");
			System.out.print(att.getWorkHours() + "\t");
			System.out.print(att.getAtdStatusCd() + "\t");
			System.out.println(att.getRegDt());
			
			System.out.println("조회 완료되었습니다.");
		} catch (Exception e) {
			
		}
		
	}

	public void insertVacation() {
    	System.out.println("\n[휴가 신청]");
    	VacationDTO dto = new VacationDTO();
    	
    	try {
			System.out.print("휴가 시작일자 ? ");
			dto.setStartDt(br.readLine()); 
			
			System.out.print("휴가 종료일자 ? ");
			dto.setEndDt(br.readLine());
			
			System.out.print("휴가 사유 ? ");
			dto.setVacationMemo(br.readLine());
    		
			attDao.insertVacation(dto);
			
			System.out.println("휴가 신청 완료!");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
   		} catch (IOException e) {
   			e.printStackTrace();
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
    }
    
    
}