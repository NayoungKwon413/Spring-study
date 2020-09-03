package chap1;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationContext.xml");  //컨테이너(applicationContext.xml 을 읽음)
		Greeter g = ctx.getBean("greeter", Greeter.class);  // 이름이 greeter 인 클래스를 가져올 것
		System.out.println(g.greet("스프링"));
		// m : MessageKr 의 객체
		Message m = ctx.getBean("message", Message.class);
//		m.sayHello("홍길동");
		m.sayHello("Hongkildong");
		Greeter g2 = ctx.getBean("greeter", Greeter.class);
		if(g == g2) {
			System.out.println("g 참조변수 객체와 g2 참조변수 객체는 같은 객체임");
		}
	}

}
