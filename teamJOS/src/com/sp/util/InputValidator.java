package com.sp.util;

import com.sp.exception.UserQuitException;

/*
20251108:0855:김세민 임시주석
부서코드는 D로 시작하고 길이는 6이어야함
사원번호는 5자리 문자여야함
*/
public class InputValidator {
	
    public static boolean isValidDeptCode(String deptCd) {
        System.out.println("부서코드는 'D'로 시작하고 총 6자리여야 합니다. (예: D10000)\n");
        return deptCd.matches("^D[A-Za-z0-9]{5}$");
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
}
