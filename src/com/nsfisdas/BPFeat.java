package com.nsfisdas;
import java.io.*;

public class BPFeat
{

 private int rows1, rows2, rows3, rows4, rows5;
 private int cols1, cols2, cols3, cols4, cols5;
 
 private int R1[][];
 private int G1[][];
 private int B1[][];
 
 private int R2[][];
 private int G2[][];
 private int B2[][];
 
 private int R3[][];
 private int G3[][];
 private int B3[][];
 
 private int R4[][];
 private int G4[][];
 private int B4[][];

 private int R5[][];
 private int G5[][];
 private int B5[][]; 

 private DataInputStream matfile1;
 private DataInputStream matfile2;
 private DataInputStream matfile3;
 private DataInputStream matfile4;
 private DataInputStream matfile5;

 private QtClusters qCluster;
 private double multiplier;

 private  double FeatArr[][]; // rows = clusters and cols = num of features
  
 private double OrgFeat[][];
  

 private  QtyFeatures qtyFeat1,qtyFeat2,qtyFeat3, qtyFeat4, qtyFeat5 ;
 public BPFeat()
 {
  BPNet bpNet;
  bpNet = new BPNet("FeatWeights");
  FeatArr = new double[5][4];  // has the flag also
  OrgFeat = new double[5][4];
  
  
  double amount = 10000;    // decides the number of times data is generated

  OpenandReadintoMat(); // reads the images into Matrix
  FillInitialFeats();   // forms the initial feature vector

  multiplier = 5.0; 
  for(int i = 0; i< 10; i++)
  {  
   double TrainErr =0;         
   for( int newdata =0; newdata < amount; newdata++)
    {
     DisturbFeatures();
      for(int j= 0; j < 5; j++)
       {
    	   // Commented by Bhavana on 5/27/2011 as it was giving errors
         //TrainErr += bpNet.TrainNet(FeatArr[j]);
         bpNet.SaveWeights();
       }
    }
   System.out.println(" Training error is "+TrainErr);
  }
      // test here to print with multiplier

  multiplier = 15.0;
  double output[];
  output = new double[1];
  for(int i = 0; i < 50; i++)
  {
   DisturbFeatures();
   // Commented by Bhavana on 5/27/2011 as it was giving errors
   //bpNet.TestNet(FeatArr[2],output);  
   System.out.println(" Material loss is "+output[0]);
  }
}
 
 public void DisturbFeatures()
 {
  double changeby =0;
  for(int i=0; i<5; i++)
   {
       FeatArr[i][0] = OrgFeat[i][0] * (100 - (multiplier * Math.random()))/100;
      FeatArr[i][1] = OrgFeat[i][1] * (100 + (multiplier * Math.random()))/100;
      FeatArr[i][2] = OrgFeat[i][2] * (100 + (multiplier * Math.random()))/100;
   }
    
 }

