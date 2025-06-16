package com.nsfisdas;

import javax.swing.*;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.awt.image.*;

public class TestFrame extends Frame
{
	private String fileName;
	private BufferedReader input; 
	private PpmReader ppmhandler;
	private int height, width;
	private ImageMatrix OriginalImage;
	private ImageMatrix SegmentedImage;
	private BufferedImage image;
	private MarkSegments markSegments;
	private String catString;
	private float waveMat[][];
	private Image img;
	public URL path;
	public String str_path;
	MediaTracker mt;
	private Thread imgTest;
	public TextArea feat;
	public TextArea para;
	Panel p1;
	Panel p2;
	public DisplayImage imgdisp;
	public TestFrame(String strpath) 
	{
			
		img = null;

		//str_path ="file:/C:/tmp/images/";
		str_path =strpath;
		try {
			path = new URL(str_path);
			System.out.println("path:"+path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("In init");

	/*	BorderLayout borderLayout = new BorderLayout(8, 8);
		setLayout(borderLayout);
		//  setBackground(Color.black);
		setSize(400,400);
		setLayout(new BorderLayout(3, 1));
		
		p1 = new Panel(new BorderLayout(2, 2));
		p1.setSize(100, getSize().height);
		//p1.add(new Label("No of Rows: "+noRows), "North");
		feat = new TextArea("", 10, 35, 1);
		feat.setEditable(false);
		add(p1, "East");
		p2 = new Panel(new BorderLayout(2, 2));
		para = new TextArea("", 10, 18, 1);
		para.setEditable(false);
		add(p2, "West");;
		setVisible(false);*/
		
		catString = "EC-04k";
		this.catString = new String(catString);
		fileName = "imgupload.JPG";
		//fileName = "9";
		OriginalImage = new ImageMatrix();
		openFile();
		//  Stretch stretch;
		// find the category of the image if not given

		ImageMatrix stretchedImage, equalizedImage;
		DisplayImage display;
		//stretch = new Stretch(OriginalImage);
		// stretchedImage = stretch.getStretchedImage();
		HistoEqualizer histoEqualize;

		// Image type is hard coded for now.
	
		histoEqualize = new HistoEqualizer(OriginalImage,catString);  
		// histoEqualize.trainHistoMap();
		equalizedImage = histoEqualize.testHistoMap();
		display = new DisplayImage("C:/tmp/images/Original_"+fileName,OriginalImage);
		//display = new DisplayImage("Stretched Image",stretchedImage);
		display = new DisplayImage("C:/tmp/images/Equalized_"+fileName, equalizedImage);

		WTransform1 wTransform = new WTransform1(equalizedImage); //OriginalImage);

		wTransform.Segment();
		SegmentedImage = wTransform.getSegmentedImage();
		display = new DisplayImage("C:/tmp/images/Segmented_"+fileName, SegmentedImage);
		waveMat = wTransform.getMatrix();

		//display = new DisplayImage("Features_"+fileName, waveMat);
		//System.out.println(waveMat);
		
		try{
			markSegments= new MarkSegments(fileName,SegmentedImage,OriginalImage, waveMat, catString);
		}
		finally{
			System.out.println("Finally");
		//	loadImage();
		//	repaint();
			// System.exit(0);
		}
		//mt = new MediaTracker(this);
		/*imgTest = new Thread(this);
		imgTest.start();*/

	}
	public void loadImage()
	{
	//	img = getImage(path, "MarkedSegments_imgupload1.JPG.gif");


	}
	/*public void paint(Graphics g)
	{
		// mtrx.repaint();
		System.out.println("In paint"+img);
		if(img!=null){
			g.drawImage(img, 0, 0, this);
		}
		else{
			g.drawString("In progress", 10, 10);
		}
	}*/

	private void openFile()
	{
		URL url;
		String location;
		// location = "D:\\gifs\\"+fileName.trim();
		// location = "C:\\Research\\Images\\gifs\\" +fileName.trim(); 
		// location = fileName.trim();
		//C:\jswdk-1.0.1\home\msdnsf\surya\gifs
		//location = "C:/xampp/htdocs/isdas/appimages/thumbs/"+fileName.trim();
		location = "C:/tmp/images/"+fileName.trim();


		Image loadedImage = Toolkit.getDefaultToolkit().getImage(location);
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(loadedImage, 0);
		try { tracker.waitForID(0); }
		catch (InterruptedException e) {System.out.println(" Exception ");}
		width = loadedImage.getWidth(null);
		height = loadedImage.getHeight(null);

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		g2.drawImage(loadedImage,0,0,null);
		//repaint();

		ColorModel model = image.getColorModel();
		Raster raster = image.getRaster();
		int numDataElements = raster.getNumDataElements();

		int TempR[][], TempG[][], TempB[][];
		TempR  = new int [height][width];
		TempG  = new int [height][width];
		TempB = new int [height][width];
		for(int i=0; i < height; i++)
		{
			for(int j=0; j < width; j++)
			{
				Object data = raster.getDataElements(j,i,null);
				TempR[i][j] = model.getRed(data);
				TempG[i][j] = model.getGreen(data);
				TempB[i][j] = model.getBlue(data);

			}
		}
		OriginalImage.setRedPointer(TempR);
		OriginalImage.setGreenPointer(TempG);
		OriginalImage.setBluePointer(TempB);

		//img = getImage(path, "imgupload.JPG");



	}
	

	/* public static void main(String args[]) {

	TestFrame tframe = new TestFrame();     
	  tframe.setVisible(false);


  }*/
	/* public static String TestFrameJava()
 {
	 TestFrame tframe = new TestFrame("t0.JPG","EC-04k");     
	 tframe.setVisible(true);
	 return "COmplete";

 }*/
}


