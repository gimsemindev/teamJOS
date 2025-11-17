package com.sp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sp.dao.DeptDAO;
import com.sp.model.DeptDTO;
import com.sp.model.DeptMemberDTO;
import com.sp.util.DBConn;
import com.sp.util.DBUtil;
import com.sp.util.FileDownloadUtil;

/**
 * <h2>DeptDAOImpl (부서 관리 DAO 구현체)</h2>
 *
 * <p>DeptDAO 인터페이스를 구현한 클래스로, 부서 정보 및 부서 소속 직원 정보에 대한
 * CRUD 및 조회 기능을 실제 데이터베이스와 연동하여 처리합니다.</p>
 *
 * <ul>
 *   <li>부서 등록, 수정, 삭제</li>
 *   <li>부서 단건 및 전체 조회</li>
 *   <li>부서 소속 직원을 페이징 조회</li>
 *   <li>계층형 조직도 기반의 하위 부서 조회</li>
 *   <li>부서별 인원수 및 비율 통계 조회</li>
 *   <li>CSV 파일 생성</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> DEPT_INS_001 ~ DEPT_SEL_010</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 김세민</p>
 * <p><b>작성일:</b> 2025-11-16</p>
 * <p><b>버전:</b> 0.9</p>
 */
public class DeptDAOImpl implements DeptDAO{

	private Connection conn = DBConn.getConnection();
	
