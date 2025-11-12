package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import com.sp.dao.BoardDAO;
import com.sp.model.BoardDTO;
import com.sp.util.LoginInfo;

public class AdminBoardUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private BoardDAO boardDao;
    private LoginInfo loginInfo;
    
    public AdminBoardUI(BoardDAO boardDao, LoginInfo loginInfo) {
        this.boardDao = boardDao;
        this.loginInfo = loginInfo;
    }
    
    public void menu() {
        int ch;
        String input;
        
        System.out.println("\n[관리자 - 게시판관리]");
        while(true) {
        	try {
        		do {
        			System.out.print("1.게시글등록 2.게시글수정 3.게시글삭제 4.게시글 보기 5.메뉴로돌아가기 => ");

        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 5);
        		
        		switch(ch) {
        		case 1: insert(); break; // BOARD_INS_001 
        		case 2: update(); break; // BOARD_UPD_002 
        		case 3: delete(); break; // BOARD_DEL_003 어드민 전용 삭제 추가
        		case 4: viewPostsList(); break;
        		case 5: return; // 4. 메뉴화면으로 
        		}
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	
        }
    }
    private void insert() {
        System.out.println("\n--- [ 1. 게시글 등록 ] ---");
        try {
           
            BoardDTO dto = new BoardDTO();

            
            System.out.print("제목 ? ");
            String title = br.readLine();
            System.out.print("내용 ? ");
            String content = br.readLine();
            
            
            dto.setTitle(title);
            dto.setContent(content);
            
           
            dto.setEmpNo("01001"); 

           
            int result = boardDao.insertPost(dto);

            
            if (result > 0) {
                System.out.println("✓ 게시글이 성공적으로 등록되었습니다.");
            } else {
                System.out.println("! 게시글 등록에 실패했습니다.");
            }

        } catch (Exception e) {
            System.out.println("! 게시글 등록 중 오류 발생: " + e.getMessage());
        }
        
    }
    
    
    private void update() {
        System.out.println("\n--- [ 2. 게시글 수정 ] ---");
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

           
            dto.setEmpNo(loginInfo.loginMember().getMemberId()); 
            int result = boardDao.updatePost(dto);

          
            if (result > 0) {
                System.out.println("✓ " + boardNo + "번 글이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("! 글 수정에 실패했습니다. (글번호 또는 권한을 확인하세요)");
            }

        } catch (NumberFormatException e) {
            System.out.println("! 글번호는 숫자로 입력해야 합니다.");
        } catch (Exception e) {
            System.out.println("! 게시글 수정 중 오류 발생: " + e.getMessage());
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
        System.out.println("\n--- [ 3. 게시글 삭제 ] ---");
        BoardDTO dto= new BoardDTO();
        int boardNo;
        try {
            // 1. 사용자로부터 삭제할 글번호 입력
        	System.out.print("삭제할 글번호 ? ");
        	boardNo = Integer.parseInt(br.readLine());
        	 // TODO: 나중에 loginInfo.loginMember().getEmpNo()로 변경
            
            
            dto.setBoardNo(boardNo);
            
            

            
            System.out.print("! 정말 " + boardNo + "번 글을 삭제하시겠습니까? (Y/N) ");
            String confirm = br.readLine();

            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("! 삭제를 취소했습니다.");
                return;
            }

            
            int result = boardDao.deletePost_Admin(dto);

            // 5. 결과 피드백
            if (result > 0) {
                System.out.println("✓ " + boardNo + "번 글이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("! 글 삭제에 실패했습니다. ");
            }

        } catch (NumberFormatException e) {
            System.out.println("! 글번호는 숫자로 입력해야 합니다.");
        } catch (Exception e) {
            System.out.println("! 게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }
    
}