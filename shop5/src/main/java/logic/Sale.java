package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {
	private int saleid;
	private String userid;
	private Date saledate;
	private User user;
	private List<SaleItem> itemList = new ArrayList<SaleItem>();
	
	// mypage.jsp 에서 ${sale.total}을 가져오기 위해서는 Sale 객체 안에 getTotal이라는 프로퍼티가 필요함
	public long getTotal() {
		long sum = 0;
		for(SaleItem si : itemList) {
			sum += si.getItem().getPrice() * si.getQuantity();
		}
		return sum;
	}
	
	//getter, setter, toString
	public int getSaleid() {
		return saleid;
	}
	public void setSaleid(int saleid) {
		this.saleid = saleid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getSaledate() {
		return saledate;
	}
	public void setSaledate(Date saledate) {
		this.saledate = saledate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<SaleItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleItem> itemList) {
		this.itemList = itemList;
	}
	@Override
	public String toString() {
		return "Sale [saleid=" + saleid + ", userid=" + userid + ", saledate=" + saledate + ", user=" + user + "]";
	}
}
