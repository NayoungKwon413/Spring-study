package controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

public class IndexController {
	// shopService : logic.ShopService 객체 주입
	private ShopService shopService;
	// setShopService(set 프로퍼티) 를 통해 주입 완료
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	@RequestMapping   //요청정보를 매핑. index.shop 요청 시 호출되는 메서드
	// Model : 데이터베이스  View : jsp 페이지  => ModelAndView 이들을 모두 갖고 있음
	public ModelAndView itemList() {
		// itemList : item 테이블에서 모든 정보를 저장하고 있는 List 객체
		List<Item> itemList = shopService.getItemList();
		// view 이름 : index 로 설정
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("itemList", itemList);   //  view 로 전송할 data 저장
		return mav;
	}
}
