package com.nsfisdas;
import java.io.*;

public class PCA
{
 private final int NUM_COMPONENTS = 10;
 private final String inputFileName = "Equalized-Coeffs-W1-Test-S";
 //private final String outputFileName = "Equalized-PC10-W1-Test-S";
 private final String outputFileName = "FeaturesPCA1.txt";
 private float data[][];   // input data is written from 1,1 not 0,0
 private float symmat[][];
 private int n; // number of rows
 private int m; // number of columns used for PCA. but has m + 2 data 
 private float evals[];
 private float interm[];
 private RandomAccessFile input, output;

 public PCA()
 {
  readData();
  covcol();
  System.out.println(" Symmat ");
/*  for(int i=1; i <=n; i++)
   {
    for(int j=1; j<=m; j++)
    {
     System.out.print(" "+symmat[i][j]);
    }
    System.out.println("");
   }
   */
  evals = new float[m+1];
  interm = new float[m+1];
  
  tred();
//  for(int i=1; i <= m; i++)
//    System.out.println(" Evals:"+evals[i]+" interm:"+interm[i]);
  tqli();
  System.out.println(" Eigenvalues ");
 for(int j = m; j >=1; j--)
  {
   System.out.print(" "+evals[j]);
  }

  System.out.println("  ");
  System.out.println(" Writing Eigen Vectors to file");
  writeEigenVectors();

 }
 
 private void writeEigenVectors()
 {
  try
  {
	
   output.writeInt(NUM_COMPONENTS);      // like header
   for(int  j=1; j <=m; j++)
   {
    for(int i=1; i <= NUM_COMPONENTS; i++)
	{
	  output.writeFloat(symmat[j][m-i+1]);           //Eigen vectors written
System.out.print("  "+symmat[j][m-i+1]);
	 }
System.out.println("");
   }
  }
  catch( IOException ex)
  {
   System.err.println(" Error while writing Eigen Vectors to file");
  }
 }

 private void tred()
 {
  int l;
  float h, scale,g,hh,f;
  for(int i =m; i>=2; i--)
  {
   l = i-1;
   h = scale = 0.0f;
   if( l > 1)
   {
    for( int k=1; k <= l; k++)
       scale += Math.abs(symmat[i][k]);
    if( scale == 0.0)
       interm[i] = symmat[i][l];
     else
     {
      for(int k= 1; k <=l; k++)
      {
       symmat[i][k] /=scale;
       h += symmat[i][k] * symmat[i][k];
      }
      f = symmat[i][l];
      g = f > 0 ? -((float)Math.sqrt(h)) : (float)Math.sqrt(h);
      interm[i] = scale * g;
      h -= f * g;
      symmat[i][l] = f - g;
      f = 0.0f;
      for( int j=1; j <= l; j++)
      {
       symmat[j][i] = symmat[i][j]/h;
       g = 0.0f;
       for( int k=1; k <=j; k++)
         g += symmat[j][k] * symmat[i][k];
       for(int k= j+1; k <= l; k++)
         g += symmat[k][j] * symmat[i][k];
       interm[j] = g/h;
       f +=interm[j] * symmat[i][j];
      }
      hh = f /(h + h);
      for( int j =1; j <= l; j++)
      {
       f = symmat[i][j];
       interm[j] = g = interm[j] - hh * f;
       for( int k= 1; k <= j; k++)
         symmat[j][k] -= (f * interm[k] + g * symmat[i][k]);
      }
     }
    }
   else
       interm[i] = symmat[i][l];
   evals[i]= h;
   }
   evals[1] = 0.0f;
   interm[1] = 0.0f;
   for( int i=1; i <= m; i++)
   {
    l = i-1;
    if( evals[i] > 0.0001 )
    {
     for( int j=1; j <= l; j++)
     {
      g = 0.0f;
      for( int k=1; k <=l; k++)
         g += symmat[i][k] * symmat[k][j];
      for( int k=1; k <=l; k++)
         symmat[k][j] -= g* symmat[k][i];
     }
    }
    evals[i] = symmat[i][i];
    symmat[i][i] = 1.0f;
    for( int j=1; j <= l; j++)
      symmat[j][i] = symmat[i][j] = 0.0f;
  }
 }

