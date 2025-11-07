package com.sp.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sp.dao.AttDAO;
import com.sp.dao.BoardDAO;
import com.sp.dao.DeptDAO;
import com.sp.dao.EmpDAO;
import com.sp.dao.impl.AttDAOImpl;
import com.sp.dao.impl.BoardDAOImpl;
import com.sp.dao.impl.DeptDAOImpl;
import com.sp.dao.impl.EmpDAOImpl;


public class EmployeeUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private EmpDAO empDao = new EmpDAOImpl();
	private DeptDAO deptDao = new DeptDAOImpl();
	private AttDAO attDao = new AttDAOImpl();
	private BoardDAO boardDao = new BoardDAOImpl();
	
	public  EmployeeUI(EmpDAO empDao,DeptDAO deptDao,AttDAO attDao,BoardDAO boardDao) {
		this.empDao = empDao;
		this.deptDao = deptDao;
		this.attDao = attDao;
		this.boardDao = boardDao;
	}
	
	public void manageEmployee() {
		int ch;
		System.out.println("\n[사원관리]");
		try {
			
			do {
				System.out.print("1.정보등록 2.정보수정 3.정보조회 4.부서이동이력조회 5.이력조회  6.메뉴로돌아가기 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch < 1 || ch > 6);

			switch (ch) {
			case 1: empDao.insertEmployee(null); break;
			case 2: empDao.updateEmployee(null); break;
			case 3: empDao.selectByEmpNo(0); break;
			case 4: empDao.selectDeptMove(0); break;
			case 5: empDao.selectHistory(0); break;
			case 6: return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void manageDepartment() {
		int ch;
		System.out.println("\n[부서관리]");
		
		try {
			
			do {
				System.out.print("1.부서조회 2.부서인원현황 3.메뉴로돌아가기 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch < 1 || ch > 3);
			
			switch(ch) {
			case 1: deptDao.selectDeptByNo(0); break;
			case 2: deptDao.selectDeptMemberCount(); break;
			case 3: return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void manageAttendance() {
		int ch;
		System.out.println("\n[근태관리]");
		
		try {
			
			do {
				System.out.print("1.출근등록 2.퇴근등록 3.휴가신청 4.휴가수정 5.연차조회 6.근무시간조회 7.메뉴로돌아가기 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch < 1 || ch > 7);
			
			switch(ch) {
			case 1: attDao.insertAttendanceIn(null); break;
			case 2: attDao.insertAttendanceOut(null); break;
			case 3: attDao.insertVacation(null); break;
			case 4: attDao.updateVacation(null); break;
			case 5: attDao.selectVacationByEmp(0); break;
			case 6: attDao.selectWorkTimeByEmp(0); break;
			case 7: return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void manageBoard() {
		int ch;
		System.out.println("\n[게시판관리]");
		
		try {
			do {
				System.out.print("1.게시글등록 2.게시글수정 3.게시글삭제 4.메뉴로돌아가기 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch < 1 || ch > 4);
			
			switch(ch) {
			case 1: boardDao.insertPost(null); break;
			case 2: boardDao.updatePost(null); break;
			case 3: boardDao.deletePost(0); break;
			case 4: return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
