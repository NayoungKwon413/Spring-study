package chap3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import chap2.Executor;
import chap2.HomeController;
import chap2.InfraredRaySensor;
import chap2.WorkUnit;

public class Main2 {
	public static void main(String[] args) {
		// ApplicationContext : 객체를 저장하고 있는 컨테이너
		// AnnotationConfigApplicationContext : 자바설정파일에서 객체 저장
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppCtx.class);
		Executor exec = ctx.getBean("executor", Executor.class);   // executor(worker 객체가 들어있는) 객체 
		exec.addUnit(new WorkUnit());
		exec.addUnit(new WorkUnit());
		
		HomeController home = ctx.getBean("homeController", HomeController.class);
		home.checkSensorAndAlarm();
		
		// 창문에 침입
		System.out.println("================= 창문에 침입자 발견 =====================");
		InfraredRaySensor sensor = ctx.getBean("windowSensor", InfraredRaySensor.class);
		sensor.foundObject();
		home.checkSensorAndAlarm();
		
		// 현관에 침입
		System.out.println("================= 현관에 침입자 발견 =====================");
		sensor = ctx.getBean("doorSensor", InfraredRaySensor.class);
		sensor.foundObject();
		home.checkSensorAndAlarm();
		
		// 전등에 침입
		System.out.println("================= 전등에 침입자 발견 =====================");
		sensor = new InfraredRaySensor("전등센서");   // 컨테이너에 있는 객체를 그대로 명확하게 사용해야 올바른 결과가 나타남.
													// 해당 문장으로 객체가 생성되긴 하나 컨테이너 안에 없는, 새로운 객체이므로 해당 결과가 발견되지 않음.
		sensor.foundObject();
		home.checkSensorAndAlarm();
	}

}
