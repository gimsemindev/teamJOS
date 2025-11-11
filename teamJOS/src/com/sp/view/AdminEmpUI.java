package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.sp.dao.EmpDAO;
import com.sp.model.CareerDTO;
import com.sp.model.DeptMoveDTO;
import com.sp.model.EmployeeDTO;
import com.sp.model.PromotionDTO;
import com.sp.model.RewardDTO;
import com.sp.util.LoginInfo;
import com.sp.view.common.DeptCommonUI;

public class AdminEmpUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private DeptCommonUI deptCommonUI = new DeptCommonUI();
	private EmpDAO empDao;

	public AdminEmpUI(EmpDAO empDao, LoginInfo loginInfo) {
		this.empDao = empDao;
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
						  6. 재직결재     7. 경력등록     8. 자격증등록    9. 이력조회     10. 상위메뉴
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
				case 10 -> {
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
		System.out.println("\n[관리자 - 사원관리 - 사원정보등록]");
		EmployeeDTO dto = new EmployeeDTO();

		try {
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. D1001 형식으로 입력해주세요.");
					continue;
				}
				if (empDao.selectByEmpNo(empNo) != null) {
					System.out.println("이미 등록된 사원번호입니다. 다른 번호를 입력해주세요.");
					continue;
				}
				break;
			}

			dto.setEmpNo(empNo);
			System.out.print("이름: ");
			dto.setEmpNm(br.readLine());
			System.out.print("주민번호(ex.0000000000000): ");
			dto.setRrn(br.readLine());
			System.out.print("주소: ");
			dto.setEmpAddr(br.readLine());

			// ==================== 부서 코드 ====================
			System.out.println("\n───────────────────────────────");
			System.out.println("[부서 목록]");
			deptCommonUI.selectAllDept();
			System.out.println("───────────────────────────────");
			System.out.print("부서코드 입력 ➤ ");
			dto.setDeptCd(br.readLine());

			// ==================== 직급 코드 ====================
			System.out.println("\n───────────────────────────────");
			System.out.println("[직급 코드]");
			System.out.println("01.사원 | 02.대리 | 03.과장 | 04.차장 | 05.부장 | 06.이사 | 07.대표이사");
			System.out.print("직급코드 입력 ➤ ");
			dto.setGradeCd(br.readLine());

			// ==================== 사원 상태 ====================
			System.out.println("\n───────────────────────────────");
			System.out.println("[사원상태 코드]");
			System.out.println("A.재직 | R.퇴직 | L.휴직");
			System.out.print("사원상태코드 입력 ➤ ");
			dto.setEmpStatCd(br.readLine());

			// ==================== 계약 구분 ====================
			System.out.println("\n───────────────────────────────");
			System.out.println("[계약구분 코드]");
			System.out.println("1.정규직 | 2.계약직 | 3.인턴");
			System.out.print("계약구분코드 입력 ➤ ");
			dto.setContractTpCd(br.readLine());

			// ==================== 기본 정보 ====================
			System.out.print("이메일: ");
			dto.setEmail(br.readLine());
			System.out.print("비밀번호: ");
			dto.setPwd(br.readLine());

			// ==================== 권한 레벨 ====================
			System.out.println("\n───────────────────────────────");
			System.out.println("[권한 레벨 코드]");
			System.out.println("01.일반사원 | 02.관리자 | 03.인사담당자");
			System.out.print("레벨코드 입력 ➤ ");
			dto.setLevelCode(br.readLine());

			// ==================== DB 등록 ====================
			empDao.insertEmployee(dto);
			System.out.println("\n사원 정보 등록이 완료되었습니다.\n");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 2. 사원관리 - 정보 수정 */
	protected void updateEmployeeInfo() {
		System.out.println("\n[관리자 - 사원관리 - 정보수정]");
		try {
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. D1001 형식으로 입력해주세요.");
					continue;
				}
				if (empDao.selectByEmpNo(empNo) == null) {
					System.out.println("존재하지 않는 사원번호입니다.");
					continue;
				}
				break;
			}

			System.out.println("""
					=====================================================
									[수정할 항목 선택]
					1.이름 | 2.주소 | 3.이메일 | 4.비밀번호 | 5.권한레벨 | 6.상위메뉴
					=====================================================
					""");

			System.out.print("선택 ➤ ");
			int ch = Integer.parseInt(br.readLine());
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

			System.out.print("변경할 값 입력 ➤ ");
			String val = br.readLine();

			empDao.updateEmployee(empNo, col, val);
			System.out.println("\n수정이 완료되었습니다.\n");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 3. 사원관리 - 부서 이동 */
	private void updateDeptMoveInfo() {
		System.out.println("\n[관리자 - 사원관리 - 부서이동]");
		try {
			DeptMoveDTO dto = new DeptMoveDTO();
			EmployeeDTO emp = new EmployeeDTO();
			String empNo;

			// ==================== 사번 입력 ====================
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				emp = empDao.selectByEmpNo(empNo);

				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. D1001 형식으로 입력해주세요.");
					continue;
				}

				if (emp == null) {
					System.out.println("해당 사원번호가 존재하지 않습니다. 다시 입력해주세요.");
					continue;
				}
				break;
			}

			// ==================== 이동할 부서 입력 ====================
			System.out.println("[ 부서 코드 목록 ]");
			deptCommonUI.selectAllDept();

			// ==================== 현재 부서 정보 표시 ====================
			System.out.println("\n──────────────────────────────");
			System.out.println("사원명       : " + emp.getEmpNm());
			emp = empDao.selectDeptName(empNo);
			System.out.println("현재 부서코드 : " + emp.getDeptCd());
			System.out.println("──────────────────────────────\n");

			System.out.print("이동할 부서코드 ➤ ");
			dto.setNewDeptCd(br.readLine());
			dto.setEmpNo(empNo);



			// ==================== DB 업데이트 ====================
			empDao.updateDeptMove(dto);
			System.out.println("\n부서 이동이 완료되었습니다.\n");

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

	/** 4. 사원관리 - 진급 관리 */
	private void updatePromotionInfo() {
		System.out.println("\n[관리자 - 사원관리 - 진급관리]");
		try {
			PromotionDTO dto = new PromotionDTO();
			EmployeeDTO emp = null;
			String empNo;

			// ==================== 사번 입력 ====================
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();

				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. D1001 형식으로 입력해주세요.");
					continue;
				}

				emp = empDao.selectByEmpNo(empNo);
				if (emp == null) {
					System.out.println("해당 사원번호가 존재하지 않습니다. 다시 입력해주세요.");
					continue;
				}
				break;
			}

			// ==================== 부서명 + 직급 불러오기 ====================
			emp = empDao.selectDeptName(empNo);

			// ==================== 진급 코드 입력 ====================
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

			System.out.print("진급할 직급코드 ➤ ");
			String newGrade = br.readLine();
			System.out.println("진급 사유 ➤ ");

			// ==================== DTO 세팅 ====================
			dto.setEmpNo(empNo);
			dto.setCurrentGradeCd(emp.getGradeCd());
			dto.setNewGradeCd(newGrade);

			// ==================== DB 업데이트 ====================
			empDao.updatePromotion(dto);
			System.out.println("\n진급 처리가 완료되었습니다.\n");

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
				    String empNo;
				    while (true) {
				        System.out.print("사원번호(ex. D1001): ");
				        empNo = br.readLine();
				        if (!empNo.matches("^D\\d{4}$")) {
				            System.out.println("잘못된 형식입니다. D1001 형식으로 입력해주세요.");
				            continue;
				        }
				        break;
				    }

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
		System.out.println("\n[관리자 - 사원관리 - 재직결재]");
		try {
			String empNo;
			String status;

			// ==================== 사번 입력 ====================
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. D1001 형식으로 입력해주세요.");
					continue;
				}
				break;
			}

			// ==================== 상태 선택 ====================
			System.out.println("==============================================================");
			System.out.println("                    [ 근무상태 코드 목록 ]");
			System.out.println("==============================================================");
			System.out.println("  1. 재직(A)     2. 휴직(L)     3. 퇴직(R)     4. 교육파견(T)");
			System.out.println("==============================================================");
			System.out.print("변경할 상태 번호 선택 ➤ ");

			int ch = Integer.parseInt(br.readLine());

			switch (ch) {
			case 1 -> status = "A";
			case 2 -> status = "L";
			case 3 -> status = "R";
			case 4 -> status = "T";
			default -> {
				System.out.println("잘못된 번호입니다. 1~4 중 하나를 선택해주세요.");
				return;
			}
			}

			// ==================== DB 업데이트 ====================
			empDao.updateRetireApproval(empNo, status);
			System.out.println("부서 이동이 완료되었습니다.");

		} catch (IOException e) {
			System.out.println("입력 처리 중 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("잘못된 숫자 형식이 입력되었습니다.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("예상치 못한 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	/** 7. 사원관리 - 경력등록 */
	protected void insertCareerInfo() {
		System.out.println("\n[관리자 - 사원관리 - 경력등록]");
		try {
			CareerDTO dto = new CareerDTO();
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. D1001 형식으로 입력해주세요.");
					continue;
				}
				break;
			}
			dto.setEmpNo(empNo);

			// ==================== 경력 정보 입력 ====================
			System.out.print("회사명: ");
			dto.setCompanyName(br.readLine());
			System.out.print("근무시작일(YYYY-MM-DD): ");
			dto.setStartDt(br.readLine());
			System.out.print("근무종료일(YYYY-MM-DD): ");
			dto.setEndDt(br.readLine());
			System.out.print("상세: ");
			dto.setDetails(br.readLine());

			// ==================== DB 등록 ====================
			empDao.insertCareer(dto);
			System.out.println("\n경력 등록이 완료되었습니다.\n");

		} catch (IOException e) {
			System.out.println("입력 처리 중 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("데이터베이스 처리 중 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("입력된 값이 누락되었거나 null 상태입니다.");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("숫자 형식이 잘못되었습니다.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("예상치 못한 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	/** 8. 사원관리 - 자격증등록 */
	protected void insertLicenseInfo() {
		System.out.println("\n[관리자 - 사원관리 - 자격증등록]");
		try {
			RewardDTO dto = new RewardDTO();
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. D1001 형식으로 입력해주세요.");
					continue;
				}
				break;
			}
			dto.setEmpNo(empNo);

			// ==================== 자격증 정보 입력 ====================
			System.out.print("자격증명: ");
			dto.setRewardName(br.readLine());
			System.out.print("발급기관: ");
			dto.setIssuer(br.readLine());
			System.out.print("취득일(YYYY-MM-DD): ");
			dto.setDate(br.readLine());

			// ==================== DB 등록 ====================
			empDao.insertLicense(dto);
			System.out.println("\n자격증 등록이 완료되었습니다.\n");

		} catch (IOException e) {
			System.out.println("입력 처리 중 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("데이터베이스 처리 중 오류가 발생했습니다.");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("입력 데이터가 누락되었거나 잘못되었습니다.");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("잘못된 숫자 형식이 입력되었습니다.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("예상치 못한 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	/** 9. 사원관리 - 이력조회 */
	protected void selectHistoryInfo() {
		System.out.println("\n[관리자 - 사원관리 - 이력조회]");
		try {
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. D1001 형식으로 입력해주세요.");
					continue;
				}
				break;
			}

			// ==================== DB 조회 ====================
			empDao.selectHistory(empNo);
			System.out.println("\n이력 조회가 완료되었습니다.\n");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
