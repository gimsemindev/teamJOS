package com.sp.dao;

import java.sql.SQLException;
import java.util.List;

import com.sp.model.AnnualLeaveDTO;
import com.sp.model.AttendanceDTO;
import com.sp.model.HistoryDTO;
import com.sp.model.VacationDTO;

/**
 * <h2>AttDAO (근태 관리 데이터 접근 인터페이스)</h2>
 *
 * <p>근태(Attendance) 및 휴가(Vacation) 관련 데이터베이스 접근 기능을 정의하는 인터페이스입니다.</p>
 * 
 * <ul>
 *   <li>출퇴근 등록</li>
 *   <li>휴가 신청, 승인, 수정</li>
 *   <li>근태정보 수정 및 조회</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> ATT_INS_001 ~ ATT_UPD_010</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 홍길동</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public interface AttDAO {

    /** ATT_INS_001 */
    String insertAttendanceIn(AttendanceDTO att) throws SQLException;

    /** ATT_INS_002 */
    String insertAttendanceOut(AttendanceDTO att) throws SQLException;

    /** ATT_INS_008 */
    int insertVacation(VacationDTO vacation) throws SQLException;

    /** ATT_UPD_003 */
    int updateVacationApprove(int vacationSeq) throws SQLException;

    /** ATT_UPD_009 */
    int updateVacation(VacationDTO vacation) throws SQLException;

    /** ATT_UPD_010 */
    String updateAttendance(AttendanceDTO att) throws SQLException;
    
    /** ATT_SLT_001 */ // 전체 근태정보조회 테이블(관리자)
    List<AttendanceDTO> selectAttendanceAll(String date) throws SQLException;
    
    /** ATT_SLT_002 */ // 근태정보조회 테이블(일반 사용자)
    AttendanceDTO selectAttendance(AttendanceDTO att) throws SQLException;
    
    List<VacationDTO> listVaction();

    /** ATT_SEL_004 */ //
    List<AttendanceDTO> selectAllWorkTime();

    /** ATT_SEL_005 */
    List<AttendanceDTO> selectWorkTimeByEmp(int empNo);

    /** ATT_SEL_006 */
    List<AnnualLeaveDTO> selectAllAnnualLeave(int start, int end);

    /** ATT_SEL_007 */
    List<VacationDTO> selectAnnualLeaveByEmp(int empNo);
    
    /** ATT_SEL_008 */
    int selectAllAnnualLeaveCount();
}
