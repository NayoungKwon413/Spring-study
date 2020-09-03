package logic;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<ItemSet>();
	public List<ItemSet> getItemSetList() {
		return itemSetList;
	}
	public void push(ItemSet itemSet) {
		for(ItemSet it : itemSetList) {
			// 원래 저장된 itemSetList에 새로 입력한 id가 같은 id가 있을 경우
			if(it.getItem().getId().equals(itemSet.getItem().getId())) {
				it.setQuantity(it.getQuantity() + itemSet.getQuantity());   // 기존 itemSetList의 quantity에 새로 입력된 quantity를 더해줌
				return;
			}
		}
		itemSetList.add(itemSet);
	}
	public long getTotal() {
		long sum = 0;
		for(ItemSet is : itemSetList) {
			sum += is.getItem().getPrice() * is.getQuantity();
		}
		return sum;
	}
}
