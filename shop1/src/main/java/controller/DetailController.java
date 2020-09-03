package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

// /detail.shop 요청시 호출되는 Controller
public class DetailController {
	private ShopService shopService;
	// setShopService(set 프로퍼티) 를 통해 주입 완료(DI 완료)
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	@RequestMapping
	// 컨트롤러에 있는 매개변수 이름과 파라미터 이름이 같은 경우 값이 설정됨.
	public ModelAndView detail(Integer id) {
		// item : id에 해당하는 db 정보를 읽어서 저장
		Item item = shopService.getItemById(id);
		ModelAndView mav = new ModelAndView();   // url 기본 view 설정 -> detail.shop
		mav.addObject("item", item);
		return mav;
	}
}
