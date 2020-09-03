package hash;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * 
 * 2. useraccount 테이블을 읽어 usersecurity 
 */

public class DigestMain2 {
	public static void main(String[] args) throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classdb","scott","1234");
		PreparedStatement pstmt = conn.prepareStatement("select userid, password from useraccount");
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			String id = rs.getString(1);
			String pass = rs.getString(2);
			// md : SHA-256 알고리즘을 실행하는 해쉬암호 객체
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String hashpass = "";
			byte[] plain = pass.getBytes();
			byte[] hash = md.digest(plain);    //해쉬실행
			for(byte b : hash) {
				hashpass += String.format("%02X", b);
			}
			pstmt.close();
			pstmt = conn.prepareStatement("update usersecurity set password=? where userid=?");
			pstmt.setString(1, hashpass);
			pstmt.setString(2, id);
			pstmt.execute();
		}
	}
}
