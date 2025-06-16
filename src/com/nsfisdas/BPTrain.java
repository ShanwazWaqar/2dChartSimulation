package com.nsfisdas;
import java.io.*;
import java.util.*;

public class BPTrain
{
 private final int TYPE = 1; // ( 0 for identification, 1 for quantification)
 private final int m = 15;  // num of rows on PCs equal to the vectorDim length
 private int rows;
 private final int NUM_PCA_COMP = 10;
 private final float trainPercent = 0.75f;
 private final String PCFile = "Equalized-PC10-W1-Test-S";
 private final String CoeffsFile = "Equalized-Coeffs-W1-Test-S";

 private RandomAccessFile pcFile, coeffsFile, weightsFile;
 private float coeffsData[][];
 private float eigenSpace[][];
 private float featArr[][]; // with the transformed space and flags
                            // so each row is 7 (PCs) +   1 ( flags )

 private float trainFeatArr[][];
 private float testFeatArr[][];
 private int trainNum;
 private int testNum;


 public BPTrain()
 {
  BPNetSave bpNetTrain;
 bpNetTrain = new BPNetSave();
 openFiles();
 readInputData();
 featArr = new float[rows][NUM_PCA_COMP + 1];  // has the flags also
 transformCoeffs();
 splitTrain_Test();
 System.out.println(" Using "+NUM_PCA_COMP+" principal components");
 System.out.println(" In each epoch rows * 100 features are given. rows ="+rows);
 

// to train for long...increase the values in the loop


 for(int i = 0; i< 10; i++)
  {  
   double TrainErr =0;         
   for( int newdata =0; newdata < trainNum * 500; newdata++)
    {

      TrainErr += bpNetTrain.TrainNet(trainFeatArr[(int)(trainNum * Math.random())]);
      bpNetTrain.SaveWeights();
    }
    System.out.println(" Training "+(i+1)+" error is "+TrainErr);
   if( TYPE == 0)
   {
     bpNetTrain.WeightsToFile("Equalized-Weights-Identify-PC10-Test-S");
   }
   else
   {
     bpNetTrain.WeightsToFile("Equalized-Weights-Quantify-PC10-Test-S");
   }
  }
 
  // now test the weights
  BPNet bpNetTest;
 
  if( TYPE == 1)   // quantify
  { 
    bpNetTest = new BPNet("Equalized-Weights-Quantify-PC10-Test-S");
  }
  else  // identify
  {
    bpNetTest = new BPNet("Equalized-Weights-Identify-PC10-Test-S");
  }
  float output[];
  output = new float[1];
  for(int i=0; i < testNum; i++)
  {
    bpNetTest.TestNet(testFeatArr[i],output);
    System.out.println(" Actual "+testFeatArr[i][NUM_PCA_COMP]+" obtained is "+output[0]);
  }
}
 
private void splitTrain_Test()
{
  Vector vector;
  vector = new Vector(rows);
  Integer Int;

  for(int i=0; i < rows; i++)
  {
   Int = new Integer(i);
   vector.add(Int);
  }

trainNum = (int)(rows * trainPercent);
testNum = rows - trainNum;

trainFeatArr = new float[trainNum][NUM_PCA_COMP + 1];
testFeatArr = new float[testNum][NUM_PCA_COMP + 1];

int removeAt=0;
int j;

 // first copy the training set
 for(int i=0; i < trainNum; i++)
 {
  removeAt =(int)(Math.random() * ( vector.size() ));
  Integer got = (Integer)vector.remove(removeAt);
  j = got.intValue();
  // now move featArr[j][*] into trainFeatArr[i][*];
  for(int ctr=0; ctr < (NUM_PCA_COMP + 1); ctr++)
    trainFeatArr[i][ctr] = featArr[j][ctr];
  }

  // now copy the testing set
 for(int i=0; i < testNum; i++)
 {
  removeAt =(int)(Math.random() * ( vector.size() ) );
  Integer got = (Integer)vector.remove(removeAt);
  j = got.intValue();
  // now move featArr[j][*] into trainFeatArr[i][*];
  for(int ctr=0; ctr < (NUM_PCA_COMP + 1); ctr++)
    testFeatArr[i][ctr] = featArr[j][ctr];
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
   if (TYPE == 0) // identify
   {
      featArr[i][NUM_PCA_COMP] = coeffsData[i][m];     // corrosion yes/no 1/0
   }
   else
   {
      featArr[i][NUM_PCA_COMP] = coeffsData[i][m + 1]; // materail loss
   }
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
  BPTrain BpMat;
  BpMat = new BPTrain();
 }
}
