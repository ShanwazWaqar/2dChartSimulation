


import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(urlPatterns="/ImageUpload" , name="ImageUpload")
@MultipartConfig(location= "C:\\tmp", fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)


public class ImageUpload extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, 
			HttpServletResponse response)
	throws IOException, ServletException {
		 File file = new File("C:/tmp/images/imgupload.JPG");
	     if (file.exists()) {
	          file.delete();
	     }
		response.setContentType("text/html");
		response.getWriter().println("Hello from FileUploadServlet");

		String action = request.getParameter("action");
		String path = request.getParameter("path");
		String serverpath = request.getParameter("serverpath");
		
		System.out.println("serverpath:"+serverpath);
		if (action.equals("Submit")) {
			request.getPart("simImage").write(path+"\\imgupload.JPG");
			request.getPart("simImage").write(serverpath+"/images/imgupload.JPG");
			//  System.out.println("Path in servlet:"+path+"\\imgupload.JPG");



			RequestDispatcher view = request.getRequestDispatcher("/imageanalysis.jsp") ;
			view.forward(request,response) ;


		}

	}
}