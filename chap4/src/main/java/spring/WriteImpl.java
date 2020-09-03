package spring;

public class WriteImpl {
	private ArticleDao dao;    // ArticleDao 는 인터페이스이므로, 이를 구현하는 구현클래스 객체가 들어오게 됨.
	// dao :  spring.MariadbArticleDao 객체 주입
	public WriteImpl(ArticleDao dao) {    // 생성자
		this.dao = dao;
	}
	public void write() {
		System.out.println("WriteImpl.write 메서드 호출");
		dao.insert();
	}
}
