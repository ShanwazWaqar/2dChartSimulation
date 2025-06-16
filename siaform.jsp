<%@ page session="true" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
	<title> Simulation</title>
	<meta http-equiv="Content-Type" content="text/html; charset=unicode"/>
    <link href="css/survey.css" rel="stylesheet" type="text/css"/>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>  
	<link rel="stylesheet" type="text/css" href="css/jqueryslidemenu.css" />
		<link rel="stylesheet" type="text/css" href="css/style.css" />

	<!--[if lte IE 7]> <![endif]-->
	<style type="text/css">
	html .jqueryslidemenu{height: 4%;} /*Holly Hack for IE7 and below*/
	</style>
	

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jqueryslidemenu.js"></script>

</head>
 
<script type="text/javascript" language="javascript">
 function validate_form()
{
	
    valid = true;
	

   /* if (document.siaform.trainfile.value == "" || document.siaform.testfile.value == "")
    {
        alert ( "Please select the train/test file" );
        valid = false;
    }*/
	return valid;
}

</script>
<%@ page language = "java" import="java.io.*"%>
<%@ page language = "java" import="java.util.*"%>
<%

String serverpath = request.getRealPath("/");
out.println("serverpath:"+serverpath);
// make change from localhost to IP address here 
String baseurl = "http://192.168.1.248:8080/Simulation/";


%>
<body>

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

   <div align="center" class="content">
	<h1>Neural Network Analysis</h1>
	  <form method=POST name ="siaform"  action="/Simulation/servlet/SiaServlet" enctype="multipart/form-data" onsubmit="return validate_form();">
	  <div class = "data" align="center">

		<table>
		<tr>
		<td> 
		<h3> Train the neural network </h3>
		</br>
		    Enter Number of Input Nodes: <input type="text" name="inNodes" value ="3" /><br/>
			Enter Number of Output Nodes : <input type="text" name="outNodes" value ="1"  /><br/>
			Enter Number of Layers: <input type="text" name="noLayers" value ="3"  /><br/>
			Number of Nodes in the First Hidden Layer: <input type="text" name="noFirstLayer" value ="15"  /><br/>
			Number of Nodes in the Second Hidden Layer : <input type="text" name="noSecLayer" value ="4" /><br/>
			
			Enter Number of Training Data :<input type="text" name="notraindata" value ="37" /><br/>
		
			<br/>
			<br/>

			<b> Enter the Training Data -> InputFile: </b> <INPUT TYPE=FILE SIZE = 50 NAME=trainfile ><br><br>

		 <p><b> 

		Select option for neural network: </b><p>
		<input type="radio" name="radionet" value="train" CHECKED ="TRUE" /> Train the neural network <br />
		<input type="hidden" name="serverpath" value ="<%=serverpath%>">

		  <br>
		  </td>
		</tr>
		<tr>

		<td>
		<h3> Test the neural network </h3>
		</br>
			Enter Number of Testing Data :<input type="text" name="notestdata" value ="100"  /><br/>
					 <p>
		 	<b> Enter the Testing Data -> InputFile: </b> <INPUT TYPE=FILE SIZE = 50 NAME=testfile><br><br>
					<input type="radio" name="radionet" value="test" /> Test the neural network <br />
		</td>


		</tr>

	</table>

	

	<br/>
		<div align = "center">
			
			<input type="hidden" name="action"  value="Submit">
			<input value ="Submit" class ="btn" type="SUBMIT">
		</div>
		
	</div>
</form>
</div>
</div>
</body>
</html>