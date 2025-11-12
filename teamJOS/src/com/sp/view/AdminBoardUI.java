package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.BoardDAO;
import com.sp.model.BoardDTO;
import com.sp.util.LoginInfo;
import com.sp.view.common.BoardCommonUI;

public class AdminBoardUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private BoardDAO boardDao;
    private LoginInfo loginInfo;
    private BoardCommonUI boardCommonUI;
    
    public AdminBoardUI(BoardDAO boardDao, LoginInfo loginInfo) {
        this.boardDao = boardDao;
        this.loginInfo = loginInfo;
        this.boardCommonUI = new BoardCommonUI(loginInfo);
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