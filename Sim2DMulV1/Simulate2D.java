// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 9/28/2006 7:52:18 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Sim2D.java

import java.applet.Applet;

import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

public class Simulate2D extends Applet implements  Runnable
{
		public ImageMatrix2D mtrx;
	
	  //  public WriteSimFiles simfiles;
	    public TextArea feat;
	    public TextArea para;
	    public int stp;
	    Button btn_stp;
	    Panel p1;
	    Panel p2;
	    
	    public String[] PitSize;
	    public String[] PitGrowthArray;
//	    private Thread sim2d;
	    private int t_count;
	    private int s_m;
	    private int iter;
	    private double inputp1;
	    private double inputp2;
	    private double inputp3;
	    private double inputp4;
	    private double cst;
	    private int sf; 
	    private String noRows;
	    private String noColumns;
		private String param1;
		private String param2;
		private String param3;
		private String param4;
		private String constant;
		private String stressfact;
		private String noIterations;
		private String path;
	    
	    public String fileName1; // Features1.txt
	    public String fileName2; // ph,potential,concentration->Others1.txt
	    public String fileName3; //PitGrowth1.txt
	    public String fileName4; //PitSize1.txt
	    public String fileName5; //RedPixels.txt
	
	    public FileWriter fos1;
	    public FileWriter fos2;
	    public FileWriter fos3;	    
	    public FileWriter fos4;
	    public FileWriter fos5;
	 
	   
	    public String[] mtrx_stat2D ;
	    public String[] mtrx_wavelet2D;
	    public String[] mtrx_simulate;

	    static final int BUFFER = 2048;
   public Simulate2D()
    {
	   s_m = 0;
	}

    public void init()
    {
    	//deletefiles();
    	noRows = getParameter("noRows");
    	noColumns = getParameter("noColumns");
    	param1 = getParameter("param1");
    	param2 = getParameter("param2");
    	param3 = getParameter("param3");
    	param4 = getParameter("param4");
    	constant= getParameter("constant");
    	stressfact =getParameter("stressfact");
    	noIterations = getParameter("noIterations");
    	//path = getParameter("path");
    	path = "C:/tmp/";
    
    	noRows = "200";
    	noColumns = "200";
    	param1 = "3.5"; 
    	param2 = "290";
    	param3 = "0.6";
    	param4 = "0.2";
    	noIterations = "30";
    	stressfact = "50";
    	constant ="0";
     	inputp1 = Double.valueOf(param1).doubleValue();  // ph from previous program
    	inputp2 = Double.valueOf(param2).doubleValue();  //Temp from previous program
    	inputp3 = Double.valueOf(param3).doubleValue();  //pot from previous program
    	inputp4 = Double.valueOf(param4).doubleValue();  //concen from previous program
    	cst = Double.valueOf(constant).doubleValue(); // new parameter addition constant
    	sf=Integer.valueOf(stressfact).intValue();   // new parameter addition stress factor
    
    	
    	BorderLayout borderLayout = new BorderLayout(8, 8);
        setLayout(borderLayout);
        setSize(400,400);
         
    	int i = Integer.parseInt(noRows);
        int j = Integer.parseInt(noColumns);
        int scale = 400 / i;
        stp = 0;
        iter = Integer.parseInt(noIterations);
        PitSize = new String[iter];
        PitGrowthArray = new String[iter];
        
        setLayout(new BorderLayout(3, 1));
        mtrx = new ImageMatrix2D(i, j, scale,iter,path,inputp1,inputp2,inputp3,inputp4);
         
        add(mtrx);
        p1 = new Panel(new BorderLayout(2, 2));
        p1.setSize(100, getSize().height);
        //p1.add(new Label("No of Rows: "+noRows), "North");
        feat = new TextArea("", 10, 35, 1);
        feat.setEditable(false);
        add(p1, "East");
        p2 = new Panel(new BorderLayout(2, 2));
        para = new TextArea("", 10, 18, 1);
        para.setEditable(false);
        add(p2, "West");
   
        if(s_m == 0){
            mtrx.setPixel(i / 2, j / 2, 4F);
        }
        setVisible(false);
        mtrx_stat2D = new String[iter+1];
        mtrx_wavelet2D = new String[iter+1];
        mtrx_simulate = new String[iter+1];


//        added by Fang Zhou
        final SafeStopThread sim2d = new SafeStopThread();
//        sim2d = new Thread(this);
        t_count = 0;
        sim2d.start();
    }

