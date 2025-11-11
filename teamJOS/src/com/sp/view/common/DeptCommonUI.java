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
import com.sp.util.PrintUtil;

public class DeptCommonUI {
	private static final int PAGE_SIZE = 15;
    private DeptDAO deptDao = new DeptDAOImpl();
    private AttDAO attDao = new AttDAOImpl(); 
    
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

	public void selectDeptMember() {
		Scanner sc = new Scanner(System.in);
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
			sc.close();
		}
	}
	// 202511111628 : 김세민 수정중
	public void selectAllAnnualLeave() {
		Scanner sc = new Scanner(System.in);
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
			sc.close();
		}
	}
	
}
