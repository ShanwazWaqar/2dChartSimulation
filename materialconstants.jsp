<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
	<title> Corrosion Fatigue Life Prediction  </title>
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
  window.location.href="materialconstants.jsp";
     //window.parent.location.reload();
}
function selectValue(id){
	 window.open('inputMaterial.jsp?id=' + encodeURIComponent(id),'popuppage',
      'width=500,toolbar=1,resizable=1,scrollbars=yes,height=600,top=20,left=50');

}
function setdefaults(){
	if (document.matForm.pitCoeff.value == ""){
		document.matForm.pitCoeff.value ="3.52";
	}
	if (document.matForm.freq.value == ""){
		document.matForm.freq.value ="10";
	}

	if (document.matForm.scoeff.value == ""){
		document.matForm.scoeff.value ="1.01";
	}
	if (document.matForm.sigma.value == ""){
		document.matForm.sigma.value ="100";
	}

}
function validateForm(str){
	//String btnMat =request.getParameter("choice");

	if (document.matForm.pitCoeff.value == ""){
		alert("Please enter the Pit Coefficient value");
		return;
	}
	if (document.matForm.freq.value == ""){
		alert("Please enter the frequency value");
		return;
	}
	if (document.matForm.sigma.value == ""){
		alert("Please enter the sigma value");
		return;
	}
	if (document.matForm.apc_final.value == ""){
		alert("Please enter the crack size apc value");
		return;
	}

	if (document.matForm.af_final.value == ""){
		alert("Please enter the final crack size value");
		return;
	}
	if (document.matForm.scoeff.value == ""){
		alert("Please enter the stress coefficient value");
		return;
	}



}
/*function resetForm() {
//alert('My "Reset" called.');
document.MyForm1.reset(); 
request.getParameter("noRows") = null;
} */

</script>
<%@ page language = "java" import="java.io.*"%>
<%@ page language = "java" import="java.util.*"%>
<%@ page language = "java" import="com.sim.*"%>

