package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;


@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		System.out.println("BoardContlroller");
		
		//action 꺼내기
		String action = request.getParameter("action");
		System.out.println(action);
		
		
		
		
		
		
		
		if("list".equals(action)) {		//게시판 폼 
			System.out.println("BoardController>list");
			
			//boardList 데이터 가져오기
			BoardDao BoardDao = new BoardDao();
			List<BoardVo> boardList = BoardDao.getBoardList();
			
			//request에 데이터 추가
			request.setAttribute("boardList", boardList);
			
			// list.jsp과 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");

			
		}else if("writeForm".equals(action)) {		 //게시판 리스트
			System.out.println("BoardController>writeForm");
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
		
	
			
			
		
		} else if("read".equals(action)) {		//게시판 글 보기
			System.out.println("BoardController>read");
			
			//주의 -> getParameter은 string타입으로 숫차로 변환시 int를 사용하여 변환하기!!!!
			int no = Integer.parseInt(request.getParameter("no"));
			
			//read 데이터 가져오기
			BoardDao BoardDao = new BoardDao();
			BoardVo boardVo = BoardDao.getBoard(no);
			
			
			
			//request에 데이터 추가
			request.setAttribute("boardVo", boardVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}