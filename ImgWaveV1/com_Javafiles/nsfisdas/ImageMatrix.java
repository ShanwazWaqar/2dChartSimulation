package com.nsfisdas;
import java.awt.*;

public class ImageMatrix
{
  private int RedPixels[][];
  private int GreenPixels[][];
  private int BluePixels[][];
  private int Rows;
  private int Cols;

  public ImageMatrix(int rows, int cols)
  {
   Rows = rows;
   Cols = cols;
   RedPixels = new int[rows][cols];
   GreenPixels = new int[rows][cols];
   BluePixels = new int[rows][cols];
  }
  public ImageMatrix()
  {}

  public ImageMatrix(ImageMatrix imgmat)
  { 
   int i,j;
   Rows = imgmat.getRows();
   Cols = imgmat.getCols();
   RedPixels = new int[Rows][Cols];
   GreenPixels = new int[Rows][Cols];
   BluePixels = new int[Rows][Cols];

   for(i = 0; i < Rows; i++)
    {
     for ( j= 0; j < Cols; j++)
      {
        RedPixels[i][j] = imgmat.RedPixels[i][j];
	    GreenPixels[i][j] = imgmat.GreenPixels[i][j];
        BluePixels[i][j] = imgmat.BluePixels[i][j];
      }
   }
 }  

 public void setPixelValue( int i, int j, int Rval, int Gval, int Bval)
 {
    RedPixels[i][j] = Rval;
    GreenPixels[i][j] = Gval;
    BluePixels[i][j] = Bval;
 }

 public int getRedPixel(int i, int j)
 {
    return RedPixels[i][j];
 }

 public int getGreenPixel(int i, int j)
 {
   return GreenPixels[i][j];
 }

 public int getBluePixel(int i, int j)
 {
  return BluePixels[i][j];
 }

 public int  getRows()
 {
   return Rows;
 }
 public int getCols()
 {
   return Cols;
 }

 public int[][] getRedPointer()
 {
  return RedPixels;
 }
 
 public void setRedPointer(int rPtr[][])
 {
  Rows = rPtr.length;
  Cols = rPtr[0].length; 
  RedPixels = rPtr;
 }

 
 public int[][] getGreenPointer()
 {
  
  return GreenPixels;
 }

 public void setGreenPointer(int gPtr[][])
 {
   
   GreenPixels = gPtr;
 }

 public int[][] getBluePointer()
 {
  return BluePixels;
 }

 public void setBluePointer(int bPtr[][])
 {
  BluePixels = bPtr;
 }
}
