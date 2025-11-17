package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.BoardDAO;
import com.sp.exception.UserQuitException;
import com.sp.model.BoardDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.view.common.BoardCommonUI;

import static com.sp.util.PrintUtil.*;

public class AdminBoardUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private BoardDAO boardDao;
    @SuppressWarnings("unused")
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
        
        printTitle("🏢 [관리자 - 게시판관리]");
        while(true) {
        	try {
        		do {
        			printMenu(YELLOW, "① 게시글 등록", "② 게시글 수정", "③ 게시글 삭제", "④ 게시글 보기");

        			input = br.readLine();
        			InputValidator.isUserExit(input);
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 4);
        		
        		switch(ch) {
        		case 1: insert(); break; // BOARD_INS_001 
        		case 2: update(); break; // BOARD_UPD_002 
        		case 3: delete(); break; // BOARD_DEL_003 어드민 전용 삭제 추가
        		case 4: viewPostsList(); break;
        		}
        		
        	} catch (NumberFormatException e) {
        		printLineln(MAGENTA, "📢 1 ~ 4 사이의 숫자만 입력 가능합니다.");
			} catch (UserQuitException e) {
				printLineln(MAGENTA, "📢 작업을 취소하였습니다.");
				return;
		    } catch (Exception e) {
        		e.printStackTrace();
        	}
        	
        }
    }
    private void insert() {
    	printTitle("📝 [게시글 등록]");
        
    	try {
    		boardCommonUI.insert();
    		} catch (Exception e) {
    			e.printStackTrace();
    	}
    }
    
    
    private void update() {
    	printTitle("✏️ [게시글 수정]");
    	try {
    		boardCommonUI.update();
    		} catch (Exception e) {
    			e.printStackTrace();
    	}
    }
    
    private void viewPostsList() {
    	printTitle("🗂️ [게시글 전체 보기]");
    	try {
    	     boardCommonUI.viewPostsList();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    private void delete() {
    	printTitle("🗑️ [게시글 삭제]");
        BoardDTO dto= new BoardDTO();
        int boardNo;
        try {
            // 1. 사용자로부터 삭제할 글번호 입력
        	printLine(GREEN, "👉 삭제할 글 번호를 입력하세요. : ");
        	boardNo = Integer.parseInt(br.readLine());
        	 // TODO: 나중에 loginInfo.loginMember().getEmpNo()로 변경
            
            dto.setBoardNo(boardNo);
            
            printLine(MAGENTA, "❓ 정말 " + boardNo + "번 글을 삭제하시겠습니까? (Y/N) : ");
            String confirm = br.readLine();

            if (!confirm.equalsIgnoreCase("Y")) {
            	printLineln(MAGENTA, "📢 삭제를 취소했습니다.");
                return;
            }

            
            int result = boardDao.deletePost_Admin(dto);

            // 5. 결과 피드백
            if (result > 0) {
            	printLine(MAGENTA, "✓ " + boardNo + "번 글이 성공적으로 삭제되었습니다.");
            } else {
            	printLineln(MAGENTA, "📢 글 삭제에 실패했습니다. ");
            }

        } catch (NumberFormatException e) {
        	printLineln(MAGENTA, "📢 글번호는 숫자로 입력해야 합니다.");
        } catch (Exception e) {
        	printLineln(MAGENTA, "📢 게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }
    
}