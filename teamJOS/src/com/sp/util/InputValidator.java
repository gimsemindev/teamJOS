package com.sp.util;

import com.sp.exception.UserQuitException;

/*
20251108:0855:김세민 임시주석
부서코드는 D로 시작하고 길이는 6이어야함
사원번호는 5자리 문자여야함
*/

/**
 * <h2>InputValidator (입력값 유효성 검사 유틸리티)</h2>
 *
 * <p>사용자로부터 입력받은 데이터의 형식을 검증하고 특정 조건에 맞는지 확인하는
 * 정적(static) 메서드를 제공하는 유틸리티 클래스입니다.
 * 데이터의 무결성을 확보하고 예상치 못한 오류를 방지하는 데 사용됩니다.</p>
 *
 * <ul>
 * <li>특정 코드 형식 (부서 코드, 사원 번호) 검증</li>
 * <li>날짜, 이메일, 주민등록번호 형식 검증</li>
 * <li>문자열 비어 있는지 확인 및 안전한 숫자 변환 기능</li>
 * <li>사용자 종료 입력 처리 ({@code q} 또는 {@code Q})</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 김세민</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class InputValidator {
	
    /**
     * 부서 코드가 유효한 형식인지 검사합니다.
     * <p>부서 코드는 'D'로 시작하며, 총 6자리 문자(영문 대소문자 또는 숫자)여야 합니다. (정규식: {@code ^D[A-Za-z0-9]{5}$})</p>
     *
     * @param deptCd 검사할 부서 코드 문자열
     * @return 형식이 올바르면 true, 아니면 false (콘솔에 오류 메시지 출력)
     */
    public static boolean isValidDeptCode(String deptCd) {
          	
        if(!deptCd.matches("^D[A-Za-z0-9]{5}$")) {
        	System.out.println("부서코드는 'D'로 시작하고 총 6자리여야 합니다. (예: D10000)\n");
        	return false;
        }
        
        return true;
    }

    /**
     * 사원 번호가 유효한 형식인지 검사합니다.
     * <p>사원 번호는 영문 대소문자 또는 숫자로 구성된 5자리 문자여야 합니다. (정규식: {@code ^[A-Za-z0-9]{5}$})</p>
     *
     * @param empNo 검사할 사원 번호 문자열
     * @return 형식이 올바르면 true, 아니면 false
     */
    public static boolean isValidEmpNo(String empNo) {
        return empNo.matches("^[A-Za-z0-9]{5}$");
    }
    
    
    /**
     * 사용자 입력이 프로그램 종료 요청('q' 또는 'Q')인지 확인하고, 그렇다면 {@code UserQuitException}을 발생시킵니다.
     * <p>주로 사용자 입력 루프에서 프로그램 종료를 처리할 때 사용됩니다.</p>
     * * @param q 사용자 입력 문자열
     * @throws RuntimeException 사용자 입력이 'q' 또는 'Q'일 경우 {@code UserQuitException} 발생
     */
    public static void isUserExit(String q) throws RuntimeException {
        if ("q".equalsIgnoreCase(q)) {
            throw new UserQuitException();
        }
    }
    
    /**
     * 문자열이 'yyyy-MM-dd' 형식의 유효한 날짜인지 확인하는 메소드입니다.
     * 예: 2025-11-12
     * * @param date 검사할 날짜 문자열
     * @return 형식이 올바르면 true, 아니면 false
     */
    public static boolean isValidDate(String date) {
        return date != null && date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    /**
     * 문자열이 올바른 이메일 형식인지 확인하는 메소드입니다.
     * 예: example@test.com
     * * @param email 검사할 이메일 문자열
     * @return 이메일 형식이 올바르면 true, 아니면 false
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * 주민등록번호가 13자리 숫자인지 확인하는 메소드입니다.
     * 예: 1234561234567
     * * @param rrn 검사할 주민등록번호 문자열
     * @return 13자리 숫자면 true, 아니면 false
     */
    public static boolean isValidRRN(String rrn) {
        return rrn != null && rrn.matches("^\\d{13}$");
    }

    /**
     * 문자열이 null이 아니고 공백이 아닌 값인지 확인하는 메소드입니다.
     * * @param str 검사할 문자열
     * @return 비어 있지 않으면 true, 아니면 false
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * 문자열을 Integer로 안전하게 변환하는 메소드입니다.
     * <p>변환이 불가능한 경우 {@code null}을 반환하며, {@code NumberFormatException}을 방지합니다.</p>
     * * @param input 숫자로 변환할 문자열
     * @return Integer 값 또는 변환 실패 시 null
     */
    public static Integer parseIntOrNull(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return null;
          
        }
    }

}