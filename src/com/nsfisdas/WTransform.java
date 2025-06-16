package com.nsfisdas;
public class WTransform
{
 private final int vectorDim = 15;
 private final int clusters = 6;
 private float Min[], Max[];
 float Centers[][];
 float numMembers[];

 private ImageMatrix orgImg;
 private int rows, cols;
 private float Mat[][];

 private float R[][],G[][],B[][];

 private int SR[][],SG[][],SB[][];

 private int red[][], green[][], blue[][];  

 private float LL[][], LH[][], HL[][], HH[][];

 private ImageMatrix segImage;

 public WTransform(ImageMatrix setImg)
 {
  orgImg = setImg;
  rows = orgImg.getRows();
  cols = orgImg.getCols();

  red = orgImg.getRedPointer();
  green = orgImg.getGreenPointer();
  blue = orgImg.getBluePointer();

  Mat = new float [rows * cols][vectorDim];
  R = new float [rows][cols];
  G = new float [rows][cols];
  B = new float [rows][cols];

  for(int i=0; i< rows; i++)
  {
   for(int j=0; j <cols; j++)
   {
    Mat[cols * i + j][0]= R[i][j] = (float)red[i][j];
    Mat[cols * i + j][1]= G[i][j] = (float)green[i][j];
    Mat[cols * i + j][2]= B[i][j] = (float)blue[i][j];
   }
  }
 }

 public ImageMatrix getSegmentedImage()
 {
  segImage = new ImageMatrix();
  segImage.setRedPointer(SR);
  segImage.setGreenPointer(SG);
  segImage.setBluePointer(SB);
  return segImage;
 }

 public void Segment()
 {
  performTransform();
  NormalizeMatrix();
  float vector[];

  float minBetweenCenters = Float.MAX_VALUE;
  int minCenter;
  int closestTo = 0;
  float value;
  float minPointCenter = Float.MAX_VALUE;;
  float centerdis[]; 
  int closestCenter[];
  int numMembersInCluster[];

  centerdis = new float[clusters];
  numMembersInCluster = new int[clusters];
  closestCenter = new int[clusters];
 
  Centers = new float[clusters][vectorDim];

 for(int c=0; c<clusters; c++)
  {
   int i,j;
   i = (int)(Math.random() * rows * cols);
 
   for( int k=0; k < vectorDim; k++)
   {
    Centers[c][k] = Mat[i][k]; 
   }
   numMembersInCluster[c] = 1; 
  }
// number of times the list is gone thru to change centers

int times = 1;
float maxcent2cent;
int closecent =0;
float cent2cent;

for ( int count = 0; count < times; count++)
{
  // for all the pixels go through the procedure

  for(int r =0; r < rows*cols ; r++)
  {
  
   // centers of clusters found and closest calculates
     
   for( int  from = 0; from < clusters; from++)
    {
     maxcent2cent = Float.MAX_VALUE;
     for ( int to = 1; to < clusters; to++) 
     {
       cent2cent = EucledianDistance(Centers[(from+to)%clusters],Centers[from]);
       if ( cent2cent < maxcent2cent )
        {
          maxcent2cent = cent2cent;
          closecent = (from + to)%clusters;
        }
      }
     centerdis[from] = maxcent2cent;    // this has the distance to the closest center
     closestCenter[from] = closecent;   // this has the cluster number of the closest center
    } 
  
    minCenter = 0;
    minBetweenCenters = Float.MAX_VALUE;
    for(int i =0; i< clusters; i++)     // finding the closest pair of clusters ( from )
    {
      if (centerdis[i]  < minBetweenCenters   )
       {
         minBetweenCenters = centerdis[i];  // has the distance between closest clusters
         minCenter = i;                    // minCenter has "from"
       }
    }
  
   // fill the vector
  vector = Mat[r];

 // find the closest cluster 
  
    minPointCenter = Float.MAX_VALUE;   // for distance from vector to closest center

    for (int i =0; i < clusters; i++)
    {
      value = EucledianDistance(Centers[i],vector);
   
      if ( value < minPointCenter )
      {
        closestTo = i;            //  this is the nearest cluster
        minPointCenter = value;
      }
    }

    if ( minPointCenter < minBetweenCenters )   // belongs to "closestTo" cluster
     {
       // adjust the center

        for(int k=0; k<vectorDim; k++)
        {
	  Centers[closestTo][k] = (Centers[closestTo][k] * numMembersInCluster[closestTo] + vector[k])/(numMembersInCluster[closestTo] + 1);
        }
        numMembersInCluster[closestTo]++;
     }
     else
     {
        // new center  the closest centers are clubbed here

      for(int k=0; k < vectorDim; k++)
      {
       Centers[minCenter][k] = (Centers[minCenter][k]*numMembersInCluster[minCenter] + numMembersInCluster[closestCenter[minCenter]]*Centers[closestCenter[minCenter]][k])/(numMembersInCluster[minCenter]+numMembersInCluster[closestCenter[minCenter]]);
       }

       numMembersInCluster[minCenter] = numMembersInCluster[minCenter] + numMembersInCluster[closestCenter[minCenter]];

      // the new vector becomes the center at "to" that is closestcenter[minCent]
       for(int k=0; k<vectorDim; k++)
         Centers[closestCenter[minCenter]][k]=vector[k];

        numMembersInCluster[closestCenter[minCenter]] = 1;

     } // end of else
 
  } //for r
    //  for (int q = 0; q < clusters; q++)
    //  {
    //   System.out.println(" Cluster " + q + " members " + numMembersInCluster[q] );
    //  }
 }// count --> times the loop is performed
 Segmentation();
}//func


