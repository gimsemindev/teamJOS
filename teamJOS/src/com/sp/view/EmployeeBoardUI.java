package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import com.sp.dao.BoardDAO;
import com.sp.model.BoardDTO;
//import com.sp.model.LoginDTO; 
import com.sp.util.LoginInfo;

public class EmployeeBoardUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private BoardDAO boardDao;
   // private LoginInfo loginInfo;
    
    public EmployeeBoardUI(BoardDAO boardDao, LoginInfo loginInfo) {
        this.boardDao = boardDao;
       // this.loginInfo = loginInfo;
        
    }
    
    // EmployeeUI의 manageBoard() 기능을 menu()로 변경
    public void menu() {
        int ch;
        String input;
        
        while(true) {
        	try {
                System.out.println("\n========================================");
                System.out.println("           [ 게시판 관리 ]");
                System.out.println("========================================");
                System.out.println(" 1. 게시글 등록");
                System.out.println(" 2. 게시글 수정");
                System.out.println(" 3. 게시글 삭제");
                System.out.println(" 4. 게시글 보기");
                System.out.println(" 5. 메뉴로 돌아가기");
                System.out.println("----------------------------------------");

        		do {
        			System.out.print("선택 > "); // 프롬프트 변경

        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 5);
        		
                System.out.println(); // 선택 후 한 줄 띄우기

        		switch(ch) {
        		case 1: insert(); break; 
        		case 2: update(); break; 
        		case 3: delete(); break; 
        		case 4: viewPostsList(); break;
        		case 5: return; 
        		}
        		
                System.out.println(); // 각 작업 후 한 줄 띄우기

        	} catch (Exception e) {
        		System.out.println("[오류] 알 수 없는 예외가 발생했습니다.");
        		// e.printStackTrace(); // (디버깅 시 필요)
        	}
        	
        }
    }
    private void insert() {
        System.out.println("--- [ 1. 게시글 등록 ] ---");
        try {
           
            BoardDTO dto = new BoardDTO();

            
            System.out.print("제목 ? ");
            String title = br.readLine();
            System.out.print("내용 ? ");
            String content = br.readLine();
            
            
            dto.setTitle(title);
            dto.setContent(content);
            
           
            dto.setEmpNo("00001"); // TODO: 로그인 정보 연동

           
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
    
    
    private void update() {
        System.out.println("--- [ 2. 게시글 수정 ] ---");
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

           
            dto.setEmpNo("00001"); // TODO: 로그인 정보 연동
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
    
    private void viewPostsList() {
        System.out.println("--- [ 4. 게시글 전체 보기 ] ---");
        try {
            List<BoardDTO> list = boardDao.listPosts();

            if (list == null || list.isEmpty()) {
                System.out.println("\n[정보] 등록된 게시글이 없습니다.");
                return;
            }

            // 1. (수정) 헤더의 총 길이를 51 문자로 고정
            //    (데이터 printf 형식: " %-5d | %-18s | %-8s | %-10s\n")
            //    (1+5) + 3 + 18 + 3 + 8 + 3 + 10 = 51 문자
            String line = "---------------------------------------------------"; // 51개
            
            System.out.println("\n" + line);
            // 2. (수정) 헤더도 printf를 사용해 데이터와 동일한 칸을 차지하도록 함
            System.out.printf(" %-5s | %-18s | %-8s | %-10s\n", 
                                "번호", "제목", "작성자", "작성일");
            System.out.println(line);
            
            for (BoardDTO dto : list) {
                String title = dto.getTitle();
                if (title.length() > 10) {
                    title = title.substring(0, 10) + "...";
                }
                
                String regDate = dto.getRegDtm().substring(0, 10);
                
                // 3. (기존) 데이터 출력 (헤더와 형식이 동일함)
                System.out.printf(" %-5d | %-18s | %-8s | %-10s\n", 
                    dto.getBoardNo(), 
                    title, 
                    dto.getEmpNo(), 
                    regDate
                );
            }
            System.out.println(line); // 마지막 구분선

            // --- [이하 동일] ---
            while (true) {
                System.out.print("\n상세히 볼 글번호 (0: 메뉴로 돌아가기) > ");
                String input = br.readLine();
                int boardNo;

                try {
                    boardNo = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("[오류] 숫자를 입력해야 합니다.");
                    continue;
                }

                if (boardNo == 0) {
                    return; // 메뉴로
                }
                
                viewPostDetail(boardNo);
                
                System.out.println("\n--- [ 전체 목록 다시 표시 ] ---");
                list = boardDao.listPosts(); 
                
                // (수정) 갱신된 목록 출력 시에도 동일한 형식 적용
                System.out.println(line);
                System.out.printf(" %-5s | %-18s | %-8s | %-10s\n", 
                                    "번호", "제목", "작성자", "작성일");
                System.out.println(line);
                for (BoardDTO dto : list) {
                    String title = dto.getTitle();
                    if (title.length() > 10) {
                        title = title.substring(0, 10) + "...";
                    }
                    String regDate = dto.getRegDtm().substring(0, 10);
                    System.out.printf(" %-5d | %-18s | %-8s | %-10s\n", 
                        dto.getBoardNo(), title, dto.getEmpNo(), regDate);
                }
                System.out.println(line);
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
    
    private void delete() {
        System.out.println("--- [ 3. 게시글 삭제 ] ---");
        BoardDTO dto= new BoardDTO();
        int boardNo;
        try {
        	System.out.print("삭제할 글번호 ? ");
        	boardNo = Integer.parseInt(br.readLine());
        	String empNo = "00001"; // TODO: 나중에 loginInfo.loginMember().getEmpNo()로 변경
            
            
            dto.setBoardNo(boardNo);
            dto.setEmpNo(empNo);
            
            System.out.print("\n[경고] 정말 " + boardNo + "번 글을 삭제하시겠습니까? (Y/N) ");
            String confirm = br.readLine();

            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("\n[정보] 삭제를 취소했습니다.");
                return;
            }

            int result = boardDao.deletePost(dto);

            if (result > 0) {
                System.out.println("\n[정보] " + boardNo + "번 글이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("\n[오류] 글 삭제에 실패했습니다. (글번호가 없거나 삭제 권한이 없습니다)");
            }

        } catch (NumberFormatException e) {
            System.out.println("\n[오류] 글번호는 숫자로 입력해야 합니다.");
        } catch (Exception e) {
            System.out.println("\n[오류] 게시글 삭제 중 예외 발생: " + e.getMessage());
        }
    }
    
}