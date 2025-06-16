package com.sim; 


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

@WebServlet(urlPatterns="/SiaServlet" , name="SiaServlet")
@MultipartConfig(location="c:\\tmp", fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)

public class SiaServlet extends HttpServlet {
	
	@Override
    public void doPost(HttpServletRequest request, 
         HttpServletResponse response)
                  throws IOException, ServletException {
		
	response.setContentType("text/html");
    response.getWriter().println("Hello from FileUploadServlet");
    
	String action = request.getParameter("action");
	String serverpath = request.getParameter("serverpath");
    if (action.equals("Submit")) {
      System.out.println("Action:"+action);
	  String[] args;
	  args = new String[15];  
	  args[0] = request.getParameter("inNodes") ; //NoinputNodes
	  args[1] = request.getParameter("outNodes") ; //NoOutputNodes
	  args[2] = request.getParameter("noLayers") ; //NoOfLayers
	  args[3] = request.getParameter("noFirstLayer") ; //NoFirstHiddenLayer
	  args[4] = request.getParameter("noSecLayer") ; //NoSecondHiddenLayer
	  args[5] = request.getParameter("notraindata") ; //NoTrainData
	  args[6] = request.getParameter("notestdata") ; //NoTestData
	  args[7] = request.getParameter("radionet") ; //choice_nnet
	  args[8] = request.getParameter("serverpath") ; //choice_nnet
	  
	  
	 /* Collection fileParts = request.getParts();
	  Iterator filePartIt = fileParts.iterator();
	  while(filePartIt.hasNext()){
	      Part filePart = (Part) filePartIt.next();
	      System.out.println("File Name " + filePart.getName());
	      System.out.println("File Size " + filePart.getSize());
	      
	      System.out.println("File Content ");
	      BufferedReader fileReader =
	        new BufferedReader(new InputStreamReader(filePart.getInputStream()));
	      String line = null;
	      while(( line = fileReader.readLine()) != null){
	        System.out.println(line);
	      }
	    }*/
	  System.out.println("arg 7:"+args[7]);
	  if(args[7].equals("train")){
	  request.getPart("trainfile").write(serverpath+"/upload/traindata.txt");
	  }else if(args[7].equals("test")){
	  request.getPart("testfile").write(serverpath+"/upload/testdata.txt");
	  }
	 // request.getPart("trainfile").write("C:\\xampp\\Tomcat 7.0\\webapps\\isdas\\upload\\traindata.txt");
	  System.out.println("Exiting HelloFileUploadAnnotationServlet.doPost()");
	  
	  try{
		nnet nnet = new nnet();
		com.sim.nnet.main(args);
		
	  } catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("Catch");
		e.printStackTrace();
	  }
	  RequestDispatcher view = request.getRequestDispatcher("/siaresults.jsp") ;
	  view.forward(request,response) ;
	  
	  
	 /* String url="/jsp/siaresults.jsp";
      ServletContext sc = getServletContext();
      RequestDispatcher rd = sc.getRequestDispatcher(url);
      rd.forward(request,response);*/
    }
    
    }
}