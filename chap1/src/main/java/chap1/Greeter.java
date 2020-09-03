package chap1;

public class Greeter {
	private String format;
	public String greet(String guest) {    // guest = %s 즉, 스프링
		return String.format(format, guest);   // format = %s 을 시작합니다
	}
	//format : %s을 시작합니다   -> 의존성 주입
	public void setFormat(String format) {
		this.format = format;
	}
}
