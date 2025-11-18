/********************************************************************
 프로젝트 : teamJOS 인사관리 프로젝트
 프로시저ID : SP_ANNUAL_LEAVE_DAYILY
 프로시저명 : 연차발생일배치프로시저
 작성자    : 김세민
 작성일    : 2025-11-18
 수행주기  : 일
 설명     : 1) 입사일을 기준으로 1년이 되면 연차 15일을 추가하고
              미사용 연차를 N 처리 한다.
           2) 신입사원의 경우 한달 만근 시 1일의 연차가 발생하며
              입사 1년이 되면 15일이 발생한다.
*********************************************************************/
CREATE OR REPLACE PROCEDURE SP_ANNUAL_LEAVE_DAYILY
AS
  v_today   DATE   := TRUNC(SYSDATE);
  v_year    NUMBER := TO_NUMBER(TO_CHAR(SYSDATE,'YYYY'));
  v_months  NUMBER;
  v_exists  NUMBER;
  v_total   NUMBER;
  v_current NUMBER;
BEGIN
  DBMS_OUTPUT.PUT_LINE('=== [연차 일배치 시작 - 안정형] ===');

  FOR emp IN (
    SELECT EMP_NO, HIRE_DT
      FROM TB_EMP
     WHERE EMP_STAT_CD='A'
  ) LOOP
    v_months := TRUNC(MONTHS_BETWEEN(v_today, emp.HIRE_DT));

    ------------------------------------------------------------------
    -- 1) 신입 월차 발생 (오늘이 정확히 발생일일 때만)
    ------------------------------------------------------------------
    IF v_months BETWEEN 1 AND 11
       AND ADD_MONTHS(emp.HIRE_DT, v_months) = v_today THEN

      BEGIN
        SELECT TOTAL_DAYS INTO v_current
          FROM TB_ANNUAL_LEAVE
         WHERE EMP_NO = emp.EMP_NO
           AND LEAVE_YEAR = v_year
           AND USE_YN = 'Y';
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_current := 0;
      END;

      IF v_current < v_months THEN
        IF v_current = 0 THEN
          INSERT INTO TB_ANNUAL_LEAVE(
            LEAVE_SEQ, EMP_NO, LEAVE_YEAR,
            TOTAL_DAYS, USED_DAYS, REMAIN_DAYS, REG_DT, USE_YN
          ) VALUES (
            SQ_TB_ANNUAL_LEAVE.NEXTVAL, emp.EMP_NO, v_year,
            1, 0, 1, v_today, 'Y'
          );
          DBMS_OUTPUT.PUT_LINE('신입 '||emp.EMP_NO||' → 첫 월차 1일 생성');
        ELSE
          UPDATE TB_ANNUAL_LEAVE
             SET TOTAL_DAYS = v_current + 1,
                 REMAIN_DAYS = (v_current + 1) - USED_DAYS
           WHERE EMP_NO = emp.EMP_NO
             AND LEAVE_YEAR = v_year
             AND USE_YN = 'Y';
          DBMS_OUTPUT.PUT_LINE('신입 '||emp.EMP_NO||' → 월차 +1일 추가');
        END IF;
      ELSE
        DBMS_OUTPUT.PUT_LINE('신입 '||emp.EMP_NO||' → 이미 최신 상태, 스킵');
      END IF;
    END IF;

    ------------------------------------------------------------------
    -- 2) 입사 1주년 → 기존 비활성화 후 새 연차 15일 부여
    ------------------------------------------------------------------
    IF v_months = 12
       AND ADD_MONTHS(emp.HIRE_DT,12)=v_today THEN

      UPDATE TB_ANNUAL_LEAVE
         SET USE_YN='N'
       WHERE EMP_NO=emp.EMP_NO
         AND USE_YN='Y';

      INSERT INTO TB_ANNUAL_LEAVE(
        LEAVE_SEQ, EMP_NO, LEAVE_YEAR,
        TOTAL_DAYS, USED_DAYS, REMAIN_DAYS, REG_DT, USE_YN
      ) VALUES (
        SQ_TB_ANNUAL_LEAVE.NEXTVAL, emp.EMP_NO, v_year,
        15, 0, 15, v_today, 'Y'
      );

      DBMS_OUTPUT.PUT_LINE('정규 '||emp.EMP_NO||' → 입사 1주년, 연차 15일 부여');
    END IF;

    ------------------------------------------------------------------
    -- 3) 2년차 이상 → 매 기념일마다 리셋
    ------------------------------------------------------------------
    IF v_months > 12
       AND MOD(v_months,12)=0
       AND ADD_MONTHS(emp.HIRE_DT,v_months)=v_today THEN

      UPDATE TB_ANNUAL_LEAVE
         SET USE_YN='N'
       WHERE EMP_NO=emp.EMP_NO
         AND USE_YN='Y';

      INSERT INTO TB_ANNUAL_LEAVE(
        LEAVE_SEQ, EMP_NO, LEAVE_YEAR,
        TOTAL_DAYS, USED_DAYS, REMAIN_DAYS, REG_DT, USE_YN
      ) VALUES (
        SQ_TB_ANNUAL_LEAVE.NEXTVAL, emp.EMP_NO, v_year,
        15, 0, 15, v_today, 'Y'
      );

      DBMS_OUTPUT.PUT_LINE('정규 '||emp.EMP_NO||' → 연차 갱신 15일 부여');
    END IF;

  END LOOP;

  COMMIT;
  DBMS_OUTPUT.PUT_LINE('=== [연차 일배치 완료 - 안정형] ===');
