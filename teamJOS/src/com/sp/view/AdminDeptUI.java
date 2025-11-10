package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import com.sp.dao.DeptDAO;
import com.sp.dao.impl.DeptDAOImpl;
import com.sp.exception.UserQuitException;
import com.sp.model.DeptDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;
import com.sp.view.common.DeptCommonUI;

public class AdminDeptUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private DeptDAO deptDao = new DeptDAOImpl();
	private DeptCommonUI deptCommonUI = new DeptCommonUI();
	private LoginInfo loginInfo;
	
	
	public AdminDeptUI(DeptDAO deptDao, LoginInfo loginInfo) {
		this.deptDao = deptDao;
		this.loginInfo = loginInfo;
	}

	public void menu() {
		int ch;
		String input;
		
		System.out.println("\n[관리자 - 부서관리]");
		while(true) {
			
			try {
				
				do {
					System.out.print("1.부서등록 2.부서수정 3.부서조회 4.부서삭제 5.부서인원현황 6.부서인원현황 다운로드 7.메뉴로돌아가기 => ");
					
					input = br.readLine();
	                
	                if(input == null || input.trim().isEmpty()) {
	                	ch = 0;
	                	continue;
	                }
	                ch = Integer.parseInt(input);
					
				} while (ch < 1 || ch > 6);
				
				switch (ch) {
				case 1:
					insertDept();
					break; // DEPT_INS_001
				case 2:
					updateDept();
					break; // DEPT_UPD_002
				case 3:
					deptCommonUI.selectAllDept();
					break; // DEPT_SEL_003 (기존 코드의 selectDeptByNo(0)은 selectAllDept로 수정)
				case 4:
					deleteDept();
					break; // DEPT_DEL_004
				case 5:
					deptCommonUI.selectDeptMember();
					break; // DEPT_SEL_005
				case 6:
					makeCSVFile();
					break;
				case 7:
					return; // 6.메뉴화면으로
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void insertDept() {
		System.out.println("\n[부서 등록]");

		DeptDTO dto = new DeptDTO();

		try {
			while (true) {
				System.out.print("부서코드를 입력 [예: D10000, 입력중단:q]: ");

				String deptCd = br.readLine().trim();

				// 부서코드 필수 값 : null 이나 공백은 허용안됨
				if (deptCd.isEmpty()) {
					System.out.println("부서코드는 필수 입력사항입니다. 다시 입력하세요.\n");
					continue;
				}

				// 사용자가 q를 입력하면 루프를 빠져나간다.
				InputValidator.isUserExit(deptCd);

				// 부서코드는 영문 D로 시작하는 5자리 문자여야함
				if (!InputValidator.isValidDeptCode(deptCd)) {
					continue;
				}

				System.out.print("부서명을 입력 [예: 마케팅부, 입력중단:q]: ");
				String deptNm = br.readLine().trim();

				if (deptNm.isEmpty()) {
					System.out.println("부서명은 필수 입력사항입니다. 다시 입력하세요.\n");
					continue;
				}

				// 사용자가 q를 입력하면 루프를 빠져나간다.
				InputValidator.isUserExit(deptNm);

				System.out.print("내선번호를 입력(미배정시 엔터) [예: 1111, 입력중단: q]: ");
				String extNo = br.readLine().trim();

				// 사용자가 q를 입력하면 루프를 빠져나간다.
				InputValidator.isUserExit(extNo);

				System.out.print("상위부서코드 입력(미배정시 엔터) [예: D10000, 입력중단:q]: ");
				String superDeptCd = br.readLine().trim();

				// 사용자가 q를 입력하면 루프를 빠져나간다.
				InputValidator.isUserExit(superDeptCd);

				System.out.print("사용여부 입력 [예: Y 또는 N, 입력중단:q] : ");
				String useYn = br.readLine().trim();
				if (useYn.isEmpty())
					useYn = "Y";

				// 사용자가 q를 입력하면 루프를 빠져나간다.
				InputValidator.isUserExit(useYn);

				dto.setDeptCd(deptCd);
				dto.setDeptNm(deptNm);
				dto.setExtNo(extNo);
				dto.setSuperDeptCd(superDeptCd);
				dto.setUseYn(useYn);

				break;
			}

			deptDao.insertDept(dto);

			System.out.println("데이터 등록이 완료 되었습니다.");

		} catch (UserQuitException e) {
			System.out.println(e.getMessage());
		} catch (SQLIntegrityConstraintViolationException e) {
			// 기본키 중복, NOT NULL 제약 위반등
			if (e.getErrorCode() == 1) {
				System.out.println("에러-부서코드 중복으로 추가가 불가능합니다.");
			} else if (e.getErrorCode() == 1400) {
				// INSERT - NOT NULL 제약 위반
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateDept() {
	    System.out.println("\n부서 정보 수정...");
	    String deptCd;

	    try {
	        System.out.print("수정할 부서코드 ? ");
	        deptCd = br.readLine();

	        // null 또는 공백만 입력했는지 확인
	        if (deptCd == null || deptCd.trim().isEmpty()) {
	            System.out.println("부서코드는 필수 입력사항입니다.");
	            return;
	        }

	        DeptDTO dto = deptDao.selectOneByDeptCd(deptCd.trim());
	        if (dto == null) {
	            System.out.println("등록된 부서가 아닙니다.");
	            return;
	        }

	        DeptDTO updDTO = new DeptDTO();
	        updDTO.setDeptCd(dto.getDeptCd());

	        // 기존 부서 정보 출력
	        PrintUtil.printLine('=', 131);
	        System.out.printf("%s | %s \t| %s | %s | %s | %s \n",
	                PrintUtil.padCenter("부서코드", 14),
	                PrintUtil.padCenter("부서명", 24),
	                PrintUtil.padCenter("내선번호", 6),
	                PrintUtil.padCenter("상위부서코드", 12),
	                PrintUtil.padCenter("사용여부", 10),
	                PrintUtil.padCenter("등록일시", 30)
	        );
	        PrintUtil.printLine('=', 131);

	        System.out.printf("%s | %s \t | %s | %s | %s | %s  \n",
	                PrintUtil.padCenter(dto.getDeptCd(), 12),
	                PrintUtil.padRight(dto.getDeptNm(), 24),
	                PrintUtil.padCenter(dto.getExtNo(), 6),
	                PrintUtil.padCenter(dto.getSuperDeptCd(), 10),
	                PrintUtil.padCenter(dto.getUseYn(), 10),
	                PrintUtil.padCenter(dto.getRegDt(), 10)
	        );
	        PrintUtil.printLine('-', 131);

	        while (true) {

	            // 부서명 입력
	            System.out.print("수정 부서명을 입력 [예: 마케팅부, 현재유지:p, 입력중단:q]: ");
	            String deptNm = br.readLine();
	            if (deptNm == null) deptNm = "";
	            deptNm = deptNm.trim();

	            InputValidator.isUserExit(deptNm); // q 입력 시 예외 발생

	            if (deptNm.equalsIgnoreCase("p")) {
	                updDTO.setDeptNm(dto.getDeptNm());
	            } else if (deptNm.isEmpty()) {
	                System.out.println("부서명은 필수 입력사항입니다. 다시 입력하세요.\n");
	                continue;
	            } else {
	                updDTO.setDeptNm(deptNm);
	            }

	            // 선번호 입력
	            System.out.print("수정 내선번호를 입력 [예: 1111, 현재유지:p, 입력중단:q]: ");
	            String extNo = br.readLine();
	            if (extNo == null) extNo = "";
	            extNo = extNo.trim();

	            InputValidator.isUserExit(extNo);

	            if (extNo.equalsIgnoreCase("p")) {
	                updDTO.setExtNo(dto.getExtNo());
	            } else {
	                updDTO.setExtNo(extNo);
	            }

	            // 상위부서코드 입력
	            System.out.print("수정 상위부서코드 입력 [예: D10000, 현재유지:p, 입력중단:q]: ");
	            String superDeptCd = br.readLine();
	            if (superDeptCd == null) superDeptCd = "";
	            superDeptCd = superDeptCd.trim();

	            InputValidator.isUserExit(superDeptCd);

	            if (superDeptCd.equalsIgnoreCase("p")) {
	                updDTO.setSuperDeptCd(dto.getSuperDeptCd());
	            } else {
	                if (!InputValidator.isValidDeptCode(superDeptCd)) {
	                    System.out.println("상위부서코드는 D로 시작하는 5자리여야 합니다. 다시 입력하세요.\n");
	                    continue;
	                }
	                updDTO.setSuperDeptCd(superDeptCd);
	            }

	            System.out.print("수정 사용여부 입력 [예: Y 또는 N, 현재유지:p, 입력중단:q] : ");
	            String useYn = br.readLine();
	            if (useYn == null) useYn = "";
	            useYn = useYn.trim();

	            InputValidator.isUserExit(useYn);

	            if (useYn.isEmpty()) {
	                updDTO.setUseYn("Y");
	            } else if (useYn.equalsIgnoreCase("p")) {
	                updDTO.setUseYn(dto.getUseYn());
	            } else {
	                updDTO.setUseYn(useYn.toUpperCase());
	            }

	            break;
	        }

	        deptDao.updateDept(updDTO);
	        System.out.println("수정이 완료되었습니다.");

	    } catch (UserQuitException e) {
	        System.out.println(e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public void deleteDept() {
	    System.out.println("\n부서 삭제 (사용여부 N 처리)...");

	    try {
	        System.out.print("삭제할 부서코드 입력: ");
	        String deptCd = br.readLine().trim();

	        if (deptCd.isEmpty()) {
	            System.out.println("부서코드를 입력해야 합니다.");
	            return;
	        }

	        // 삭제할 부서 존재 여부 확인
	        DeptDTO dto = deptDao.selectOneByDeptCd(deptCd);
	        if (dto == null) {
	            System.out.println("등록된 부서가 아닙니다.");
	            return;
	        }

	        // 삭제 대상 부서 리스트 조회 (상위 + 모든 하위부서)
	        List<DeptDTO> targetDepts = deptDao.selectDeptWithAllChildren(deptCd);

	        // 삭제 대상 부서 목록 출력
	        System.out.println("다음 부서들이 사용 안 함(N) 처리됩니다:");
	        PrintUtil.printLine('=', 93);
	        System.out.printf("%s | %s | %s\n",
	        		PrintUtil.padCenter("부서코드", 14),
	        		PrintUtil.padCenter("부서명", 34),
	        		PrintUtil.padCenter("사용여부",10));
	        PrintUtil.printLine('=', 93);
	        
	        for (DeptDTO d : targetDepts) {           
	            System.out.printf("%s | %s \t | %s\n",
	            		PrintUtil.padCenter(d.getDeptCd(), 12),
	            		PrintUtil.padRight(d.getDeptNm(), 32),
	            		PrintUtil.padCenter(d.getUseYn(), 8));
	        }
	        PrintUtil.printLine('-', 93);	        
	        
	        // 삭제전 사용자 확인
	        System.out.print("정말 삭제하시겠습니까? (Y/N): ");
	        String confirm = br.readLine().trim();
	        if (!confirm.equalsIgnoreCase("Y")) {
	            System.out.println("삭제가 취소되었습니다.");
	            return;
	        }

	        // 5️⃣ 하위 부서를 포함한 USE_YN = 'N' 처리
	        int updatedCount = deptDao.deleteDept(deptCd);
	        System.out.println(updatedCount + "개의 부서를 사용 처리했습니다.");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void makeCSVFile() {
		try {
			 deptDao.makeCSVFile();
		 } catch (Exception e) {
		        e.printStackTrace();
		    }
	}
}