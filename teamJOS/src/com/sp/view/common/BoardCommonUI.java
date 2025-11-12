package com.sp.view.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import com.sp.dao.BoardDAO;
import com.sp.dao.impl.BoardDAOImpl;
import com.sp.model.BoardDTO;
import com.sp.util.LoginInfo;
import com.sp.util.PrintUtil;

public class BoardCommonUI {
	
    private BoardDAO boardDao = new BoardDAOImpl();
	private LoginInfo loginInfo;
	private static final int PAGE_SIZE = 15;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public BoardCommonUI(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
    public void insert() {
        try {           
            BoardDTO dto = new BoardDTO();

            System.out.print("제목 ? ");
            String title = br.readLine();
            System.out.print("내용 ? ");
            String content = br.readLine();
            
            dto.setTitle(title);
            dto.setContent(content);
            dto.setEmpNo(loginInfo.loginMember().getMemberId()); // TODO: 로그인 정보 연동

            int result = boardDao.insertPost(dto);
            
            if (result > 0) {
                System.out.println("\n[정보] 게시글이 성공적으로 등록되었습니다.");
            } else {
                System.out.println("\n[오류] 게시글 등록에 실패했습니다.");
            }

        } catch (Exception e) {
            System.out.println("\n[오류] 게시글 등록 중 예외 발생: " + e.getMessage());
        }   
    }

    public void update() {
        try {
           
            BoardDTO dto = new BoardDTO();

           
            System.out.print("수정할 글번호 ? ");
            String inputSeq = br.readLine();
            int boardNo = Integer.parseInt(inputSeq);
            
            System.out.print("새 제목 ? ");
            String newTitle = br.readLine();
            System.out.print("새 내용 ? ");
            String newContent = br.readLine();

           
            dto.setBoardNo(boardNo); 
            dto.setTitle(newTitle);
            dto.setContent(newContent);

           
            dto.setEmpNo(loginInfo.loginMember().getMemberId()); // TODO: 로그인 정보 연동
            int result = boardDao.updatePost(dto);

          
            if (result > 0) {
                System.out.println("\n[정보] " + boardNo + "번 글이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("\n[오류] 글 수정에 실패했습니다. (글번호 또는 권한을 확인하세요)");
            }

        } catch (NumberFormatException e) {
            System.out.println("\n[오류] 글번호는 숫자로 입력해야 합니다.");
        } catch (Exception e) {
            System.out.println("\n[오류] 게시글 수정 중 예외 발생: " + e.getMessage());
        }
    }    
	
    public void viewPostsList() {

        try {
			int totalCnt = boardDao.listPostsCount();
			
	        // 0건 처리
	        if (totalCnt == 0) {
	        	System.out.println("\n[정보] 등록된 게시글이 없습니다.");
	            return;
	        }
	        // 페이징 처리해서 소수점이 나오면 올림 처리
	        int totalPage = (int) Math.ceil(totalCnt / (double) PAGE_SIZE);
	        int page = 1;

	        while (true) {
	            
	            int start = (page - 1) * PAGE_SIZE + 1;
	            int end   = page * PAGE_SIZE;

	            List<BoardDTO> list = boardDao.listPosts(start, end);
	            PrintUtil.printLine('=', 70);	
	            System.out.printf("페이지 %d / %d  (총 %d건)   [조회범위: %d ~ %d]\n",
	                    page, totalPage, totalCnt, start, Math.min(end, totalCnt));
	            
	            // 1. (수정) 헤더의 총 길이를 51 문자로 고정
	            //    (데이터 printf 형식: " %-5d | %-18s | %-8s | %-10s\n")
	            //    (1+5) + 3 + 18 + 3 + 8 + 3 + 10 = 51 문자
	            PrintUtil.printLine('=', 70);
	            
	            // 2. (수정) 헤더도 printf를 사용해 데이터와 동일한 칸을 차지하도록 함
		        System.out.printf("%s \t| %s \t| %s | %s \t\n",
		                PrintUtil.padCenter("번호", 8),
		                PrintUtil.padCenter("제목", 30),
		                PrintUtil.padCenter("작성자", 10),
		                PrintUtil.padCenter("작성일", 12)
		        );
	            PrintUtil.printLine('=', 70);	            
	            
	            for (BoardDTO dto : list) {
	                String title = dto.getTitle();
	                if (title.length() > 10) {
	                    title = title.substring(0, 10) + "...";
	                }
	                
	                String regDate = dto.getRegDtm().substring(0, 10);
	                
	                // 3. (기존) 데이터 출력 (헤더와 형식이 동일함)                    
	                System.out.printf("%s | %s \t| %s | %s \t\n",
	        	    		PrintUtil.padLeft(Integer.toString(dto.getBoardNo()), 7),
	        	            PrintUtil.padRight(title, 30),
	        	            PrintUtil.padCenter(dto.getEmpNo(), 8),
	        	            PrintUtil.padCenter(regDate, 12)
	        	        );
	            }
	            PrintUtil.printLine('-', 70);
	            System.out.print("[n:다음  p:이전 q:종료 번호엔터:자세히보기 ➤ \n");
	            PrintUtil.printLine('-', 70);
	        
		        System.out.print("옵션입력 ? ");
	            String cmd = br.readLine().trim();

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
      
                int boardNo;
                                
                try {
                    boardNo = Integer.parseInt(cmd);
                    if(Integer.parseInt(cmd) > 0) {
                    	viewPostDetail(boardNo);
                    }
                    
                } catch (NumberFormatException e) {
                    System.out.println("[오류] 숫자를 입력해야 합니다.");
                    continue;
                }   
	        }	        
	        
        } catch (Exception e) {
            System.out.println("\n[오류] 게시글 조회 중 예외 발생: " + e.getMessage());
        }
    }
    
    
    private void viewPostDetail(int boardNo) {
        try {
            BoardDTO dto = boardDao.getPost(boardNo);

            if (dto == null) {
                System.out.println("\n[오류] 해당 번호의 게시글이 존재하지 않습니다.");
                return;
            }

            System.out.println("\n==================================================");
            System.out.println("           [ " + boardNo + "번 게시글 상세 보기 ]");
            System.out.println("--------------------------------------------------");
            System.out.println(" > 글번호: " + dto.getBoardNo());
            System.out.println(" > 제  목: " + dto.getTitle());
            System.out.println(" > 작성자: " + dto.getEmpNo());
            System.out.println(" > 작성일: " + dto.getRegDtm());
            
            if (dto.getUpdateDtm() != null) {
                System.out.println(" > 수정일: " + dto.getUpdateDtm());
            }
            
            System.out.println("--------------------------------------------------");
            System.out.println("\n" + dto.getContent() + "\n"); // 내용 위아래 공백 추가
            System.out.println("==================================================");
            System.out.print("\n(엔터를 누르면 목록으로 돌아갑니다.)");
            br.readLine(); // 사용자가 내용을 읽을 때까지 대기

        } catch (Exception e) {
            System.out.println("\n[오류] 상세 보기 중 예외 발생: " + e.getMessage());
            // e.printStackTrace();
        }
    } 
}
