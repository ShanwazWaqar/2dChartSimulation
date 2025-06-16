package com.nsfisdas;

public class QtyFeatures
{
 private int Red[][];
 private int Green[][];
 private int Blue[][];
 private int rows;
 private int cols;
 int num;  // (area)
 double rmean, gmean, bmean;
 double rstd, gstd, bstd;
 double area =0;
 public QtyFeatures( int R[][], int G[][], int B[][])
 {
  Red = R;
  Green = G;
  Blue = B;
  rows = R.length;
  cols = R[0].length;
 }

 public void setFeatures( double farry[])
 {
  area = setArea();
  setMeans();
  farry[0] = rmean;
  farry[1] = gmean;
  farry[2] = bmean;
  setSTDs();
  farry[3] = rstd;
  farry[4] = gstd;
  farry[5] = bstd;
 
 }
 
 private double setArea()
 {

  for(int i=0; i < rows; i++)
  {
   for(int j =0; j< cols; j++) 
   {
    if( Red[i][j] !=0 ||Green[i][j] != 0 || Blue[i][j]!=0 )
     area++;
   }
  }
  num = (int) area;
  return area;
 }

 private void setMeans()
 {
   rmean = 0;
   gmean = 0;
   bmean = 0;
  for( int i=0; i< rows; i++)
  {
   for( int j=0; j<cols; j++)
   {
   if( Red[i][j] !=0 ||Green[i][j] != 0 || Blue[i][j]!=0 )
    {
      rmean = rmean + Red[i][j];
      gmean = gmean + Green[i][j];
      bmean = bmean + Blue[i][j];
    }
   }
  }
  rmean = rmean/num;
  gmean = gmean/num;
  bmean = bmean/num;
 }

 private void setSTDs()
 {
  double rsum,gsum, bsum;
  rsum = gsum = bsum =0;
  for( int i =0; i<rows; i++)
   {
    for(int j =0; j < cols; j++)
      {
       if( Red[i][j] !=0 ||Green[i][j] != 0 || Blue[i][j]!=0 )
        {
          rsum =  rsum +  Math.pow((Red[i][j] - rmean), 2);
          gsum = gsum +  Math.pow((Green[i][j] - gmean),2);
          bsum = bsum +  Math.pow((Blue[i][j] - bmean),2);
        }
      }
   }

  rstd  = Math.sqrt(rsum/num);
  gstd = Math.sqrt(gsum/num);
  bstd = Math.sqrt(bsum/num);

 }

}