END;


/********************************************************************
 프로젝트 : teamJOS 인사관리 프로젝트
 프로시저ID : SP_INIT_ANNUAL_LEAVE_INIT
 프로시저명 : 연차발생초기화프로시저
 작성자    : 김세민
 작성일    : 2025-11-18
 수행주기  : 수시
 설명     : 1) 관리자가 과거데이터를 소급처리하기 위한 프로시저(이행용)
*********************************************************************/
create or replace  PROCEDURE SP_INIT_ANNUAL_LEAVE_INIT
AS
    v_today     DATE   := TRUNC(SYSDATE);
    v_year      NUMBER := TO_NUMBER(TO_CHAR(SYSDATE,'YYYY'));
    v_months    NUMBER;
    v_exists    NUMBER;
    v_total     NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== [소급 연차 생성 프로시저 - 리셋 버전 시작] ===');

    FOR emp IN (
        SELECT EMP_NO, HIRE_DT
          FROM TB_EMP
         WHERE EMP_STAT_CD = 'A'
           AND HIRE_DT <= v_today
    ) LOOP

        v_months := FLOOR(MONTHS_BETWEEN(v_today, emp.HIRE_DT));

        ----------------------------------------------------------------
        -- 1️⃣ 입사 1년 미만 → 월차 누적 (근속 개월 수)
        ----------------------------------------------------------------
        IF v_months < 12 THEN
            v_total := v_months;

            SELECT COUNT(*) INTO v_exists
              FROM TB_ANNUAL_LEAVE
             WHERE EMP_NO = emp.EMP_NO
               AND LEAVE_YEAR = v_year
               AND USE_YN = 'Y';

            IF v_exists = 0 THEN
                INSERT INTO TB_ANNUAL_LEAVE (
                    LEAVE_SEQ, EMP_NO, LEAVE_YEAR,
                    TOTAL_DAYS, USED_DAYS, REMAIN_DAYS, REG_DT, USE_YN
                ) VALUES (
                    SQ_TB_ANNUAL_LEAVE.NEXTVAL, emp.EMP_NO, v_year,
                    v_total, 0, v_total, SYSDATE, 'Y'
                );
                DBMS_OUTPUT.PUT_LINE('신입 '||emp.EMP_NO||' → 월차 '||v_total||'일 등록');
            ELSE
                UPDATE TB_ANNUAL_LEAVE
                   SET TOTAL_DAYS = v_total,
                       REMAIN_DAYS = v_total - USED_DAYS
                 WHERE EMP_NO = emp.EMP_NO
                   AND LEAVE_YEAR = v_year
                   AND USE_YN = 'Y';
                DBMS_OUTPUT.PUT_LINE('신입 '||emp.EMP_NO||' → 월차 '||v_total||'일 갱신');
            END IF;

        ----------------------------------------------------------------
        -- 2️⃣ 입사 1년 이상 → 과거 기록 비활성화 + 새 연차 15일 지급
        ----------------------------------------------------------------
        ELSE
            -- 기존 연차 이력 비활성화 (이전 데이터 보존)
            UPDATE TB_ANNUAL_LEAVE
               SET USE_YN = 'N'
             WHERE EMP_NO = emp.EMP_NO
               AND USE_YN = 'Y';

            -- 새 연차 지급
            INSERT INTO TB_ANNUAL_LEAVE (
                LEAVE_SEQ, EMP_NO, LEAVE_YEAR,
                TOTAL_DAYS, USED_DAYS, REMAIN_DAYS, REG_DT, USE_YN
            ) VALUES (
                SQ_TB_ANNUAL_LEAVE.NEXTVAL, emp.EMP_NO, v_year,
                15, 0, 15, SYSDATE, 'Y'
            );

            DBMS_OUTPUT.PUT_LINE('정규 '||emp.EMP_NO||' → 연차 15일 신규 지급 (과거연차 N처리)');
        END IF;

    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('=== [소급 연차 생성 프로시저 완료] ===');
