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

import static com.sp.util.PrintUtil.*;

/**
 * <h2>AdminDeptUI (관리자 부서 관리 UI)</h2>
 *
 * <p>관리자 메뉴에서 부서 관련 기능을 제어하는 콘솔 기반 UI 클래스입니다.</p>
 *
 * <ul>
 *   <li>부서 등록 (DEPT_INS_001)</li>
 *   <li>부서 수정 (DEPT_UPD_002)</li>
 *   <li>부서 조회 (DEPT_SEL_003)</li>
 *   <li>부서 삭제 (DEPT_DEL_004)</li>
 *   <li>부서별 소속 인원 조회 (DEPT_SEL_005)</li>
 *   <li>전사 인원 현황 CSV 다운로드 (DEPT_SEL_009)</li>
 *   <li>본부 부서 소속 인원 통계 조회 (DEPT_SEL_010)</li>
 * </ul>
 *
 * <p>사용자 입력 검증, 예외 처리, 콘솔 출력 구조를 관리하며  
 * DeptDAO, DeptCommonUI 를 통해 실제 로직과 연동됩니다.</p>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 황선호, 김세민</p>
 * <p><b>작성일:</b> 2025-11-16</p>
 * <p><b>버전:</b> 0.9</p> 
 */
public class AdminDeptUI {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private DeptDAO deptDao = new DeptDAOImpl();
	private DeptCommonUI deptCommonUI = null;
	private LoginInfo loginInfo;
	
	/**
	 * AdminDeptUI 생성자
	 *
	 * @param deptDao   부서 DAO
	 * @param loginInfo 로그인 사용자 정보
	 */
	public AdminDeptUI(DeptDAO deptDao, LoginInfo loginInfo) {
		this.deptDao = deptDao;
		this.loginInfo = loginInfo;
		this.deptCommonUI = new DeptCommonUI(this.loginInfo);
	}

