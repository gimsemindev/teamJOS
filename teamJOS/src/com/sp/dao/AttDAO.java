package com.sp.dao;

import java.sql.SQLException;
import java.util.List;

import com.sp.model.AnnualLeaveDTO;
import com.sp.model.AttendanceDTO;
import com.sp.model.VacationDTO;

/**
 * <h2>AttDAO (근태 관리 데이터 접근 인터페이스)</h2>
 *
 * <p>근태(Attendance) 및 휴가(Vacation) 관련 데이터베이스 접근 기능을 정의하는 인터페이스입니다.</p>
 *
 * <ul>
 * <li>출퇴근 기록 등록 및 수정</li>
 * <li>휴가 신청, 승인, 수정 및 목록 조회</li>
 * <li>근태 정보 및 연차 정보 조회 (관리자/일반 사용자)</li>
 * </ul>
 *
 * <p><b>Service ID 범위:</b> ATT_INS_001 ~ ATT_SEL_014</p>
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 오다은</p>
 * <p><b>작성일:</b> 2025-11-06</p>
 * <p><b>버전:</b> 1.0</p>
 */
public interface AttDAO {

    /** * ATT_INS_001 : 사원의 출근 기록을 등록합니다.
     * @param att 출근 정보 DTO (사번, 시간 정보 포함)
     * @return 성공 여부를 나타내는 문자열
     * @throws SQLException DB 접근 오류 발생 시
     */
    String insertAttendanceIn(AttendanceDTO att) throws SQLException;

    /** * ATT_INS_002 : 사원의 퇴근 기록을 등록합니다. (기존 출근 기록에 업데이트)
     * @param att 퇴근 정보 DTO (사번, 시간 정보 포함)
     * @return 성공 여부를 나타내는 문자열
     * @throws SQLException DB 접근 오류 발생 시
     */
    String insertAttendanceOut(AttendanceDTO att) throws SQLException;

    /** * ATT_INS_008 : 신규 휴가 신청 정보를 등록합니다.
     * @param vacation 등록할 휴가 신청 정보 DTO
     * @return 등록된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int insertVacation(VacationDTO vacation) throws SQLException;

    /** * ATT_UPD_003 : 휴가 신청 건을 승인 처리(결재)합니다.
     * @param vacationSeq 승인할 휴가 신청 일련번호
     * @return 수정된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int updateVacationApprove(int vacationSeq) throws SQLException;

    /** * ATT_UPD_009 : 휴가 신청 정보를 수정합니다.
     * @param vacation 수정할 휴가 신청 정보 DTO
     * @return 수정된 레코드 수 (1)
     * @throws SQLException DB 접근 오류 발생 시
     */
    int updateVacation(VacationDTO vacation) throws SQLException;

    /** * ATT_UPD_010 : 특정 근태 기록 정보를 수정합니다.
     * @param att 수정할 근태 정보 DTO
     * @return 성공 여부를 나타내는 문자열
     * @throws SQLException DB 접근 오류 발생 시
     */
    String updateAttendance(AttendanceDTO att) throws SQLException;
    
    /** * ATT_SEL_012 : 특정 근태 기록에서 출근(CHECK_IN) 또는 퇴근(CHECK_OUT) 컬럼이 NULL인지 확인합니다.
     * @param att 확인할 근태 정보 DTO (조회 조건 포함)
     * @return 해당 컬럼이 NULL이면 true, 아니면 false
     * @throws SQLException DB 접근 오류 발생 시
     */
    boolean checkAtdColumnIsNull(AttendanceDTO att) throws SQLException;
    
    /** * ATT_SEL_013 : 특정 날짜 기준 전체 사원의 근태 정보 목록을 조회합니다. (관리자용)
     * @param date 조회 기준 날짜
     * @return 근태 정보 DTO 리스트
     * @throws SQLException DB 접근 오류 발생 시
     */
    List<AttendanceDTO> selectAttendanceAll(String date) throws SQLException;
    
    /** * ATT_SEL_014 : 특정 사원의 근태 정보를 조회합니다. (일반 사용자용)
     * @param att 조회할 사번 및 날짜 정보가 포함된 DTO
     * @return 조회된 단일 근태 정보 DTO
     * @throws SQLException DB 접근 오류 발생 시
     */
    AttendanceDTO selectAttendance(AttendanceDTO att) throws SQLException;
    
    /** * ATT_SEL_011 : 전체 휴가 신청 리스트를 조회합니다.
     * @return 전체 휴가 신청 정보 DTO 리스트
     */
    List<VacationDTO> listVaction();

    List<VacationDTO> listVaction(String empNo);
    
    /** * ATT_SEL_006 : 전체 사원의 연차 정보를 페이징하여 조회합니다.
     * @param start 조회 시작 row 번호
     * @param end   조회 종료 row 번호
     * @return 연차 정보 DTO 리스트
     */
    List<AnnualLeaveDTO> selectAllAnnualLeave(int start, int end);

    /** * ATT_SEL_007 : 특정 사원의 연차 정보(사용 및 잔여 연차)를 조회합니다.
     * @param empNo 조회할 사원 번호
     * @return 연차 정보 DTO 리스트
     */
    List<VacationDTO> selectAnnualLeaveByEmp(int empNo);
    
    /** * ATT_SEL_008 : 전체 사원의 연차 정보 건수(총 개수)를 조회합니다.
     * @return 전체 연차 정보 레코드의 총 개수
     */
    int selectAllAnnualLeaveCount();
}