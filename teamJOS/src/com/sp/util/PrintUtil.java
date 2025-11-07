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
	
	
    // 오른쪽으로 공백 채워서 일정 폭으로 맞추기
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
	
}
