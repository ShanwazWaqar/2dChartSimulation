package com.nsfisdas;
import java.io.*;


public class CategoryFeatures
{
 final int NUM_FEATURES = 13;  //12 features + 1 flag
 final int numWindows = 1;
 final int windowSize = 32;

 private ImageMatrix OrgImage;
 private int rows, cols, Xo, Yo;


 private int R[][], G[][], B[][];
 private float L[][], a[][], b[][]; 
 private int red[][], green[][], blue[][];

 private float featureVector[];
 private float category;

 public CategoryFeatures(ImageMatrix OriginalImage)
 {
  OrgImage = OriginalImage;
  rows = OrgImage.getRows();
  cols = OrgImage.getCols();

  R = OrgImage.getRedPointer();
  G = OrgImage.getGreenPointer();
  B = OrgImage.getBluePointer();

  L = new float[windowSize][windowSize];
  a = new float[windowSize][windowSize];
  b = new float[windowSize][windowSize];

  red = new int [windowSize][windowSize];
  green = new int [windowSize][windowSize];
  blue = new int [windowSize][windowSize];
 }

 public float[] getFeatures()
 {
  ComputeFeatures computeFeatures;
 
 
  for(int i=0; i < numWindows; i++)
  {
   getXoYo();
   convertToLAB();
   computeFeatures = new ComputeFeatures(L,a,b);
   computeFeatures.setRGB(red,green,blue);
   featureVector = new float[NUM_FEATURES];
   computeFeatures.setFeatureVector(featureVector);
   featureVector = computeFeatures.getFeatureVector();
  }
  return featureVector;
 }

 private void getXoYo()
 {
  Xo = (int) ((rows - windowSize) * Math.random());
  Yo = (int) ((cols - windowSize) * Math.random());
 
 }

 private void convertToLAB()
 {
   float X, Y, Z;

  int x, y;
  for(int i = 0; i < windowSize; i++)
  {
   for(int j= 0; j < windowSize; j++)
   {
    x = Xo + i;
    y = Yo + j;

    X = (0.49f * R[x][y] + 0.31f * G[x][y] + 0.2f * B[x][y])/255.0f;
    Y = (0.177f * R[x][y] + 0.813f * G[x][y] + 0.011f *B[x][y])/255.0f;
    Z = (0.0f  * R[x][y] + 0.010f * G[x][y] + 0.99f * B[x][y])/255.0f;
    
    L[i][j] =  (float)(25.0f * Math.pow((100*Y),(1./3)) - 16.0f);
    a[i][j] = (float)(500.0f * ( Math.pow( (X/0.982), (1./3)) - Math.pow( (Y), (1./3) ) ));
    b[i][j] = (float)(200.0f *( Math.pow( (Y), (1./3)) - Math.pow( (Z/1.183), (1./3) ) ) );

    red[i][j] = R[x][y];
    green[i][j] = G[x][y];
    blue[i][j] = B[x][y];

   }
  }
 }
}
