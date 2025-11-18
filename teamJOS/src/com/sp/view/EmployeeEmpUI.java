package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.sp.dao.EmpDAO;
import com.sp.dao.impl.EmpDAOImpl;
import com.sp.exception.UserQuitException;
import com.sp.model.EmployeeDTO;
import com.sp.model.HistoryDTO;
import com.sp.model.RetireDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;

import static com.sp.util.PrintUtil.*;

/**
 * <h2>EmployeeEmpUI (사원 - 내 정보 관리 UI)</h2>
 * <p>사원이 자신의 정보를 조회, 수정하고, 직급/이력 조회 및 퇴직 신청을 할 수 있는 사용자 인터페이스 클래스입니다.</p>
 *
 * <ul>
 *   <li>메인 메뉴 제공 <!-- 서비스 번호: EMP_SEL_005~EMP_INS_013 범위 포함 --> </li>
 *   <li>내 정보 조회 <!-- EMP_SEL_005 --> </li>
 *   <li>내 정보 수정 <!-- EMP_UPD_002 --> </li>
 *   <li>직급(진급) 이력 조회 <!-- EMP_SEL_011 --> </li>
 *   <li>전체 이력 조회 <!-- EMP_SEL_012 --> </li>
 *   <li>퇴직 신청 <!-- EMP_INS_013 --> </li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> 사원관리 시스템</p>
 * <p><b>작성자:</b> 이지영, 오다은</p>
 * <p><b>작성일:</b> 2025-11-18</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class EmployeeEmpUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private EmpDAO empDao;
	private LoginInfo loginInfo;

	/**
	 * 생성자
	 * 
	 * @param empDao    EmpDAO 객체
	 * @param loginInfo 로그인 정보 객체
	 */
	public EmployeeEmpUI(EmpDAO empDao, LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
		this.empDao = new EmpDAOImpl(this.loginInfo);
	}

	/**
	 * 메인 메뉴
	 */
	public void menu() {
		int ch;
		
		try {
			do {
				printTitle("🏢 [ 사원 - 내 정보 관리 메뉴 ] ");
				printMenu(YELLOW, "① 내정보 조회", "② 내정보 수정", "③ 직급 이동 이력 조회", "④ 이력 조회", "⑤ 퇴직 신청");

				String s = br.readLine();
				InputValidator.isUserExit(s);
				ch = Integer.parseInt(s);
				System.out.println();

				switch (ch) {
				case 1 -> selectMyInfo(); // EMP_SEL_005
				case 2 -> updateMyInfo(); // EMP_UPD_002
				case 3 -> selectMyGradeHistory(); // EMP_SEL_011
				case 4 -> selectMyAllHistory(); // EMP_SEL_012
				case 5 -> insertRetire(); // EMP_INS_013
				default -> printLineln(MAGENTA, "📢 잘못된 입력입니다. 1~5 사이의 숫자를 입력해주세요.");
				}

			} while (ch != 5);

		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 \n사원 - 내 정보 관리 메뉴를 종료합니다.\n");
			return;
		} catch (Exception e) {
			printLineln(MAGENTA, "📢 오류가 발생했습니다. " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 1. 내 정보 조회
	 * <p>로그인한 사원의 정보를 조회하고 화면에 출력합니다.</p>
	 * <p>서비스 번호: EMP_SEL_005</p>
	 * 
	 * @throws SQLException SQL 예외
	 */
	private void selectMyInfo() throws SQLException {
		try {
			String empNo = loginInfo.loginMember().getMemberId();
			EmployeeDTO dto = empDao.selectByEmpNo(empNo);

			if (dto == null) {
				printLineln(MAGENTA, "📢 내 정보가 존재하지 않습니다.\n");
				return;
			}

			String regDt = dto.getRegDt() == null ? "-" : dto.getRegDt();
			String level = dto.getLevelCode() == null ? "-" : dto.getLevelCode();
			String addr = getFirstTwoWords(dto.getEmpAddr());

			PrintUtil.printLine('═', 200);
			System.out.println(padCenter("사원 - 내 정보 조회", 200));
			PrintUtil.printLine('═', 200);

			System.out.printf("%s\t| %s\t| %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s%n",
					padCenterDisplay("사번", 6), 
					padCenterDisplay("이름", 8),
					padCenterDisplay("주민번호", 16), 
					padCenterDisplay("주소", 22),
					padCenterDisplay("입사일", 10), 
					padCenterDisplay("부서명", 10),
					padCenterDisplay("직급", 8), 
					padCenterDisplay("재직", 4),
					padCenterDisplay("계약", 4), 
					padCenterDisplay("이메일", 16),
					padCenterDisplay("비밀번호", 8), 
					padCenterDisplay("등록일", 10),
					padCenterDisplay("권한", 8));
			PrintUtil.printLine('─', 200);

			System.out.printf("%s\t| %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s%n",
					padRightDisplay(dto.getEmpNo(), 6), 
					padRightDisplay(dto.getEmpNm(), 8),
					padRightDisplay(dto.getRrn(), 12), 
					padRightDisplay(addr, 24),
					padRightDisplay(dto.getHireDt(), 10), 
					padRightDisplay(dto.getDeptNm(), 10),
					padRightDisplay(dto.getGradeNm(), 8), 
					padRightDisplay(dto.getEmpStatNm(), 6),
					padRightDisplay(dto.getContractTpNm(), 4), 
					padRightDisplay(dto.getEmail(), 16),
					padRightDisplay(dto.getPwd(), 8), 
					padRightDisplay(regDt, 10),
					padRightDisplay(level, 8));

			printLine('═', 200);
			System.out.println();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 2. 내 정보 수정
	 * <p>주소, 이메일, 비밀번호를 수정할 수 있습니다.</p>
	 * <p>서비스 번호: EMP_UPD_002</p>
	 */
	private void updateMyInfo() {
		printTitle("🏢 [사원 - 내 정보 수정]");
		try {
			String empNo = loginInfo.loginMember().getMemberId();
			printMenu(YELLOW, "① 주소", "② 이메일", "③ 비밀번호");

			String s = br.readLine();
			InputValidator.isUserExit(s);
			int ch = Integer.parseInt(s);

			String col = switch (ch) {
			case 1 -> "EMP_ADDR";
			case 2 -> "EMAIL";
			case 3 -> "PWD";
			default -> null;
			};

			if (col == null) {
				printLineln(MAGENTA, "📢 잘못된 입력입니다\n");
				return;
			}
			printLine(GREEN, "👉 변경할 값을 입력하세요. [ q: 돌아가기 ] : ");
			String val = br.readLine();
			InputValidator.isUserExit(val);

			if (col.equals("EMAIL") && !InputValidator.isValidEmail(val)) {
				printLineln(MAGENTA, "📢 잘못된 이메일 형식입니다.");
				return;
			}

			if (col.equals("PWD") && !InputValidator.isNotEmpty(val)) {
				printLineln(MAGENTA, "📢 비밀번호는 비워둘 수 없습니다.");
				return;
			}
			empDao.updateEmployee(empNo, col, val);
			printLineln(MAGENTA, "📢 내 정보 수정이 완료되었습니다.");

		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 수정을 취소하고 상위 메뉴로 돌아갑니다.");
		} catch (IOException e) {
			printLineln(MAGENTA, "📢 입력 오류가 발생하였습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 3. 직급(진급) 이력 조회
	 * <p>사원의 직급 변경 이력을 조회합니다.</p>
	 * <p>서비스 번호: EMP_SEL_011</p>
	 */
	private void selectMyGradeHistory() {
		printTitle("🏢 [사원 - 직급(진급) 이력 조회]");
		System.out.println("\n");
		try {
			String empNo = loginInfo.loginMember().getMemberId();
			List<HistoryDTO> list = empDao.selectGradeHis(empNo);

			if (list == null || list.isEmpty()) {
				printLineln(MAGENTA, "📢 진급 이력이 없습니다.");
				return;
			}

			final int W_DATE = 15;
			final int W_OLD = 15;
			final int W_NEW = 15;
			final int W_REASON = 25;

			PrintUtil.printLine('═', 80);
			System.out.printf("%s\t| %s\t| %s\t| %s%n",
					PrintUtil.padCenterDisplay("진급일자", W_DATE),
					PrintUtil.padCenterDisplay("이전 직급", W_OLD),
					PrintUtil.padCenterDisplay("신규 직급", W_NEW),
					PrintUtil.padCenterDisplay("진급 사유", W_REASON));
			PrintUtil.printLine('─', 80);

			for (HistoryDTO dto : list) {
				System.out.printf("%s\t| %s\t| %s\t| %s%n",
						PrintUtil.padRightDisplay(dto.getStartDt(), W_DATE),
						PrintUtil.padRightDisplay(dto.getDeptNm(), W_OLD),
						PrintUtil.padRightDisplay(dto.getGradeNm(), W_NEW),
						PrintUtil.padRightDisplay(dto.getDetails(), W_REASON));
			}

			PrintUtil.printLine('═', 80);

		} catch (Exception e) {
			printLineln(MAGENTA, "📢 예상치 못한 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	/**
	 * 4. 전체 이력 조회
	 * <p>경력, 자격증, 기본 사원 정보를 모두 조회합니다.</p>
	 * <p>서비스 번호: EMP_SEL_012</p>
	 */
	private void selectMyAllHistory() {
		printTitle("🏢 [사원 - 전체 이력 조회]");

		final int LINE_WIDTH = 120;

		try {
			String empNo = loginInfo.loginMember().getMemberId();

			// 경력 이력
			List<HistoryDTO> careerList = empDao.selectCareerHis(empNo);

			if (careerList != null && !careerList.isEmpty()) {
				final int W_STRT = 12;
				final int W_END = 12;
				final int W_DEPT = 25;
				final int W_DETAIL = 45;

				PrintUtil.printLine('═', LINE_WIDTH);
				System.out.println(PrintUtil.padCenter(" [ 경력 이력 ] ", LINE_WIDTH));
				PrintUtil.printLine('═', LINE_WIDTH);

				System.out.printf("%s | %s | %s | %s%n", 
						PrintUtil.padCenterDisplay("시작일자", W_STRT),
						PrintUtil.padCenterDisplay("종료일자", W_END), 
						PrintUtil.padCenterDisplay("부서명", W_DEPT),
						PrintUtil.padCenterDisplay("상세내용", W_DETAIL));
				PrintUtil.printLine('─', LINE_WIDTH);

				for (HistoryDTO dto : careerList) {
					System.out.printf("%s | %s | %s | %s%n", 
							PrintUtil.padRightDisplay(dto.getStartDt(), W_STRT),
							PrintUtil.padRightDisplay(dto.getEndDt(), W_END),
							PrintUtil.padRightDisplay(dto.getDeptNm(), W_DEPT),
							PrintUtil.padRightDisplay(dto.getDetails(), W_DETAIL));
				}
				PrintUtil.printLine('═', LINE_WIDTH);
				System.out.println();
			} else {
				printLineln(MAGENTA, "📢 등록된 경력 이력이 없습니다.");
			}

			// 자격증 이력
			List<HistoryDTO> certList = empDao.selectCertHis(empNo);

			if (certList != null && !certList.isEmpty()) {
				final int W_REG = 12;
				final int W_CERT = 25;
				final int W_DETAIL = 45;

				PrintUtil.printLine('═', LINE_WIDTH);
				System.out.println(PrintUtil.padCenter(" [ 자격증 이력 ] ", LINE_WIDTH));
				PrintUtil.printLine('═', LINE_WIDTH);

				System.out.printf("%s | %s | %s%n", 
						PrintUtil.padCenterDisplay("등록일자", W_REG),
						PrintUtil.padCenterDisplay("자격증", W_CERT), 
						PrintUtil.padCenterDisplay("상세내용", W_DETAIL));
				PrintUtil.printLine('-', LINE_WIDTH);

				for (HistoryDTO dto : certList) {
					System.out.printf("%s | %s | %s%n", 
							PrintUtil.padRightDisplay(dto.getRegDt(), W_REG),
							PrintUtil.padRightDisplay(dto.getGradeNm(), W_CERT),
							PrintUtil.padRightDisplay(dto.getDetails(), W_DETAIL));
				}
				PrintUtil.printLine('═', LINE_WIDTH);
				System.out.println();
			} else {
				printLineln(MAGENTA, "📢 등록된 자격증이 없습니다.");
			}

			// 기본 사원 정보
			EmployeeDTO empInfo = empDao.selectByEmpNo(empNo);
			if (empInfo != null) {

				final int INFO_WIDTH = 120;
				PrintUtil.printLine('═', INFO_WIDTH);
				System.out.println(PrintUtil.padCenter(" [ 기본 사원 정보 ] ", INFO_WIDTH));
				PrintUtil.printLine('═', INFO_WIDTH);

				System.out.printf("%s | %s | %s | %s%n",
						PrintUtil.padRightDisplay("사원번호: " + empInfo.getEmpNo(), 28),
						PrintUtil.padRightDisplay("이름: " + empInfo.getEmpNm(), 20),
						PrintUtil.padRightDisplay("부서: " + empInfo.getDeptNm(), 30),
						PrintUtil.padRightDisplay("직급: " + empInfo.getGradeNm(), 20));

				System.out.printf("%s | %s%n", PrintUtil.padRightDisplay("입사일자: " + empInfo.getHireDt(), 28),
						PrintUtil.padRightDisplay("계약구분: " + empInfo.getContractTpNm(), 30));

				PrintUtil.printLine('═', INFO_WIDTH);
			}

			printLineln(MAGENTA, "📢 이력 조회가 완료되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 5. 퇴직 신청
	 * <p>퇴직 희망일자와 사유를 입력하여 퇴직 신청을 합니다.</p>
	 * <p>서비스 번호: EMP_INS_013</p>
	 */
	private void insertRetire() {
		printTitle("🏢 [퇴직신청]");
		RetireDTO dto = new RetireDTO();

		try {
			printLine(GREEN, "👉 희망하는 퇴직 일자를 입력하세요. [q : 취소] : ");
			String regDt = br.readLine();
			InputValidator.isUserExit(regDt);
			dto.setRegDt(regDt);

			printLine(GREEN, "👉 퇴직 사유를 입력하세요. [q : 취소] : ");
			String memo = br.readLine();
			InputValidator.isUserExit(memo);
			dto.setRetireMemo(memo);

			empDao.insertRetire(dto);

			printLineln(MAGENTA, "📢 퇴직 신청이 완료되었습니다.");
		} catch (UserQuitException e) {
			printLineln(MAGENTA, "📢 퇴직 신청을 취소했습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
}
