package spring;

public class ReadImpl {
	private ArticleDao dao;
	public ReadImpl(ArticleDao dao) {
		this.dao = dao;
	}
	public String read() {
		System.out.println("ReadImpl.read 메서드 호출");
		return dao.select();
		
	}
}
