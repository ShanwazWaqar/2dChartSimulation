<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
	<title> Analyze Corrosion Image </title>
	<meta http-equiv="Content-Type" content="text/html; charset=unicode"/>
    <link href="css/survey.css" rel="stylesheet"/>
    <link href="css/style.css" rel="stylesheet"/>  
		<link rel="stylesheet" type="text/css" href="css/jqueryslidemenu.css" />

	<!--[if lte IE 7]> 	<![endif]-->
	<style type="text/css">
	html .jqueryslidemenu{height: 4%;} /*Holly Hack for IE7 and below*/
	</style>


<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jqueryslidemenu.js"></script>
	
</head>
 
<script type="text/javascript" language="javascript">
function refreshParentWindow()
{
     //This below line of code will try to reload/refresh the parent window which is your base jsp page.
     //I am not sure the below is the correct line of code to refresh...just try it and if not look for the appropriate line of code. However idea is this one.
  window.location.href="imageanalysis.jsp";
//   window.parent.location.reload();
}

function validate_form()
{
    valid = true;
    if (document.MyForm1.simImage.value == "")
    {
		 alert ( "Please upload the image first" );
        valid = false;
	}
	 return valid;
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
String path = "C:/tmp/";
String serverpath = request.getRealPath("/");

path =path+"images/";
// make change from localhost to IP address here 
String baseurl = "http://192.168.1.248:8080/Simulation/";

%>

<body  bgcolor="white">

<body style="background-color:#67595e;">


 <div id="maincontent">
  <div>  <img class="banner" src="<%=baseurl%>images/UGABanner.jpg"></div>
  <div id="myslidemenu" class="jqueryslidemenu">
		<ul>
		<li><a href="<%=baseurl%>index.jsp">Home</a></li>
		<li><a href="#"> Applications- Simulation </a>
		  <ul>
		  <li><a href="<%=baseurl%>simulate.jsp">Corrosion Single Pit Growth Simulation  (2D) </a></li>
		  <li><a href="<%=baseurl%>sim2D.jsp">Corrosion Multi Pit Growth Simulation (2D) </a></li>
		   <li><a href="<%=baseurl%>materialconstants.jsp"> Material Constants </a></li>
		  </ul> </li>
		 <li><a href="#"> Applications- Analysis </a>
		  <ul>
		  <li><a href="<%=baseurl%>imageanalysis.jsp"> Corrosion Image Analysis </a></li>
	      <li><a href="<%=baseurl%>siaform.jsp"> Neural Network Analysis </a></li>
		  </ul> </li>
		<li><a href="#"> Approach </a>
		</li>
		<li><a href="#">Scope</a></li>
		<li><a href="#"> Publications</a>
		 
		</li>
		<li><a href="#"> Images</a></li>
		</ul>
	 </div>
</br>
 <div align="center" class="content">
  <h1> Analyze Corrosion Image </h1>
  </div>
   <form method=POST  name="MyForm1" action="/Simulation/ImageUpload" enctype="multipart/form-data"  onsubmit="return validate_form();" >
 <div class = "data" align="center" >
			<table>
				<tr>
			     	<td> 
						<b>UploadImage: </b> <INPUT TYPE=FILE  SIZE = 50 NAME=simImage></td>
				 <td>
				 	<div align = "center" >
					<input type="hidden" name="action" value="Submit">
					<input type="hidden" name="path" value ="<%=path%>">
					<input type="hidden" name="serverpath" value ="<%=serverpath%>">
					<input value ="Submit" name ="Submit" type="Submit" class ="btn" >
<!--					<input value ="Reset" type="button" class ="btn" onclick ="refreshParentWindow();" >-->
					<input value ="Reset" type="button" class ="btn" onClick="window.location.href='<%=response.encodeURL("imageanalysis.jsp")%>'"> 
				</div>
	
				 </td>
				</tr>
		
			</table>
		</div>
	
   <div align="center" class="content">
   
    <table>
	 <tr>
	<td>

	 <div class = "data" align="center">
	 <% 
        if(request.getParameter("Submit") != null) {

			 String delPath = path;
	
//				  
			%>
		<table>
		<tr>
		<td width="500" >
		  <h3> Original Image </h3>
		</td>
		<td width="50">

		</td>
		<td width="500">
			  <h3> Wavelet Transformed Image</h3>
		</td>

		</tr>
		<tr>
		<td>

		  <div><img name="uploadedimage" class="banner" src="<%=baseurl%>images/imgupload.JPG"></div>
		<!--<jsp:plugin type="applet" code="LoadImage.class" archive="ImgWaveV1.jar,charts4j-1.3.jar,junit-4.4.jar" jreversion="1.6" width="500" height="500"  >
			<jsp:params>
			 <jsp:param name ="path" value ="<%=path%>"/>
				 </jsp:params>
				 
	
				
		<jsp:fallback>
			Plugin tag OBJECT or EMBED not supported by browser.
		</jsp:fallback> 
		</jsp:plugin> -->

		</td>
		<td width="50">
		</td>
		<td>
		<jsp:plugin type="applet" code="ImageAnalysis.class" archive="ImgWaveV1.jar,charts4j-1.3.jar,junit-4.4.jar" jreversion="1.6" width="500" height="500"  >
			<jsp:params>
			 <jsp:param name ="path" value ="<%=path%>"/>
				 </jsp:params>
				
		<jsp:fallback>
			Plugin tag OBJECT or EMBED not supported by browser.
		</jsp:fallback>
		</jsp:plugin>
	
		</td>
		</tr>

		<tr>
		<tr>
		<td>

				 <a target="_new" href="file:///C:/tmp/images/Features.txt" > Click here to open Features File</a>
		</td>
	
		</tr>

	<td align ="right" >
	 <div align = "right" >
		<b>Run Another Image </b>
				
				
	</td>
	<td>

	</td>
	<td>
	<input type="hidden" name="action" value="Submit">
	<input type="hidden" name="path" value ="<%=path%>">
	<input value ="Yes"  type="button" class ="btn" onclick ="refreshParentWindow();" >
	<input value ="No"   type="button" class ="btn">
	</td>
	</div>
	</tr>
	</table>
	</div>



	
		<%
		 
		}
		else {
						 String delPath = request.getRealPath("/");
	
						 File f1 = new File(path+"imgupload.JPG");
						 File f2 = new File(path+"Original_imgupload.JPG.GIF");
						 File f3 = new File(path+"Segmented_imgupload.JPG.GIF");
						 File f4 = new File(path+"Equalized_imgupload.JPG.GIF");
						 File f5 = new File(path+"MarkedSegments_imgupload.JPG.GIF");
				   	     File f6 = new File(path+"Features_imgupload.JPG.PCA");
				    	 File f7 = new File(path+"Features_imgupload.JPG.WAVE");
						 File f8 = new File(delPath+"/images/imgupload.JPG");

						  if(f1.exists()){
							   boolean success1 = f1.delete();
								boolean success2 = f2.delete();
								boolean success3 = f3.delete();
								boolean success4 = f4.delete();
								boolean success5 = f5.delete();
								boolean success6 = f6.delete();
								boolean success7 = f7.delete();
								boolean success8 = f8.delete();
						   if (!(success1)){
							   System.out.println("Deletion failed.");
						  }else{
						//  System.out.println("Files deleted.");
							}
						  }
		}
	
		%>
			
	</td>


	</tr>
	</table>
	</td>
	</tr>
	
		</table>
	
	</div>
	</div>
		</form>
</body>
</html>
