package kr.bit.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.bit.model.MemberDAO;
import kr.bit.model.MemberVO;

public class MemberUpdateController implements Controller{

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String ctx=request.getContextPath();
		
		int num=Integer.parseInt(request.getParameter("num"));
		int age=Integer.parseInt(request.getParameter("age"));
		String email=request.getParameter("email");
		String phone=request.getParameter("phone");
		
		MemberVO vo=new MemberVO();
		if(request.getParameter("mode").equals("fupdate")) {
			String filename=request.getParameter("filename");
			vo.setFilename(filename);
			System.out.println("vo==="+vo.toString());
		}
		
		vo.setNum(num);
		vo.setAge(age);
		vo.setEmail(email);
		vo.setPhone(phone);
		
		MemberDAO dao=new MemberDAO();
		int cnt=-1;
		if(request.getParameter("mode").contentEquals("fupdate")) {
			System.out.println("11111");
			cnt=dao.memberUpdateFile(vo);
		}else {
			System.out.println("22222");
			cnt=dao.memberUpdate(vo);
		}
		String nextPage=null;
		if(cnt>0) {
		    	// 수정성공		        
			nextPage="redirect:"+ctx+"/memberList.do";
		 }else {
		    	// 가입실패-> 예외객체를 만들어서  WAS에게 던지자.
		    	throw new ServletException("not update");	    	
		 }		
		return nextPage;
	}
}
