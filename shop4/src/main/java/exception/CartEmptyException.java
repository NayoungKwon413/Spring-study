package exception;

// RuntimeException : 예외는 RuntimeException과 나머지로 나뉘는데, RuntimeException은 예외처리가 생략이 가능함
public class CartEmptyException extends RuntimeException {
	private String url;
	public CartEmptyException(String msg, String url) {
		super(msg);
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
}