<%
// make change from localhost to IP address here 
String baseurl = "http://localhost:8080/Simulation/";

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
    <h1> Corrosion Fatique Life Prediction </h1>
    <table >
	 <tr>
	  <td>
		  <form method=POST  name="matForm" action="/Simulation/materialconstants.jsp"  onsubmit="return validate_form();" >
		  <div class = "data" align="center" >
			<table>
				<tr>  
				<td> <b>INPUTS:</b> </td>
				<td> </td>	 <td> </td>	<td></td>
				</tr>

				<tr> 
					 <td> Select a Material Constant : </td>
					 <td>
					   <input type="button" name="choice" onClick="selectValue('Material 1')" value="Material 1">
					</td>
					<td>
					 <input type="button" name="choice" onClick="selectValue('Material 2')" value="Material 2">
					</td>
					<td>
					 <input type="button" name="choice" onClick="selectValue('Material 3')" value="Material 3">
					</td>
   
				</tr>			
			
				<tr>
					<td>
							Pitting current coefficient,<I>Ip0</I> (C/s):</td><td><input type="text" name="pitCoeff" size ="7" maxlength ="10" value="<%=request.getParameter("pitCoeff")!=null?request.getParameter("pitCoeff"):""%>" /> x 10<sup>-2</sup></td>
					</tr>
				<tr>
					<td>
							Frequency,<I>f</I> (Hz):  </td><td><input type="text" name="freq" size ="7" maxlength ="10"  value="<%=request.getParameter("freq")!=null?request.getParameter("freq"):""%>" />
					</td>
				</tr>
				<tr>
					<td>
							Sigma,<I>&#963;</I> : </td> <td><input type="text" name="sigma" size ="7" maxlength ="10"  value="<%=request.getParameter("sigma")!=null?request.getParameter("sigma"):""%>" />
					</td>
				</tr>
				<tr>
					<td>
							Crack Size from pitting to crack,<I>apc</I> : </td> <td><input type="text" name="apc_final" size ="7" maxlength ="10"  value="<%=request.getParameter("apc_final")!=null?request.getParameter("apc_final"):""%>" />
					</td>
				</tr>
					<tr>
					<td>
							Final Crack Size,<I>af</I> : </td> <td><input type="text" name="af_final" size ="7" maxlength ="10"  value="<%=request.getParameter("af_final")!=null?request.getParameter("af_final"):""%>" />
					</td>
				</tr>
					<tr>
					<td>
							Stress Coefficient,<I>C</I> : </td> <td><input type="text" name="scoeff" size ="7" maxlength ="10"  value="<%=request.getParameter("scoeff")!=null?request.getParameter("scoeff"):""%>" />
					</td>
				</tr>
							  <% 
					
								if(request.getParameter("submit") != null) {
									  if(request.getParameter("submit").equals("RunModel")) {

							   %>
								<jsp:useBean id="formHandler" class="com.sim.CorrosionBean">
								<jsp:setProperty name="formHandler" property="*"/>
								</jsp:useBean>
									</table>
						<table width ="100%" border =0>

				
								<tr width ="20%">  <td>
									<b>RESULTS:</b>
								 </td> <td> </td>	
								 <td> </td>	<td> </td>	<td> </td>	<td> </td>	<td> </td>	<td> </td>	<td> </td>	<td> </td>	<td> </td>	

								</tr>
								<tr>
								<td>
								<% if(formHandler.getInitiationLife() =="Error" ||formHandler.getcrackPropagation() =="Error" ||formHandler.getFatigueLife() =="Error"){
									out.println("Please check if all values are entered correctly");
								}else{	%>

								InitiationLife Value <I>(Ni)</I> is : </td> <td><%=formHandler.getInitiationLife()%>
								</td> 
								</tr>
								<tr>
								<td> Crack Propagation Value <I>(Np)</I> is :</td><td><%=formHandler.getcrackPropagation()%>
								</td> 										
								</tr>
								<tr>
								<td>
								FatigueLife Value <I>(Nf)</I> is :</td><td><%=formHandler.getFatigueLife()%>
								</td>
								</tr>
											
							<%
								}
							 }
						
							}
							%>
				</tr>
			</table>
			</div>
		<table>
			<tr>
			<td>
				<input type="hidden" name="atomMass">
			</td>
			<td>
				<input type="hidden" name="valence">
			</td>
			<td>
				<input type="hidden" name="farCnst">
			</td>
			<td>
				<input type="hidden" name="density">
			</td>
			<td>
				<input type="hidden" name="thresRng">
			</td>
			<td>
				<input type="hidden" name="stressCnst">
			</td>
			<td>

			</td>
			<td>
				<input type="hidden" name="enthalpy">
			</td>
			<td>
				<input type="hidden" name="gasCnst">
			</td>
			<td>
				<input type="hidden" name="temp">
			</td>
			<td>

			</td>
			<!-- fatique-->
			<td>
				<input type="hidden" name="fcoeffsmall">
			</td>
			<td>
				<input type="hidden" name="fcoefflong">
			</td>
			<td>
				<input type="hidden" name="fexpsmall">
			</td>
			<td>
				<input type="hidden" name="fexplong">
			</td>
			<td>
				<input type="hidden" name="geoSmall">
			</td>
			<td>
				<input type="hidden" name="geoLong">
			</td>
			<td>
				<input type="hidden" name="crackLen">
			</td>
			<td>
				<input type="hidden" name="fracTough">
			</td>
			</tr>
			</br>
		<tr>
		<div align = "center" >
					<input value ="RunModel" name ="submit" type="submit" class ="btn"  onclick ="validateForm('yes');">
					<input value ="Reset" type="button" class ="btn" onclick ="refreshParentWindow();" >
			</div>
	</tr>
	
	
		</table>

	
		
		
		</form>
		
	</td>
	</tr>
	</table>


</body>
</html>
