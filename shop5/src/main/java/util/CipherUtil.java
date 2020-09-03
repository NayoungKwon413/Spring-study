package util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil {
	private final static byte[] iv = new byte[] {
			(byte) 0X8E, 0X12, 0X39, (byte) 0X9C,
				0X07, 0X72, 0X6F, (byte) 0X5A,
				(byte) 0X8E, 0X12, 0X39, (byte) 0X9C,
				0X07, 0X72, 0X6F, (byte) 0X5A };
	static Cipher cipher;
	static {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static String makehash(String plain) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] pbyte = plain.getBytes();
		byte[] hash = md.digest(pbyte);
		return byteToHex(hash);
	}

	private static String byteToHex(byte[] hash) {
		if(hash == null) {
			return null;
		}
		String str = "";
		for(byte b : hash) str += String.format("%02X", b);
		return str;
	}

	public static String decrypt(String ciphermsg, String key) {
		byte[] plainMsg = new byte[1024];
		try {
			Key genkey = new SecretKeySpec(key.getBytes(), "AES");
			// AlgorithmParameterSpec : 초기화벡터 -> CBC모드에서 필요(사이퍼블락체인. 앞에 암호화된 문장이 뒷부분에 영향을 미침
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			//복호화 모드 설정
			cipher.init(Cipher.DECRYPT_MODE, genkey, paramSpec);
			plainMsg = cipher.doFinal(hexToByte(ciphermsg.trim()));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();
	}

	private static byte[] hexToByte(String str) {
		if(str == null || str.length() < 2)  return null;
		int len = str.length() / 2;
		byte[] buf = new byte[len];
		for(int i=0; i<len; i++) {
			buf[i] = (byte)Integer.parseInt(str.substring(i*2, i*2+2), 16);
		}
		return buf;
	}

	public static String encrypt(String plain, String key) {
		byte[] cipherMsg = new byte[1024];
		try {
			Key genKey = new SecretKeySpec(key.getBytes(), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, genKey, paramSpec);
			cipherMsg = cipher.doFinal(plain.getBytes());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg);
	}
	
}
