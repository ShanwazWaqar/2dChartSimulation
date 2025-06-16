import java.applet.Applet;



import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.nsfisdas.TestFrame;

public class ImageAnalysis extends Applet implements Runnable
{
	private Image img;
	public ImageAnalysisMatrix mtrx;
	public TestFrame tframe;
	public static  String IMG = null;

	public TextArea feat;
	public TextArea para;
	public int stp;
	Button btn_stp;
	Panel p1;
	Panel p2;

	public String[] PitSize;
	public String[] PitGrowthArray;

	// SimFrame sf;
	private Thread imgAnalysis;
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

	public String fileName1; // Features.txt
	public String fileName2; // ph,potential,concentration->Otheres.txt
	public String fileName3; //PitGrowth.txt
	public String fileName4; //PitSize.txt

	public FileWriter fos1;
	public FileWriter fos2;
	public FileWriter fos3;
	public FileWriter fos4;


	public String[] mtrx_stat2D ;
	public String[] mtrx_wavelet2D;
	public String[] mtrx_simulate;
	public URL path;
	public String str_path;
	public String img_path;
	public URL imagepath;
	public ImageAnalysis()
	{
		s_m = 0;
	}

	public void init()
	{
		img = null;
		//str_path = "file:/"+getParameter("path");
		System.out.println("Get Code Base: "+getCodeBase());
		str_path =getCodeBase()+"images/";
		img_path ="file:/C:/tmp/images/";
		
		try {
			imagepath  =new URL(img_path);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//str_path ="file:/C:/tmp/images/";
		
		System.out.println("New str_path:"+str_path);
		//deletefiles();
		//noRows = getParameter("noRows");
		//noColumns = getParameter("noColumns");

		//noIterations = getParameter("noIterations");
		// System.out.println("Path java:"+str_path+"images/");
		try {
			System.out.println("3-------------");
			System.getProperty("catalina.base");
			
			path = new URL(str_path);
			System.out.println("SYstem cataline base:"+System.getProperty("catalina.base"));
			System.out.println("SYstem user home:"+System.getProperty("user.dir"));
			System.out.println("path:"+path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//  System.out.println("getDocumentBase:"+getDocumentBase());
		noRows = "100";
		noColumns = "100";
		noIterations = "30";

		//System.out.println("\nconstant:"+constant);
		BorderLayout borderLayout = new BorderLayout(8, 8);
		setLayout(borderLayout);
		//  setBackground(Color.black);
		setSize(400,400);

		int i = Integer.parseInt(noRows);
		int j = Integer.parseInt(noColumns);
		int scale = 400 / i;
		stp = 0;
		iter = Integer.parseInt(noIterations);
		PitSize = new String[iter];
		PitGrowthArray = new String[iter];
		setLayout(new BorderLayout(3, 1));
		mtrx = new ImageAnalysisMatrix(i, j, scale,img);

		//  add(mtrx);
		p1 = new Panel(new BorderLayout(2, 2));
		p1.setSize(100, getSize().height);
		//p1.add(new Label("No of Rows: "+noRows), "North");
		feat = new TextArea("", 10, 35, 1);
		feat.setEditable(false);
		//  p1.add(feat, "Center");
		add(p1, "East");
		p2 = new Panel(new BorderLayout(2, 2));
		// p2.add(new Label("pH, potential, concentration: "), "North");
		para = new TextArea("", 10, 18, 1);
		para.setEditable(false);
		// p2.add(para, "Center");
		add(p2, "West");
		//pack();
		//  btn_stp = new Button("Stop/Resume");

		//  sf = new SimFrame(i, j, scale);

		// Create file 

		if(s_m == 0){
			mtrx.setPixel(i / 2, j / 2, 4F);
		}
		setVisible(false);


		mtrx_stat2D = new String[iter+1];
		mtrx_wavelet2D = new String[iter+1];


		imgAnalysis = new Thread(this);
		t_count = 1;
		imgAnalysis.start();
	}

	public void run()
	{
		try{
			getPixelsData();
			 tframe = new TestFrame(str_path);
			 loadImage();
			 repaint();
			// tframe.setVisible(false);
		// commented by bhavana on 16 July
			/*new File("C:\\tmp\\").mkdir();
			new File("C:\\tmp\\images\\").mkdir();
			fileName1 = "C:/tmp/images/Features.txt";*/
			 //fileName1 = str_path+"/Features.txt";
			 System.out.println("No path added");
			 fileName1 = "Features.txt";
			 
			fos1 = new FileWriter(fileName1);


			fos1.write("Skew,Energy,Entropy,Ratio1,Skew,Energy,Entropy,Ratio2\n");

			fos1.write(mtrx_stat2D[t_count] + mtrx_wavelet2D[t_count] + "\n");

			fos1.close();

			

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		finally{
			
		}
		
		System.out.print("stoppp");
		imgAnalysis.stop();
}

	public void getPixelsData(){
		 System.out.println("In pixel data");
		BufferedImage image;
	
		try
		{
			//fileName2 = "C:/tmp/images/pixels.txt";
			fileName2 = str_path+"/pixels.txt";
			
			fos2 = new FileWriter(fileName2);
			//IMG = "C:/tmp/images/imgupload.JPG";
			IMG = str_path+"/imgupload.JPG";
			
			image = ImageIO.read(new File(IMG));
			//   System.out.println("Height:"+image.getHeight()+"\t Width:"+image.getWidth());
			float[][] pixelData = new float[100][100];
			int[] rgb;
			double rgbpix1;
			double rgbpix2;
			double rgbpix3;
			

			//int counter = 0;
			for(int i = 0; i < 100; i++){
				for(int j = 0; j < 100; j++){
					rgb = getPixelData(image, i, j);

					//  System.out.println("rgb: ["+i+"]["+j+"]:" + rgb[0] + " " + rgb[1] + " " + rgb[2]);
					

					pixelData[i][j]= getMaxValue(rgb);
				   /*  rgbpix1 = jetR(rgb[0]);
				     rgbpix2 =  jetG(rgb[1]);
				     rgbpix3 =  jetB(rgb[2]);
				     fos2.write(rgbpix1 +","+rgbpix1 +","+rgbpix3+ "\n");*/
					 fos2.write(pixelData[i][j]+ "\n");
				     
					    //System.out.println("pixeldata["+i+"]["+j+"]:"+pixelData[i][j]);
				//	    System.out.println("rgbpix1:"+rgbpix1 +"\trgbpix2:"+rgbpix2+"\trgbpix3:"+rgbpix3);
					/* for(int k = 0; k < rgb.length; k++){
                    pixelData[counter][k] = rgb[k];
                 //  System.out.println("pixeldata["+counter+"]["+k+"]:"+pixelData[counter][k]);

                }*/

					//counter++;
				}
			}
			fos2.close();
			mtrx_stat2D[t_count] =mtrx.stat2D(pixelData) ;
			mtrx_wavelet2D[t_count] =mtrx.wavelet2D(6,pixelData);
		}
		catch(Exception e) { e.printStackTrace();}


	}
	public int getMaxValue(int[] numbers){
		int maxValue = numbers[0];
		for(int i=1;i < numbers.length;i++){
			if(numbers[i] > maxValue){
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}

	public void paint(Graphics g)
	{
		// mtrx.repaint();
		//System.out.println("In paint"+img);
		
		if(img!=null){
			g.drawImage(img, 0, 0, this);
		}else{
			g.drawString("In progress , please wait", 10, 10);
		}
	}
	
	public void simulate(int itrNo)
	{

		mtrx.simulate(itrNo);
	}



	public void loadImage()
	{
				img = getImage(imagepath, "MarkedSegments_imgupload.JPG.gif");
		
	}
	public int[] getPixelData(BufferedImage img, int x, int y) {
		int argb = img.getRGB(x, y);
																																																																																																						
		int rgb[] = new int[] {
				(argb >> 16) & 0xff, //red
				(argb >>  8) & 0xff, //green
				(argb      ) & 0xff  //blue
		};

	//	System.out.println("rgb: " + rgb[0] + " " + rgb[1] + " " + rgb[2]);
		return rgb;
	}


}