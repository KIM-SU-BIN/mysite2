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
		
		

		if("list".equals(action)) {		//게시판 폼  OK
			System.out.println("BoardController>list");
			
			//boardList 데이터 가져오기
			BoardDao BoardDao = new BoardDao();
			List<BoardVo> boardList = BoardDao.getBoardList();
			System.out.println(boardList);
			
			//request에 데이터 추가
			request.setAttribute("boardList", boardList);
			
			// list.jsp과 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");

			
			
			
		}else if("writeForm".equals(action)) {		 //게시판 리스트	 OK
			System.out.println("BoardController>writeForm");
						
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
		
	
		} else if("write".equals(action)) {			//write OK
			System.out.println("boardController->write");	
			
			
			int userNo = Integer.parseInt(request.getParameter("userNo"));
			System.out.println(userNo);
			
			//글쓰기폼에 제목과 콘텐트
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			//db에 저장
			BoardDao boardDao = new BoardDao();
			boardDao.boardWrite(new BoardVo(title, content, userNo));
			
			//list로 리다이렉트
			WebUtil.redirect(request, response, "./board?action=list");
			
			
			
		
		} else if("read".equals(action)) {		//게시판 글 보기 
			System.out.println("BoardController>read");
			
			//주의 -> getParameter은 string타입으로 숫차로 변환시 int를 사용하여 변환하기!!!!
			int no = Integer.parseInt(request.getParameter("no"));
			System.out.println(no);
			
			//read 데이터 가져오기
			BoardDao BoardDao = new BoardDao();
			BoardVo boardVo = BoardDao.getBoard(no);
			System.out.println(boardVo);
			
			//조회수
			boardVo.setHit(boardVo.getHit()+1);
			BoardDao.boardUpdateHit(boardVo);
			
			//request에 데이터 추가
			request.setAttribute("boardVo", boardVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
	
			
			
			
			
	
		} else if("modifyForm".equals(action)) {		//modifyForm
			System.out.println("BoardController>modifyForm");
			
			//제목, 콘텐트 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			
			//db에 저장
			BoardDao boardDao = new BoardDao();
			
			//생성자 대신 사용
			BoardVo boardVo = boardDao.getBoard(no);
			request.setAttribute("boardVo", boardVo);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");	
			
			
			
			
		} else if("modify".equals(action)) {		//modify
			System.out.println("BoardController>modify");
			
			//제목, 콘텐트 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			String name = request.getParameter("name");
			String content = request.getParameter("content");
			
			//db에 저장
			BoardDao boardDao = new BoardDao();
			
			//생성자 대신 사용
			BoardVo boardVo = new BoardVo();
			boardVo.setNo(no);
			boardVo.setName(name);
			boardVo.setContent(content);
			
			boardDao.boardModify(boardVo);
			
			WebUtil.redirect(request, response, "./board?action=list");
			
			
			
			
		
		}	else if("delete".equals(action)) {			//삭제
			System.out.println("boardController->delete");	
			
			//no 파라미터 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			
			//board 삭제
			BoardDao boardDao = new BoardDao();
			boardDao.boardDelete(no);
			
			//list로 리다이렉트
			WebUtil.redirect(request, response, "./board?action=list");
			
			
		}else {
				System.out.println("action 파라미터 없음");
				WebUtil.redirect(request, response, "./board?action=list");
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
