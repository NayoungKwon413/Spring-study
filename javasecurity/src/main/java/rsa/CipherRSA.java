package rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

/*
 * RSA : 공개키 암호 알고리즘. 비대칭키
 * 		 공개키로 암호화 -> 개인키로 복호화 가능  : 기밀성(인증받은 사람만 볼 수 있도록 처리)
 * 		 개인키로 암호화 -> 공개키로 복호화 가능  : 부인 방지 
 * 
 * 무결성 : 해시값으로 처리(값을 보내면서 해시값도 같이 보냄)
 */
public class CipherRSA {
	static Cipher cipher;
	static PrivateKey priKey;   //개인키
	static PublicKey pubKey;    //공개키
	static {
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			//공개키 방식의 알고리즘에서 사용되는 키 생성 객체
			KeyPairGenerator key = KeyPairGenerator.getInstance("RSA");
			key.initialize(2048);  //2048비트로 키 크기를 설정
			KeyPair keyPair = key.genKeyPair();
			priKey = keyPair.getPrivate();  //개인키
			pubKey = keyPair.getPublic();   //공개키
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static String encrypt(String plain) {
		byte[] cipherMsg = new byte[1024];
		try {
			cipher.init(Cipher.ENCRYPT_MODE, priKey);   //암호화 모드 설정, 암호키를 개인키로 설정
			cipherMsg = cipher.doFinal(plain.getBytes());   //암호문을 만듦
		}catch(Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg);   //byte -> hex 값으로 문자열로 리턴
	}
	private static String byteToHex(byte[] cipherMsg) {
		if(cipherMsg == null)  return null;
		int len = cipherMsg.length;
		String str = "";
		for(byte b : cipherMsg) {
			str += String.format("%02X", b);
		}
		return str;
	}
	public static String decrypt(String cipherMsg) {
		byte[] plainMsg = new byte[1024];
		try {
			cipher.init(Cipher.DECRYPT_MODE, pubKey);   //복호화 -> 공개키로 복호화하도록 설정
			plainMsg = cipher.doFinal(hexToByte(cipherMsg.trim()));   // 문자(hex)열을 다시 byte로 변환
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();
	}
	private static byte[] hexToByte(String str) {
		if(str == null || str.length() < 2) return null;
		byte[] buf = new byte[str.length() / 2];
		for(int i=0; i<buf.length; i++) {
			buf[i] = (byte) Integer.parseInt(str.substring(i*2, i*2+2), 16);
		}
		return buf;
	}
}
