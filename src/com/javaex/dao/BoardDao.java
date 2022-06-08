package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {

	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	private void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

	}

	public List<BoardVo> getBoardList() { // list

		//List<BoardVo> boardList = new ArrayList<BoardVo>();
		List<BoardVo> list = new ArrayList<>();
		
		this.getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			// SQL문 준비
			String query = ""; // 항상고정
			query += " select  board.no ";
			query += "         ,board.title ";
			query += "         ,board.content ";
			query += "         ,board.hit ";
			query += "         ,to_char(board.reg_date,'YY-MM-DD HH24:MI') \"reg_date\" ";
			query += "         ,board.user_no ";
			query += "         ,users.name ";
			query += " from board, users ";
			query += " where board.user_no = users.no ";

			// 바인딩 => 쿼리로 만들기
			pstmt = conn.prepareStatement(query);

			// 실행
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String date = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String name = rs.getString("name");

				list.add(new BoardVo(no, title, content, hit, date, userNo, name));
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return list;

	}

	public BoardVo getBoard(int no) { // board read
		BoardVo boardVo = null;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " select  board.no ";
			query += "         ,board.title ";
			query += "         ,board.content ";
			query += "         ,board.hit ";
			query += "         ,to_char(b.reg_date,'YY-MM-DD HH24:MI') \"reg_date\" ";
			query += "         ,board.user_no ";
			query += "         ,users.name ";
			query += " from board , users ";
			query += " where board.user_no = users.no ";
			query += " and board.no = ? ";

			// 바인딩
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			pstmt.setInt(1, no);

			// 실행
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String date = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String name = rs.getString("name");

				boardVo = new BoardVo(no, title, content, hit, date, userNo, name);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return boardVo;

	}

	public void boardWrite(BoardVo boardVo) { 	// write 글쓰기

		//int count = -1;

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			// SQL문 준비
			String query = ""; // 고정
			query += " insert into board ";
			query += " values (seq_board_no.nextval, ?, ?, 0, sysdate, ?) ";

			// 바인딩 => 쿼리로 만들기
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getUserNo());

			// 실행
			int count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("[" + count + "건 추가되었습니다.]");
			
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		//return count;
		
		

	}	public void boardDelete(int no) { 		// delete
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행 
			// SQL문 준비
			String query = ""; // 고정
			query += " delete from board ";
			query += " where no = ? ";

			// 바인딩 => 쿼리로 만들기
			pstmt = conn.prepareStatement(query); 
			pstmt.setInt(1, no);
			
			// 실행
			int count = pstmt.executeUpdate();
		
			// 4.결과처리
			System.out.println("["+count+"건 삭제 되었습니다.]");
			

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

	}
}
