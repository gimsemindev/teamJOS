package com.sp.dao;

import java.sql.SQLException;
import java.util.List;

import com.sp.model.DeptDTO;
import com.sp.model.DeptMemberDTO;

/**
 * <h2>DeptDAO (부서 관리 데이터 접근 인터페이스)</h2>
 *
 * <p>부서(Department) 관련 데이터베이스 접근 기능을 정의하는 인터페이스입니다.</p>
 * 
 * <ul>
 *   <li>부서 등록, 수정, 삭제</li>
 *   <li>부서 단건 조회 및 전체 조회</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> DEPT_INS_001 ~ DEPT_SEL_005</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 홍길동</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public interface DeptDAO {

    /** DEPT_INS_001 */
    int insertDept(DeptDTO dept) throws SQLException;

    /** DEPT_UPD_002 */
    int updateDept(DeptDTO dept) throws SQLException;

    /** DEPT_DEL_004 */
    int deleteDept(String deptCd) throws SQLException;

    /** DEPT_SEL_003 */
    List<DeptDTO> selectAllDept();

    /** DEPT_SEL_005 */
    List<DeptMemberDTO> selectDeptMember(int start, int end);
    
    /** DEPT_SEL_006 */
    DeptDTO selectOneByDeptCd(String deptCd);
    
    /** DEPT_SEL_007 */
    List<DeptDTO> selectDeptWithAllChildren(String deptCd) throws SQLException;
    
    /** DEPT_SEL_008 */
    int selectDeptMemberCount();
    
    /** DEPT_SEL_009 */
    void makeCSVFile() throws Exception;
    
    /** DEPT_SEL_010 */
    List<DeptDTO> selectDeptMemberCountRatio();
        
}
