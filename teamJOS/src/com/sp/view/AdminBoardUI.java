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
 * <h2>AdminBoardUI (관리자 - 게시판 관리 UI)</h2>
 *
 * <p>관리자 모드에서 게시판 기능을 관리하는 UI 클래스입니다.
 * 일반 사원의 기능({@code BoardCommonUI})을 포함하며, 
 * 특히 관리자 권한으로 <b>모든 게시글을 강제 삭제</b>할 수 있는 기능을 제공합니다.</p>
 *
 * <ul>
 *   <li>게시글 등록 – <b>BOARD_INS_001</b></li>
 *   <li>게시글 수정 – <b>BOARD_UPD_002</b></li>
 *   <li>게시글 강제 삭제(관리자 전용) – <b>BOARD_DEL_003</b></li>
 *   <li>게시글 목록 조회 – <b>BOARD_LIS_004</b></li>
 * </ul>
 *
 * <p><b>프로젝트명:</b> teamJOS 인사관리 프로젝트</p>
 * <p><b>작성자:</b> 이석준</p> 
 * <p><b>작성일:</b> 2025-11-17</p>
 * <p><b>버전:</b> 1.0</p>
 */
public class AdminBoardUI {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private BoardDAO boardDao;
    @SuppressWarnings("unused")
    private LoginInfo loginInfo;
    private BoardCommonUI boardCommonUI;
    
    /**
     * AdminBoardUI 생성자
     *
     * <p>관리자 게시판 UI를 초기화합니다.</p>
     *
     * @param boardDao 게시판 데이터 접근 객체
     * @param loginInfo 로그인 정보를 담는 유틸리티 객체
     *
     * <p><b>관련 서비스 번호:</b> BOARD_INS_001 / BOARD_UPD_002 / BOARD_DEL_003 / BOARD_LIS_004</p>
     */
    public AdminBoardUI(BoardDAO boardDao, LoginInfo loginInfo) {
        this.boardDao = boardDao;
        this.loginInfo = loginInfo;
        this.boardCommonUI = new BoardCommonUI(loginInfo);
    }
    
    /**
     * <h3>관리자 게시판 관리 메인 메뉴</h3>
     *
     * <p>관리자용 게시판 관리 메뉴를 출력하고 사용자의 선택에 따라 기능을 실행합니다.</p>
     *
     * <ul>
     *   <li>① 게시글 등록 – <b>BOARD_INS_001</b></li>
     *   <li>② 게시글 수정 – <b>BOARD_UPD_002</b></li>
     *   <li>③ 게시글 삭제(관리자 전용) – <b>BOARD_DEL_003</b></li>
     *   <li>④ 게시글 보기 – <b>BOARD_LIS_004</b></li>
     * </ul>
     */
    public void menu() {
        int ch;
        String input;
        
        printTitle("🏢 [관리자 - 게시판관리]");
        while(true) {
            try {
                do {
                    printMenu(YELLOW, "① 게시글 등록", "② 게시글 수정", "③ 게시글 삭제", "④ 게시글 보기");

                    input = br.readLine();
                    InputValidator.isUserExit(input);
                    if(input == null || input.trim().isEmpty()) {
                        ch = 0;
                        continue;
                    }
                    ch = Integer.parseInt(input);
                    
                } while(ch < 1 || ch > 4);
                
                switch(ch) {
                case 1: insert(); break; // BOARD_INS_001 
                case 2: update(); break; // BOARD_UPD_002 
                case 3: delete(); break; // BOARD_DEL_003
                case 4: viewPostsList(); break; // BOARD_LIS_004
                }
                
            } catch (NumberFormatException e) {
                printLineln(MAGENTA, "📢 1 ~ 4 사이의 숫자만 입력 가능합니다.");
            } catch (UserQuitException e) {
                printLineln(MAGENTA, "📢 작업을 취소하였습니다.");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
    
    /**
     * <h3>게시글 등록</h3>
     *
     * <p>새로운 게시글을 등록합니다.  
     * 기능 처리 자체는 {@code BoardCommonUI.insert()}에 위임됩니다.</p>
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
     * <p>기존 게시글을 수정합니다.  
     * 기능 처리 자체는 {@code BoardCommonUI.update()}에 위임됩니다.</p>
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
     * 기능 처리 자체는 {@code BoardCommonUI.viewPostsList()}에 위임됩니다.</p>
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
     * <h3>게시글 강제 삭제 (관리자 전용)</h3>
     *
     * <p>관리자가 모든 게시글을 작성자 제한 없이 삭제할 수 있는 기능입니다.  
     * 사용자가 입력한 게시글 번호를 기반으로 {@code BoardDAO.deletePost_Admin()}을 호출합니다.</p>
     *
     * <p><b>서비스 번호:</b> BOARD_DEL_003</p>
     */
    private void delete() {
        printTitle("🗑️ [게시글 삭제]");
        BoardDTO dto= new BoardDTO();
        int boardNo;
        try {
            // 1. 사용자로부터 삭제할 글번호 입력
            printLine(GREEN, "👉 삭제할 글 번호를 입력하세요. : ");
            boardNo = Integer.parseInt(br.readLine());
             // TODO: 나중에 loginInfo.loginMember().getEmpNo()로 변경
            
            dto.setBoardNo(boardNo);
            
            printLine(MAGENTA, "❓ 정말 " + boardNo + "번 글을 삭제하시겠습니까? (Y/N) : ");
            String confirm = br.readLine();

            if (!confirm.equalsIgnoreCase("Y")) {
                printLineln(MAGENTA, "📢 삭제를 취소했습니다.");
                return;
            }

            
            int result = boardDao.deletePost_Admin(dto);

            // 5. 결과 피드백
            if (result > 0) {
                printLine(MAGENTA, "✓ " + boardNo + "번 글이 성공적으로 삭제되었습니다.");
            } else {
                printLineln(MAGENTA, "📢 글 삭제에 실패했습니다. ");
            }

        } catch (NumberFormatException e) {
            printLineln(MAGENTA, "📢 글번호는 숫자로 입력해야 합니다.");
        } catch (Exception e) {
            printLineln(MAGENTA, "📢 게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }
    
}
