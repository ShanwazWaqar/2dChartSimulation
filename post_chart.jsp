<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

     <link href="css/style.css" rel="stylesheet"/>   
  <script type='application/javascript'>
 
  </script>

   <script type="text/javascript" src="https://www.google.com/jsapi"></script>
 <script type="text/javascript"  language="javascript" >
    </script>
  <script type="text/javascript">
      google.load("visualization", "1.1", {packages:["imagelinechart"]});
      google.setOnLoadCallback(drawChart);
	 
      function drawChart() {
		var delimiter =",";
		var iterArray = document.formgraph.iterArray1.value;
		var heightArray = document.formgraph.heightArray.value;
		var widthArray = document.formgraph.widthArray.value;
	
		var listIterations = new Array();
		var listHeight = new Array();
		var listWidth = new Array();
		listIterations = iterArray.split(delimiter);
		listHeight = heightArray.split(delimiter);
		listWidth = widthArray.split(delimiter);

	    var data = new google.visualization.DataTable();
        data.addColumn('string', 'Iterations');
        data.addColumn('number', 'Height');
        data.addColumn('number', 'Width');
        data.addRows(30);
		
		for(var i=0;i<listIterations.length-2;i++){
		  data.setValue(i, 0, listIterations[i+1]);
		
		}
			alert(listHeight.length);
		for(var j =0;j<listHeight.length-2;j++){
		  data.setValue(j, 1, parseInt(listHeight[j+1]));
		}
		for(var k=0;k<listWidth.length-2;k++){
		  data.setValue(k, 2,parseInt(listWidth[k+1]));
		}

     
		
        var chart = new google.visualization.ImageLineChart(document.getElementById('chart_div'));
        chart.draw(data, {width: 500, height: 250, title: 'Pit Size',colors:['#0000FF','#00FF00']});

		drawchart2();
      }
	  function drawchart2(){

		var delimiter =",";
		var iterArray = document.formgraph.iterArray2.value;
		var redRateArray = document.formgraph.redRate.value;
		var greenRateArray = document.formgraph.greenRate.value;
		var blueRateArray = document.formgraph.blueRate.value;
	
		var listIterations = new Array();
		var listRedRate = new Array();
		var listGreenRate = new Array();
		var listBlueRate = new Array();

		listIterations = iterArray.split(delimiter);
		listRedRate = redRateArray.split(delimiter);
		listGreenRate = greenRateArray.split(delimiter);
		listBlueRate = blueRateArray.split(delimiter);

	    var data = new google.visualization.DataTable();
        data.addColumn('string', 'Iterations');


		data.addColumn('number', 'Blue');
		data.addColumn('number', 'Green');
		data.addColumn('number', 'Red');
	
		

	
        data.addRows(30);
		
		for(var i=0;i<listIterations.length-2;i++){
		  data.setValue(i, 0, listIterations[i+1]);
		
		}
		for(var l=0;l<listBlueRate.length-2;l++){
		  data.setValue(l, 1 ,parseInt(listBlueRate[l+1]));
		}
		for(var k=0;k<listGreenRate.length-2;k++){
		  data.setValue(k, 2,parseInt(listGreenRate[k+1]));
		}
		for(var j =0;j<listRedRate.length-2;j++){
		  data.setValue(j, 3, parseInt(listRedRate[j+1]));
		}
		
		
		
        var chart = new google.visualization.ImageLineChart(document.getElementById('chart_div1'));
        chart.draw(data, {width: 500, height: 250, title: 'Pit Growth Rate',colors:['#0000FF','#00FF00','#FF0000'],chp:5,vAxis:{title: 'Iterations'}});

	  }
    </script>
  </head>
  <body>
<%@ page language = "java" import="java.io.*"%>
<%@ page language = "java" import="java.util.*"%>
<%
String path = "C:/tmp/";
%>

  <form action="/Simulation/post_chart.jsp" method='POST'  name="formgraph" id='post_form' >
  <input type='submit' value ="Display Graphs" name ="submit" class ="btn"/>
		 <% 
            if(request.getParameter("submit") != null) {
				  if(request.getParameter("submit").equals("Display Graphs")) {
        %>	
		<%      File file1 = new File(path+"files/PitSize.txt");
		File file2 = new File(path+"files/PitGrowthRate.txt");
		if(file1.exists() || file2.exists ()){
		
        StringBuffer iterations1 = new StringBuffer();
		StringBuffer width = new StringBuffer();
        StringBuffer height = new StringBuffer();

		StringBuffer iterations2 = new StringBuffer();
		StringBuffer RedRate = new StringBuffer();
        StringBuffer GreenRate = new StringBuffer();
        StringBuffer BlueRate = new StringBuffer();
       
		String delimiter = ",";
        BufferedReader reader1 = null;
		BufferedReader reader2 = null;

        try {
            reader1 = new BufferedReader(new FileReader(file1));
			reader2 = new BufferedReader(new FileReader(file2));
            String text1 = null;
		    String text2 = null;

            // repeat until all lines is read
            while ((text1 = reader1.readLine()) != null) {
			String[] listtokena = text1.split(delimiter);
			iterations1.append(listtokena[0]+",");
			width.append(listtokena[1]+",");
			height.append(listtokena[2]+",");
            }

			while ((text2 = reader2.readLine()) != null) {
			String[] listtokenb = text2.split(delimiter);
			iterations2.append(listtokenb[0]+",");
			RedRate.append(listtokenb[1]+",");
			GreenRate.append(listtokenb[2]+",");
			BlueRate.append(listtokenb[3]+",");
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
				  if (reader2 != null) {
                    reader2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // show file contents here
     //   System.out.println(contents.toString());

%>
	<input type="hidden" name="iterArray1" value ="<%=iterations1.toString()%>"  >
		<input type="hidden" name="widthArray" value ="<%=width.toString()%>"  >
		<input type="hidden" name="heightArray" value ="<%=height.toString()%>"  >

		<input type="hidden" name="iterArray2" value ="<%=iterations2.toString()%>"  >
		<input type="hidden" name="redRate" value ="<%=RedRate.toString()%>"  >
		<input type="hidden" name="greenRate" value ="<%=GreenRate.toString()%>"  >
		<input type="hidden" name="blueRate" value ="<%=BlueRate.toString()%>"  >
		<table>
		<tr>
		<td>
 			  <div id="chart_div"></div>
			</td>
			<td>
			 <div id="chart_div1"></div>
			 </td>
			 </tr>
			 </table>


	<%
	}else{%>
	<table>
	<tr>
	<td>
	<font face="Verdana" size="2" color ="red"><% out.println("Simulation in progress, please wait");%> </font>
	</td>
	</tr>
	</table>
	<%} 
	}}%>
    </form>
  </body>
</html>