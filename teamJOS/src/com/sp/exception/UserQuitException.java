package com.sp.exception;

public class UserQuitException extends RuntimeException {
    private static final long serialVersionUID = 1L; // ✅ 추가
	
    public UserQuitException() {
        super("사용자의 요청에 의해 중단합니다.");
    }
}