 public void OpenandReadintoMat()
 {
  try
  {
   FileInputStream fileStream1 = new FileInputStream("DC91");
   matfile1 = new DataInputStream( fileStream1 );

   FileInputStream fileStream2 = new FileInputStream("DC92");
   matfile2 = new DataInputStream( fileStream2 );
  
   FileInputStream fileStream3 = new FileInputStream("DC93");
   matfile3 = new DataInputStream( fileStream3 );

   FileInputStream fileStream4 = new FileInputStream("DC94");
   matfile4 = new DataInputStream( fileStream4 );
  
   FileInputStream fileStream5 = new FileInputStream("DC95");
   matfile5 = new DataInputStream( fileStream5 );
   
  }
  catch( FileNotFoundException e)
  {
   System.out.println(" File not Found : matfile ");
//   System.exit(1);
  }  
  try
  {
   rows1 = matfile1.readInt();
   cols1 = matfile1.readInt();

   rows2 = matfile2.readInt();
   cols2 = matfile2.readInt();
 
   rows3 = matfile3.readInt();
   cols3 = matfile3.readInt();

   rows4 = matfile4.readInt();
   cols4 = matfile4.readInt();

   rows5 = matfile5.readInt();
   cols5 = matfile5.readInt();

   R1 = new int[rows1][cols1];
   G1 = new int[rows1][cols1];
   B1 = new int[rows1][cols1];

   R2 = new int[rows2][cols2];
   G2 = new int[rows2][cols2];
   B2 = new int[rows2][cols2];

   R3 = new int[rows3][cols3];
   G3 = new int[rows3][cols3];
   B3 = new int[rows3][cols3];

   R4 = new int[rows4][cols4];
   G4 = new int[rows4][cols4];
   B4 = new int[rows4][cols4];

   R5 = new int[rows5][cols5];
   G5 = new int[rows5][cols5];
   B5 = new int[rows5][cols5];
    
    for (int i=0; i < rows1; i++)
     {
      for(int j= 0; j < cols1; j++)
       {
        R1[i][j] = matfile1.readInt();
        G1[i][j] = matfile1.readInt();
        B1[i][j] = matfile1.readInt();
       }
     }

   for (int i=0; i < rows2; i++)
     {
      for(int j= 0; j < cols2; j++)
       {
        R2[i][j] = matfile2.readInt();
        G2[i][j] = matfile2.readInt();
        B2[i][j] = matfile2.readInt();
       }
     }
    
   for (int i=0; i < rows3; i++)
     {
      for(int j= 0; j < cols3; j++)
       {
        R3[i][j] = matfile3.readInt();
        G3[i][j] = matfile3.readInt();
        B3[i][j] = matfile3.readInt();
       }
     }

    for (int i=0; i < rows4; i++)
     {
      for(int j= 0; j < cols4; j++)
       {
        R4[i][j] = matfile4.readInt();
        G4[i][j] = matfile4.readInt();
        B4[i][j] = matfile4.readInt();
       }
     }

    for (int i=0; i < rows5; i++)
     {
      for(int j= 0; j < cols5; j++)
       {
        R5[i][j] = matfile5.readInt();
        G5[i][j] = matfile5.readInt();
        B5[i][j] = matfile5.readInt();
       }
     }
    }
    catch ( IOException e)
    {  
      System.out.println(" IO exception: reading matfiles ");
   //   System.exit(1);
    }

   try
   {
    matfile1.close();
    matfile2.close();
    matfile3.close();
    matfile4.close();
    matfile5.close();
   }
   catch( IOException ex)
   {
    System.out.println(" IO Exception during closing ");
   }

  }


 public void FillInitialFeats()
 {
  
  
  qtyFeat1 = new QtyFeatures(R1,G1,B1);
  qtyFeat2 = new QtyFeatures(R2,G2,B2);
  qtyFeat3 = new QtyFeatures(R3,G3,B3);
  qtyFeat4 = new QtyFeatures(R4,G4,B4);
  qtyFeat5 = new QtyFeatures(R5,G5,B5);

  qtyFeat1.setFeatures(FeatArr[0]);
  qtyFeat2.setFeatures(FeatArr[1]);
  qtyFeat3.setFeatures(FeatArr[2]);
  qtyFeat4.setFeatures(FeatArr[3]);
  qtyFeat5.setFeatures(FeatArr[4]);
  
  for(int i=0; i<5; i++)
   {
    for(int j=0; j <3; j++)
     {
      OrgFeat[i][j] = FeatArr[i][j];
     }
   }
   OrgFeat[0][3] = FeatArr[0][3] = 0.15;
   OrgFeat[1][3] = FeatArr[1][3] = 0.20;
   OrgFeat[2][3] = FeatArr[2][3] = 0.05;
   OrgFeat[3][3] = FeatArr[3][3] = 0.10;
   OrgFeat[4][3] = FeatArr[4][3] = 0.25;
 } 


 private void Testing()
 {
  int error =0;
  int res1, res2, res3, res4, res5;
  
  multiplier = 20.0;

  for (int ctr =1; ctr <=50; ctr ++)
  {
   DisturbFeatures();

   //  res1 = qCluster.FindCluster(FeatArr[0]);
   //res2 = qCluster.FindCluster(FeatArr[1]);
   //res3 = qCluster.FindCluster(FeatArr[2]);
   //res4 = qCluster.FindCluster(FeatArr[3]);
   res5 = qCluster.FindCluster(FeatArr[4]);
  
   if(res5 != 4) 
      System.out.println("*"+res5+1);
    else
      System.out.println("   --");
 //  if(res2 != 1) error++;
  // if(res3 != 2) error++;
   //if(res4 != 3) error++;
   //if(res5 != 4) error++;
 //System.out.println(" Multiplier = "+ multiplier+ " Number of errors " + error);
   }
 }

 public static void main( String args[])
 {
  BPFeat BpMat;
  BpMat = new BPFeat();
 }
}