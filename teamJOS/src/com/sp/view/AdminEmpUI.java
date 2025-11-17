package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.sp.dao.EmpDAO;
import com.sp.exception.UserQuitException;
import com.sp.model.CareerDTO;
import com.sp.model.DeptMoveDTO;
import com.sp.model.EmployeeDTO;
import com.sp.model.HistoryDTO;
import com.sp.model.PromotionDTO;
import com.sp.model.RetireDTO;
import com.sp.model.RewardDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;
import com.sp.view.common.DeptCommonUI;

import static com.sp.util.PrintUtil.*;

/**
 * 관리자 - 사원관리 UI 1. 정보등록 2. 정보수정 3. 부서이동 4. 진급관리 5. 정보조회 6. 재직결재 7. 경력등록 8.자격증등록
 * 9. 이력조회 10. 일괄등록(CSV) 11. 상위메뉴
 */
public class AdminEmpUI {

	private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private final EmpDAO empDao;
	private final DeptCommonUI deptCommonUI;

	public AdminEmpUI(EmpDAO empDao, LoginInfo loginInfo) {
		this.empDao = empDao;
		this.deptCommonUI = new DeptCommonUI(loginInfo);
	}

	/** 메인 메뉴 */
	public void menu() {
		while (true) {
			try {
				printTitle("🏢 [관리자 - 사원관리]");
				printMenu(YELLOW, "① 정보 등록", "② 정보 수정", "③ 부서 이동", "④ 진급 관리", "⑤ 정보 조회", 
						"⑥ 재직 결재", "⑦ 경력 등록", "⑧ 자격증 등록", "⑨ 이력 조회", "⑩ 일괄 등록" );

				String s = br.readLine();
				InputValidator.isUserExit(s);
				int ch = Integer.parseInt(s);

				System.out.println();

				switch (ch) {
				case 1 -> insertEmployeeInfo();
				case 2 -> updateEmployeeInfo();
				case 3 -> updateDeptMoveInfo();
				case 4 -> updatePromotionInfo();
				case 5 -> manageEmployeeSearch();
				case 6 -> updateRetireApprovalInfo();
				case 7 -> insertCareerInfo();
				case 8 -> insertLicenseInfo();
				case 9 -> selectHistoryInfo();
				case 10 -> loadEmployeeInfo();
				default -> System.out.println("잘못된 번호입니다. 1~10 사이의 값을 입력해주세요.");
				}
			} catch (UserQuitException e) {
				printLineln(MAGENTA, "📢 사원관리 메뉴를 종료합니다.");
				return;
			} catch (NumberFormatException e) {
				printLineln(MAGENTA, "📢 숫자만 입력해주세요.");
			} catch (Exception e) {
				e.printStackTrace();
				printLineln(MAGENTA, "📢 처리 중 오류가 발생했습니다.");
			}
		}
	}