END;


/********************************************************************
 프로젝트 : teamJOS 인사관리 프로젝트
 프로시저ID : SP_APPROVE_RETIRE_SUSI
 프로시저명 : 퇴직신청처리프로시저
 작성자    : 황선호
 작성일    : 2025-11-18
 수행주기  : 수시:승인시
 설명     : 1) 관리자가 퇴직신청을 승인처리한다.
*********************************************************************/
CREATE OR REPLACE PROCEDURE SP_APPROVE_RETIRE_SUSI(
    p_retire_seq IN NUMBER -- 퇴직신청번호
)
AS
    r_emp_no    CHAR(5);
    r_reg_dt    DATE;
BEGIN
    -------------------------------------------------------------------
    -- 1. 퇴직신청 정보 조회
    -------------------------------------------------------------------
    BEGIN 
        SELECT EMP_NO, REG_DT
        INTO r_emp_no, r_reg_dt
        FROM TB_EMP_RETIRE
        WHERE RETIRE_SEQ = p_retire_seq;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, '퇴직신청번호 '||p_retire_seq||' 에 해당 데이터가 없습니다.');    
    END;
    -------------------------------------------------------------------
    -- 2. 퇴직 승인 (TB_EMP_RETIRE)
    -------------------------------------------------------------------
    UPDATE TB_EMP_RETIRE 
       SET APPROVER_YN = 'Y'
     WHERE RETIRE_SEQ = p_retire_seq;  

    -------------------------------------------------------------------
    -- 3. 커밋 및 메시지
    -------------------------------------------------------------------
    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20099, '퇴직 승인 중 오류 발생: ' || SQLERRM);    

END;


/********************************************************************
 프로젝트 : teamJOS 인사관리 프로젝트
 프로시저ID : SP_APPROVE_VACATION_SUSI
 프로시저명 : 휴가신청처리프로시저
 작성자    : 김세민
 작성일    : 2025-11-18
 수행주기  : 수시:승인시
 설명     : 1) 관리자가 휴가신청을 승인처리한다.
           2) 휴가일수중 공휴일을 차감한다.
*********************************************************************/
create or replace  PROCEDURE SP_APPROVE_VACATION_SUSI (
    p_vacation_seq IN NUMBER   -- 휴가신청번호
)
AS
    v_emp_no    CHAR(5);
    v_start_dt  DATE;
    v_end_dt    DATE;
    v_workdays  NUMBER(3,1);
    v_year      NUMBER(4);
    v_daynum    NUMBER;
    v_remain    NUMBER(3,1);
