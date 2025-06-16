package com.nsfisdas;
class IsolateNonCorroded 
{

 private int matrixR[][], matrixG[][], matrixB[][];
 private RGBList rgbList;
 private float Mat[][];
 private int valueR, valueG, valueB;
 private int rows, cols;
 private ImageMatrix OriginalImage;

 public IsolateNonCorroded(ImageMatrix tempImage, ImageMatrix orgImage, float waveMat[][])
 {
  NoConfirm noConfirm;
  matrixR = tempImage.getRedPointer();
  matrixG = tempImage.getGreenPointer();
  matrixB = tempImage.getBluePointer();
  OriginalImage = orgImage;
  Mat = waveMat;
  rows = tempImage.getRows();
  cols = tempImage.getCols();
  
  for(int i=0; i < rows; i++)
    {
     for(int j=0; j < cols; j++)
     {
      valueR = matrixR[i][j];
      valueG = matrixG[i][j];
      valueB = matrixB[i][j];
      if ( (valueR != -1) && (valueG != -1) && ( valueB != -1 ) )
      {
       PaintCorroded(i,j);
       noConfirm = new NoConfirm(rgbList, Mat);
       noConfirm.setRowsCols(rows,cols);
       noConfirm.averageFeatures();
      }
     } 
    }
 }
private void PaintCorroded(int x, int y)
 {
   int red,green,blue;
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
  
 
 } // end of func 


}
