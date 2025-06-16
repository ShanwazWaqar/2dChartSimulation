package com.nsfisdas;
public class QtClusters
{
 private double centers[][];
 private int numMembers[];
 private int numClusters;
 private int numFeatures;

 public QtClusters(double cent[][])
 {
  numClusters = cent.length;
  numFeatures = cent[0].length;
  centers = new double[numClusters][numFeatures];
  numMembers = new int[numClusters];

 for(int i = 0; i < numClusters; i++ )
  {
   for( int j = 0; j < numFeatures; j++)
    {
     centers[i][j] = cent[i][j];
    }
   numMembers[i] = 1;
  }
 
 }

 public void ClassifyFeature(double feat[])
 {
  double minBetweenCenters = 9.9E15;
  double maxcent2cent;
  double minPointCenter;
  int closestCenter[];   // stores the closest cluster number to it
  double centerdis[];      // stores the distence to the nearest cluster
  centerdis = new double[numClusters];
  int closecent =0;
  closestCenter = new int[numClusters];
  int minCenter;   
  double cent2cent;

  for( int from= 0; from < numClusters; from++)
  {
   maxcent2cent = 9.99E15;
   for( int to = 1; to < numClusters; to++)
   {
    cent2cent =  EucledianDistance( centers[from], centers[(from + to)%numClusters]);
    if (cent2cent < maxcent2cent )
     {
      maxcent2cent = cent2cent;
      closecent = (from + to)%numClusters;
     }
   }
   centerdis[from] = maxcent2cent;   // distance to closest cluster
   closestCenter[from] = closecent;  // "to" cluster number
  }
  

  // now find the closest pair
  minCenter = 0;
  minBetweenCenters = 9.9E15;
  for( int i=0; i < numClusters; i++)
  {
   if (centerdis[i] < minBetweenCenters ) 
   {
    minBetweenCenters = centerdis[i]; // has the distance between the closest pair
    minCenter = i; // has the "from"
   }
  }

 // now find the closest cluster to the vector
  minPointCenter = 9.99E15;
  double value;
  int closestTo = 0;
  for( int i = 0; i < numClusters; i++)
  {
   value = EucledianDistance(feat, centers[i]);
   if (value < minPointCenter)
    {
     closestTo = i;
     minPointCenter = value;
    }
  }
 
  if ( minPointCenter < minBetweenCenters ) // belongs to the closest cluster
   {
    AddVector(centers[closestTo] , closestTo , feat);
    numMembers[closestTo]++;
   }
   else
   {
    // club the closest pair
    ClubCluster(centers[minCenter], centers[closestCenter[minCenter]], minCenter, closestCenter[minCenter]);
    // put feat into "to"

   Transfer(feat, centers[closestCenter[minCenter]]);
   numMembers[closestCenter[minCenter]] = 1;
  
   } 
 }

 public int FindCluster( double vect[])
 {
  double max = 9.9E15;
  int res = 0;
  double val;

  for(int i=0; i < numClusters; i++)
   {
    val = EucledianDistance(centers[i], vect);
    if ( val < max )
    {
     max = val;
     res = i;
    }
  }
  return res;
 } 

 private void Transfer( double fromVec[], double toVec[] )
 {
  for(int i =0; i < numFeatures; i++)
  {
   toVec[i] = fromVec[i] ;
  }
 }

 private void ClubCluster(double centerfrom[], double centerto[], int clusterfrom, int clusterto)
 {
  System.out.println(" Centers changes ");
  for(int i=0; i < numFeatures; i++)
  {
   centerfrom[i] = (centerfrom[i] * numMembers[clusterfrom] + centerto[i] * numMembers[clusterto])/(numMembers[clusterfrom] + numMembers[clusterto]);
  }
  numMembers[clusterfrom] = numMembers[clusterfrom] + numMembers[clusterto];
 } 

 private double EucledianDistance( double To[], double From[])
 { 
   double dis = 0;
   for( int i=0; i < numFeatures; i++)
   {
    dis = dis + Math.pow((To[i] - From[i]), 2);
   }
  return dis;
 }

 private void AddVector(double bulk[],int clust, double addThis[])
 {
  for( int i =0; i < numFeatures; i++)
  {
   bulk[i] = (bulk[i] * numMembers[clust] + addThis[i])/(numMembers[clust] + 1);
  }
 }
}
