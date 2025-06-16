package com.sim;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CorrosionBean {
	// Inputs from main screen values added by user
	 private String apc_final; 
	 private String af_final;
	 private String scoeff;
	 
	 
	// Corrossion Inputs
	  private String atomMass;//Atomic Mass
	  private String valence; //Valence
	  private String farCnst;//Faraday's constant
	  private String density;// Density
	  private String thresRng; //Threshold stress intensity range
	  private String stressCnst;//Stress concentration factor
	  private String pitCoeff; //Pitting current coefficient
	  private String enthalpy; //enthalpy
	  private String gasCnst; //Universal gas constant
	  private String temp; // Temperature
	  private String freq; //Frequency
	//Fatigue Inputs
	  private String fcoeffsmall;//Fatigue coefficient for small crack
	  private String fcoefflong;//Fatigue coefficient for long crack
	  private String fexpsmall; //Fatigue exponent for small crack
	  private String fexplong; //Fatigue exponent for long crack
	  private String geoSmall;//Geometry factor for small crack
	  private String geoLong;//Geometry factor for long crack
	  private String crackLen;//Transition Crack Length
	  private String fracTough;//Fracture Toughness	  
	  private String initLife; // Initiation Life Ni
	  private String crackProp;// Crack Propagation Np
	  private String fatigueLife;// Fatigue Life Nf
	  //private double sigma;
	  private String sigma;
	  double inLife;
	  double crackNp;
	  
	public CorrosionBean() {
		apc_final="";
		af_final="";
		scoeff="";
		freq ="";
		pitCoeff="";
		/* Corrosion parameters*/
		atomMass="";
		valence="";
		farCnst="";
		density="";
		thresRng="";
		stressCnst="";		
		enthalpy="";
		gasCnst="";
		temp ="";
		
		/*fatique parameters */
		fcoeffsmall ="";
		fcoefflong ="";
		fexpsmall ="";
		fexplong ="";
		geoSmall ="";
		geoLong ="";
		crackLen ="";
		fracTough="";
		initLife ="";
		crackProp ="";
		fatigueLife ="";
		sigma = "";
		inLife =0.0;
		crackNp =0.0;
	}
	
		public String getApc_final() {
		    return apc_final;
	 	}
	 	public void setApc_final(String apc_final) {
		    this.apc_final = apc_final;
	 	}
	 	public String getAf_final() {
		    return af_final;
	 	}
	 	public void setAf_final(String af_final) {
		    this.af_final = af_final;
	 	}
	 	public String getScoeff() {
		    return scoeff;
	 	}
	 	public void setScoeff(String scoeff) {
		    this.scoeff = scoeff;
	 	}
		public String getSigma() {
		    return sigma;
	 	}
	 	public void setSigma(String sigma) {
		    this.sigma = sigma;
	 	}
	 	public String getAtomMass() {
		    return atomMass;
	 	}
	 	public void setAtomMass(String atomMass) {
		    this.atomMass = atomMass;
	 	}

	 	public String getValence() {
	 		return valence;
	 	}
	 	public void setValence(String valence) {
	 		this.valence = valence;
	 	}
	 	public String getFarCnst() { // Faraday's constant
	 		return farCnst;
	 	}
	    public void setFarCnst(String farCnst) { 
	    	this.farCnst = farCnst;
	    }
	    public String getDensity() {
		    return density;
		}
		public void setDensity(String density) {
		    this.density = density;
		}
		public String getThresRng() {
		    return thresRng;
		}
		public void setThresRng(String thresRng) {
		    this.thresRng = thresRng;
		}
		
		public String getStressCnst() {
		    return stressCnst;
		}
		public void setStressCnst(String stressCnst) {
		    this.stressCnst = stressCnst;
		}
		public String getPitCoeff() {
		    return pitCoeff;
		}
		public void setPitCoeff(String pitCoeff) {
		    this.pitCoeff = pitCoeff;
		}
		public String getEnthalpy() {
		    return enthalpy;
		}
		public void setEnthalpy(String enthalpy) {
		    this.enthalpy = enthalpy;
		}
		public String getGasCnst() {
		    return gasCnst;
		}
		public void setGasCnst(String gasCnst) {
		    this.gasCnst = gasCnst;
		}
		public String getTemp() {
		    return temp;
		}
		public void setTemp(String temp) {
		    this.temp = temp;
		}
		public String getFreq() {
		    return freq;
		}
		public void setFreq(String freq) {
		    this.freq = freq;
		}
		public String getFcoeffsmall() {
		    return fcoeffsmall;
		}
		public void setFcoeffsmall(String fcoeffsmall) {
		    this.fcoeffsmall = fcoeffsmall;
		}
		public String getFcoefflong() {
		    return fcoefflong;
		}
		public void setFcoefflong(String fcoefflong) {
		    this.fcoefflong = fcoefflong;
		}
		public String getFexpsmall() {
		    return fexpsmall;
		}
		public void setFexpsmall(String fexpsmall) {
		    this.fexpsmall = fexpsmall;
		}
		public String getFexplong() {
		    return fexplong;
		}
		public void setFexplong(String fexplong) {
		    this.fexplong = fexplong;
		}
		public String getGeoSmall() {
		    return geoSmall;
		}
		public void setGeoSmall(String geoSmall) {
		    this.geoSmall = geoSmall;
		}
		public String getGeoLong() {
		    return geoLong;
		}
		public void setGeoLong(String geoLong) {
		    this.geoLong = geoLong;
		}
		public String getCrackLen() {
		    return crackLen;
		}
		public void setCrackLen(String crackLen) {
		    this.crackLen = crackLen;
		}
		public String getFracTough() {
		    return fracTough;
		}
		public void setFracTough(String fracTough) {
		    this.fracTough = fracTough;
		}
		
		public String getInitiationLife(){
			double inLifePart1=0;
			double inLifePart2=0;
			double inLifePart3=0;
			double atMass= 0.0;
			double pittingcoeff = 0.0;
			double enthal =0.0;
			double sig =0.0;// sigma
			/*double enthal =0.0;
			double fatiguecoeffsm = 0.0;
			double fatiguecoefflng = 0.0;
			double geosmall =0.0;
			double tcracklen =0.0;*/
			double Cnst_C = 1.01;
			try{
				enthal = Double.parseDouble(enthalpy)*Math.pow(10, 3);
				atMass = Double.parseDouble(atomMass) * Math.pow(10,-3);
				pittingcoeff = Double.parseDouble(pitCoeff)*Math.pow(10, -2);
				sig = Double.parseDouble(sigma);
				double Ip = pittingcoeff*Math.exp((-enthal/(Double.parseDouble(gasCnst)*Double.parseDouble(temp))));
				
				
				System.out.println("IP value ::"+Ip);
				System.out.println("parsed atomMass Value:"+Double.parseDouble(atomMass));
				System.out.println("atMass:"+atMass);
				
				inLifePart1 = (2*3.14*Double.parseDouble(valence)*Double.parseDouble(farCnst)*Double.parseDouble(density)*Double.parseDouble(freq))/(3*atMass);
				inLifePart2 = Math.pow((3.14 *Math.pow((Double.parseDouble(thresRng)/(4.4*Double.parseDouble(stressCnst)*sig)), 2)),3);
				inLifePart3 = (1/Ip)*Math.pow(1/(Math.pow(Cnst_C,sig)), 3);
				inLife = inLifePart1 *inLifePart2*inLifePart3;
				System.out.println("initLife :"+inLife);
				NumberFormat formatter = new DecimalFormat("0.0000E0");
				//formatter = new DecimalFormat("###E0"); 
				initLife = formatter.format(inLife);
				}
			catch(NumberFormatException e){
				initLife ="Error";
				//e.printStackTrace();
				}
			catch(Exception e){
				initLife ="Error";
				//e.printStackTrace();
			}
			return  String.valueOf(initLife);
		}
		public String getcrackPropagation(){
			double crackPropPart1 = 0.0;
			double crackPropPart2 = 0.0;
			double apc =0.0;
			double af =0.0;
			double thresholdrng=Double.parseDouble(thresRng);
			double stressfact = Double.parseDouble(stressCnst);
			double fractoughness = Double.parseDouble(fracTough);
			double atr =Double.parseDouble(crackLen)*Math.pow(10, -3);
			double M1 = Double.parseDouble(fexpsmall);
			double M2 = Double.parseDouble(fexplong);
			double B1 = Double.parseDouble(geoSmall)/3.14;
			double B2 = Double.parseDouble(geoLong);
			double C1 = Double.parseDouble(fcoeffsmall)*Math.pow(10, -11);
			double C2 = Double.parseDouble(fcoefflong)*Math.pow(10, -11);
			double sig = Double.parseDouble(sigma);
			/*enthal = Double.parseDouble(enthalpy)*Math.pow(10, 3);
			fatiguecoeffsm = Double.parseDouble(fcoeffsmall)*Math.pow(10, -11);
			fatiguecoefflng = Double.parseDouble(fcoefflong)*Math.pow(10, -11);
			geosmall = Double.parseDouble(geoSmall)/3.14;
			tcracklen = Double.parseDouble(crackLen)*Math.pow(10, -3);*/
			
			try{
				
			apc = 3.14 * Math.pow((thresholdrng/(4.4*stressfact*sig)),2);
			af = (1/3.14)* Math.pow((fractoughness/(1.12*sig)),2);
			System.out.println("FINAL af :"+Double.parseDouble(af_final));
			if(af > Double.parseDouble(af_final)){
				System.out.println("af :::"+af);
				af= Double.parseDouble(af_final);
				System.out.println("af FINAL :::"+af);
			}
			if(apc > Double.parseDouble(apc_final)){
				System.out.println("apc :::"+apc);
				apc =  Double.parseDouble(apc_final);
				System.out.println("apc final:::"+apc);
			}
				
			crackPropPart1 = ((Math.pow(apc,(1-(M1/2)))) - (Math.pow(atr,(1-(M1/2)))))/(((M1/2)-1) * C1 * (Math.pow((2*B1*sig*(Math.pow(3.14,(1/2)))), M1)));
			crackPropPart2 = ((Math.pow(atr,(1-(M2/2)))) - (Math.pow(af,(1-(M2/2)))))/(((M2/2)-1) * C2 * (Math.pow((2*B2*sig*(Math.pow(3.14,(1/2)))), M2)));
			System.out.println("crackPropPart1:"+crackPropPart1);
			System.out.println("crackPropPart2:"+crackPropPart2);
			
			crackNp = crackPropPart1 +crackPropPart2;
			NumberFormat formatter = new DecimalFormat("0.0000E0");
			//formatter = new DecimalFormat("###E0"); 
			crackProp = formatter.format(crackNp);
			}
			catch(Exception e){
				crackProp ="Error";
				//e.printStackTrace();
			}
			return String.valueOf(crackProp);
		}
		public String getFatigueLife(){
			double fatigue =0.0;
		//	getInitiationLife();
		//	getcrackPropagation();
			try{
			System.out.println("In getFatigue:");
			System.out.println("inLife value in fatigue:"+inLife);
			System.out.println("crackNp value in fatigue:"+crackNp);
			fatigue = inLife +crackNp;
			System.out.println("fatigue value in fatigue:"+fatigue);
			
			NumberFormat formatter = new DecimalFormat("0.0000E0");
		//	formatter = new DecimalFormat("###E0"); 
			fatigueLife = formatter.format(fatigue);
			}catch(Exception e){
				fatigueLife ="Error";
			//	e.printStackTrace();
			}
			return  String.valueOf(fatigueLife);
		}
		
				
}


