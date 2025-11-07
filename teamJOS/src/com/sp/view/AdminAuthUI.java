package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.AuthDAO;

public class AdminAuthUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private AuthDAO authDao;
    
    public AdminAuthUI(AuthDAO authDao) {
        this.authDao = authDao;
    }
    
    public void menu() {
        int ch;
        System.out.println("\n[관리자 - 권한관리]");
        
        try {
            do {
                System.out.print("1.관리자정보수정 2.관리자계정등록 3.관리자계정삭제 4.메뉴로돌아가기 => ");
                ch = Integer.parseInt(br.readLine());
            } while(ch < 1 || ch > 4);
            
            switch(ch) {
            case 1: updateAdmin(); break; // AUTH_UPD_002
            case 2: insertAdmin(); break; // AUTH_INS_001
            case 3: deleteAdmin(); break; // AUTH_DEL_003
            case 4: return; // 4.메뉴화면으로
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateAdmin() {
    	
    }
    
    public void insertAdmin() {
    	
    }
    
    public void deleteAdmin() {
    	
    }
}