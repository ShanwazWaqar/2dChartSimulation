package com.nsfisdas;
import java.util.*;

public class BPLayer
{
 private int Units;
 private float Output[];
 private float Error[];
 private float Weight[][];
 private float WeightSave[][];
 private float dWeight[][];

 private int BIAS = 1;
 private float Gain = 1.0f; // actually belongs to BPNet but used here too. 
 private float Eta = 0.25f;  // these three values are actually used in BPLayer
 private float Alpha = 0.9f;



 public BPLayer() {}

 public void setUnits(int num)
 { //System.out.println(" setting units");
   Units = num;  }

 public void allocateOutput(int num)
 {   Output = new float[num];  }
 
 public void allocateError(int num)
 {   Error = new float[num];  }

 public void allocate1(int num)
 {
    Weight = new float[num][];  
    WeightSave = new float[num][];   
    dWeight = new float[num][]; 
 }

 public void setBias()
 {   Output[0] = BIAS;  }

 public void allocate2(int num)
 {
  for (int i = 1; i <= Units; i++)
  {
   Weight[i] = new float[num];
   WeightSave[i] = new float[num];
   dWeight[i] = new float[num];
  }
 }

 public void setRandomWeights(int lUnits, int lmin1Units)
 {
  Random ran;
  ran = new Random((long)(93063));

  for (int i = 1; i <= lUnits; i++)
  {
   for( int j =0; j <= lmin1Units; j++)
   {
    Weight[i][j] = (float)( ran.nextFloat()  - 0.5f);
   }
  }
 }

 public float[][] getWeights()
 {
  return Weight;
 }
 
 public void setWeights(float[][] wts)
 {
  Weight = wts;
 }
  
 public void setInputLayer(float input[])
 {
  for(int i =1; i<= Units; i++)
   Output[i] = input[i-1];
 }

 public void getOutput( float output[])
 {
  for(int i=1; i <= Units; i++)
  {
   output[i-1] = Output[i];
   //System.out.println("O:"+output[i-1]);
  }

 }
 
 public void saveWeights(int lUnits, int lmin1Units)
 {
  for(int i =1; i<= lUnits; i++)
  {
   for(int j = 0; j<=lmin1Units; j++)
   {
    WeightSave[i][j] = Weight[i][j];
   }
  }
 }

 public void restoreWeights(int lUnits, int lmin1Units)
 {
  for(int i =1; i<= lUnits; i++)
  {
   for(int j = 0; j<=lmin1Units; j++)
   {
    Weight[i][j] = WeightSave[i][j];
   }
  }
 }

 public void propagateLayer( BPLayer upper)  //of lower
 {
  float sum;
  for(int i=1; i<=upper.Units; i++)
  {
   sum =0;
   for(int j=0; j <=Units; j++)
    {
     sum = sum + upper.Weight[i][j] * Output[j] ;
    }
   upper.Output[i] = (float)( 1/(1 + Math.exp(-Gain * sum )));
  }
 }

 public void propagateNet(BPLayer layerp1) //of layer l 
 {
   propagateLayer(layerp1);
 }

 public float computeOutputError( float Target[]) // returns net error to BPNet
 {
  float Out, Err, result =0;
  for( int i =1; i <=Units; i++)
  {
   Out = Output[i];
   Err = Target[i-1] - Out;
   //System.out.println("Target ="+ Target[i-1]+"Output="+Out+"Err="+Err);
   Error[i] = Gain * Out * (1-Out) * Err;
   result = result + 0.5f * (Err * Err );
  }
  return result;
 }

 public void backpropagateLayer(BPLayer lower)  // called by upper
 {
  float Out, Err;
  for(int i =1; i <= lower.Units; i++)
  {
   Out = lower.Output[i];
   Err = 0;
   for( int j = 1; j <= Units; j++)
   {
    Err = Err + Weight[j][i] * Error[j];
   }
   lower.Error[i] = Gain * Out * (1 - Out) * Err;
  }
 }
  
 public void adjustWeights( BPLayer lmin1Layer) // called by l layer 
 {
  float Out, Err, dWt;
  for( int i = 1; i<=Units; i++)
  {
   for( int j=0; j <=lmin1Layer.Units; j++)
   {
    Out = lmin1Layer.Output[j];
    Err = Error[i];
    dWt = dWeight[i][j];
    Weight[i][j] = Weight[i][j] + Eta*Err*Out + Alpha * dWt;
    dWeight[i][j] = Eta*Err*Out;  
   }
  }
 }

}