	/**
	 * 관리자 부서관리 메인 메뉴 화면을 출력하고 사용자 입력을 처리합니다.
	 *
	 * <p>1~8번까지의 기능을 선택하여 부서 등록/수정/조회/삭제 및  
	 * 부서 인원 통계 기능을 실행합니다.</p>
	 */
	public void menu() {
		int ch;
		String input;
		while(true) {
			try {
				do {
					printTitle("🏢 [관리자 - 부서관리]");
					printMenu(YELLOW, "① 부서 등록", "② 부서 수정", "③ 부서 조회", "④ 부서 삭제", "⑤ 전사 인원 현황", "⑥ 전사 인원 현황 다운로드", "⑦ 본부 부서 소속 인원");
					
					input = br.readLine();
					InputValidator.isUserExit(input);
					
	                if(input == null || input.trim().isEmpty()) {
	                	ch = 0;
	                	continue;
	                }
	                ch = Integer.parseInt(input);
					
				} while (ch < 1 || ch > 7);
				
				switch (ch) {
				case 1:
					insertDept();
					break; // DEPT_INS_001
				case 2:
					updateDept();
					break; // DEPT_UPD_002
				case 3:
					deptCommonUI.selectAllDept();
					break; // DEPT_SEL_003
				case 4:
					deleteDept();
					break; // DEPT_DEL_004
				case 5:
					deptCommonUI.selectDeptMember();
					break; // DEPT_SEL_005
				case 6:
					makeCSVFile();
					break; // DEPT_SEL_009
				case 7:
					selectDeptMemberCountRatio();
					break; // DEPT_SEL_010
				default : printLineln(MAGENTA, "📢 잘못된 입력입니다");
				}
				
			} catch (UserQuitException e) {
				printLineln(MAGENTA, "📢 작업을 취소하였습니다.");
				return;
		    } catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 부서 등록 기능 (DEPT_INS_001)
	 *
	 * <p>부서코드, 부서명, 내선번호, 상위부서코드, 사용여부를 입력받아  
	 * 신규 부서를 생성합니다.</p>
	 *
	 * <p>입력 검증 및 q 입력 시 중단 기능을 포함합니다.</p>
	 */
	public void insertDept() {
		printTitle("📌 [부서 등록]");

		DeptDTO dto = new DeptDTO();

		try {
			while (true) {
				printLine(GREEN, "👉 부서코드를 입력 [예: D10000, 입력중단:q]: ");

				String deptCd = br.readLine().trim();

				if (deptCd.isEmpty()) {
					printLineln(MAGENTA, "📢 부서코드는 필수 입력사항입니다. 다시 입력하세요.");
					continue;
				}

				InputValidator.isUserExit(deptCd);

				if (!InputValidator.isValidDeptCode(deptCd)) {
					continue;
				}

				printLine(GREEN, "👉 부서명을 입력 [예: 마케팅부, 입력중단:q]: ");
				String deptNm = br.readLine().trim();

				if (deptNm.isEmpty()) {
					printLineln(MAGENTA, "📢 부서명은 필수 입력사항입니다. 다시 입력하세요.");
					continue;
				}

				InputValidator.isUserExit(deptNm);

				printLine(GREEN, "👉 내선번호를 입력(미배정시 엔터) [예: 1111, 입력중단: q]: ");
				String extNo = br.readLine().trim();
				InputValidator.isUserExit(extNo);

				printLine(GREEN, "👉 상위부서코드 입력(미배정시 엔터) [예: D10000, 입력중단:q]: ");
				String superDeptCd = br.readLine().trim();
				InputValidator.isUserExit(superDeptCd);

				printLine(GREEN, "👉 사용여부 입력 [예: Y 또는 N, 입력중단:q] : ");
				String useYn = br.readLine().trim();
				if (useYn.isEmpty())
					useYn = "Y";
				InputValidator.isUserExit(useYn);

				dto.setDeptCd(deptCd);
				dto.setDeptNm(deptNm);
				dto.setExtNo(extNo);
				dto.setSuperDeptCd(superDeptCd);
				dto.setUseYn(useYn);

				break;
			}

			deptDao.insertDept(dto);

			printLineln(MAGENTA, "📢 데이터 등록이 완료 되었습니다.");

		} catch (UserQuitException e) {
			System.out.println(e.getMessage());
		} catch (SQLIntegrityConstraintViolationException e) {
			if (e.getErrorCode() == 1) {
				printLineln(MAGENTA, "📢 에러-부서코드 중복으로 추가가 불가능합니다.");
			} else if (e.getErrorCode() == 1400) {
				printLineln(MAGENTA, "📢 에러-필수 입력사항을 입력하지 않았습니다.");
			} else {
				printLineln(MAGENTA, e.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 부서 수정 기능 (DEPT_UPD_002)
	 *
	 * <p>기존 부서를 조회하여 정보(부서명/내선/상위부서/사용여부)를 변경합니다.</p>
	 * <p>입력 중단(q), 기존 값 유지(p) 기능을 포함합니다.</p>
	 */
	public void updateDept() {
		printTitle("📌 [부서 정보 수정]");
	    String deptCd;

	    try {
	    	printLine(GREEN, "👉 수정할 부서코드를 입력하세요. : ");
	        deptCd = br.readLine();

	        if (deptCd == null || deptCd.trim().isEmpty()) {
	        	printLineln(MAGENTA, "📢 부서코드는 필수 입력사항입니다.");
	            return;
	        }

	        DeptDTO dto = deptDao.selectOneByDeptCd(deptCd.trim());
	        if (dto == null) {
	        	printLineln(MAGENTA, "📢 등록된 부서가 아닙니다.");
	            return;
	        }

	        DeptDTO updDTO = new DeptDTO();
	        updDTO.setDeptCd(dto.getDeptCd());

	        PrintUtil.printLine('═', 131);
	        System.out.printf("%s | %s \t| %s | %s | %s | %s \n",
	                PrintUtil.padCenter("부서코드", 14),
	                PrintUtil.padCenter("부서명", 24),
	                PrintUtil.padCenter("내선번호", 6),
	                PrintUtil.padCenter("상위부서코드", 12),
	                PrintUtil.padCenter("사용여부", 10),
	                PrintUtil.padCenter("등록일시", 30)
	        );
	        PrintUtil.printLine('═', 131);

	        System.out.printf("%s | %s \t | %s | %s | %s | %s  \n",
	                PrintUtil.padCenter(dto.getDeptCd(), 12),
	                PrintUtil.padRight(dto.getDeptNm(), 24),
	                PrintUtil.padCenter(dto.getExtNo(), 6),
	                PrintUtil.padCenter(dto.getSuperDeptCd(), 10),
	                PrintUtil.padCenter(dto.getUseYn(), 10),
	                PrintUtil.padCenter(dto.getRegDt(), 10)
	        );
	        PrintUtil.printLine('─', 131);

	        while (true) {

	        	printLine(GREEN, "👉 수정 부서명을 입력 [예: 마케팅부, 현재유지:p, 입력중단:q]: ");
	            String deptNm = br.readLine();
	            if (deptNm == null) deptNm = "";
	            deptNm = deptNm.trim();
	            InputValidator.isUserExit(deptNm);

	            if (deptNm.equalsIgnoreCase("p")) {
	                updDTO.setDeptNm(dto.getDeptNm());
	            } else if (deptNm.isEmpty()) {
	            	printLineln(MAGENTA, "📢 부서명은 필수 입력사항입니다. 다시 입력하세요.");
	                continue;
	            } else {
	                updDTO.setDeptNm(deptNm);
	            }

	            printLine(GREEN, "👉 수정 내선번호를 입력 [예: 1111, 현재유지:p, 입력중단:q]: ");
	            String extNo = br.readLine();
	            if (extNo == null) extNo = "";
	            extNo = extNo.trim();
	            InputValidator.isUserExit(extNo);

	            if (extNo.equalsIgnoreCase("p")) {
	                updDTO.setExtNo(dto.getExtNo());
	            } else {
	                updDTO.setExtNo(extNo);
	            }

	            printLine(GREEN, "👉 수정 상위부서코드 입력 [예: D10000, 현재유지:p, 입력중단:q]: ");
	            String superDeptCd = br.readLine();
	            if (superDeptCd == null) superDeptCd = "";
	            superDeptCd = superDeptCd.trim();
	            InputValidator.isUserExit(superDeptCd);

	            if (superDeptCd.equalsIgnoreCase("p")) {
	                updDTO.setSuperDeptCd(dto.getSuperDeptCd());
	            } else {
	                if (!InputValidator.isValidDeptCode(superDeptCd)) {
	                	printLineln(MAGENTA, "📢 상위부서코드는 D로 시작하는 5자리여야 합니다. 다시 입력하세요.");
	                    continue;
	                }
	                updDTO.setSuperDeptCd(superDeptCd);
	            }

	            printLine(GREEN, "👉 수정 사용여부 입력 [예: Y 또는 N, 현재유지:p, 입력중단:q] : ");
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
	        printLineln(MAGENTA, "📢 수정이 완료되었습니다.");

	    } catch (UserQuitException e) {
	        System.out.println(e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * 부서 삭제 기능 (DEPT_DEL_004)
	 *
	 * <p>입력한 부서 및 모든 하위 부서를 조회 후  
	 * USE_YN = 'N' 으로 변경합니다.</p>
	 *
	 * <p>삭제 전 확인 메시지 출력 및 사용자 승인 절차 포함.</p>
	 */
	public void deleteDept() {
		printTitle("📌 [부서 삭제 (사용여부 N 처리)]");

	    try {
	    	printLine(GREEN, "👉 삭제할 부서코드 : ");
	        String deptCd = br.readLine().trim();

	        if (deptCd.isEmpty()) {
	        	printLineln(MAGENTA, "📢 부서코드를 입력해야 합니다.");
	            return;
	        }

	        DeptDTO dto = deptDao.selectOneByDeptCd(deptCd);
	        if (dto == null) {
	        	printLineln(MAGENTA, "📢 등록된 부서가 아닙니다.");
	            return;
	        }

	        List<DeptDTO> targetDepts = deptDao.selectDeptWithAllChildren(deptCd);

	        printLineln(MAGENTA, "📢 다음 부서들이 사용 안 함(N) 처리됩니다:");
	        PrintUtil.printLine('═', 93);
	        System.out.printf("%s | %s | %s\n",
	        		PrintUtil.padCenter("부서코드", 14),
	        		PrintUtil.padCenter("부서명", 34),
	        		PrintUtil.padCenter("사용여부",10));
	        PrintUtil.printLine('═', 93);
	        
	        for (DeptDTO d : targetDepts) {           
	            System.out.printf("%s | %s \t | %s\n",
	            		PrintUtil.padCenter(d.getDeptCd(), 12),
	            		PrintUtil.padRight(d.getDeptNm(), 32),
	            		PrintUtil.padCenter(d.getUseYn(), 8));
	        }
	        PrintUtil.printLine('─', 93);	        
	        
	        printLineln(MAGENTA, "📢 정말 삭제하시겠습니까? (Y/N): ");
	        String confirm = br.readLine().trim();
	        if (!confirm.equalsIgnoreCase("Y")) {
	        	printLineln(MAGENTA, "📢 삭제가 취소되었습니다.");
	            return;
	        }

	        int updatedCount = deptDao.deleteDept(deptCd);
	        printLineln(MAGENTA, updatedCount + "개의 부서를 사용 처리했습니다.");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * 전사 인원 현황을 CSV 파일로 생성합니다. (DEPT_SEL_009)
	 */
	public void makeCSVFile() {
		printLineln(MAGENTA, "📁 전사인원현황 다운로드...");
		try {
			 deptDao.makeCSVFile();
		 } catch (Exception e) {
		        e.printStackTrace();
		    }
	}

	/**
	 * 본부부서 소속 인원 및 비율을 조회하여 그래프 형태로 출력합니다.
	 *
	 * <p>DEPT_SEL_010 쿼리 결과를 기반으로  
	 * 부서별 인원수, 비율, 그래프(색상 막대)를 출력합니다.</p>
	 */
    public void selectDeptMemberCountRatio() {
    	
    	String[] colors = {
    		    "\033[31m", // red
    		    "\033[32m", // green
    		    "\033[33m", // yellow
    		    "\033[34m", // blue
    		    "\033[35m", // magenta
    		    "\033[36m"  // cyan
    		};
    	
    	printTitle("📌 [본부 부서 소속 인원]");
	    System.out.println("\n본부부서소속인원...");
	    
        List<DeptDTO> list = deptDao.selectDeptMemberCountRatio();

        System.out.println("전체 부서수 : " + list.size());    
        PrintUtil.printLine('═', 80);
        System.out.printf("%s|%s\t|%s|%s\t|%s\n",
        		PrintUtil.padCenter("본부부서코드", 12),
        		PrintUtil.padCenter("본부부서명", 24),
        		PrintUtil.padCenter("소속인원",10),
                PrintUtil.padCenter("비율",8),
                PrintUtil.padCenter("그래프",10)
        		);
        PrintUtil.printLine('═', 80);
        
        int idx = 0;
        for(DeptDTO dto : list) {            
            System.out.printf("%s|%s\t|%s|%s\t|",
            		PrintUtil.padCenter(dto.getDeptCd(), 9),
            		PrintUtil.padRight(dto.getDeptNm(), 20),
            		PrintUtil.padLeft(Integer.toString(dto.getDeptCount()), 8),
            		PrintUtil.padLeft(Integer.toString(dto.getDeptCountRatio())+"%", 8)
            		);

    	    int barLen = (int)Math.round(dto.getDeptCountRatio() / 2.0);

    	    String color = colors[idx % colors.length];

    	    System.out.print(color);
    	    System.out.print("█".repeat(barLen));
    	    System.out.println("\033[0m");

    	    idx++;    		
        }
        PrintUtil.printLine('─', 80);
    }
}
