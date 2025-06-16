package com.nsfisdas;
import java.io.*;
import java.awt.*;
import java.awt.image.*;

import org.apache.catalina.User;

public class HistoEqualizer 
{
 private  final int MAX_GRAY = 256;
 private ImageMatrix OriginalImage;
 private ImageMatrix EqualizedImage; 
 private int matrixR[][], matrixG[][], matrixB[][];
 private float HistoR[], HistoG[], HistoB[];  // numbers could be large
 private int MapR[], MapG[], MapB[];
 private int rows, cols;
 private String fileName;
 private RandomAccessFile histoFile;
 private int eqR[][], eqG[][], eqB[][];
 private BufferedImage image;


 public HistoEqualizer(ImageMatrix Org, String type)
 {
   OriginalImage = Org;
  fileName = "HistoMap-" + type;
  HistoR = new float[MAX_GRAY];
  HistoG = new float[MAX_GRAY];
  HistoB = new float[MAX_GRAY];
  
  matrixR = OriginalImage.getRedPointer();
  matrixG = OriginalImage.getGreenPointer();
  matrixB = OriginalImage.getBluePointer();
  rows = OriginalImage.getRows();
  cols = OriginalImage.getCols();
  EqualizedImage = new ImageMatrix(rows,cols);
 }
/*

 public void paint( Graphics g)
 {
   if ( image != null)
   {
    setVisible(true);
    setSize(cols,rows);
	g.drawImage(image,0,0,null);
	}
 }

*/

 // only during training histogram will be modified.
 // map is generated while testing
 public void trainHistoMap()  // only the histogram is modified
 {
  openFile();
  updateHistogram();
  closeFile();
 }

 
 public ImageMatrix testHistoMap()
 {
  openFile();
  readHistogram(); // of the trained images
  formRatios(); 
  // now do the mapping

  eqR = EqualizedImage.getRedPointer();
  eqG = EqualizedImage.getGreenPointer();
  eqB = EqualizedImage.getBluePointer();

  for(int i=0; i<rows; i++)
  {
   for(int j=0; j<cols; j++)
   {
    eqR[i][j] = MapR[matrixR[i][j]];
    eqG[i][j] = MapG[matrixG[i][j]];
    eqB[i][j] = MapB[matrixB[i][j]];
   }
  }
  closeFile();
  //convertToGIF();
  return EqualizedImage;
 }

 private void convertToGIF()
 {
  GifEncoder gifEncoder;
  Color color;
  int rgb;
  image = new BufferedImage(cols, rows, BufferedImage.TYPE_BYTE_INDEXED);
  WritableRaster raster = image.getRaster();
  ColorModel model = image.getColorModel();

  for(int i=0; i < cols; i++)
  {
   for(int j=0; j < rows; j++)
   {
     color = new Color(eqR[j][i], eqG[j][i], eqB[j][i]);
	 rgb = color.getRGB();
     Object colorData = model.getDataElements(rgb, null);
	 raster.setDataElements(i,j,colorData);
   }
  }
  Graphics2D g2 = image.createGraphics();
   g2.drawImage(image,0,0,null);
  DataOutputStream output;
  try
  { 
  output = new DataOutputStream( new FileOutputStream("Equal94.gif")); 
  gifEncoder = new GifEncoder(image);
  gifEncoder.write(output);
  }
  catch (IOException ex) { System.err.println(" Error call from encoder");
  //System.exit(1); 
  }
 }



