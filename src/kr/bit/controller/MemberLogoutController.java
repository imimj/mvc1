package kr.bit.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberLogoutController implements Controller{

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String ctx=request.getContextPath(); // /MVC06
		
		//세션을 가져와서 세션 제거
		request.getSession().invalidate();
	
		return "redirect:"+ctx+"/memberList.do";
	}
	
	
}
