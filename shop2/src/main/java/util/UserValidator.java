package util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import logic.User;

public class UserValidator implements Validator{
	@Override   // 검증해야할 객체가 User 객체인지 검증
				// 유효성 검증을 위한 객체 자료형 선택
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}
	@Override   // target=User 객체
	public void validate(Object target, Errors errors) {
		User user = (User)target;
		// user : 파라미터값을 저장하고 있는 객체
		String group = "error.required";
		if(user.getUserid() ==  null || user.getUserid().length()==0) {
			errors.rejectValue("userid", group);   // 파라미터별 오류 등록
		}
		if(user.getPassword() ==  null || user.getPassword().length()==0) {
			errors.rejectValue("password", group);
		}
		if(user.getUsername() ==  null || user.getUsername().length()==0) {
			errors.rejectValue("username", group);
		}
		if(user.getPhoneno() ==  null || user.getPhoneno().length()==0) {
			errors.rejectValue("phoneno", group);
		}
		if(user.getPostcode() ==  null || user.getPostcode().length()==0) {
			errors.rejectValue("postcode", group);
		}
		if(user.getAddress() ==  null || user.getAddress().length()==0) {
			errors.rejectValue("address", group);
		}
		if(user.getEmail() ==  null || user.getEmail().length()==0) {
			errors.rejectValue("email", group);
		}
		if(errors.hasErrors()) {   // 오류 발생
			errors.reject("error.input.user");    // regject 글로벌 오류
		}
	}
	
}
