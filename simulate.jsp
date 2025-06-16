<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
	<title> 2D Simulation  </title>
	<meta http-equiv="Content-Type" content="text/html; charset=unicode"/>
    <link href="css/survey.css" rel="stylesheet" type="text/css"/>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>  
		<link rel="stylesheet" type="text/css" href="css/jqueryslidemenu.css" />

	<!--[if lte IE 7]>	<![endif]-->
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
  window.location.href="simulate.jsp";
     //window.parent.location.reload();
}
function setdefaults(){
	//alert("In sertfeault");
	if (document.MyForm1.noRows.value == ""){
		document.MyForm1.noRows.value ="100";
	}
	if (document.MyForm1.noColumns.value == ""){
		document.MyForm1.noColumns.value ="100";
	}
	if (document.MyForm1.param1.value == ""){
		document.MyForm1.param1.value ="3";
	}
	if (document.MyForm1.param2.value == ""){
		document.MyForm1.param2.value ="290";
	}
	if (document.MyForm1.param3.value == ""){
		document.MyForm1.param3.value ="0.6";
	}
	if (document.MyForm1.param4.value == ""){
		document.MyForm1.param4.value ="0.2";
	}
	if (document.MyForm1.constant.value == ""){
		document.MyForm1.constant.value ="1";
	}
	if (document.MyForm1.stressfact.value == ""){
		document.MyForm1.stressfact.value ="0";
	}
	if (document.MyForm1.noIterations.value == ""){
		document.MyForm1.noIterations.value ="30";
	}
}


 function validate_form()
{
	
    valid = true;
	

    if (document.MyForm1.noRows.value == "")
    {
        alert ( "Please enter No of rows" );
        valid = false;
    }
	else if (document.MyForm1.noColumns.value == "")
    {
        alert ( "Please enter No of columns" );
        valid = false;
    }
	else if (document.MyForm1.param1.value == "")
    {
        alert ( "Please enter PH Value" );
        valid = false;
    }
	else if (document.MyForm1.param2.value == "")
    {
        alert ( "Please enter temperature" );
        valid = false;
    }
	else if (document.MyForm1.param3.value == "")
    {
        alert ( "Please enter potential" );
        valid = false;
    }
	else if (document.MyForm1.param4.value == "")
    {
        alert ( "Please enter concentration" );
        valid = false;
    }
	else if(document.MyForm1.constant.value == "")
	{
		alert( " Please enter the constant value");
		valid = false;
	}
	else if(document.MyForm1.stressfact.value == "")
	{
		alert( " Please enter the stress factor");
		valid = false;
	}
	else if (document.MyForm1.noIterations.value == "")
    {
        alert ( "Please enter the No of iterations" );
        valid = false;
    }

   return valid;
}

function resetForm() {
//alert('My "Reset" called.');
document.MyForm1.reset(); 
request.getParameter("noRows") = null;
}

</script>
<%@ page language = "java" import="java.io.*"%>
<%@ page language = "java" import="java.util.*"%>
<%
String path = "C:/tmp/";
// make change from localhost to IP address here 
String baseurl = "http://192.168.1.248:8080/Simulation/";

%>

