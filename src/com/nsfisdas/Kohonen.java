package com.nsfisdas;
import java.io.*;

public class Kohonen
{
  private int vectorDim;
  private int clusters;
  private DataInputStream input = null;  // for trainfile
  private DataInputStream centerFile = null; // for center file
  private DataOutputStream writeCenter = null;  //to write center file
  private String trainFileName;  // this is set using a member function
  private String centerFileName; // set by function
  private double MinVector[];  // stores the min values
  private double MaxVector[];  // stores max vector
  private double Centers[][];
  private int numMembers[];
  private boolean random;

 public Kohonen( int Vector_Dim, int numClusters)
 {
  vectorDim = Vector_Dim;
  clusters = numClusters;
  MinVector = new double[vectorDim];
  MaxVector = new double[vectorDim];
  // System.out.println(" In Kohonen ");
  numMembers = new int[clusters];
 }

 public void setFileName( String file_name )
 {
  trainFileName = file_name;
 } 
 
 public void setCenterFileName( String file_name)
 {
  centerFileName = file_name;
 }

 public void TrainKohonen()
 {
   try
   {
    FileInputStream in = new FileInputStream( trainFileName);
    input = new DataInputStream(in);
   }
   catch ( FileNotFoundException e)
   {
    System.out.println(" File not found ");
   }
   setCenters();
   setMinMax();
   

   if (!random)  closeCenterFile(); // close it since its read only
   OpenCentertoWrite();  // to write the on to the center file
   updateCenters();
   writeCenterFile();
 }
 
 private void updateCenters()
 {
  int i, belongsTo, times;
  double feature[];
  boolean end = false;
  feature = new double[vectorDim];
  try
   {
    FileInputStream in = new FileInputStream( trainFileName);
    input = new DataInputStream(in);
   }
   catch ( FileNotFoundException e)
   {
    System.out.println(" File not found ");
   }
 if ( random) 
   times =20; 
 else
   times = 8;
 
  for( int ctr = 0; ctr < times; ctr++)  // do it for ten times if random else 					//	once
  { 
  
  while( !end )
  {
    try
    {
     for(i=0; i < vectorDim; i++)  // get one feature vector
     {
      feature[i] = input.readDouble();
      feature[i] = (feature[i] - MinVector[i])/(MaxVector[i] - MinVector[i]);
   
     }
    }
    catch (EOFException eof)
    {
     //System.out.println(" EOF reading trainfile occurs always. no probs!!");
     end = true;
     break;
    }
    catch (IOException ex)
    {
     System.out.println(" IO Exception ");
     end = true;
     break;
    }

     Group(feature);

  } //while
   end = false;
   try
   { 
   input.close();
   }
   catch (IOException ex)
    {
     System.out.println(" IO Exception while closing training file");
    }
   // open again so that the file pointeer is at the start
   // this part is slow becoz of this/
   // MUST MODIFY THIS!!!!!
   try
   {
    FileInputStream in = new FileInputStream( trainFileName);
    input = new DataInputStream(in);
   }
   catch ( FileNotFoundException e)
   {
    System.out.println(" File not found ");
   }
  } // for 10 times if randomly initialized centers
  // close for the last time
  try
   { 
   input.close();
   }
   catch (IOException ex)
    {
     System.out.println(" IO Exception while closing training file");
    }
 }

 
 private void Group( double vector[])
 {
  double centerDistances[];
  int closestCenter[];
  centerDistances = new double[clusters];
  closestCenter  = new int[clusters];
  double cent2cent;
  double minCenterDistance =  99.9E5;
  double maxcent2cent;
  int closecent = 0;

 for( int from = 0; from < clusters; from++)
  {
    maxcent2cent = 9.99E15;
    for( int to = 1; to < clusters; to++)
    {
     cent2cent  = EucledianDistance( Centers[from],Centers[ (from + to)% clusters]);
     if (cent2cent < maxcent2cent )
      {
       maxcent2cent = cent2cent;
       closecent = (from + to)%clusters;
      }
    }
    centerDistances[from] = maxcent2cent;   // distance to the closest cluster
    closestCenter[from] = closecent;      // cluster number

  }
    
  int minCenter = 0;
  minCenterDistance = 9.99E15;

  for( int i = 0; i < clusters; i++)
  {
   if ( centerDistances[i] < minCenterDistance )
    {
     minCenterDistance = centerDistances[i];  //has the distacne between closest clusters
     minCenter = i;    // has the "from" of the closest clusters
    }
  }

  int closestTo = 0;
  double DistancetoCenter;
  double MinDistance =  99.99E5;
 
  for (int i =0; i < clusters; i++)
  {
    DistancetoCenter = EucledianDistance(Centers[i], vector );
    if ( DistancetoCenter < MinDistance)
    {
     closestTo = i;           // the vector is closest to this cluster
     MinDistance = DistancetoCenter;   // and this is the distance
    }
  } 
  // see if the new vector does not belong to any cluster

   if ( MinDistance <=  minCenterDistance ) // vector belongs to some cluster
    {
   numMembers[closestTo]++;
   // adjust center
    for ( int i =0; i < vectorDim; i++)
       Centers[closestTo][i] = ((Centers[closestTo][i]*(numMembers[closestTo]-1)) + vector[i])/numMembers[closestTo];
  
    }
   else
    {
   // club the closest centers. minCenter is the "from" of the closest clusters

   int pair = closestCenter[minCenter]; // the "to" of the closest cluster

   for(int i =0; i < vectorDim; i++)
    {
     Centers[minCenter][i] = ((Centers[minCenter][i] * numMembers[minCenter]) + ( Centers[pair][i] * numMembers[pair]))/(numMembers[minCenter] + numMembers[pair]);
    }
   // make the new vector as the cluster center of "to" which is "pair"

    for( int i =0; i< vectorDim; i++)
      Centers[pair][i] = vector[i]; 
 
    }
}
 