    public void run()
    {
    	
        do
        {
             if(t_count < iter)
                {
                	System.out.println("\nIteration No::"+t_count);
            	    for(int i = 0; i < 5; i++){
                    
                        simulate(inputp1, inputp3, inputp2, inputp4 ,cst ,sf,t_count);
                      
                    }
                    repaint();                   
                    setVisible(true);
                    PitGrowthArray[t_count] =(t_count+1)+","+mtrx.getMaxRedHt()+","+mtrx.getmaxGreenHt()+","+mtrx.getmaxBlueHt();
                    PitSize[t_count] = (t_count+1)+","+mtrx.getWidthCnt()+","+mtrx.getHeightCnt();
                    
                } else
                {
                
                	  try{
                		      
                		      new File(path).mkdir();
                		      new File("C:\\tmp\\files2D\\").mkdir();
                		  //    System.out.println("Get code base:"+  path);
                		   	  fileName1 = path+"files2D\\Features1.txt";
                	          fileName2 = path+"files2D\\Others1.txt";
                	          fileName3 = path+"files2D\\PitSize1.txt";
                	          fileName4 = path+"files2D\\PitGrowthRate1.txt";
                	          fileName5 = path+"RedPixels.txt";
		         			  fos1 = new FileWriter(fileName1);
		         			  fos2 = new FileWriter(fileName2);
		         			  fos3 = new FileWriter(fileName3);
		         			  fos4 = new FileWriter(fileName4);
		         			  fos5 = new FileWriter(fileName5);
		         		
		         			  
		         			  fos1.write("Skew,Energy,Entropy,Ratio1,Skew,Energy,Entropy,Ratio2\n");
		         			  fos2.write("pH,potential,concentration: \n");
		         			  
		         			  for (int i=0;i<t_count;i++){
		         				  fos1.write(mtrx_stat2D[i] + mtrx_wavelet2D[i] + "\n");
		         				  fos2.write(mtrx_simulate[i] + "\n");
		         			  }
		         			  
		         			  fos3.write("Iteration,Width,Height\n");
		         			  fos4.write("Iteration,RedRate,GreenRate,BlueRate\n");
		         			
		         			 String[] writePitSize = PitSize;
		         			  for(int j = 0;j<writePitSize.length;j++){
		         			//	  System.out.println("writePitSize["+j+"]="+writePitSize[j]);
		         				 fos3.write(writePitSize[j] + "\n");
		         			  }
		         			  String[] writePitGrowth = PitGrowthArray;
		         			  for(int i = 0;i<writePitGrowth.length;i++){
		         			//	  System.out.println("writePitGrowth["+i+"]="+writePitGrowth[i]);
		         				 fos4.write(writePitGrowth[i] + "\n");
		         			  }
		         			  fos5.write(mtrx.getRedPixels() + "\n");
		         			  
	                          fos1.close();
	                          fos2.close();
	                          fos3.close();
	                          fos4.close();
	                          fos5.close();
	                          
	                          try {
	                        	  
	                        	  
	                              BufferedInputStream origin = null;
	                              File f = new File(path+"files2D/.");
	                              String files[] = f.list();
	                              FileOutputStream dest = new 
	                                FileOutputStream(path+"files2D/sim2DFiles.zip");
	                              ZipOutputStream out = new ZipOutputStream(new 
	                                BufferedOutputStream(dest));
	                              //out.setMethod(ZipOutputStream.DEFLATED);
	                              byte data[] = new byte[BUFFER];
	                              // get a list of files from current directory
	                       

	                              for (int i=0; i<files.length; i++) {
	                              //   System.out.println("Adding: "+path+"files/"+files[i]);
	                                FileInputStream fi = new 
	                                   FileInputStream(path+"files2D/"+files[i]);
	                                 origin = new 
	                                   BufferedInputStream(fi, BUFFER);
	                                 ZipEntry entry = new ZipEntry(files[i]);
	                                 out.putNextEntry(entry);
	                                 int count;
	                                 while((count = origin.read(data, 0, 
	                                   BUFFER)) != -1) {
	                                    out.write(data, 0, count);
	                                 }
	                                 origin.close();
	                              }
	                              out.close();
	                           } catch(Exception e) {
	                              e.printStackTrace();
	                           }
	                      
                             	  	
                      	  }catch (Exception e){//Catch exception if any
                          	  System.err.println("Error: " + e.getMessage());
                          }

					final SafeStopThread sim2d = new SafeStopThread();  //added by Fang Zhou
                	System.out.print("stoppp");
//                	sim2d.stop();
                    sim2d.terminate();
                  
                 
                }
            
            try
            {
                Simulate2D _tmp = this;
                Thread.sleep(400L);
                //break;
                
               
            }
            catch(InterruptedException interruptedexception) { }
            t_count++;   
        } while(true);
      
    }
   
    final double DEFAULT_PH = 3.5D;
    final double DEFAULT_POT = 0.59999999999999998D;
    final double DEFAULT_TEMP = 290D;
    final double DEFAULT_CONCEN = 0.20000000000000001D;
    final int DEFAULT_STEP = 5;
    final int TIME_STEP = 800;
    final int DEFAULT_ROWS[] = {
        100, 300
    };
    final int DEFAULT_COLS[] = {
        100, 400
    };
    final int DEFAULT_ITER[] = {
        30, 40
    };
    

    public void paint(Graphics g)
    {
        mtrx.repaint();
        mtrx_stat2D[t_count] =mtrx.stat2D() ;
        mtrx_wavelet2D[t_count] =mtrx.wavelet2D(6);
     
    }

 
    public void simulate(double d, double d1, double d2, double d3, double cs, int sf, int itrNo)
    {
     
      mtrx_simulate[t_count] = mtrx.simulate(d, d1, d2, d3, 0.5D, 3D ,cs ,sf,itrNo);
    }

//Added by Fang Zhou, 2020
	class SafeStopThread extends Thread{
    	private volatile boolean stop = false;

	@Override
	public void run(){
		while (stop){
			System.out.println("Stop");
		}
	}
	public void terminate(){
		stop = true;
	}
}
}