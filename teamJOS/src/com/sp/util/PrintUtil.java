package com.sp.util;

public class PrintUtil {

    // 한글은 2칸, 영어/숫자는 1칸 폭으로 계산
	public static int getDisplayWidth(String text) {
	    int width = 0;
	    for (char c : text.toCharArray()) {
	        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
	        if (block == Character.UnicodeBlock.HANGUL_SYLLABLES ||
	            block == Character.UnicodeBlock.HANGUL_JAMO ||
	            block == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO ||
	            block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
	            width += 2; // 한글, 한자 등은 2칸
	        } else {
	            width += 1;
	        }
	    }
	    return width;
	}

	// 가운데 정렬
	public static String padCenter(String text, int width) {
	    if (text == null) text = "";
	    int textWidth = getDisplayWidth(text);
	    int pad = width - textWidth;
	    if (pad <= 0) return text; // 이미 길거나 같으면 그대로 반환
	    int left = pad / 2;
	    int right = pad - left;
	    return " ".repeat(left) + text + " ".repeat(right);
	}	
	
    // 왼쪽 공백 추가
    public static String padLeft(String text, int width) {
        if (text == null) text = "";
        int textWidth = getDisplayWidth(text);
        if (textWidth >= width) return text;

        int pad = width - textWidth;
        StringBuilder sb = new StringBuilder();
        while (pad-- > 0) {
            sb.append(" ");
        }
        sb.append(text);
        return sb.toString();
    }
	
    // 오른쪽 공백
	public static String padRight(String text, int width) {
	    if (text == null) text = "";
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

	// 프로그램 상단 구분 (큰 제목)
	public static void printTitle(String title) {
		printLine('=', 65);
		System.out.println(padCenter(title, 65));
		printLine('=', 65);
	}

	// 세부 항복 구분 (소 제목)
	public static void printSection(String section) {
		printLine('-', 65);
		System.out.println(padCenter("[ " + section + " ]", 65));
		printLine('-', 65);
	}
	
	// 표 형태의 데이터 출력 시 헤더 부분 출력
	public static void printTableHeader(String...headers) {
		for (String h : headers) {
			System.out.print(padRight(h, 12));
		}
		System.out.println();
		printLine('-', 70);
	}
	
	// 표 형태 출력용 데이터 행
	public static void printTableRow(String... cols) {
	    for (String c : cols) {
	        System.out.print(padRight(c, 15));
	    }
	    System.out.println();
	    
	}
}

	

