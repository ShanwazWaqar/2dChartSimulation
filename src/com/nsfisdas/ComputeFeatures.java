package com.nsfisdas;
public class ComputeFeatures
{
 private float L[][];
 private float a[][];
 private float b[][];

 private float red[][];   // can be used instead of L,a,b
 private float green[][];
 private float blue[][];

 private int R[][];
 private int G[][];
 private int B[][];

 private WindowClusters windowClusters;

 private int rows, cols, N;
 private float featureVector[];

 private float rEntropy, gEntropy, bEntropy;
 private float rACF, gACF, bACF;
 
 private float xm2[][], ym2[][], zm2[][];
 private float Pr[], Pg[], Pb[];  // histogram
 
 private float xmew1,xmew2;
 private float ymew1, ymew2;
 private float zmew1, zmew2;

 private float xr[][], yr[][], zr[][];
 
 
 public ComputeFeatures(float l[][], float A[][], float B[][])
 {
  L = l;
  a = A;
  b = B;;
  rows = L.length;
  cols = a[0].length;
  N = rows * cols;
 }

 public void setFeatureVector(float vector[])
 {
  featureVector = vector;
 }

 public void setRGB( int Rpixels[][], int Gpixels[][], int Bpixels[][])
 {
  R = Rpixels;
  G = Gpixels;
  B = Bpixels;
  red = new float[rows][cols];
  green = new float[rows][cols];
  blue = new float[rows][cols];

  for(int i=0; i < rows; i++)
  {
   for(int j =0; j < cols; j++)
   {
    red[i][j] = (float)(R[i][j]);
    green[i][j] = (float)(G[i][j]);
    blue[i][j] = (float) (B[i][j]);
   }
  }

 }

 public float [] getFeatureVector()
 { 
   float means[];
   float stds[];
   windowClusters = new WindowClusters(L,a,b);
   means = windowClusters.getMeans();
   stds = windowClusters.getStds();
   setHistogram();
   setEntropy();
   setACF();

   for(int i=0; i < means.length; i++)
    featureVector[i] = means[i];

   for(int i=0; i < stds.length; i++)
    featureVector[i + means.length] = stds[i];
   
   int ctr = means.length + stds.length;
    
   featureVector[ctr+0] = rEntropy;
   featureVector[ctr+1] = gEntropy;
   featureVector[ctr+2] = bEntropy;

   featureVector[ctr+3] = rACF;
   featureVector[ctr+4] = gACF;
   featureVector[ctr+5] = bACF;

   return featureVector;
 }

 private void setHistogram()
 {
  Pr = new float[256];
  Pg = new float[256];
  Pb = new float[256];
  for( int i =0; i < 256; i++)
   {
       Pr[i]=1.0f;
       Pg[i]=1.0f;
       Pb[i]=1.0f;
   }
  for(int i = 0; i < rows; i++)
  {
   for( int j=0; j < cols; j++)
    {
      Pr[ R[i][j] ]++;
      Pg[ G[i][j] ]++;
      Pb[ B[i][j] ]++;
    }
  }
 }

 private void setEntropy()
 {
  float rsum, gsum, bsum;
  rsum = gsum = bsum = 0.0f;
  
  for(int i=0; i < 255; i ++)
  {
 
      rsum = rsum - (float)((Pr[i]/N)*Math.log(Pr[i]/N));
      gsum = gsum - (float)((Pg[i]/N)*Math.log(Pg[i]/N));
      bsum = bsum - (float)((Pb[i]/N)*Math.log(Pb[i]/N));
    
  }
  rEntropy = rsum;
  gEntropy = gsum;
  bEntropy = bsum;
 }



 
 private void setACF()
 {
  // cross-relation
   setm2Matrix();
   setrMatrix();
   setMews(rows-1,cols-1);
   float  xsum, ysum, zsum;
   xsum = ysum = zsum = 1;

   for(int i=0; i < rows; i++)
   {
    for(int j=0; j < cols; j++)
    {
      xsum = xsum + (i-xmew1)*(j-xmew2)*xr[i][j];
      ysum = ysum + (i-ymew1)*(j-ymew2)*yr[i][j];
      zsum = zsum + (i-zmew1)*(j-zmew2)*zr[i][j];  
    }
   }
   rACF = xsum/N;
   gACF = ysum/N;
   bACF = zsum/N;
      
 }

 private void setMews(int m ,int n)
 {
  xmew1 = ymew1 = zmew1 = 0;
  xmew2 = ymew2 = zmew2 = 0;

  for(int i=0; i < m; i++)
  {
   for(int j=0; j < n; j++)
    {
     xmew1 = xmew1 + (i+1) * xr[i][j];
     ymew1 = ymew1 + (i+1) * yr[i][j];
     zmew1 = zmew1 + (i+1) * zr[i][j];

     xmew2 = xmew2 + (j+1) * xr[i][j];
     ymew2 = ymew2 + (j+1) * yr[i][j];
     zmew2 = zmew2 + (j+1) * zr[i][j];

    }
  }
  xmew1 = xmew1/(m*n);
  ymew1 = ymew1/(m*n);
  zmew1 = zmew1/(m*n);

  xmew2 = xmew2/(m*n);
  ymew2 = ymew2/(m*n);
  zmew2 = zmew2/(m*n);

  System.out.println(" xmew1 "+xmew1+ " xmew2 "+xmew2);
 }


 private void setm2Matrix()
 {
  float xm2Sum, ym2Sum, zm2Sum;
  xm2 = new float [rows][cols];
  ym2 = new float [rows][cols];
  zm2 = new float [rows][cols];

  for (int k=0; k < rows; k++)
  {
   for( int l= 0; l < cols; l++)
   {
     xm2Sum = ym2Sum = zm2Sum = 1;
   
 // compute m2(k,l)
     
     for( int i = 0; i < (rows - k); i++)
      {
       for( int j = 0; j < (cols - l); j++)
        {
         xm2Sum = xm2Sum + R[i][j] * R[i][j];
         ym2Sum = ym2Sum + G[i][j] * G[i][j];
         zm2Sum = zm2Sum + B[i][j] * B[i][j];
        }
      }

     xm2[k][l] = (float) (xm2Sum/((rows - k) * (cols - l)));
     ym2[k][l] = (float) (ym2Sum/((rows - k) * (cols - l)));
     zm2[k][l] = (float) (zm2Sum/((rows - k) * (cols - l)));
//     System.out.println(" zm2 "+zm2[k][l] + "zm200 "+ zm2[0][0]);
   }
  }
 }

 private void setrMatrix()
 {
  xr = new float [rows][cols];
  yr = new float [rows][cols];
  zr = new float [rows][cols];

  for ( int i=0; i < rows; i ++)
  {
   for( int j=0; j < cols; j++)
   {
    xr[i][j] = (xm2[i][j]) / (xm2[0][0]);
    yr[i][j] = (ym2[i][j]) / (ym2[0][0]);
    zr[i][j] = (zm2[i][j]) / (zm2[0][0]);
    //System.out.println(" xr "+xr[i][j]);
   }
  }
 }

}
 
