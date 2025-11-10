package com.sp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.sp.dao.AuthDAO;
import com.sp.util.LoginInfo;




public class AdminAuthUI {
	final String RESET  = "\u001B[0m";
    final String GREEN  = "\u001B[32m";
    final String YELLOW = "\u001B[33m";
	
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private AuthDAO authDao;
    private LoginInfo loginInfo;
    
    private static final String ADMIN_LEVEL_CODE = "3";
    private static final String EMPLOYEE_LEVEL_CODE = "1";
    
    public AdminAuthUI(AuthDAO authDao, LoginInfo loginInfo) {
        this.authDao = authDao;
        this.loginInfo = loginInfo;
    }
    
    public void menu() {
        int ch;
        String input;
        
        System.out.println("\n[관리자 - 권한관리]");
        while(true) {
        	
        	try {
        		do {
        			System.out.print("1.관리자정보수정 2.관리자계정등록 3.메뉴로돌아가기 => ");
        			
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
        		//case 3: deleteAdmin(); break; // AUTH_DEL_003
        		case 3: return; // 4.메뉴화면으로
        		}
        		
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
    }
    
    public void updateAdmin() {
    	System.out.println("\n[관리자 계정 수정]");
    	String empNo = null;
    	
    	try {
            System.out.print("수정할 사번 (관리자 ID): ");
            empNo = br.readLine();
            
            int result = authDao.insertAdmin(empNo, EMPLOYEE_LEVEL_CODE);
            
            if (result > 0) {
                System.out.println(GREEN + "✅ 권한 수정 완료! 사번 " + empNo + "이(가) 일반사원으로 변경되었습니다." + RESET);
            } else {
                System.out.println(YELLOW + "❌ 권한 수정 실패: 해당 사번이 존재하지 않거나, 이미 관리자입니다." + RESET);
            }
            
        } catch (IOException e) {
            System.err.println("입력 중 오류 발생.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    		
    }
    
    public void insertAdmin() {
    	System.out.println("\n[관리자 계정 등록]");
    	String empNo = null;
    	
    	try {
            System.out.print("등록할 사번 (관리자 ID): ");
            empNo = br.readLine();
            
            int result = authDao.insertAdmin(empNo, ADMIN_LEVEL_CODE);
            
            if (result > 0) {
                System.out.println(GREEN + "✅ 권한 등록 완료! 사번 " + empNo + "이(가) 관리자로 승급되었습니다." + RESET);
            } else {
                System.out.println(YELLOW + "❌ 권한 등록 실패: 해당 사번이 존재하지 않거나, 이미 관리자입니다." + RESET);
            }
            
        } catch (IOException e) {
            System.err.println("입력 중 오류 발생.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
  //  public void deleteAdmin() {
    	
    //}
}