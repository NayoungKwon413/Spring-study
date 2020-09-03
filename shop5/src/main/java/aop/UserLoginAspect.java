package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component // 객체화
@Aspect    // AOP 실행 클래스라는 의미
@Order(1)  // AOP 실행 순서
public class UserLoginAspect {
	// 기본메서드 실행 전, 후  -> 실행 전 들어가 실행 후까지 컨트롤 
	// pointcut : controller 패키지의 User 이름으로 시작하는 클래스
	//            메서드의 이름이 loginCheck 로 시작
	//            매개변수는 상관없음
	// args(..,session) : 가장 마지막 매개변수가 session 인 메서드를 의미.     
	// 로그인 검증 -> 메서드 이름으로 통일 시 가능.
	@Around("execution(* controller.User*.loginCheck*(..)) && args(..,session)")
	public Object userLoginCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable{
		User loginUser = (User)session.getAttribute("loginUser");   // 로그인 시, loginUser 라는 객체
		if(loginUser == null) {   // 로그인 안된 상태
			throw new LoginException("[userlogin]로그인 후 거래하세요", "login.shop");   // webapp/WEB-INF/view/exception.jsp 로 이동후 처리
		}
		return joinPoint.proceed();
	}
	
	@Around("execution(* controller.User*.check*(..)) && args(id,session)")
	public Object mypageCheck(ProceedingJoinPoint joinPoint, String id, HttpSession session) throws Throwable{
		//id = new User().getUserid();
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("로그인하세요", "login.shop");
		}else if(!loginUser.getUserid().equals("admin") && !id.equals(loginUser.getUserid())) {
			throw new LoginException("본인 정보만 조회 가능합니다", "main.shop");
		}
		return joinPoint.proceed();
	}
}
