package com.nsfisdas;
import java.io.*;

class Projection 
{
   private int n; // number of rows
   private int m =15; // number of columns used for PCA. but has m + 2 data
   private float data[][];   // input data is written from 1,1 not 0,0   
   private final int NUM_PCA_COMP = 10;  // the number of samples that are to be formed using the pixels
   private float Mat[][];
   private RGBList rgbList;
   private int rows, cols, count;
   private int coords[]; // i * (cols) + j
   private float average[];	
   private String PCFile = "Equalized-PC10-W1-";
   //private String DataFile = "Equalized-Coeffs-W1-";
   private float eigenSpace[][];
   private float aveCoeffs[];
   private float featArray[];
   private RandomAccessFile pcFile;
   private RandomAccessFile dtFile;
   private String Imagefilename;
   private File file_pca;
   private File file_wave;
   private PrintWriter print_pca;
   private PrintWriter print_wave;
   public Projection( float waveMat[][], String catString, String filename)
   {
    PCFile = new String(PCFile+catString);
    System.out.println("PCFile:"+PCFile);
    Mat = waveMat;
    Imagefilename = filename;
    openPCFile();
    readEigenSpace(); 
    file_pca = new File ("C:/tmp/images/Features_"+Imagefilename+".pca");
    file_wave = new File ("C:/tmp/images/Features_"+Imagefilename+".wave");
    try{
    print_pca = new PrintWriter (new BufferedWriter (new FileWriter (file_pca)),true);
    print_wave = new PrintWriter (new BufferedWriter (new FileWriter (file_wave)),true);
    
    /*if (print_writer.checkError ()) {
	       System.out.println ("An output error occurred!" );
	   }*/
	}
	catch(IOException ex)
	{
	System.err.println(" Error reading Eigen Space ");
	//System.exit(1);
	}
   }
 
  public void setRGBList(RGBList RGBlist)
  {
   this.rgbList = RGBlist;
  }

 public void closeFiles()
 {
  try
  {
    pcFile.close();
  }
  catch(IOException ex)
  {
    System.err.println(" Error closing PC file ");
  }
 }
 
  public void setRowsCols(int rows, int cols)
   {
      this.rows = rows;
      this.cols = cols;
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
	}
   }
  }
  catch(IOException ex)
  {
  System.err.println(" Error reading Eigen Space ");
  //System.exit(1);
  }
 }

 private void transformCoeffs()
 {
	// code added by bhavana sasne on 6/6/2011
     
	   featArray = new float[NUM_PCA_COMP + 1];
	   for(int k=0; k <NUM_PCA_COMP; k++)
	   {
	    featArray[k] = 0.0f;
		for(int k2 = 0; k2 < m; k2++)
		{
		 featArray[k] += aveCoeffs[k2] * eigenSpace[k2][k];
		// System.out.print("  "+featArray[k]);
		  //print_wave.print("  "+aveCoeffs[k2]);
		}
	    //System.out.println("");
		//print_wave.println("");
	    
	   }
	   featArray[NUM_PCA_COMP] = 0.0f; // dummy not used
	  
	  
}

 private void openPCFile()
 {
 try
  {
	System.out.println("In projection openPCFile func");
	 String filename1;
	 filename1 = "C:/tmp/readfiles/"+PCFile;
    pcFile = new RandomAccessFile(filename1, "r");
  }
  catch(IOException ex)
  {
  System.err.println(" Error while opening files ");
 // System.exit(1);
  }
 }
 

 private void formCoeffs()
 {
  formCoords();
  averageSegment();
 }

 public void formCoords()
 {
  RGBNode firstNode;
  RGBNode lastNode;
  RGBNode ptr;
  int ctr,i,j;
  ctr =0;
  count = rgbList.getNumMembers();
  firstNode = rgbList.getFirstNode();
  lastNode = rgbList.getLastNode();
  coords = new int[count];
  
  for ( ptr = firstNode; ptr != null; ptr = ptr.getNextNode() )
  {
   i = ptr.getx(); //row
   j = ptr.gety(); //col           
   coords[ctr] = i * cols + j; 
   ctr++;
  }
 
 }
 private void averageSegment()
 {
  int ctr =0;
  int numFeatures = m;
  aveCoeffs = new float[numFeatures+1];
  for(int i=0; i <  count; i++)
  {
   for(int j=0; j < numFeatures; j++)
   {
    aveCoeffs[j] += Mat[coords[i]][j];
   }
   ctr++;
  }
  //System.out.println("numFeatures:"+numFeatures);
  for(int j=0; j < numFeatures; j++){
    aveCoeffs[j] /=ctr;
    //System.out.println("  "+aveCoeffs[j]);
  }
 
 }


 public void formVector()
 {
 
  formCoeffs();
  transformCoeffs();
 }

 public float[] getProjectedVector()
 {
	 for(int k = 0; k <= 10; k++)
		{ 
		 //System.out.print("  "+aveCoeffs[k]);
		 print_pca.print("  "+featArray[k]);
		// print_wave.print("  "+aveCoeffs[k]);
		}
	    //System.out.println("");
	     print_pca.println("");
	     for(int k = 0; k <= m; k++)
			{
			   print_wave.print("  "+aveCoeffs[k]);
			}
		   print_wave.println("");
		
  return featArray;
 }
}
