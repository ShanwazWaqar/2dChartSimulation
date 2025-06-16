<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
	<title> Material Constants  </title>
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
//	alert(document.corForm.matParam.value);
     //This below line of code will try to reload/refresh the parent window which is your base jsp page.
     //I am not sure the below is the correct line of code to refresh...just try it and if not look for the appropriate line of code. However idea is this one.
	//  window.location.href="inputMaterial.jsp" + "&id=" +material;
	 window.location.href="inputMaterial.jsp";
     //window.parent.location.reload();
}

function setdefaults(){
	/*--- Corrosion Material 1 Parameters---*/
	var material = '${param.id}';  
	if(	document.corForm.matParam.value =="")
		document.corForm.matParam.value =material;
    if(material =="Material 2"){
		if (document.corForm.atomMass.value == ""){
			document.corForm.atomMass.value ="56";
		}
		if (document.corForm.valence.value == ""){
		document.corForm.valence.value ="2";
		}
		if (document.corForm.density.value == ""){
		document.corForm.density.value ="7780";
		}
		if (document.corForm.thresRng.value == ""){
		document.corForm.thresRng.value ="8.14";
		}
		if (document.corForm.pitCoeff.value == ""){
		document.corForm.pitCoeff.value ="1";
		}
		if (document.corForm.enthalpy.value == ""){
		document.corForm.enthalpy.value ="15.5";
		}


	}else{
		if (document.corForm.atomMass.value == ""){
			document.corForm.atomMass.value ="27";
		}
		if (document.corForm.valence.value == ""){
		document.corForm.valence.value ="3";
		}
		if (document.corForm.density.value == ""){
		document.corForm.density.value ="2700";
		}
		if (document.corForm.thresRng.value == ""){
		document.corForm.thresRng.value ="2.32";
		}
		if (document.corForm.pitCoeff.value == ""){
		document.corForm.pitCoeff.value ="3.52";
		}
		if (document.corForm.enthalpy.value == ""){
		document.corForm.enthalpy.value ="40";
		}

	}



	
	if (document.corForm.farCnst.value == ""){
		document.corForm.farCnst.value ="96485";
	}
	
	if (document.corForm.stressCnst.value == ""){
		document.corForm.stressCnst.value ="2.8";
	}
	
	if (document.corForm.gasCnst.value == ""){
		document.corForm.gasCnst.value ="8.314";
	}
	if (document.corForm.temp.value == ""){
		document.corForm.temp.value ="293";
	}
	if (document.corForm.freq.value == ""){
		document.corForm.freq.value ="10";
	}
	


	/*---- Fatique Material 1 parameters--*/
	if (document.corForm.fcoeffsmall.value == ""){
		document.corForm.fcoeffsmall.value ="3.94";
	}
	if (document.corForm.fcoefflong.value == ""){
		document.corForm.fcoefflong.value ="1.80";
	}
	if (document.corForm.fexpsmall.value == ""){
		document.corForm.fexpsmall.value ="3.55";
	}
	if (document.corForm.fexplong.value == ""){
		document.corForm.fexplong.value ="3.55";
	}
	if (document.corForm.geoSmall.value == ""){
		document.corForm.geoSmall.value ="2.2";
	}
	if (document.corForm.geoLong.value == ""){
		document.corForm.geoLong.value ="1";
	}
	if (document.corForm.crackLen.value == ""){
		document.corForm.crackLen.value ="1";
	}
	if (document.corForm.fracTough.value == ""){
		document.corForm.fracTough.value ="26";
	}


} 
/*
function proceed(){
   opener.location.reload(true);
   self.close();
}*/
/*function sendValuesToParent(){
 window.opener.document.matForm.atomMass.value = document.getElementById('atomMass').value;
 //alert(document.getElementById('atomMass').value);
  opener.location.reload(true);
   self.close();
}*/
function sendValue (){
opener.document.matForm.atomMass.value = document.corForm.atomMass.value;
opener.document.matForm.valence.value =  document.corForm.valence.value;
opener.document.matForm.farCnst.value =  document.corForm.farCnst.value;
opener.document.matForm.density.value = document.corForm.density.value;
opener.document.matForm.thresRng.value = document.corForm.thresRng.value;
opener.document.matForm.stressCnst.value = document.corForm.stressCnst.value;
opener.document.matForm.pitCoeff.value = document.corForm.pitCoeff.value;
opener.document.matForm.enthalpy.value = document.corForm.enthalpy.value;
opener.document.matForm.gasCnst.value = document.corForm.gasCnst.value;
opener.document.matForm.temp.value = document.corForm.temp.value;
opener.document.matForm.freq.value = document.corForm.freq.value;

opener.document.matForm.fcoeffsmall.value = document.corForm.fcoeffsmall.value;
opener.document.matForm.fcoefflong.value = document.corForm.fcoefflong.value;
opener.document.matForm.fexpsmall.value = document.corForm.fexpsmall.value;
opener.document.matForm.fexplong.value = document.corForm.fexplong.value;
opener.document.matForm.geoSmall.value = document.corForm.geoSmall.value;
opener.document.matForm.geoLong.value = document.corForm.geoLong.value;
opener.document.matForm.crackLen.value = document.corForm.crackLen.value;
opener.document.matForm.fracTough.value = document.corForm.fracTough.value;
self.close();
 
}
/*function resetForm() {
//alert('My "Reset" called.');
document.MyForm.reset(); 
request.getParameter("noRows") = null;
} */

