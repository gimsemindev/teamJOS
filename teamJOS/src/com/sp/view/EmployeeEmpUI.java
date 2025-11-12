package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.sp.dao.EmpDAO;
import com.sp.model.EmployeeDTO;
import com.sp.model.HistoryDTO;
import com.sp.model.LoginDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;

public class EmployeeEmpUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private EmpDAO empDao;
	private LoginDTO loginDTO;
	private Object careerList;
	private LoginInfo loginInfo;

	public EmployeeEmpUI(EmpDAO empDao, LoginInfo loginInfo) {
		this.empDao = empDao;
		this.loginInfo = loginInfo;
//		this.loginDTO = loginInfo.getLoginDTO();
	}

	/** 메인 메뉴 */
	public void menu() {
		int ch;
		System.out.println("\n[사원관리]");
		System.out.println("==========================================================================");
		System.out.println("                         [ 사원 - 내 정보 관리 메뉴 ]");
		try {
			do {
				System.out.println("""
						==========================================================================
						  1. 내정보조회     2. 내정보수정     3. 직급이동이력조회     4. 이력조회     5. 상위메뉴
						==========================================================================""");

				System.out.print("선택 ➤ ");
				ch = Integer.parseInt(br.readLine());
				System.out.println();

				switch (ch) {
				case 1 -> selectMyInfo();
				case 2 -> updateMyInfo();
				case 3 -> selectMyGradeHistory();
				case 4 -> selectMyAllHistory();
				case 5 -> {
					System.out.println("상위 메뉴로 돌아갑니다.");
					return;
				}
				default -> System.out.println("잘못된 입력입니다. 1~5 사이의 숫자를 입력해주세요.");
				}

			} while (ch != 5);

		} catch (Exception e) {
			System.out.println("오류가 발생했습니다: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/** 1. 내 정보 조회 */
	private void selectMyInfo() throws SQLException {
		System.out.println("\n[사원 - 내 정보 조회]");
		try {
			String empNo = loginInfo.loginMember().getMemberId();
			EmployeeDTO dto = empDao.selectByEmpNo(empNo);

			if (dto == null) {
				System.out.println("내 정보가 존재하지 않습니다.\n");
				return;
			}

			PrintUtil.printLine('-', 70);
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
			System.out.println(dto.getLevelCode());
			PrintUtil.printLine('-', 70);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 2. 내 정보 수정 */
	private void updateMyInfo() {
		System.out.println("\n[사원 - 내 정보 수정]");
		try {
			String empNo = loginInfo.loginMember().getMemberId();

			System.out.println("""
					=====================================================
					                   [수정할 항목 선택]
							 1.주소 | 2.이메일 | 3.비밀번호 | 4.상위메뉴
					=====================================================
					""");

			System.out.print("선택 ➤ ");
			int ch = Integer.parseInt(br.readLine());
			if (ch == 4)
				return;

			String col = switch (ch) {
			case 1 -> "EMP_ADDR";
			case 2 -> "EMAIL";
			case 3 -> "PWD";
			default -> null;
			};

			if (col == null) {
				System.out.println("잘못된 입력입니다\n");
				return;
			}

			System.out.print("변경할 값 입력 ➤ ");
			String val = br.readLine();

			if (col.equals("EMAIL") && !InputValidator.isValidEmail(val)) {
				System.out.println("잘못된 이메일 형식입니다.");
				return;
			}

			if (col.equals("PWD") && !InputValidator.isNotEmpty(val)) {
				System.out.println("비밀번호는 비워둘 수 없습니다.");
				return;
			}
			empDao.updateEmployee(empNo, col, val);
			System.out.println("\n내 정보 수정이 완료되었습니다.\n");

		} catch (IOException e) {
			System.out.println("입력 오류가 발생하였습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 3. 직급(진급) 이력 조회 */
	private void selectMyGradeHistory() {
		System.out.println("\n[사원 - 직급(진급) 이력 조회]");
		try {
			String empNo = loginInfo.loginMember().getMemberId();
			List<HistoryDTO> list = empDao.selectGradeHis(empNo);

			if (list == null || list.isEmpty()) {
				System.out.println("진급 이력이 없습니다.\n");
				return;
			}

			PrintUtil.printLine('-', 70);
			System.out.println(PrintUtil.padCenter("진급일자", 15) + PrintUtil.padCenter("이전 직급", 15)
					+ PrintUtil.padCenter("신규 직급", 15) + PrintUtil.padCenter("진급 사유", 25));
			PrintUtil.printLine('-', 70);

			for (HistoryDTO dto : list) {
				System.out.println(PrintUtil.padCenter(dto.getStartDt(), 15) + // 진급일자
						PrintUtil.padCenter(dto.getDeptNm(), 15) + // 이전 직급명
						PrintUtil.padCenter(dto.getGradeNm(), 15) + // 신규 직급명
						PrintUtil.padCenter(dto.getDetails(), 25) // 진급 사유
				);
			}

			PrintUtil.printLine('-', 70);

		} catch (Exception e) {
			System.out.println("예상치 못한 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	/** 4. 전체 이력 조회 */
	private void selectMyAllHistory() {
		System.out.println("\n[사원 - 전체 이력 조회]");

		try {
			String empNo = loginInfo.loginMember().getMemberId();
			List<HistoryDTO> careerList = empDao.selectCareerHis(empNo);

			if (careerList != null && !careerList.isEmpty()) {
				PrintUtil.printSection("경력이력");
				PrintUtil.printLine('-', 80);
				System.out.println(PrintUtil.padCenter("시작일자", 15) + PrintUtil.padCenter("종료일자", 15)
						+ PrintUtil.padCenter("부서명", 20) + PrintUtil.padCenter("상세내용", 25));
				PrintUtil.printLine('-', 80);
				PrintUtil.printLine('-', 80);

				for (HistoryDTO dto : careerList) {
					System.out.println(PrintUtil.padCenter(dto.getStartDt(), 15)
							+ PrintUtil.padCenter(dto.getEndDt(), 15) + PrintUtil.padCenter(dto.getDeptNm(), 20)
							+ PrintUtil.padCenter(dto.getDetails(), 25));
				}
				PrintUtil.printLine('-', 80);
			} else {
				System.out.println("등록된 경력이력이 없습니다.");
			}
			List<HistoryDTO> certList = empDao.selectCertHis(empNo);

			if (certList != null && !certList.isEmpty()) {
				PrintUtil.printSection("자격증 이력");
				PrintUtil.printLine('-', 80);
				System.out.println(PrintUtil.padCenter("등록일자", 15) + PrintUtil.padCenter("자격증", 25)
						+ PrintUtil.padCenter("상세내용", 25));
				PrintUtil.printLine('-', 80);

				for (HistoryDTO dto : certList) {
					System.out.println(PrintUtil.padCenter(dto.getRegDt(), 15)
							+ PrintUtil.padCenter(dto.getGradeNm(), 25) + PrintUtil.padCenter(dto.getDetails(), 25));

				}
				PrintUtil.printLine('-', 80);
			} else {
				System.out.println("등록된 자격증이 없습니다.");
			}
			EmployeeDTO empInfo = empDao.selectByEmpNo(empNo);
			if (empInfo != null) {
				PrintUtil.printSection("기본 사원 정보");
				System.out.printf("사원번호: %s | 이름: %s | 부서: %s | 직급: %s%n", empInfo.getEmpNo(), empInfo.getEmpNm(),
						empInfo.getDeptNm(), empInfo.getGradeNm());
				System.out.printf("입사일자: %s | 계약구분: %s%n", empInfo.getHireDt(), empInfo.getContractTpNm());
				PrintUtil.printLine('-', 80);
			}

			System.out.println("\n전체 이력 조회가 완료되었습니다.\n");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
