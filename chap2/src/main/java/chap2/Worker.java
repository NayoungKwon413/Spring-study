package chap2;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component   // 객체화
@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)    // 호출될때마다 다시 생성되게 설정 -> 재사용불가
public class Worker {
	public void work(WorkUnit unit) {
		System.out.println(this + ":work:" + unit);
	}
}
