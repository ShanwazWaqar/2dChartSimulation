package com.nsfisdas;


import java.io.*;
import java.util.*;

public class BP
{
 private final int m = 15;  // num of rows on PCs
 private int rows;
 private final int NUM_PCA_COMP = 10;
 //private final String PCFile = "Equalized-PC";
 private String PCFile = "Equalized-PC10-W1-";
 //private final String CoeffsFile = "Equalized-Coeffs";
 private final String CoeffsFile = "Equalized-Coeffs-W1-EC-02k";

 private RandomAccessFile pcFile, coeffsFile, weightsFile;
 private float coeffsData[][];
 private float eigenSpace[][];
 private float featArr[][]; // with the transformed space and flags
                            // so each row is 7 (PCs) +   1 ( flags )
 private float output[];
 public BP()
 {
  PCFile = new String(PCFile+"EC-04k");
  BPNet bpNet;
  bpNet = new BPNet("Equalized-Weights-Identify-PC10-EC-04k");

  openFiles();
  readInputData();
  featArr = new float[rows][NUM_PCA_COMP + 1];  // has the flags also
  transformCoeffs();
  output = new float[1];
  for(int i = 0; i< rows; i++)
  {  
    bpNet.TestNet(featArr[i],output);
    System.out.println(" Actual "+featArr[i][NUM_PCA_COMP]+" obtained is "+output[0]);
  
  }
  
}
 
 private void transformCoeffs()
 {

  for(int i=0; i < rows; i++)
  {
   for(int k=0; k <NUM_PCA_COMP; k++)
   {
    featArr[i][k] = 0.0f;
	for(int k2 = 0; k2 < m; k2++)
	{
	 featArr[i][k] += coeffsData[i][k2] * eigenSpace[k2][k];
	 
	}
   }
   
   featArr[i][NUM_PCA_COMP] = coeffsData[i][m];     // corrosion yes/no 1/0
  // featArr[i][NUM_PCA_COMP] = coeffsData[i][m + 1]; // materail loss
  }
 }

 private void openFiles()
 {
 try
  {
    pcFile = new RandomAccessFile(PCFile, "r");
	coeffsFile = new RandomAccessFile(CoeffsFile, "r");
  }
  catch(IOException ex)
  {
  System.err.println(" Error while opening files ");
  //System.exit(1);
  }
 }

 private void readInputData()  
 {
   readCoeffs();
   readEigenSpace();
 }

 private void readEigenSpace()
 {
  int numPCs;
  try
  {
   pcFile.seek(0);
   numPCs = pcFile.readInt();
   
   eigenSpace = new float[m][NUM_PCA_COMP];
   System.out.print("Eigen space");
   if( numPCs != NUM_PCA_COMP) System.err.println(" Num PCs mismatch ");
   for(int  i=0; i< m; i++)
   {
    for(int j=0; j < NUM_PCA_COMP; j++)
	{
	  eigenSpace[i][j] = pcFile.readFloat();
//System.out.print("  "+eigenSpace[i][j]);
	}
//System.out.println("");
   }
  }
  catch(IOException ex)
  {
  System.err.println(" Error reading Eigen Space ");
  //System.exit(1);
  }
 }

 private void readCoeffs()
 {
  int cols;  // rows is a class member variable
  try
   {
    coeffsFile.seek(0);
    rows = coeffsFile.readInt();  // number of rows
System.out.println(" Number of rows "+rows);
	cols = coeffsFile.readInt();  // number of cols or the vector length
 
    coeffsData = new float[rows][cols];
    for(int i=0; i < rows; i++)  // for all rows
    {
     for(int j=0; j < cols; j++)  // for all elements in the vector
	 {
	  coeffsData[i][j] = coeffsFile.readFloat();
	 }
    }
   }
   catch(IOException ex)
   {
    System.err.println(" Error while reading coeffs file ");
	//System.exit(1);
   }
 }
 

 public static void main( String args[])
 {
  BP BpMat;
  BpMat = new BP();
 }
}
