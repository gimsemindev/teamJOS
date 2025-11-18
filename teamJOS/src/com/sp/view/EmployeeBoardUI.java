package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.BoardDAO;
import com.sp.exception.UserQuitException;
import com.sp.model.BoardDTO;
import com.sp.util.InputValidator;
import com.sp.util.LoginInfo;
import com.sp.view.common.BoardCommonUI;

import static com.sp.util.PrintUtil.*;

/**
 * <h2>EmployeeBoardUI (사원 - 게시판 UI)</h2>
 *
 * <p>일반 사원이 사용할 수 있는 게시판 UI 클래스입니다.  
 * 게시글 등록, 수정, 삭제, 조회 기능을 제공하며 세부 작업은
 * {@link BoardCommonUI}를 통해 처리됩니다.</p>
 *
 * <h3>📌 제공 기능 & 서비스 번호(Service ID)</h3>
 * <ul>
 *   <li>게시글 등록 — <b>BOARD_INS_001</b></li>
 *   <li>게시글 수정 — <b>BOARD_UPD_002</b></li>
 *   <li>게시글 삭제(본인 작성 글만) — <b>BOARD_DEL_004</b></li>
 *   <li>게시글 조회(전체 목록) — <b>BOARD_LIS_004</b></li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 이석준</p>
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class EmployeeBoardUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private BoardDAO boardDao;
    private LoginInfo loginInfo;
    private BoardCommonUI boardCommonUI;
    
    /**
     * <h3>EmployeeBoardUI 생성자</h3>
     *
     * <p>게시판 DAO와 로그인 정보를 받아 UI 객체를 초기화합니다.</p>
     *
     * @param boardDao 게시판 데이터 접근 객체
     * @param loginInfo 로그인 정보 객체
     */
    public EmployeeBoardUI(BoardDAO boardDao, LoginInfo loginInfo) {
        this.boardDao = boardDao;
        this.loginInfo = loginInfo;
        this.boardCommonUI = new BoardCommonUI(loginInfo);
    }
    
    /**
     * <h3>📌 게시판 메인 메뉴</h3>
     *
     * <p>사원이 사용할 수 있는 게시판 메뉴를 출력하고,  
     * 메뉴 선택에 따라 게시판 기능을 실행합니다.</p>
     *
     * <ul>
     *   <li>① 게시글 등록 — <b>BOARD_INS_001</b></li>
     *   <li>② 게시글 수정 — <b>BOARD_UPD_002</b></li>
     *   <li>③ 게시글 삭제 — <b>BOARD_DEL_004</b></li>
     *   <li>④ 게시글 보기 — <b>BOARD_LIS_004</b></li>
     * </ul>
     *
     * <p>"q" 입력 시 상위 메뉴로 이동합니다.</p>
     */
    public void menu() {
        int ch;
        String input;
        
        while(true) {
            try {       
                do {
                    printTitle("📌 [게시판] 📌");
                    printMenu(YELLOW, "① 게시글 등록", "② 게시글 수정", "③ 게시글 삭제", "④ 게시글 보기");

                    input = br.readLine();
                    InputValidator.isUserExit(input);
                    
                    if(input == null || input.trim().isEmpty()) {
                        ch = 0;
                        continue;
                    }
                    ch = Integer.parseInt(input);
                    
                } while(ch < 1 || ch > 4);
                
                System.out.println();

                switch(ch) {
                case 1: insert(); break; // BOARD_INS_001
                case 2: update(); break; // BOARD_UPD_002
                case 3: delete(); break; // BOARD_DEL_004
                case 4: viewPostsList(); break; // BOARD_LIS_004
                }
                
                System.out.println();

            } catch (NumberFormatException e) {
                printLineln(MAGENTA, "📢 1 ~ 4 사이의 숫자만 입력 가능합니다.");
            } catch (UserQuitException e) {
                printLineln(MAGENTA, "📢 작업을 취소하였습니다.");
                return;
            } catch (Exception e) {
                printLineln(MAGENTA, "📢 [오류] 알 수 없는 예외가 발생했습니다.");
            }
        }
    }

    /**
     * <h3>게시글 등록</h3>
     *
     * <p>게시글 등록 기능을 수행합니다.  
     * 실제 등록 처리는 {@code BoardCommonUI.insert()}에서 이루어집니다.</p>
     *
     * <p><b>서비스 번호:</b> BOARD_INS_001</p>
     */
    private void insert() {
        printTitle("📝 [게시글 등록]");
        
        try {
            boardCommonUI.insert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * <h3>게시글 수정</h3>
     *
     * <p>게시글 수정 기능을 수행합니다.  
     * 실제 수정 처리는 {@code BoardCommonUI.update()}에서 이루어집니다.</p>
     *
     * <p><b>서비스 번호:</b> BOARD_UPD_002</p>
     */
    private void update() {
        printTitle("✏️ [게시글 수정]");
        try {
            boardCommonUI.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    /**
     * <h3>게시글 전체 목록 조회</h3>
     *
     * <p>전체 게시글 목록을 출력합니다.  
     * 실제 목록 조회 처리는 {@code BoardCommonUI.viewPostsList()}에서 수행됩니다.</p>
     *
     * <p><b>서비스 번호:</b> BOARD_LIS_004</p>
     */
    private void viewPostsList() {
        printTitle("🗂️ [게시글 전체 보기]");
        try {
             boardCommonUI.viewPostsList();
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
    
    /**
     * <h3>게시글 삭제 (본인 작성 글만 삭제 가능)</h3>
     *
     * <p>사원이 본인이 작성한 게시글만 삭제할 수 있는 기능입니다.  
     * 입력받은 게시글 번호와 로그인 사원의 ID를 DTO에 담아  
     * {@code BoardDAO.deletePost()}를 호출합니다.</p>
     *
     * <p><b>서비스 번호:</b> BOARD_DEL_004</p>
     */
    private void delete() {
        printTitle("🗑️ [게시글 삭제]");
        BoardDTO dto= new BoardDTO();
        int boardNo;
        try {
            printLine(GREEN, "👉 삭제할 글 번호 : ");
            boardNo = Integer.parseInt(br.readLine());
             
            dto.setBoardNo(boardNo);
            dto.setEmpNo(loginInfo.loginMember().getMemberId());
            
            printLineln(MAGENTA, "📢 [경고] 정말 " + boardNo + "번 글을 삭제하시겠습니까? (Y/N) ");
            String confirm = br.readLine();

            if (!confirm.equalsIgnoreCase("Y")) {
                printLineln(MAGENTA, "📢 [정보] 삭제를 취소했습니다.");
                return;
            }

            int result = boardDao.deletePost(dto);

            if (result > 0) {
                printLineln(MAGENTA, "📢 [정보] " + boardNo + "번 글이 성공적으로 삭제되었습니다.");
            } else {
                printLineln(MAGENTA, "📢 [오류] 글 삭제에 실패했습니다. (글번호가 없거나 삭제 권한이 없습니다)");
            }

        } catch (NumberFormatException e) {
            printLineln(MAGENTA, "📢 [오류] 글번호는 숫자로 입력해야 합니다.");
        } catch (Exception e) {
            printLineln(MAGENTA, "📢 [오류] 게시글 삭제 중 예외 발생: " + e.getMessage());
        }
    }
    
}
