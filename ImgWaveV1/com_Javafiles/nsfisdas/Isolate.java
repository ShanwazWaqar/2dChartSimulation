package com.nsfisdas;
import java.awt.*;
import java.awt.event.*;

public class Isolate extends Frame implements MouseListener
{
 private int matrixR[][],matrixG[][],matrixB[][];
 private int noChangeR[][], noChangeG[][], noChangeB[][];

 private ImageMatrix TempImage;
 private ImageMatrix SegmentedImage;
 private ImageMatrix OriginalImage;
 private ImageMatrix NoChangeImage;
 private int rows, cols;

 private int red, green, blue;

 private int xCoordinate, yCoordinate;

 private boolean painted;
 private int valueR,valueG,valueB;

 private Label comment;
 private boolean Clicked;
 private boolean confirmDisplayed;
 private RGBList rgbList;
 private float Mat[][];

 public Isolate( ImageMatrix SegImgIn, ImageMatrix OrgImage, float waveMat[][])
 {
  // filename for this category should also be sent with the constructor
  // trainfile is set by a function.

  super(" Isolate Segments ");
 
  addMouseListener( this );
 
  rows = SegImgIn.getRows();
  cols = SegImgIn.getCols();
  setSize( cols + 100, rows + 100 );

  SegmentedImage = SegImgIn;
  Mat = waveMat;
  TempImage = new ImageMatrix( SegmentedImage );
  NoChangeImage = new ImageMatrix (SegmentedImage);

  matrixR = TempImage.getRedPointer();
  matrixG = TempImage.getGreenPointer();
  matrixB = TempImage.getBluePointer();
   
   noChangeR = NoChangeImage.getRedPointer();
   noChangeG = NoChangeImage.getGreenPointer();
   noChangeB = NoChangeImage.getBluePointer();

  OriginalImage = OrgImage;  

  xCoordinate = 0;
  yCoordinate = 0;

  confirmDisplayed = false;
  Clicked = false;
  comment = new Label(" Click on the a segment to extract features");
  add( comment, BorderLayout.NORTH);
  addWindowListener( new CloseWindow() );
  setVisible(true);
 }
 
 public void paint(Graphics g)
 {
   Confirm confirmFrame;

   int xOnImage, yOnImage;
    Pixel clickedonPixel;
    //ToFile rgbToFile;
    double area, redMean, greenMean,blueMean;
    double redSTD,greenSTD,blueSTD;
      
   for (int i = 0; i < rows ; i++ )
    {
     for( int j = 0; j < cols ; j++ ) 
     {
      red = SegmentedImage.getRedPixel(i,j);
      green = SegmentedImage.getGreenPixel(i,j);
      blue = SegmentedImage.getBluePixel(i,j); 
      g.setColor( new Color( red, green, blue));
      g.drawLine( 40+j , 60+i, 40+j , 60+i);
     }
    }
   if ( Clicked )
   {
    if ( (xCoordinate >= 40 ) && ( xCoordinate <= 40 + cols-1 ) )
    { 
      if ( (yCoordinate >= 60) && ( yCoordinate <= 60 + rows-1))
       {
         valueR = matrixR[yCoordinate-60][xCoordinate-40];
         valueG = matrixG[yCoordinate-60][xCoordinate-40];
         valueB = matrixB[yCoordinate-60][xCoordinate-40];

        
        if ( (valueR != -1) && (valueG != -1) && ( valueB != -1 ) )
          {
             g.setColor(Color.red); //new Color(255,0,0));
             PaintCorroded(g, yCoordinate - 60, xCoordinate-40);
             clickedonPixel = new Pixel();
             clickedonPixel.setij( yCoordinate - 60, xCoordinate - 40);
             if ( !confirmDisplayed )
             {
	          confirmFrame = new Confirm(this,rgbList, Mat);
		      confirmFrame.setRowsCols(rows,cols);
		      confirmFrame.formCoords();
              confirmFrame.setVisible( true );
              confirmDisplayed = true;
             } 
         }
       }  // coordinate
    }    // coordinate
   }    // for click
 }     // end of func 

 public ImageMatrix getTempImage()
 {
  return TempImage;
 }

 public ImageMatrix getOriginalImage()
 {
  return OriginalImage;
 }

 public void mouseClicked(  MouseEvent e)
 {
   Clicked = true;
   confirmDisplayed = false;
   xCoordinate = e.getX();
   yCoordinate = e.getY();
   repaint();
 }

 public void mousePressed( MouseEvent e){}
 public void mouseReleased( MouseEvent e){}
 public void mouseEntered( MouseEvent e){}
 public void mouseExited( MouseEvent e){}

 private void PaintCorroded( Graphics gHandler,int x, int y)
 {
   SegStack segStack;
   segStack = new SegStack();
   Pixel pixel;
   RGB rgbValues;
   Pixel temp;
   int numNodes =0;
   int i =x;
   int j = y;
     
   rgbList = new RGBList(" Corroded Segmet ");
  
  while( true )      // while true loop
   {
    matrixR[i][j] = -1;
    matrixG[i][j] = -1;
    matrixB[i][j] = -1;
    gHandler.drawLine( 40+j , 60+i, 40+j , 60+i);
    red = OriginalImage.getRedPixel( i, j);
    blue = OriginalImage.getBluePixel( i, j);
    green = OriginalImage.getGreenPixel(i,j);

   rgbValues =  new RGB( red, green, blue);
 

  rgbList.PushNode( i, j, rgbValues );
      
    if( i-1 >=0)
     {
      if (( matrixR[i-1][j] == valueR) && ( matrixG[i-1][j] == valueG) && ( matrixB[i-1][j] == valueB) )
      {
        pixel = new Pixel();
        pixel.setij( i-1, j);
        segStack.PushNode( pixel );
        numNodes++;
      }
     }
    if ( i+1 < rows) 
     { 
      if (( matrixR[i+1][j] == valueR) && ( matrixG[i+1][j] == valueG) && ( matrixB[i+1][j] == valueB))
      {
        pixel = new Pixel();
        pixel.setij( i+1, j);
        segStack.PushNode( pixel );
        numNodes++;
      }
     }
    if (j-1 >=0)
    {
     if ((matrixR[i][j-1] == valueR) && (matrixG[i][j-1] == valueG) && (matrixB[i][j-1] == valueB))
      {
        pixel = new Pixel();
        pixel.setij( i, j-1);
        segStack.PushNode( pixel );
        numNodes++;
      }
     }
    if (j+1 < cols)
    {
      if (( matrixR[i][j+1] == valueR) && ( matrixG[i][j+1] == valueG) && ( matrixB[i][j+1] == valueB))
      {
       pixel = new Pixel();
       pixel.setij( i, j+1);
       segStack.PushNode( pixel );
       numNodes++;
      }
     }
    if ( numNodes > 0 )
    {
     temp = segStack.PopNode();
     i = temp.geti();
     j = temp.getj();
     numNodes--;
    }
    else
    {
     break;
    }
   } // end of while true loop
   // refill all the -1 to original values to that the feature can be extracted again
  
  for(i=0; i < rows; i++)
  {
   for(j=0; j < cols; j++)
    {
	  matrixR[i][j] = noChangeR[i][j];
	  matrixG[i][j] = noChangeG[i][j];
	  matrixB[i][j] = noChangeB[i][j];
	}
   }
 } // end of func 
} 





