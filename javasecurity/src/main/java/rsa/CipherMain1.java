package rsa;
/*
 * 공개키 암호화 예제 : RSA => 개인키와 공개키로 암호화, 복호화에 사용됨
 */
public class CipherMain1 {
	public static void main(String[] args) {
		String plain1 = "안녕하세요 홍길동입니다.";
		String cipher1 = CipherRSA.encrypt(plain1);
		System.out.println("암호문: " + cipher1);
		String plain2 = CipherRSA.decrypt(cipher1);
		System.out.println("복호문: "+ plain2);
	}

}
