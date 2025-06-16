package com.nsfisdas;
import java.util.*;
import java.io.*;

public class BPNetSave
{

 private int NumLayers = 4;
 private int N = 10; // number of input nodes/units/features
 private int M = 1; // number of output features
 private int Units[] = { N, 8,4, M} ; // The units are given in the constructor

 private BPLayer Layers[];
 private BPLayer InputLayer;
 private BPLayer OutputLayer;
 private float Alpha;
 private float Eta;  // these three values are actually used in BPLayer
 private float Gain;
 private float Error;
 private int Epoch = 1;
 private DataOutputStream wtToFile;
 private DataInputStream wtFromFile;

 public BPNetSave()
 {
 
  Layers = new BPLayer[NumLayers];
  
  for( int l = 0; l < NumLayers; l++)
  {
   Layers[l] = new BPLayer();
   Layers[l].setUnits(Units[l]);
   Layers[l].allocateOutput(Units[l] + 1);
   Layers[l].allocateError(Units[l] + 1);
   Layers[l].allocate1(Units[l] + 1);  // Weight, WeightSave and dWeight [] allocates
   Layers[l].setBias(); // Bias declared in BPLayer

   if (l != 0)
    {
     Layers[l].allocate2(Units[l-1] + 1);  // Weight, WeightSave and dWeight [][] allocated
    
    }
  }
  InputLayer = Layers[0];
  OutputLayer = Layers[NumLayers - 1];
  Alpha = 0.9f;
  Eta = 0.2f; // if changed here change in BPLayer also
  Gain = 1.0f;
  
  RandomWeights();
  
 }
 
 public void RandomWeights()
 {
  
  for(int l = 1; l < NumLayers; l++)
  {
   Layers[l].setRandomWeights(Units[l], Units[l-1]);
  }
  System.out.println(" Initialised weights randomly ");
 } 

 public void SetInput(float Input[])
 {
  InputLayer.setInputLayer(Input);
 }

 public void GetOutput( float Output[])
 {
  OutputLayer.getOutput( Output );
 }

 public void SaveWeights()
 {
  for(int l =1; l < NumLayers; l++)
  {
   Layers[l].saveWeights(Units[l], Units[l-1]);
  }
 }

 public void RestoreWeights()
 {
  for(int l = 1; l< NumLayers; l++)
  {
   Layers[l].restoreWeights(Units[l], Units[l-1]);
  }
 }

 public void PropagateLayer(BPLayer Lower, BPLayer Upper)
 {
  Lower.propagateLayer(Upper);
 }
 
 public void PropagateNet()
 {
   for(int l = 0; l < NumLayers - 1 ; l++)
   {
    Layers[l].propagateLayer(Layers[l+1]);
   }
 }

 public void ComputeOutputError( float Target[])
 {
   Error = OutputLayer.computeOutputError(Target);

 }

 public void BackpropagateLayer( BPLayer Upper, BPLayer Lower)
 {
  Upper.backpropagateLayer(Lower);
 }

 public void BackpropagateNet()
 {
  for(int l=NumLayers -1; l > 1; l--)
  {
   BackpropagateLayer(Layers[l],Layers[l-1]);
  }
 }

 public void AdjustWeights()
 {
  for(int l = 1; l <NumLayers; l++)
  {
   Layers[l].adjustWeights(Layers[l-1]);
  }
 }

 public void SimulateNet(float Input[], float Output[], float Target[], boolean Training)
 {
  SetInput(Input);
  PropagateNet();
  GetOutput(Output);
  ComputeOutputError(Target);
  
  if (Training)
  {
   BackpropagateNet();
   AdjustWeights();
  }
   
 }

 public void TestSimNet( float Input[], float Output[])
 {
  SetInput(Input);
  PropagateNet();
  GetOutput(Output);
 }

 public float TrainNet(float TrainFeature[]) // feature vector includes flag(s)
 {
  float Output[];
  Output = new float[M];

  float Target[];
  Target = new float[M];
  for( int i =0; i< M; i++)
    Target[i] = TrainFeature[N + i];
  Error = 0;
  float TrainError =0;
 
  for( int n=0; n < Epoch; n++)
  {
   SimulateNet( TrainFeature, Output, Target, true);
   TrainError = TrainError + Error;
  }
  
  return TrainError;
 }
 
 public void TestNet(float TestFeature[], float output[])
 {
  float Target[];
  Target = new float[M];
  for( int i =0; i< M; i++)
       Target[i] = TestFeature[N + i];
  
// SimulateNet(TestFeature, output, Target, false);
 TestSimNet(TestFeature, output);
  //System.out.println(" Test Error "+ Error);
 }

  public void WeightsToFile(String fileName)
 {
   try 
   {
	   
    FileOutputStream out = new FileOutputStream(fileName);
    System.out.println("BPNetSave:"+fileName);
    wtToFile = new DataOutputStream( out);
   }
   catch ( FileNotFoundException filenotfound)
   {
    System.out.println(" Could not open centerfile to write");
   // System.exit(0);
   }
 
   try
   {
    wtToFile.writeInt(NumLayers);
    wtToFile.writeInt(N);
    wtToFile.writeInt(M);

    for(int i=0; i < Units.length; i++)
    {
     wtToFile.writeInt(Units[i]);
    }
    float weights[][];
    for(int l = 1; l < NumLayers; l++)
    {
     weights = Layers[l].getWeights();
     for(int i= 1; i <= Units[l]; i++)
      {
       for( int j=0; j <= Units[l-1]; j++)
        {
          wtToFile.writeFloat(weights[i][j]);
        }
      }
    }
   }
   catch ( IOException e)
   {
    System.out.println(" IO Exception in writing center file");
   }
 }


 public void TestBP()
 {
   float  x,y,z;
   float i,j,k;
   float vector[];
   vector = new float[2];
   float TrainErr =0;
  float res[];
  res = new float[1];

  float maxError = 99999;

  for (int iteration =0; iteration < 10; iteration ++)
 {
  TrainErr = 0;
  for (int times = 0; times < 200000; times ++)
  {
   x = vector[0] = (float)(Math.random() * 4);
   y = vector[1] = (2 *x * x * x)/(2 * 4 * 4 * 4);
   
   TrainErr += TrainNet(vector);
   SaveWeights();
  }
  System.out.println(" TRain Error "+TrainErr);
 }  
   x = vector[0] = (float) (Math.random() * 4);
   y = vector[1] = (2*x*x*x)/(2*4*4*4);

   
   TestNet(vector,res);
   System.out.println("x =" +x);
   System.out.println("y = "+y+ " GOT THIS"+res[0]);

 }



}