BEGIN
    -------------------------------------------------------------------
    -- 1. 휴가 정보 조회
    -------------------------------------------------------------------
    BEGIN
        SELECT EMP_NO, START_DT, END_DT
          INTO v_emp_no, v_start_dt, v_end_dt
          FROM TB_VACATION
         WHERE VACATION_SEQ = p_vacation_seq;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, '휴가신청번호 '||p_vacation_seq||' 에 해당 데이터가 없습니다.');
    END;

    -------------------------------------------------------------------
    -- 2. 주말(토,일) 제외 휴가일 계산
    -------------------------------------------------------------------
    v_workdays := 0;
    FOR d IN 0 .. (v_end_dt - v_start_dt) LOOP
        v_daynum := TO_NUMBER(TO_CHAR(v_start_dt + d, 'D'));
        IF v_daynum NOT IN (1, 7) THEN
            v_workdays := v_workdays + 1;
        END IF;
    END LOOP;

    v_year := EXTRACT(YEAR FROM v_start_dt);

    -------------------------------------------------------------------
    -- 3. 잔여 연차 확인
    -------------------------------------------------------------------
    BEGIN
        SELECT REMAIN_DAYS
          INTO v_remain
          FROM TB_ANNUAL_LEAVE
         WHERE EMP_NO = v_emp_no
           AND LEAVE_YEAR = v_year
           AND USE_YN = 'Y'
           FOR UPDATE;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, '연차 정보가 없습니다. (사원:'||v_emp_no||', 연도:'||v_year||')');
    END;

    IF v_remain < v_workdays THEN
        RAISE_APPLICATION_ERROR(-20003,
          '잔여 연차 부족: 현재 ' || v_remain || '일, 요청 ' || v_workdays || '일');
    END IF;

    -------------------------------------------------------------------
    -- 4. 휴가 승인 (TB_VACATION)
    -------------------------------------------------------------------
    UPDATE TB_VACATION
       SET APPROVER_YN = 'Y'
     WHERE VACATION_SEQ = p_vacation_seq;

    -------------------------------------------------------------------
    -- 5. 연차 차감 (TB_ANNUAL_LEAVE)
    -------------------------------------------------------------------
    UPDATE TB_ANNUAL_LEAVE
       SET USED_DAYS   = NVL(USED_DAYS, 0) + v_workdays,
           REMAIN_DAYS = TOTAL_DAYS - (NVL(USED_DAYS, 0) + v_workdays)
     WHERE EMP_NO = v_emp_no
       AND LEAVE_YEAR = v_year
       AND USE_YN = 'Y';

    -------------------------------------------------------------------
    -- 6. 커밋 및 메시지
    -------------------------------------------------------------------
    COMMIT;

    DBMS_OUTPUT.PUT_LINE('사원번호: ' || v_emp_no ||
                         ' / 승인일수(주말제외): ' || v_workdays || '일 승인 완료');

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20099, '휴가 승인 중 오류 발생: ' || SQLERRM);
END;


/********************************************************************
 프로젝트 : teamJOS 인사관리 프로젝트
 프로시저ID : SP_INSERT_ATD
 프로시저명 : 근태정보입력프로시저
 작성자    : 오다은
 작성일    : 2025-11-18
 수행주기  : 수시
 설명     : 1) 근태정보를 입력한다.
*********************************************************************/
CREATE OR REPLACE PROCEDURE SP_INSERT_ATD
IS
    vCount number;
BEGIN
    SELECT count(*) INTO vCount 
    FROM TB_ATD 
    WHERE trunc(CHECK_IN) = trunc(sysdate);

    IF vCount = 0 THEN
    INSERT INTO TB_ATD(ATD_SEQ, EMP_NO, CHECK_IN, CHECK_OUT, WORK_HOURS, ATD_STATUS_CD)
        SELECT SQ_TB_ATD.nextval, emp.EMP_NO, null, null, null, '00'
        FROM TB_EMP emp
        WHERE emp.USE_YN = 'Y';
        COMMIT;
    ELSE NULL;
    END IF;

    EXCEPTION
        WHEN OTHERS THEN 
            DBMS_OUTPUT.PUT_LINE('AutoInsertAtd 데이터 추가 중 예외 발생.');
END;

/********************************************************************
 프로젝트 : teamJOS 인사관리 프로젝트
 프로시저ID : SP_INSERT_CHECKIN
 프로시저명 : 출근시간기록프로시저
 작성자    : 오다은
 작성일    : 2025-11-18
 수행주기  : 수시
 설명     : 1) 출근시간을 기록한다.
*********************************************************************/
CREATE OR REPLACE PROCEDURE SP_INSERT_CHECKIN (
    P_EMP_NO IN TB_ATD.EMP_NO%TYPE,
    P_MSG OUT VARCHAR2
) AS
    v_check_in TB_ATD.CHECK_IN%TYPE;
    v_now TIMESTAMP := SYSTIMESTAMP;
