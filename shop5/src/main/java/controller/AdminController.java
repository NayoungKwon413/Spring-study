package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Mail;
import logic.ShopService;
import logic.User;
import util.CipherUtil;

/*
 * AdminController 클래스는 모든 메서드는 admin 으로 로그인 한 경우만 처리 가능
 * 1. 로그인 안 된 경우 : 로그인 후 가능합니다. login.shop 으로 이동
 * 2. 관리자 로그인이 아닌 경우 : 관리자만 가능한 거래입니다. main.shop 페이지 이동
 * => AdminAspect 클래스 구현하기
 */

@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list")
	public ModelAndView listview(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<User> list = service.getUserList();
		for(User user : list) {
			try {
				String userid = CipherUtil.makehash(user.getUserid());
				String email = CipherUtil.decrypt(user.getEmail(), userid.substring(0,16));   // userid를 해시값으로 바꾼후 이 중 16자리를 키값으로 삼아 user.getEmail() 복호화
				user.setEmail(email);
			}catch(Exception e) {
				e.printStackTrace();
			}	
		}
		mav.addObject("list", list);
		return mav;
	}
	
	@RequestMapping("mailForm")
	public ModelAndView mailform(String[] idchks, HttpSession session) {
		ModelAndView mav = new ModelAndView("admin/mail");
		if(idchks == null || idchks.length == 0) {
			throw new LoginException("메일을 보낼 대상자를 선택하세요.", "list.shop");
		}
		//list : 선택된 userid에 해당하는 User 객체들이 저장 
		List<User> list = service.userList(idchks);
		for(User user : list) {
			try {
				String userid = CipherUtil.makehash(user.getUserid());
				String email = CipherUtil.decrypt(user.getEmail(), userid.substring(0,16));   // userid를 해시값으로 바꾼후 이 중 16자리를 키값으로 삼아 user.getEmail() 복호화
				user.setEmail(email);
			}catch(Exception e) {
				e.printStackTrace();
			}	
		}
		mav.addObject("list", list);
		return mav;
	}
	
	@RequestMapping("mail")
	public ModelAndView mail(Mail mail, HttpSession session) {
		ModelAndView mav = new ModelAndView("/alert");
		mailSend(mail);
		mav.addObject("msg", "메일 전송이 완료되었습니다.");
		mav.addObject("url", "list.shop");
		return mav;
	}
	
	private void mailSend(Mail mail) {
		//네이버 메일 전송을 위한 인증 객체
		MyAuthenticator auth = new MyAuthenticator(mail.getNaverid(), mail.getNaverpw());
		//메일 전송을 위한 환경 변수 설정
		Properties prop = new Properties();   
		try {
			FileInputStream fis = new FileInputStream("D:/20200703back/20200522back/20200522back/spring/workspace/shop3/src/main/resources/mail.properties");
			prop.load(fis);   //mail.properties의 내용을 Properties(Map)객체로 로드
			prop.put("mail.smtp.user", mail.getNaverid());
		}catch(IOException e) {
			e.printStackTrace();
		}
		//메일 전송을 위한 객체
		Session session = Session.getInstance(prop, auth);
		//메일의 내용을 저장하기 위한 객체
		MimeMessage mimeMsg = new MimeMessage(session);
		try {
			//보내는 사람 설정
			mimeMsg.setFrom(new InternetAddress(mail.getNaverid() + "@naver.com"));
			List<InternetAddress> addrs = new ArrayList<InternetAddress>();
			//홍길동 <hong@aaa.bb>,김삿갓 <kim@aaa.bb>,...
			String[] emails = mail.getRecipient().split(",");
			for(String email : emails) {
				try {
/*
 * new String(email.getBytes("utf-8"), "8859_1")
 *   email.getBytes("utf-8") : email 문자열을 byte[] 형태로 변경. utf-8 문자로 인식.
 *   8859_1 : byte[] 배열을 8859_1로 변경하여 문자열로 생성
 * => 수신된 메일에서 한글 이름 유지되도록 설정
 */
					addrs.add(new InternetAddress(new String(email.getBytes("UTF-8"), "8859_1")));    // 해당 코드 없으면 한글 깨짐 현상 나타남
				}catch(UnsupportedEncodingException ue) {
					ue.printStackTrace();
				}
			}
			InternetAddress[] arr = new InternetAddress[emails.length];
			for(int i=0; i<addrs.size(); i++) {
				arr[i] = addrs.get(i);
			}
			//보낸 일자
			mimeMsg.setSentDate(new Date());
			//받는 사람
			mimeMsg.setRecipients(Message.RecipientType.TO, arr);
			//이메일 제목
			mimeMsg.setSubject(mail.getTitle());
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart message = new MimeBodyPart();
			//이메일 내용
			message.setContent(mail.getContents(), mail.getMtype());
			multipart.addBodyPart(message);
			//첨부파일 부분
			for(MultipartFile mf : mail.getFile1()) {
				if((mf != null) && (!mf.isEmpty())) {
					multipart.addBodyPart(bodyPart(mf));
				}
			}
			mimeMsg.setContent(multipart);
			Transport.send(mimeMsg);   //mimeMsg : 메일 내용 -> 전송
		}catch(MessagingException me) {
			me.printStackTrace();
		}
	}

	private BodyPart bodyPart(MultipartFile mf) {
		MimeBodyPart body = new MimeBodyPart();
		//orgFile : 업로드 파일의 이름
		String orgFile = mf.getOriginalFilename();
		//path : 업로드 되는 위치
		String path = "d:/20200224/spring/mailupload/";
		File f = new File(path);
		if(!f.exists())  f.mkdirs();
		File f1 = new File(path + orgFile);   // 업로드된 내용을 저장하는 파일 
		try {
			mf.transferTo(f1);     //업로드 완성
			body.attachFile(f1);   //메일에 첨부
			body.setFileName(new String(orgFile.getBytes("UTF-8"), "8859_1"));   //첨부파일이름설정 -> 한글 이름일 때도 깨지지 않음
		}catch(Exception e) {
			e.printStackTrace();
		}
		return body;
	}

	private final class MyAuthenticator extends Authenticator {
		private String id;
		private String pw;
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id, pw);
		}
	}
	
	@RequestMapping("graph*")
	public ModelAndView graph(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = service.graph1();
		mav.addObject("map", map);
		return mav;
	}
}
