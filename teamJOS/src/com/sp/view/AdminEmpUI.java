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
import com.sp.model.RewardDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;
import com.sp.view.common.DeptCommonUI;

public class AdminEmpUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private DeptCommonUI deptCommonUI = null;
	private EmpDAO empDao;

	public AdminEmpUI(EmpDAO empDao, LoginInfo loginInfo) {
		this.empDao = empDao;
		this.deptCommonUI = new DeptCommonUI(loginInfo);
	}

	/** 메인 메뉴 */
	public void menu() {
		int ch;
		System.out.println();
		System.out.println("==================================================================");
		System.out.println("                      [ 관리자 - 사원관리 메뉴 ]");
		try {
			do {
				System.out.println("""
						===================================================================
						  1. 정보등록     2. 정보수정     3. 부서이동     4. 진급관리     5. 정보조회
						  6. 재직결재     7. 경력등록     8. 자격증등록    9. 이력조회    10. 일괄등록 
						  11. 상위메뉴
						===================================================================""");

				System.out.print("선택 ➤ ");
				ch = Integer.parseInt(br.readLine());
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
					System.out.println("상위 메뉴로 돌아갑니다.");
					return;
				}
				default -> System.out.println("잘못된 입력입니다. 1~10 사이의 숫자를 입력해주세요.");
				}

			} while (ch != 10);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 1. 사원관리 - 사원 정보 등록 */
	public void insertEmployeeInfo() {
		PrintUtil.printTitle("관리자 - 사원관리 - 사원정보등록");
		EmployeeDTO dto = new EmployeeDTO();

		try {
			// ==================== 사원번호 ====================
			while (true) {
				System.out.print("사원번호(ex.00001) 입력(q: 돌아가기) ➤ ");
				String empNo = br.readLine();
				InputValidator.isUserExit(empNo);

				if (!empNo.matches("^\\d{5}$")) {
					System.out.println("형식 오류: 숫자 5자리로 입력해주세요.\n");
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
			System.out.print("이름(q: 돌아가기) ➤ ");
			String name = br.readLine();
			InputValidator.isUserExit(name);
			dto.setEmpNm(name);
			System.out.println();

			// ==================== 주민등록번호 ====================
			while (true) {
				System.out.print("'-' 제외한 주민번호 13자리(예시 : 0101013456789, q: 돌아가기) ➤ ");
				String rrn = br.readLine();
				InputValidator.isUserExit(rrn);

				if (!InputValidator.isValidRRN(rrn)) {
					System.out.println("형식 오류: 13자리 숫자로 입력해주세요.(0101013456789)\n");
					continue;
				}
				dto.setRrn(rrn);
				break;
			}
			System.out.println();

			// ==================== 주소 ====================
			System.out.print("주소(q: 돌아가기) ➤ ");
			String addr = br.readLine();
			InputValidator.isUserExit(addr);
			dto.setEmpAddr(addr);
			System.out.println();

			// ==================== 부서 코드 ====================
			String deptCd;
			while (true) {
				PrintUtil.printSection("부서 코드");
				deptCommonUI.selectAllDept();
				System.out.print("부서코드 입력(q: 돌아가기) ➤ ");
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
				System.out.println("01.사원 | 02.대리 | 03.과장 | 04.차장 | 05.부장 | 06.이사 | 07.대표이사");
				System.out.print("직급코드 입력(q: 돌아가기) ➤ ");
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

			// ==================== 사원 상태 ====================
			dto.setEmpStatCd("A");
			PrintUtil.printSection("사원 상태");
			System.out.println("신규 등록 사원은 기본적으로 재직 상태(A)로 설정됩니다.\n");

			// ==================== 계약구분 코드 ====================
			String contractCd;
			while (true) {
				PrintUtil.printSection("계약구분 코드");
				System.out.println("  1. 정규직     |     2. 계약직     |     3. 인턴");
				System.out.print("계약구분코드 입력(q: 돌아가기) ➤ ");
				contractCd = br.readLine();
				InputValidator.isUserExit(contractCd);

				if (!contractCd.matches("^[123]$")) {
					System.out.println("입력 오류: 1~3 중 하나를 선택해주세요.\n");
					continue;
				}
				dto.setContractTpCd(contractCd);
				break;
			}
			System.out.println();

			// ==================== 이메일 ====================
			while (true) {
				System.out.print("이메일(q: 돌아가기) ➤ ");
				String email = br.readLine();
				InputValidator.isUserExit(email);

				if (!InputValidator.isValidEmail(email)) {
					System.out.println("형식 오류: example@domain.com 형태로 입력해주세요.\n");
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
				System.out.print("비밀번호(q: 돌아가기) ➤ ");
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
				System.out.println("01. 일반사원   |   02. 관리자   |   03. 인사담당자");
				System.out.print("레벨코드 입력(q: 돌아가기) ➤ ");
				levelCode = br.readLine();
				InputValidator.isUserExit(levelCode);

				if (!levelCode.matches("0[1-3]")) {
					System.out.println("입력 오류: 01~03 사이의 값을 입력해주세요.\n");
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
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("처리 중 오류가 발생했습니다.\n");
		}
	}

	/** 2. 사원관리 - 정보 수정 */
	protected void updateEmployeeInfo() {
		PrintUtil.printTitle("관리자 - 사원관리 - 정보수정");
		try {
			String empNo = checkEmpNo(true);

			PrintUtil.printSection("수정 항목 선택");
			System.out.println("1. 이름 | 2. 주소 | 3. 이메일 | 4. 비밀번호 | 5. 권한레벨 | 6. 상위메뉴");
			System.out.print("선택(q: 돌아가기) ➤ ");
			String sel = br.readLine();
			InputValidator.isUserExit(sel);
			int ch = Integer.parseInt(sel);
			if (ch == 6)
				return;

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

			System.out.print("변경할 값 입력(q: 돌아가기) ➤ ");
			String val = br.readLine();
			InputValidator.isUserExit(val);

			empDao.updateEmployee(empNo, col, val);
			System.out.println("\n수정이 완료되었습니다.\n");

		} catch (UserQuitException e) {
			System.out.println("\n수정을 취소하고 상위 메뉴로 돌아갑니다.\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("처리 중 오류가 발생했습니다.\n");
		}
	}

	/** 3. 사원관리 - 부서 이동 */
	private void updateDeptMoveInfo() {
		PrintUtil.printTitle("관리자 - 사원관리 - 부서이동");
		try {
			String empNo = checkEmpNo(true);
			EmployeeDTO emp = empDao.selectDeptName(empNo);

			PrintUtil.printSection("현재 부서 정보");
			System.out.println("사원명: " + emp.getEmpNm());
			System.out.println("현재 부서코드: " + emp.getDeptCd());
			System.out.println();

			PrintUtil.printSection("이동할 부서 선택");
			deptCommonUI.selectAllDept();

			String newDeptCd;
			while (true) {
				System.out.print("이동할 부서코드(q: 돌아가기) ➤ ");
				newDeptCd = br.readLine();
				InputValidator.isUserExit(newDeptCd);

				if (!empDao.isValidDeptCd(newDeptCd)) {
					System.out.println("존재하지 않는 부서 코드입니다.\n");
					continue;
				}
				if (newDeptCd.equals(emp.getDeptCd())) {
					System.out.println("현재 부서와 동일합니다.\n");
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
		}
	}

	/** 3. 사원관리 - 진급 이동 */
	private void updatePromotionInfo() {
		System.out.println("\n[관리자 - 사원관리 - 진급관리]");
		try {
			PromotionDTO dto = new PromotionDTO();
			EmployeeDTO emp = null;
			String empNo;

			// ==================== 사번 입력 ====================
			empNo = checkEmpNo(true);

			// ==================== 부서명 + 직급 불러오기 ====================
			emp = empDao.selectDeptName(empNo);

			// ==================== 직급 코드 목록 출력 ====================
			System.out.println("==============================================================");
			System.out.println("                       [ 직급 코드 목록 ]");
			System.out.println("==============================================================");
			System.out.println("  01.사원   02.대리   03.과장   04.차장   05.부장   06.이사   07.대표이사");
			System.out.println("==============================================================");

			// ==================== 현재 직급 정보 표시 ====================
			System.out.println("\n──────────────────────────────");
			System.out.println("사원명       : " + emp.getEmpNm());
			System.out.println("현재 직급코드 : " + emp.getGradeCd());
			System.out.println("현재 부서명   : " + emp.getDeptNm());
			System.out.println("──────────────────────────────\n");

			// ==================== 진급 코드 입력 (유효성 검증) ====================
			String newGrade;
			while (true) {
				System.out.print("진급할 직급코드 ➤ ");
				newGrade = br.readLine();

				if (!empDao.isValidGradeCd(newGrade)) {
					System.out.println("존재하지 않는 직급 코드입니다. 다시 입력해주세요.");
					continue;
				}

				if (newGrade.equals(emp.getGradeCd())) {
					System.out.println("현재 직급과 동일합니다. 다른 직급을 입력해주세요.");
					continue;
				}
				break;
			}

			// ==================== 진급 사유 입력 ====================
			String reason;
			while (true) {
				System.out.print("진급 사유 ➤ ");
				reason = br.readLine();
				if (reason == null || reason.trim().isEmpty()) {
					System.out.println("진급 사유는 반드시 입력해야 합니다.");
					continue;
				}
				break;
			}

			// ==================== DTO 세팅 ====================
			dto.setEmpNo(empNo);
			dto.setCurrentGradeCd(emp.getGradeCd());
			dto.setNewGradeCd(newGrade);

			// ==================== DB 업데이트 ====================
			int result = empDao.updatePromotion(dto);

			if (result > 2) {
				System.out.println("\n진급 처리가 완료되었습니다.\n");
			} else {
				System.out.println("\n진급 처리에 실패하였습니다.\n");
			}

		} catch (IOException e) {
			System.out.println("입력 처리 중 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("데이터베이스 처리 중 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("데이터가 존재하지 않거나 잘못된 접근입니다.");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("잘못된 숫자 형식이 입력되었습니다.");
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
				System.out.print("1.사번조회 | 2.이름조회 | 3.전체조회 | 4.상위메뉴로 ➤ ");
				int ch = Integer.parseInt(br.readLine());

				switch (ch) {
				case 1 -> {
					String empNo = checkEmpNo(true);
					EmployeeDTO dto = empDao.selectByEmpNo(empNo);
					if (dto == null) {
						System.out.println("해당 사원번호의 정보가 존재하지 않습니다.\n");
						return;
					}

					System.out.print(dto.getEmpNo() + "\t");
					System.out.print(dto.getEmpNm() + "\t");
					System.out.print(dto.getRrn() + "\t");
					System.out.print(dto.getEmpAddr() + "\t");
					System.out.print(dto.getHireDt() + "\t");
					System.out.print(dto.getDeptNm() + "\t");
					System.out.print(dto.getGradeNm() + "\t");
					System.out.print(dto.getEmpStatNm() + "\t");
					System.out.print(dto.getContractTpNm() + "\t");
					System.out.print(dto.getEmail() + "\t");
					System.out.print(dto.getPwd() + "\t");
					System.out.print(dto.getRegDt() + "\t");
					System.out.print(dto.getRetireDt() + "\t");
					System.out.println(dto.getLevelCode());
				}

				case 2 -> {
					System.out.print("조회할 이름: ");
					String name = br.readLine();
					List<EmployeeDTO> list = empDao.selectByName(name);
					if (list.size() == 0) {
						System.out.println("등록된 자료가 없습니다.\n");
						return;
					}
					for (EmployeeDTO dto : list) {
						System.out.print(dto.getEmpNo() + "\t");
						System.out.print(dto.getEmpNm() + "\t");
						System.out.print(dto.getRrn() + "\t");
						System.out.print(dto.getEmpAddr() + "\t");
						System.out.print(dto.getHireDt() + "\t");
						System.out.print(dto.getDeptNm() + "\t");
						System.out.print(dto.getGradeNm() + "\t");
						System.out.print(dto.getEmpStatNm() + "\t");
						System.out.print(dto.getContractTpNm() + "\t");
						System.out.print(dto.getEmail() + "\t");
						System.out.print(dto.getPwd() + "\t");
						System.out.print(dto.getRegDt() + "\t");
						System.out.print(dto.getRetireDt() + "\t");
						System.out.println(dto.getLevelCode());
					}
				}
				case 3 -> {
					List<EmployeeDTO> list = empDao.selectAll();
					for (EmployeeDTO dto : list) {
						System.out.print(dto.getEmpNo() + "\t");
						System.out.print(dto.getEmpNm() + "\t");
						System.out.print(dto.getRrn() + "\t");
						System.out.print(dto.getEmpAddr() + "\t");
						System.out.print(dto.getHireDt() + "\t");
						System.out.print(dto.getDeptNm() + "\t");
						System.out.print(dto.getGradeNm() + "\t");
						System.out.print(dto.getEmpStatNm() + "\t");
						System.out.print(dto.getContractTpNm() + "\t");
						System.out.print(dto.getEmail() + "\t");
						System.out.print(dto.getPwd() + "\t");
						System.out.print(dto.getRegDt() + "\t");
						System.out.print(dto.getRetireDt() + "\t");
						System.out.println(dto.getLevelCode());
					}
				}
				case 4 -> {
					return;
				}
				default -> System.out.println("잘못된 번호입니다. 1~4 사이의 값을 입력해주세요.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 6. 사원관리 - 재직결재 */
	protected void updateRetireApprovalInfo() {
		PrintUtil.printTitle("관리자 - 사원관리 - 재직결재");
		try {
			String empNo = checkEmpNo(true);
			PrintUtil.printSection("근무상태 코드");
			System.out.println("1. 재직(A) | 2. 퇴직(R)");

			System.out.print("변경할 상태 번호(q: 돌아가기) ➤ ");
			String sel = br.readLine();
			InputValidator.isUserExit(sel);

			String status = switch (sel) {
			case "1" -> "A";
			case "2" -> "R";
			default -> null;
			};

			if (status == null) {
				System.out.println("잘못된 번호입니다.\n");
				return;
			}

			empDao.updateRetireApproval(empNo, status);
			PrintUtil.printSection("처리 완료");
			System.out.println("상태 변경이 완료되었습니다.\n");

		} catch (UserQuitException e) {
			System.out.println("\n처리가 취소되었습니다.\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 7. 사원관리 - 경력등록 */
	protected void insertCareerInfo() {
		PrintUtil.printTitle("관리자 - 사원관리 - 경력등록");
		try {
			String empNo = checkEmpNo(true);
			CareerDTO dto = new CareerDTO();
			dto.setEmpNo(empNo);

			System.out.print("회사명(q: 돌아가기) ➤ ");
			String comp = br.readLine();
			InputValidator.isUserExit(comp);
			dto.setCompanyName(comp);

			System.out.print("근무시작일(YYYY-MM-DD, q: 돌아가기) ➤ ");
			String start = br.readLine();
			InputValidator.isUserExit(start);
			dto.setStartDt(start);

			System.out.print("근무종료일(YYYY-MM-DD, q: 돌아가기) ➤ ");
			String end = br.readLine();
			InputValidator.isUserExit(end);
			dto.setEndDt(end);

			System.out.print("상세(q: 돌아가기) ➤ ");
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
		PrintUtil.printTitle("관리자 - 사원관리 - 자격증등록");
		try {
			String empNo = checkEmpNo(true);
			RewardDTO dto = new RewardDTO();
			dto.setEmpNo(empNo);

			System.out.print("자격증명(q: 돌아가기) ➤ ");
			String name = br.readLine();
			InputValidator.isUserExit(name);
			dto.setRewardName(name);

			System.out.print("발급기관(q: 돌아가기) ➤ ");
			String org = br.readLine();
			InputValidator.isUserExit(org);
			dto.setIssuer(org);

			System.out.print("취득일(YYYY-MM-DD, q: 돌아가기) ➤ ");
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
				System.out.print("1. 경력 | 2. 자격증 | 3. 직급이력 | 4. 상위메뉴(q: 돌아가기) ➤ ");
				String sel = br.readLine();
				InputValidator.isUserExit(sel);
				int ch = Integer.parseInt(sel);

				List<HistoryDTO> list;

				switch (ch) {
				// ==================== 1. 경력 이력 ====================
				case 1 -> {
					list = empDao.selectCareerHisAll();

					PrintUtil.printSection("경력 이력");
					PrintUtil.printTableHeader("사번", "이름", "회사명", "시작일", "종료일", "상세");
					for (HistoryDTO d : list) {
						PrintUtil.printTableRow(d.getEmpNo(), d.getEmpNm(), d.getPrevCompNm(), d.getStartDt(),
								d.getEndDt(), d.getDetails());
					}
					PrintUtil.printLine('-', 70);
				}

				// ==================== 2. 자격증 이력 ====================
				case 2 -> {
					list = empDao.selectCertHisAll();

					PrintUtil.printSection("자격증");
					PrintUtil.printTableHeader("사번", "이름", "자격증명", "발급기관", "발급일");
					for (HistoryDTO d : list) {
						PrintUtil.printTableRow(d.getEmpNo(), d.getEmpNm(), d.getCertNm(), d.getIssueOrgNm(),
								d.getIssueDt());
					}
					PrintUtil.printLine('-', 70);
				}

				// ==================== 3. 직급 이력 ====================
				case 3 -> {
					list = empDao.selectGradeHisAll();

					PrintUtil.printSection("직급 이력");
					PrintUtil.printTableHeader("시작일", "사번", "이름", "직급", "종료일", "부서");
					for (HistoryDTO d : list) {
						PrintUtil.printTableRow(d.getStartDt(), d.getEmpNo(), d.getEmpNm(), d.getGradeNm(),
								d.getEndDt(), d.getDeptNm());
					}
					PrintUtil.printLine('-', 70);
				}

				// ==================== 4. 상위메뉴 ====================
				case 4 -> {
					return;
				}

				// ==================== 예외 입력 ====================
				default -> System.out.println("잘못된 번호입니다.\n");
				}
				System.out.println();
			}

		} catch (UserQuitException e) {
			System.out.println("\n이력 조회를 취소했습니다.\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String checkEmpNo(boolean mustExist) throws IOException, SQLException {
		while (true) {
			System.out.print("사원번호(ex.00001, q: 돌아가기) ➤ ");
			String empNo = br.readLine();
			InputValidator.isUserExit(empNo);

			if (!empNo.matches("^\\d{5}$")) {
				System.out.println("잘못된 형식입니다. 숫자 5자리로 입력해주세요.\n");
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

		}
	}
	
	protected void loadEmployeeInfo() {
		PrintUtil.printSection("csv 파일로드");
		empDao.loadEmployeeInfo();
		PrintUtil.printSection("로드 완료");
		
	}
}
