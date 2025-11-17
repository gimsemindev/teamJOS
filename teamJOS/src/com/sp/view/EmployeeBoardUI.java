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
        		do {
        			printTitle("📌 [게시판] 📌");
        			printMenu(YELLOW, "① 게시글 등록", "② 게시글 수정", "③ 게시글 삭제", "④ 게시글 보기");
//        			System.out.print("선택 > "); // 프롬프트 변경
        			input = br.readLine();
        			InputValidator.isUserExit(input);
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
        		} while(ch < 1 || ch > 4);
        		
                System.out.println(); // 선택 후 한 줄 띄우기

        		switch(ch) {
        		case 1: insert(); break; 
        		case 2: update(); break; 
        		case 3: delete(); break; 
        		case 4: viewPostsList(); break;
        		}
        		
                System.out.println(); // 각 작업 후 한 줄 띄우기

        	} catch (NumberFormatException e) {
				printLineln(MAGENTA, "📢 1 ~ 4 사이의 숫자만 입력 가능합니다.");
			} catch (UserQuitException e) {
				printLineln(MAGENTA, "📢 작업을 취소하였습니다.");
		    } catch (Exception e) {
		    	printLineln(MAGENTA, "📢 [오류] 알 수 없는 예외가 발생했습니다.");
        		// e.printStackTrace(); // (디버깅 시 필요)
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
        	printLine(GREEN, "👉 삭제할 글 번호 : ");
        	boardNo = Integer.parseInt(br.readLine());
        	 // TODO: 나중에 loginInfo.loginMember().getEmpNo()로 변경
            
            
            dto.setBoardNo(boardNo);
            dto.setEmpNo(loginInfo.loginMember().getMemberId());
            
            printLineln(MAGENTA, "📢 [경고] 정말 " + boardNo + "번 글을 삭제하시겠습니까? (Y/N) ");
            String confirm = br.readLine();

            if (!confirm.equalsIgnoreCase("Y")) {
            	printLineln(MAGENTA, "📢 [정보] 삭제를 취소했습니다.");
                return;
            }

            int result = boardDao.deletePost(dto);

            if (result > 0) {
            	printLineln(MAGENTA, "📢 [정보] " + boardNo + "번 글이 성공적으로 삭제되었습니다.");
            } else {
            	printLineln(MAGENTA, "📢 [오류] 글 삭제에 실패했습니다. (글번호가 없거나 삭제 권한이 없습니다)");
            }

        } catch (NumberFormatException e) {
        	printLineln(MAGENTA, "📢 [오류] 글번호는 숫자로 입력해야 합니다.");
        } catch (Exception e) {
        	printLineln(MAGENTA, "📢 [오류] 게시글 삭제 중 예외 발생: " + e.getMessage());
        }
    }
    
}