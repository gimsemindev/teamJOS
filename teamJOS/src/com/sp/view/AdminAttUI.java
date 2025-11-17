package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.sp.dao.AttDAO;
import com.sp.dao.EmpDAO;
import com.sp.dao.impl.EmpDAOImpl;
import com.sp.exception.UserQuitException;
import com.sp.model.AttendanceDTO;
import com.sp.model.VacationDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;
import com.sp.view.common.DeptCommonUI;

import static com.sp.util.PrintUtil.*;

public class AdminAttUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private EmpDAO empDao = new EmpDAOImpl();
	private AttDAO attDao;
	private LoginInfo loginInfo;
	private DeptCommonUI deptCommonUI;

	public AdminAttUI(AttDAO attDao, LoginInfo loginInfo) {
		this.attDao = attDao;
		this.loginInfo = loginInfo;
		this.deptCommonUI = new DeptCommonUI(this.loginInfo);
	}

	public void menu() {
		int ch;
		String input;

		System.out.println();

		while (true) {
			try {
				do {
					printTitle("🏢 [관리자 - 근태관리]");
					printMenu(YELLOW, "① 출근 시간 입력", "② 퇴근 시간 입력", "③ 근태 정보 수정", "④ 근태 조회", "⑤ 휴가 승인", "⑥ 연차 조회",
							"⑦ 상위 메뉴로 돌아가기" + PrintUtil.RESET);

					input = br.readLine();

					if (input == null || input.trim().isEmpty()) {
						ch = 0;
						continue;
					}
					ch = Integer.parseInt(input);

				} while (ch < 1 || ch > 7);

				if (ch == 7)
					return; // 5.메뉴화면으로

				switch (ch) {
				case 1:
					insertCheckInInfo();
					break; // 1. 출근시간 입력
				case 2:
					insertCheckOutInfo();
					break; // 2. 퇴근시간 입력
				case 3:
					updateAttendanceInfo();
					break; // 3.근태정보수정 // ATT_UPD_010
				case 4:
					selectAttendanceInfo();
					break; // 4. 근태정보조회
				case 5:
					updateVacationApproveInfo();
					break; // 5.휴가승인 // ATT_UPD_003
				case 6:
					deptCommonUI.selectAllAnnualLeave();
					break; // 6.연차조회 (전체조회) // ATT_SEL_006
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 출근 시간 입력
	protected void insertCheckInInfo() {
		PrintUtil.printTitle("[관리자 - 근태관리 - 출근시간입력]");
		AttendanceDTO att = new AttendanceDTO();
		String empNo = loginInfo.loginMember().getMemberId();
		att.setEmpNo(empNo);

		try {
			printLine(GREEN, "❓ 출근 시간을 입력하시겠습니까? [ Y | N ] ");
			String ch = br.readLine();
			ch = ch.toUpperCase();

			switch (ch) {
			case "Y": {
				String msg = attDao.insertAttendanceIn(att);
				msg = "📢 " + msg;
				printLineln(MAGENTA, msg);
				break;
			}
			case "N":
				printLineln(MAGENTA, "📢 출근 입력을 취소하였습니다.");
				return;
			default:
				printLineln(MAGENTA, "📢 Y | N 만 입력 가능합니다.");
				break;
			}
		} catch (Exception e) {
		}
	}

	// 퇴근 시간 입력
	protected void insertCheckOutInfo() {
		printTitle("[관리자 - 근태관리 - 퇴근 시간 입력]");
		AttendanceDTO att = new AttendanceDTO();
		String empNo = loginInfo.loginMember().getMemberId();
		att.setEmpNo(empNo);

		try {
			printLine(GREEN, "❓ 퇴근 시간을 입력하시겠습니까? [ Y | N ] ");
			String ch = br.readLine().toUpperCase();

			switch (ch) {
			case "Y": {
				String msg = attDao.insertAttendanceOut(att);
				msg = "📢 " + msg;
				printLineln(MAGENTA, msg);
				break;
			}
			case "N":
				printLineln(MAGENTA, "📢 퇴근 입력을 취소하였습니다.");
				return;
			default:
				printLineln(MAGENTA, "📢 Y | N 만 입력 가능합니다.");
				break;
			}
		} catch (Exception e) {
		}
	}

	// 근태 정보 수정
	protected void updateAttendanceInfo() {
		AttendanceDTO att = new AttendanceDTO();
		printTitle("[관리자 - 근태관리 - 근태정보수정]");
		
		try {
			while(true) {
				att.setEmpNo(checkEmpNo(true));
				
				printLine(PrintUtil.GREEN, null);
				printLine(GREEN, "❓ 조회할 날짜 (ex.2025-10-10) : ");
				String date = (br.readLine());
				InputValidator.isUserExit(date);
				InputValidator.isValidDate(date);
				
				att.setRegDt(date);

				printLine(CYAN, "❓ 수정할 항목 ? ");
				printMenu(YELLOW, " ① 출근일시", " ② 출근일시", " ③ 상위 메뉴로 돌아가기");

				int ch = Integer.parseInt(br.readLine());
				if (ch == 3)
					return;

				String col = switch (ch) {
					case 1 -> "CHECK_IN";
					case 2 -> "CHECK_OUT";
					default -> null;
				};

				if (col == null) {
					printLineln(MAGENTA, "📢 잘못된 입력입니다\n");
					return;
				}

				att.setAtdNo(col);

				boolean canUpdate = attDao.checkAtdColumnIsNull(att);

				if (!canUpdate) {
					printLineln(MAGENTA, "❌ 해당 근태는 수정할 수 없습니다.\\n");
					return; // 상위 메뉴로
				}
				
				printLine(GREEN, "❓ 변경할 값 입력(ex.2025-11-11 09:00:00) : ");
				att.setAtdStatusCd(br.readLine());

				String msg = attDao.updateAttendance(att);
				msg = "📢 " + msg; 
				printLineln(MAGENTA, msg);
				System.out.println();
			}
		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 작업을 취소하였습니다.");
	    } catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 근태 정보 조회
	protected void selectAttendanceInfo() {
		printTitle("[관리자 - 근태관리 - 근태정보조회]");
		try {
			while(true) {
				printLine(GREEN, " ❓ 조회할 날짜 (ex.2025-10-10) [q:돌아가기] : ");
				String date = (br.readLine());
				InputValidator.isUserExit(date);
				InputValidator.isValidDate(date);

				List<AttendanceDTO> list = attDao.selectAttendanceAll(date);
				
				 if (list == null || list.isEmpty()) {
		                printLineln(MAGENTA, "📢 조회된 근태 정보가 없습니다.");
		                continue;
		            }
				 /*
				for (AttendanceDTO att : list) {
					System.out.print(att.getEmpNo() + "\t");
					System.out.print(att.getAtdNo() + "\t");
					System.out.print(att.getCheckIn() + "\t");
					System.out.print(att.getCheckOut() + "\t");
					System.out.print(att.getWorkHours() + "\t");
					System.out.print(att.getAtdStatusCd() + "\t");
					System.out.println(att.getRegDt());
				}
				printLineln(MAGENTA, "📢 조회 완료되었습니다.");
				*/
				 
				 final int pageSize = 10;
		            int total = list.size();
		            int totalPage = (total + pageSize - 1) / pageSize;
		            int page = 1;

		            while (true) {
		                int startIndex = (page - 1) * pageSize;
		                int endIndex = Math.min(startIndex + pageSize, total);

		                System.out.println();
		                System.out.printf("▶ 근태 조회 결과 | 페이지 %d / %d | 총 %d건 | 조회범위: %d~%d%n",
		                        page, totalPage, total, startIndex + 1, endIndex);
		                PrintUtil.printLine('═', 120);

		                // 컬럼 헤더
		                System.out.printf("%s | %s | %s | %s | %s | %s\t | %s%n",
		                        PrintUtil.padCenter("사번", 7),
		                        PrintUtil.padCenter("근태번호", 9),
		                        PrintUtil.padCenter("출근시간", 21),
		                        PrintUtil.padCenter("퇴근시간", 22),
		                        PrintUtil.padCenter("근무시간", 10),
		                        PrintUtil.padCenter("상태", 6),
		                        PrintUtil.padCenter("등록일", 12)
		                );
		                PrintUtil.printLine('─', 120);

		                // 데이터 출력
		                for (int i = startIndex; i < endIndex; i++) {
		                    AttendanceDTO d = list.get(i);

		                    System.out.printf("%s | %s | %s | %s | %s | %s\t | %s%n",
		                            PrintUtil.padRight(d.getEmpNo(), 6),
		                            PrintUtil.padRight(d.getAtdNo(), 8),
		                            PrintUtil.padRight(d.getCheckIn(), 20),
		                            PrintUtil.padRight(d.getCheckOut(), 20),
		                            PrintUtil.padRight(String.valueOf(d.getWorkHours()), 8),
		                            PrintUtil.padRight(d.getAtdStatusCd(), 6),
		                            PrintUtil.padRight(d.getRegDt(), 12)
		                    );
		                }

		                PrintUtil.printLine('═', 120);
		                printLine(GREEN, "[n: 다음, p: 이전, q: 종료] 👉 ");
		                String cmd = br.readLine();
		                if (cmd == null) cmd = "";
		                cmd = cmd.trim().toLowerCase();

		                if ("n".equals(cmd)) {
		                    if (page < totalPage) page++;
		                    else System.out.println("마지막 페이지입니다.\n");
		                } else if ("p".equals(cmd)) {
		                    if (page > 1) page--;
		                    else System.out.println("첫 페이지입니다.\n");
		                } else if ("q".equals(cmd)) {
		                    break; // 페이징 종료 → 날짜 입력 메뉴로 돌아감
		                }
		            }
		            printLineln(MAGENTA, "📢 조회 완료되었습니다.");
			}
		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 작업을 취소하였습니다.");
	    } catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void updateVacationApproveInfo() {
		printTitle("🗓️  관리자 - 휴가 승인 관리 ");

		String input;
		int vacationSeq;

		try {
			// 1. 미승인 휴가 목록 조회 및 출력
			List<VacationDTO> list = attDao.listVaction();
			
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
			for (VacationDTO dto : list) {
				System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t | %s\t\n",
						PrintUtil.padCenter(Integer.toString(dto.getVacationSeq()), 12),
						PrintUtil.padCenter(dto.getEmpNo(), 8), PrintUtil.padCenter(dto.getStartDt(), 12),
						PrintUtil.padCenter(dto.getEndDt(), 12),
						PrintUtil.padCenter(dto.getVacationMemo() != null && dto.getVacationMemo().length() > 18
								? dto.getVacationMemo().substring(0, 15) + "..."
								: dto.getVacationMemo(), 8),
						PrintUtil.padCenter(dto.getApproverYn(), 8));
			}
			PrintUtil.printLine('-', 100);

			// 2. 승인 번호 입력
			printLine(GREEN, "👉 승인하실 휴가 신청 번호를 입력하세요 (취소: Enter) : ");
			input = br.readLine();

			if (input == null || input.trim().isEmpty()) {
				printLineln(MAGENTA, "📢 취소되었습니다.");
				return;
			}

			// NumberFormatException 처리
			vacationSeq = Integer.parseInt(input.trim());

			// 3. DAO 호출 (updateVacationApprove: 프로시저 호출)
			attDao.updateVacationApprove(vacationSeq);
			msg = "\n✅ 휴가 신청 번호 " + vacationSeq + " 승인 및 연차 차감 완료.";
			printLineln(MAGENTA, msg);
		} catch (NumberFormatException e) {
			printLineln(RED, "❌ 입력 오류: 휴가 번호는 숫자로만 입력해야 합니다.");
		} catch (SQLException e) {
			// PL/SQL 프로시저에서 발생한 에러 코드 처리 (-20000 대 오류)
			if (e.getErrorCode() == 20001) {
				printLineln(RED, "❌ 승인 실패: 입력하신 번호에 해당하는 휴가 신청번호가 없거나 연차 정보가 없습니다.");
			} else if (e.getErrorCode() == 20003) {
				// 잔여 연차 부족 상세 메시지 출력
				String errorDetail = e.getMessage().substring(e.getMessage().indexOf(":") + 1).trim();
				String msg = "❌ 승인 실패: 잔여 연차가 부족합니다. (" + errorDetail + ")";
				printLineln(RED, msg);
			} else if (e.getErrorCode() == 20099) {
				printLineln(RED, "❌ 승인 실패: 시스템 오류로 승인 중 오류가 발생했습니다.");
			} else {
				String msg = "❌ DB 오류 발생 (코드: " + e.getErrorCode() + "): " + e.getMessage();
				printLineln(RED, msg);
			}
		} catch (IOException e) {
			printLineln(RED, "❌ 입출력 오류가 발생했습니다.");
		} catch (Exception e) {
			String msg = "❌ 알 수 없는 오류가 발생했습니다: " + e.getMessage();
			printLineln(RED, msg);
		}
	}
	/*
	 * // WBS의 4레벨 메뉴(3.근무시간조회) 처리를 위한 별도 메서드 private void manageWorkTimeSearch() {
	 * int ch; System.out.println("\n[관리자 - 근태관리 - 근무시간조회]"); try { do {
	 * System.out.print("1.전체조회 2.사번조회 3.상위메뉴로돌아가기 => "); ch =
	 * Integer.parseInt(br.readLine()); } while(ch < 1 || ch > 3);
	 * 
	 * switch (ch) { case 1: attDao.selectAllWorkTime(); break; // ATT_SEL_004 case
	 * 2: attDao.selectWorkTimeByEmp(0); break; // ATT_SEL_005 case 3: return; } }
	 * catch (Exception e) { e.printStackTrace(); } }
	 */

	// 사원 번호 입력받는 메소드(통합)
	protected String checkEmpNo(boolean mustExist) throws IOException, SQLException {
		String empNo;
		while (true) {
			printLine(GREEN, "👉 사원번호(ex. 00001) : ");
			empNo = br.readLine();

			// 형식검증
			if (!empNo.matches("^\\d{5}$")) {
				printLineln(MAGENTA, "📢 잘못된 형식입니다. 숫자 5자리로 입력해주세요.");
				continue;
			}

			// DB 존재여부
			boolean exists = empDao.selectByEmpNo(empNo) != null;

			if (mustExist && !exists) {
				printLineln(MAGENTA, "📢 존재하지 않는 사원번호입니다.");
				continue;
			}

			if (!mustExist && exists) {
				printLineln(MAGENTA, "📢 이미 존재하는 사원번호입니다.");
				continue;
			}
			break;
		}
		return empNo;

	}

}