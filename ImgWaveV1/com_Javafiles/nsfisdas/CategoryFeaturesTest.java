package com.nsfisdas;
import java.io.*;


public class CategoryFeaturesTest
{
 final int NUM_FEATURES = 13;  //12 features + 1 flag

 private ImageMatrix OrgImage;
 private int rows, cols;


 private int R[][], G[][], B[][];
 private float L[][], a[][], b[][]; 
 private int red[][], green[][], blue[][];

 private float featureVector[];
 private float category;

 public CategoryFeaturesTest(ImageMatrix OriginalImage)
 {
  OrgImage = OriginalImage;
  rows = OrgImage.getRows();
  cols = OrgImage.getCols();

  R = OrgImage.getRedPointer();
  G = OrgImage.getGreenPointer();
  B = OrgImage.getBluePointer();

  L = new float[rows][cols];
  a = new float[rows][cols];
  b = new float[rows][cols];

  red = new int [rows][cols];
  green = new int [rows][cols];
  blue = new int [rows][cols];
 }

 public float[] getFeatures()
 {
  ComputeFeatures computeFeatures;
   convertToLAB();
   computeFeatures = new ComputeFeatures(L,a,b);
   computeFeatures.setRGB(red,green,blue);
   featureVector = new float[NUM_FEATURES];
   computeFeatures.setFeatureVector(featureVector);
   featureVector = computeFeatures.getFeatureVector();
   return featureVector;
 }

 private void convertToLAB()
 {
   float X, Y, Z;
  for(int x = 0; x < rows; x++)
  {
   for(int y= 0; y < cols; y++)
   {
    X = (0.49f * R[x][y] + 0.31f * G[x][y] + 0.2f * B[x][y])/255.0f;
    Y = (0.177f * R[x][y] + 0.813f * G[x][y] + 0.011f *B[x][y])/255.0f;
    Z = (0.0f  * R[x][y] + 0.010f * G[x][y] + 0.99f * B[x][y])/255.0f;
    
    L[x][y] =  (float)(25.0f * Math.pow((100*Y),(1./3)) - 16.0f);
    a[x][y] = (float)(500.0f * ( Math.pow( (X/0.982), (1./3)) - Math.pow( (Y), (1./3) ) ));
    b[x][y] = (float)(200.0f *( Math.pow( (Y), (1./3)) - Math.pow( (Z/1.183), (1./3) ) ) );

    red[x][y] = R[x][y];
    green[x][y] = G[x][y];
    blue[x][y] = B[x][y];

   }
  }
 }

}
