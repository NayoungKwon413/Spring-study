package aes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * usersecurity 테이블의 암호화된 email 값을 복호화하여 화면에 출력하기
 * key 는 userid 해쉬값의 앞의 16자리로 정한다.
 */
public class CipherMain4 {
	public static void main(String[] args) throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classdb","scott","1234");
		PreparedStatement pstmt = conn.prepareStatement("select userid, email from usersecurity");
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			String id = rs.getString("userid");
			String key = CipherUtil.makehash(id).substring(0, 16);
			String email = rs.getString("email");   //암호화된 이메일
			String plainEmail = CipherUtil.decrypt(email, key);  //복호화
			System.out.println(id + "의 이메일:" + plainEmail);
		}
	}

}
