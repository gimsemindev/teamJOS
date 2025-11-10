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

public class FileDownloadUtil {
	
    public static <T> void exportToCsv(String filePrefix, Connection conn, Class<T> dtoClass, Map<String, String> columnMapping, String sql) throws Exception {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = filePrefix + "_" + timestamp + ".csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {

                Field[] fields = dtoClass.getDeclaredFields();

                // 헤더 자동 생성
                for (int i = 0; i < fields.length; i++) {
                    bw.write(fields[i].getName());
                    if (i < fields.length - 1) bw.write(",");
                }
                bw.newLine();

                // 각 행 데이터를 필드명 기준으로 가져와 출력
                while (rs.next()) {
                    for (int i = 0; i < fields.length; i++) {
                        String fieldName = fields[i].getName();
                        String columnName = columnMapping.getOrDefault(fieldName, fieldName);
                        Object value = null;
                        try {
                            value = rs.getObject(columnName);
                        } catch (SQLException e) {
                            // DB 컬럼 이름이 DTO 필드명과 다를 경우 무시 (혹은 매핑 테이블로 처리 가능)
                        }

                        bw.write(escapeCsv(value == null ? "" : value.toString()));
                        if (i < fields.length - 1) bw.write(",");
                    }
                    bw.newLine();
                }

                bw.flush();
                System.out.println("CSV 생성 완료: " + fileName);
            }
        }
    }

    // CSV 안전 처리
    private static String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
