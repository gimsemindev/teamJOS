package com.sp.util;

public class PrintUtil {
	
	public static final String RESET = "\u001B[0m";
	public static final String BLUE = "\u001B[34m";     // 파란색
	public static final String YELLOW = "\u001B[33m";   // 노란색
	public static final String GREEN = "\u001B[32m";    // 초록색
	public static final String CYAN = "\u001B[36m";     // 청록색
	public static final String GRAY = "\u001B[90m";	 	// 회색
	public static final String MAGENTA = "\u001B[35m";
	public static final String LIGHT_YELLOW = "\u001B[93m";
	public static final String RED        = "\u001B[31m";

	// 한글은 2칸, 영어/숫자는 1칸 폭으로 계산
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

	// 가운데 정렬
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

	// 왼쪽 공백 추가
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

	// 오른쪽 공백
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

	// 라인 반복 출력
	public static void printLine(char ch, int length) {
		System.out.println(String.valueOf(ch).repeat(length));
	}
	
    // 컬러 라인 반복 출력
    public static void printLine(char ch, int length, String color) {
        System.out.print(color);
        System.out.print(String.valueOf(ch).repeat(length));
        System.out.println(RESET);
    }

	// 프로그램 상단 구분 (큰 제목)
	public static void printTitle(String title) {
		printLine('═', 65, CYAN);
		System.out.println(YELLOW + padCenter(title, 65) + RESET);
		printLine('═', 65, CYAN);
	}

	// 세부 항복 구분 (소 제목)
	public static void printSection(String section) {
		printLine('─', 65, CYAN);
		System.out.println(padCenter(YELLOW + "[ " + section + " ]", 65) + RESET);
		printLine('─', 65, CYAN);
	}

	// 표 형태의 데이터 출력 시 헤더 부분 출력
	public static void printTableHeader(String... headers) {
		for (String h : headers) {
			System.out.print(padRight(h, 12));
		}
		System.out.println();
		printLine('─', 70);
	}

	// 표 형태 출력용 데이터 행
	public static void printTableRow(String... cols) {
		for (String c : cols) {
			System.out.print(padRight(c, 15));
		}
		System.out.println();

	}
	
	public static void printMenu(String color, String... items) {
	    // 상단 라인
		printLine('─', 65, GRAY);

	    // 메뉴 항목 출력
	    for (int i = 0; i < items.length; i++) {
	        System.out.println(color + String.format("   %s", items[i]) + RESET);
	    }

	    // 하단 라인
	    printLine('─', 65, GRAY);
	    
	    System.out.print(GREEN + "👉 메뉴 선택 : " + RESET);
	}
	
	public static void printLine(String color, String str) {
		System.out.print(color + str + RESET);
	}
	
	public static void printLineln(String color, String str) {
		System.out.println("\n" + color + str + RESET);
	}

	

	// 표시 폭 기준 오른쪽 패딩
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

	// 표시 폭 기준 가운데 정렬
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

	// 한글/영문 폭 계산해서 잘라내기
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

