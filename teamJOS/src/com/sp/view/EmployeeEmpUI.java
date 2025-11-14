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
//import com.sp.model.LoginDTO;
import com.sp.model.RetireDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;

public class EmployeeEmpUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private EmpDAO empDao;
//	private LoginDTO loginDTO;
//	private Object careerList;
	private LoginInfo loginInfo;

	public EmployeeEmpUI(EmpDAO empDao, LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
		this.empDao = new EmpDAOImpl(this.loginInfo);
	}

	/** 메인 메뉴 */
	public void menu() {
		int ch;
		System.out.println("\n[사원관리]");
		System.out.println("======================================================================================");
		System.out.println("                         [ 사원 - 내 정보 관리 메뉴 ]");
		try {
			do {
				System.out.println("""
						======================================================================================
						  1. 내정보조회     2. 내정보수정     3. 직급이동이력조회     4. 이력조회     5.퇴직신청     6. 상위메뉴
						======================================================================================""");

				System.out.print("선택 [q: 상위메뉴] ➤ ");
				String s = br.readLine();
				InputValidator.isUserExit(s);
				ch = Integer.parseInt(s);
				System.out.println();

				switch (ch) {
				case 1 -> selectMyInfo();
				case 2 -> updateMyInfo();
				case 3 -> selectMyGradeHistory();
				case 4 -> selectMyAllHistory();
				case 5 -> insertRetire();
				case 6 -> {
					System.out.println("상위 메뉴로 돌아갑니다.");
					return;
				}
				default -> System.out.println("잘못된 입력입니다. 1~5 사이의 숫자를 입력해주세요.");
				}

			} while (ch != 5);

		} catch (UserQuitException e) {
			System.out.println("\n사원 - 내 정보 관리 메뉴를 종료합니다.\n");
			return;
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

			// null 처리 + 주소 앞 두 단어만
			String regDt = dto.getRegDt() == null ? "-" : dto.getRegDt();
			String level = dto.getLevelCode() == null ? "-" : dto.getLevelCode();
			String addr = getFirstTwoWords(dto.getEmpAddr()); // 주소 앞 두 단어만

			PrintUtil.printLine('=', 200);
			System.out.println(PrintUtil.padCenter("사원 - 내 정보 조회", 200));
			PrintUtil.printLine('=', 200);

			// ───── 헤더 (한글 폭 기준 정렬) ─────
			System.out.printf("%s\t| %s\t| %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s%n",
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
					PrintUtil.padCenterDisplay("권한", 8));
			PrintUtil.printLine('-', 200);

			// ───── 데이터 1행 (목록이랑 동일 포맷) ─────
			System.out.printf("%s\t| %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s\t | %s%n",
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
					PrintUtil.padRightDisplay(level, 8));

			PrintUtil.printLine('=', 200);
			System.out.println();

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

			System.out.print("선택 [q: 돌아가기] ➤ ");
			String s = br.readLine();
			InputValidator.isUserExit(s);
			int ch = Integer.parseInt(s);
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

			System.out.print("변경할 값 입력([q: 돌아가기]) ➤ ");
			String val = br.readLine();
			InputValidator.isUserExit(val);

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

		} catch (UserQuitException e) {
			System.out.println("\n수정을 취소하고 상위 메뉴로 돌아갑니다.\n");
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

	            // 컬럼 폭
	            final int W_DATE   = 15; // 진급일자
	            final int W_OLD    = 15; // 이전 직급
	            final int W_NEW    = 15; // 신규 직급
	            final int W_REASON = 25; // 진급 사유

	            PrintUtil.printLine('=', 80);
	            System.out.printf("%s\t| %s\t| %s\t| %s%n",
	                    PrintUtil.padCenterDisplay("진급일자",  W_DATE),
	                    PrintUtil.padCenterDisplay("이전 직급", W_OLD),
	                    PrintUtil.padCenterDisplay("신규 직급", W_NEW),
	                    PrintUtil.padCenterDisplay("진급 사유", W_REASON));
	            PrintUtil.printLine('-', 80);

	            for (HistoryDTO dto : list) {
	                System.out.printf("%s\t| %s\t| %s\t| %s%n",
	                        PrintUtil.padRightDisplay(dto.getStartDt(), W_DATE),   // 진급일자
	                        PrintUtil.padRightDisplay(dto.getDeptNm(),  W_OLD),    // 이전 직급명(지금 이렇게 쓰고 있었음)
	                        PrintUtil.padRightDisplay(dto.getGradeNm(), W_NEW),    // 신규 직급명
	                        PrintUtil.padRightDisplay(dto.getDetails(), W_REASON)  // 진급 사유
	                );
	            }

	            PrintUtil.printLine('=', 80);

	        } catch (Exception e) {
	            System.out.println("예상치 못한 오류가 발생했습니다.");
	            e.printStackTrace();
	        }
	    }

	  /** 4. 전체 이력 조회 */
		private void selectMyAllHistory() {
			System.out.println("\n[사원 - 전체 이력 조회]");

			final int LINE_WIDTH = 120;

			try {
				String empNo = loginInfo.loginMember().getMemberId();

				// ==================== 경력 이력 ====================
				List<HistoryDTO> careerList = empDao.selectCareerHis(empNo);

				if (careerList != null && !careerList.isEmpty()) {

					final int W_STRT = 12; // 시작일자
					final int W_END = 12; // 종료일자
					final int W_DEPT = 25; // 부서명(또는 회사명)
					final int W_DETAIL = 45; // 상세내용

					PrintUtil.printLine('=', LINE_WIDTH);
					System.out.println(PrintUtil.padCenter(" [ 경력 이력 ] ", LINE_WIDTH));
					PrintUtil.printLine('=', LINE_WIDTH);

					// 헤더
					System.out.printf("%s | %s | %s | %s%n", 
							PrintUtil.padCenterDisplay("시작일자", W_STRT),
							PrintUtil.padCenterDisplay("종료일자", W_END), 
							PrintUtil.padCenterDisplay("부서명", W_DEPT),
							PrintUtil.padCenterDisplay("상세내용", W_DETAIL));
					PrintUtil.printLine('-', LINE_WIDTH);

					// 데이터
					for (HistoryDTO dto : careerList) {
						System.out.printf("%s | %s | %s | %s%n", 
								PrintUtil.padRightDisplay(dto.getStartDt(), W_STRT),
								PrintUtil.padRightDisplay(dto.getEndDt(), W_END),
								PrintUtil.padRightDisplay(dto.getDeptNm(), W_DEPT),
								PrintUtil.padRightDisplay(dto.getDetails(), W_DETAIL));
					}
					PrintUtil.printLine('=', LINE_WIDTH);
					System.out.println();
				} else {
					System.out.println("등록된 경력 이력이 없습니다.");
				}

				// ==================== 자격증 이력 ====================
				List<HistoryDTO> certList = empDao.selectCertHis(empNo);

				if (certList != null && !certList.isEmpty()) {

					final int W_REG = 12; // 등록일자
					final int W_CERT = 25; // 자격증명
					final int W_DETAIL = 45; // 상세내용

					PrintUtil.printLine('=', LINE_WIDTH);
					System.out.println(PrintUtil.padCenter(" [ 자격증 이력 ] ", LINE_WIDTH));
					PrintUtil.printLine('=', LINE_WIDTH);

					// 헤더
					System.out.printf("%s | %s | %s%n", 
							PrintUtil.padCenterDisplay("등록일자", W_REG),
							PrintUtil.padCenterDisplay("자격증", W_CERT), 
							PrintUtil.padCenterDisplay("상세내용", W_DETAIL));
					PrintUtil.printLine('-', LINE_WIDTH);

					// 데이터
					for (HistoryDTO dto : certList) {
						System.out.printf("%s | %s | %s%n", 
								PrintUtil.padRightDisplay(dto.getRegDt(), W_REG),
								PrintUtil.padRightDisplay(dto.getGradeNm(), W_CERT),
								PrintUtil.padRightDisplay(dto.getDetails(), W_DETAIL));
					}
					PrintUtil.printLine('=', LINE_WIDTH);
					System.out.println();
				} else {
					System.out.println("등록된 자격증이 없습니다.");
				}

				// ==================== 기본 사원 정보 ====================
				EmployeeDTO empInfo = empDao.selectByEmpNo(empNo);
				if (empInfo != null) {

					final int INFO_WIDTH = 120;
					PrintUtil.printLine('=', INFO_WIDTH);
					System.out.println(PrintUtil.padCenter(" [ 기본 사원 정보 ] ", INFO_WIDTH));
					PrintUtil.printLine('=', INFO_WIDTH);

					// 1행 : 사번 / 이름 / 부서 / 직급
					System.out.printf("%s | %s | %s | %s%n",
							PrintUtil.padRightDisplay("사원번호: " + empInfo.getEmpNo(), 28),
							PrintUtil.padRightDisplay("이름: " + empInfo.getEmpNm(), 20),
							PrintUtil.padRightDisplay("부서: " + empInfo.getDeptNm(), 30),
							PrintUtil.padRightDisplay("직급: " + empInfo.getGradeNm(), 20));

					// 2행 : 입사일자 / 계약구분
					System.out.printf("%s | %s%n", PrintUtil.padRightDisplay("입사일자: " + empInfo.getHireDt(), 28),
							PrintUtil.padRightDisplay("계약구분: " + empInfo.getContractTpNm(), 30));

					PrintUtil.printLine('=', INFO_WIDTH);
				}

				System.out.println("\n이력 조회가 완료되었습니다.\n");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	  
	  /** 5. 퇴직 신청 */
	  private void insertRetire() {
		System.out.println("\n[퇴직신청]");
		RetireDTO dto = new RetireDTO();

		try {
			System.out.print("퇴직일자 ? [q: 취소] ➤ ");
			String regDt = br.readLine();
			InputValidator.isUserExit(regDt);
			dto.setRegDt(regDt);

			System.out.print("퇴직사유 ? [q: 취소] ➤ ");
			String memo = br.readLine();
			InputValidator.isUserExit(memo);
			dto.setRetireMemo(memo);

			empDao.insertRetire(dto);

			System.out.println("퇴직 신청 완료!");
		} catch (UserQuitException e) {
			System.out.println("\n퇴직 신청을 취소했습니다.\n");
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
		String[] parts = addr.trim().split("\\s+"); // ← 역슬래시 두 개
		if (parts.length >= 2) {
			return parts[0] + " " + parts[1];
		} else {
			return addr.trim();
		}
	}
}
