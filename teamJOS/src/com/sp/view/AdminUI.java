package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.AttDAO;
import com.sp.dao.AuthDAO;
import com.sp.dao.BoardDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.EmpDAO;
import com.sp.dao.impl.AttDAOImpl;
import com.sp.dao.impl.AuthDAOImpl;
import com.sp.dao.impl.BoardDAOImpl;
import com.sp.dao.impl.DeptDAOImpl;
import com.sp.dao.impl.EmpDAOImpl;


public class AdminUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private EmpDAO empDao = new EmpDAOImpl();
	private DeptDAO deptDao = new DeptDAOImpl();
	private AttDAO attDao = new AttDAOImpl();
	private AuthDAO authDao = new AuthDAOImpl();
	private BoardDAO boardDao = new BoardDAOImpl();
	
	public  AdminUI(EmpDAO empDao,DeptDAO deptDao,AttDAO attDao,AuthDAO authDao,BoardDAO boardDao) {
		this.empDao = empDao;
		this.deptDao = deptDao;
		this.attDao = attDao;
		this.authDao = authDao;
		this.boardDao = boardDao;
	}
	
    public void manageEmployee() {
    	int ch;
		System.out.println("\n[사원관리]");
		
		try {
			
			do {
				System.out.print("1.정보등록 2.정보수정 3.부서이동 4.진급관리 5.정보조회 6.퇴직신청결재 7.경력등록 8.자격증및포상등록 9.이력조회 10.메뉴로돌아가기 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch < 1 || ch > 10);

			switch (ch) {
			case 1: empDao.insertEmployee(null); break;
			case 2: empDao.updateEmployee(null); break;
			case 3: empDao.updateDeptMove(null); break;
			case 4: empDao.selectDeptMove(0); break;
			case 5: selectByEmp(0); break;
			case 6: break;
			case 7: break;
			case 8: break;
			case 9: break;
			case 10: return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
    private void selectByEmp(int chNo) {
    	int ch;
		System.out.println("\n[정보조회]");
    }
    
	public void manageDepartment() {
		int ch;
		System.out.println("\n[부서관리]");
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void manageAttendance() {
		int ch;
		System.out.println("\n[근태관리]");
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void manageAuth() {
		int ch;
		System.out.println("\n[권한관리]");
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void manageBoard() {
		int ch;
		System.out.println("\n[게시판관리]");
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