	/**
	 * DEPT_INS_001 : 신규 부서를 등록합니다.
	 *
	 * @param dept 등록할 부서 정보 DTO
	 * @return insert 결과 건수
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override	
    public int insertDept(DeptDTO dept) throws SQLException {
        int result = 0;
		PreparedStatement pstmt = null;
		String sql;
				
        try {
            sql = """
            	  INSERT /* DEPT_INS_001 */ 
            	    INTO TB_DEPT
                       ( DEPT_CD
                       , DEPT_NM
                       , EXT_NO
                       , SUPER_DEPT_CD
                       , USE_YN
                       , REG_DT
                       )
                  VALUES (?, ?, ?, ?, ?, SYSDATE) """;

     		pstmt = conn.prepareStatement(sql);
        	
            pstmt.setString(1, dept.getDeptCd());
            pstmt.setString(2, dept.getDeptNm());
            pstmt.setString(3, dept.getExtNo());
            pstmt.setString(4, dept.getSuperDeptCd());
            pstmt.setString(5, dept.getUseYn());

            result = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			DBUtil.close(pstmt);			
		}
        return result;
    }	
	
	
	/**
	 * DEPT_UPD_002 : 부서 정보를 수정합니다.
	 *
	 * @param dept 수정할 부서 정보 DTO
	 * @return update 결과 건수
	 * @throws SQLException SQL 실행 실패 시
	 */
	@Override
	public int updateDept(DeptDTO dept) throws SQLException{
        int result = 0;
        PreparedStatement pstmt = null;
        String sql;

        try {                        
            sql = """   
            	  UPDATE /** DEPT_UPD_002 */ TB_DEPT 
            	     SET DEPT_NM       = ? 
            	       , EXT_NO        = ?
            	       , SUPER_DEPT_CD = ? 	
            	       , USE_YN        = ? 
            	   WHERE DEPT_CD       = ? """;
            pstmt= conn.prepareStatement(sql);
            
            pstmt.setString(1, dept.getDeptNm());
            pstmt.setString(2, dept.getExtNo());
            pstmt.setString(3, dept.getSuperDeptCd());
            pstmt.setString(4, dept.getUseYn());
            pstmt.setString(5, dept.getDeptCd());
            
			result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            DBUtil.close(pstmt);
        }
        
        return result;
	}

	/**
	 * DEPT_SEL_003 : 전체 부서를 트리 구조(조직도) 형태로 조회합니다.
	 *
	 * @return 전체 부서 목록
	 */
	@Override
	public List<DeptDTO> selectAllDept() {
		List<DeptDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = """
				  SELECT /* DEPT_SEL_003 */ 
					     DEPT_CD 
					   , CASE WHEN CONNECT_BY_ISLEAF = 1 THEN 
					            LPAD(' ', (LEVEL-1)*4, ' ') || '└─ ' || DEPT_NM 
					            ELSE LPAD(' ', (LEVEL-1)*4, ' ') || '├─ ' || DEPT_NM 
					      END AS DEPT_NM 
				       , EXT_NO 
					FROM TB_DEPT 
				   WHERE USE_YN = 'Y' 
				   START WITH SUPER_DEPT_CD IS NULL 
				  CONNECT BY PRIOR DEPT_CD = SUPER_DEPT_CD 
				   ORDER SIBLINGS BY DEPT_CD """;

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				DeptDTO dto = new DeptDTO();

				dto.setDeptCd(rs.getString("DEPT_CD"));
				dto.setDeptNm(rs.getString("DEPT_NM"));
				dto.setExtNo(rs.getString("EXT_NO"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	/**
	 * DEPT_SEL_006 : 부서 코드를 기준으로 단일 부서 상세 정보를 조회합니다.
	 *
	 * @param deptCd 조회할 부서 코드
	 * @return 부서 정보 DTO (없으면 null)
	 */
	@Override
	public DeptDTO selectOneByDeptCd(String deptCd) {
		DeptDTO dto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
       
        try {
        	sql = """
        		  SELECT /* DEPT_SEL_006 */ 
        		         DEPT_CD
        		       , DEPT_NM
        		       , EXT_NO
        		       , SUPER_DEPT_CD
        		       , NVL(USE_YN,'') AS USE_YN
        		       , TO_CHAR(REG_DT, 'YYYY/MM/DD HH24:MI:SS') AS REG_DT
        			FROM TB_DEPT 
        		   WHERE DEPT_CD = ? """;

            pstmt = conn.prepareStatement(sql);           
            pstmt.setString(1, deptCd);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                dto = new DeptDTO();
                                
                dto.setDeptCd(rs.getString("DEPT_CD"));
                dto.setDeptNm(rs.getString("DEPT_NM"));
                dto.setExtNo(rs.getString("EXT_NO"));
                dto.setSuperDeptCd(rs.getString("SUPER_DEPT_CD"));
                dto.setUseYn(rs.getString("USE_YN"));
                dto.setRegDt(rs.getString("REG_DT"));
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
             
        return dto;
	}
	
	/**
	 * DEPT_SEL_008 : 전체 사원 수를 조회합니다.
	 *
	 * @return 전체 직원 수
	 */
	@Override
	public int selectDeptMemberCount() {
        int result = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        
        try {
        	sql = "SELECT /* DEPT_SEL_008 */ COUNT(*) AS CNT FROM TB_EMP WHERE USE_YN='Y'";
        	        	
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt("CNT");
            }        	
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
		
        return result;
	}
	
	
	/**
	 * DEPT_SEL_005 : 부서 소속 사원을 페이징하여 조회합니다.
	 *
	 * @param start 조회 시작 번호
	 * @param end   조회 종료 번호
	 * @return 조회된 사원 목록
	 */
	@Override
	public List<DeptMemberDTO> selectDeptMember(int start, int end) {
	    
		List<DeptMemberDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
	    
		try {
	    	sql = """
	              SELECT /** DEPT_SEL_005 */ *
	                FROM (
	                      SELECT ROWNUM rn, A.*
	                        FROM (
	                              SELECT D.DEPT_CD
	                                   , D.DEPT_NM
	                                   , G.GRADE_NM
	                                   , C.CONTRACT_TP_NM
	                                   , S.EMP_STAT_NM
	                                   , E.EMP_NO
	                                   , E.EMP_NM
	                                   , TO_CHAR(E.HIRE_DT, 'YYYY/MM/DD') AS HIRE_DT
	                                   , NVL(P.CONTACT_NO,' ') AS CONTACT_NO, E.EMAIL
	                                FROM TB_EMP E
	                                LEFT JOIN TB_DEPT D 
	                                  ON E.DEPT_CD  = D.DEPT_CD 
	                                 AND D.USE_YN   = 'Y'
	                                LEFT JOIN TB_GRADE G 
	                                  ON E.GRADE_CD = G.GRADE_CD 
	                                 AND G.USE_YN= 'Y'
	                                LEFT JOIN TB_EMP_CNTRT_TYPE C 
	                                  ON E.CONTRACT_TP_CD = C.CONTRACT_TP_CD 
	                                 AND C.USE_YN = 'Y'
	                                LEFT JOIN TB_EMP_STATUS S 
	                                  ON E.EMP_STAT_CD = S.EMP_STAT_CD 
	                                 AND S.USE_YN = 'Y'
	                                LEFT JOIN TB_EMP_CNTCT P 
	                                  ON E.EMP_NO=P.EMP_NO 
	                                 AND P.CONTACT_TP_CD = '1' 
	                                 AND P.USE_YN = 'Y'
	                               WHERE E.USE_YN = 'Y'
	                               ORDER BY D.DEPT_CD, E.GRADE_CD DESC
	                             ) A
	                     )
	               WHERE rn BETWEEN ? AND ? """;
	    	
	    	pstmt = conn.prepareStatement(sql);
            
	    	pstmt.setInt(1, start);
	    	pstmt.setInt(2, end);
	    	
	        rs = pstmt.executeQuery();

	    	while (rs.next()) {
	    		DeptMemberDTO dto = new DeptMemberDTO();
	            dto.setDeptCd(rs.getString("DEPT_CD"));
	            dto.setDeptNm(rs.getString("DEPT_NM"));
	            dto.setGradeNM(rs.getString("GRADE_NM"));
	            dto.setCotractTpNM(rs.getString("CONTRACT_TP_NM"));
	            dto.setEmpStatNM(rs.getString("EMP_STAT_NM"));
	            dto.setEmpNo(rs.getString("EMP_NO"));
	            dto.setEmpNm(rs.getString("EMP_NM"));
	            dto.setHireDt(rs.getString("HIRE_DT"));
	            dto.setContactNo(rs.getString("CONTACT_NO"));
	            dto.setEmail(rs.getString("EMAIL"));
	            list.add(dto);
	        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
	    
	    return list;		
	}

	/**
	 * DEPT_SEL_007 : 특정 부서 기준으로 해당 부서와 모든 하위 부서를 조회합니다.
	 *
	 * @param deptCd 조회 기준 부서 코드
	 * @return 하위 부서 포함 전체 리스트
	 * @throws SQLException SQL 오류 발생 시
	 */
	@Override
	public List<DeptDTO> selectDeptWithAllChildren(String deptCd) throws SQLException {
		List<DeptDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
	    try {
	    	sql = """
	    		  SELECT /* DEPT_SEL_007 */ 
	    		         DEPT_CD
				       , CASE WHEN CONNECT_BY_ISLEAF = 1 THEN 
				                   LPAD(' ', (LEVEL-1)*4, ' ') || '└─ ' || DEPT_NM 
				              ELSE LPAD(' ', (LEVEL-1)*4, ' ') || '├─ ' || DEPT_NM 
				          END AS DEPT_NM 
	    		       , SUPER_DEPT_CD, USE_YN  
	    	        FROM TB_DEPT  
	               START WITH DEPT_CD = ? 
	              CONNECT BY PRIOR DEPT_CD = SUPER_DEPT_CD """;	
	    	
	    	pstmt = conn.prepareStatement(sql);
		 
	        pstmt.setString(1, deptCd);
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            DeptDTO dto = new DeptDTO();
	            dto.setDeptCd(rs.getString("DEPT_CD"));
	            dto.setDeptNm(rs.getString("DEPT_NM"));
	            dto.setSuperDeptCd(rs.getString("SUPER_DEPT_CD"));
	            dto.setUseYn(rs.getString("USE_YN"));
	            list.add(dto);
	        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
	    
	    return list;
	}
	
	/**
	 * DEPT_DEL_004 : 특정 부서 및 모든 하위 부서를 비활성화(USE_YN='N') 처리합니다.
	 *
	 * @param deptCd 비활성화할 루트 부서 코드
	 * @return update 결과 건수
	 * @throws SQLException SQL 오류 발생 시
	 */
	@Override
	public int deleteDept(String deptCd) throws SQLException{
        int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = """
				  UPDATE /* DEPT_DEL_004 */ TB_DEPT 
                     SET USE_YN = 'N' 
                   WHERE DEPT_CD IN ( 
                                     SELECT DEPT_CD 
                                       FROM TB_DEPT 
                                      START WITH DEPT_CD = ? 
                                    CONNECT BY PRIOR DEPT_CD = SUPER_DEPT_CD) """;
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, deptCd);
			result = pstmt.executeUpdate();
			} catch (Exception e) {
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		return result;
		}

	/**
	 * DEPT_DEL_009 : 부서 및 사원 정보를 CSV 파일로 생성합니다.
	 *
	 * @throws Exception CSV 파일 생성 실패 시
	 */
	@Override	
	public void makeCSVFile() throws Exception {
		String sql;
		sql = """
			  SELECT /* DEPT_DEL_009 */
			         D.DEPT_CD
	               , D.DEPT_NM
	               , G.GRADE_NM
	               , C.CONTRACT_TP_NM
	               , S.EMP_STAT_NM
	               , E.EMP_NO
	               , E.EMP_NM
	               , TO_CHAR(E.HIRE_DT, 'YYYY/MM/DD') AS HIRE_DT
	               , NVL(P.CONTACT_NO,' ') AS CONTACT_NO, E.EMAIL
	            FROM TB_EMP E
	            LEFT JOIN TB_DEPT D 
	              ON E.DEPT_CD  = D.DEPT_CD 
	             AND D.USE_YN   = 'Y'
	            LEFT JOIN TB_GRADE G 
	              ON E.GRADE_CD = G.GRADE_CD 
	             AND G.USE_YN= 'Y'
	            LEFT JOIN TB_EMP_CNTRT_TYPE C 
	              ON E.CONTRACT_TP_CD = C.CONTRACT_TP_CD 
	             AND C.USE_YN = 'Y'
	            LEFT JOIN TB_EMP_STATUS S 
	              ON E.EMP_STAT_CD = S.EMP_STAT_CD 
	             AND S.USE_YN = 'Y'
	            LEFT JOIN TB_EMP_CNTCT P 
	              ON E.EMP_NO=P.EMP_NO 
	             AND P.CONTACT_TP_CD = '1' 
	             AND P.USE_YN = 'Y'
	           WHERE E.USE_YN = 'Y'
	           ORDER BY D.DEPT_CD, E.GRADE_CD DESC """;
		
        Map<String, String> columnMapping = Map.of(
                "deptCd"      , "DEPT_CD",
                "deptNm"      , "DEPT_NM",
                "gradeNM"     , "GRADE_NM",
                "contractTpNM", "CONTRACT_TP_NM",
                "empStatNM"   , "EMP_STAT_NM",
                "empNo"       , "EMP_NO",
                "empNm"       , "EMP_NM",
                "hireDt"      , "HIRE_DT",
                "contactNo"   , "CONTACT_NO",
                "email"       , "EMAIL"
        );		
		try {
			FileDownloadUtil.exportToCsv("부서", conn, DeptMemberDTO.class, columnMapping, sql);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/**
	 * DEPT_SEL_010 : 부서별 인원 수 및 전체 대비 비율(%)을 조회합니다.
	 *
	 * @return 부서별 인원수 및 비율이 포함된 리스트
	 */
	public List<DeptDTO> selectDeptMemberCountRatio() {
		List<DeptDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
	    try {
	    	sql = """
                  SELECT /* DEPT_SEL_010 */
                         ROOT_DEPT_CD      AS DEPT_CD
                       , MAX(ROOT_DEPT_NM) AS DEPT_NM
                       , COUNT(DISTINCT EMP_NO) AS TOTAL_EMP_COUNT
                       , ROUND(RATIO_TO_REPORT(COUNT(DISTINCT EMP_NO)) OVER(), 2) * 100 AS RATIO_PERCENT
                    FROM ( 
                          SELECT CONNECT_BY_ROOT D.DEPT_CD AS ROOT_DEPT_CD
                               , CONNECT_BY_ROOT D.DEPT_NM AS ROOT_DEPT_NM
                               , E.EMP_NO
                            FROM TB_DEPT D
                            LEFT JOIN TB_EMP E
                              ON E.DEPT_CD = D.DEPT_CD
                             AND E.USE_YN = 'Y'
                           WHERE D.USE_YN = 'Y'
                           START WITH D.DEPT_CD IN (
                                                    SELECT DEPT_CD
                                                      FROM TB_DEPT
                                                     WHERE USE_YN = 'Y'
                                                       AND SUPER_DEPT_CD IN (
                                                                             SELECT DEPT_CD
                                                                               FROM TB_DEPT
                                                                              WHERE SUPER_DEPT_CD IS NULL
                                                                            )
                                                   )
                        CONNECT BY PRIOR D.DEPT_CD = D.SUPER_DEPT_CD
                        )
                  GROUP BY ROOT_DEPT_CD
                  ORDER BY ROOT_DEPT_CD""";	
	    	
	    	pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            DeptDTO dto = new DeptDTO();
	            dto.setDeptCd(rs.getString("DEPT_CD"));
	            dto.setDeptNm(rs.getString("DEPT_NM"));
	            dto.setDeptCount(rs.getInt("TOTAL_EMP_COUNT"));
	            dto.setDeptCountRatio(rs.getInt("RATIO_PERCENT"));
	            list.add(dto);
	        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
	    
		return list;
	}
}
