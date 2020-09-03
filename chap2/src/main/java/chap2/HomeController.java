package chap2;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component   // 객체화됨.
public class HomeController {
	private AlarmDevice alarmDevice;   // SmsAlarmDevice 객체 주입
	private Viewer viewer;			   // SmartPhoneViewer 객체 주입
	
	// 객체 이름으로 주입
	@Resource(name="camera1")
	private Camera camera1;
	@Resource(name="camera2")
	private Camera camera2;
	@Resource(name="camera3")
	private Camera camera3;
	@Resource(name="camera4")
	private Camera camera4;
	
	// 창센서, 현관센서 객체가 주입
	private List<InfraredRaySensor> sensors;
	
	@Autowired(required=false)   // (required=false) : 주입되는 객체가 없는 경우도 허용
	private Recorder recorder;
	
	@Autowired    // 반드시 주입되는 객체가 있어야 함.
				  // AlarmDevice 객체 : AlarmDevice는 인터페이스이므로, 구현클래스(SmsAlarmDevice)를 가지고 주입
				  // Viewer 객체 : Viewer는 인터페이스이므로, 구현클래스(SmartPhoneViewer)를 가지고 주입
				  // 객체가 주입되기 위해서는 무조건 
	public void prepare(AlarmDevice alarmDevice, Viewer viewer) {   // 해당 메서드를 통해서 객체가 주입됨.
		this.alarmDevice = alarmDevice;
		this.viewer = viewer;
	}
	
	@PostConstruct    // 객체 주입까지 모두 완료된 이후에 호출되는 메서드
	public void init() {
		System.out.println("init() 메서드 호출");
		viewer.add(camera1);
		viewer.add(camera2);
		viewer.add(camera3);
		viewer.add(camera4);
		viewer.draw();
	}
	
	@Autowired    // 객체 주입 -> Autowired가 완성되어야 객체 주입까지 모두 완료되는 것
	@Qualifier("intrusionDetection")   // 별명 설정(intrusionDetection 이름을 가진 qualifier 을 모두 가져올 것)
	public void setSensors(List<InfraredRaySensor> sensors) {
		System.out.println("setSensors() 메서드 호출");
		this.sensors = sensors;
		for(InfraredRaySensor s : sensors) {
//			alarmDevice.alarm(s.getName());
			System.out.println(s.getName());
		}
	}
	
	public void checkSensorAndAlarm() {
		for(InfraredRaySensor s : sensors) {
			if(s.isObjectFounded()) {
				alarmDevice.alarm(s.getName());
			}
		}
	}
}
/*
	기본 어노테이션
	1. 객체 생성 : @Component
		xml : <bean id="소문자로 시작하는 클래스이름" class="패키지를 포함한 클래스 이름">
		=> <context:component-scan base-package="chap2" /> 이 부분이 있어야 위의 컴포넌트를 실행할 수 있음
	2. 객체 주입  
		@Autowired : 변수 선언문 위, 메서드 위에 표현 가능
		 			  객체 선택 기준이 자료형임. 같은 자료형을 가진 객체가 여러개인 경우 주의 요함.
		 			 (required=false) : 객체가 없으면 null 로 주입하라는 옵션 설정 가능.
		@Resource(이름) : 객체 중 이름으로 객체를 선택하여 주입함.
		@Required : 객체 선택의 기준이 자료형임. 반드시 주입되어야 함.
	3. 그 외
		@PostConstruct : 객체 생성이 완료된 후 호출되는 메서드 지정. 객체 생성 완료란, 필요한 객체 주입이 완료된 시점(@Autowired 완료)을 말함. 
		@Qualifier(별명) : 객체에 설정된 별명을 사용함. 객체 주입시 @Autowired와 함께 사용됨.
		@Scope(...) : 생성된 객체의 지속가능한 영역 설정. 일회성 객체 생성을 가능하게 함. Scope이 없으면 무한 재사용 가능.

*/