package com.nsfisdas;
import java.io.*;

class FindCategory 
{
	private final String aveFileName = "AveCategory";
	private final int NUM_FEATURES = 13; // 12 feature and one flag
	private final int NUM_CATEGORIES = 4; // Number of categories in the feature file
	private float aveVector[][];
	private int countVector[];
	private float calculatedVector[];
    private int category;

	private RandomAccessFile aveFile;
    private ImageMatrix OrgImage;

	public FindCategory(ImageMatrix OrgImage)
	{
		this.OrgImage = OrgImage;
		aveVector = new float[NUM_CATEGORIES][NUM_FEATURES];
		countVector = new int[NUM_CATEGORIES];
		openAveFile();
		readAveVectors();
		computeFeatures();
		findCategory();
		closeAveFile();
	}
   
 public int getCategory()
 {
  return category;
 }

 private void computeFeatures()
   {
    CategoryFeatures catFeatures;
	catFeatures = new CategoryFeatures(OrgImage);
	calculatedVector = catFeatures.getFeatures();
   }

  private void findCategory()
  {
   // find nearest neighbour
   category = 1;
  float  closestDistance = Float.MAX_VALUE;
  float newDistance;
  for(int i = 0; i < NUM_FEATURES - 1; i++)
   System.out.println("  "+calculatedVector[i]);
  System.out.println("");
   for(int i =0; i< NUM_CATEGORIES; i++)
    {
	  newDistance = 0.0f;
	   for( int j=0; j < NUM_FEATURES - 1; j++)  // flag is not included
	   {
	     newDistance += (float)( Math.pow((calculatedVector[j] - aveVector[i][j]), 2));
	   }
       if (newDistance < closestDistance)
	   {
		  closestDistance = newDistance;
	      category = i+1;
	   }
	 }
  }
  private void readAveVectors()
	{
		try
		{
			for(int i=0; i < NUM_CATEGORIES; i++)
			 countVector[i] = aveFile.readInt();
			
			for(int i=0; i < NUM_CATEGORIES; i++)
			{
				for(int j=0; j < NUM_FEATURES; j++)
				{
					aveVector[i][j] = aveFile.readFloat();
			
				}
			}
		}
		catch( IOException ex)
		{
		 System.out.println(" Exception reading output file "+ex.toString());
		// System.exit(1);
		}
	}
private void openAveFile()
	{
     try
    {
       
	   aveFile = new RandomAccessFile(aveFileName, "rw");
     }
     catch( IOException e)
     {
      System.err.println(" Error while opening outputfiles (text/java) "+e.toString() );
      System.out.println("Exiting with error");
     // System.exit(1);
     }
    }

	private void closeAveFile()
	{
     try
    {
      
	   aveFile.close();
     }
     catch( IOException e)
     {
      System.err.println(" Error while closing outputfiles (text/java) "+e.toString() );
      System.out.println("Exiting with error");
    //  System.exit(1);
     }
    }
	public static void main(String[] args) 
	{
		System.out.println("Hello World!");
	}
}
