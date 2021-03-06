package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import annotation.MemberService;

import xml.Article;
import xml.Member;
import xml.ReadArticleService;
import xml.UpdateInfo;

public class main4 {
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(config.AppCtx.class);
		ReadArticleService service = ctx.getBean("readArticleService", ReadArticleService.class);
		try {
			Article a1 = service.getArticleAndReadCnt(1);
			Article a2 = service.getArticleAndReadCnt(1);
			System.out.println("[main]a1=a2 : " + (a1==a2)); 
			service.getArticleAndReadCnt(0);
		}catch(Exception e) {
			System.out.println("[main]" + e.getMessage());
		}
		System.out.println("\n UpdateMemberInfoTraceAspect 연습");
		MemberService ms = ctx.getBean("memberService", MemberService.class);
		ms.regist(new Member());   //loggingAspect 에 속한 
		ms.update("hong", new UpdateInfo());
		ms.delete("hong2", "test");

		System.out.println("\n main.MemberService 객체 연습");
		main.MemberService ms2 = ctx.getBean("memberService2", main.MemberService.class);
		ms2.regist(new Member());   //loggingAspect 에 속한 
		ms2.update("hong", new UpdateInfo());
		ms2.delete("hong2", "test");
	}

}