 private void formRatios()
 {
  float totalPixels = 0.0f;  // a large integer
  float ratioR[], ratioG[], ratioB[];

  ratioR = new float[MAX_GRAY];
  ratioG = new float[MAX_GRAY];
  ratioB = new float[MAX_GRAY];

  MapR = new int[MAX_GRAY];
  MapG = new int[MAX_GRAY];
  MapB = new int[MAX_GRAY];
  
  for(int i=0; i < MAX_GRAY; i++)
  {
    totalPixels += HistoR[i];  //green or blue can also be used instead
  }
 // System.out.println(" Total pixels so far "+totalPixels);
  ratioR[0] = HistoR[0];
  ratioG[0] = HistoG[0];
  ratioB[0] = HistoB[0];

  for(int i=1; i <MAX_GRAY; i++) //cummulative
  {
   ratioR[i] = HistoR[i] + ratioR[i-1];
   ratioG[i] = HistoG[i] + ratioG[i-1];
   ratioB[i] = HistoB[i] + ratioB[i-1];
  }

  for(int i=1; i <MAX_GRAY; i++) //divide cummulative by total and round
  {
   MapR[i] = (int)Math.floor((ratioR[i]*(MAX_GRAY-1))/totalPixels + 0.5);
   MapG[i] = (int)Math.floor((ratioG[i]*(MAX_GRAY-1))/totalPixels + 0.5);
   MapB[i] = (int)Math.floor((ratioB[i]*(MAX_GRAY-1))/totalPixels + 0.5);
  
  }

 }
  
 private void updateHistogram()
 {
  // open the file and read previous histos into HistoR, HistoG, HistoB
  // create file if it does not exist.
  // structure of the file..
  
  // MAX_GRAY float value representing the number of pixels with each intensity
 try
  {
    for(int i=0; i < MAX_GRAY; i++)
	{
	 HistoR[i] = histoFile.readFloat();
	 HistoG[i] = histoFile.readFloat();
	 HistoB[i] = histoFile.readFloat();
	}
  }
  catch( EOFException eofReached)
  {
     System.err.println(" Trying to read for the first time.. filling with zeros");
     for(int i=0; i < MAX_GRAY; i++)
	  {
	   HistoR[i] = 0.0f;
	   HistoG[i] = 0.0f;
	   HistoB[i] = 0.0f;
	  }
  }
  catch( IOException ex)
  {
   System.err.println(" IOException during histo read..exiting");
   //System.exit(1);
  }

  // now update Histo
 System.out.println(" "+HistoR[0]+ " "+HistoG[0]+" "+HistoB[0]);
  for(int i=0; i <rows; i++)
  {
   for(int j=0; j < cols; j++)
   {
    HistoR[ matrixR[i][j]]++;
    HistoG[ matrixG[i][j]]++;
    HistoB[ matrixB[i][j]]++;
   }
  }
 System.out.println(" "+HistoR[0]+ " "+HistoG[0]+" "+HistoB[0]);
 

 // write the histogram on to the file
  try
  {
   // first rewind the file
   histoFile.seek(0);
   for(int i=0; i < MAX_GRAY; i++)
    {
	 histoFile.writeFloat(HistoR[i]);
	 histoFile.writeFloat(HistoG[i]);
	 histoFile.writeFloat(HistoB[i]);
	}
  }
  catch(IOException ex)
  {
   System.err.println(" Error during writing histo file..exiting");
  // System.exit(1);
  }
 }

 private void readHistogram()
 {

  try
  {
    for(int i=0; i < MAX_GRAY; i++)
	{
	 HistoR[i] = histoFile.readFloat();
	 HistoG[i] = histoFile.readFloat();
	 HistoB[i] = histoFile.readFloat();
	}
  }
 catch( IOException ex)
  {
   System.err.println(" IOException during histo read..trouble!!!!....exiting.");
   //System.exit(1);
  }
 }
// functions to open and close histo file

 private void openFile()
 {
  try
   {
	  String filename1;
	 filename1 = "C:/tmp/readfiles/"+fileName;
    histoFile = new RandomAccessFile( filename1, "rw");
   }
   catch ( IOException ex)
   {
    System.err.println(" File not opened properly :"+ex.toString() );
//	System.exit(1);
   }
 }

 private void closeFile()
 {
  try
   {
    histoFile.close();
   }
   catch( IOException ex)
   {
    System.err.println(" Error closing file "+ex.toString() );
//	System.exit(1);
   }
 }

}