 private void writeCenterFile()
 {
   int i,j;
   System.out.println("Writing center file");
   try
   {
   for( i = 0; i < clusters; i++)
   {
    for ( j= 0; j < vectorDim; j++)
     {
      writeCenter.writeDouble(Centers[i][j]);
     }
   }
  // write numbers of members in each clusters

   for(i=0; i< clusters; i++)
   {
    writeCenter.writeInt(numMembers[i]);
   }

   // write minimum values
   for ( j= 0; j < vectorDim; j++)
     {
      writeCenter.writeDouble(MinVector[j]);
     }
   
 //  write maximum values
    for ( j= 0; j < vectorDim; j++)
     {
      writeCenter.writeDouble(MaxVector[j]);
     }
   }
   catch ( IOException e)
  {
   System.out.println(" IO Exception in writing center file");
  }
  // System.exit(0);
 } 

 private void OpenCentertoWrite()
 {
  try 
   {
    FileOutputStream out = new FileOutputStream( centerFileName);
    System.out.println("Kohonen:"+centerFileName);
    writeCenter = new DataOutputStream( out);
   }
   catch ( FileNotFoundException filenotfound)
   {
    System.out.println(" Could not open centerfile to write");
   // System.exit(0);
   }
   catch ( IOException e)
   {
    System.out.println(" IOException opening centerfile to write"); 
   // System.exit(0);
   }
  }


 private void closeCenterFile()
 {
  try
  {
  centerFile.close();
  } 
    catch (IOException ex2)
    {
     System.out.println(" IO Exception while closing center file");
    }
 }

 private void setCenters()
 {
  Centers = new double[clusters][vectorDim];
  try
  {
   FileInputStream centerFileStream = new FileInputStream( centerFileName );
   centerFile = new DataInputStream( centerFileStream );
  }
  catch ( FileNotFoundException e)
  {
    System.out.println("Center File not found ");
    System.out.println("Initializing centers Randomly ");
    random = true;
    InitializeCentersRandomly();
   return;
  }
  InitializefromFile();
 }

 private void InitializeCentersRandomly()
 {
  final double MIN = 9999999, MAX = -9999999;  // this is right way to get
						// min and max
  System.out.println(" Initializing Randomly ");
  for(int i =0; i < clusters; i++)
   for(int j =0 ; j < vectorDim; j++)
      Centers[i][j] = Math.random();

  for(int i =0; i < clusters; i++)
       Centers[i][vectorDim -1 ] = 0;  // last feature

 // initialize number of members in each cluster to zero
  for(int i =0; i< clusters; i++)
    numMembers[i] =1;


  // initialize min and max
    for (int i =0; i < vectorDim; i++)
  {
    MinVector[i] = MIN;  // has max now
    MaxVector[i] = MAX;  //has min 
  }  

 }
 
 private void InitializefromFile()
 {
  int i;
  double feature[];
  feature = new double[vectorDim];
  int clustersRead = 0;
  System.out.println(" Initializing from file ");
  
  // initialize centers
  
 while( clustersRead != clusters)
  {
   try
   {
   for(i=0; i < vectorDim ; i++)  // get one feature vector
      Centers[clustersRead][i] = centerFile.readDouble();
   }
   catch (EOFException eof)
   {
    System.out.println("Error: Center Initialization 1");
    break;
   }
   catch (IOException ex)
   {
    System.out.println(" Error:IO Exception during center initialization ");
    break;
   }
   clustersRead++;
  }

  try
  {

  // initialize numMembers, minVectors and MaxVectors
   for( i =0; i < clusters; i++)
     numMembers[i] = centerFile.readInt();
  
   for( i =0; i < vectorDim; i++)
     MinVector[i] = centerFile.readDouble();

   for(i =0; i < vectorDim; i++)
     MaxVector[i] = centerFile.readDouble(); 

   }
   catch (EOFException eof)
   {
    System.out.println("Error: Center Initialization 2");
   }
   catch (IOException ex)
   {
    System.out.println(" Error:IO Exception during center initialization ");
   }
 }

 private double EucledianDistance( double vector1[], double vector2[])
 {
  double distance = 0;
  for(int i=0; i < vectorDim; i++)
    distance = distance + Math.pow(( vector1[i] - vector2[i] ),2);
  return  Math.sqrt(distance);
 }

 private void setMinMax()
 {
  // this can be done from the center file also.
  // load from center file if present.
  int i;
  double feature[];
 
  boolean end = false;
  feature = new double[vectorDim];

  while( !end )
  {
   for(i=0; i < vectorDim; i++)  // get one feature vector
   {
    try
    {
     feature[i] = input.readDouble();
    }
    catch (EOFException eof)
    {
     System.out.println(" End of File reading trainfile");
     end = true;
     break;
    }
    catch (IOException ex)
    {
     System.out.println(" IO Exception ");
     end = true;
     break;
    }
   }  // end of for

   if (!end)
   {
    for (i =0; i < vectorDim; i++)
    {
     if ( MinVector[i] > feature[i])
        MinVector[i] = feature[i];

     if (MaxVector[i]  < feature[i])
        MaxVector[i]= feature[i]; 
    } //for
   } // if

  } // while
  try
  {
   input.close();
  }  
   catch (IOException ex2)
    {
     System.out.println(" IO Exception while closing after setMinMax");
    }
 } //func

}
