package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("cart")
public class CartController {

	@Autowired
	private ShopService service;
	
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id, Integer quantity, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id);   // 선택된 상품객체
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null) {   // 등록된 cart 객체가 없을 경우 
			cart = new Cart();    
			session.setAttribute("CART", cart);
		}
		cart.push(new ItemSet(item, quantity));    // Cart.java의 push 메서드를 통해 itemSetList에 itemSet 저장
		mav.addObject("message", item.getName() + ":" + quantity + "개 장바구니 추가");
		mav.addObject("cart", cart);
		return mav;
	}
	
	@RequestMapping("cartDelete")
	public ModelAndView delete(int index, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		ItemSet itemset = null;
		try {
			// List.remove(int) : index에 해당하는 객체를 제거
			// List.remove(Integer) : Integer는 Object로 인식. Object 객체 자체를 제거
			// itemset : List에서 제거된 객체를 저장 -> message로 찍어주기 위해 저장함.
			itemset = cart.getItemSetList().remove(index);
			mav.addObject("message", itemset.getItem().getName() + "상품을 삭제하였습니다.");
		}catch(Exception e) {
			mav.addObject("message", "장바구니 상품이 삭제되지 않았습니다.");
		}
		mav.addObject("cart", cart);
		return mav;
		
	}
	
	@RequestMapping("cartView")
	public ModelAndView cartview(HttpSession session) {
		ModelAndView mav  = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size()==0) {
			throw new CartEmptyException("장바구니에 상품이 없습니다.", "../item/list.shop");
		}
		mav.addObject("cart", cart);
		return mav;
	}
	
	@RequestMapping("checkout")   // AOP 클래스에서 첫번째 매개변수를 사용하므로 첫번째 매개변수는 HttpSession이어야함.
	public String checkout(HttpSession session) {
		return null;
	}
	
	@RequestMapping("end")
	public ModelAndView checkend(HttpSession session) {   // 메서드 이름에 check가 들어가므로 -> CartAspect 
		ModelAndView mav = new ModelAndView();
		Cart cart = (Cart)session.getAttribute("CART");
		User loginUser = (User)session.getAttribute("loginUser");
		Sale sale = service.checkend(loginUser, cart);
		long total = cart.getTotal();
		session.removeAttribute("CART");   // 주문완료 후 장바구니 비우기 -> invalidate 하면 login session까지 모두 제거되므로 주의
		mav.addObject("sale", sale);
		mav.addObject("total", total);
		return mav;
	}
	
}