 private float EucledianDistance( float vector1[], float vector2[])
 {
  float distance = 0;
  for(int i=0; i < vectorDim; i++)
    distance = distance + (float)Math.pow(( vector1[i] - vector2[i] ),2);
  return  distance;
 }


 private void NormalizeMatrix()
 {
  Min = new float[vectorDim];
  Max = new float[vectorDim];
  Min[0] = 0;
  Max[0] = 255;

  float min;
  float max; 
 
  for(int k=1; k < vectorDim; k++)
  {
   Min[k] = Float.MAX_VALUE;
   Max[k] = Float.MIN_VALUE;
  }


   for(int i=0; i <rows*cols; i++)
   {
    for(int k=0; k < vectorDim;k++)
    {
     if (Min[k] > Mat[i][k]) Min[k] = Mat[i][k];
     if (Max[k] < Mat[i][k]) Max[k] = Mat[i][k];
    }
   }


   for(int i=0; i < rows * cols; i++)
   {
    for(int k=0; k < vectorDim; k++)
    {
     Mat[i][k] = (Mat[i][k] - Min[k])/(Max[k] - Min[k]);
    }
   }
  
 }





 public void performTransform()
 {

  Convolve convolveR, convolveG, convolveB;
  LL = new float[rows][cols];
  LH = new float[rows][cols];
  HL = new float[rows][cols];
  HH = new float[rows][cols];

  convolveR = new Convolve(R,LL,LH,HL,HH);
   for(int i=0; i<rows; i++)
   {
     for(int j=0; j<cols; j++) 
     {
      Mat[cols * i + j][3]= LL[i][j];
      Mat[cols * i + j][4]= LH[i][j];
      Mat[cols * i + j][5]= HL[i][j];
      Mat[cols * i + j][6]= HL[i][j];
     }
   }
  convolveG = new Convolve(G,LL,LH,HL,HH);
  for(int i=0; i<rows; i++)
   {
     for(int j=0; j<cols; j++) 
     {
      Mat[cols * i + j][7]= LL[i][j];
      Mat[cols * i + j][8]= LH[i][j];
      Mat[cols * i + j][9]= HL[i][j];
      Mat[cols * i + j][10]= HL[i][j];
     }
   }


  convolveB = new Convolve(B,LL,LH,HL,HH);
  for(int i=0; i<rows; i++)
   {
     for(int j=0; j<cols; j++) 
     {
      Mat[cols * i + j][11]= LL[i][j];
      Mat[cols * i + j][12]= LH[i][j];
      Mat[cols * i + j][13]= HL[i][j];
      Mat[cols * i + j][14]= HL[i][j];
     }
   }

 // DisplayImage displayO = new DisplayImage("Original",red,green,blue); //Mat[0],Mat[1],Mat[2]);

 }

 private int FindClosestCenter(float vector[])
{
 int retCtr=0, i;
 float maxValue = Float.MAX_VALUE;
 float value;
 for( i =0; i < clusters; i++)
 {
  value = EucledianDistance(vector,Centers[i]);
  if ( value < maxValue )
   {
    retCtr = i;
    maxValue = value;
   }
 }
 return retCtr; 

}
 public void Segmentation()
 {
 int i, j, ctr, center;
 SR = new int[rows][cols];
 SG = new int[rows][cols];
 SB = new int[rows][cols];
 float vector[];
 vector = new float[vectorDim];
 for ( i=0; i < rows; i++)
 {
   for ( j=0; j < cols; j++)
    {
     center = FindClosestCenter(Mat[i*cols + j]);
     SR[i][j] = (int)(Centers[center][0] * 255);
     SG[i][j] = (int)(Centers[center][1]* 255);
     SB[i][j] = (int)(Centers[center][2] * 255);

    }
  }
 //DisplayImage displayO = new DisplayImage("Segmented D-1",SR,SG,SB);

 } 
}