BEGIN
    -- 오늘 근태 기록 조회
    SELECT CHECK_IN INTO v_check_in
    FROM TB_ATD
    WHERE EMP_NO = P_EMP_NO
      AND TRUNC(REG_DT) = TRUNC(SYSDATE);

    IF v_check_in IS NOT NULL THEN
        P_MSG := '이미 출근하셨습니다.';
    ELSE
        -- 상태 결정
        IF TO_CHAR(v_now,'HH24:MI') < '09:00' THEN
            UPDATE TB_ATD
            SET CHECK_IN = v_now,
                ATD_STATUS_CD = '01'
            WHERE EMP_NO = P_EMP_NO
              AND TRUNC(REG_DT) = TRUNC(SYSDATE);
            P_MSG := '출근이 기록되었습니다. 상태: 정상출근';
        ELSE
            UPDATE TB_ATD
            SET CHECK_IN = v_now,
                ATD_STATUS_CD = '02'
            WHERE EMP_NO = P_EMP_NO
              AND TRUNC(REG_DT) = TRUNC(SYSDATE);
            P_MSG := '출근이 기록되었습니다. 상태: 지각';
        END IF;
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        P_MSG := '오늘 근태 정보가 존재하지 않습니다.';
END;

/********************************************************************
 프로젝트 : teamJOS 인사관리 프로젝트
 프로시저ID : SP_INSERT_CHECKIN
 프로시저명 : 퇴근시간기록프로시저
 작성자    : 오다은
 작성일    : 2025-11-18
 수행주기  : 수시
 설명     : 1) 퇴근시간을 기록한다.
*********************************************************************/
create or replace  PROCEDURE SP_INSERT_CHECKOUT (
    P_EMP_NO IN TB_ATD.EMP_NO%TYPE,
    P_MSG OUT VARCHAR2
) AS
    v_check_in TB_ATD.CHECK_IN%TYPE;
    v_now TIMESTAMP := SYSTIMESTAMP;
    v_work_hours NUMBER(5,2);
BEGIN
    -- 오늘 출근 시간 조회
    SELECT CHECK_IN INTO v_check_in
    FROM TB_ATD
    WHERE EMP_NO = P_EMP_NO
      AND TRUNC(REG_DT) = TRUNC(SYSDATE);

    -- 출근 기록 없으면 퇴근 입력 불가
    IF v_check_in IS NULL THEN
        P_MSG := '출근 기록이 없어 퇴근을 기록할 수 없습니다. 증빙 자료를 인사운영팀에 제출해 주세요.';
        RETURN;
    END IF;

    -- 근무시간 계산
    v_work_hours := ROUND((CAST(v_now AS DATE) - CAST(v_check_in AS DATE)) * 24, 2);

    -- 퇴근 갱신
    UPDATE TB_ATD
    SET CHECK_OUT = v_now,
        WORK_HOURS = v_work_hours
    WHERE EMP_NO = P_EMP_NO
      AND TRUNC(REG_DT) = TRUNC(SYSDATE);

    P_MSG := '퇴근이 기록되었습니다. 근무시간: ' || v_work_hours || '시간';

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        P_MSG := '오늘 근태 정보가 존재하지 않습니다. 증빙 자료를 인사운영팀에 제출해 주세요.';
END;


