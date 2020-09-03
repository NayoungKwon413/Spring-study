package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

@Controller   // @Component + controller(요청을 받을 수 있는 객체) action 역할
@RequestMapping("item")   // /item/ 요청시 Controller
public class ItemController {

	@Autowired
	private ShopService service;
	
	@RequestMapping("list")   // /item/list.shop 요청이 들어오면 실행
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		List<Item> itemList = service.getItemList();   // item 목록 view단으로 전달하기
		mav.addObject("itemList", itemList);
		return mav;
	}
	
	@RequestMapping("create")
	public String addform(Model model) {  // Model : view 에 전달할 데이터 객체
		model.addAttribute(new Item());
		return "item/add";  // view 이름
	}
	
	@RequestMapping("register")
	// add.jsp에서 입력한 name값은 item의 name으로 등록. -> Item.java에서 유효성 검증 -> 그 결과 BindingResult 로 저장 -> 에러가 있으면, 해당 오류 메세지 뷰단으로 출력
	public ModelAndView add(@Valid Item item, BindingResult br, HttpServletRequest request) {   
		ModelAndView mav = new ModelAndView("item/add");
		if(br.hasErrors()) {
			mav.getModel().putAll(br.getModel());
			return mav;
		}
		service.itemCreate(item, request);
		mav.setViewName("redirect:/item/list.shop");   // 상품 등록이 끝나면 list.shop으로 이동
		return mav;
		
	}
	
	@PostMapping("update")
	public ModelAndView update(@Valid Item item, BindingResult br, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/edit");
		if(br.hasErrors()) {
			mav.getModel().putAll(br.getModel());
			return mav;
		}
		// 유효성 검증 후 -> db, 파일 업로드
		service.itemUpdate(item, request);
		mav.setViewName("redirect:/item/detail.shop?id="+item.getId());
		return mav;
	}
	
	@GetMapping("delete")
	public ModelAndView delete(String id) {
		ModelAndView mav = new ModelAndView();
		service.itemDelete(id);
		mav.setViewName("redirect:/item/list.shop");
		return mav;
	}
	
	@GetMapping("*")   // /item/*.shop : item 밑에 지정해주지 않은 모든 값들일 때 해당 controller호출->처리
	public ModelAndView detail(@Valid Integer id) {
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item", item);
		return mav;
	}
}