</script>
<%@ page language = "java" import="java.io.*"%>
<%@ page language = "java" import="java.util.*"%>
<%@ page language = "java" import="com.sim.*"%>

<%
//String path = "C:/tmp/";
String baseurl = "http://192.168.1.248:8080/Simulation/";
//out.println(request.getRealPath("/"));

%>

<body  bgcolor="white" onload="setdefaults();">

 <div>  

   <div align="center" class="content">
    <h1> Material Constants </h1>
    <table>
	 <tr>
	  <td>
		  <form method=POST  name="corForm" action="" >
		  <div class = "data" align="center" >
			<table>
				<tr> 
					<td>
						<b>Corrosion:</b>
					</td>
					<td>
						Enter parameter input values for <b>  <u>${param.id} </u> :</b>
						<td>
							</td> <td><input type="hidden" name="matParam" size ="7" maxlength ="10" value="${param.id}"/>
					</td>
					</td>
				</tr>
				
				<tr>
				 <!-- Material 1 parameters-->
				<td>
					<tr> 

						<td> 
						Atomic Mass,<I>M</I> (kg/mol):  </td> <td><input type="text" name="atomMass" size ="7" maxlength ="10"  value="<%=request.getParameter("atomMass")!=null?request.getParameter("atomMass"):""%>"/> x 10<sup>-3</sup>
						 </td>
						

					</tr>
					<tr>
						<td> 
						Valence,<I>n</I>: </td><td><input type="text" name="valence" size ="7" maxlength ="10" value="<%=request.getParameter("valence")!=null?request.getParameter("valence"):""%>"/></td>
					</tr>
					<tr>
						<td>
						Faraday's constant,<I>F</I> (C/mol): </td><td><input type="text" name="farCnst" size ="7" maxlength ="10"  value="<%=request.getParameter("farCnst")!=null?request.getParameter("farCnst"):""%>" /></td>
					</tr>
					<tr>
						<td>
						Density,<I>p</I> (kg/m^3):</td><td> <input type="text" name="density"  size ="7" maxlength ="10" value="<%=request.getParameter("density")!=null?request.getParameter("density"):""%>"/></td>
				
					</tr>
					<tr>
						<td>
						Threshold stress intensity range,&#8710<I>;Kth</I> (MPa&#8730;m) : </td><td><input type="text" name="thresRng" size ="7" maxlength ="10"  value="<%=request.getParameter("thresRng")!=null?request.getParameter("thresRng"):""%>" /></td>
						</tr>
					<tr>
						<td>
						Stress concentration factor,<I>Kt </I>:  </td> <td><input type="text" name="stressCnst" size ="7" maxlength ="10"  value="<%=request.getParameter("stressCnst")!=null?request.getParameter("stressCnst"):""%>"/> </td>
				
					</tr>
					<tr>
					
					<td>
					<input type="hidden" name="pitCoeff" size ="7" maxlength ="10"  value="<%=request.getParameter("pitCoeff")!=null?request.getParameter("pitCoeff"):""%>"/></td>
					
					</tr>
					<tr>
						<td>
						Enthalpy,&#8710<I>H</I> (J/mol):  </td> <td><input type="text" name="enthalpy" size ="7" maxlength ="10"  value="<%=request.getParameter("enthalpy")!=null?request.getParameter("enthalpy"):""%>"/>x 10<sup>3</sup></td>
						
					</tr>
					<tr>
						<td>
						Universal gas constant,<I>R</I> (J/mol K):  </td> <td><input type="text" name="gasCnst" size ="7" maxlength ="10"  value="<%=request.getParameter("gasCnst")!=null?request.getParameter("gasCnst"):""%>"/></td>

					</tr>
					<tr>
						<td>
						Temperature,<I>T</I> (K):  </td> <td><input type="text" name="temp" size ="7" maxlength ="10"  value="<%=request.getParameter("temp")!=null?request.getParameter("temp"):""%>"/></td>

					</tr>
					<tr>
						<td>
						<input type="hidden" name="freq" size ="7" maxlength ="10"  value="<%=request.getParameter("freq")!=null?request.getParameter("freq"):""%>"/></td>


					</tr>
	    			</td>
					
					<tr> <td>
						<b>Fatigue :</b>
					</td> </tr>
				 <tr>
					<td> 
					Fatigue coefficient for small crack ,<I>C1</I>:  </td> <td><input type="text" name="fcoeffsmall" size ="7" maxlength ="10"  value="<%=request.getParameter("fcoeffsmall")!=null?request.getParameter("fcoeffsmall"):""%>"/> x 10<sup>-11</sup>
					 </td>
	
				</tr>

				<tr>
					<td> 
					Fatigue coefficient for long crack ,<I>C2</I>:  </td> <td><input type="text" name="fcoefflong" size ="7" maxlength ="10"  value="<%=request.getParameter("fcoefflong")!=null?request.getParameter("fcoefflong"):""%>"/>x 10<sup>-11</sup>
					 </td>
		
				</tr>
				
				<tr>
					<td> 
					Fatigue exponent for small crack ,<I>m1</I>:  </td> <td><input type="text" name="fexpsmall" size ="7" maxlength ="10"  value="<%=request.getParameter("fexpsmall")!=null?request.getParameter("fexpsmall"):""%>"/>
					 </td>
	
				</tr>
				<tr>
					<td> 
					Fatigue exponent for long crack ,<I>m2</I>:  </td> <td><input type="text" name="fexplong" size ="7" maxlength ="10"  value="<%=request.getParameter("fexplong")!=null?request.getParameter("fexplong"):""%>"/>
					 </td>

				</tr>

					<tr>
					<td> 
					Geometry factor for small crack ,<I>&#946;1</I>:  </td> <td><input type="text" name="geoSmall" size ="7" maxlength ="10"  value="<%=request.getParameter("geoSmall")!=null?request.getParameter("geoSmall"):""%>"/>
					/&#960;
					 </td>

				</tr>

				<tr>
					<td> 
					Geometry factor for long crack ,<I>&#946;2</I>:  </td> <td><input type="text" name="geoLong" size ="7" maxlength ="10"  value="<%=request.getParameter("geoLong")!=null?request.getParameter("geoLong"):""%>"/>
					 </td>

				</tr>

				<tr>
					<td> 
					Transition Crack Length ,<I>atr</I> (m):  </td> <td><input type="text" name="crackLen" size ="7" maxlength ="10"  value="<%=request.getParameter("crackLen")!=null?request.getParameter("crackLen"):""%>"/>x 10<sup>-3</sup>
					 </td>

				</tr>

				<tr>
					<td> 
					Fracture Toughness ,<I>Kc</I> (MPa &#8730;m):  </td> <td><input type="text" name="fracTough" size ="7" maxlength ="10"  value="<%=request.getParameter("fracTough")!=null?request.getParameter("fracTough"):""%>"/>
					 </td>

				</tr>
				<tr>
									

				</tr>

			</td>
	</table>
</br>

	
			<div align = "center" >
					<input value ="Submit Paramter Values" name ="submit" type="submit" class ="btn" onClick="sendValue();">
					<input value ="Reset" type="button" class ="btn" onclick ="refreshParentWindow();" >

			</div>
		</form>
		
	</td>
	
	
	</tr>

	</table>


</body>
</html>