/********************************************************************
 프로젝트 : teamJOS 인사관리 프로젝트
 프로시저ID : SP_SELECT_ATD_BY_DATE
 프로시저명 : 근태조회프로시저
 작성자    : 오다은
 작성일    : 2025-11-18
 수행주기  : 수시
 설명     : 1) 사원의 근태정보를 조회한다.
*********************************************************************/
create or replace  PROCEDURE SP_SELECT_ATD_BY_DATE (
    p_emp_no IN TB_ATD.EMP_NO%TYPE,
    p_date IN  DATE,
    p_result OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_result FOR
        SELECT a.EMP_NO,
               e.EMP_NM,
               TO_CHAR(a.CHECK_IN,  'YYYY-MM-DD HH24:MI:SS') AS CHECK_IN,
               TO_CHAR(a.CHECK_OUT, 'YYYY-MM-DD HH24:MI:SS') AS CHECK_OUT,
               a.WORK_HOURS,
               s.STATUS_NM,
               TO_CHAR(a.REG_DT, 'YYYY-MM-DD') AS REG_DT
        FROM TB_ATD a
        JOIN TB_EMP e ON a.EMP_NO = e.EMP_NO
        JOIN TB_ATD_STATUS s ON a.ATD_STATUS_CD = s.ATD_STATUS_CD
        WHERE a.EMP_NO = p_emp_no 
          AND a.REG_DT >= TRUNC(p_date)
          AND a.REG_DT < TRUNC(p_date) + 1; -- 다음날 자동 계산
END;


/********************************************************************
 프로젝트 : teamJOS 인사관리 프로젝트
 프로시저ID : SP_SELECT_ATD_BY_DATE_ALL
 프로시저명 : 전체사원근태조회프로시저
 작성자    : 오다은
 작성일    : 2025-11-18
 수행주기  : 수시
 설명     : 1) 전체사원의 근태정보를 조회한다.
*********************************************************************/
CREATE OR REPLACE PROCEDURE SP_SELECT_ATD_BY_DATE_ALL (
    p_date IN  DATE,
    p_result OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_result FOR
        SELECT a.EMP_NO,
               e.EMP_NM,
               TO_CHAR(a.CHECK_IN,  'YYYY-MM-DD HH24:MI:SS') AS CHECK_IN,
               TO_CHAR(a.CHECK_OUT, 'YYYY-MM-DD HH24:MI:SS') AS CHECK_OUT,
               a.WORK_HOURS,
               s.STATUS_NM,
               TO_CHAR(a.REG_DT, 'YYYY-MM-DD') AS REG_DT
        FROM TB_ATD a
        JOIN TB_EMP e ON a.EMP_NO = e.EMP_NO
        JOIN TB_ATD_STATUS s ON a.ATD_STATUS_CD = s.ATD_STATUS_CD
        WHERE a.REG_DT >= TRUNC(p_date)
          AND a.REG_DT < TRUNC(p_date) + 1; -- 다음날 자동 계산
END;



/********************************************************************
 프로젝트 : teamJOS 인사관리 프로젝트
 프로시저ID : SP_SELECT_ATD_BY_DATE_ALL
 프로시저명 : 근태정보프로시저
 작성자    : 오다은
 작성일    : 2025-11-18
 수행주기  : 수시
 설명     : 1) 사원의 근태정보를 수정한다.
*********************************************************************/
CREATE OR REPLACE PROCEDURE SP_UPDATE_ATD_COLUMN (
    P_EMP_NO        IN TB_ATD.EMP_NO%TYPE,
    P_REG_DT        IN TB_ATD.REG_DT%TYPE,
    P_COLUMN_NAME   IN VARCHAR2,
    P_NEW_VALUE     IN VARCHAR2,
    P_MSG           OUT VARCHAR2
) AS
    v_count NUMBER;
    v_sql   VARCHAR2(2000);
BEGIN
    -- 해당 날짜 + 직원의 근태 기록이 존재하는지 확인
    SELECT COUNT(*) INTO v_count
    FROM TB_ATD
    WHERE EMP_NO = P_EMP_NO
      AND REG_DT = TO_DATE(P_REG_DT, 'YYYY-MM-DD');

    IF v_count = 0 THEN
        P_MSG := '해당 날짜에 근태 기록이 존재하지 않습니다.';
        RETURN;
    END IF;

    -- 변경하려는 컬럼이 NULL인지 확인
    v_sql := '
        SELECT COUNT(*)
        FROM TB_ATD
        WHERE EMP_NO = :1
          AND REG_DT = TO_DATE(:2, ''YYYY-MM-DD'')
          AND ' || P_COLUMN_NAME || ' IS NULL';

    EXECUTE IMMEDIATE v_sql INTO v_count USING P_EMP_NO, P_REG_DT;

    IF v_count = 0 THEN
        P_MSG := P_COLUMN_NAME || '은(는) 이미 입력되어 수정할 수 없습니다.';
        RETURN;
    END IF;

    -- UPDATE 실행
    v_sql := '
        UPDATE TB_ATD
           SET ' || P_COLUMN_NAME || ' = :1
         WHERE EMP_NO = :2
           AND REG_DT = TO_DATE(:3, ''YYYY-MM-DD'')
           AND ' || P_COLUMN_NAME || ' IS NULL';

    EXECUTE IMMEDIATE v_sql USING P_NEW_VALUE, P_EMP_NO, P_REG_DT;

    P_MSG := P_COLUMN_NAME || ' 수정이 완료되었습니다.';
END;
