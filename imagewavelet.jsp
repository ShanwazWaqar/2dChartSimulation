<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
	<title> Analyze Corrosion Image </title>
	<meta http-equiv="Content-Type" content="text/html; charset=unicode"/>
    <link href="css/survey.css" rel="stylesheet"/>
    <link href="css/style.css" rel="stylesheet"/>  
	
</head>
 
<script type="text/javascript" language="javascript">
function refreshParentWindow()
{
     //This below line of code will try to reload/refresh the parent window which is your base jsp page.
     //I am not sure the below is the correct line of code to refresh...just try it and if not look for the appropriate line of code. However idea is this one.
  window.location.href="imagewavelet.jsp";
     //window.parent.location.reload();
}

function resetForm() {
alert('My "Reset" called.');
document.MyForm1.reset(); 
request.getParameter("noRows") = null;
}

</script>
<%@ page language = "java" import="java.io.*"%>
<%@ page language = "java" import="java.util.*"%>
<%
String path = request.getRealPath("/");
path =path+"images/";

 
%>

<body  bgcolor="white">

 <div id ="wrapper">
 </br>
 
   <div align="center" class="content">
    <h1> Analyze Corrosion Image </h1>
    <table>
	 <tr>
	  <td>

		   <form method=POST  name="MyForm1" action="/Simulation/imagewavelet.jsp" >
		  <div class = "data" align="center" >
			<table>
			
			</table>
		</div>
		</br>
		<div align = "center" >
				<input type="hidden" name="action" value="Submit">
					<input type="hidden" name="path" value ="<%=path%>">
					<input value ="Submit" name ="Submit" type="Submit" class ="btn" >
					<input value ="Reset" type="button" class ="btn" onclick ="refreshParentWindow();" >
				</div>
		</form>
		
	</td>
	<td width ="50">
	</td>
	<td>

	 <div class = "data" align="center">
		<%-- Create the bean only when the form is posted --%>
	  
     <% 

	
            if(request.getParameter("Submit") != null) {
				  if(request.getParameter("Submit").equals("Submit")) {
					 
		  String delPath = "C:/tmp/images/";
		  File f1 = new File(delPath+"MarkedSegments_imgupload1.JPG.gif");
		  if(f1.exists()){
    	  boolean success1 = f1.delete();
  		  }
   		%>

		
		<table>
		<tr>
		<td>
		<jsp:plugin type="applet" code="ImageAnalysis.class" archive="ImgWaveV1.jar,charts4j-1.3.jar,junit-4.4.jar" jreversion="1.6"  width="400" height="400" >
		<jsp:params>
		 <jsp:param name ="path" value ="<%=path%>"/>
		</jsp:params>
	

		<jsp:fallback>
			Plugin tag OBJECT or EMBED not supported by browser.
		</jsp:fallback>
		</jsp:plugin>
	
		</td>
		</tr>
	</table>
	</div>

	 
		<%
		 } }
		else {
			 String delPath = request.getRealPath("/");
			 
						 File f1 = new File(delPath+"images/imgupload.JPG");
						  if(f1.exists()){
							   boolean success1 = f1.delete();
						  
						   if (!(success1)){
							   System.out.println("Deletion failed.");
						  }else{
						  System.out.println("Files deleted.");
							}
						  }
		}
	
		%>
	</td>
	</tr>
	</table>
	</td>
	</tr>
	<tr>
	<td>

	
	</div>
	</div>
</body>
</html>