	/** 1. 사원관리 - 사원 정보 등록 */
	protected void insertEmployeeInfo() {
		printTitle("🏢 [관리자  -  사원관리  -  정보등록]");
		EmployeeDTO dto = new EmployeeDTO();

		try {
			// ==================== 사원번호 ====================
			while (true) {
				printLine(GREEN, "👉 사원번호 (ex.00001) [q:돌아가기] : ");
				String empNo = br.readLine();
				InputValidator.isUserExit(empNo);

				if (!InputValidator.isValidEmpNo(empNo)) {
					printLineln(MAGENTA, "📢 형식 오류 : 영문 / 숫자 조합 5자리로 입력해주세요.\n");
					continue;
				}
				if (empDao.selectByEmpNo(empNo) != null) {
					printLineln(MAGENTA, "📢 이미 존재하는 사원번호입니다.\n");
					continue;
				}
				dto.setEmpNo(empNo);
				break;
			}
			System.out.println();

			// ==================== 이름 ====================
			printLine(GREEN, "👉 이름 [q:돌아가기] : ");
			String name = br.readLine();
			InputValidator.isUserExit(name);
			dto.setEmpNm(name);
			System.out.println();

			// ==================== 주민등록번호 ====================
			while (true) {
				printLine(GREEN, "👉 주민번호('-' 제외 13자리, ex.0101013456789) [ q : 돌아가기 ] : ");
				String rrn = br.readLine();
				InputValidator.isUserExit(rrn);

				if (!InputValidator.isValidRRN(rrn)) {
					printLineln(MAGENTA, "📢 형식 오류 : 숫자 13자리로 입력해주세요.\n");
					continue;
				}
				dto.setRrn(rrn);
				break;
			}
			System.out.println();

			// ==================== 주소 ====================
			printLine(GREEN, "👉 주소 [q:돌아가기] : ");
			String addr = br.readLine();
			InputValidator.isUserExit(addr);
			dto.setEmpAddr(addr);
			System.out.println();

			// ==================== 부서 코드 ====================
			String deptCd;
			while (true) {
				deptCommonUI.selectAllDept();
				printLine(GREEN, "👉 부서코드 입력 [q:돌아가기] : ");
				deptCd = br.readLine();
				InputValidator.isUserExit(deptCd);

				if (!empDao.isValidDeptCd(deptCd)) {
					printLineln(MAGENTA, "📢 존재하지 않는 부서 코드입니다.\n");
					continue;
				}
				dto.setDeptCd(deptCd);
				break;
			}
			System.out.println();

			// ==================== 직급 코드 ====================
			String gradeCd;
			while (true) {
				printTitle("직급 코드");
				printLineln(YELLOW, "📑 01.사원  02.대리  03.과장  04.차장  05.부장  06.이사  07.대표이사");
				printLine(GREEN, "👉 직급코드 입력 [q:돌아가기] : ");
				gradeCd = br.readLine();
				InputValidator.isUserExit(gradeCd);

				if (!empDao.isValidGradeCd(gradeCd)) {
					printLineln(MAGENTA, "📢 존재하지 않는 직급 코드입니다.\n");
					continue;
				}
				dto.setGradeCd(gradeCd);
				break;
			}
			System.out.println();

			// ==================== 사원 상태 기본값 ====================
			dto.setEmpStatCd("A");
			printTitle("사원 상태");
			printLine(MAGENTA, "신규 등록 사원은 기본적으로 재직 상태(A)로 설정됩니다.\n");
			printLine(MAGENTA, "계속 진행하려면 엔터를 눌러주세요. [Enter] ");
			br.readLine();
			System.out.println();

			// ==================== 계약구분 코드 ====================
			String contractCd;
			while (true) {
				printTitle("계약구분 코드");
				printLineln(YELLOW, "📑 1. 정규직   2. 계약직   3. 인턴");
				printLine(GREEN, "👉 계약구분코드 입력 [q:돌아가기] : ");
				contractCd = br.readLine();
				InputValidator.isUserExit(contractCd);

				if (!contractCd.matches("[123]")) {
					printLineln(MAGENTA, "📢 입력 오류 : 1~3 중 하나를 선택해주세요.\n");
					continue;
				}
				dto.setContractTpCd(contractCd);
				break;
			}
			System.out.println();

			// ==================== 이메일 ====================
			while (true) {
				printLine(GREEN, "👉 이메일 [q:돌아가기] : ");
				String email = br.readLine();
				InputValidator.isUserExit(email);

				if (!InputValidator.isValidEmail(email)) {
					printLineln(MAGENTA, "📢 형식 오류: example@jos.com 형태로 입력해주세요.\n");
					continue;
				}
				if (empDao.isEmailExists(email)) {
					printLineln(MAGENTA, "📢 이미 등록된 이메일입니다. 다른 이메일을 입력해주세요.\n");
					continue;
				}
				dto.setEmail(email);
				break;
			}
			System.out.println();

			// ==================== 비밀번호 ====================
			while (true) {
				printLine(GREEN, "👉 비밀번호 [q:돌아가기 ] : ");
				String pwd = br.readLine();
				InputValidator.isUserExit(pwd);

				if (!InputValidator.isNotEmpty(pwd)) {
					printLineln(MAGENTA, "📢 비밀번호는 필수 입력값입니다.\n");
					continue;
				}
				dto.setPwd(pwd);
				break;
			}
			System.out.println();

			// ==================== 권한 레벨 ====================
			String levelCode;
			while (true) {
				printTitle("권한 레벨 코드");
				printLineln(YELLOW, "📑 01.일반사원 03.인사담당자");
				printLine(GREEN, "👉 레벨코드 입력 [q:돌아가기] : ");
				levelCode = br.readLine();
				InputValidator.isUserExit(levelCode);

				if (!levelCode.matches("0(1|3)")) {
					printLineln(MAGENTA, "📢 입력 오류 : 01, 03 중 입력해주세요.\n");
					continue;
				}
				dto.setLevelCode(levelCode);
				break;
			}

			// ==================== DB 등록 ====================
			int result = empDao.insertEmployee(dto);
			System.out.println();
			if (result > 0) {
				PrintUtil.printSection("등록 완료");
				printLineln(MAGENTA, "📢 사원 정보 등록이 성공적으로 완료되었습니다.");
			} else {
				PrintUtil.printSection("등록 실패");
				printLineln(MAGENTA, "📢 사원 정보 등록에 실패했습니다.");
			}

		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 입력을 취소하고 상위 메뉴로 돌아갑니다.");
		} catch (Exception e) {
			e.printStackTrace();
			printLineln(MAGENTA, "📢 처리 중 오류가 발생했습니다.");
		}
	}

