package kr.bit.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileAddController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			
		String UPLOAD_DIR="file_repo"; //디렉토리 이름
		String uploadPath=request.getServletContext().getRealPath("")+File.separator+UPLOAD_DIR; //경로를 얻음. File.separator=운영체제에 맞는 구분자 사용
		File currentDirPath=new File(uploadPath);  //업로드 할 경로를 File 객체로 만들기
		if(!currentDirPath.exists()) {  
			currentDirPath.mkdir();	//경로가 존재하는지 확인 후 없으면 만들어주기
		}
		//파일을 업로드 할 때 먼저 저장될 임시 저장경로를 설정. 보통 임시 저장경로에 저장->실제 저장경로에 저장 이렇게 한다?
		//파일 업로드시 필요한 API - commons-fileupload, commons-io  jar파일 다운
		DiskFileItemFactory factory=new DiskFileItemFactory(); //그 디렉토리에 임시로 저장할 메모리 공간 만듦
		factory.setRepository(currentDirPath);	//저장위치 세팅
		factory.setSizeThreshold(1024*1024); 	//용량 설정
		
		String fileName=null;
		
		ServletFileUpload upload=new ServletFileUpload(factory);	//request에서 파일을 꺼내기 위해 도와줌
		try {
			//items -> FileItem[], FileItem[], FileItem[]
			List<FileItem> items=upload.parseRequest(request);	//request 안에 여러개의 파일이 업로드 된 경우 parseRequest()로 인해 list로 담김
			for(int i=0; i<items.size(); i++) {
				FileItem fileItem=items.get(i);
				if(fileItem.isFormField()) {	//폼필드이면(회원가입 페이지에서 작성한 id,비밀번호,나이 등이 담긴) //여기서는 ajax로 파일만 보내기때문에 필요없는 코드지만 나중에 실습시 폼과 파일을 같이 보낼때를 대비해서 연습
					System.out.println(fileItem.getFieldName()+"="+fileItem.getString("utf-8"));
				}else{	//파일이면
					if(fileItem.getSize()>0) {
						int idx=fileItem.getName().lastIndexOf("\\");  //  \\(window), /(Linux)
						if(idx==-1) {
							idx=fileItem.getName().lastIndexOf("/");
						}
						fileName=fileItem.getName().substring(idx+1); 
						//파일위치는 경로+파일명이며 경로의 제일 마지막 구분자위치+1로 파일명만 추출   예)C:\eGovFrame-3.10.0\maven\파일명  
						
						File uploadFile=new File(currentDirPath+"\\"+fileName);	//임시 디렉토리가 아닌 실제 디렉토리를 가진 파일 객체 생성
						//파일 중복체크
						if(uploadFile.exists()) {
							fileName=System.currentTimeMillis()+"_"+fileName; //파일 중복시 시분초를 파일네임 앞에 붙여서 중복안나게 바꿔줌
							uploadFile=new File(currentDirPath+"\\"+fileName); //바뀐 이름으로 파일을 다시 생성
						}
						fileItem.write(uploadFile);  //임시 경로에서 새로운 경로로 파일을 쓰기.  현재 이 페이지에선 임시 경로와 새로운 경로가 같음
						//경로 C:\eGovFrame-3.10.0\workspace.edu\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\MVC06\file_repo
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		//ajax쪽으로 업로드된 최종파일이름을 전송시키기
		response.setContentType("text/html;charset=euc-kr");
		response.getWriter().print(fileName);
		
		return null;
	}

}
