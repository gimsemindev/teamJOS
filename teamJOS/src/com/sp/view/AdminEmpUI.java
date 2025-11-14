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

/**
 * 관리자 - 사원관리 UI
 *  1. 정보등록
 *  2. 정보수정
 *  3. 부서이동
 *  4. 진급관리
 *  5. 정보조회
 *  6. 재직결재
 *  7. 경력등록
 *  8. 자격증등록
 *  9. 이력조회
 * 10. 일괄등록(CSV)
 * 11. 상위메뉴
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
				System.out.println();
				PrintUtil.printLine('=', 70);
				System.out.println("                    관리자  -  사원관리");
				PrintUtil.printLine('=', 70);
				System.out.println(" 1. 정보등록   2. 정보수정   3. 부서이동   4. 진급관리   5. 정보조회");
				System.out.println(" 6. 재직결재   7. 경력등록   8. 자격증등록  9. 이력조회  10. 일괄등록");
				System.out.println("11. 상위메뉴");
				PrintUtil.printLine('=', 70);
				System.out.print("메뉴 선택 [q: 종료] ▶ ");

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
				case 11 -> {
					System.out.println("상위 메뉴로 돌아갑니다.\n");
					return;
				}
				default -> System.out.println("잘못된 번호입니다. 1~11 사이의 값을 입력해주세요.\n");
				}
			} catch (UserQuitException e) {
				System.out.println("\n사원관리 메뉴를 종료합니다.\n");
				return;
			} catch (NumberFormatException e) {
				System.out.println("숫자만 입력해주세요.\n");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("처리 중 오류가 발생했습니다.\n");
			}
		}
	}

	/** 1. 사원관리 - 사원 정보 등록 */
	protected void insertEmployeeInfo() {
		PrintUtil.printTitle("관리자  -  사원관리  -  정보등록");
		EmployeeDTO dto = new EmployeeDTO();

		try {
			// ==================== 사원번호 ====================
			while (true) {
				System.out.print("사원번호(ex.00001, [q: 돌아가기]) ▶ ");
				String empNo = br.readLine();
				InputValidator.isUserExit(empNo);

				if (!InputValidator.isValidEmpNo(empNo)) {
					System.out.println("형식 오류 : 영문 / 숫자 조합 5자리로 입력해주세요.\n");
					continue;
				}
				if (empDao.selectByEmpNo(empNo) != null) {
					System.out.println("이미 존재하는 사원번호입니다.\n");
					continue;
				}
				dto.setEmpNo(empNo);
				break;
			}
			System.out.println();

			// ==================== 이름 ====================
			System.out.print("이름([q: 돌아가기]) ▶ ");
			String name = br.readLine();
			InputValidator.isUserExit(name);
			dto.setEmpNm(name);
			System.out.println();

			// ==================== 주민등록번호 ====================
			while (true) {
				System.out.print("주민번호('-' 제외 13자리, ex.0101013456789, [q: 돌아가기]) ▶ ");
				String rrn = br.readLine();
				InputValidator.isUserExit(rrn);

				if (!InputValidator.isValidRRN(rrn)) {
					System.out.println("형식 오류 : 숫자 13자리로 입력해주세요.\n");
					continue;
				}
				dto.setRrn(rrn);
				break;
			}
			System.out.println();

			// ==================== 주소 ====================
			System.out.print("주소([q: 돌아가기]) ▶ ");
			String addr = br.readLine();
			InputValidator.isUserExit(addr);
			dto.setEmpAddr(addr);
			System.out.println();

			// ==================== 부서 코드 ====================
			String deptCd;
			while (true) {
				PrintUtil.printSection("부서 코드");
				deptCommonUI.selectAllDept();
				System.out.print("부서코드 입력([q: 돌아가기]) ▶ ");
				deptCd = br.readLine();
				InputValidator.isUserExit(deptCd);

				if (!empDao.isValidDeptCd(deptCd)) {
					System.out.println("존재하지 않는 부서 코드입니다.\n");
					continue;
				}
				dto.setDeptCd(deptCd);
				break;
			}
			System.out.println();

			// ==================== 직급 코드 ====================
			String gradeCd;
			while (true) {
				PrintUtil.printSection("직급 코드");
				System.out.println("01.사원  02.대리  03.과장  04.차장  05.부장  06.이사  07.대표이사");
				System.out.print("직급코드 입력([q: 돌아가기]) ▶ ");
				gradeCd = br.readLine();
				InputValidator.isUserExit(gradeCd);

				if (!empDao.isValidGradeCd(gradeCd)) {
					System.out.println("존재하지 않는 직급 코드입니다.\n");
					continue;
				}
				dto.setGradeCd(gradeCd);
				break;
			}
			System.out.println();

			// ==================== 사원 상태 기본값 ====================
			dto.setEmpStatCd("A");
			PrintUtil.printSection("사원 상태");
			System.out.println("신규 등록 사원은 기본적으로 재직 상태(A)로 설정됩니다.");
			System.out.print("계속 진행하려면 엔터를 눌러주세요. ");
			br.readLine();
			System.out.println();

			// ==================== 계약구분 코드 ====================
			String contractCd;
			while (true) {
				PrintUtil.printSection("계약구분 코드");
				System.out.println("1. 정규직   2. 계약직   3. 인턴");
				System.out.print("계약구분코드 입력([q: 돌아가기]) ▶ ");
				contractCd = br.readLine();
				InputValidator.isUserExit(contractCd);

				if (!contractCd.matches("[123]")) {
					System.out.println("입력 오류 : 1~3 중 하나를 선택해주세요.\n");
					continue;
				}
				dto.setContractTpCd(contractCd);
				break;
			}
			System.out.println();

			// ==================== 이메일 ====================
			while (true) {
				System.out.print("이메일([q: 돌아가기]) ▶ ");
				String email = br.readLine();
				InputValidator.isUserExit(email);

				if (!InputValidator.isValidEmail(email)) {
					System.out.println("형식 오류: example@jos.com 형태로 입력해주세요.\n");
					continue;
				}
				if (empDao.isEmailExists(email)) {
					System.out.println("이미 등록된 이메일입니다. 다른 이메일을 입력해주세요.\n");
					continue;
				}
				dto.setEmail(email);
				break;
			}
			System.out.println();

			// ==================== 비밀번호 ====================
			while (true) {
				System.out.print("비밀번호([q: 돌아가기]) ▶ ");
				String pwd = br.readLine();
				InputValidator.isUserExit(pwd);

				if (!InputValidator.isNotEmpty(pwd)) {
					System.out.println("비밀번호는 필수 입력값입니다.\n");
					continue;
				}
				dto.setPwd(pwd);
				break;
			}
			System.out.println();

			// ==================== 권한 레벨 ====================
			String levelCode;
			while (true) {
				PrintUtil.printSection("권한 레벨 코드");
				System.out.println("01.일반사원  02.관리자  03.인사담당자");
				System.out.print("레벨코드 입력([q: 돌아가기]) ▶ ");
				levelCode = br.readLine();
				InputValidator.isUserExit(levelCode);

				if (!levelCode.matches("0[1-3]")) {
					System.out.println("입력 오류 : 01~03 사이의 값을 입력해주세요.\n");
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
				System.out.println("사원 정보 등록이 성공적으로 완료되었습니다.\n");
			} else {
				PrintUtil.printSection("등록 실패");
				System.out.println("사원 정보 등록에 실패했습니다.\n");
			}

		} catch (UserQuitException e) {
			System.out.println("\n입력을 취소하고 상위 메뉴로 돌아갑니다.\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("처리 중 오류가 발생했습니다.\n");
		}
	}

	/** 2. 사원관리 - 정보 수정 */
	protected void updateEmployeeInfo() {
		PrintUtil.printTitle("관리자  -  사원관리  -  정보수정");
		try {
			String empNo = checkEmpNo(true);

			PrintUtil.printSection("수정 항목 선택");
			System.out.println("1. 이름  2. 주소  3. 이메일  4. 비밀번호  5. 권한레벨  6. 상위메뉴");
			System.out.print("선택([q: 돌아가기]) ▶ ");
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
				System.out.println("잘못된 번호입니다.\n");
				return;
			}

			System.out.print("변경할 값 입력([q: 돌아가기]) ▶ ");
			String val = br.readLine();
			InputValidator.isUserExit(val);

			empDao.updateEmployee(empNo, col, val);
			System.out.println("\n수정이 완료되었습니다.\n");

		} catch (UserQuitException e) {
			System.out.println("\n수정을 취소하고 상위 메뉴로 돌아갑니다.\n");
		} catch (NumberFormatException e) {
			System.out.println("숫자만 입력해주세요.\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("처리 중 오류가 발생했습니다.\n");
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
				System.out.print("이동할 부서코드([q: 돌아가기]) ▶ ");
				newDeptCd = br.readLine();
				InputValidator.isUserExit(newDeptCd);

				if (!empDao.isValidDeptCd(newDeptCd)) {
					System.out.println("존재하지 않는 부서 코드입니다.\n");
					continue;
				}
				if (deptInfo != null && newDeptCd.equals(deptInfo.getDeptCd())) {
					System.out.println("현재 부서와 동일한 코드입니다.\n");
					continue;
				}
				break;
			}

			DeptMoveDTO dto = new DeptMoveDTO();
			dto.setEmpNo(empNo);
			dto.setNewDeptCd(newDeptCd);
			empDao.updateDeptMove(dto);

			PrintUtil.printSection("이동 완료");
			System.out.println("부서 이동이 성공적으로 처리되었습니다.\n");

		} catch (UserQuitException e) {
			System.out.println("\n부서 이동을 취소하고 상위 메뉴로 돌아갑니다.\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("처리 중 오류가 발생했습니다.\n");
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
			PrintUtil.printSection("직급 코드 목록");
			System.out.println("01.사원  02.대리  03.과장  04.차장  05.부장  06.이사  07.대표이사");
			PrintUtil.printLine('-', 70);

			// 진급 직급 입력
			String newGrade;
			while (true) {
				System.out.print("진급할 직급코드([q: 돌아가기]) ▶ ");
				newGrade = br.readLine();
				InputValidator.isUserExit(newGrade);

				if (!empDao.isValidGradeCd(newGrade)) {
					System.out.println("존재하지 않는 직급 코드입니다.\n");
					continue;
				}
				if (emp != null && newGrade.equals(emp.getGradeCd())) {
					System.out.println("현재 직급과 동일합니다. 다른 직급을 선택해주세요.\n");
					continue;
				}
				break;
			}

			// 진급 사유
			String reason;
			while (true) {
				System.out.print("진급 사유([q: 돌아가기]) ▶ ");
				reason = br.readLine();
				InputValidator.isUserExit(reason);

				if (!InputValidator.isNotEmpty(reason)) {
					System.out.println("진급 사유는 반드시 입력해야 합니다.\n");
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
				System.out.println("\n진급 처리가 완료되었습니다.\n");
			} else {
				System.out.println("\n진급 처리에 실패하였습니다.\n");
			}

		} catch (UserQuitException e) {
			System.out.println("\n진급관리를 취소하고 상위 메뉴로 돌아갑니다.\n");
		} catch (SQLException e) {
			System.out.println("데이터베이스 처리 중 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("예상치 못한 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	/** 5. 사원관리 - 정보조회 */
	private void manageEmployeeSearch() {
		System.out.println("\n[관리자 - 사원관리 - 정보조회]");
		try {
			while (true) {
				System.out.print("1.사번조회 | 2.이름조회 | 3.전체조회 | [q: 돌아가기] ➤ ");
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
					System.out.println("잘못된 번호입니다. 1~4 사이의 값을 입력해주세요.\n");
					continue;
				}

				switch (ch) {
				case 1 -> {
					String empNo = checkEmpNo(true);
					EmployeeDTO dto = empDao.selectByEmpNo(empNo);
					if (dto == null) {
						System.out.println("해당 사원번호의 정보가 존재하지 않습니다.\n");
						break;
					}

					// null 값 처리
					String regDt = dto.getRegDt() == null ? "-" : dto.getRegDt();
					String retireDt = dto.getRetireDt() == null ? "-" : dto.getRetireDt();
					String level = dto.getLevelCode() == null ? "-" : dto.getLevelCode();

					String line = "============================================================";

					System.out.println(line);
					System.out.println("                    [ 단일 사원 정보 ]");
					System.out.println(line);
					System.out.println();

					System.out.println("사번: " + dto.getEmpNo());
					System.out.println("이름: " + dto.getEmpNm());
					System.out.println("주민번호: " + dto.getRrn());
					System.out.println("주소: " + dto.getEmpAddr());
					System.out.println("입사일자: " + dto.getHireDt());
					System.out.println("부서명: " + dto.getDeptNm());
					System.out.println("직급: " + dto.getGradeNm());
					System.out.println("재직상태: " + dto.getEmpStatNm());
					System.out.println("계약유형: " + dto.getContractTpNm());
					System.out.println("이메일: " + dto.getEmail());
					System.out.println("비밀번호: " + dto.getPwd());
					System.out.println("등록일: " + regDt);
					System.out.println("퇴사일: " + retireDt);
					System.out.println("권한레벨: " + level);

					System.out.println("────────────────────────────────────────────────────────────");
					System.out.println();
				}

				case 2 -> {
					System.out.print("조회할 이름([q: 돌아가기]) ➤ ");
					String name = br.readLine();
					InputValidator.isUserExit(name);

					List<EmployeeDTO> list = empDao.selectByName(name);
					printEmployeeListPaged(list);
				}

				case 3 -> {
					List<EmployeeDTO> list = empDao.selectAll();
					printEmployeeListPaged(list);
				}

				case 4 -> {
					return;
				}

				default -> System.out.println("잘못된 번호입니다. 1~4 사이의 값을 입력해주세요.\n");
				}
			}
		} catch (UserQuitException e) {
			System.out.println("\n정보 조회를 취소했습니다.\n");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 6. 사원관리 - 재직결재 */
	protected void updateRetireApprovalInfo() {
		PrintUtil.printTitle("관리자 - 사원관리 - 퇴직결재");

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
			System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t\n",
					PrintUtil.padCenter("번호", 8),
					PrintUtil.padCenter("사번", 8),
					PrintUtil.padCenter("퇴직일", 12),
					PrintUtil.padCenter("신청사유", 8),
					PrintUtil.padCenter("승인상태", 8)
			);

			PrintUtil.printLine('-', 64);

			if (list.isEmpty()) {
				System.out.println(CYAN + "👉 현재 미승인된 퇴직 신청이 없습니다." + RESET);
				PrintUtil.printLine('-', 64);
				return;
			}

			for (RetireDTO dto : list) {
				System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t\n",
						PrintUtil.padCenter(Integer.toString(dto.getRetireSeq()), 8),
						PrintUtil.padCenter(dto.getEmpNo(), 8),
						PrintUtil.padCenter(dto.getRegDt(), 12),
						PrintUtil.padCenter(dto.getRetireMemo() != null && dto.getRetireMemo().length() > 18
								? dto.getRetireMemo().substring(0, 15) + "..."
								: dto.getRetireMemo(), 8),
						PrintUtil.padCenter(dto.getApproverYn(), 8));
			}
			PrintUtil.printLine('-', 64);

			System.out.print(GREEN + "👉 승인하실 퇴직 신청 번호를 입력하세요 (취소: Enter) : " + RESET);
			input = br.readLine();

			if (input == null || input.trim().isEmpty()) {
				System.out.println(GRAY + "취소되었습니다." + RESET);
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
		PrintUtil.printTitle("관리자  -  사원관리  -  경력등록");
		try {
			String empNo = checkEmpNo(true);
			CareerDTO dto = new CareerDTO();
			dto.setEmpNo(empNo);

			System.out.print("회사명([q: 돌아가기]) ▶ ");
			String comp = br.readLine();
			InputValidator.isUserExit(comp);
			dto.setCompanyName(comp);

			System.out.print("근무시작일(YYYY-MM-DD, [q: 돌아가기]) ▶ ");
			String start = br.readLine();
			InputValidator.isUserExit(start);
			dto.setStartDt(start);

			System.out.print("근무종료일(YYYY-MM-DD, [q: 돌아가기]) ▶ ");
			String end = br.readLine();
			InputValidator.isUserExit(end);
			dto.setEndDt(end);

			System.out.print("상세([q: 돌아가기]) ▶ ");
			String det = br.readLine();
			InputValidator.isUserExit(det);
			dto.setDetails(det);

			empDao.insertCareer(dto);
			PrintUtil.printSection("등록 완료");
			System.out.println("경력 등록이 완료되었습니다.\n");

		} catch (UserQuitException e) {
			System.out.println("\n등록이 취소되었습니다.\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 8. 사원관리 - 자격증등록 */
	protected void insertLicenseInfo() {
		PrintUtil.printTitle("관리자  -  사원관리  -  자격증등록");
		try {
			String empNo = checkEmpNo(true);
			RewardDTO dto = new RewardDTO();
			dto.setEmpNo(empNo);

			System.out.print("자격증명([q: 돌아가기]) ▶ ");
			String name = br.readLine();
			InputValidator.isUserExit(name);
			dto.setRewardName(name);

			System.out.print("발급기관([q: 돌아가기]) ▶ ");
			String org = br.readLine();
			InputValidator.isUserExit(org);
			dto.setIssuer(org);

			System.out.print("취득일(YYYY-MM-DD, [q: 돌아가기]) ▶ ");
			String date = br.readLine();
			InputValidator.isUserExit(date);
			dto.setDate(date);

			empDao.insertLicense(dto);
			PrintUtil.printSection("등록 완료");
			System.out.println("자격증 등록이 완료되었습니다.\n");

		} catch (UserQuitException e) {
			System.out.println("\n등록이 취소되었습니다.\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 9. 사원관리 - 이력조회 */
	protected void selectHistoryInfo() {
		PrintUtil.printTitle("관리자 - 사원관리 - 이력조회");
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
					System.out.println("잘못된 번호입니다.\n");
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
				default -> System.out.println("잘못된 번호입니다.\n");
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
			System.out.print("사원번호(ex.00001, [q: 돌아가기]) ▶ ");
			String empNo = br.readLine();
			InputValidator.isUserExit(empNo);

			if (!InputValidator.isValidEmpNo(empNo)) {
				System.out.println("잘못된 형식입니다. 영문/숫자 조합 5자리로 입력해주세요.\n");
				continue;
			}
			boolean exists = empDao.selectByEmpNo(empNo) != null;

			if (mustExist && !exists) {
				System.out.println("존재하지 않는 사원번호입니다.\n");
				continue;
			}
			if (!mustExist && exists) {
				System.out.println("이미 존재하는 사원번호입니다.\n");
				continue;
			}
			return empNo;
		}
	}

	// ==================== 공통 : 사원 목록 페이징 ====================
	private void printEmployeeListPaged(List<EmployeeDTO> list) throws IOException {
		if (list == null || list.isEmpty()) {
			System.out.println("조회 결과가 없습니다.\n");
			return;
		}

		final int pageSize = 15; // 한 페이지에 15명
		int total = list.size();
		int totalPage = (total + pageSize - 1) / pageSize;
		int page = 1;

		// 각 컬럼 폭(내용 기준)
		final int W_EMP_NO = 6;
		final int W_NAME   = 6;
		final int W_RRN    = 13;
		final int W_ADDR   = 20;
		final int W_HIRE   = 10;
		final int W_DEPT   = 10;
		final int W_GRADE  = 6;
		final int W_STAT   = 4;
		final int W_CNTR   = 4;
		final int W_EMAIL  = 20;

		while (true) {
			int startIndex = (page - 1) * pageSize;
			int endIndex = Math.min(startIndex + pageSize, total);

			System.out.println();
			System.out.printf("▶ 사원 정보 목록 | 페이지 %d / %d | 총 %d건 | 조회범위: %d~%d%n",
					page, totalPage, total, startIndex + 1, endIndex);
			PrintUtil.printLine('=', 140);

			// 헤더
			System.out.printf("%s | %s | %s | %s | %s | %s | %s | %s | %s | %s%n",
					PrintUtil.padCenter("사번", W_EMP_NO),
					PrintUtil.padCenter("이름", W_NAME),
					PrintUtil.padCenter("주민번호", W_RRN),
					PrintUtil.padCenter("주소", W_ADDR),
					PrintUtil.padCenter("입사일", W_HIRE),
					PrintUtil.padCenter("부서명", W_DEPT),
					PrintUtil.padCenter("직급", W_GRADE),
					PrintUtil.padCenter("재직", W_STAT),
					PrintUtil.padCenter("계약", W_CNTR),
					PrintUtil.padCenter("이메일", W_EMAIL));
			PrintUtil.printLine('-', 140);

			for (int i = startIndex; i < endIndex; i++) {
				EmployeeDTO d = list.get(i);

				System.out.printf("%s | %s | %s | %s | %s | %s | %s | %s | %s | %s%n",
						PrintUtil.padRight(d.getEmpNo(), W_EMP_NO),
						PrintUtil.padRight(d.getEmpNm(), W_NAME),
						PrintUtil.padRight(d.getRrn(), W_RRN),
						PrintUtil.padRight(d.getEmpAddr(), W_ADDR),
						PrintUtil.padRight(d.getHireDt(), W_HIRE),
						PrintUtil.padRight(d.getDeptNm(), W_DEPT),
						PrintUtil.padRight(d.getGradeNm(), W_GRADE),
						PrintUtil.padRight(d.getEmpStatNm(), W_STAT),
						PrintUtil.padRight(d.getContractTpNm(), W_CNTR),
						PrintUtil.padRight(d.getEmail(), W_EMAIL));
			}

			PrintUtil.printLine('=', 140);
			System.out.print("[n: 다음, p: 이전, q: 종료] ➤ ");
			String cmd = br.readLine();
			if (cmd == null)
				cmd = "";
			cmd = cmd.trim().toLowerCase();

			if ("n".equals(cmd)) {
				if (page < totalPage)
					page++;
				else
					System.out.println("마지막 페이지입니다.\n");
			} else if ("p".equals(cmd)) {
				if (page > 1)
					page--;
				else
					System.out.println("첫 페이지입니다.\n");
			} else if ("q".equals(cmd)) {
				break;
			}
		}
	}

	// ==================== 공통 : 경력 이력 페이징 ====================
	private void printCareerHistoryPaged(List<HistoryDTO> list) throws IOException {
		if (list == null || list.isEmpty()) {
			System.out.println("등록된 경력 이력이 없습니다.\n");
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
			System.out.printf("▶ 경력 이력 목록 | 페이지 %d / %d | 총 %d건 | 조회범위: %d~%d%n",
					page, totalPage, total, startIndex + 1, endIndex);
			PrintUtil.printLine('=', 120);

			System.out.printf("%s | %s | %s | %s | %s | %s%n",
					PrintUtil.padCenter("사번", 6),
					PrintUtil.padCenter("이름", 8),
					PrintUtil.padCenter("회사명", 20),
					PrintUtil.padCenter("시작일", 10),
					PrintUtil.padCenter("종료일", 10),
					PrintUtil.padCenter("상세", 30));
			PrintUtil.printLine('-', 120);

			for (int i = startIndex; i < endIndex; i++) {
				HistoryDTO d = list.get(i);

				System.out.printf("%s | %s | %s | %s | %s | %s%n",
						PrintUtil.padRight(d.getEmpNo(), 6),
						PrintUtil.padRight(d.getEmpNm(), 8),
						PrintUtil.padRight(d.getPrevCompNm(), 20),
						PrintUtil.padRight(d.getStartDt(), 10),
						PrintUtil.padRight(d.getEndDt(), 10),
						PrintUtil.padRight(d.getDetails(), 30));
			}
			PrintUtil.printLine('=', 120);
			System.out.print("[n: 다음, p: 이전, q: 종료] ➤ ");
			String cmd = br.readLine();
			if (cmd == null)
				cmd = "";
			cmd = cmd.trim().toLowerCase();

			if ("n".equals(cmd)) {
				if (page < totalPage)
					page++;
				else
					System.out.println("마지막 페이지입니다.\n");
			} else if ("p".equals(cmd)) {
				if (page > 1)
					page--;
				else
					System.out.println("첫 페이지입니다.\n");
			} else if ("q".equals(cmd)) {
				break;
			}
		}
	}

	// ==================== 공통 : 자격증 이력 페이징 ====================
	private void printCertHistoryPaged(List<HistoryDTO> list) throws IOException {
		if (list == null || list.isEmpty()) {
			System.out.println("등록된 자격증 이력이 없습니다.\n");
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
			System.out.printf("▶ 자격증 이력 목록 | 페이지 %d / %d | 총 %d건 | 조회범위: %d~%d%n",
					page, totalPage, total, startIndex + 1, endIndex);
			PrintUtil.printLine('=', 120);

			System.out.printf("%s | %s | %s | %s | %s%n",
					PrintUtil.padCenter("사번", 6),
					PrintUtil.padCenter("이름", 8),
					PrintUtil.padCenter("자격증명", 20),
					PrintUtil.padCenter("발급기관", 20),
					PrintUtil.padCenter("발급일", 10));
			PrintUtil.printLine('-', 120);

			for (int i = startIndex; i < endIndex; i++) {
				HistoryDTO d = list.get(i);

				System.out.printf("%s | %s | %s | %s | %s%n",
						PrintUtil.padRight(d.getEmpNo(), 6),
						PrintUtil.padRight(d.getEmpNm(), 8),
						PrintUtil.padRight(d.getCertNm(), 20),
						PrintUtil.padRight(d.getIssueOrgNm(), 20),
						PrintUtil.padRight(d.getIssueDt(), 10));
			}
			PrintUtil.printLine('=', 120);
			System.out.print("[n: 다음, p: 이전, q: 종료] ➤ ");
			String cmd = br.readLine();
			if (cmd == null)
				cmd = "";
			cmd = cmd.trim().toLowerCase();

			if ("n".equals(cmd)) {
				if (page < totalPage)
					page++;
				else
					System.out.println("마지막 페이지입니다.\n");
			} else if ("p".equals(cmd)) {
				if (page > 1)
					page--;
				else
					System.out.println("첫 페이지입니다.\n");
			} else if ("q".equals(cmd)) {
				break;
			}
		}
	}

	// ==================== 공통 : 직급 이력 페이징 ====================
	private void printGradeHistoryPaged(List<HistoryDTO> list) throws IOException {
		if (list == null || list.isEmpty()) {
			System.out.println("등록된 직급 이력이 없습니다.\n");
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
			System.out.printf("▶ 직급 이력 목록 | 페이지 %d / %d | 총 %d건 | 조회범위: %d~%d%n",
					page, totalPage, total, startIndex + 1, endIndex);
			PrintUtil.printLine('=', 120);

			System.out.printf("%s | %s | %s | %s | %s | %s%n",
					PrintUtil.padCenter("시작일", 10),
					PrintUtil.padCenter("사번", 6),
					PrintUtil.padCenter("이름", 8),
					PrintUtil.padCenter("직급", 6),
					PrintUtil.padCenter("종료일", 10),
					PrintUtil.padCenter("부서", 12));
			PrintUtil.printLine('-', 120);

			for (int i = startIndex; i < endIndex; i++) {
				HistoryDTO d = list.get(i);

				System.out.printf("%s | %s | %s | %s | %s | %s%n",
						PrintUtil.padRight(d.getStartDt(), 10),
						PrintUtil.padRight(d.getEmpNo(), 6),
						PrintUtil.padRight(d.getEmpNm(), 8),
						PrintUtil.padRight(d.getGradeNm(), 6),
						PrintUtil.padRight(d.getEndDt(), 10),
						PrintUtil.padRight(d.getDeptNm(), 12));
			}
			PrintUtil.printLine('=', 120);
			System.out.print("[n: 다음, p: 이전, q: 종료] ➤ ");
			String cmd = br.readLine();
			if (cmd == null)
				cmd = "";
			cmd = cmd.trim().toLowerCase();

			if ("n".equals(cmd)) {
				if (page < totalPage)
					page++;
				else
					System.out.println("마지막 페이지입니다.\n");
			} else if ("p".equals(cmd)) {
				if (page > 1)
					page--;
				else
					System.out.println("첫 페이지입니다.\n");
			} else if ("q".equals(cmd)) {
				break;
			}

		}
	}

}