	/** 2. 사원관리 - 정보 수정 */
	protected void updateEmployeeInfo() {
		printTitle("🏢 [관리자  -  사원관리  -  정보수정]");
		try {
			String empNo = checkEmpNo(true);

			printTitle(" 📌 수정 항목 선택 ");
			printMenu(YELLOW, "① 이름 ", "② 주소", "③ 이메일", "④ 비밀번호", "⑤ 권한 레벨");

			String sel = br.readLine();
			InputValidator.isUserExit(sel);
			int ch = Integer.parseInt(sel);
			if (ch == 6) {
				return;
			}

			String col = switch (ch) {
			case 1 -> "EMP_NM";
			case 2 -> "EMP_ADDR";
			case 3 -> "EMAIL";
			case 4 -> "PWD";
			case 5 -> "LEVEL_CODE";
			default -> null;
			};

			if (col == null) {
				printLineln(MAGENTA, "📢 잘못된 번호입니다.");
				return;
			}

			printLine(GREEN, "👉 변경할 값 입력 [q: 돌아가기] : ");
			String val = br.readLine();
			InputValidator.isUserExit(val);

			empDao.updateEmployee(empNo, col, val);
			printLineln(MAGENTA, "📢 수정이 완료되었습니다.");

		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 수정을 취소하고 상위 메뉴로 돌아갑니다.");
		} catch (NumberFormatException e) {
			printLineln(MAGENTA, "📢 숫자만 입력해주세요.");
		} catch (Exception e) {
			e.printStackTrace();
			printLineln(MAGENTA, "📢 처리 중 오류가 발생했습니다.");
		}
	}

	/** 3. 사원관리 - 부서이동 */
	private void updateDeptMoveInfo() {
		PrintUtil.printTitle("관리자  -  사원관리  -  부서이동");
		try {
			String empNo = checkEmpNo(true);

			// 현재 부서 정보
			EmployeeDTO emp = empDao.selectByEmpNo(empNo);
			EmployeeDTO deptInfo = empDao.selectDeptName(empNo);

			PrintUtil.printSection("현재 부서 정보");
			System.out.printf("사원명 : %s%n", emp != null ? emp.getEmpNm() : "");
			System.out.printf("현재 부서코드 : %s%n", deptInfo != null ? deptInfo.getDeptCd() : "");
			System.out.printf("현재 부서명 : %s%n", deptInfo != null ? deptInfo.getDeptNm() : "");
			PrintUtil.printLine('-', 70);

			// 이동할 부서 선택
			PrintUtil.printSection("이동할 부서 선택");
			deptCommonUI.selectAllDept();

			String newDeptCd;
			while (true) {
				printLine(GREEN, "👉 이동할 부서코드 [q: 돌아가기] : ");
				newDeptCd = br.readLine();
				InputValidator.isUserExit(newDeptCd);

				if (!empDao.isValidDeptCd(newDeptCd)) {
					printLineln(MAGENTA, "📢 존재하지 않는 부서 코드입니다.");
					continue;
				}
				if (deptInfo != null && newDeptCd.equals(deptInfo.getDeptCd())) {
					printLineln(MAGENTA, "📢 현재 부서와 동일한 코드입니다.");
					continue;
				}
				break;
			}

			DeptMoveDTO dto = new DeptMoveDTO();
			dto.setEmpNo(empNo);
			dto.setNewDeptCd(newDeptCd);
			empDao.updateDeptMove(dto);

			PrintUtil.printSection("이동 완료");
			printLineln(MAGENTA, "📢 부서 이동이 성공적으로 처리되었습니다.\n");

		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 부서 이동을 취소하고 상위 메뉴로 돌아갑니다.\n");
		} catch (Exception e) {
			e.printStackTrace();
			printLineln(MAGENTA, "📢 처리 중 오류가 발생했습니다.\n");
		}
	}

