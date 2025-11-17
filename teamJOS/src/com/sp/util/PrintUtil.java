package com.sp.util;

/**
 * <h2>PrintUtil (콘솔 출력 유틸리티)</h2>
 *
 * <p>콘솔 환경에서 텍스트의 색상 지정, 정렬, 폭 계산 및 표 형태의 구조화된
 * 출력을 지원하는 정적 메서드를 제공하는 유틸리티 클래스입니다.
 * 사용자 인터페이스(UI)를 구성하고 가독성을 높이는 데 사용됩니다.</p>
 *
 * <ul>
 * <li>ANSI 이스케이프 코드를 사용한 다양한 색상 정의 및 적용</li>
 * <li>한글(2칸) 및 영문/숫자(1칸)를 고려한 정확한 표시 폭 계산</li>
 * <li>문자열의 좌/우/가운데 정렬 및 패딩 기능</li>
 * <li>제목, 구분선, 메뉴, 테이블 형태의 일관된 출력 형식 제공</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 김세민</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class PrintUtil {
	
    /** ANSI 이스케이프 코드: 색상 리셋 */
	public static final String RESET = "\u001B[0m";
    /** ANSI 이스케이프 코드: 파란색 */
	public static final String BLUE = "\u001B[34m";     // 파란색
    /** ANSI 이스케이프 코드: 노란색 */
	public static final String YELLOW = "\u001B[33m";   // 노란색
    /** ANSI 이스케이프 코드: 초록색 */
	public static final String GREEN = "\u001B[32m";    // 초록색
    /** ANSI 이스케이프 코드: 청록색 */
	public static final String CYAN = "\u001B[36m";     // 청록색
    /** ANSI 이스케이프 코드: 회색 */
	public static final String GRAY = "\u001B[90m";	 	// 회색
    /** ANSI 이스케이프 코드: 마젠타색 */
	public static final String MAGENTA = "\u001B[35m";
    /** ANSI 이스케이프 코드: 밝은 노란색 */
	public static final String LIGHT_YELLOW = "\u001B[93m";
    /** ANSI 이스케이프 코드: 빨간색 */
	public static final String RED        = "\u001B[31m";

	/**
	 * 문자열의 콘솔 표시 폭(Display Width)을 계산합니다.
	 * <p>일반적으로 한글/한자는 2칸, 영문/숫자는 1칸 폭으로 계산합니다.</p>
	 *
	 * @param text 폭을 계산할 문자열
	 * @return 계산된 표시 폭
	 */
	public static int getDisplayWidth(String text) {
		int width = 0;
		for (char c : text.toCharArray()) {
			Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
			if (block == Character.UnicodeBlock.HANGUL_SYLLABLES || block == Character.UnicodeBlock.HANGUL_JAMO
					|| block == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
					|| block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
				width += 2; // 한글, 한자 등은 2칸
			} else {
				width += 1;
			}
		}
		return width;
	}

	/**
	 * 문자열을 주어진 폭 내에서 가운데 정렬하여 공백(패딩)을 추가합니다.
	 *
	 * @param text 정렬할 문자열
	 * @param width 전체 폭
	 * @return 가운데 정렬된 문자열
	 */
	public static String padCenter(String text, int width) {
		if (text == null)
			text = "";
		int textWidth = getDisplayWidth(text);
		int pad = width - textWidth;
		if (pad <= 0)
			return text; // 이미 길거나 같으면 그대로 반환
		int left = pad / 2;
		int right = pad - left;
		return " ".repeat(left) + text + " ".repeat(right);
	}

	/**
	 * 문자열 왼쪽에 공백을 추가하여 오른쪽 정렬 효과를 냅니다.
	 *
	 * @param text 정렬할 문자열
	 * @param width 전체 폭
	 * @return 왼쪽 공백이 추가된 문자열 (오른쪽 정렬)
	 */
	public static String padLeft(String text, int width) {
		if (text == null)
			text = "";
		int textWidth = getDisplayWidth(text);
		if (textWidth >= width)
			return text;

		int pad = width - textWidth;
		StringBuilder sb = new StringBuilder();
		while (pad > 0) {
			sb.append(" ");
			pad = pad - 1;
		}
		sb.append(text);
		return sb.toString();
	}

	/**
	 * 문자열 오른쪽에 공백을 추가하여 왼쪽 정렬 효과를 냅니다.
	 *
	 * @param text 정렬할 문자열
	 * @param width 전체 폭
	 * @return 오른쪽 공백이 추가된 문자열 (왼쪽 정렬)
	 */
	public static String padRight(String text, int width) {
		if (text == null)
			text = "";
		int displayWidth = getDisplayWidth(text);
		StringBuilder sb = new StringBuilder(text);
		while (displayWidth < width) {
			sb.append(" ");
			displayWidth++;
		}
		return sb.toString();
	}

	/**
	 * 특정 문자를 지정된 길이만큼 반복하여 출력합니다. (줄 바꿈 포함)
	 *
	 * @param ch 반복할 문자
	 * @param length 반복 길이
	 */
	public static void printLine(char ch, int length) {
		System.out.println(String.valueOf(ch).repeat(length));
	}
	
    /**
     * 특정 문자를 지정된 길이만큼 반복하여 컬러로 출력합니다. (줄 바꿈 포함)
     *
     * @param ch 반복할 문자
     * @param length 반복 길이
     * @param color 적용할 ANSI 컬러 코드
     */
    public static void printLine(char ch, int length, String color) {
        System.out.print(color);
        System.out.print(String.valueOf(ch).repeat(length));
        System.out.println(RESET);
    }

	/**
	 * 프로그램의 주 제목을 형식에 맞춰 출력합니다. (굵은 이중선 사용)
	 *
	 * @param title 출력할 제목 문자열
	 */
	public static void printTitle(String title) {
		printLine('═', 65, CYAN);
		System.out.println(YELLOW + padCenter(title, 65) + RESET);
		printLine('═', 65, CYAN);
	}

	/**
	 * 프로그램 섹션 또는 소제목을 형식에 맞춰 출력합니다. (가는 단일선 사용)
	 *
	 * @param section 출력할 섹션 제목 문자열
	 */
	public static void printSection(String section) {
		printLine('─', 65, CYAN);
		System.out.println(padCenter(YELLOW + "[ " + section + " ]", 65) + RESET);
		printLine('─', 65, CYAN);
	}

	/**
	 * 테이블 헤더를 출력합니다. (각 헤더 폭 12칸으로 고정)
	 *
	 * @param headers 헤더 문자열 배열
	 */
	public static void printTableHeader(String... headers) {
		for (String h : headers) {
			System.out.print(padRight(h, 12));
		}
		System.out.println();
		printLine('─', 70);
	}

	/**
	 * 테이블의 데이터 행을 출력합니다. (각 컬럼 폭 15칸으로 고정)
	 *
	 * @param cols 컬럼 데이터 문자열 배열
	 */
	public static void printTableRow(String... cols) {
		for (String c : cols) {
			System.out.print(padRight(c, 15));
		}
		System.out.println();

	}
	
	/**
	 * 메뉴 항목 리스트를 형식에 맞춰 출력하고 사용자 입력을 대기합니다.
	 *
	 * @param color 메뉴 항목에 적용할 ANSI 컬러 코드
	 * @param items 메뉴 항목 문자열 배열
	 */
	public static void printMenu(String color, String... items) {
	    // 상단 라인
		printLine('─', 65, GRAY);

	    // 메뉴 항목 출력
	    for (int i = 0; i < items.length; i++) {
	        System.out.println(color + String.format("   %s", items[i]) + RESET);
	    }

	    // 하단 라인
	    printLine('─', 65, GRAY);
	    
	    System.out.print(GREEN + "👉 메뉴 선택 [ q : 돌아가기 ] : " + RESET);
	}
	
	/**
	 * 지정된 컬러로 문자열을 출력합니다. (줄 바꿈 없음)
	 *
	 * @param color 적용할 ANSI 컬러 코드
	 * @param str 출력할 문자열
	 */
	public static void printLine(String color, String str) {
		System.out.print(color + str + RESET);
	}
	
	/**
	 * 지정된 컬러로 문자열을 출력하고 앞뒤에 줄 바꿈을 추가합니다.
	 *
	 * @param color 적용할 ANSI 컬러 코드
	 * @param str 출력할 문자열
	 */
	public static void printLineln(String color, String str) {
		System.out.println("\n" + color + str + RESET + "\n");
	}

	

	/**
	 * 문자열 오른쪽에 공백을 추가하여 왼쪽 정렬 효과를 냅니다. (한글/영문 폭 계산 기준)
	 * <p>문자열의 표시 폭이 주어진 폭을 초과하면 잘라냅니다. ({@code cutDisplay} 사용)</p>
	 *
	 * @param s 정렬할 문자열
	 * @param width 전체 폭
	 * @return 오른쪽 공백이 추가된 문자열
	 */
	public static String padRightDisplay(String s, int width) {
		if (s == null)
			s = "";
		if (getDisplayWidth(s) > width) {
			s = cutDisplay(s, width);
		}
		int dw = getDisplayWidth(s);
		StringBuilder sb = new StringBuilder(s);
		for (int i = dw; i < width; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}

	/**
	 * 문자열을 주어진 폭 내에서 가운데 정렬하여 공백(패딩)을 추가합니다. (한글/영문 폭 계산 기준)
	 * <p>문자열의 표시 폭이 주어진 폭을 초과하면 잘라냅니다. ({@code cutDisplay} 사용)</p>
	 *
	 * @param s 정렬할 문자열
	 * @param width 전체 폭
	 * @return 가운데 정렬된 문자열
	 */
	public static String padCenterDisplay(String s, int width) {
		if (s == null)
			s = "";
		if (getDisplayWidth(s) > width) {
			s = cutDisplay(s, width);
		}
		int dw = getDisplayWidth(s);
		if (dw >= width)
			return s;

		int left = (width - dw) / 2;
		int right = width - dw - left;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < left; i++)
			sb.append(' ');
		sb.append(s);
		for (int i = 0; i < right; i++)
			sb.append(' ');
		return sb.toString();
	}

	/**
	 * 문자열을 표시 폭 기준으로 지정된 최대 폭에 맞게 잘라냅니다.
	 * <p>한글/한자(2칸)를 정확히 계산하여 텍스트가 깨지지 않도록 처리합니다.</p>
	 *
	 * @param s 잘라낼 문자열
	 * @param maxWidth 최대 표시 폭
	 * @return 잘라낸 문자열
	 */
	public static String cutDisplay(String s, int maxWidth) {
		if (s == null)
			return "";
		int w = 0;
		StringBuilder sb = new StringBuilder();
		for (char c : s.toCharArray()) {
			int cw = getDisplayWidth(String.valueOf(c));
			if (w + cw > maxWidth)
				break;
			sb.append(c);
			w += cw;
		}
		return sb.toString();
	}
}