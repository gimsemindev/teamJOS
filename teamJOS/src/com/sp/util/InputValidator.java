package com.sp.util;

import com.sp.exception.UserQuitException;

/*
20251108:0855:김세민 임시주석
부서코드는 D로 시작하고 길이는 6이어야함
사원번호는 5자리 문자여야함
*/
public class InputValidator {
	
    public static boolean isValidDeptCode(String deptCd) {
          	
        if(!deptCd.matches("^D[A-Za-z0-9]{5}$")) {
        	System.out.println("부서코드는 'D'로 시작하고 총 6자리여야 합니다. (예: D10000)\n");
        	return false;
        }
        
        return true;
    }

    public static boolean isValidEmpNo(String empNo) {
        return empNo.matches("^[A-Za-z0-9]{5}$");
    }
    
    
    // 사용자가 q/Q 를 넣고 엔터를 치면 예외를 발생시키고 빠져나온다.
    public static void isUserExit(String q) throws RuntimeException {
        if ("q".equalsIgnoreCase(q)) {
            throw new UserQuitException();
        }
    }
    
    /**
     * 문자열이 'yyyy-MM-dd' 형식의 유효한 날짜인지 확인하는 메소드
     * 예: 2025-11-12
     * 
     * @param date 검사할 날짜 문자열
     * @return 형식이 올바르면 true, 아니면 false
     */
    public static boolean isValidDate(String date) {
        return date != null && date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    /**
     * 문자열이 올바른 이메일 형식인지 확인하는 메소드
     * 예: example@test.com
     * 
     * @param email 검사할 이메일 문자열
     * @return 이메일 형식이 올바르면 true, 아니면 false
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * 주민등록번호가 13자리 숫자인지 확인하는 메소드
     * 예: 1234561234567
     * 
     * @param rrn 검사할 주민등록번호 문자열
     * @return 13자리 숫자면 true, 아니면 false
     */
    public static boolean isValidRRN(String rrn) {
        return rrn != null && rrn.matches("^\\d{13}$");
    }

    /**
     * 문자열이 null이 아니고 공백이 아닌 값인지 확인하는 메소드
     * 
     * @param str 검사할 문자열
     * @return 비어 있지 않으면 true, 아니면 false
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * 문자열을 Integer로 안전하게 변환하는 메소드
     * 변환이 불가능한 경우 null 반환
     * 
     * @param input 숫자로 변환할 문자열
     * @return Integer 값 또는 변환 실패 시 null
     */
    public static Integer parseIntOrNull(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 사용자가 'q' 또는 'Q'를 입력하면 프로그램 종료 예외를 던지는 메소드
     * 
     * @param input 사용자 입력값
     * @throws UserQuitException 사용자가 'q'를 입력한 경우 발생
     */
    public static void checkUserExit(String input) throws UserQuitException {
        if ("q".equalsIgnoreCase(input)) {
            throw new UserQuitException();
        }
    }
   
}
