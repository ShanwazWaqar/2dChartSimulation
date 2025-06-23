package com.simulation.legacy;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ImageMatrix2D
{
   public String[] Ratio1;
   public String[] Ratio2;
   public int itrCnt;
 //  public int iter;
   public int iterationNo;
   public String[] PitGrowthArray;
   public String[] PitSize;
   public float maxRHt;
   public float maxGHt;
   public float maxBHt;
   public int widthCntTot;
   public int heightCntTot;
   public String syspath;
   private float Pixels[][];
   private float init[][];
   private float prob[][];
   
   private double param1[][]; 
   private double param2[][];
   private double param3[][];
   private double param4[][];
  
   double temp1;
   int ini=0;
   int dea=0;
   int gro=0;
   int sat=0;
   
   private int Rows;
   private int Cols;
   private int f;
   private Graphics buf;
   ArrayList pixelslist = new ArrayList();
   ArrayList pitCrack = new ArrayList();
   ArrayList redpixels = new ArrayList();
  
    public ImageMatrix2D(int i, int j, int k,int iter,String path, double input1,double input2,double input3,double input4)
    {
        Rows = i;
        Cols = j;
        f = k;
        Pixels = new float[Rows][Cols];
        init = new float[Rows + 2][Cols + 2];
        prob = new float[Rows][Cols];
        
        param1 = new double[Rows][Cols];
    	param2 = new double[Rows][Cols];
    	param3 = new double[Rows][Cols];
    	param4 = new double[Rows][Cols];
    	
        itrCnt =iter;
        syspath =path;
        PitGrowthArray = new String[iter];
        PitSize = new String[iter];
        for(int l = Rows - 1; l >= 0; l--)
        {
            for(int i1 = Cols - 1; i1 >= 0; i1--)
            {
                Pixels[l][i1] = 0.0F;
                prob[l][i1] = (float)Math.random();
                param1[l][i1]=input1; //ph
                param2[l][i1]=input2; //temp
                param3[l][i1]=input3; //pot
                param4[l][i1]=input4; //concen
            }

        }

        for(int j1 = 0; j1 < Rows + 2; j1++)
        {
            for(int k1 = 0; k1 < Cols + 2; k1++){
                init[j1][k1] = (float)(Math.random() - 0.10000000000000001D);
            //    System.out.println(" init[j1][k1]:"+ init[j1][k1]);
            }

        }
      Ratio1 = new String[1];
      Ratio2 = new String[1];

    }

    public ImageMatrix2D()
    {
    }

    public float getPixel(int i, int j)
    {
        return Pixels[i][j];
    }

    public void setPixel(int i, int j, float f1)
    {
        Pixels[i][j] = f1;
    }

    public int getRows()
    {
        return Rows;
    }

    public int getCols()
    {
        return Cols;
    }

    public String stat2D()
    {
    //	Cnt++;
    	Ratio1[0] = histogram2D(Pixels, Rows, Cols);
         //  System.out.println("histogram2D(Pixels, Rows, Cols):"+histogram2D(Pixels, Rows, Cols));
        return histogram2D(Pixels, Rows, Cols);
    }

    public String histogram2D(float af[][], int i, int j)
    {
        float af1[] = new float[1000];
        float af2[] = new float[1000];
        int k = 0;
        float f1 = 0.0F; // mean
        float f2 = 0.0F; //std
        float f3 = 0.0F;  //skew
        float f4 = 0.0F;  // energy
        float f5 = 0.0F;  // entropy
        double s =0.0D;
        double e1 =0.0D;
        double e2 =0.0D;
        
        for(int l = 0; l < 1000; l++)
        {
            af1[l] = 0.0F;
            af2[l] = 0.0F;
        }

        for(int i1 = 0; i1 < i; i1++)
        {
            for(int j1 = 0; j1 < j; j1++)
            {
                int l1 = (int)af[i1][j1];
                if(l1 < -999)
                    l1 = -999;
                if(l1 > 999)
                    l1 = 999;
                k++;
                if(l1 < 0)
                    af2[-l1] = af2[-l1] + 1.0F;
                else
                    af1[l1] = af1[l1] + 1.0F;
            }

        }

        for(int k1 = 0; k1 < 1000; k1++)
        {
            af1[k1] = af1[k1] / (float)k;
            af2[k1] = af2[k1] / (float)k;
            f1 += af1[k1] * (float)k1; // mean
            f1 -= af2[k1] * (float)k1; 
            f4 += af1[k1] * af1[k1];   // energy
            f4 += af2[k1] * af2[k1];
           //if((double)af1[k1] > 9.9999999999999994E-012D)
            if((double)af1[k1] > 0.00000000001)
                f5 = (float)((double)f5 + ((double)(-af1[k1]) * Math.log(af1[k1])) / Math.log(2D));
            //if((double)af2[k1] > 9.9999999999999994E-012D)
            if((double)af2[k1] >  0.00000000001)
                f5 = (float)((double)f5 + ((double)(-af2[k1]) * Math.log(af2[k1])) / Math.log(2D));
        }

        for(int i2 = 0; i2 < 1000; i2++)
        {   // f1-> mean, f2 ->std,f3-> skew
            f2 += ((float)i2 - f1) * ((float)i2 - f1) * af1[i2];
            f2 += ((float)(-i2) - f1) * ((float)(-i2) - f1) * af2[i2];
            f3 += ((float)i2 - f1) * ((float)i2 - f1) * ((float)i2 - f1) * af1[i2];
            f3 += ((float)(-i2) - f1) * ((float)(-i2) - f1) * ((float)(-i2) - f1) * af2[i2];
        }

        f2 = (float)Math.sqrt(f2);
        f3 = f3 / f2 / f2 / f2;
        // 1st returned parameter Skew, 2nd-> Energy, 3rd-> Entropy
        s= ((double)(int)(f3 * 1000F) / 1000D);
        e1 = ((double)(int)(f4 * 1000F) / 1000D);
        e2 =((double)(int)(f5 * 1000F) / 1000D);
       
        
      //return String.format("%3.3f, %3.3f, %3.3f", ((double)(int)(f3 * 1000F) / 1000D) , ((double)(int)(f4 * 1000F) / 1000D), ((double)(int)(f5 * 1000F) / 1000D));
        return String.format("%3.3f,%3.3f,%3.3f,%3.3f", s , e1, e2, (e2/e1));
    }

    public String wavelet2D(int i)
    {

        double ad[] = new double[i]; // lowpass
        double ad1[] = new double[i]; //highpass
        if(i == 4)
        {
            ad[0] = 0.48299999999999998D;
            ad[1] = 0.83650000000000002D;
            ad[2] = 0.22409999999999999D;
            ad[3] = -0.12939999999999999D;
            ad1[0] = -0.12939999999999999D;
            ad1[1] = -0.22409999999999999D;
            ad1[2] = 0.83650000000000002D;
            ad1[3] = -0.48299999999999998D;
        } else
        if(i == 6)
        {
            ad[0] = 0.3327D;
            ad[1] = 0.80689999999999995D;
            ad[2] = 0.45989999999999998D;
            ad[3] = -0.13500000000000001D;
            ad[4] = -0.085400000000000004D;
            ad[5] = 0.035200000000000002D;
            ad1[0] = 0.035200000000000002D;
            ad1[1] = 0.085400000000000004D;
            ad1[2] = -0.13500000000000001D;
            ad1[3] = -0.45989999999999998D;
            ad1[4] = 0.80689999999999995D;
            ad1[5] = -0.3327D;
        } else
        if(i == 8)
        {
            ad[0] = 0.23039999999999999D;
            ad[1] = 0.71479999999999999D;
            ad[2] = 0.63090000000000002D;
            ad[3] = -0.028000000000000001D;
            ad[4] = -0.187D;
            ad[5] = 0.030800000000000001D;
            ad[6] = 0.032899999999999999D;
            ad[7] = -0.0106D;
            ad1[0] = -0.0106D;
            ad1[1] = -0.032899999999999999D;
            ad1[2] = 0.030800000000000001D;
            ad1[3] = 0.187D;
            ad1[4] = -0.028000000000000001D;
            ad1[5] = -0.63090000000000002D;
            ad1[6] = 0.71479999999999999D;
            ad1[7] = -0.23039999999999999D;
        } else
        {
            ad[0] = 0.70709999999999995D;
            ad[1] = 0.70709999999999995D;
            ad1[0] = 0.70709999999999995D;
            ad1[1] = -0.70709999999999995D;
        }
        float af[][] = new float[Rows][Cols];
        float af1[][] = new float[Rows][Cols];
        float af2[][] = new float[Rows / 2][Cols / 2];
        int j = ad.length;
     //   System.out.println("J:"+j);
       
        int k = ad1.length;
        for(int l = 0; l < Rows; l++)
        {
            for(int i1 = 0; i1 < Cols / 2; i1++)
            {
            //	System.out.println("Pixels:["+l+"]["+i1+"]:"+Pixels[l][i1]); 
                double d = 0.0D;
                for(int l2 = 0; l2 < ad.length; l2++){
                //	System.out.println("ad[12]:"+ad[l2]);
                    d += (double)Pixels[l][((i1 * 2 - j / 2) + l2 + Cols) % Cols] * ad[l2];
                    //System.out.println("d:"+d);
                }
                

                af1[l][i1] = (float)d;
            }

            for(int k1 = Cols / 2; k1 < Cols; k1++)
            {
                double d1 = 0.0D;
                for(int i3 = 0; i3 < ad1.length; i3++)
                    d1 += (double)Pixels[l][(((k1 - Cols / 2) * 2 - k / 2) + i3 + Cols) % Cols] * ad1[i3];

                af1[l][k1] = (float)d1;
            }

        }

        for(int j1 = 0; j1 < Cols; j1++)
        {
            for(int l1 = 0; l1 < Rows / 2; l1++)
            {
                double d2 = 0.0D;
                for(int j3 = 0; j3 < ad.length; j3++)
                    d2 += (double)af1[((l1 * 2 - j / 2) + j3 + Rows) % Rows][j1] * ad[j3];

                af[l1][j1] = (float)d2;
            }

            for(int j2 = Rows / 2; j2 < Rows; j2++)
            {
                double d3 = 0.0D;
                for(int k3 = 0; k3 < ad1.length; k3++)
                    d3 += (double)af1[(((j2 - Rows / 2) * 2 - k / 2) + k3 + Rows) % Rows][j1] * ad1[k3];

                af[j2][j1] = (float)d3;
            }

        }

        for(int i2 = 0; i2 < Rows / 2; i2++)
        {
            for(int k2 = 0; k2 < Cols / 2; k2++)
                af2[i2][k2] = af[i2][k2];

        }

        String s = "," + histogram2D(af2, Rows / 2, Cols / 2);
        
        Ratio2[0] = histogram2D(af2, Rows / 2, Cols / 2);
      
        return s;
    }

    public String simulate(double d, double d1, double d2, double d3, double d4, double d5, double cs, int sf,int itrNo)
    {
    	// d =ph, d1 =pot, d2 = temp , d3 = concen
    	// not sure if we need max and min values ( confirm with professor)
    	double tmax=320, phmax=5.0, potmax=0.7, concmax=0.4;
    	double tmin=250, phmin=3, potmin=0.4, concmin=0.1;
    	
    	iterationNo = itrNo;
    //	System.out.print("In simulate canvas");
        float af[][] = new float[Rows + 2][Cols + 2];
    	double dh = 0, dp = 0, dc = 0, dt = 0;
        double d6 = 0.0D;
        double d7 = 0.0D;
        double d8 = 0.0D;
        for(int i = 0; i < Rows; i++)
        {
            for(int j = 0; j < Cols; j++)
            {
                af[i + 1][j + 1] = Pixels[i][j];
            //    System.out.print("af[i + 1][j + 1]::"+af[i + 1][j + 1]);
                af[0][j + 1] = 0.0F;
                af[Rows + 1][j + 1] = 0.0F;
            }

            af[i + 1][0] = 0.0F;
            af[i + 1][Cols + 1] = 0.0F;
        }

        af[0][0] = 0.0F;
        af[0][Cols + 1] = 0.0F;
        af[Rows + 1][0] = 0.0F;
        af[Rows + 1][Cols + 1] = 0.0F;
        for(int k = 0; k < Rows; k++)
        {
            for(int l = 0; l < Cols; l++)
            {
                if(Math.random() < 0.75D)
                    prob[k][l] = prob[k][l] + (float)(Math.random() - 0.5D) / 5F;
                if(prob[k][l] < 0.0F)
                    prob[k][l] = prob[k][l] + 1.0F;
                if(prob[k][l] > 1.0F)
                    prob[k][l] = prob[k][l] - 1.0F;
                // Code added from Anuj's program to match the 2D Simulation program
             
                if (af[k+1][l+1]>40)
				{
                	dh = (Math.random())/200.0*4.0;
                	dp = (Math.random())/2000.0*4.0;
                	dc = (Math.random())/800.0*4.0;
                	dt = (Math.random())/25.0*4.0;
				}
                if (af[k+1][l+1]<=40)
				{
					dh = (Math.random())/200.0*(af[k+1][l+1]/10.0);
					dp = (Math.random())/2000.0*(af[k+1][l+1]/10.0);
					dc = (Math.random())/800.0*(af[k+1][l+1]/10.0);
					dt = (Math.random())/25.0*(af[k+1][l+1]/10.0);
				}
			
                if (d==0) dh=0;
    			if (d1==0) dp=0;
    			if (d2==0) dt=0;
    			if (d3==0) dc=0;
    			
    			param1[k][l]+=dh;
    			param2[k][l]+=dt;	
    			param3[k][l]+=dp;
    			param4[k][l]+=dc;
    			
    			temp1=Pixels[k][l];
    			if (param2[k][l]>tmax) param2[k][l]=tmax;
    			if (param1[k][l]>phmax) param1[k][l]=phmax;
    			if (param4[k][l]>concmax) param4[k][l]=concmax;
    			if (param3[k][l]>potmax) param3[k][l]=potmax;

    			if (param2[k][l]<tmin) param2[k][l]=tmin;
    			if (param1[k][l]<phmin) param1[k][l]=phmin;
    			if (param4[k][l]<concmin) param4[k][l]=concmin;
    			if (param3[k][l]<potmin) param3[k][l]=potmin;
// Change on 1/17
                d6 = (Math.random() - 0.5D) / 2D;
                d7 = (Math.random() - 0.5D) / 10D;
                d8 = (Math.random() - 0.5D) / 25D;
                //Pixels[k][l] = (float)applyCA(af[k][l], af[k][l + 1], af[k][l + 2], af[k + 1][l], af[k + 1][l + 1], af[k + 1][l + 2], af[k + 2][l], af[k + 2][l + 1], af[k + 2][l + 2], d + d6, d1 + d7, d2, d3 + d8, d4, d5, prob[k][l],cs,sf);
                Pixels[k][l] = (float)applyCA(af[k][l], af[k][l + 1], af[k][l + 2], af[k + 1][l], af[k + 1][l + 1], af[k + 1][l + 2], af[k + 2][l], af[k + 2][l + 1], af[k + 2][l + 2], param1[k][l], param3[k][l], param2[k][l], param4[k][l], d4, d5, prob[k][l],cs,sf);
                if (temp1==0 && Pixels[k][l]>0)
				{
				ini++;
				}

                if (temp1>0 && Pixels[k][l]==0)
				{
				dea++;
				}				

                if (temp1<=20 && Pixels[k][l]>20)
				{
				gro++;
				if (temp1<255 && Pixels[k][l]==255)
					{
					sat++;
					}
				}

             //   System.out.print("\nPixels["+k+"]["+l+"]:"+Pixels[k][l]);
            }

        }

       /* for(int i1 = Rows; i1 > 0; i1--)
        {
            for(int j1 = Cols; j1 > 0; j1--)
            {
                if((double)init[i1][j1] != 0.0D)
                    init[i1][j1] += (Math.random() - 0.42999999999999999D) / 10D;
                if(init[i1][j1] + (init[i1 - 1][j1] + init[i1 + 1][j1] + init[i1][j1 - 1] + init[i1][j1 + 1]) / 6F > 3F && (double)Pixels[i1 - 1][j1 - 1] < 0.01D){
                	
                    Pixels[i1 - 1][j1 - 1] = 4F;
                  //  System.out.print("Pixels["+(i1- 1)+"]["+(j1 - 1)+"]:"+Pixels[i1 - 1][j1 - 1]);
                }
            }
        }*/
        return Double.toString(((double)((int)(((ini))*1000)))/1000.0) + ",\t " + Double.toString((double)((int)(dea*1000)/1000.0)) + ",\t" + Double.toString((double)((int)(gro*1000)/1000.0))+ ",\t" + Double.toString((double)((int)(sat*1000)/1000.0))+ ", ";
        
        //return String.format("%5.3f, %5.3f, %5.3f",((double)(int)((d + d6) * 1000D) / 1000D), ((double)(int)((d1 + d7) * 1000D) / 1000D) ,  ((double)(int)((d3 + d8) * 1000D) / 1000D));
    }

   /* private double applyCA(float f1, float f2, float f3, float f4, float f5, float f6, float f7, 
            float f8, float f9, double d, double d1, double d2, double d3, double d4, double d5, 
            float f10,double cs, int sf)
    {
        if((double)(f1 + f2 + f3 + f4 + f5 + f6 + f7 + f8 + f9) < 0.10000000000000001D)
            return 0.0D;
        double d6 = f5;
        double d7;
        //double d7 = ((d - 7D) * (d - 7D) * (d <= 4D || d > 8.5D ? 1.0D : 0.0D) * Math.exp(d1) * d3 * d4 * d5) / d2;
        if(cs!=0.0){
         d7 = (((d - 7D) * (d - 7D) * (d <= 4D || d > 8.5D ? 1.0D : 0.0D) * Math.exp(d1) * d3 * d4 * d5) / d2) *Math.pow(cs, sf);
        }else
        {
         d7 = ((d - 7D) * (d - 7D) * (d <= 4D || d > 8.5D ? 1.0D : 0.0D) * Math.exp(d1) * d3 * d4 * d5) / d2;
        }
        double d8 = 2D;
        double d9 = 3D;
        if(f5 > 80F)
        {
            d6 += (eff(f5) * d7) / 1.5D;
        } else
        {
            d6 += (eff(f5) * d7) / 2.5D;
            d8 = 2.7999999999999998D;
            d9 = 4D;
        }
        double d10 = Math.random();
        double d11 = Math.random();
        if(d10 < 0.25D)
        {
            d6 += (eff(f2) * d7) / d8;
            if(d11 > 0.5D)
                d6 += (eff(f1) * d7) / d9;
            else
                d6 += (eff(f3) * d7) / d9;
        } else
        if(d10 < 0.5D)
        {
            d6 += (eff(f4) * d7) / d8;
            if(d11 > 0.5D)
                d6 += (eff(f1) * d7) / d9;
            else
                d6 += (eff(f7) * d7) / d9;
        } else
        if(d10 < 0.75D)
        {
            d6 += (eff(f6) * d7) / d8;
            if(d11 > 0.5D)
                d6 += (eff(f3) * d7) / d9;
            else
                d6 += (eff(f9) * d7) / d9;
        } else
        if(d10 < 1.0D)
        {
            d6 += (eff(f8) * d7) / d8;
            if(d11 > 0.5D)
                d6 += (eff(f7) * d7) / d9;
            else
                d6 += (eff(f9) * d7) / d9;
        }
        if(d6 <= 200D)
            d6 += Math.random() <= 0.94999999999999996D ? 0.0D : 2D;
        if(d6 > 255D)
            d6 = 255D;
        return d6;
    }*/
    private double applyCA(float nw, float n, float ne, float w, float c, float e, float sw, float s, float se, double ph, double pot, double temp, double concen, double diffus, double charge, float prob,double cs, int sf)

	{

	double z=c;

	double k;
	
	if(cs!=0.0){
        //d7 = (((d - 7D) * (d - 7D) * (d <= 4D || d > 8.5D ? 1.0D : 0.0D) * Math.exp(d1) * d3 * d4 * d5) / d2) *Math.pow(cs, sf);
        k=((ph-7)*(ph-7)*((ph>4)&&(ph<=8.5)?0:1)*Math.exp(pot)*concen*diffus*charge/temp)*Math.pow(cs, sf);
       }else
       {
    	  k=(ph-7)*(ph-7)*((ph>4)&&(ph<=8.5)?0:1)*Math.exp(pot)*concen*diffus*charge/temp;
      
     }
	

	double d1=2;

	double d2=3;



	int i, j;
	double d;
	double re=0;

	double birthprob=(6.7572*60*Math.pow(10,-2))* concen * (Math.pow(2.7182,(0.5*96454/6.023/Math.pow(10,23)/temp*(pot-0.086))))/40;
	double deathprob=(2.2137 * 60 * Math.pow(10,13))* Math.pow (10, (-14+ph)) * (Math.pow(2.7182,(-0.5*96454/temp/8.314*(pot-0.001))))*2;
	double tranrate=(5 * Math.pow(10,2)*60)*Math.pow (2.7182,(-30000/8.314/temp)) * 10;
	double growthrate=Math.pow(2.7182, (13.409-(5558.7/temp)-0.087*ph + 0.56965 * concen)) * 400;


	int in=0, in1=0;
	int dn=0, dn1=0;

	if (e>=1) in++;
	if (w>=1) in++;
	if (s>=1) in++;
	if (n>=1) in++;

	if (nw>=1) dn++;
	if (ne>=1) dn++;
	if (sw>=1) dn++;
	if (se>=1) dn++;


	if (e>=20) in1++;
	if (w>=20) in1++;
	if (s>=20) in1++;
	if (n>=20) in1++;

	if (nw>=20) dn1++;
	if (ne>=20) dn1++;
	if (sw>=20) dn1++;
	if (se>=20) dn1++;


	d = Math.random();
	if (c<1)
		{
		if (d<=birthprob || (in+dn)>=4)
			{
		re=1;
		return re;
			}
		if (d>birthprob)
			{
			re=0;
			}
		return re;
		}

	if (c>=1 && c<=6)
		{
		if (d<deathprob && (in+dn)<=4)
			{
			re=0;
			return re;
			}
		}

	if (c>=1 && c<=20)
		{
		re=c + tranrate;
		return re;
		}

	if (c>20)
		{
		re=c+(int) (growthrate * (1+0.1*in1+0.05+dn1));
		}

	if (re>255)
		{
		re=255;
		}
	return re;
	}

    private double eff(float f1)
    {
        if(f1 > 255F)
            return 5D;
        if(f1 <= 0.0F)
            return 0.0D;
        else
            return (double)((16384F - (f1 - 128F) * (f1 - 128F)) / 120F);
    }

    public String[] getPitGrowthArray(){
    	return PitGrowthArray;
    }
    public String[] getPitSize(){
    	return PitSize;
    }
    public float getMaxRedHt(){
    	return maxRHt;
    	
    }
    public float getmaxGreenHt(){
    	return maxGHt;
    }
    public float getmaxBlueHt(){
    	return maxBHt;
    }
    public int getWidthCnt(){
    	return widthCntTot;
    }
    public int getHeightCnt(){
    	return heightCntTot;
    }
    @SuppressWarnings("rawtypes")
	public ArrayList getRedPixels(){
    	/*System.out.println("Getting pixels" +redpixels.size());
    	String[] RedPixels = null;
    	if(redpixels.size()>0){
			RedPixels = new String[redpixels.size()];
	    	for(int i=0;i<=redpixels.size();i++){
	    		RedPixels[i]= ((String) redpixels.get(i));
	    		System.out.println("Redpixels["+i+"]:"+RedPixels[i]);
			}
    	}*/
    	Collections.sort(redpixels);
		return redpixels;    	
    }
}