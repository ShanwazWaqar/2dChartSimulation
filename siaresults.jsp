
<html lang="en">
<%@ page language = "java" import="java.io.*"%>
<%@ page language = "java" import="java.util.*"%>
<%

String path = request.getRealPath("/");
String baseurl = "http://192.168.1.248:8080/Simulation/";

 
%>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
	<title> Simulation</title>
	<meta http-equiv="Content-Type" content="text/html; charset=unicode"/>
    <link href="<%=baseurl%>css/survey.css" rel="stylesheet" type="text/css"/>
    <link href="<%=baseurl%>css/style.css" rel="stylesheet" type="text/css"/>  
	<link rel="stylesheet" type="text/css" href="<%=baseurl%>css/jqueryslidemenu.css" />

	<!--[if lte IE 7]><![endif]-->
	<style type="text/css">
	html .jqueryslidemenu{height: 4%;} /*Holly Hack for IE7 and below*/
	</style>
	 
  <script type='application/javascript'>
 </script>

<script type="text/javascript" src="<%=baseurl%>js/jquery.min.js"></script>
<script type="text/javascript" src="<%=baseurl%>js/jqueryslidemenu.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
  <script type="text/javascript">
      google.load("visualization", "1.1", {packages:["imagelinechart"]});
      google.setOnLoadCallback(drawChart);
	 function drawChart() {
		var delimiter =",";
		var iterArray = document.siagraph.iterArray.value;
		var errValArray = document.siagraph.errValArray.value;

		if(iterArray !=null || errValArray!=null){
	
		var listIterations = new Array();
		var listerrVal = new Array();
	
		listIterations = iterArray.split(delimiter);
		listerrVal = errValArray.split(delimiter);

	    var data = new google.visualization.DataTable();

        data.addColumn('string', 'Iterations');
        data.addColumn('number', 'Error Value');
        data.addRows(500);
		

		for(var i=0;i<listIterations.length-2;i++){
		  data.setValue(i, 0, listIterations[i+1]);
		
		}
			
		for(var j =0;j<listerrVal.length-2;j++){
		  data.setValue(j, 1, parseFloat(listerrVal[j+1]));
		}
	
     
		
        var chart = new google.visualization.ImageLineChart(document.getElementById('chart_div'));
        chart.draw(data, {width: 500, height: 250, title: 'Error Graph',colors:['#0000FF','#00FF00']});
		}
		
      }
	  
    </script>
</head>


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
 <form action="/Simulation/siaresults.jsp" method='POST'  name="siagraph" id='post_form' >
  <div class = "data" align="center">
	<h1>Neural Network Results</h1>

	<table width ="70%">
	<tr>
	<td> 
		<div>
		<h3>Training Related Files</h3>
	</td>
	</tr>
	<tr>
	<td>
		 <a target="_new" href="<%=baseurl%>nnet_sia/EFile.txt"> Click here to open Error File</a>
		</div>
		</br>
		<div>
		 <a target="_new" href="<%=baseurl%>nnet_sia/WtFile.txt">Click here to open Weight File</a>
		</div>
		<div>	
	</td>
	</tr>
	<tr>	
	<td>
	<h3>Testing Related Files</h3>
	</td>
	</tr>
	<tr>
	<td>
	 <a target="_new" href="<%=baseurl%>nnet_sia/results.txt">Click here to open results File</a>
	</div>

	</br>
	<input type='submit' value ="Display Graphs" name ="submit" class ="btn"/>
	</div>


 <% 
            if(request.getParameter("submit") != null) {
				  if(request.getParameter("submit").equals("Display Graphs")) {
        %>	
<%	
		File file1 = new File(path+"nnet_sia/EFile.txt");

		if(file1.exists()){
		

        StringBuffer iterations = new StringBuffer();
	    StringBuffer errVal = new StringBuffer();
 
		String delimiter = ",";
        BufferedReader reader1 = null;
	
			try {

				reader1 = new BufferedReader(new FileReader(file1));
				String text1 = null;
		
				// repeat until all lines is read
				while ((text1 = reader1.readLine()) != null) {
				String[] listtokena = text1.split(delimiter);
				iterations.append(listtokena[0]+",");
				errVal.append(listtokena[1]+",");
			
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader1 != null) {
						reader1.close();
					}
					 
				} catch (IOException e) {
					e.printStackTrace();
				}
			} // end of finally
%>
<tr>
<td>
<div>
		<input type="hidden" name="iterArray" value ="<%=iterations.toString()%>"  >
		<input type="hidden" name="errValArray" value ="<%=errVal.toString()%>"  >
	</div>
	<div align ="center">

	<table>
		<tr>
		<td>
 			  <div id="chart_div"></div>
			</td>
			 </tr>
			 </table>
	</div>

	<%
	}} }%>


 <p>
 <div>
 <a href="/Simulation/siaform.jsp"><font face ="Arial Unicode MS" >Train or test the data again ? </font></a>
</div>
 </td>
</tr>
 </div>
 </div>
 </form>
</body>
</html>