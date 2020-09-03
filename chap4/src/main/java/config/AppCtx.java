package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import main.MemberService;



@Configuration // 설정을 위한 자바 프로그램 
@ComponentScan(basePackages = {"annotation"})   //객체화되는 패키지를 설정
// @ComponentScan(basePackages = {"annotation", "main"})  에러나지 않음
@EnableAspectJAutoProxy   //annotation 행태로 AOP를 사용할 거라 설정
public class AppCtx {
	@Bean
	public MemberService memberService2() {
		MemberService ms2 = new MemberService();
		return ms2;
	}
}
