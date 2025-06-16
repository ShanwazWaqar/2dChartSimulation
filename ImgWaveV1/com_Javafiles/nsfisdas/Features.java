package com.nsfisdas;

public class Features
{
 private RGBList rgbList;
 private RGBNode firstNode;
 private RGBNode lastNode;

 private float redMean;
 private float greenMean;
 private float blueMean;

 private float stdRed;
 private float stdBlue;
 private float stdGreen;

 private float skewRed;
 private float skewGreen;
 private float skewBlue;

 private float energyRed;
 private float energyGreen;
 private float energyBlue;

 private float entropyRed;
 private float entropyGreen;
 private float entropyBlue;


 public Features( RGBList list )
 {
  rgbList = list;
  firstNode = rgbList.getFirstNode();
  lastNode = rgbList.getLastNode();
  
 }
 public void InitializeMeans()
 {
  RGBNode ptr;
  int rsum,gsum, bsum;
  RGB rgb;
  int numMembers; 
   rsum = gsum = bsum =0;
 int bval;
  for ( ptr = firstNode; ptr != null; ptr = ptr.getNextNode() )
  {
   rgb = ptr.getRGB();
   rsum = rsum + rgb.getRed();
   gsum = gsum + rgb.getGreen();
   bval = rgb.getBlue();
   bsum = bsum + bval;
  
  }
  numMembers = rgbList.getNumMembers();
  redMean = rsum/numMembers;
  greenMean = gsum/numMembers;
  blueMean = bsum/numMembers;
 }

 public float getRedMean()
 {
  return redMean;
 }
 public float getGreenMean()
 {
  return greenMean;
 }
 public float getBlueMean()
 {
  return blueMean;
 } 
 
 public float getArea()
 {
  return (float) rgbList.getNumMembers();
 }
 
 public void InitializeSTD()
 {
  RGBNode ptr;
  float rsum,gsum, bsum;
  RGB rgb;
  int numMembers; 
  rsum = gsum = bsum =0;
  for ( ptr = firstNode; ptr != null; ptr = ptr.getNextNode() )
  {
   rgb = ptr.getRGB();
   rsum =  rsum + ( float) Math.pow((rgb.getRed() - redMean), 2);
   gsum = gsum + (float) Math.pow((rgb.getGreen() - greenMean),2);
   bsum = bsum + ( float) Math.pow((rgb.getBlue() - blueMean),2);
  }
  
  numMembers = rgbList.getNumMembers();
  rsum = rsum/numMembers;
  gsum = gsum/numMembers;
  bsum = bsum/numMembers;

  stdRed = (float)Math.sqrt(rsum);
  stdGreen = (float)Math.sqrt(gsum);
  stdBlue = (float)Math.sqrt(bsum);
 }

 public void InitializeSkew()
 {
  RGBNode ptr;
  RGB rgb;
  float redsum=0;
  float greensum =0;
  float bluesum =0;
  int numMembers = rgbList.getNumMembers();
  int rCount, gCount, bCount;
  for(int i=0; i<256; i++)
  {
   rCount = gCount = bCount =0;
   for ( ptr = firstNode; ptr != null; ptr = ptr.getNextNode() )
   {
    rgb = ptr.getRGB();
    if(rgb.getRed() == i)
      rCount++;
    if(rgb.getGreen() == i)
      gCount++;
    if(rgb.getBlue() == i)
      bCount++;
   }
   redsum = redsum + (float)Math.pow((i - redMean),3)*((float)(rCount/numMembers));
   greensum = greensum + (float)Math.pow((i - greenMean),3)*((float)(gCount/numMembers));
   bluesum = bluesum + (float)Math.pow((i - blueMean),3)*((float)(bCount/numMembers));
  }

  skewRed = redsum/(float)(Math.pow(stdRed,3));
  skewGreen = greensum/(float)(Math.pow(stdGreen,3));
  skewBlue = bluesum/(float)(Math.pow(stdBlue,3));
 }


 public void InitializeEnergy()
 {
  RGBNode ptr;
  RGB rgb;
  int numMembers = rgbList.getNumMembers();
  int rCount, gCount, bCount;
  for(int i=0; i<256; i++)
  {
   rCount = gCount = bCount =0;
   for ( ptr = firstNode; ptr != null; ptr = ptr.getNextNode() )
   {
    rgb = ptr.getRGB();
    if(rgb.getRed() == i)
      rCount++;
    if(rgb.getGreen() == i)
      gCount++;
    if(rgb.getBlue() == i)
      bCount++;
   }
   energyRed = energyRed + (float)Math.pow(((float)(rCount/numMembers)),2);
   energyGreen = energyGreen + (float)Math.pow(((float)(gCount/numMembers)),2);
   energyBlue = energyBlue + (float)Math.pow(((float)(bCount/numMembers)),2);
  }
 }

 public void InitializeEntropy()
 {
  
 RGBNode ptr;
  RGB rgb;
  int numMembers = rgbList.getNumMembers();
  int rCount, gCount, bCount;
  for(int i=0; i<256; i++)
  {
   rCount = gCount = bCount =0;
   for ( ptr = firstNode; ptr != null; ptr = ptr.getNextNode() )
   {
    rgb = ptr.getRGB();
    if(rgb.getRed() == i)
      rCount++;
    if(rgb.getGreen() == i)
      gCount++;
    if(rgb.getBlue() == i)
      bCount++;
   }
   float Pr = (float)(rCount/numMembers);
   float Pg = (float)(gCount/numMembers);
   float Pb = (float)(bCount/numMembers);

   entropyRed = entropyRed - Pr * (float)Math.log(Pr);
   entropyGreen = entropyGreen - Pg * (float)Math.log(Pg);
   entropyBlue = entropyBlue - Pb *(float) Math.log(Pb);
  }

 }

 public float getRedSTD()
 {
  return stdRed;
 }
 public float getGreenSTD()
 {
   return stdGreen;
 }
 public float getBlueSTD()
 {
  return stdBlue;
 }


}