	/** 4. 사원관리 - 진급관리 */
	private void updatePromotionInfo() {
		PrintUtil.printTitle("관리자  -  사원관리  -  진급관리");
		try {
			String empNo = checkEmpNo(true);

			EmployeeDTO emp = empDao.selectByEmpNo(empNo);

			PrintUtil.printSection("현재 정보");
			System.out.printf("사원명 : %s%n", emp != null ? emp.getEmpNm() : "");
			System.out.printf("현재 직급 : %s%n", emp != null ? emp.getGradeNm() : "");
			System.out.printf("현재 부서명 : %s%n", emp != null ? emp.getDeptNm() : "");
			PrintUtil.printLine('-', 70);

			// 직급 목록 출력
			printTitle(" 📑 직급 코드 목록 ");
			printLineln(YELLOW, "📑 01.사원  02.대리  03.과장  04.차장  05.부장  06.이사  07.대표이사");

			// 진급 직급 입력
			String newGrade;
			while (true) {
				printLine(GREEN, "👉 진급할 직급코드 [q: 돌아가기] : ");
				newGrade = br.readLine();
				InputValidator.isUserExit(newGrade);

				if (!empDao.isValidGradeCd(newGrade)) {
					printLineln(MAGENTA, "📢 존재하지 않는 직급 코드입니다.\n");
					continue;
				}
				if (emp != null && newGrade.equals(emp.getGradeCd())) {
					printLineln(MAGENTA, "📢 현재 직급과 동일합니다. 다른 직급을 선택해주세요.\n");
					continue;
				}
				break;
			}

			// 진급 사유
			String reason;
			while (true) {
				printLine(GREEN, "👉 진급 사유 [q: 돌아가기] : ");
				reason = br.readLine();
				InputValidator.isUserExit(reason);

				if (!InputValidator.isNotEmpty(reason)) {
					printLineln(MAGENTA, "📢 진급 사유는 반드시 입력해야 합니다.\n");
					continue;
				}
				break;
			}

			PromotionDTO dto = new PromotionDTO();
			dto.setEmpNo(empNo);
			dto.setCurrentGradeCd(emp != null ? emp.getGradeCd() : null);
			dto.setNewGradeCd(newGrade);
			dto.setDetails(reason);

			int result = empDao.updatePromotion(dto);

			if (result > 0) {
				printLineln(MAGENTA, "📢 진급 처리가 완료되었습니다.\n");
			} else {
				printLineln(MAGENTA, "📢 진급 처리에 실패하였습니다.\n");
			}

		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 진급관리를 취소하고 상위 메뉴로 돌아갑니다.\n");
		} catch (SQLException e) {
			printLineln(MAGENTA, "📢 데이터베이스 처리 중 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (Exception e) {
			printLineln(MAGENTA, "📢 예상치 못한 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	/** 5. 사원관리 - 정보조회 */
	private void manageEmployeeSearch() {
		printTitle("🏢 [관리자 - 사원관리 - 정보조회]");
	    try {
	        while (true) {
	        	printMenu(YELLOW, "① 사번 조회", "② 이름 조회", "③ 전체 조회");
	            String sel = br.readLine();
	            if (sel == null) sel = "";
	            sel = sel.trim();
	            if ("q".equalsIgnoreCase(sel)) {
	                return;
	            }

	            int ch;
	            try {
	                ch = Integer.parseInt(sel);
	            } catch (NumberFormatException e) {
	            	printLineln(MAGENTA, "📢 잘못된 번호입니다. 1~3 사이의 값을 입력해주세요.\n");
	                continue;
	            }

	            switch (ch) {
	            // 1. 사번 조회 - 단건도 목록이랑 같은 형식 사용
	            case 1 -> {
	                String empNo = checkEmpNo(true);
	                EmployeeDTO dto = empDao.selectByEmpNo(empNo);
	                if (dto == null) {
	                	printLineln(MAGENTA, "📢 해당 사원번호의 정보가 존재하지 않습니다.\n");
	                    break;
	                }

	                String regDt = dto.getRegDt() == null ? "-" : dto.getRegDt();
	                String level = dto.getLevelCode() == null ? "-" : dto.getLevelCode();
	                String addr  = getFirstTwoWords(dto.getEmpAddr()); // 주소는 앞 두 단어만

	                PrintUtil.printLine('=', 150);
	                System.out.println(PrintUtil.padCenter("관리자 - 사원관리 - 사원 정보 조회", 150));
	                PrintUtil.printLine('=', 150);

	                // ───── 헤더 (한글 폭 기준 정렬) ─────
	                System.out.printf(
	                        "%s\t| %s\t| %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s%n",
	                        PrintUtil.padCenterDisplay("사번", 6),
	                        PrintUtil.padCenterDisplay("이름", 8),
	                        PrintUtil.padCenterDisplay("주민번호", 16),
	                        PrintUtil.padCenterDisplay("주소", 22),
	                        PrintUtil.padCenterDisplay("입사일", 10),
	                        PrintUtil.padCenterDisplay("부서명", 10),
	                        PrintUtil.padCenterDisplay("직급", 8),
	                        PrintUtil.padCenterDisplay("재직", 4),
	                        PrintUtil.padCenterDisplay("계약", 4),
	                        PrintUtil.padCenterDisplay("이메일", 16),
	                        PrintUtil.padCenterDisplay("비밀번호", 8),
	                        PrintUtil.padCenterDisplay("등록일", 10),
	                        PrintUtil.padCenterDisplay("권한", 8)
	                );
	                PrintUtil.printLine('-', 150);

	                // ───── 데이터 1행 (목록이랑 동일 포맷) ─────
	                System.out.printf(
	                        "%s\t| %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s%n",
	                        PrintUtil.padRightDisplay(dto.getEmpNo(), 6),
	                        PrintUtil.padRightDisplay(dto.getEmpNm(), 8),
	                        PrintUtil.padRightDisplay(dto.getRrn(), 12),
	                        PrintUtil.padRightDisplay(addr, 24),
	                        PrintUtil.padRightDisplay(dto.getHireDt(), 10),
	                        PrintUtil.padRightDisplay(dto.getDeptNm(), 10),
	                        PrintUtil.padRightDisplay(dto.getGradeNm(), 8),
	                        PrintUtil.padRightDisplay(dto.getEmpStatNm(), 6),
	                        PrintUtil.padRightDisplay(dto.getContractTpNm(), 4),
	                        PrintUtil.padRightDisplay(dto.getEmail(), 16),
	                        PrintUtil.padRightDisplay(dto.getPwd(), 8),
	                        PrintUtil.padRightDisplay(regDt, 10),
	                        PrintUtil.padRightDisplay(level, 8)
	                );

	                PrintUtil.printLine('=', 150);
	                System.out.println();
	            }

	            case 2 -> {
	            	printLine(GREEN, "👉 조회할 이름 [q: 돌아가기] : ");
	                String name = br.readLine();
	                InputValidator.isUserExit(name);

	                List<EmployeeDTO> list = empDao.selectByName(name);
	                printEmployeeListPaged(list);
	            }

	            case 3 -> {
	                List<EmployeeDTO> list = empDao.selectAll();
	                printEmployeeListPaged(list);
	            }

	            default -> printLineln(MAGENTA, "📢 잘못된 번호입니다. 1~3 사이의 값을 입력해주세요.\n");
	            }
	        }
	    } catch (UserQuitException e) {
	    	printLineln(MAGENTA, "📢 정보 조회를 취소했습니다.\n");
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	/** 6. 사원관리 - 재직결재 */
	protected void updateRetireApprovalInfo() {
		printTitle("🏢 [관리자 - 사원관리 - 퇴직결재]");

		final String RESET = "\u001B[0m";
		final String GREEN = "\u001B[32m";
		final String YELLOW = "\u001B[33m";
		final String CYAN = "\u001B[36m";
		final String GRAY = "\u001B[90m";

		System.out.println(CYAN + "\n╔════════════════════════════════════════╗" + RESET);
		System.out.println(CYAN + "║       🗓️  관리자 - 퇴직 승인 관리            ║" + RESET);
		System.out.println(CYAN + "╚════════════════════════════════════════╝" + RESET);

		String input;
		int retireSeq;

		try {
			List<RetireDTO> list = empDao.listRetire();

			PrintUtil.printLine('─', 64);
			System.out.println(YELLOW + " 미승인 퇴직 신청 (총 " + list.size() + "건)" + RESET);
			PrintUtil.printLine('─', 64);
			// 헤더 출력
			System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t\n", 
					PrintUtil.padCenter("번호", 8),
					PrintUtil.padCenter("사번", 8),
					PrintUtil.padCenter("퇴직일", 12), PrintUtil.padCenter("신청사유", 8),
					PrintUtil.padCenter("승인상태", 8));

			PrintUtil.printLine('-', 64);

			if (list.isEmpty()) {
				printLineln(MAGENTA, "📢 현재 미승인된 퇴직 신청이 없습니다.");
				PrintUtil.printLine('-', 64);
				return;
			}

			for (RetireDTO dto : list) {
				System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t\n",
						PrintUtil.padCenter(Integer.toString(dto.getRetireSeq()), 8),
						PrintUtil.padCenter(dto.getEmpNo(), 8), PrintUtil.padCenter(dto.getRegDt(), 12),
						PrintUtil.padCenter(dto.getRetireMemo() != null && dto.getRetireMemo().length() > 18
								? dto.getRetireMemo().substring(0, 15) + "..."
								: dto.getRetireMemo(), 8),
						PrintUtil.padCenter(dto.getApproverYn(), 8));
			}
			PrintUtil.printLine('-', 64);

			printLine(GREEN, "👉  승인하실 퇴직 신청 번호를 입력하세요 (취소: Enter) : ");
			input = br.readLine();

			if (input == null || input.trim().isEmpty()) {
				printLineln(MAGENTA, "📢 취소되었습니다.");
				return;
			}

			retireSeq = Integer.parseInt(input.trim());

			empDao.updateRetireApproval(retireSeq);

			System.out.println(GREEN + "\n✅ 퇴직 신청 번호 " + retireSeq + " 승인 완료." + RESET);

		} catch (Exception e) {
		}
	}

	/** 7. 사원관리 - 경력등록 */
	protected void insertCareerInfo() {
		printTitle("🏢 [관리자 - 사원관리 - 경력등록]");
		try {
			String empNo = checkEmpNo(true);
			CareerDTO dto = new CareerDTO();
			dto.setEmpNo(empNo);

			printLine(GREEN, "👉 회사명([q: 돌아가기]) : ");
			String comp = br.readLine();
			InputValidator.isUserExit(comp);
			dto.setCompanyName(comp);

			printLine(GREEN, "👉 근무시작일(YYYY-MM-DD, [q: 돌아가기]) : ");
			String start = br.readLine();
			InputValidator.isUserExit(start);
			dto.setStartDt(start);

			printLine(GREEN, "👉 근무종료일(YYYY-MM-DD, [q: 돌아가기]) : ");
			String end = br.readLine();
			InputValidator.isUserExit(end);
			dto.setEndDt(end);

			printLine(GREEN, "👉 상세([q: 돌아가기]) : ");
			String det = br.readLine();
			InputValidator.isUserExit(det);
			dto.setDetails(det);

			empDao.insertCareer(dto);
			PrintUtil.printSection("등록 완료");
			printLineln(MAGENTA, "📢 경력 등록이 완료되었습니다.");

		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 등록이 취소되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 8. 사원관리 - 자격증등록 */
	protected void insertLicenseInfo() {
		printTitle("🏢 [관리자  -  사원관리  -  자격증등록]");
		try {
			String empNo = checkEmpNo(true);
			RewardDTO dto = new RewardDTO();
			dto.setEmpNo(empNo);

			printLine(GREEN, "👉 자격증명([q: 돌아가기]) ▶ ");
			String name = br.readLine();
			InputValidator.isUserExit(name);
			dto.setRewardName(name);

			printLine(GREEN, "👉 발급기관([q: 돌아가기]) ▶ ");
			String org = br.readLine();
			InputValidator.isUserExit(org);
			dto.setIssuer(org);

			printLine(GREEN, "👉 취득일(YYYY-MM-DD, [q: 돌아가기]) ▶ ");
			String date = br.readLine();
			InputValidator.isUserExit(date);
			dto.setDate(date);

			empDao.insertLicense(dto);
			PrintUtil.printSection("등록 완료");
			printLineln(MAGENTA, "📢 자격증 등록이 완료되었습니다.\n");

		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 등록이 취소되었습니다.\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 9. 사원관리 - 이력조회 */
	protected void selectHistoryInfo() {
		printTitle("🏢 [관리자 - 사원관리 - 이력조회]");
		try {
			while (true) {
				System.out.print("1. 경력 | 2. 자격증 | 3. 직급이력 | [q: 돌아가기] ➤ ");
				String sel = br.readLine();
				if (sel == null)
					sel = "";
				sel = sel.trim();
				if ("q".equalsIgnoreCase(sel)) {
					return;
				}

				int ch;
				try {
					ch = Integer.parseInt(sel);
				} catch (NumberFormatException e) {
					printLineln(MAGENTA, "📢 잘못된 번호입니다.\n");
					continue;
				}

				List<HistoryDTO> list;

				switch (ch) {
				case 1 -> {
					list = empDao.selectCareerHisAll();
					printCareerHistoryPaged(list);
				}
				case 2 -> {
					list = empDao.selectCertHisAll();
					printCertHistoryPaged(list);
				}
				case 3 -> {
					list = empDao.selectGradeHisAll();
					printGradeHistoryPaged(list);
				}
				case 4 -> {
					return;
				}
				default -> printLineln(MAGENTA, "📢 잘못된 번호입니다.\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 10. 사원관리 - CSV 일괄등록 */
	protected void loadEmployeeInfo() {
		PrintUtil.printSection("CSV 파일 로드");
		empDao.loadEmployeeInfo();
		PrintUtil.printSection("로드 완료");
		System.out.println();
	}

	/** 사원번호 입력 공통 메소드 */
	protected String checkEmpNo(boolean mustExist) throws IOException, SQLException, UserQuitException {
		while (true) {
			printLine(GREEN, "👉 사원번호(ex.00001) [q: 돌아가기] : ");
			String empNo = br.readLine();
			InputValidator.isUserExit(empNo);

			if (!InputValidator.isValidEmpNo(empNo)) {
				printLineln(MAGENTA, "📢 잘못된 형식입니다. 영문/숫자 조합 5자리로 입력해주세요.");
				continue;
			}
			boolean exists = empDao.selectByEmpNo(empNo) != null;

			if (mustExist && !exists) {
				printLineln(MAGENTA, "📢 존재하지 않는 사원번호입니다.");
				continue;
			}
			if (!mustExist && exists) {
				printLineln(MAGENTA, "📢 이미 존재하는 사원번호입니다.");
				continue;
			}
			return empNo;
		}
	}

	// ==================== 공통 : 사원 목록 페이징 ====================
	private void printEmployeeListPaged(List<EmployeeDTO> list) throws IOException {
		if (list == null || list.isEmpty()) {
			printLineln(MAGENTA, "📢 조회 결과가 없습니다.\n");
			return;
		}

		final int pageSize = 15; // 한 페이지에 15명
		int total = list.size();
		int totalPage = (total + pageSize - 1) / pageSize;
		int page = 1;

		while (true) {
			int startIndex = (page - 1) * pageSize;
			int endIndex = Math.min(startIndex + pageSize, total);

			System.out.println();
			System.out.printf("▶ 사원 정보 목록 | 페이지 %d / %d | 총 %d건 | 조회범위: %d~%d%n", page, totalPage, total,
					startIndex + 1, endIndex);
			PrintUtil.printLine('=', 150);

			// ───── 헤더 (한글 폭 기준 정렬) ─────
			System.out.printf("%s\t| %s\t| %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s%n",
					PrintUtil.padCenterDisplay("사번", 6),
					PrintUtil.padCenterDisplay("이름",8),
					PrintUtil.padCenterDisplay("주민번호", 16),
					PrintUtil.padCenterDisplay("주소", 22),
					PrintUtil.padCenterDisplay("입사일", 10),
					PrintUtil.padCenterDisplay("부서명", 10),
					PrintUtil.padCenterDisplay("직급", 8),
					PrintUtil.padCenterDisplay("재직", 4),
					PrintUtil.padCenterDisplay("계약", 4),
					PrintUtil.padCenterDisplay("이메일", 16));
			PrintUtil.printLine('-', 150);

			// ───── 데이터 행 ─────
			for (int i = startIndex; i < endIndex; i++) {
				EmployeeDTO d = list.get(i);

				String empNo = d.getEmpNo();
				String empNm = d.getEmpNm();
				String rrn = d.getRrn();
				// ★ 주소는 "앞의 두 단어"만 사용
				String addr = getFirstTwoWords(d.getEmpAddr());
				String hireDt = d.getHireDt();
				String deptNm = d.getDeptNm();
				String gradeNm = d.getGradeNm();
				String statNm = d.getEmpStatNm();
				String cntrNm = d.getContractTpNm();
				String email = d.getEmail();

				System.out.printf("%s\t| %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s%n",
						PrintUtil.padRightDisplay(empNo, 6),
						PrintUtil.padRightDisplay(empNm, 8),
						PrintUtil.padRightDisplay(rrn, 12),
						PrintUtil.padRightDisplay(addr, 24),
						PrintUtil.padRightDisplay(hireDt, 10),
						PrintUtil.padRightDisplay(deptNm, 10),
						PrintUtil.padRightDisplay(gradeNm, 8),
						PrintUtil.padRightDisplay(statNm, 6),
						PrintUtil.padRightDisplay(cntrNm, 4),
						PrintUtil.padRightDisplay(email, 16));
			}

			PrintUtil.printLine('=', 150);
			printLine(GREEN, "[n: 다음, p: 이전, q: 종료] 👉 ");
			String cmd = br.readLine();
			if (cmd == null)
				cmd = "";
			cmd = cmd.trim().toLowerCase();

			if ("n".equals(cmd)) {
				if (page < totalPage)
					page++;
				else
					printLineln(MAGENTA, "📢 마지막 페이지입니다.\n");
			} else if ("p".equals(cmd)) {
				if (page > 1)
					page--;
				else
					printLineln(MAGENTA, "📢 첫 페이지입니다.\n");
			} else if ("q".equals(cmd)) {
				break;
			}
		}
	}

	// 주소에서 앞의 두 단어(시/도 + 시/군/구)만 반환
	private String getFirstTwoWords(String addr) {
		if (addr == null)
			return "";
		String[] parts = addr.trim().split("\\s+");
		if (parts.length >= 2) {
			return parts[0] + " " + parts[1];
		} else {
			return addr.trim();
		}
	}



	// ==================== 공통 : 경력 이력 페이징 ====================
	private void printCareerHistoryPaged(List<HistoryDTO> list) throws IOException {
		if (list == null || list.isEmpty()) {
			printLineln(MAGENTA, "📢 등록된 경력 이력이 없습니다.\n");
			return;
		}

		final int pageSize = 10;
		int total = list.size();
		int totalPage = (total + pageSize - 1) / pageSize;
		int page = 1;

		while (true) {
			int startIndex = (page - 1) * pageSize;
			int endIndex = Math.min(startIndex + pageSize, total);

			System.out.println();
			System.out.printf("▶ 경력 이력 목록 | 페이지 %d / %d | 총 %d건 | 조회범위: %d~%d%n", page, totalPage, total,
					startIndex + 1, endIndex);
			PrintUtil.printLine('=', 120);

			System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t | %s%n", 
					PrintUtil.padCenter("사번", 6),
					PrintUtil.padCenter("이름", 8), 
					PrintUtil.padCenter("회사명", 20), 
					PrintUtil.padCenter("시작일", 10),
					PrintUtil.padCenter("종료일", 10), 
					PrintUtil.padCenter("상세", 30));
			PrintUtil.printLine('-', 120);

			for (int i = startIndex; i < endIndex; i++) {
				HistoryDTO d = list.get(i);

				System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t | %s%n", 
						PrintUtil.padRight(d.getEmpNo(), 6),
						PrintUtil.padRight(d.getEmpNm(), 8), 
						PrintUtil.padRight(d.getPrevCompNm(), 20),
						PrintUtil.padRight(d.getStartDt(), 10),
						PrintUtil.padRight(d.getEndDt(), 10),
						PrintUtil.padRight(d.getDetails(), 30));
			}
			PrintUtil.printLine('=', 120);
			printLine(GREEN, "[n: 다음, p: 이전, q: 종료] 👉 ");
			String cmd = br.readLine();
			if (cmd == null)
				cmd = "";
			cmd = cmd.trim().toLowerCase();

			if ("n".equals(cmd)) {
				if (page < totalPage)
					page++;
				else
					printLineln(MAGENTA, "📢 마지막 페이지입니다.\n");
			} else if ("p".equals(cmd)) {
				if (page > 1)
					page--;
				else
					printLineln(MAGENTA, "📢 첫 페이지입니다.\n");
			} else if ("q".equals(cmd)) {
				break;
			}
		}
	}

	// ==================== 공통 : 자격증 이력 페이징 ====================
	private void printCertHistoryPaged(List<HistoryDTO> list) throws IOException {
		if (list == null || list.isEmpty()) {
			printLineln(MAGENTA, "📢 등록된 자격증 이력이 없습니다.\n");
			return;
		}

		final int pageSize = 10;
		int total = list.size();
		int totalPage = (total + pageSize - 1) / pageSize;
		int page = 1;

		while (true) {
			int startIndex = (page - 1) * pageSize;
			int endIndex = Math.min(startIndex + pageSize, total);

			System.out.println();
			System.out.printf("▶ 자격증 이력 목록 | 페이지 %d / %d | 총 %d건 | 조회범위: %d~%d%n", page, totalPage, total,
					startIndex + 1, endIndex);
			PrintUtil.printLine('=', 120);

			System.out.printf("%s\t | %s\t | %s\t | %s\t | %s%n", 
					PrintUtil.padCenter("사번", 6), 
					PrintUtil.padCenter("이름", 8),
					PrintUtil.padCenter("자격증명", 20),
					PrintUtil.padCenter("발급기관", 20), 
					PrintUtil.padCenter("발급일", 10));
			PrintUtil.printLine('-', 120);

			for (int i = startIndex; i < endIndex; i++) {
				HistoryDTO d = list.get(i);

				System.out.printf("%s\t | %s\t | %s\t | %s\t | %s%n",
						PrintUtil.padRight(d.getEmpNo(), 6),
						PrintUtil.padRight(d.getEmpNm(), 8), 
						PrintUtil.padRight(d.getCertNm(), 20),
						PrintUtil.padRight(d.getIssueOrgNm(), 20), 
						PrintUtil.padRight(d.getIssueDt(), 10));
			}
			PrintUtil.printLine('=', 120);
			printLine(GREEN, "[n: 다음, p: 이전, q: 종료] 👉 ");
			String cmd = br.readLine();
			if (cmd == null)
				cmd = "";
			cmd = cmd.trim().toLowerCase();

			if ("n".equals(cmd)) {
				if (page < totalPage)
					page++;
				else
					printLineln(MAGENTA, "📢 마지막 페이지입니다.\n");
			} else if ("p".equals(cmd)) {
				if (page > 1)
					page--;
				else
					printLineln(MAGENTA, "📢 첫 페이지입니다.");
			} else if ("q".equals(cmd)) {
				break;
			}
		}
	}

	// ==================== 공통 : 직급 이력 페이징 ====================
	private void printGradeHistoryPaged(List<HistoryDTO> list) throws IOException {
		if (list == null || list.isEmpty()) {
			printLineln(MAGENTA, "📢 등록된 직급 이력이 없습니다.");
			return;
		}

		final int pageSize = 10;
		int total = list.size();
		int totalPage = (total + pageSize - 1) / pageSize;
		int page = 1;

		while (true) {
			int startIndex = (page - 1) * pageSize;
			int endIndex = Math.min(startIndex + pageSize, total);

			System.out.println();
			System.out.printf("▶ 직급 이력 목록 | 페이지 %d / %d | 총 %d건 | 조회범위: %d~%d%n", page, totalPage, total,
					startIndex + 1, endIndex);
			PrintUtil.printLine('=', 120);

			System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t | %s%n", 
					PrintUtil.padCenter("시작일", 10),
					PrintUtil.padCenter("사번", 8), 
					PrintUtil.padCenter("이름", 8), 
					PrintUtil.padCenter("직급", 6),
					PrintUtil.padCenter("종료일", 10), 
					PrintUtil.padCenter("부서", 12));
			PrintUtil.printLine('-', 120);

			for (int i = startIndex; i < endIndex; i++) {
				HistoryDTO d = list.get(i);

				System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t | %s%n", 
						PrintUtil.padRight(d.getStartDt(), 10),
						PrintUtil.padRight(d.getEmpNo(), 6), 
						PrintUtil.padRight(d.getEmpNm(), 8),
						PrintUtil.padRight(d.getGradeNm(), 6), 
						PrintUtil.padRight(d.getEndDt(), 10),
						PrintUtil.padRight(d.getDeptNm(), 12));
			}
			PrintUtil.printLine('=', 120);
			printLine(GREEN, "[n: 다음, p: 이전, q: 종료] 👉 ");
			String cmd = br.readLine();
			if (cmd == null)
				cmd = "";
			cmd = cmd.trim().toLowerCase();

			if ("n".equals(cmd)) {
				if (page < totalPage)
					page++;
				else
					printLineln(MAGENTA, "📢 마지막 페이지입니다.\n");
			} else if ("p".equals(cmd)) {
				if (page > 1)
					page--;
				else
					printLineln(MAGENTA, "📢 첫 페이지입니다.\n");
			} else if ("q".equals(cmd)) {
				break;
			}

		}
	}
}
