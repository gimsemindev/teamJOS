package com.sp.util;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h2>DBUtil (데이터베이스 유틸리티)</h2>
 *
 * <p>JDBC 리소스(ResultSet, Statement, Connection)를 안전하고 간단하게 닫거나 롤백하기 위한
 * 정적 메서드를 제공하는 유틸리티 클래스입니다.
 * 자원 누수를 방지하고 코드의 안정성을 높이는 데 사용됩니다.</p>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 황선호</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p> 
 */
public class DBUtil {
    
    /**
     * ResultSet 객체를 닫습니다.
     * <p>ResultSet이 null이 아닌 경우에만 닫으며, SQLException은 무시합니다.</p>
     *
     * @param rs 닫을 ResultSet 객체
     */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}
	
    /**
     * Statement 객체 (Statement, PreparedStatement, CallableStatement 포함)를 닫습니다.
     * <p>Statement가 null이 아닌 경우에만 닫으며, SQLException은 무시합니다.</p>
     *
     * @param stmt 닫을 Statement 객체
     */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

    /**
     * Connection 객체의 트랜잭션을 롤백(Rollback) 합니다.
     * <p>Connection이 null이 아닌 경우에만 롤백하며, SQLException은 무시합니다.</p>
     *
     * @param conn 롤백할 Connection 객체
     */
	public static void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
			}
		}
	}

	/**
     * Connection 객체를 닫습니다.
     * <p>Connection이 null이 아닌 경우에만 닫으며, SQLException은 무시합니다.</p>
     *
     * @param conn 닫을 Connection 객체
     */
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
	
}