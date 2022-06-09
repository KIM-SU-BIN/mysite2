package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestBookVo;

public class GuestBookDao {

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

	// Guestbook 출력
	public List<GuestBookVo> getGuestList() {

		List<GuestBookVo> guestList = new ArrayList<GuestBookVo>();

		try {
			getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			// SQL문 준비
			String query = "";
			query += " select no ";
			query += "         ,name ";
			query += "         ,password ";
			query += "         ,content ";
			query += "         ,to_char(reg_date, 'YYYY-MM-DD HH:MI:SS') \"reg_date\" ";
			query += " from guestbook ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			System.out.println(query);
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String reg_date = rs.getString("reg_date");

				guestList.add(new GuestBookVo(no, name, password, content, reg_date));
				// list.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return guestList;
	}

	// Guest 추가
	public int insert(GuestBookVo vo) {
		int count = 0;
		this.getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " INSERT INTO guestbook ";
			query += " values(SEQ_GUESTBOOK_no.nextval, ?, ?, ?, sysdate) ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContent());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return count;
	}

	// Guest 찾기
	public GuestBookVo getGuest(int no) {

		GuestBookVo guestBookVo = null;
		this.getConnection();

		List<GuestBookVo> list = new ArrayList<GuestBookVo>();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " select no ";
			query += "         ,name ";
			query += "         ,password ";
			query += "         ,content ";
			query += "         ,to_char(reg_date, 'YYYY-MM-DD HH:MI:SS') \"reg_date\" ";
			query += " from guestbook ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			pstmt.setInt(1, no);

			rs = pstmt.executeQuery();
			System.out.println(query);
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String reg_date = rs.getString("reg_date");

				guestBookVo = new GuestBookVo(no, name, password, content, reg_date);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return guestBookVo;
	}

	// Guest 삭제
	public int guestDelete(GuestBookVo vo) {
		getConnection();
		int count = 0;

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " delete guestbook ";
			query += " where no = ? ";
			query += " and password = ? ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			pstmt.setInt(1, vo.getNo());
			pstmt.setString(2, vo.getPassword());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("[" + count + "건 삭제 되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return count;
	}
}