 private void tqli()
 {
  int iter = 0;
 int mm;
  float dd, s, r, p, g, f, c, b;

  for( int i=2; i <=m; i++)
   interm[i-1] = interm[i];
  interm[m]= 0.0f;

  for(int l=1; l <=m; l++)
  {
   iter =0;
   do
   {
    for(  mm=l; mm <= m-1; mm++)
    {
     dd = Math.abs(evals[mm]) + Math.abs(evals[mm+1]);
     if ( Math.abs(interm[mm]) + dd == dd ) break;
    }
    if( mm != l)
    {
      if( iter++ == 50)
      {
        System.out.println(" TLQI did not converge ");
      //  System.exit(1);
      }
      g = (evals[l+1] - evals[l])/(2.0f * interm[l]);
      r = (float)Math.sqrt((g * g) + 1.0f);
      g = evals[mm] - evals[l] + interm[l] / (g + SIGN(r,g));
      s = c = 1.0f;
      p = 0.0f;
      for( int i= mm-1; i >= l; i--)
      {
        f = s * interm[i];
        b = c * interm[i];
        if( Math.abs(f) >= Math.abs(g))
         {
          c = g/f;
          r = (float)Math.sqrt((c* c) + 1.0f);
          interm[i+1] = f * r;
          s = 1.0f/r;
          c = c * s;
         }
         else
         {
          s = f/g;
          r = (float)Math.sqrt(( s * s) + 1.0f);
          interm[i+1] = g * r;
          c = 1.0f/r;
          s *= c;
         }
        g = evals[i+1] - p;
        r = (evals[i] - g) * s + 2.0f * c * b;
        p = s * r;
        evals[i+1] = g +p;
        g = c * r - b;
        for( int k = 1; k <=m; k++)
          {
            f = symmat[k][i+1];
            symmat[k][i+1] = s * symmat[k][i] + c * f;
            symmat[k][i] = c * symmat[k][i] - s * f;
          }
        }
       evals[l] = evals[l] - p;
       interm[l] = g;
       interm[mm] = 0.0f;
      }
    }while( mm != l);
  }
 }      
          
  
 private float SIGN(float a, float b)
 {
  if ( b < 0 )
   return (float)(- Math.abs(a));
  else
   return (float)(Math.abs(a));
 }


 private void covcol()
 {
  float mean[];
  mean = new float[m+1];
  for(int j=1;j<=m; j++)
  {
   mean[j] = 0.0f;
   for(int i=1; i <= n; i++)
   {
    mean[j] += data[i][j];
   }
   mean[j] /= n;
  }

  System.out.println("Means of column vectors");
  for(int j=1; j <=m; j++)
   System.out.print(" "+mean[j]);
  System.out.println("");
 
 
  for(int i=1; i <=n; i++)
  {
   for(int j=1; j <=m; j++)
   {
    data[i][j] -=mean[j];
   }
  }

  symmat = new float[m+1][m+1];
  for(int j1=1; j1 <= m; j1++)
  {
   for(int j2=j1; j2 <= m; j2++)
   {
    symmat[j1][j2] = 0.0f;
    for( int i = 1; i <=n; i++)
    {
     symmat[j1][j2] += data[i][j1] * data[i][j2];
    }
    symmat[j2][j1] = symmat[j1][j2];
   }
  }
 

  return;
 }

 private void readData()
 {
   
   // should follow the header of the vector file writen in Confirm.java
   openFiles();
   try
   {
    n = input.readInt();  // number of rows
	m = input.readInt();  // number of cols or the vector length
    System.out.println(" Number of Rows is "+n+" with vector Length "+m);
    data = new float[n+1][m+1];
    for(int i=1; i <= n; i++)  // for all rows
    {
     for(int j=1; j <= m; j++)  // for all elements in the vector
	 {
	  data[i][j] = input.readFloat();

	 }

    }
   }
   catch( IOException ex)
   {
    System.err.println(" Exception during reading input file..PCA");
	//System.exit(1);
   }

   /* The last two elements in the vector actually represent whether the vector represents 
      a corroded segments and its corresponding material loss. Therefore it should not be included
	  in performing PCA. m should hence be "m-2"
   */

     m = m-2;



  /* Print the data read form the file */
  
/*    for(int i=1; i <=n; i++)
   {
    for(int j=1; j<=m; j++)
    {
     System.out.print(" "+data[i][j]);
    }
    System.out.println("");
   }

  */
}

private void openFiles()
 {
  try
  {
   input = new RandomAccessFile(inputFileName,"r");  
   output = new RandomAccessFile(outputFileName, "rw");
  }
  catch( IOException ex)
  {
   System.err.println(ex.toString() );
  }
 }
 public static void main(String args[])
 {
  PCA pca;
  pca = new PCA();
 }
}
