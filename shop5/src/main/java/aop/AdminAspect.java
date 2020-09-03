package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect
@Order(3)
public class AdminAspect {
	@Around("execution(* controller.Admin*.*(..))")
	public Object adminCheck(ProceedingJoinPoint joinPoint) throws Throwable {
//		HttpSession session = (HttpSession)joinPoint.getArgs()[0];
//		User loginUser = (User)session.getAttribute("loginUser");
		
		// HttpSession이 앞에 있던, 뒤에 있던 순서에 상관없이 설정
		User loginUser = null;
		for(Object o : joinPoint.getArgs()) {
			if(o instanceof HttpSession) {
				HttpSession session = (HttpSession)o;
				loginUser = (User)session.getAttribute("loginUser");
			}
		}
		if(loginUser == null) {
			throw new LoginException("로그인 후 가능합니다", "../user/login.shop");
		}
		if(!loginUser.getUserid().equals("admin")) {
			throw new LoginException("관리자만 가능한 거래입니다.", "../user/main.shop");
		}
		return joinPoint.proceed();
	}
}
