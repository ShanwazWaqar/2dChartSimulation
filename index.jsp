<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
	<title> Simulation</title>
	<meta http-equiv="Content-Type" content="text/html; charset=unicode"/>
    <link href="css/survey.css" rel="stylesheet" type="text/css"/>
    <link href="css/style.css" rel="stylesheet" type="text/css"/>  
	<link rel="stylesheet" type="text/css" href="css/jqueryslidemenu.css" />

	<!--[if lte IE 7]> 	<![endif]-->
	<style type="text/css">
	html .jqueryslidemenu{height: 4%;} /*Holly Hack for IE7 and below*/
	</style>


<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jqueryslidemenu.js"></script>

</head>
 
<script type="text/javascript" language="javascript">


</script>
<%@ page language = "java" import="java.io.*"%>
<%@ page language = "java" import="java.util.*"%>
<%
// make change from localhost to IP address here 
String baseurl = "http://192.168.1.248:8080/Simulation/";

%> 

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
  <div>  <img class="banner" src="<%=baseurl%>/images/sim_home.png"></div>
	</div>
	</div>

</body>
</html>
