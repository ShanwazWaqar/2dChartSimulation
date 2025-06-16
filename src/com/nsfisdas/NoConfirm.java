package com.nsfisdas;


import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NoConfirm
{
 private final int MINMEMBERS = 6; // also in Confirm
 private final String FileName = "Coeffs";
 float Mat[][];
 RGBList rgbList;
 int rows, cols;
 int coords[]; // i * (cols) + j
 private int numFeatures,count;
 private boolean answer;
 private RandomAccessFile output;
 private float corroded, amount;

 public NoConfirm(RGBList RGBlist, float waveMat[][])
 {
  Mat = waveMat;
  numFeatures = Mat[0].length;
  rgbList = RGBlist;
 }

 public void setRowsCols(int r, int c)
 {
  rows = r;
  cols = c;
 }

 public void averageFeatures()
 {
  RGBNode firstNode;
  RGBNode lastNode;
  RGBNode ptr;
  int ctr,i,j;
  ctr =0;
  count = rgbList.getNumMembers();
  firstNode = rgbList.getFirstNode();
  lastNode = rgbList.getLastNode();
  coords = new int[count];
 
  for ( ptr = firstNode; ptr != null; ptr = ptr.getNextNode() )
  {
   i = ptr.getx(); //row
   j = ptr.gety(); //col           
   // Make sure i and j are right ...row and col respectively
   coords[ctr] = i * cols + j;  // can paint and see if you get the right shape
  }
  AverageSegment();
 }

 private void AverageSegment()
 {
 
  float aveWave[];
  aveWave = new float[numFeatures];
  openFile(); //randomaccessfile with "rw"
  corroded = 0.0f;
  amount = 0.0f;
  averageGroup(aveWave,1.0f);
  writeVector(aveWave);
 }

 private void averageGroup( float aveWave[],float percent)
 {
  int ctr =0;
  for(int i=0; i < (int) count * percent; i++)
  {
   for(int j=0; j < numFeatures; j++)
   {
    aveWave[j] += Mat[coords[(int)(Math.random() * count)]][j];
   }
   ctr++;
  }
  for(int j=0; j < numFeatures; j++)
   aveWave[j] /=ctr;

 }

 private void openFile()
 {
  try
  {
   output = new RandomAccessFile(FileName,"rw");
  }
  catch( IOException ex)
  {
   System.err.println(ex.toString() );
  }
 }

 private void writeVector(float vector[])
 {
  int vectorLength; // numfeatures + 2 (one for corrosion 1/0 and one for amount 0-1)
  int  numRows; // num of vectors written so far
  try
  {
   if (rgbList.getNumMembers() > MINMEMBERS )
   {
    if ( output.length() == 0)  // nothing on file
    {
     vectorLength = numFeatures + 2;
     numRows =0;
     output.writeInt(vectorLength);
     output.writeInt(numRows);
    }
    output.seek(0);
    vectorLength = output.readInt();
    numRows = output.readInt();
    output.seek( 8 + numRows * vectorLength * 32 );
    for(int j=0; j < numFeatures; j++)
      output.writeFloat(vector[j]);
    output.writeFloat(corroded);
    output.writeFloat(amount);
    output.seek(0);
    numRows++;
    output.writeInt(vectorLength);
    output.writeInt(numRows);
   }
   output.close();
  }
    catch(IOException ex)
  {
   System.out.println(" Excpetion"+ex.toString() );
  }
 }



}
