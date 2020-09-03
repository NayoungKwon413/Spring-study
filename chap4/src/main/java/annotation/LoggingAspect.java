package annotation;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component    //객체화
@Aspect       //aop 에 의해 실행되는 클래스
@Order(3)     //3번째 순서
public class LoggingAspect {
	final String publicMethod = "execution(public * annotation..*(..))";   // annotation 패키지 안에 있는 모든 public 메서드는 기본메서드
	// 기본메서드(annotation 패키지 안에 있는 모든 public 메서드) 실행 전 호출
	@Before(publicMethod)    
	public void before() {
		System.out.println("[LA] 메서드 실행 전 실행");
	}
	// 기본메서드(annotation 패키지 안에 있는 모든 public 메서드) 가 정상 종료 후 호출
	@AfterReturning(pointcut=publicMethod, returning="ret")
	public void afterReturning(Object ret) {
		System.out.println("[LA]메서드 정상 종료 후 실행. 리턴값=" + ret);
	}
	// 기본메서드(annotation 패키지 안에 있는 모든 public 메서드) 가 예외 종료 후 호출
	@AfterThrowing(pointcut=publicMethod, throwing="ex")
	public void afterThrowing(Throwable ex) {
		System.out.println("[LA]메서드 예외 종료 후 실행. 예외 메세지=" + ex.getMessage());
	}
	// 기본메서드(annotation 패키지 안에 있는 모든 public 메서드) 가 종료 후 호출
	@After(publicMethod)
	public void afterFinally() {
		System.out.println("[LA]메서드 종료 후 실행");
	}
}
