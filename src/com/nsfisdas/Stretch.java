package com.nsfisdas;
public class Stretch
{
 private  final int MAX_GRAY = 255;
 private ImageMatrix OriginalImage;
 private ImageMatrix StretchedImage; 
 private int matrixR[][], matrixG[][], matrixB[][];
 private int rows, cols;

 public Stretch(ImageMatrix Org)
 {
  OriginalImage = Org;
  matrixR = OriginalImage.getRedPointer();
  matrixG = OriginalImage.getGreenPointer();
  matrixB = OriginalImage.getBluePointer();
  rows = OriginalImage.getRows();
  cols = OriginalImage.getCols();
  StretchedImage = new ImageMatrix(rows,cols);
 }

 public ImageMatrix getStretchedImage()
 {
  int maxR, maxG, maxB;
  int minR, minG, minB;

  minR = minG = minB = MAX_GRAY;
  maxR = maxG = maxB = 0;

  for(int i=0; i< rows; i++)
  {
   for(int j=0; j<cols; j++)
   {
    if( minR > matrixR[i][j]) minR = matrixR[i][j];
    if( minG > matrixG[i][j]) minG = matrixG[i][j];
    if( minB > matrixB[i][j]) minB = matrixB[i][j];
  
    if( maxR < matrixR[i][j]) maxR = matrixR[i][j];
    if( maxG < matrixG[i][j]) maxG = matrixG[i][j];
    if( maxB < matrixB[i][j]) maxB = matrixB[i][j];
   }
  }

  int stretchedR[][], stretchedG[][], stretchedB[][];
 
  stretchedR = StretchedImage.getRedPointer();
  stretchedG = StretchedImage.getGreenPointer();
  stretchedB = StretchedImage.getBluePointer();
  
  if(maxR == minR ) maxR++;
  if(maxG == minG ) maxG++;
  if(maxB == minB) maxB++;

  for(int i=0; i< rows; i++)
  {
   for(int j=0; j<cols; j++)
   {
    stretchedR[i][j] = ((matrixR[i][j] - minR)* MAX_GRAY)/(maxR - minR);
    stretchedG[i][j] = ((matrixG[i][j] - minG)* MAX_GRAY)/(maxG - minG);
    stretchedB[i][j] = ((matrixB[i][j] - minB)* MAX_GRAY)/(maxB - minB);
	//System.out.println(" R:"+ stretchedR[i][j]+" G:"+stretchedG[i][j]+" B:"+stretchedB[i][j]);
   }
  }
 
  return StretchedImage;
 }
}
