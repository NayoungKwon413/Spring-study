package chap2;

// xml 에서 객체화. 4개의 camera 객체를 생성
public class Camera {
	private int number;
	public void setNumber(int number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "Camera [number=" + number + "]";
	}
}
