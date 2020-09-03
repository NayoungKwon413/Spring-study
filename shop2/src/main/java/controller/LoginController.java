package controller;

import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;


public class LoginController {
	private ShopService shopService;
	private Validator loginValidator;
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	public void setLoginValidator(Validator validator) {
		this.loginValidator = validator;
	}
	@GetMapping
	public String loginForm(Model model) {  // Model : view 에 전달할 데이터 객체
		model.addAttribute(new User());
//		return null;     null로 해도 url에 따라 login.jsp로 이동함
		return "login";  // view 이름
	}
	@PostMapping
	public ModelAndView login(User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		loginValidator.validate(user, bresult);
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		/*
		 * 1. db에서 userid에 해당하는 고객정보를 읽어 User객체에 저장
		 * 2. 입력된 비밀번호와 db의 비밀번호 비교해 일치하는 경우
		 *    session.setAttribute("loginUser", dbuser) 실행
		 * 3. 입력된 비밀번호와 db의 비밀번호 비교해 불일치하는 경우
		 *    유효성 검증으로 "비밀번호를 확인하세요" 메세지를 login.jsp 로 전송
		 * 4. 로그인이 정상적인 경우 loginsuccess.jsp 페이지로 이동
		 */
		
		try {
			//1.
			User dbuser = shopService.getUser(user.getUserid());
			//2.
			// user.getPassword() : 입력된 비밀번호
			// dbuser.getPassword() : db에 저장된 비밀번호
			if(user.getPassword().equals(dbuser.getPassword())) {
				session.setAttribute("loginUser", dbuser);
				mav.setViewName("loginsuccess");
				return mav;
			}else {
				//3.
				bresult.reject("error.login.password");
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}

		}catch(EmptyResultDataAccessException e) {   // spring jdbc에서만 가능
			// dbuser의 정보가 없는 경우 -> mybatis에서는 null 리턴했으나, spring에서는 오류 발생
			bresult.reject("error.login.id");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		
		
	}
}
