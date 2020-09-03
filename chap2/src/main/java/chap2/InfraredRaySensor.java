package chap2;

public class InfraredRaySensor {
	private String name;
	private boolean objectFounded;
	public InfraredRaySensor(String name) {
		this.name = name;
	}
	public void foundObject() {
		this.objectFounded = true;
	}
	// getter, setter, toString
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isObjectFounded() {   //boolean 일 경우 get~ 이 아니라 is~로 나타남
		return objectFounded;
	}
	public void setObjectFounded(boolean objectFounded) {
		this.objectFounded = objectFounded;
	}
	@Override
	public String toString() {
		return "InfraredRaySensor [name=" + name + ", objectFounded=" + objectFounded + "]";
	}
}
