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

                System.out.print("선택 [q: 돌아가기] ➤ ");
                String input = br.readLine();
                InputValidator.isUserExit(input);
                ch = Integer.parseInt(input.trim());
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
                    default -> System.out.println("잘못된 입력입니다. 1~11 사이의 숫자를 입력해주세요.\n");
                }

            } while (ch != 11);

        } catch (UserQuitException e) {
            System.out.println("\n메뉴 선택을 취소하고 상위 메뉴로 돌아갑니다.\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("메뉴 처리 중 오류가 발생했습니다.\n");
        }
    }

    /** 1. 사원관리 - 사원 정보 등록 */
    public void insertEmployeeInfo() {
        PrintUtil.printTitle("관리자 - 사원관리 - 사원정보등록");
        EmployeeDTO dto = new EmployeeDTO();

        try {
            // ==================== 사원번호 ====================
            String empNo = checkEmpNo(false);   // 신규 등록이므로 존재하면 안 됨
            dto.setEmpNo(empNo);
            System.out.println();

            // ==================== 이름 ====================
            System.out.print("이름 입력 [q: 돌아가기] ➤ ");
            String name = br.readLine();
            InputValidator.isUserExit(name);
            dto.setEmpNm(name);
            System.out.println();

            // ==================== 주민등록번호 ====================
            while (true) {
                System.out.print("주민번호('-' 제외 13자리, 예: 0101013456789, [q: 돌아가기]) ➤ ");
                String rrn = br.readLine();
                InputValidator.isUserExit(rrn);

                if (!InputValidator.isValidRRN(rrn)) {
                    System.out.println("형식 오류: 13자리 숫자로 입력해주세요. (예: 0101013456789)\n");
                    continue;
                }
                dto.setRrn(rrn);
                break;
            }
            System.out.println();

            // ==================== 주소 ====================
            System.out.print("주소 입력 [q: 돌아가기] ➤ ");
            String addr = br.readLine();
            InputValidator.isUserExit(addr);
            dto.setEmpAddr(addr);
            System.out.println();

            // ==================== 부서 코드 ====================
            String deptCd;
            while (true) {
                PrintUtil.printSection("부서 코드");
                deptCommonUI.selectAllDept();
                System.out.print("부서코드 입력 [예: D10000, q: 돌아가기] ➤ ");
                deptCd = br.readLine();
                InputValidator.isUserExit(deptCd);

                if (!InputValidator.isValidDeptCode(deptCd)) {
                    // isValidDeptCode 내부에서 안내 문구 출력
                    continue;
                }

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
                System.out.print("직급코드 입력 [예: 01, q: 돌아가기] ➤ ");
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

            // ==================== 사원 상태 (기본값 A) ====================
            dto.setEmpStatCd("A");
            PrintUtil.printSection("사원 상태");
            System.out.println("신규 등록 사원은 기본적으로 재직 상태(A)로 설정됩니다.");
            System.out.print("계속하려면 엔터를 누르세요. ");
            br.readLine();
            System.out.println();

            // ==================== 계약구분 코드 ====================
            String contractCd;
            while (true) {
                PrintUtil.printSection("계약구분 코드");
                System.out.println("1. 정규직   |   2. 계약직   |   3. 인턴");
                System.out.print("계약구분코드 입력 [1~3, q: 돌아가기] ➤ ");
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
                System.out.print("이메일 입력 [예: example@domain.com, q: 돌아가기] ➤ ");
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
                System.out.print("비밀번호 입력 [q: 돌아가기] ➤ ");
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
                System.out.print("레벨코드 입력 [예: 01, q: 돌아가기] ➤ ");
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
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB 처리 중 오류가 발생했습니다.\n");
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
            System.out.print("선택 [q: 돌아가기] ➤ ");
            String sel = br.readLine();
            InputValidator.isUserExit(sel);
            int ch = Integer.parseInt(sel.trim());
            if (ch == 6) return;

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

            System.out.print("변경할 값 입력 [q: 돌아가기] ➤ ");
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
            EmployeeDTO emp = empDao.selectDeptName(empNo); // DEPT_CD, DEPT_NM, EMP_NM

            PrintUtil.printSection("현재 부서 정보");
            System.out.println("사원명       : " + nvl(emp.getEmpNm()));
            System.out.println("현재 부서코드: " + nvl(emp.getDeptCd()));
            System.out.println("현재 부서명  : " + nvl(emp.getDeptNm()));
            PrintUtil.printLine('-', 65);

            PrintUtil.printSection("이동할 부서 선택");
            deptCommonUI.selectAllDept();

            String newDeptCd;
            while (true) {
                System.out.print("이동할 부서코드 입력 [예: D10000, q: 돌아가기] ➤ ");
                newDeptCd = br.readLine();
                InputValidator.isUserExit(newDeptCd);

                if (!InputValidator.isValidDeptCode(newDeptCd)) {
                    continue;
                }
                if (!empDao.isValidDeptCd(newDeptCd)) {
                    System.out.println("존재하지 않는 부서 코드입니다.\n");
                    continue;
                }
                if (newDeptCd.equals(emp.getDeptCd())) {
                    System.out.println("현재 부서와 동일합니다. 다른 부서코드를 선택해주세요.\n");
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
            System.out.println("부서 이동 처리 중 오류가 발생했습니다.\n");
        }
    }

    /** 4. 사원관리 - 진급 관리 */
    private void updatePromotionInfo() {
        PrintUtil.printTitle("관리자 - 사원관리 - 진급관리");
        try {
            String empNo = checkEmpNo(true);
            EmployeeDTO emp = empDao.selectDeptName(empNo); // 현재 부서/직급 등

            PrintUtil.printSection("현재 정보");
            System.out.println("사원명   : " + nvl(emp.getEmpNm()));
            System.out.println("현 부서  : " + nvl(emp.getDeptNm()));
            System.out.println("현 직급코드 : " + nvl(emp.getGradeCd()));
            System.out.println();

            PrintUtil.printSection("직급 코드 목록");
            System.out.println("01.사원   02.대리   03.과장   04.차장   05.부장   06.이사   07.대표이사");
            System.out.println();

            String newGrade;
            while (true) {
                System.out.print("진급할 직급코드 입력 [예: 02, q: 돌아가기] ➤ ");
                newGrade = br.readLine();
                InputValidator.isUserExit(newGrade);

                if (!empDao.isValidGradeCd(newGrade)) {
                    System.out.println("존재하지 않는 직급 코드입니다. 다시 입력해주세요.\n");
                    continue;
                }
                if (newGrade.equals(emp.getGradeCd())) {
                    System.out.println("현재 직급과 동일합니다. 다른 직급을 입력해주세요.\n");
                    continue;
                }
                break;
            }

            String reason;
            while (true) {
                System.out.print("진급 사유 입력 [q: 돌아가기] ➤ ");
                reason = br.readLine();
                InputValidator.isUserExit(reason);

                if (reason == null || reason.trim().isEmpty()) {
                    System.out.println("진급 사유는 반드시 입력해야 합니다.\n");
                    continue;
                }
                break;
            }

            PromotionDTO dto = new PromotionDTO();
            dto.setEmpNo(empNo);
            dto.setCurrentGradeCd(emp.getGradeCd());
            dto.setNewGradeCd(newGrade);
            dto.setDetails(reason);

            int result = empDao.updatePromotion(dto);
            if (result > 0) {
                System.out.println("\n진급 처리가 완료되었습니다.\n");
            } else {
                System.out.println("\n진급 처리에 실패하였습니다.\n");
            }

        } catch (UserQuitException e) {
            System.out.println("\n진급 관리를 취소하고 상위 메뉴로 돌아갑니다.\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("진급 처리 중 오류가 발생했습니다.\n");
        }
    }

    /** 5. 사원관리 - 정보조회 (Dept 전사인원현황 스타일) */
    private void manageEmployeeSearch() {
        PrintUtil.printTitle("관리자 - 사원관리 - 정보조회");
        try {
            while (true) {
                System.out.print("1.사번조회 | 2.이름조회 | 3.전체조회 | 4.상위메뉴 [q: 돌아가기] ➤ ");
                String sel = br.readLine();
                InputValidator.isUserExit(sel);

                int ch;
                try {
                    ch = Integer.parseInt(sel.trim());
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 번호입니다. 1~4 사이의 값을 입력해주세요.\n");
                    continue;
                }

                if (ch == 4) {
                    return;
                }

                List<EmployeeDTO> list;
                switch (ch) {
                    case 1 -> {
                        String empNo = checkEmpNo(true);
                        EmployeeDTO dto = empDao.selectByEmpNo(empNo);
                        if (dto == null) {
                            System.out.println("해당 사원번호의 정보가 존재하지 않습니다.\n");
                            continue;
                        }
                        list = java.util.List.of(dto);
                    }
                    case 2 -> {
                        System.out.print("조회할 이름 입력 [q: 돌아가기] ➤ ");
                        String name = br.readLine();
                        InputValidator.isUserExit(name);

                        list = empDao.selectByName(name);
                        if (list == null || list.isEmpty()) {
                            System.out.println("등록된 자료가 없습니다.\n");
                            continue;
                        }
                    }
                    case 3 -> {
                        list = empDao.selectAll();
                        if (list == null || list.isEmpty()) {
                            System.out.println("등록된 자료가 없습니다.\n");
                            continue;
                        }
                    }
                    default -> {
                        System.out.println("잘못된 번호입니다. 1~4 사이의 값을 입력해주세요.\n");
                        continue;
                    }
                }

                printEmployeeListPaged(list);
                System.out.println();
            }
        } catch (UserQuitException e) {
            System.out.println("\n정보 조회를 취소했습니다.\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("정보 조회 중 오류가 발생했습니다.\n");
        }
    }

    /** 6. 사원관리 - 재직결재 (현재는 화면만 제공) */
    protected void updateRetireApprovalInfo() {
        PrintUtil.printTitle("관리자 - 사원관리 - 퇴직결재");
        
        final String RESET  = "\u001B[0m";
		final String GREEN  = "\u001B[32m";
		final String YELLOW = "\u001B[33m";
		final String CYAN   = "\u001B[36m";
		final String GRAY   = "\u001B[90m";

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
			
			for(RetireDTO dto : list) {
				System.out.printf("%s\t | %s\t | %s\t | %s\t | %s\t\n",
						PrintUtil.padCenter(Integer.toString(dto.getRetireSeq()), 8),
	            		PrintUtil.padCenter(dto.getEmpNo(), 8),
	            		PrintUtil.padCenter(dto.getRegDt(),  12),
	            		PrintUtil.padCenter(dto.getRetireMemo() != null && dto.getRetireMemo().length() > 18 ? dto.getRetireMemo().substring(0, 15) + "..." : dto.getRetireMemo(), 8),
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
			// TODO: handle exception
		}
    }

    /** 7. 사원관리 - 경력등록 */
    protected void insertCareerInfo() {
        PrintUtil.printTitle("관리자 - 사원관리 - 경력등록");
        try {
            String empNo = checkEmpNo(true);
            CareerDTO dto = new CareerDTO();
            dto.setEmpNo(empNo);

            System.out.print("회사명 입력 [q: 돌아가기] ➤ ");
            String comp = br.readLine();
            InputValidator.isUserExit(comp);
            dto.setCompanyName(comp);

            System.out.print("근무시작일 입력 [YYYY-MM-DD, q: 돌아가기] ➤ ");
            String start = br.readLine();
            InputValidator.isUserExit(start);
            dto.setStartDt(start);

            System.out.print("근무종료일 입력 [YYYY-MM-DD, q: 돌아가기] ➤ ");
            String end = br.readLine();
            InputValidator.isUserExit(end);
            dto.setEndDt(end);

            System.out.print("상세 입력 [q: 돌아가기] ➤ ");
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
            System.out.println("경력 등록 중 오류가 발생했습니다.\n");
        }
    }

    /** 8. 사원관리 - 자격증등록 */
    protected void insertLicenseInfo() {
        PrintUtil.printTitle("관리자 - 사원관리 - 자격증등록");
        try {
            String empNo = checkEmpNo(true);
            RewardDTO dto = new RewardDTO();
            dto.setEmpNo(empNo);

            System.out.print("자격증명 입력 [q: 돌아가기] ➤ ");
            String name = br.readLine();
            InputValidator.isUserExit(name);
            dto.setRewardName(name);

            System.out.print("발급기관 입력 [q: 돌아가기] ➤ ");
            String org = br.readLine();
            InputValidator.isUserExit(org);
            dto.setIssuer(org);

            System.out.print("취득일 입력 [YYYY-MM-DD, q: 돌아가기] ➤ ");
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
            System.out.println("자격증 등록 중 오류가 발생했습니다.\n");
        }
    }

    /** 9. 사원관리 - 이력조회 (경력 / 자격증 / 직급) */
    protected void selectHistoryInfo() {
        PrintUtil.printTitle("관리자 - 사원관리 - 이력조회");
        try {
            while (true) {
                System.out.print("1. 경력 | 2. 자격증 | 3. 직급이력 | 4. 상위메뉴 [q: 돌아가기] ➤ ");
                String sel = br.readLine();
                InputValidator.isUserExit(sel);
                int ch = Integer.parseInt(sel.trim());

                List<HistoryDTO> list;

                switch (ch) {
                    case 1 -> { // 경력 이력
                        list = empDao.selectCareerHisAll();
                        if (list == null || list.isEmpty()) {
                            System.out.println("등록된 자료가 없습니다.\n");
                            break;
                        }
                        printCareerHistory(list);
                    }
                    case 2 -> { // 자격증 이력
                        list = empDao.selectCertHisAll();
                        if (list == null || list.isEmpty()) {
                            System.out.println("등록된 자료가 없습니다.\n");
                            break;
                        }
                        printCertHistory(list);
                    }
                    case 3 -> { // 직급 이력
                        list = empDao.selectGradeHisAll();
                        if (list == null || list.isEmpty()) {
                            System.out.println("등록된 자료가 없습니다.\n");
                            break;
                        }
                        printGradeHistory(list);
                    }
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("잘못된 번호입니다.\n");
                }
                System.out.println();
            }

        } catch (UserQuitException e) {
            System.out.println("\n이력 조회를 취소했습니다.\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("이력 조회 중 오류가 발생했습니다.\n");
        }
    }

    /** 사번 형식을 검증하고 존재 여부를 검사하는 공통 메소드 */
    protected String checkEmpNo(boolean mustExist) throws IOException, SQLException {
        while (true) {
            System.out.print("사원번호 입력 [예: 00001, q: 돌아가기] ➤ ");
            String empNo = br.readLine();
            InputValidator.isUserExit(empNo);

            if (!InputValidator.isValidEmpNo(empNo)) {
                System.out.println("사원번호는 영문/숫자 조합 5자리여야 합니다.\n");
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

    /** CSV 파일을 이용한 일괄 등록 */
    protected void loadEmployeeInfo() {
        PrintUtil.printSection("CSV 파일 로드");
        empDao.loadEmployeeInfo();
        PrintUtil.printSection("로드 완료");
    }

    // ==================== 리스트 / 히스토리 출력용 헬퍼들 ====================

    
    private void printEmployeeListPaged(List<EmployeeDTO> list) throws IOException {
        if (list == null || list.isEmpty()) {
            System.out.println("등록된 자료가 없습니다.\n");
            return;
        }

        final int pageSize = 15;
        final int lineWidth = 180;

        int total = list.size();
        int totalPage = (total + pageSize - 1) / pageSize;
        int page = 0;

        while (true) {
            int from = page * pageSize;
            int to = Math.min(from + pageSize, total);

            PrintUtil.printLine('=', lineWidth);
            System.out.printf("페이지 %d / %d   (총 %d건)   [조회범위: %d ~ %d]%n",
                    page + 1, totalPage, total, from + 1, to);
            PrintUtil.printLine('=', lineWidth);

            printEmployeeListHeader();
            for (int i = from; i < to; i++) {
                printEmployeeRow(list.get(i));
            }
            PrintUtil.printLine('=', lineWidth);

            if (totalPage == 1) {
                break;
            }

            System.out.print("[n:다음  p:이전  q:종료] ➤ ");
            String cmd = br.readLine();
            if (cmd == null) break;
            cmd = cmd.trim().toLowerCase();

            if ("q".equals(cmd)) {
                break;
            } else if ("n".equals(cmd)) {
                if (page < totalPage - 1) page++;
            } else if ("p".equals(cmd)) {
                if (page > 0) page--;
            }
        }
    }

    private void printEmployeeListHeader() {
        int wEmpNo   = 6;
        int wName    = 8;
        int wRrn     = 15;
        int wAddr    = 40;
        int wHireDt  = 12;
        int wDept    = 12;
        int wGrade   = 8;
        int wStat    = 8;
        int wContract= 8;
        int wEmail   = 26;

        System.out.println(
                PrintUtil.padRight("사번",     wEmpNo)   + " | " +
                PrintUtil.padRight("이름",     wName)    + " | " +
                PrintUtil.padRight("주민번호", wRrn)     + " | " +
                PrintUtil.padRight("주소",     wAddr)    + " | " +
                PrintUtil.padRight("입사일자", wHireDt)  + " | " +
                PrintUtil.padRight("부서명",   wDept)    + " | " +
                PrintUtil.padRight("직급",     wGrade)   + " | " +
                PrintUtil.padRight("재직상태", wStat)    + " | " +
                PrintUtil.padRight("계약유형", wContract)+ " | " +
                PrintUtil.padRight("이메일",   wEmail)
        );
        PrintUtil.printLine('-', 180);
    }

    private void printEmployeeRow(EmployeeDTO dto) {
        int wEmpNo   = 6;
        int wName    = 8;
        int wRrn     = 15;
        int wAddr    = 40;
        int wHireDt  = 12;
        int wDept    = 12;
        int wGrade   = 8;
        int wStat    = 8;
        int wContract= 8;
        int wEmail   = 26;

        System.out.println(
                PrintUtil.padRight(nvl(dto.getEmpNo()),        wEmpNo)   + " | " +
                PrintUtil.padRight(nvl(dto.getEmpNm()),        wName)    + " | " +
                PrintUtil.padRight(nvl(dto.getRrn()),          wRrn)     + " | " +
                PrintUtil.padRight(nvl(dto.getEmpAddr()),      wAddr)    + " | " +
                PrintUtil.padRight(nvl(dto.getHireDt()),       wHireDt)  + " | " +
                PrintUtil.padRight(nvl(dto.getDeptNm()),       wDept)    + " | " +
                PrintUtil.padRight(nvl(dto.getGradeNm()),      wGrade)   + " | " +
                PrintUtil.padRight(nvl(dto.getEmpStatNm()),    wStat)    + " | " +
                PrintUtil.padRight(nvl(dto.getContractTpNm()), wContract)+ " | " +
                PrintUtil.padRight(nvl(dto.getEmail()),        wEmail)
        );
    }

    private void printCareerHistory(List<HistoryDTO> list) {
        PrintUtil.printSection("경력 이력");
        int wEmpNo = 6, wName = 8, wComp = 20, wStart = 12, wEnd = 12, wDet = 20;

        System.out.println(
                PrintUtil.padRight("사번",   wEmpNo) + " | " +
                PrintUtil.padRight("이름",   wName)  + " | " +
                PrintUtil.padRight("회사명", wComp) + " | " +
                PrintUtil.padRight("시작일", wStart)+ " | " +
                PrintUtil.padRight("종료일", wEnd)  + " | " +
                PrintUtil.padRight("상세",   wDet)
        );
        PrintUtil.printLine('=', 90);

        for (HistoryDTO d : list) {
            System.out.println(
                    PrintUtil.padRight(nvl(d.getEmpNo()),      wEmpNo) + " | " +
                    PrintUtil.padRight(nvl(d.getEmpNm()),      wName)  + " | " +
                    PrintUtil.padRight(nvl(d.getPrevCompNm()), wComp)  + " | " +
                    PrintUtil.padRight(nvl(d.getStartDt()),    wStart) + " | " +
                    PrintUtil.padRight(nvl(d.getEndDt()),      wEnd)   + " | " +
                    PrintUtil.padRight(nvl(d.getDetails()),    wDet)
            );
        }
        PrintUtil.printLine('-', 90);
    }

    private void printCertHistory(List<HistoryDTO> list) {
        PrintUtil.printSection("자격증 이력");
        int wEmpNo = 6, wName = 8, wCert = 20, wOrg = 20, wDate = 12;

        System.out.println(
                PrintUtil.padRight("사번",     wEmpNo) + " | " +
                PrintUtil.padRight("이름",     wName)  + " | " +
                PrintUtil.padRight("자격증명", wCert)  + " | " +
                PrintUtil.padRight("발급기관", wOrg)   + " | " +
                PrintUtil.padRight("발급일",   wDate)
        );
        PrintUtil.printLine('=', 90);

        for (HistoryDTO d : list) {
            System.out.println(
                    PrintUtil.padRight(nvl(d.getEmpNo()),   wEmpNo) + " | " +
                    PrintUtil.padRight(nvl(d.getEmpNm()),   wName)  + " | " +
                    PrintUtil.padRight(nvl(d.getCertNm()),  wCert)  + " | " +
                    PrintUtil.padRight(nvl(d.getIssueOrgNm()), wOrg) + " | " +
                    PrintUtil.padRight(nvl(d.getIssueDt()), wDate)
            );
        }
        PrintUtil.printLine('-', 90);
    }

    private void printGradeHistory(List<HistoryDTO> list) {
        PrintUtil.printSection("직급 이력");
        int wStart = 12, wEmpNo = 6, wName = 8, wGrade = 8, wEnd = 12, wDept = 12;

        System.out.println(
                PrintUtil.padRight("시작일", wStart) + " | " +
                PrintUtil.padRight("사번",   wEmpNo) + " | " +
                PrintUtil.padRight("이름",   wName)  + " | " +
                PrintUtil.padRight("직급",   wGrade) + " | " +
                PrintUtil.padRight("종료일", wEnd)   + " | " +
                PrintUtil.padRight("부서",   wDept)
        );
        PrintUtil.printLine('=', 80);

        for (HistoryDTO d : list) {
            System.out.println(
                    PrintUtil.padRight(nvl(d.getStartDt()), wStart) + " | " +
                    PrintUtil.padRight(nvl(d.getEmpNo()),   wEmpNo) + " | " +
                    PrintUtil.padRight(nvl(d.getEmpNm()),   wName)  + " | " +
                    PrintUtil.padRight(nvl(d.getGradeNm()), wGrade) + " | " +
                    PrintUtil.padRight(nvl(d.getEndDt()),   wEnd)   + " | " +
                    PrintUtil.padRight(nvl(d.getDeptNm()),  wDept)
            );
        }
        PrintUtil.printLine('-', 80);
    }

    /** null 방지용 헬퍼 */
    private String nvl(String s) {
        return s == null ? "" : s;
    }
}
