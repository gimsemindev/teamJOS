package com.sp.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * <h2>FileDownloadUtil (파일 다운로드 유틸리티)</h2>
 *
 * <p>데이터베이스의 조회 결과를 CSV 파일로 추출(Export)하는 정적 메서드를 제공하는 유틸리티 클래스입니다.
 * DTO 클래스의 필드 정보와 ResultSet을 매핑하여 CSV 파일을 생성합니다.</p>
 *
 * <ul>
 * <li>데이터 추출 시 CSV 안전 처리를 위한 로직 포함.</li>
 * <li>파일 이름에 타임스탬프를 포함하여 중복을 방지.</li>
 * <li>컬럼 매핑을 통해 DB 컬럼명과 DTO 필드명이 다른 경우를 처리.</li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 김세민</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class FileDownloadUtil {
	
    /**
     * 데이터베이스 조회 결과를 CSV 파일로 추출합니다.
     *
     * @param <T> DTO 클래스 타입
     * @param filePrefix 생성될 CSV 파일명의 접두사
     * @param conn 데이터베이스 연결 객체
     * @param dtoClass 데이터를 매핑할 DTO 클래스
     * @param columnMapping DTO 필드명과 DB 컬럼명을 매핑하는 Map (필드명 -> 컬럼명)
     * @param sql 실행할 SQL 쿼리 문자열
     * @throws Exception 파일 입출력 또는 SQL 실행 실패 시
     */
    public static <T> void exportToCsv(String filePrefix, Connection conn, Class<T> dtoClass, Map<String, String> columnMapping, String sql) throws Exception {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = filePrefix + "_" + timestamp + ".csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {

                Field[] fields = dtoClass.getDeclaredFields();

                // 헤더 자동 생성 : 마지막 컬럼에는 , 붙으면 안됨
                for (int i = 0; i < fields.length; i++) {
                    bw.write(fields[i].getName());
                    if (i < fields.length - 1) {
                    	bw.write(",");
                    }
                }
                bw.newLine();

                // 각 행 데이터를 필드명 기준으로 가져와 출력
                while (rs.next()) {
                    for (int i = 0; i < fields.length; i++) {
                        String fieldName = fields[i].getName();
                        // 매핑이 안된 컬럼은 DTO명을 사용
                        String columnName = columnMapping.containsKey(fieldName)? columnMapping.get(fieldName) : fieldName;
                        Object value = null;
                        try {
                            value = rs.getObject(columnName);
                        } catch (SQLException e) {
                            // DB 컬럼 이름이 DTO 필드명과 다를 경우 무시 (혹은 매핑 테이블로 처리 가능)
                        }

                        bw.write(escapeCsv(value == null ? "" : value.toString()));
                        
                        // 각 열마다 ,을 넣어줘야함
                        if (i < fields.length - 1) {
                        	bw.write(",");
                        }
                    }
                    bw.newLine();
                }

                bw.flush();
                System.out.println("CSV 생성 완료: " + fileName);
            }
        }
    }

    /**
     * CSV 파일에 데이터를 안전하게 기록하기 위해 값에 따옴표를 추가하고 특수 문자를 이스케이프합니다.
     * <p>쉼표, 큰따옴표, 줄 바꿈 문자가 포함된 경우 값 전체를 큰따옴표로 묶고,
     * 값 내부의 큰따옴표는 두 개의 큰따옴표(" ")로 이스케이프합니다.</p>
     *
     * @param value CSV에 기록할 문자열 값
     * @return 이스케이프 처리된 문자열 값
     */
    private static String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}