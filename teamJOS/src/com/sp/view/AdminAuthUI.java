package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sp.dao.AuthDAO;
import com.sp.model.AdminDTO;



public class AdminAuthUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private AuthDAO authDao;
    
    public AdminAuthUI(AuthDAO authDao) {
        this.authDao = authDao;
    }
    
    public void menu() {
        int ch;
        String input;
        
        System.out.println("\n[관리자 - 권한관리]");
        while(true) {
        	
        	try {
        		do {
        			System.out.print("1.관리자정보수정 2.관리자계정등록 3.관리자계정삭제 4.메뉴로돌아가기 => ");
        			
        			input = br.readLine();
                    
                    if(input == null || input.trim().isEmpty()) {
                    	ch = 0;
                    	continue;
                    }
                    ch = Integer.parseInt(input);
        			
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
    }
    
    public void updateAdmin() {
    	AdminDTO dto = new AdminDTO();
    	try {
    		authDao.updateAdmin(dto);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    public void insertAdmin() {
    	
    }
    
    public void deleteAdmin() {
    	
    }
}