package com.nsfisdas;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.awt.*;

public class DisplayImage 
{
 private String name;
 private int rows, cols;
 private int R[][];
 private int G[][];
 private int B[][];
 private BufferedImage image;
 private DisplayPixelInfo displayPixelInfo;

 public DisplayImage(String name,int red[][], int green[][], int blue[][])
 {
  //super(name);
 //  addMouseListener( this );
  // addMouseMotionListener(this);
  this.name = name;
  rows = red.length;
  cols = red[0].length;
  R = red;
  G = green;
  B = blue;
  displayPixelInfo = new DisplayPixelInfo(name);
  /*addWindowListener( new CloseWindow() );
  setSize(cols,rows);
  setVisible(true);*/
 }
 public DisplayImage(String name, ImageMatrix image)
 { 
 //  super(name);
   //addMouseListener( this );
   //addMouseMotionListener(this);
   this.name = name;
   rows = image.getRows();
   cols = image.getCols();
   R = image.getRedPointer();
   G = image.getGreenPointer();
   B = image.getBluePointer();
   displayPixelInfo = new DisplayPixelInfo(name);
  /* addWindowListener( new CloseWindow() );
   setSize(cols,rows);
   setVisible(false);*/
   getImage();
 }


 public void paint(Graphics g)
 {   
   //setSize(cols,rows);
   if ( image != null)
   {
     g.drawImage(image,0,0,null);
   }
 }

 public void mouseClicked(  MouseEvent e)
 { }

 public void mousePressed(  MouseEvent e)
  {
   int x,y;
   x = e.getX();
   y = e.getY();
   displayPixelInfo.setValues(x,y,R[y][x],G[y][x],B[y][x]);
  }
 public void mouseReleased(  MouseEvent e)
 {
   displayPixelInfo.setVisible(false);
 }
 public void mouseEntered( MouseEvent e){}
 public void mouseExited( MouseEvent e){}
 public void mouseMoved( MouseEvent e){}

 public void mouseDragged(MouseEvent e)
 {
   int x,y;
   x = e.getX();
   y = e.getY();
   displayPixelInfo.setValues(x,y,R[y][x],G[y][x],B[y][x]);
 }

 private void getImage()
  {
  Color color;
  int rgb;
  image = new BufferedImage(cols, rows, BufferedImage.TYPE_BYTE_INDEXED);
  WritableRaster raster = image.getRaster();
  ColorModel model = image.getColorModel();
  for(int i=0; i < cols; i++)
  {
   for(int j=0; j < rows; j++)
   {
 // System.out.println(" "+R[i][j]+" "+G[i][j]+" "+B[i][j]);
     color = new Color(R[j][i], G[j][i], B[j][i]);
	 rgb = color.getRGB();
     Object colorData = model.getDataElements(rgb, null);
	 raster.setDataElements(i,j,colorData);
   }
  }
  Graphics2D g2 = image.createGraphics();
  g2.drawImage(image,0,0,null);
  GifEncoder gifEncoder;
  DataOutputStream output;
  try
  { 
  String imageName = new String(""+name+".gif");
  System.out.println(" File name is "+imageName);
  output = new DataOutputStream( new FileOutputStream(imageName)); 
  gifEncoder = new GifEncoder(image);
  gifEncoder.write(output);
 
  }
  catch (IOException ex) { System.err.println(" Error call from encoder");
  //System.exit(1); 
  }
  

  // repaint();
   
  }

}
