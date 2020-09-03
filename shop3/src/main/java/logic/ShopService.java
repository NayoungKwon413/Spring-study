package logic;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.BoardDao;
import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import dao.UserDao;

@Service   //@Component 하위 클래스 = @Component + service(Controller-요청부분 와 dao-db 중간 역할)
public class ShopService {
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SaleDao saleDao;
	@Autowired
	private SaleItemDao saleitemDao;
	@Autowired
	private BoardDao boardDao;

	public List<Item> getItemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.selectOne(id);
	}

	// 파일 업로드, dao에 데이터 전달
	public void itemCreate(Item item, HttpServletRequest request) {
		// 업로드 되는 파일이 존재하는 경우
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(), request, "item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.insert(item);     //저장된 파일 db에 저장
	}
	
	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request, String path) {
		// picture : 파일의 내용 저장
		String orgFile = picture.getOriginalFilename();
		String uploadPath = request.getServletContext().getRealPath("/") + path;
		File fpath = new File(uploadPath);
		if(!fpath.exists()) fpath.mkdirs();   // 해당 폴도 없을 경우 만들것.
		try {
			picture.transferTo(new File(uploadPath + orgFile));   // 파일의 내용을 실제 물리적인 파일로 저장
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	// 파일 업로드, dao 수정된 데이터 전달
	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(), request, "item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}

	public void itemDelete(String id) {
		itemDao.delete(id);
		
	}

	public void userInsert(User user) {
		userDao.insert(user);
		
	}

	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}

	/*
	 * checkend 기능 : db에 sale 정보와 saleitem 정보를 저장 -> 저장된 내용을 Sale 객체로 리턴
	 * 1. sale 테이블의 saleid 값을 설정 => 최대값+1
	 * 2. sale 의 내용 설정 => insert 
	 * 3. Cart 정보로부터 SaleItem 내용 설정 => insert 
	 * 4. 모든 정보를 Sale 객체로 저장
	 */
	public Sale checkend(User loginUser, Cart cart) {
		Sale sale = new Sale();
		int maxnum = saleDao.getMaxSaleid();
		sale.setSaleid(++maxnum);
		sale.setUser(loginUser);
		sale.setUserid(loginUser.getUserid());
		saleDao.insert(sale);
		// 장바구니에서 판매 상품 정보를 가져오기
		List<ItemSet> itemList = cart.getItemSetList();   // itemList : Cart 상품 정보
		int i=0;
		for(ItemSet is : itemList) {
			int seq = ++i;
			SaleItem saleItem = new SaleItem(sale.getSaleid(), seq, is);   // 생성자(int saleid, int seq, ItemSet itemSet)
			sale.getItemList().add(saleItem);   // Sale 객체에 SaleItem 추가
			saleitemDao.insert(saleItem);       // SaleItem 정보 db 등록
		}
		return sale;
	}

	public List<Sale> saleList(String id) {
		return saleDao.list(id);    // id:사용자 id
	}

	public List<SaleItem> saleItemList(int saleid) {
		return saleitemDao.list(saleid);    // saleid : 주문번호
	}

	public void userUpdate(User user) {
		userDao.update(user);
		
	}

	public void userDelete(User user) {
		userDao.delete(user);
		
	}

	public List<User> getUserList() {
		return userDao.list();
	}

	public List<User> userList(String[] idchks) {
		return userDao.list(idchks);
	}

	public void bordWrite(Board board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			uploadFileCreate(board.getFile1(), request, "board/file/");
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		int max = boardDao.maxnum();
		board.setNum(++max);
		board.setGrp(max);
		boardDao.insert(board);
	}

	public int boardCount(String searchtype, String searchcontent) {
		return boardDao.count(searchtype, searchcontent);
	}

	public List<Board> getBoardList(Integer pageNum, int limit, String searchtype, String searchcontent) {
		return boardDao.list(pageNum, limit, searchtype, searchcontent);
	}

//	public Board getBoardOne(int num) {
//		return boardDao.selectOne(num);
//	}
//
//	public void addReadcnt(int num) {
//		boardDao.addReadcnt(num);
//	}

	public Board getBoard(Integer num, boolean readcntable) {
		if(readcntable) {
			boardDao.addReadcnt(num);
		}
		return boardDao.selectOne(num);
	}

	public void bordReply(Board board) {
		boardDao.updateGrpStep(board);  //같은 그룹에 속한 것 중에 grpstep 증가(기존답글에 대한 정보
		int max = boardDao.maxnum();
		board.setNum(++max);
		board.setGrplevel(board.getGrplevel()+1);
		board.setGrpstep(board.getGrpstep()+1);
		boardDao.insert(board);
	}

	public void updateBoard(Board board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {   //업로드되는 파일이 존재하는 경우
			uploadFileCreate(board.getFile1(), request, "board/file/");
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.update(board);
	}

	public void deleteBoard(Board board) {
		boardDao.delete(board);
		
	}

	
}
