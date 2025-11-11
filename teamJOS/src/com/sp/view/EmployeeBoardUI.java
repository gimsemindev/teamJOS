package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import com.sp.dao.BoardDAO;
import com.sp.model.BoardDTO;
import com.sp.model.LoginDTO;
import com.sp.util.LoginInfo;

public class EmployeeBoardUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private BoardDAO boardDao;
    private LoginInfo loginInfo;
    
    public EmployeeBoardUI(BoardDAO boardDao, LoginInfo loginInfo) {
        this.boardDao = boardDao;
        this.loginInfo = loginInfo;
        
    }
    
    // EmployeeUI의 manageBoard() 기능을 menu()로 변경
    public void menu() {
        int ch;
        String input;
        
        System.out.println("\n[게시판관리]");
        
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
        		case 3: delete(); break; // BOARD_DEL_003 
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
            
           
            dto.setEmpNo("00001"); 

           
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

           
            dto.setEmpNo("00001"); 
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
        System.out.println("\n--- [ 4. 게시글 전체 보기 ] ---");
        try {
            // 1. DAO로부터 데이터 리스트를 받음 (출력 X)
            List<BoardDTO> list = boardDao.listPosts();

            if (list == null || list.isEmpty()) {
                System.out.println("! 등록된 게시글이 없습니다.");
                return;
            }

            // 2. [UI 담당] 리스트를 간략하게 출력
            System.out.println("──────────────────────────────────────────────────");
            System.out.println("  번호  |        제목        |  작성자  |   작성일");
            System.out.println("──────────────────────────────────────────────────");
            
            for (BoardDTO dto : list) {
                // 제목이 너무 길면 잘라내기
                String title = dto.getTitle();
                if (title.length() > 10) {
                    title = title.substring(0, 10) + "...";
                }
                
                // 날짜만 표시 (시간 제외)
                String regDate = dto.getRegDtm().substring(0, 10);
                
                System.out.printf(" %-5d | %-18s | %-8s | %-10s\n", 
                    dto.getBoardNo(), 
                    title, 
                    dto.getEmpNo(), // (추후 사원 이름으로 변경 가능)
                    regDate
                );
            }
            System.out.println("──────────────────────────────────────────────────");

            // 3. [UI 담당] 사용자에게 상세 보기할 글번호 입력받기
            while (true) {
                System.out.print("👉 상세히 볼 글번호를 입력하세요 (0: 메뉴로 돌아가기) => ");
                String input = br.readLine();
                int boardNo = Integer.parseInt(input);

                if (boardNo == 0) {
                    return; // 메뉴로
                }
                
                // 4. 상세 보기 메소드 호출
                viewPostDetail(boardNo);
                // 상세 보기가 끝나면 목록을 다시 보여주기 위해 루프를 빠져나가지 않음
                // (만약 상세 보기 후 바로 메뉴로 가고 싶다면 viewPostDetail 호출 후 return;)
                
                // 상세 보기 후 목록을 다시 보여주기 전, 목록을 다시 로드할 수도 있음 (선택 사항)
                list = boardDao.listPosts(); // (선택) 데이터 갱신
            }

        } catch (NumberFormatException e) {
            System.out.println("! 글번호는 숫자로 입력해야 합니다.");
        } catch (Exception e) {
            System.out.println("! 게시글 조회 중 오류: " + e.getMessage());
        }
    }
    
    private void viewPostDetail(int boardNo) {
        try {
            // 1. DAO로부터 DTO 하나를 받음
            BoardDTO dto = boardDao.getPost(boardNo);

            if (dto == null) {
                System.out.println("! 해당 번호의 게시글이 존재하지 않습니다.");
                return;
            }

            // 2. [UI 담당] DTO의 모든 내용 출력
            System.out.println("\n--- [ " + boardNo + "번 게시글 상세 보기 ] ---");
            System.out.println(" 글번호: " + dto.getBoardNo());
            System.out.println(" 제  목: " + dto.getTitle());
            System.out.println(" 작성자: " + dto.getEmpNo());
            System.out.println(" 작성일: " + dto.getRegDtm());
            
            if (dto.getUpdateDtm() != null) {
                System.out.println(" 수정일: " + dto.getUpdateDtm());
            }
            
            System.out.println("──────────────────────────────────────────────────");
            System.out.println(dto.getContent()); // 내용 출력
            System.out.println("──────────────────────────────────────────────────");
            System.out.println("(엔터를 누르면 목록으로 돌아갑니다.)");
            br.readLine(); // 사용자가 내용을 읽을 때까지 대기

        } catch (Exception e) {
            System.out.println("! 상세 보기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
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
        	String empNo = "00001"; // TODO: 나중에 loginInfo.loginMember().getEmpNo()로 변경
            
            
            dto.setBoardNo(boardNo);
            dto.setEmpNo(empNo);
            
            // 2. [임시] 본인 확인용 사번 (로그인 기능 연동 시 변경)

            // 3. (중요) 사용자에게 삭제 재확인
            System.out.print("! 정말 " + boardNo + "번 글을 삭제하시겠습니까? (Y/N) ");
            String confirm = br.readLine();

            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("! 삭제를 취소했습니다.");
                return;
            }

            // 4. DAO에 삭제 요청 (글번호와 사번을 넘겨 본인 글인지 확인)
            int result = boardDao.deletePost(dto);

            // 5. 결과 피드백
            if (result > 0) {
                System.out.println("✓ " + boardNo + "번 글이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("! 글 삭제에 실패했습니다. (글번호가 없거나 삭제 권한이 없습니다)");
            }

        } catch (NumberFormatException e) {
            System.out.println("! 글번호는 숫자로 입력해야 합니다.");
        } catch (Exception e) {
            System.out.println("! 게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }
    
}

