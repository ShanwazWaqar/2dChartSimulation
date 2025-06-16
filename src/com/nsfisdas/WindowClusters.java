package com.nsfisdas;

public class WindowClusters
{
 private int NumClusters = 4;
 private int validClusters = 1;

 private float MeanL[], Meana[], Meanb[];
 private float StdL[], Stda[], Stdb[];

 private float L[][];
 private float a[][];
 private float b[][];
 private int rows, cols;
 private int N;
 private float Centers[][]; //Lab times num clusters
 private int numMembers[];
 private  float means[];
 private float stds[];

 public WindowClusters(float l[][], float A[][], float B[][])
 {
  L = l;
  a = A;
  b = B;
  rows = L.length;
  cols = L[0].length;
  N = rows * cols;
  Centers = new float[NumClusters][3];
  numMembers = new int[NumClusters];

  MeanL = new float [validClusters];
  Meana = new float [validClusters];
  Meanb = new float [validClusters];

  StdL = new float [validClusters];
  Stda = new float [validClusters];
  Stdb = new float [validClusters];

  initializeCenters();
  formMembers();
  sortCenters();
 }
 
 public float[] getMeans()
 {

  means = new float[3* validClusters];
  for(int i=0; i < validClusters; i++)
  {
   means[(i*3)+0] = Centers[i][0];
   means[(i*3)+1] = Centers[i][1];
   means[(i*3)+2] = Centers[i][2];
  }
  return means;
 }

 public float[] getStds()
 {
  CalculateStd();
  stds = new float[3* validClusters];
  for(int i=0; i < validClusters; i++)
  {
   stds[(i*3)+0] = (float)( Math.sqrt(StdL[i])/N);
   stds[(i*3)+1] = (float)( Math.sqrt(Stda[i])/N);
   stds[(i*3)+2] = (float) ( Math.sqrt(Stdb[i])/N);
 // System.out.println(" Stds "+stds[(i*3)+0]+"  "+stds[(i*3)+1]+"  "+stds[(i*3)+2]);
  //System.out.println("Members"+ numMembers[i]);
  }

  return stds;
 }

 private  void CalculateStd()
 {
  int belongsTo;
  int count=0;
  for(int i=0; i < rows; i++)
  {
   for(int j=0; j < cols; j++)
   {
      belongsTo = belongsTo(L[i][j], a[i][j], b[i][j]);
      
      if ( belongsTo < validClusters)
      {
       StdL[belongsTo] = (float)(StdL[belongsTo] + Math.pow((L[i][j] - Centers[belongsTo][0]),2));
       Stda[belongsTo] = (float) (Stda[belongsTo] + Math.pow((a[i][j] - Centers[belongsTo][1]),2));
       Stdb[belongsTo] = (float) (Stdb[belongsTo] + Math.pow((b[i][j] - Centers[belongsTo][2]),2));
       count++;
      }
   }
  }
  System.out.println(" count is "+count);
 }

 private int belongsTo(float  rL, float ra, float rb)
 {
  int nearest =0;
  float eucDis;
  float minEucDis = Eucledian(Centers[0], rL, ra, rb);
  for(int i=1; i < NumClusters; i++)
  {
   eucDis = Eucledian(Centers[i], rL, ra, rb);
   if( eucDis < minEucDis )
     nearest = i;
  }
  return nearest;
 }

 private void sortCenters()
 {
  // sort centers based on number of clusters 
    float tempL, tempa, tempb;
  for(int i=0; i < validClusters; i++)
  {
   for(int j=(i+1); j < NumClusters; j++)
   {
    if( numMembers[j] > numMembers[i])
     { //swap

      int tempMem = numMembers[i];
      numMembers[i] = numMembers[j];
      numMembers[j] = tempMem;

       // swap centers also
      

         tempL = Centers[i][0];
         tempa = Centers[i][1];
         tempb = Centers[i][2];

         Centers[i][0] = Centers[j][0];
         Centers[i][1] = Centers[j][1];
         Centers[i][2] = Centers[j][2];

         Centers[j][0] = tempL;
         Centers[j][1] = tempa;
         Centers[j][2] = tempb;
     }
   }
  }
 
 }

 private void initializeCenters()
 {
  // Randomly pick some and assign
  
  for (int i=0; i < NumClusters; i++)
  {
   Centers[i][0] = L[(int)(Math.random() * rows)][(int)(Math.random() * cols)];
   Centers[i][1] = a[(int)(Math.random() * rows)][(int)(Math.random() * cols)];
   Centers[i][2] = b[(int)(Math.random() * rows)][(int)(Math.random() * cols)];
   numMembers[i] = 1;
  }
 }

 private void formMembers()
 {
  for(int i=0; i < NumClusters; i++)
   numMembers[i]=1;

  // now count the number of of points in each cluster

  for(int i=0; i < rows; i++)
  {
   for(int j=0; j < cols; j++)
   {
    adjustCenter(L[i][j], a[i][j], b[i][j]);
   }
  }
 
 }

 private void adjustCenter(float  rL, float ra, float rb)
 {
  int nearest =0;
  float eucDis;
  float minEucDis = Eucledian(Centers[0], rL, ra, rb);
  for(int i=1; i < NumClusters; i++)
  {
   eucDis = Eucledian(Centers[i], rL, ra, rb);
   if( eucDis < minEucDis )
     {
      nearest = i;
      minEucDis = eucDis;
     }
  }

  // adjust centers
  Centers[nearest][0] = (Centers[nearest][0] * numMembers[nearest] + rL)/(numMembers[nearest] + 1);
  Centers[nearest][0] = (Centers[nearest][1] * numMembers[nearest] + ra)/(numMembers[nearest] + 1);
  Centers[nearest][0] = (Centers[nearest][2] * numMembers[nearest] + rb)/(numMembers[nearest] + 1);
 
  numMembers[nearest]++;
 }


 private float Eucledian(float Center[], float vL, float va, float vb)
 {
  float dis;
  dis = (float)(Math.sqrt(Math.pow((Center[0]-vL),2) + Math.pow((Center[1]-va),2) + Math.pow((Center[2]-vb),2)));
  return dis;
 }
}

