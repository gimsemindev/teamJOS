package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.BoardDAO;

public class AdminBoardUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private BoardDAO boardDao;
    
    public AdminBoardUI(BoardDAO boardDao) {
        this.boardDao = boardDao;
    }
    
    public void menu() {
        int ch;
        System.out.println("\n[관리자 - 게시판관리]");
        
        try {
            do {
                System.out.print("1.게시글등록 2.게시글수정 3.게시글삭제 4.메뉴로돌아가기 => ");
                ch = Integer.parseInt(br.readLine());
            } while(ch < 1 || ch > 4);
            
            switch(ch) {
            case 1: boardDao.insertPost(null); break; // BOARD_INS_001
            case 2: boardDao.updatePost(null); break; // BOARD_UPD_002
            case 3: boardDao.deletePost(0); break; // BOARD_DEL_003
            case 4: return; // 4.메뉴화면으로
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}