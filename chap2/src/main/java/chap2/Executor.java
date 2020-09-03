package chap2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component   // 객체화(객체를 생성) == <bean id="executeor" class="chap2.Executor" />
public class Executor {
	@Autowired   // 컨테이너에서 Worker 라는 객체를 주입(만들어져 있는 객체를 주입시킴 = DI)
	private Worker worker;
	public void addUnit(WorkUnit unit) {
		worker.work(unit);
	}
}