<body  bgcolor="white" onload="setdefaults();">

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
    <h1> Corrosion Single Pit Growth Simulation (2D) </h1>
    <table>
	 <tr>
	  <td>
		  <form method=POST  name="MyForm1" action="/Simulation/simulate.jsp"   onsubmit="return validate_form();" >
		  <div class = "data" align="center" >
			<table>
				<tr>
				<td> 
					Panel Size (Rows X Columns):  </td> <td><input type="text" name="noRows" size ="5" maxlength ="10"  value="<%=request.getParameter("noRows")!=null?request.getParameter("noRows"):""%>"/>
					 X <input type="text" name="noColumns" size ="5" maxlength ="10"  value="<%=request.getParameter("noColumns")!=null?request.getParameter("noColumns"):""%>" /></td>
					</tr>
					<tr><td> 
					<!-- pH value can be changed to whatever parameter name it may be, In the applet called it is passed as param1-->
					PH Value: </td><td><input type="text" name="param1" size ="8" maxlength ="10" value="<%=request.getParameter("param1")!=null?request.getParameter("param1"):""%>"/></td>
					</tr>
					<tr><td>
					<!-- Temperature can be changed to whatever parameter name it may be, In the applet called it is passed as param2-->
					Temperature: </td><td><input type="text" name="param2" size ="8" maxlength ="10"  value="<%=request.getParameter("param2")!=null?request.getParameter("param2"):""%>" /></td>
					</tr>
					<tr><td>
					<!-- Potential can be changed to whatever parameter name it may be, In the applet called it is passed as param3-->
					Potential:</td><td> <input type="text" name="param3"  size ="8" maxlength ="10" value="<%=request.getParameter("param3")!=null?request.getParameter("param3"):""%>"/></td>
					</tr>
					<tr><td>
					<!-- Concentration can be changed to whatever parameter name it may be, In the applet called it is passed as param4-->
					Concentration : </td><td><input type="text" name="param4" size ="8" maxlength ="10"  value="<%=request.getParameter("param4")!=null?request.getParameter("param4"):""%>" /></td>
					</tr>
					<tr><td>
					Stress factor (Constant ^ &#948):  </td> <td><input type="text" name="constant" size ="5" maxlength ="10"  value="<%=request.getParameter("constant")!=null?request.getParameter("constant"):""%>"/>
					 ^ <input type="text" name="stressfact" size ="5" maxlength ="10"  value="<%=request.getParameter("stressfact")!=null?request.getParameter("stressfact"):""%>" /></td>
					</tr>
					<tr><td>
					Number of Iterations (Time):</td><td><input type="text" name="noIterations" size ="8" maxlength ="10"  value="<%=request.getParameter("noIterations")!=null?request.getParameter("noIterations"):""%>"/></td>
					
				  </td>
				</tr>
		
			</table>
		</div>
		</br>
		<div align = "center" >
					<input value ="Run" name ="submit" type="submit" class ="btn" >
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

	
            if(request.getParameter("submit") != null) {
				  if(request.getParameter("submit").equals("Run")) {
		 String delPath = path+"files/";
		  File f1 = new File(delPath+"Features.txt");
    	  File f2 = new File(delPath+"Others.txt");
    	  File f3 = new File(delPath+"PitSize.txt");
    	  File f4 = new File(delPath+"PitGrowthRate.txt");
		  File f5 = new File(delPath+"simFiles.zip");
		  File f6 = new File(delPath+"simulation.JPG");
    	 
		  if(f1.exists() || f2.exists () || f3.exists() || f4.exists()|| f5.exists() || f6.exists()){
    	  boolean success1 = f1.delete();
    	  boolean success2 = f2.delete();
    	  boolean success3 = f3.delete();
    	  boolean success4 = f4.delete();
		  boolean success5 = f5.delete();
		  boolean success6 = f6.delete();
		  }
    	  
    	  
    	 
        %>
      
   
		<jsp:useBean id="formHandler" class="com.sim.SimulateBean">
		</jsp:useBean>
		<jsp:setProperty name="formHandler" property="*"/>
		<table>
		<tr>
		<td>
		<jsp:plugin type="applet" code="Sim2D.class" archive="Sim2DV1.jar,charts4j-1.3.jar,junit-4.4.jar" jreversion="1.6" width="400" height="400"  >
		 <jsp:params>
				 <jsp:param name="noRows"  value="<%= formHandler.getNoRows() %>"/>
				 <jsp:param name="noColumns"  value="<%= formHandler.getNoColumns() %>"/>
				 <jsp:param name="param1"  value="<%= formHandler.getparam1() %>"/>
				 <jsp:param name="param2"  value="<%= formHandler.getparam2() %>"/>
				 <jsp:param name="param3"  value="<%= formHandler.getparam3() %>"/>
				 <jsp:param name="param4"  value="<%= formHandler.getparam4() %>"/>
				 <jsp:param name="noIterations"  value="<%= formHandler.getNoIterations() %>"/>
				 <jsp:param name="constant"  value="<%= formHandler.getconstant() %>"/>
				 <jsp:param name="stressfact"  value="<%= formHandler.getstressfact() %>"/>
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
	
	</td>
	</tr>
	</table>
	</td>
	</tr>
	<tr>
	<td>

	<table >
	<tr align ="left">
 
	<a href="file:///C:/tmp/files/simFiles.zip" target ="_new" > Right Click and Save Target As to download the Files & Images</a>

	</tr>
	<tr>
	<td>
	   <iframe src="post_chart.jsp" width="950" height="350"></iframe>
	   </td>
	   </tr>
	   </table>

	   
		<%
		 }
	
		}
		%>
	</td>
	</tr>
	</table>

	
	</div>
	</div>
</body>
</html>
