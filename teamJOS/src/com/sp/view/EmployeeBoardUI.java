package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.BoardDAO;
import com.sp.model.BoardDTO;
import com.sp.util.LoginInfo;
import com.sp.view.common.BoardCommonUI;

public class EmployeeBoardUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private BoardDAO boardDao;
    private LoginInfo loginInfo;
    private BoardCommonUI boardCommonUI;
    
    
    public EmployeeBoardUI(BoardDAO boardDao, LoginInfo loginInfo) {
        this.boardDao = boardDao;
        this.loginInfo = loginInfo;
        this.boardCommonUI = new BoardCommonUI(loginInfo);
        
    }
    
    // EmployeeUI의 manageBoard() 기능을 menu()로 변경
    public void menu() {
        int ch;
        String input;
        
        
        while(true) {
        	try {
                System.out.println("\n========================================");
                System.out.println("           [ 게시판 ]");
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
    		boardCommonUI.insert();
    		} catch (Exception e) {
    			e.printStackTrace();
    	}
    }
    
    private void update() {
        System.out.println("--- [ 2. 게시글 수정 ] ---");
    	try {
    		boardCommonUI.update();
    		} catch (Exception e) {
    			e.printStackTrace();
    	}
    }
  
    private void viewPostsList() {
        System.out.println("--- [ 4. 게시글 전체 보기 ] ---");
    	try {
    	     boardCommonUI.viewPostsList();
    	} catch (Exception e) {
    		e.printStackTrace();
    	} 	
    }
    
    private void delete() {
        System.out.println("--- [ 3. 게시글 삭제 ] ---");
        BoardDTO dto= new BoardDTO();
        int boardNo;
        try {
        	System.out.print("삭제할 글번호 ? ");
        	boardNo = Integer.parseInt(br.readLine());
        	 // TODO: 나중에 loginInfo.loginMember().getEmpNo()로 변경
            
            
            dto.setBoardNo(boardNo);
            dto.setEmpNo(loginInfo.loginMember().getMemberId());
            
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