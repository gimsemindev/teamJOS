package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.BoardDAO;
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
        		case 1: boardDao.insertPost(null); break; // BOARD_INS_001 
        		case 2: boardDao.updatePost(null); break; // BOARD_UPD_002 
        		case 3: boardDao.deletePost(0); break; // BOARD_DEL_003 
        		case 4: boardDao.listPosts(); break;
        		case 5: return; // 4. 메뉴화면으로 
        		}
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	
        }
    }
}