package com.sp.dao;

import java.sql.SQLException;
import java.util.List;

import com.sp.model.DeptDTO;
import com.sp.model.DeptMemberDTO;

/**
 * <h2>DeptDAO (부서 관리 DAO 인터페이스)</h2>
 *
 * <p>부서(Department) 정보 및 부서 소속 사원 정보에 대한 데이터베이스 접근 기능을 정의합니다.</p>
 * 
 * <ul>
 *   <li>부서 등록, 수정, 삭제 기능 제공</li>
 *   <li>부서 단건 및 전체 조회</li>
 *   <li>부서별 소속 인원 조회 및 통계 조회</li>
 *   <li>부서 조직도 기반의 하위 전체 부서 조회</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> DEPT_INS_001 ~ DEPT_SEL_010</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 김세민</p>
 * <p><b>작성일:</b> 2025-11-16</p>
 * <p><b>버전:</b> 0.9</p>
 */
public interface DeptDAO {

    /** DEPT_INS_001 : 신규 부서를 등록합니다. */
    int insertDept(DeptDTO dept) throws SQLException;

    /** DEPT_UPD_002 : 부서 정보를 수정합니다. */
    int updateDept(DeptDTO dept) throws SQLException;

    /** DEPT_DEL_004 : 부서 코드를 기준으로 부서를 삭제합니다. */
    int deleteDept(String deptCd) throws SQLException;

    /** DEPT_SEL_003 : 전체 부서 목록을 조회합니다. */
    List<DeptDTO> selectAllDept();

    /** 
     * DEPT_SEL_005 : 부서 소속 사원 목록을 페이징 조회합니다.
     * @param start 조회 시작 row 번호
     * @param end   조회 종료 row 번호
     */
    List<DeptMemberDTO> selectDeptMember(int start, int end);
    
    /** 
     * DEPT_SEL_006 : 부서 코드를 기준으로 단일 부서 정보를 조회합니다.
     * @param deptCd 부서 코드
     */
    DeptDTO selectOneByDeptCd(String deptCd);
    
    /** 
     * DEPT_SEL_007 : 특정 부서 코드 기준으로 해당 부서 및 모든 하위 부서를 조회합니다.
     * @param deptCd 조회 기준 부서 코드
     */
    List<DeptDTO> selectDeptWithAllChildren(String deptCd) throws SQLException;
    
    /** DEPT_SEL_008 : 전체 사원 수를 조회합니다. */
    int selectDeptMemberCount();
    
    /** 
     * DEPT_SEL_009 : 부서 및 소속 사원 정보를 CSV 파일로 생성합니다.
     * @throws Exception 파일 생성 오류 발생 시
     */
    void makeCSVFile() throws Exception;
    
    /** 
     * DEPT_SEL_010 : 부서별 사원 수 및 비율 정보를 조회합니다.
     * <p>각 부서의 인원 비율과 시각화용 값(그래프 출력 등)을 포함합니다.</p>
     */
    List<DeptDTO> selectDeptMemberCountRatio();
        
}
