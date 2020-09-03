package hash;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/*
 * 화면에서 아이디와 비밀번호 입력받아
 * 해당 아이디가 usersecurity 테이블에 없으면 "아이디를 확인하세요" 출력
 * 해당 아이디의 비밀번호를 비교해서 맞으면 "반갑습니다. 아이디님." 출력
 * 해당 아이디의 비밀번호를 비교해서 틀리면 "비밀번호를 확인하세요" 출력
 */
public class DigestMain3 {
	public static void main(String[] args) throws Exception {
		//db 설정
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classdb","scott","1234");
		PreparedStatement pstmt = conn.prepareStatement("select password from usersecurity where userid=?");
		//화면입력
		System.out.println("아이디를 입력하세요");
		Scanner scan = new Scanner(System.in);
		String scanid = scan.nextLine();
		System.out.println("비밀번호를 입력하세요");
		String scanpass = scan.nextLine();
		pstmt.setString(1, scanid);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {   // 아이디가 db에 존재하는 경우
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String hashpass = "";
			byte[] plain = scanpass.getBytes();
			byte[] hash = md.digest(plain);    //해쉬실행
			for(byte b : hash) {
				hashpass += String.format("%02X", b);
			}
			if(rs.getString(1).equals(hashpass)) {
				System.out.println("[반갑습니다, " + scanid + "님]");
			}else {
				System.out.println("[비밀번호가 틀립니다. 비밀번호를 확인하세요]");
			}
			
		}else {  // 아이디가 db에 존재하지 않는 경우
			System.out.println("[등록된 아이디가 없습니다. 아이디를 확인하세요]");
		}
	}
}
