package com.nsfisdas;
// TrainFrame
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.awt.image.*;
import java.util.*;

public class TrainFrame extends Frame
{
 private String fileName;
 private BufferedReader input; 
 private PpmReader ppmhandler;
 private int height, width;
 private ImageMatrix OriginalImage;
 private ImageMatrix SegmentedImage;
 private BufferedImage image;
 private Isolate isolateSegments;
 private float waveMat[][];

 public TrainFrame(String file, String catString) 
 {
  
  fileName = file;
  OriginalImage = new ImageMatrix();
  openFile();
  Stretch stretch;
  ImageMatrix stretchedImage, equalizedImage;
  DisplayImage display;

  HistoEqualizer histoEqualize;

  // Image type is hard coded for now.
// stretch = new Stretch(OriginalImage);
  //System.out.println(" displaying stretched image");
// stretchedImage = stretch.getStretchedImage();
 //display = new DisplayImage("Stretched-Image",stretchedImage);


 histoEqualize = new HistoEqualizer(OriginalImage, catString);  

//  histoEqualize.trainHistoMap();                 // this should not be a coment!!!!
 equalizedImage = histoEqualize.testHistoMap();

// display = new DisplayImage("Original-Image "+fileName,OriginalImage);

// display = new DisplayImage("Equalized-Image"+fileName, equalizedImage);

 // if image is gray can perform w2 transforms else 1. (just an idea now, not implemented )

  WTransform1 wTransform = new WTransform1(equalizedImage); //);OriginalImage
  wTransform.Segment();
 SegmentedImage = wTransform.getSegmentedImage();
 //display = new DisplayImage("Segmented-Image"+fileName, SegmentedImage);
 waveMat = wTransform.getMatrix();
 isolateSegments = new Isolate(SegmentedImage,OriginalImage, waveMat);


 }

 private void openFile()
 {
  URL url;
  String location;
//  location = "C:\\Research\\Images\\gifs\\" +fileName.trim(); 
  location = "/home/msdnsf/surya/gifs/"+fileName.trim();
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
  repaint();

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
  
 }

}


