package kr.bit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileGetController implements Controller{

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("asdsa2222d");
		String filename=request.getParameter("filename");
		String UPLOAD_DIR="file_repo";
		String uploadPath=request.getServletContext().getRealPath("")+File.separator+UPLOAD_DIR; //물리적인 주소
		File f=new File(uploadPath+"\\"+filename);
		System.out.println("asdsad");
		//클라이언트로부터 넘어오는 파일이름에 한글이 있는 경우 깨지지 않게하기 위해
		filename=URLEncoder.encode(filename,"UTF-8");
		filename=filename.replace("+", "");
		
		//서버에서 클라리언트에게 다운로드 준비를 시키는 부분(다운로드 창을 띄움). 밑의 소스들은 규칙이라고 보면 됨
		response.setContentLength((int)f.length()); //파일 크기를 가져와 클라이언트에게 알려주겠다는것
		response.setContentType("application/x-msdownload;charset=utf-8");	
		response.setHeader("Content-Disposition", "attachment;filename="+filename+";"); 
		response.setHeader("Content-Transfer-Encoding","binary");
		response.setHeader("Pragma","no-cache");
		response.setHeader("Expires","0");
		
		//실제 다운로드를 하는 부분
		FileInputStream in=new FileInputStream(f); //다운로드할 파일을 읽기 준비
		OutputStream out=response.getOutputStream();  //출력 스트림 만들기
		byte[] buffer=new byte[1024];
		while(true) {
			int count=in.read(buffer);  //다운로드 할 파일을 1024크기씩 읽어들임
			if(count==-1) {
				break;
			}
			out.write(buffer,0,count);  //버퍼에 있는 데이터를 0퍼센트에서 100퍼센트까지 읽어들임
		}
		in.close();
		out.close();
		
		return null;
	}
	
}
