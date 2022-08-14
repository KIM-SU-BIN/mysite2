package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		System.out.println("UserContlroller");

		// action을 꺼낸다
		String action = request.getParameter("action");
		System.out.println(action);
		

		if ("joinForm".equals(action)) {// 회원가입 폼
			System.out.println("UserController>joinForm");

			// joinForm.jsp과 포워드!(=회원가입 폼 포워드)
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");

		} else if ("join".equals(action)) {		// 회원가입
			System.out.println("UserController>join");

			// 파라미터 꺼내기*4
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");

			// 0*333 Vo만들기
			UserVo uservo = new UserVo(id, password, name, gender);
			System.out.println(uservo);

			// Dao를 이용하여 저장하기
			UserDao userDao = new UserDao();
			userDao.insert(uservo);

			// 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");

		} else if ("loginForm".equals(action)) { // 로그인 폼
			System.out.println("UserContoller>loginForm");

			// 로그인 폼 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
			
		} else if ("login".equals(action)) {// 로그인
			System.out.println("UserContoller>login");

			// 파라미터 꺼내기
			String id = request.getParameter("id");
			String password = request.getParameter("password");

			// Vo만들기
			UserVo userVo = new UserVo();
			userVo.setId(id);
			userVo.setPassword(password);

			// dao
			UserDao userDao = new UserDao();
			UserVo authUser = userDao.getUser(userVo); // id,password--> user전체

			// authUser 주소값이 있으면 --> 로그인 성공
			// authUSer null이면 --> 로그인 실패
			if (authUser == null) {
				System.out.println("로그인 실패");
			} else {
				System.out.println("로그인 성공");

				HttpSession session = request.getSession();
				session.setAttribute("authUser", authUser);

				// 메인 리다이렉트
				WebUtil.redirect(request, response, "/mysite2/main");

			}

		} else if ("logout".equals(action)) {
			System.out.println("UserController>logout");

			// 세션값을 지운다
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();

			// 메인으로 리다이렉트
			WebUtil.redirect(request, response, "/mysite2/main");
			
			
		}else if ("logout".equals(action)) {		// 로그아웃
			System.out.println("UserController>logout");

			// 세션값을 지운다
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();

			// 메인으로 리다이렉트
			WebUtil.redirect(request, response, "/mysite2/main");
		
		
		
		} else if ("modifyForm".equals(action)) {		// 수정폼
			System.out.println("UserController>modifyForm");
			
			//로그인시 사용자의 no 값을 세션에서 가져오기
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo) session.getAttribute("authUser");
			
			//no로 사용자 정보 가져오기
			UserDao userDao = new UserDao();
			UserVo userVo = userDao.getUser(authUser.getNo());
			
			//request의 attribute에 userVo를 넣어서 포워딩
			request.setAttribute("userVo", userVo);
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");

		} else if ("modify".equals(action)) {
			
			//세션에서 no 가져옴
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			int no = authUser.getNo();
			String id = authUser.getId();
			
			//파라미터 가져옴
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			

			// Dao를 이용해서 저장하기
			UserVo userVo = new UserVo(no, id, password, name, gender);
			UserDao userDao = new UserDao();
			int count = userDao.updateUser(userVo);
            
			authUser = userDao.getUser(userVo);
			session.setAttribute("authUser", authUser);

			// modifyForm과 포워드!(=수정폼 포워드)
			WebUtil.redirect(request, response, "mysite2/main");

		} else {
			System.out.println("action 파라미터 없음");
			WebUtil.redirect(request, response, "/mysite2/main");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

} 