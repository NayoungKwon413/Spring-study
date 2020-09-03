package main;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import spring.ReadImpl;
import spring.WriteImpl;

public class Main1 {
	public static void main(String[] args) {
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:aop.xml");
		WriteImpl bean = ctx.getBean("write", WriteImpl.class);
		// LoggingAspect.loggin(...) 먼저 실행됨.
		// Logging() 내부에 joinPoint.proceed() 문장으로 write() 메서드 호출됨.
		bean.write();  // 기본메서드
		// logging()의 joinPoint.preceed() 문장으로 이후로 제어 이동
		System.out.println("===================");
		ReadImpl read = ctx.getBean("read", ReadImpl.class);   // read 이름으로 ReadImpl 객체를 가져옴
		// LoggingAspect.loggin(...) 먼저 실행됨.
		// Logging() 내부에 joinPoint.proceed() 문장으로 read() 메서드 호출됨.
		System.out.println("[main]" + read.read());
		// logging()의 joinPoint.preceed() 문장 이후로 제어 이동
		// reg 값을 main으로 전달  => [main]홍길동의 글 콘솔 출력
	}
}
