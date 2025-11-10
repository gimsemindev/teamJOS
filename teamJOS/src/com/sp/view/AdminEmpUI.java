package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

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
	private LoginInfo loginInfo;

	public AdminEmpUI(EmpDAO empDao, LoginInfo loginInfo) {
		this.empDao = empDao;
		this.loginInfo = loginInfo;
	}

	/** 메인 메뉴 */
	public void menu() {
		int ch;
		System.out.println("\n[관리자 - 사원관리]");
		try {
			do {
				System.out.print("""
						───────────────────────────────────────────────
						[관리자 - 사원관리 메뉴]
						1. 정보등록
						2. 정보수정
						3. 부서이동
						4. 진급관리
						5. 정보조회
						6. 퇴직결재
						7. 경력등록
						8. 자격증등록
						9. 이력조회
						10. 상위메뉴로 돌아가기
						───────────────────────────────────────────────
						선택 ➤ """);
				ch = Integer.parseInt(br.readLine());
			} while (ch < 1 || ch > 10);

			switch (ch) {
			case 1 -> insertEmployeeInfo();
			case 2 -> updateEmployeeInfo();
			case 3 -> updateDeptMoveInfo();
			case 4 -> updatePromotionInfo();
			case 5 -> manageEmployeeSearch();
			case 6 -> updateRetireApprovalInfo();
			case 7 -> insertCareerInfo();
			case 8 -> insertLicenseInfo();
//			case 9 -> selectHistoryInfo();
			case 10 -> {
				return;
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 사원관리 - 정보등록 */
	public void insertEmployeeInfo() {
		System.out.println("\n[관리자 - 사원관리 - 사원정보등록]");
		EmployeeDTO dto = new EmployeeDTO();

		try {
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. 예) D1001 형식으로 입력해주세요.");
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

			// 부서 리스트
			System.out.println("\n╔════════════════════════════════════╗");
			System.out.println("║             [부서 목록]               ║");
			System.out.println("╠════════════════════════════════════╣");
			deptCommonUI.selectAllDept();
			System.out.println("╚════════════════════════════════════╝");
			System.out.print("부서코드 입력 ➤ ");
			dto.setDeptCd(br.readLine());

			// 직급 리스트
			System.out.println("\n╔════════════════════════════════════╗");
			System.out.println("║             [직급 코드 목록]           ║");
			System.out.println("╠════════════════════════════════════╣");
			System.out.println("║  01 │ 사원                         ║");
			System.out.println("║  02 │ 대리                         ║");
			System.out.println("║  03 │ 과장                         ║");
			System.out.println("║  04 │ 차장                         ║");
			System.out.println("║  05 │ 부장                         ║");
			System.out.println("║  06 │ 이사                         ║");
			System.out.println("║  07 │ 대표이사                     ║");
			System.out.println("╚════════════════════════════════════╝");
			System.out.print("직급코드 입력 ➤ ");
			dto.setGradeCd(br.readLine());

			// 사원 상태
			System.out.println("\n╔════════════════════════════════════╗");
			System.out.println("║           [사원상태 코드]             ║");
			System.out.println("╠════════════════════════════════════╣");
			System.out.println("║  A │ 재직                          ║");
			System.out.println("║  R │ 퇴직                          ║");
			System.out.println("║  L │ 휴직                          ║");
			System.out.println("╚════════════════════════════════════╝");
			System.out.print("사원상태코드 입력 ➤ ");
			dto.setEmpStatCd(br.readLine());

			// 계약 구분
			System.out.println("\n╔════════════════════════════════════╗");
			System.out.println("║           [계약구분 코드]             ║");
			System.out.println("╠════════════════════════════════════╣");
			System.out.println("║  1 │ 정규직                         ║");
			System.out.println("║  2 │ 계약직                         ║");
			System.out.println("║  3 │ 인턴                           ║");
			System.out.println("╚════════════════════════════════════╝");
			System.out.print("계약구분코드 입력 ➤ ");
			dto.setContractTpCd(br.readLine());

			// 기본 정보
			System.out.print("이메일: ");
			dto.setEmail(br.readLine());
			System.out.print("비밀번호: ");
			dto.setPwd(br.readLine());

			// 권한 레벨
			System.out.println("\n╔════════════════════════════════════╗");
			System.out.println("║           [권한 레벨 코드]            ║");
			System.out.println("╠════════════════════════════════════╣");
			System.out.println("║  01 │ 일반사원                      ║");
			System.out.println("║  02 │ 관리자                        ║");
			System.out.println("║  03 │ 인사담당자                    ║");
			System.out.println("╚════════════════════════════════════╝");
			System.out.print("레벨코드 입력 ➤ ");
			dto.setLevelCode(br.readLine());

			empDao.insertEmployee(dto);
			System.out.println("\n사원 정보 등록이 완료되었습니다.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 사원관리 - 정보수정 */
	protected void updateEmployeeInfo() {
		System.out.println("\n[관리자 - 사원관리 - 정보수정]");
		try {
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. 예) D1001 형식으로 입력해주세요.");
					continue;
				}
				if (empDao.selectByEmpNo(empNo) == null) {
					System.out.println("존재하지 않는 사원번호입니다.");
					continue;
				}
				break;
			}

			System.out.println("""
					─────────────────────────────
					수정할 항목 선택
					1. 이름
					2. 주소
					3. 이메일
					4. 비밀번호
					5. 권한레벨
					6. 상위메뉴로
					─────────────────────────────
					""");

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
//			if (r > 0)
				System.out.println("수정이 완료되었습니다.");
//			else
//				System.out.println("수정에 실패했습니다.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 사원관리 - 부서이동 */
	private void updateDeptMoveInfo() {
		System.out.println("\n[관리자 - 사원관리 - 부서이동]");
		try {
			DeptMoveDTO dto = new DeptMoveDTO();
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. 예) D1001 형식으로 입력해주세요.");
					continue;
				}
				break;
			}
			
			 EmployeeDTO emp = empDao.selectByEmpNo(empNo);
		        if (emp == null) {
		            System.out.println("해당 사원번호가 존재하지 않습니다.");
		            return;
		        }

		        // 수정
//		        String currentDeptCd = emp.getDeptCd();
//		        if (currentDeptCd == null) {
//		            System.out.println("현재 부서 정보를 찾을 수 없습니다.");
//		            return;
//		        }
		        System.out.println("현재 부서코드: " + emp.getDeptCd());
		        
			dto.setEmpNo(empNo);
			System.out.print("이동할 부서코드: ");
			dto.setNewDeptCd(br.readLine());

			empDao.updateDeptMove(dto);
			System.out.println("부서 이동이 완료되었습니다.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 사원관리 - 진급관리 */
	private void updatePromotionInfo() {
		System.out.println("\n[관리자 - 사원관리 - 진급관리]");
		try {
			PromotionDTO dto = new PromotionDTO();
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. 예) D1001 형식으로 입력해주세요.");
					continue;
				}
				break;
			}
			dto.setEmpNo(empNo);

			System.out.print("현재 직급코드: ");
			dto.setCurrentGradeCd(br.readLine());
			System.out.print("진급할 직급코드: ");
			dto.setNewGradeCd(br.readLine());

			empDao.updatePromotion(dto);
			System.out.println("진급 처리가 완료되었습니다.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 사원관리 - 정보조회 */
	private void manageEmployeeSearch() {
		System.out.println("\n[관리자 - 사원관리 - 정보조회]");
		try {
			while (true) {
				System.out.print("1. 사번조회  2. 이름조회  3. 전체조회  4. 상위메뉴로 ➤ ");
				int ch = Integer.parseInt(br.readLine());

				switch (ch) {
				case 1 -> {
					String empNo;
					while (true) {
						System.out.print("사원번호(ex. D1001): ");
						empNo = br.readLine();
						if (!empNo.matches("^D\\d{4}$")) {
							System.out.println("잘못된 형식입니다. 예) D1001 형식으로 입력해주세요.");
							continue;
						}
						break;
					}
					empDao.selectByEmpNo(empNo);
				}
				case 2 -> {
					System.out.print("조회할 이름: ");
					String name = br.readLine();
					empDao.selectByName(name);
				}
				case 3 -> empDao.selectAll();
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

	/** 사원관리 - 퇴직결재 */
	protected void updateRetireApprovalInfo() {
		System.out.println("\n[관리자 - 사원관리 - 퇴직신청결재]");
		try {
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. 예) D1001 형식으로 입력해주세요.");
					continue;
				}
				break;
			}

			int result = empDao.updateEmployee(empNo, "EMP_STAT_CD", "R");

			if (result > 0)
				System.out.println("퇴직 승인이 완료되었습니다.");
			else
				System.out.println("해당 사원번호가 존재하지 않습니다.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 사원관리 - 경력등록 */
	protected void insertCareerInfo() {
		System.out.println("\n[관리자 - 사원관리 - 경력등록]");
		try {
			CareerDTO dto = new CareerDTO();
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. 예) D1001 형식으로 입력해주세요.");
					continue;
				}
				break;
			}
	        System.out.print("회사명: ");
	        dto.setCompanyName(br.readLine());

	        System.out.print("근무시작일(YYYY-MM-DD): ");
	        dto.setStartDt(br.readLine());
	        System.out.print("근무종료일(YYYY-MM-DD): ");
	        dto.setEndDt(br.readLine());
	        System.out.print("상세: ");
	        dto.setDetails(br.readLine());
	        
			empDao.insertCareer(dto);
			System.out.println("경력 등록이 완료되었습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 사원관리 - 자격증등록 */
	protected void insertLicenseInfo() {
		System.out.println("\n[관리자 - 사원관리 - 자격증등록]");
		try {
			RewardDTO dto = new RewardDTO();
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. 예) D1001 형식으로 입력해주세요.");
					continue;
				}
				break;
			}
			dto.setEmpNo(empNo);

			System.out.print("자격증명: ");
			dto.setRewardName(br.readLine());
			System.out.print("발급기관: ");
			dto.setIssuer(br.readLine());
			System.out.print("취득일(YYYY-MM-DD): ");
			dto.setDate(br.readLine());

			empDao.insertLicense(dto);
			System.out.println("자격증 등록이 완료되었습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 사원관리 - 이력조회 */
	protected void selectHistoryInfo() {
		System.out.println("\n[관리자 - 사원관리 - 이력조회]");
		try {
			String empNo;
			while (true) {
				System.out.print("사원번호(ex. D1001): ");
				empNo = br.readLine();
				if (!empNo.matches("^D\\d{4}$")) {
					System.out.println("잘못된 형식입니다. 예) D1001 형식으로 입력해주세요.");
					continue;
				}
				break;
			}
			empDao.selectHistory(empNo);
			System.out.println("이력 조회가 완료되었습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}