package controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardException;
import logic.Board;
import logic.ShopService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public ModelAndView getBoard(Integer num, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		Board board = null;
		if(num == null) {
			board = new Board();
		}else {
			boolean readcntable = false;
			if(request.getRequestURI().contains("detail.shop"))
				readcntable = true;
			// 답변 클릭시 get방식으로 들어와 해당 게시글 num에 해당하는 정보를 가진채 reply.jsp로 이동
			// board : 파라미터 num에 해당하는 게시물 정보 저장
			board = service.getBoard(num, readcntable);
		}
		mav.addObject("board", board);
		return mav;
	}
	
	@PostMapping("write")
	public ModelAndView write(@Valid Board board, BindingResult br, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(br.hasErrors()) {
			mav.getModel().putAll(br.getModel());
			return mav;
		}
		try {
			service.bordWrite(board, request);
			mav.setViewName("redirect:list.shop");
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("게시물 등록에 실패했습니다.", "write.shop");
		}
		return mav;
	}
	/*
	 * pageNum : 현재 페이지 번호
	 * maxpage : 최대 페이지
	 * startpage : 보여지는 시작 페이지 번호
	 * endpage : 보여지는 끝 페이지 번호
	 * listcount : 전체 등록된 게시물 건수
	 * boardlist : 화면에 보여질 게시물 객체들
	 * boardno : 화면에 보여지는 게시물 번호
	 */
	@RequestMapping("list")
	public ModelAndView list(Integer pageNum, String searchtype, String searchcontent) {    // pageNum 파라미터가 있을수도, 없을수도 있음 -> 없는 경우 null로 전달.
												   // int 일 경우, null을 전달 할 수 없음 -> 오류 발생. 파라미터가 반드시 들어오는 경우라면 int로 받아도 문제 없음.
		ModelAndView mav = new ModelAndView();
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(searchtype == null || searchtype.trim().equals("")) {    // column 값 선택 안함
			searchtype = null;
			searchcontent = null;
		}
		if(searchcontent == null || searchcontent.trim().equals("")) {   // search 입력값 없는 경우
			searchcontent = null;
			searchtype = null;
		}
		int limit = 10;  //한페이지에 보여질 게시물의 건수
		int listcount = service.boardCount(searchtype, searchcontent);  //등록 게시물 건수
		List<Board> boardlist = service.getBoardList(pageNum, limit, searchtype, searchcontent);
		int maxpage = (int)((double)listcount/limit +0.95);
		int startpage = ((int)(pageNum/10.0 + 0.9)-1)*10+1;
		int endpage = startpage + 9;
		if(endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (pageNum -1) * limit;
		mav.addObject("pageNum", pageNum);
		mav.addObject("maxpage", maxpage);
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage);
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno);
		mav.addObject("today", new SimpleDateFormat("yyyyMMdd").format(new Date()));
		return mav;
	}
	
	@RequestMapping("imgupload")
	// upload : CKEditor 에서 전달해주는 파일 정보의 이름
	//			<input type="file" name="upload"> 의미
	// CKEditorFuncNum : ckeditor 에서 내부에서 사용되는 파라미터
	public String imgupload(MultipartFile upload, String CKEditorFuncNum, HttpServletRequest request, Model model) {
		String path = request.getServletContext().getRealPath("/") + "board/imgfile/";   //이미지를 저장할 폴더를 지정
		File f = new File(path);
		if(!f.exists())  f.mkdirs();
		if(!upload.isEmpty()) {   //업로드된 이미지 정보가 존재함
			File file = new File(path, upload.getOriginalFilename()); //업로드될 파일을 저장할 File 객체 지정
			try {
				upload.transferTo(file);   //업로드 파일 생성
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		String fileName = request.getServletContext().getContextPath() + "/board/imgfile/" + upload.getOriginalFilename();
		model.addAttribute("fileName", fileName);
		model.addAttribute("CKEditorFuncNum", CKEditorFuncNum);
		return "ckedit";
	}
	
	/*
	 * 1. 파라미터값 Board 객체 저장 + 유효성 검증
	 * 2. 답변글 데이터 추가
	 *   - grp 에 해당하는 레코드 grpstep 값보다 큰 grpstep의 값을 grpstep+1 로 수정
	 *   - maxnum+1 값으로 num 저장
	 *   - grplevel+1 값으로 grplevel 값 저장
	 *   - grpstep+1 값으로 grpstep 값 저장
	 *   - 파라미터값으로 board 테이블에 insert
	 * 3. list.shop 페이지 요청
	 */
	@PostMapping("reply")
	public ModelAndView reply(@Valid Board board, BindingResult br) {
		ModelAndView mav = new ModelAndView();
		if(br.hasErrors()) {  //답변글등록 누를때마다 RE:가 추가되는걸 방지하기 위함
			Board dbBoard = service.getBoard(board.getNum(), false);
			Map<String, Object> map = br.getModel();
			Board b = (Board)map.get("board");   
			b.setSubject(dbBoard.getSubject());
			return mav;
		}
		try {
			//board : 화면에 전달받은 파라미터 정보 저장
			/*
			 * num, grp, grplevel, grpstep : 원글에 대한 정보
			 * name, pass, subject, content : 답글에 저장될 정보
			 */
			service.bordReply(board);   //입력한정보 4 + 기존게시글에 해당하는 정보 4
			mav.setViewName("redirect:list.shop");
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("답글 등록에 실패했습니다.", "reply.shop?num="+board.getNum());
		}
		return mav;
	}
	
	/*
	 * 1. 파라미터 값 Board 객체 저장. 유효성 검증
	 * 2. 입력된 비밀번호와 db의 비밀번호를 비교-> 비밀번호가 맞는 경우 3번으로
	 * 	   틀린 경우 '비밀번호가 틀립니다' 출력 후 update.shop Get방식으로 호출
	 * 3. 수정정보를 db에 변경
	 *    - 첨부파일 변경 : 첨부파일 업로드, fileurl 정보 수정
	 * 4. detail.shop 페이지 요청
	 */
	@PostMapping("update")
	public ModelAndView update(@Valid Board board, BindingResult br, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		Board dbBoard = service.getBoard(board.getNum(), false);
		if(br.hasErrors()) {
			mav.getModel().putAll(br.getModel());
			return mav;
		}
		if(!board.getPass().equals(dbBoard.getPass())) {
			throw new BoardException("비밀번호가 틀립니다.", "update.shop?num="+board.getNum());
		}
		try {
			service.updateBoard(board, request);   // 파일을 내 어플리케이션 폴더에 저장해야하기 때문에 request 객체 필요함
			mav.setViewName("redirect:detail.shop?num="+board.getNum());
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("게시글 수정에 실패했습니다.", "update.shop?num="+board.getNum());
		}
		return mav;
	}
	/*
	 * 1. num, pass 파라미터 저장
	 * 2. db의 비밀번호와 입력된 비밀번호가 틀리면 error.login.password 코드입력 -> 유효성 검증 내용 출력하기
	 * 3. db에서 해당 게시물 삭제
	 *   - 삭제 실패 : 게시글 삭제 실패. delete.shop 페이지로 이동
	 *   - 삭제 성공 : list.shop 페이지 이동
	 */
	@PostMapping("delete")
	public ModelAndView delete(Board board, BindingResult br) {
		ModelAndView mav = new ModelAndView();
		try {
			Board dbBoard = service.getBoard(board.getNum(), false);
			if(!board.getPass().equals(dbBoard.getPass())) {
				br.reject("error.login.password");
				return mav;
			}
			service.deleteBoard(board);
			mav.setViewName("redirect:list.shop");
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("게시글 삭제를 실패했습니다.", "delete.shop?num="+board.getNum());
		}
		return mav;
	}
}
