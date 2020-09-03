package chap2;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		// 객체를 이미 생성해서 컨테이너에 저장함.
		// GenericXmlApplicationContext : xml 문서를 이용하여 객체 저장 
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:annotation.xml");  // xml 파일을 읽어서 컨테이너 구성
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
