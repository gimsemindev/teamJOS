package com.sp.view.common;

import java.util.List;
import java.util.Scanner;

import com.sp.dao.AttDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.impl.AttDAOImpl;
import com.sp.dao.impl.DeptDAOImpl;
import com.sp.model.AnnualLeaveDTO;
import com.sp.model.DeptDTO;
import com.sp.model.DeptMemberDTO;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;

/**
 * <h2>DeptCommonUI (부서 및 근태 공통 UI)</h2>
 *
 * <p>관리자 및 일반 사원 메뉴에서 공통적으로 사용되는 부서 정보 조회, 부서별 인원 현황 조회,
 * 전체 사원의 연차 현황 조회 기능을 제공하는 콘솔 기반 UI 클래스입니다.</p>
 *
 * <h3>주요 기능 (유스케이스 ID)</h3>
 * <ul>
 * <li>전체 부서 목록 조회 (DEPT_SEL_003) - 부서 코드, 부서명, 내선번호 목록 출력</li>
 * <li>부서 인원 현황 조회 (DEPT_SEL_005) - 부서별 소속 사원의 상세 정보 목록 출력 (페이징)</li>
 * <li>전체 연차 현황 조회 (ATT_SEL_003) - 전 사원의 연차 발생/사용/잔여 현황 목록 출력 (페이징)</li>
 * </ul>
 *
 * <p>이 클래스는 {@code DeptDAO}와 {@code AttDAO}를 통해 데이터를 조회하며,
 * 목록 조회 시 페이징 처리 기능을 포함합니다.</p>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 김세민, 황선호</p>
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class DeptCommonUI {
	
	private LoginInfo loginInfo;
	
	private static final int PAGE_SIZE = 15;
    private DeptDAO deptDao = new DeptDAOImpl();
    private AttDAO attDao; 
	private Scanner sc = new Scanner(System.in);
    
	/**
	 * DeptCommonUI 생성자
	 *
	 * @param loginInfo 로그인 사용자 정보 객체. {@code AttDAO} 초기화에 사용됩니다.
	 */
	public DeptCommonUI(LoginInfo loginInfo){
		this.loginInfo = loginInfo;
		this.attDao = new AttDAOImpl(this.loginInfo);
	}
    
	/**
	 * 전체 부서 목록 조회 기능 (DEPT_SEL_003)
	 *
	 * <p>DB에 등록된 모든 부서의 부서 코드, 부서명, 내선번호 정보를 조회하고 콘솔에 출력합니다.</p>
	 */
	public void selectAllDept() {
		System.out.println("\n전체 부서 리스트...");

        List<DeptDTO> list = deptDao.selectAllDept();

        System.out.println("전체 부서수 : " + list.size());    
        PrintUtil.printLine('=', 60);
        System.out.printf("%s | %s | %s\n",
        		PrintUtil.padCenter("부서코드", 14),
        		PrintUtil.padCenter("부서명", 34),
        		PrintUtil.padCenter("내선번호",10));
        PrintUtil.printLine('=', 60);
        
        for(DeptDTO dto : list) {            
            System.out.printf("%s | %s \t | %s\n",
            		PrintUtil.padCenter(dto.getDeptCd(), 12),
            		PrintUtil.padRight(dto.getDeptNm(), 32),
            		PrintUtil.padCenter(dto.getExtNo(), 8));
        }
        PrintUtil.printLine('-', 60);
	}

	/**
	 * 부서 인원 현황 조회 기능 (DEPT_SEL_005)
	 *
	 * <p>전체 사원 정보를 부서 기준으로 조회하고, 페이지당 15명씩 페이징 처리하여 출력합니다.</p>
	 * <p>사원의 부서, 직급, 계약 유형, 재직 상태, 사번, 이름, 입사일, 연락처, 이메일 등의 상세 정보를 포함합니다.</p>
	 * <p>사용자는 'n' (다음), 'p' (이전), 'q' (종료) 명령을 통해 페이지를 이동할 수 있습니다.</p>
	 */
	public void selectDeptMember() {
		System.out.println("[부서인원현황]");	
		
		try {
			int totalCnt = deptDao.selectDeptMemberCount();
			
	        // 0건 처리
	        if (totalCnt == 0) {
	            System.out.println("조회 결과가 없습니다.");
	            return;
	        }
	        // 페이징 처리해서 소수점이 나오면 올림 처리
	        int totalPage = (int) Math.ceil(totalCnt / (double) PAGE_SIZE);
	        int page = 1;

	        
	        while (true) {
	            
	            int start = (page - 1) * PAGE_SIZE + 1;
	            int end   = page * PAGE_SIZE;

	            List<DeptMemberDTO> list = deptDao.selectDeptMember(start, end);

	            PrintUtil.printLine('=', 84);
	            System.out.printf("페이지 %d / %d  (총 %d건)   [조회범위: %d ~ %d]\n",
	                    page, totalPage, totalCnt, start, Math.min(end, totalCnt));	            
	       
	            PrintUtil.printLine('=', 177);
	            System.out.printf("%s | %s \t | %s \t | %s \t| %s \t| %s \t| %s \t| %s \t| %s \t| %s \t\n",
	            		PrintUtil.padCenter("부서코드", 14),
	            		PrintUtil.padCenter("부서명", 15),
	            		PrintUtil.padCenter("직급", 8),
	            		PrintUtil.padCenter("계약유형", 8),	            		
	            		PrintUtil.padCenter("재직상태", 8),	            		
	            		PrintUtil.padCenter("사원번호", 8),	            		
	            		PrintUtil.padCenter("사원이름", 8),	            		
	            		PrintUtil.padCenter("입사일자", 8),	            		
	            		PrintUtil.padCenter("연락처", 15),	            		
	            		PrintUtil.padCenter("이메일", 15)	  
	            		);
	            PrintUtil.printLine('=', 177);	            
	            
	            for (DeptMemberDTO dto : list) {
	            	System.out.printf("%s | %s \t | %s\t | %s \t| %s \t| %s \t| %s \t| %s \t| %s \t| %s\t",
	                	PrintUtil.padCenter(dto.getDeptCd(),12),
	                	PrintUtil.padRight(dto.getDeptNm(), 15),
	                	PrintUtil.padRight(dto.getGradeNM(), 8),
	                	PrintUtil.padRight(dto.getCotractTpNM(), 8),
	                	PrintUtil.padCenter(dto.getEmpStatNM(), 8),
	                	PrintUtil.padCenter(dto.getEmpNo(), 8),
	                	PrintUtil.padRight(dto.getEmpNm(), 8),
	                	PrintUtil.padCenter(dto.getHireDt(), 8),
	                	PrintUtil.padCenter(dto.getContactNo(), 15),
	                	PrintUtil.padRight(dto.getEmail(), 15)
	                );
	            	System.out.println();
	            }

	            PrintUtil.printLine('-', 177);
	            System.out.print("[n:다음  p:이전 q:종료] ➤ ");

	            String cmd = sc.nextLine().trim();

	            if (cmd.equalsIgnoreCase("q")) break;
	            else if (cmd.equalsIgnoreCase("p")) {
	                if (page == 1) { 
	                	System.out.println("이미 첫 페이지입니다. 더 이전으로 갈 수 없습니다.");
	                	return;
	                } else {
	                	page--;
	                }
	            }
	            else if (cmd.equalsIgnoreCase("n")) {
	                if (page == totalPage) {
	                	System.out.println("이미 마지막 페이지입니다. 더 앞으로 갈 수 없습니다.");
	                	return;
	                } else {
	                	page++;
	                }
	            }        
	            PrintUtil.printLine('-', 97);
	        }
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	/**
	 * 전체 사원 연차 현황 조회 기능 (ATT_SEL_003)
	 *
	 * <p>전체 사원의 연차 발생일수, 사용일수, 잔여일수 현황을 페이지당 15명씩 페이징 처리하여 출력합니다.</p>
	 * <p>사용자는 'n' (다음), 'p' (이전), 'q' (종료) 명령을 통해 페이지를 이동할 수 있습니다.</p>
	 * // 202511111628 : 김세민 수정중
	 */
	public void selectAllAnnualLeave() {

		System.out.println("[연차현황]");	
				
		try {
			int totalCnt = attDao.selectAllAnnualLeaveCount();
			
	        // 0건 처리
	        if (totalCnt == 0) {
	            System.out.println("조회 결과가 없습니다.");
	            return;
	        }
	        // 페이징 처리해서 소수점이 나오면 올림 처리
	        int totalPage = (int) Math.ceil(totalCnt / (double) PAGE_SIZE);
	        int page = 1;

	        
	        while (true) {
	            
	            int start = (page - 1) * PAGE_SIZE + 1;
	            int end   = page * PAGE_SIZE;

	            List<AnnualLeaveDTO> list = attDao.selectAllAnnualLeave(start, end);

	            PrintUtil.printLine('=', 84);
	            System.out.printf("페이지 %d / %d  (총 %d건)   [조회범위: %d ~ %d]\n",
	                    page, totalPage, totalCnt, start, Math.min(end, totalCnt));	            
	       
	            PrintUtil.printLine('=', 155);
	            System.out.printf("%s | %s \t | %s \t | %s \t| %s \t| %s \t| %s \t| %s \t| %s \t| %s \t\n",
	            		PrintUtil.padCenter("연차일련번호", 14),
	            		PrintUtil.padCenter("부서명", 15),
	            		PrintUtil.padCenter("직급", 8),
	            		PrintUtil.padCenter("사원번호", 8),	            		
	            		PrintUtil.padCenter("사원이름", 8),	            		
	            		PrintUtil.padCenter("입사일자", 12),	            		
	            		PrintUtil.padCenter("발생연차", 8),	            		
	            		PrintUtil.padCenter("사용연차", 8),	            		
	            		PrintUtil.padCenter("잔여연차", 8),	            		
	            		PrintUtil.padCenter("활성화여부", 8)	  
	            		);	        	
	            PrintUtil.printLine('=', 155);	            
	            
	            for (AnnualLeaveDTO dto : list) {
	            	System.out.printf("%s | %s \t | %s\t | %s \t| %s \t| %s \t| %s \t| %s \t| %s \t| %s\t",
	                	PrintUtil.padCenter(Integer.toString(dto.getLeaveSeq()),12),
	                	PrintUtil.padRight(dto.getDeptNm(), 15),
	                	PrintUtil.padRight(dto.getGradeNm(), 8),
	                	PrintUtil.padCenter(dto.getEmpNo(), 8),
	                	PrintUtil.padRight(dto.getEmpNm(), 8),
	                	PrintUtil.padCenter(dto.getHireDt(), 8),
	                	PrintUtil.padLeft(Integer.toString(dto.getTotalDays()),8),
	                	PrintUtil.padLeft(Integer.toString(dto.getUsedDays()),8),
	                	PrintUtil.padLeft(Integer.toString(dto.getRemainDays()),8),
	                	PrintUtil.padCenter(dto.getUseYn(), 8)
	                );
	            	System.out.println();
	            }
	            
	            PrintUtil.printLine('-', 155);
	            System.out.print("[n:다음  p:이전 q:종료] ➤ ");

	            String cmd = sc.nextLine().trim();

	            if (cmd.equalsIgnoreCase("q")) break;
	            else if (cmd.equalsIgnoreCase("p")) {
	                if (page == 1) { 
	                	System.out.println("이미 첫 페이지입니다. 더 이전으로 갈 수 없습니다.");
	                	return;
	                } else {
	                	page--;
	                }
	            }
	            else if (cmd.equalsIgnoreCase("n")) {
	                if (page == totalPage) {
	                	System.out.println("이미 마지막 페이지입니다. 더 앞으로 갈 수 없습니다.");
	                	return;
	                } else {
	                	page++;
	                }
	            }        
	            PrintUtil.printLine('-', 97);
	        }
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
}