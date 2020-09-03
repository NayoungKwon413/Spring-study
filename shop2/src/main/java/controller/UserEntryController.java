package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;
import util.UserValidator;

public class UserEntryController {
	private ShopService shopService;
	private UserValidator userValidator;
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	public void setUserValidator(UserValidator userValidator) {
		this.userValidator = userValidator;
	}
/*	
	// @RequestMapping(method=RequestMethod.GET)
	// Get 방식으로 들어왔을 때
	@GetMapping
	public String userEntryForm() {    // 보내줄 데이터가 없는 경우(데이터 필요없는 경우) String 리턴함
		return null;   // view 이름. 기본이름으로 지정됨 
					   // userEntry.jsp가 설정됨. -> 요청이 userEntry.shop으로 올 것이기 때문
	}
	// ModelAttribute 는 userEntry.jsp 와 연결
	@ModelAttribute
	public User getUser() {
		return new User();
	}
*/	
	// 하나의 요청을 get 또는 post 방식으로 사용할 수 있음
	@GetMapping   // get 방식 요청. view 단에서 필요한 객체들을 전달하는 역할
	public ModelAndView userEntryForm() {    
		ModelAndView mav = new ModelAndView();
		User u = new User();
//		u.setUsername("홍길동");
		mav.addObject(u);
		return mav;    // WEB-INF/view/userEntry.jsp 로 이동 (spring-mvc.xml에 viewResolver 에서 지정)
	}
	@PostMapping  // post 방식 요청
	//User : 매개변수에 User 타입이 선언된 경우
	//       파라미터값과 User의 setProperty가 동일한 값을 저장(시스템이 알아서 )
	public ModelAndView userEntry(User user,BindingResult bindResult) {
		ModelAndView mav = new ModelAndView();
		userValidator.validate(user, bindResult);
		if(bindResult.hasErrors()) {   // true 일 경우-> 입력에 문제가 있음
			mav.getModel().putAll(bindResult.getModel());    // 원래 화면으로 되돌아감
			return mav;
		}
		// bindResult.hasErrors() 가 false인 경우 -> 입력에 문제 없음. 정상적인 입력
		try {
			shopService.insertUser(user);
			mav.addObject("user", user);           // 입력된 내용 그대로 user로 넣어줌 -> userEntrySuccess에서 사용
		}catch(DataIntegrityViolationException e) {    // 중복키 오류일 경우
			e.printStackTrace();
			bindResult.reject("error.duplicate.user");   // 중복된 아이디 입니다 메세지와 함께 원래 화면으로 되돌아옴
			mav.getModel().putAll(bindResult.getModel());
			return mav;
		}
		mav.setViewName("userEntrySuccess");    // 등록 성공인 경우-> view 이름 설정(view 이름 설정안했을 경우에는 무조건 userEntry.jsp)
		return mav;
	}
	// 파라미터 값을 형변환해주는 기능
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, false));  
		// true : 입력값이 필수가 아님.
		// false : 입력값은 필수
	}
}
