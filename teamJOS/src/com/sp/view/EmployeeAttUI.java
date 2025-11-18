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

/**
 * <h2>EmployeeAttUI (일반 사원 근태 관리 UI)</h2>
 *
 * <p>일반 사원 메뉴에서 자신의 근태(출퇴근 기록) 등록 및 조회, 휴가 신청 및 수정, 연차 현황 조회 기능을
 * 제어하는 콘솔 기반 UI 클래스입니다.</p>
 *
 * <h3>주요 기능 (유스케이스 ID)</h3>
 * <ul>
 * <li>출근 등록 (ATT_INS_001) - 현재 시각을 기준으로 출근 기록 등록</li>
 * <li>퇴근 등록 (ATT_INS_002) - 현재 시각을 기준으로 퇴근 기록 등록</li>
 * <li>휴가 신청 (ATT_INS_008) - 휴가 시작일, 종료일, 사유를 입력받아 휴가 신청</li>
 * <li>휴가 수정 (ATT_UPD_009) - 미승인 상태의 휴가 신청을 조회하고 수정</li>
 * <li>연차 조회 (ATT_SEL_007) - 전체 사원의 연차 잔여 현황 조회 (DeptCommonUI 위임)</li>
 * <li>근태 조회 (ATT_SEL_001, ATT_SEL_002) - 특정 날짜의 출퇴근 기록 및 근무 시간 조회</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 이지영, 오다은, 황선호</p>
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class EmployeeAttUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private AttDAO attDao;
    private LoginInfo loginInfo;
    private DeptCommonUI deptCommonUI;
    
    /**
     * EmployeeAttUI 생성자
     *
     * @param attDao 근태 DAO (데이터 접근 객체)
     * @param loginInfo 로그인 사용자 정보 객체
     */
    public EmployeeAttUI(AttDAO attDao, LoginInfo loginInfo) {
    	this.loginInfo = loginInfo;
        this.attDao = new AttDAOImpl(this.loginInfo);
        this.deptCommonUI = new DeptCommonUI(loginInfo);
    }
    
    /**
     * 일반 사원 근태 관리 메인 메뉴 화면을 출력하고 사용자 입력을 처리합니다.
     *
     * <p>1~6번까지의 기능을 선택하여 근태 등록/조회 및 휴가 신청 기능을 실행합니다.</p>
     * <p>사용자 입력 'q' 또는 'Q' 입력 시 상위 메뉴로 돌아갑니다.</p>
     */
    public void menu() {
        int ch;
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
        		case 3: insertVacation(); break; // ATT_INS_008
        		case 4: updateVacation(); break; // ATT_UPD_009 
        		case 5: deptCommonUI.selectAllAnnualLeave(); break; // ATT_SEL_007 
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
    
	/**
	 * 출근 시간 등록 기능 (ATT_INS_001)
	 *
	 * <p>현재 로그인된 사원의 사번으로 현재 시각을 출근 시간으로 기록합니다.
	 * 당일 이미 출근 기록이 있으면 등록 실패 메시지를 출력합니다.</p>
	 */
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

	/**
	 * 퇴근 시간 등록 기능 (ATT_INS_002)
	 *
	 * <p>현재 로그인된 사원의 사번으로 현재 시각을 퇴근 시간으로 기록합니다.
	 * 출근 기록이 없거나 이미 퇴근 기록이 있으면 처리 실패 메시지를 출력합니다.</p>
	 */
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
	
	/**
	 * 근태 정보 조회 기능 (ATT_SEL_001, ATT_SEL_002)
	 *
	 * <p>사용자로부터 특정 날짜를 입력받아 해당 날짜의 자신의 출퇴근 기록 및 근무 시간 정보를 조회합니다.</p>
	 */
    protected void selectAttendaceInfo() {
    	printTitle("⏰ [근태 정보 조회]");
    	AttendanceDTO att = new AttendanceDTO();
    	String empNo = loginInfo.loginMember().getMemberId();
    	att.setEmpNo(empNo);
    	String inputDt;
    	try {
    		while(true) {
    		printLine(GREEN, "👉 조회할 날짜 ? ex.2025-10-10 [종료:'q'] ");
    		inputDt = br.readLine();
    		
    		InputValidator.isUserExit(inputDt); 
            if (InputValidator.isValidDate(inputDt)) {
            	att.setRegDt(inputDt);
                break;
            }
            printLineln(MAGENTA, "❌ 날짜 형식이 올바르지 않습니다. (YYYY-MM-DD 형식으로 입력하세요)");
    		}
    		
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

	/**
	 * 휴가 신청 기능 (ATT_INS_008)
	 *
	 * <p>휴가 시작일, 종료일, 사유를 입력받아 휴가를 신청합니다.
	 * 신청된 휴가는 미승인 상태로 저장되며, 관리자의 승인을 기다립니다.</p>
	 */
	public void insertVacation() {
		printTitle("🧳 [휴가 신청]");
    	VacationDTO dto = new VacationDTO();
    	
    	String inputDt;
        
        try {
        	while (true) {
                printLine(GREEN, "👉 휴가 시작일자 (YYYY-MM-DD, 종료:'q') ? ");
                inputDt = br.readLine();

                InputValidator.isUserExit(inputDt); 
                if (InputValidator.isValidDate(inputDt)) {
                    dto.setStartDt(inputDt);
                    break;
                }
                
            }
            
       
            while (true) {
                printLine(GREEN, "👉 휴가 종료일자 (YYYY-MM-DD, 종료:'q') ? ");
                inputDt = br.readLine();
                
         
                InputValidator.isUserExit(inputDt);

                if (InputValidator.isValidDate(inputDt)) {
                    dto.setEndDt(inputDt);
                    break;
                }
                printLineln(MAGENTA, "❌ 날짜 형식이 올바르지 않습니다. (YYYY-MM-DD 형식으로 입력하세요)");
            }
            
 
            printLine(GREEN, "👉 휴가 사유 (종료:'q') ? ");
            String memo = br.readLine();
            

            InputValidator.isUserExit(memo);
            dto.setVacationMemo(memo);
            
            attDao.insertVacation(dto);
            
            printLineln(MAGENTA, "📢 휴가 신청 완료!");
      
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 작업을 취소하였습니다.");
			return;    
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * 휴가 수정 기능 (ATT_UPD_009)
	 *
	 * <p>현재 자신의 미승인된 휴가 신청 목록을 조회하고, 수정할 휴가 번호를 입력받아
	 * 휴가 기간 및 사유를 변경하여 다시 신청합니다.</p>
	 */
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