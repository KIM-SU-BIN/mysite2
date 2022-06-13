package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestBookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestBookVo;


@WebServlet("/gbc")
public class GuestbookController extends HttpServlet {
	//필드
	private static final long serialVersionUID = 1L;
       
    //생성자-디폴트

	
	
	//get 방식
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//post 방식일때 한글 깨짐 방지
		request.setCharacterEncoding("UTF-8");
		
		
		//action 파라미터 꺼내기
		String action = request.getParameter("action");
		System.out.println(action);
		
		if("addList".equals(action)) { //리스트
			//데이터 가져오기
			GuestBookDao guestBookDao  = new GuestBookDao();
			List<GuestBookVo> guestList = guestBookDao .getGuestList();
			System.out.println(guestList);
			
			//request에 데이터 추가
			request.setAttribute("gList", guestList);
			WebUtil.forward(request, response, "/WEB-INF/addList.jsp");
			
			//데이터 + html -> jsp
			//RequestDispatcher rd = request.getRequestDispatcher("./addList.jsp");
			//rd.forward(request, response);
			
			
			
			
		} else if("add".equals(action)) { //글 추가하기
			//파라미터 가져오기
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");
			
			//guestInsert로 DB에 추가
	         GuestBookVo guestBookVo = new GuestBookVo(name, password, content);
	         GuestBookDao guestBookDao = new GuestBookDao();
	         guestBookDao.insert(guestBookVo);
			
			//리다이렉트 list
	         WebUtil.redirect(request, response, "/guestbook2/gbc?action=addList");
			
		
		
		}else if("deleteForm".equals(action)) {
	    	 
	         WebUtil.forward(request, response, "/WEB-INF/deleteForm.jsp");
		
		
		} else if("delete".equals(action)) {
			
			//파라미터 가져오기 => addList정보가 담긴 no
			int delNo = Integer.parseInt(request.getParameter("del_no"));
			String delPw = request.getParameter("del_pw");
			
			GuestBookDao guestBookDao = new GuestBookDao();
			GuestBookVo guestBookVo = guestBookDao.getGuest(delNo);
			
			//입력한 비밀번호와 같으면 삭제
			if(guestBookVo.getPassword().equals(delPw)) {
				
				 int count = guestBookDao.guestDelete(guestBookVo);
				 
				 WebUtil.redirect(request, response, "/guestbook2/gbc?action=addList");
				 System.out.println(count);
			
			} else {
				//리다이렉트 list
				WebUtil.redirect(request, response, "/guestbook2/gbc?action=addList");
				
				System.out.println("비밀번호가 틀렸습니다.");
			}
		
		} 	
		
	}
	
	//post 방식
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}