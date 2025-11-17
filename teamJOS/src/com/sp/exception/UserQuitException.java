package com.sp.exception;

/**
 * <h2>UserQuitException (사용자 종료 요청 예외)</h2>
 *
 * <p>사용자가 프로그램 입력 단계에서 'q' 또는 'Q'와 같은 종료 신호를 보냈을 때,
 * 현재 실행 중인 작업을 안전하게 중단하고 이전 단계 또는 메인 루프로 돌아가기 위해
 * 설계된 런타임 예외(RuntimeException) 클래스입니다.</p>
 *
 * <p><b>특징:</b></p>
 * <ul>
 * <li>RuntimeException을 상속받아 호출 메서드에서 예외 처리를 강제하지 않습니다.</li>
 * <li>프로그램 흐름을 제어하는 유틸리티 메서드({@code InputValidator.isUserExit()})에서 사용됩니다.</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 김세민</p>
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class UserQuitException extends RuntimeException {
    
    /** 유효성 관리를 위한 기본 serialVersionUID */
    private static final long serialVersionUID = 1L;
	
    /**
     * 기본 생성자입니다.
     * <p>기본 예외 메시지를 "사용자의 요청에 의해 중단합니다."로 설정합니다.</p>
     */
    public UserQuitException() {
        super("사용자의 요청에 의해 중단합니다.");
    }
}