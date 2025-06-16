package com.nsfisdas;
public class Convolve
{
 private float orgImg[][];
 private int rows, cols;
 private int maskLen;
 private float g[];
 private float h[];

 float L[][];
 float H[][];
 float LL[][];
 float LH[][];
 float HL[][];
 float HH[][];

 public Convolve( float setImg[][], float iLL[][], float iLH[][], float iHL[][], float iHH[][])
 {
  orgImg = setImg;
  rows = orgImg.length;
  cols = orgImg[0].length;

  Daubechies filters = new Daubechies();
  g = filters.getG1();
  h = filters.getH1();
  maskLen = h.length;
  L = new float[rows][cols];
  H = new float[rows][cols];
  LL = iLL;
  LH = iLH;
  HL = iHL;
  HH = iHH;
  convoleRows();
  convolveCols();
 }

 private void  convoleRows()
 {
  //  low pass (h)
  // high pass (g)
 float sumL, sumH;
   for( int i=0; i < rows; i++)
   {
    for(int j = 0; j < cols; j++)
    {
     // sum for length of filter
  
      sumL = 0.0f;
      sumH = 0.0f;
      for( int k=0; k < maskLen; k++ )
      {
       sumL = sumL + h[k] * orgImg[i][(cols + (j - (maskLen/2) + k))% cols]; 
       sumH = sumH + g[k] * orgImg[i][(cols + (j - (maskLen/2) + k))% cols]; 
      }
      L[i][j] = sumL;
      H[i][j] = sumH;
 
    }
   }

 }

 private void convolveCols()
 {
  // Obtain LL
 float sumLL, sumHL, sumLH, sumHH;
      sumLL = 0.0f;
      sumHL = 0.0f;
      sumLH = 0.0f;
      sumHH = 0.0f;

  for( int j =0 ; j < cols; j++)
  {
   for( int i=0; i < rows; i++)
   {
    // sum for length of filter

  
      for( int k=0; k < maskLen; k++ )
      {
       sumLL = sumLL + h[k] * L[(rows + (i - (maskLen/2) + k))% rows][j]; 
       sumHL = sumHL + h[k] * H[(rows + (i - (maskLen/2) + k))% rows][j];
       sumLH = sumLH + g[k] * L[(rows + (i - (maskLen/2) + k))% rows][j];
       sumHH = sumHH + g[k] * H[(rows + (i - (maskLen/2) + k))% rows][j];
      }
   
     LL[i][j] = sumLL;
     HL[i][j] = sumHL;
     LH[i][j] = sumLH;
     HH[i][j] = sumHH;
 // System.out.println("LL "+sumLL+" HL "+sumHL+" LH "+sumLH+" HH "+sumHH);

      sumLL = 0.0f;
      sumHL = 0.0f;
      sumLH = 0.0f;
      sumHH = 0.0f;
   } 
  }
 }

}
