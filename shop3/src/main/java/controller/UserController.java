package controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Item;
import logic.Sale;
import logic.SaleItem;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private ShopService service;

	@RequestMapping("userEntry")
	public ModelAndView userentry(@Valid User user, BindingResult br) {
		ModelAndView mav = new ModelAndView();
		if(br.hasErrors()) {
			br.reject("error.input.user");
			mav.getModel().putAll(br.getModel());
			return mav;
		}
		try {
			service.userInsert(user);
			mav.setViewName("redirect:login.shop");
		}catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			br.reject("error.duplicate.user");
			mav.getModel().putAll(br.getModel());
		}
		return mav;
	}
	
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult br, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(br.hasErrors()) {
			br.reject("error.input.user");
			return mav;
		}
		//1. DB 정보의 id, password 비교
		//2. 일치하는 경우 : session에 loginUser 정보 저장
		//3. 불일치하는 경우 : 확인 메세지 출력 후 다시 로그인화면으로
		//4. DB에 해당 id 정보가 없는 경우 : 메세지 출력
		try {
			User dbUser = service.getUser(user.getUserid());
			if(user.getPassword().equals(dbUser.getPassword())) {
				session.setAttribute("loginUser", dbUser);
				mav.setViewName("redirect:main.shop");
			}else {
				br.reject("error.login.password");
			}
		}catch(EmptyResultDataAccessException e) {
			e.printStackTrace();
			br.reject("error.login.id");
		}
			return mav;
	}
	
	@RequestMapping("logout")
	public String loginChecklogout(HttpSession session) {
		session.invalidate();
		return "redirect:login.shop";
	}

	@RequestMapping("main")  // login이 되어야 실행 가능. 메서드 이름을 login~~~ 으로 지정
	public String loginCheckmain(HttpSession session) {
		return null;
	}

	/*
	 * AOP 설정하기
	 * 1. UserController의 check로 시작하는 메서드에 매개변수가 id, session 인 경우
	 *   - 로그인 안된 경우 : 로그인 하세요 => login.shop 페이지 이동
	 *   - admin이 아니면서, 다른 아이디 정보 조회 시 : 본인 정보만 조회 가능합니다.  -> main.shop 이동
	 */
	@RequestMapping("mypage")
	public ModelAndView checkmypage(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		//sale 테이블에서 saleid, userid, saledate 컬럼값만 저장된 Sale 객체의 List 로 리턴
		List<Sale> salelist = service.saleList(id);
		for(Sale s : salelist) {
			// saleitemlist : saleid, seq, itemid, quantity + itemid에 해당하는 Item 객체(주문번호에 해당하는 상품목록)
			List<SaleItem> saleitemlist = service.saleItemList(s.getSaleid());
			for(SaleItem si : saleitemlist) {
				Item item = service.getItem(Integer.parseInt(si.getItemid()));
				si.setItem(item);
			}
			s.setItemList(saleitemlist);   // Sale 객체에 상품목록 저장
		}
		mav.addObject("user", user);
		mav.addObject("salelist", salelist);
		return mav;
	}
	
	@GetMapping(value= {"update", "delete"})
	public ModelAndView checkview(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		mav.addObject("user",user);
		return mav;
	}
	/*
	 * 1. 유효성검증
	 * 2. 비밀번호 검증 : 불일치할 경우 -> 유효성 출력으로 error.login.password 코드로 실행
	 * 3. 비밀번호 검증 : 일치할 경우 -> update 실행 + 로그인 정보 수정
	 * 				(단, admin이 회원들의 정보르 수정할 때는 로그인 정보 수정하면 안됨! 본인이 본인 정보를 수정할 때만 로그인정보 수정해야함)
	 * 4. 수정완료 -> mypage.shop으로 이동
	 */
	
	@PostMapping("update")
	public ModelAndView checkupdate(@Valid User user, BindingResult br, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// 1. 유효성 검증
		if(br.hasErrors()) {
			br.reject("error.input.user");
			mav.getModel().putAll(br.getModel());
			return mav;
		}
		// 2. 비밀번호 검증 : 불일치할 경우
		User loginUser = (User)session.getAttribute("loginUser");
		// 로그인한 정보의 비밀번화와 입력된 비밀번호 검증
		if(!user.getPassword().equals(loginUser.getPassword())) {
			br.reject("error.login.password");
			return mav;
		}
		// 3. 비밀번호 일치할 경우
		try {
			service.userUpdate(user);
			mav.setViewName("redirect:mypage.shop?id="+user.getUserid());
			// 로그인 정보 수정
			if(loginUser.getUserid().equals(user.getUserid())) {
				session.setAttribute("loginUser", user);
			}
		}catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			br.reject("error.user.update");
		}
		return mav;
	}
	
	/*
	 * 회원 탈퇴
	 * 1. 비밀번호 검증 : 불일치 -> 비밀번호 오류 메세지 출력 + delete.shop으로 이동
	 * 2. 비밀번호 검증 : 일치 -> delete 실행 + 본인인 경우, 로그아웃 후 login.shop 으로 이동
	 * 									관리자인 경우, main.shop으로 이동
	 */
	@PostMapping("delete")
	public ModelAndView checkdelete(User user, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User loginUser = (User)session.getAttribute("loginUser");
		
		// 유효성 검증
		if(user.getUserid().equals("admin")) {
			throw new LoginException("관리자 탈퇴는 불가능합니다.", "main.shop");
		}
		// 관리자 로그인 : 관리자 비밀번호 검증
		// 사용자 로그인 : 본인 비밀번호로 검증
		if(!user.getPassword().equals(loginUser.getPassword())) {
			throw new LoginException("비밀번호가 틀립니다.", "../user/delete.shop?id="+user.getUserid());
		}
		try {
			service.userDelete(user);
//			if(user.getUserid().equals(loginUser.getUserid())) {
//				session.invalidate();
//				mav.setViewName("redirect:login.shop");
//			}else if(loginUser.getUserid().equals("admin")) {
//				mav.setViewName("redirect:main.shop");
//			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new LoginException("탈퇴 시 오류 발생. 전산부 연락 요망", "../user/delete.shop?id="+user.getUserid());
		}
		// 탈퇴 이후 -> 일반사용자 탈퇴 시 로그아웃 후 login.shop으로 이동
		if(user.getUserid().equals(loginUser.getUserid())) {
			session.invalidate();
			throw new LoginException(user.getUserid()+"회원님, 탈퇴완료되었습니다.", "login.shop");
		}else if(loginUser.getUserid().equals("admin")) {   // 탈퇴 이후 -> 관리자가 회원 강제 탈퇴 시, main.shop으로 이동
			mav.setViewName("redirect:main.shop");
		}
		return mav;
	}
	
	@GetMapping("*")
	public String form(Model model) {
		model.addAttribute(new User());
		return null;
	}
	
}
