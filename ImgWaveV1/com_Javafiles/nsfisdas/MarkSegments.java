package com.nsfisdas;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MarkSegments extends Frame 
{

 private int matrixR[][],matrixG[][],matrixB[][]; //used to extract the segment
 private int paintCorrodedR[][], paintCorrodedG[][], paintCorrodedB[][]; //used to mark the segment
 private int markedR[][], markedG[][], markedB[][];
 private ImageMatrix imageToExtractSegment; //used to extract the segment
 private ImageMatrix imageToPaintCorroded;  //used to mark the segment
 private ImageMatrix markedImage;
 private ImageMatrix SegmentedImage;
 private ImageMatrix OriginalImage;

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
 private String catString;
 private String filename;

 public MarkSegments(String fileName,ImageMatrix SegImgIn, ImageMatrix OrgImage, float waveMat[][], String catString )
 {
  
  super(" Isolate Segments ");
  filename = new String(fileName);
  this.catString = catString; 
  rows = SegImgIn.getRows();
  cols = SegImgIn.getCols();
  setSize( cols, rows + 100 );

  SegmentedImage = SegImgIn;
  Mat = waveMat;
  imageToExtractSegment = new ImageMatrix( SegmentedImage );
 

  matrixR = imageToExtractSegment.getRedPointer();
  matrixG = imageToExtractSegment.getGreenPointer();
  matrixB = imageToExtractSegment.getBluePointer();
 
  imageToPaintCorroded = new ImageMatrix (SegmentedImage);
  markedImage = new ImageMatrix(rows,cols);
  markedR = markedImage.getRedPointer();
  markedG = markedImage.getGreenPointer();
  markedB = markedImage.getBluePointer();

   paintCorrodedR = imageToPaintCorroded.getRedPointer();
   paintCorrodedG = imageToPaintCorroded.getGreenPointer();
   paintCorrodedB = imageToPaintCorroded.getBluePointer();
  OriginalImage = OrgImage;  

  xCoordinate = 0;
  yCoordinate = 0;
  comment = new Label(" The Marked segments are identified as corroded");
  add( comment, BorderLayout.NORTH);
  addWindowListener( new CloseWindow() );
 //  setVisible(true);
  /*ColorIndex colorIndex;
  colorIndex = new ColorIndex();*/
  Extract();
 }
 
 public void paint(Graphics g)
 {



 }
 private void Extract()
 {

  BPNet bpNetIdentify;
  bpNetIdentify = new BPNet( "Equalized-Weights-Identify-PC10-"+catString); 

  BPNet bpNetQuantify;
  bpNetQuantify = new BPNet("Equalized-Weights-Quantify-PC10-"+catString );

  float output[];
  output = new float[1];
   int xOnImage, yOnImage;
   Projection projection;
   float featArray[];  // memory allocated in Projection

  projection = new Projection(Mat, catString, filename);
  Color color;
   for(int i=0; i < rows; i++)
   {
    for(int j=0; j < cols; j++)
    {
         valueR = matrixR[i][j];
         valueG = matrixG[i][j];
         valueB = matrixB[i][j];
       
        if ( (valueR != -1) && (valueG != -1) && ( valueB != -1 ) )
          {
            markedR[i][j] = markedG[i][j] = markedB[i][j] = 0;
            
             ExtractSegmentCoords(i,j);
            if( rgbList.getNumMembers() > 10)
             {
               projection.setRGBList(rgbList);
               projection.setRowsCols(rows,cols);
               projection.formVector();
               featArray = projection.getProjectedVector();
            
               bpNetIdentify.TestNet(featArray, output);
            if( output[0] > 0.5f )
		{
	            bpNetQuantify.TestNet(featArray, output);
	           
		    PaintCorrodedSegment(i,j, output[0]);
		   }
              }
          }
     }
  
  }
   
 DisplayImage display;
 display = new DisplayImage("C:/tmp/images/MarkedSegments_"+filename,markedImage); 
 projection.closeFiles(); 
 }     // end of func 


 public ImageMatrix getOriginalImage()
 {
  return OriginalImage;
 }

 private void ExtractSegmentCoords(int x, int y)
 {
   SegStack segStack;
   segStack = new SegStack();
   Pixel pixel;
   RGB rgbValues;
   Pixel temp;
   int numNodes =0;
   int i = x;
   int j = y;
     
  rgbList = new RGBList(" Corroded Segmet ");
  while( true )      // while true loop
   {
    matrixR[i][j] = -1;
    matrixG[i][j] = -1;
    matrixB[i][j] = -1;
  
    red = OriginalImage.getRedPixel( i, j);
    blue = OriginalImage.getBluePixel( i, j);
    green = OriginalImage.getGreenPixel(i,j);

   rgbValues =  new RGB( red, green, blue);
       
	   // NO painitng here!!!

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
    // System.out.println("i: "+i);
     //System.out.println("j: "+j);
     numNodes--;
    }
    else
    {
     break;
    }
   } // end of while true loop
   // refill all the -1 to original values to that the feature can be extracted again
 } // end of func 

  private void PaintCorrodedSegment(int x, int y, float output)
 {
   SegStack segStack;
   segStack = new SegStack();
   Pixel pixel;
   RGB rgbValues;
   Pixel temp;
   int numNodes =0;
   int i =x;
   int j = y;
   Color color;
  
  while( true )      // while true loop
   {
    paintCorrodedR[i][j] = -1;
    paintCorrodedG[i][j] = -1;
    paintCorrodedB[i][j] = -1;
          
    if( i-1 >=0)
     {
      if (( paintCorrodedR[i-1][j] == valueR) && ( paintCorrodedG[i-1][j] == valueG) && ( paintCorrodedB[i-1][j] == valueB) )
      {
        pixel = new Pixel();
        pixel.setij( i-1, j);
        segStack.PushNode( pixel );
        numNodes++;
      }
     }
    if ( i+1 < rows) 
     { 
      if (( paintCorrodedR[i+1][j] == valueR) && ( paintCorrodedG[i+1][j] == valueG) && ( paintCorrodedB[i+1][j] == valueB))
      {
        pixel = new Pixel();
        pixel.setij( i+1, j);
        segStack.PushNode( pixel );
        numNodes++;
      }
     }
    if (j-1 >=0)
    {
     if ((paintCorrodedR[i][j-1] == valueR) && (paintCorrodedG[i][j-1] == valueG) && (paintCorrodedB[i][j-1] == valueB))
      {
        pixel = new Pixel();
        pixel.setij( i, j-1);
        segStack.PushNode( pixel );
        numNodes++;
      }
     }
    if (j+1 < cols)
    {
      if (( paintCorrodedR[i][j+1] == valueR) && ( paintCorrodedG[i][j+1] == valueG) && ( paintCorrodedB[i][j+1] == valueB))
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
 if( output < 0.05f ) 
 		     {
                	color = Color.blue; 
                         markedR[i][j] = color.getRed(); 
 			 markedG[i][j] = color.getGreen();
			 markedB[i][j] = color.getBlue();
 	             }
	           if( output < 0.1f && output > 0.05f)
			{
			  color = Color.green;
	                   markedR[i][j] = color.getRed(); 
      			   markedG[i][j] = color.getGreen();
			  markedB[i][j] = color.getBlue();
 			}
			   if( output < 0.15f && output > 0.1f)
				{
			         color = Color.yellow;
 				 markedR[i][j] = color.getRed(); 
      			         markedG[i][j] = color.getGreen();
			         markedB[i][j] = color.getBlue();
 			   	}
			   if( output < 0.2f && output > 0.15f)
				{
			         color = Color.magenta;
	 			 markedR[i][j] = color.getRed(); 
      			         markedG[i][j] = color.getGreen();
			         markedB[i][j] = color.getBlue();
 			   	}
			   if( output > 0.2 ) 
				{
			         color = Color.red;
				 markedR[i][j] = color.getRed(); 
      			         markedG[i][j] = color.getGreen();
			         markedB[i][j] = color.getBlue();
 			   	}
  
     numNodes--;
    }
    else
    {
     break;
    }
   } // end of while true loop
 } // end of func 
} 





