// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 9/28/2006 7:53:50 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Sim2D.java

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

class ImageMatrix extends Canvas
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
	ArrayList pixelslist = new ArrayList();
	ArrayList pitCrack = new ArrayList();

	public ImageMatrix(int i, int j, int k,int iter,String path)
	{
		setSize(j * k, i * k);
		Rows = i;
		Cols = j;
		f = k;
		Pixels = new float[Rows][Cols];
		init = new float[Rows + 2][Cols + 2];
		prob = new float[Rows][Cols];
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

				//    System.out.println(" prob[l][i1]:"+ prob[l][i1]);
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

	public ImageMatrix()
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
		float f1 = 0.0F;
		float f2 = 0.0F;
		float f3 = 0.0F;
		float f4 = 0.0F;
		float f5 = 0.0F;
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
			f1 += af1[k1] * (float)k1;
			f1 -= af2[k1] * (float)k1;
			f4 += af1[k1] * af1[k1];
			f4 += af2[k1] * af2[k1];
			if((double)af1[k1] > 9.9999999999999994E-012D)
				f5 = (float)((double)f5 + ((double)(-af1[k1]) * Math.log(af1[k1])) / Math.log(2D));
			if((double)af2[k1] > 9.9999999999999994E-012D)
				f5 = (float)((double)f5 + ((double)(-af2[k1]) * Math.log(af2[k1])) / Math.log(2D));
		}

		for(int i2 = 0; i2 < 1000; i2++)
		{
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

		double ad[] = new double[i];
		double ad1[] = new double[i];
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
		// System.out.println("J:"+j);

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
					//   System.out.print("\nPixels["+l+"]["+i1+"]:"+Pixels[1][i1]);
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
		iterationNo = itrNo;
		//	System.out.print("In simulate canvas");
		float af[][] = new float[Rows + 2][Cols + 2];
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
				d6 = (Math.random() - 0.5D) / 2D;
				d7 = (Math.random() - 0.5D) / 10D;
				d8 = (Math.random() - 0.5D) / 25D;
				Pixels[k][l] = (float)applyCA(af[k][l], af[k][l + 1], af[k][l + 2], af[k + 1][l], af[k + 1][l + 1], af[k + 1][l + 2], af[k + 2][l], af[k + 2][l + 1], af[k + 2][l + 2], d + d6, d1 + d7, d2, d3 + d8, d4, d5, prob[k][l],cs,sf);
				
			}

		}

		for(int i1 = Rows; i1 > 0; i1--)
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
		}

		return String.format("%5.3f, %5.3f, %5.3f",((double)(int)((d + d6) * 1000D) / 1000D), ((double)(int)((d1 + d7) * 1000D) / 1000D) ,  ((double)(int)((d3 + d8) * 1000D) / 1000D));
	}

	private double applyCA(float f1, float f2, float f3, float f4, float f5, float f6, float f7, 
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

	public void paint(Graphics2D g)
	{

		//	System.out.println("In paint");
		Font bigFont;
		bigFont = new Font("Arial",Font.BOLD,16);
		float RedVal,GreenVal,BlueVal;
		int heightCnt=0,widthCnt=0;
		int maxRedWidth=0,maxGreenWidth=0,maxBlueWidth=0;    	
		int maxRedHeight=0,maxGreenHeight=0,maxBlueHeight=0;
		float RedWidth = 0, BlueWidth = 0, GreenWidth =0;
		int CenterRow = (int) Math.ceil(Rows/2);
		int CenterCol = (int) Math.ceil(Cols/2);

		String delims = ",";
		// To get the fourth element from the histogram function i.e Entropy/Energy tokens1[3]
		String[] tokens1 = Ratio1[0].split(delims);
		// System.out.println("tokens1[3]:"+tokens1[3]);
		String[] tokens2 = Ratio2[0].split(delims);
		// System.out.println("tokens2[3]:"+tokens2[3]);
		double r1 = Double.parseDouble(tokens1[3]);
		double r2 = Double.parseDouble(tokens2[3]);
		//System.out.print("\nCenterRow:"+CenterRow+"\tCenterCol:"+CenterCol);
		try{
			for(int i = 0; i < Rows; i++)
			{
				for(int j = 0; j < Cols; j++)
				{
					g.setColor(new Color(jetR(Pixels[i][j]), jetG(Pixels[i][j]), jetB(Pixels[i][j])));
					// to get width
					if(jetR(Pixels[i][j])==1.0 && (Math.floor(r1)==4.0 || Math.floor(r2)==4.0 )){
						//	System.out.println("\nPixels["+i+"]["+j+"]");
						pixelslist.add(i+","+j);

					}
					if(i==CenterRow && j>=CenterCol){

						RedVal = jetR(Pixels[CenterRow][j]);
						GreenVal = jetG(Pixels[CenterRow][j]);
						BlueVal = jetB(Pixels[CenterRow][j]);
						if(RedVal>0.55 ||GreenVal >0.55 ||BlueVal>0.55){
							widthCnt++;
							if ( RedVal > GreenVal && RedVal > BlueVal)
								maxRedWidth++;
							else if ( GreenVal > RedVal && GreenVal > BlueVal )
								maxGreenWidth++;
							else if ( BlueVal > RedVal && BlueVal > GreenVal )
								maxBlueWidth++;
						}

						//   System.out.print("\nWidth::Pixels["+i+"]["+j+"]"+":\tR :"+RedVal +"\tG:"+GreenVal+"\tB:"+BlueVal);
						//	System.out.print("\nPixels["+i+"]["+j+"]"+":\tR :"+RedVal +"\tG:"+GreenVal+"\tB:"+BlueVal);
					}

					// to get height 
					if(i>=CenterRow && j==CenterCol){

						RedVal = jetR(Pixels[i][CenterCol]);
						GreenVal = jetG(Pixels[i][CenterCol]);
						BlueVal = jetB(Pixels[i][CenterCol]);
						if(RedVal>0.55 ||GreenVal >0.55 ||BlueVal>0.55){
							heightCnt++;
							if ( RedVal > GreenVal && RedVal > BlueVal){
								maxRedHeight++;
							}
							else if ( GreenVal > RedVal && GreenVal > BlueVal ){
								maxGreenHeight++;
							}
							else if ( BlueVal > RedVal && BlueVal > GreenVal ){
								maxBlueHeight++;
							}
						}
					}
					g.fillRect(j * f, i * f, f, f);
				}

			}
			String delimiter = ",";
			String p1;
			String p2;
			for(int i =0;i<pixelslist.size();i++){
				String[] listtoken = ((String) pixelslist.get(i)).split(delimiter);

				p1 = listtoken[0];
				p2 = listtoken[1];
				if(iterationNo==29){
					pitCrack.add(Integer.valueOf(p1)+","+Integer.valueOf(p2));
				}
			}
			if(iterationNo==29){
				String v1=null,v2=null,v3=null,v4=null;
				if(pitCrack.size()>0){
					for (int p=0;p<=pitCrack.size();p++){
						if(p==0){
							String[] listtoken = ((String) pitCrack.get(0)).split(delimiter);
							v1 = listtoken[0];
							v2 = listtoken[1];
						}
						if(p==pitCrack.size()){
							String[] listtoken = ((String) pitCrack.get(p-1)).split(delimiter);
							v3 = listtoken[0];
							v4 = listtoken[1];
						}
					}
					if(!(v1.equals(null) && v2.equals(null) && v3.equals(null) && v4.equals(null))){

						Line2D line = new Line2D.Double(4*Integer.valueOf(v1),4*Integer.valueOf(v2),4*Integer.valueOf(v3),4*Integer.valueOf(v4));
						g.setColor(Color.white);
						g.setStroke(new BasicStroke(3));
						g.draw(line);
						g.setFont(bigFont); 
						g.drawString("Pit Crack",4*Integer.valueOf(v1)-5,4*Integer.valueOf(v2)-5);

						/*g.setFont(bigFont); 
        	g.setColor(Color.black);

        	g.drawString("Pit Crack",4*Integer.valueOf(v1),4*Integer.valueOf(v2)-2);
           	g.drawLine(4*Integer.valueOf(v1),4*Integer.valueOf(v2),4*Integer.valueOf(v3),4*Integer.valueOf(v4));*/
					}
				}
			}
			RedWidth = (float) Math.sqrt((maxRedHeight*maxRedHeight)+(maxRedWidth*maxRedWidth));
			GreenWidth = (float) Math.sqrt((maxGreenHeight*maxGreenHeight)+(maxGreenWidth*maxGreenWidth));
			BlueWidth = (float) Math.sqrt((maxBlueHeight*maxBlueHeight)+(maxBlueWidth*maxBlueWidth));
			widthCntTot=widthCnt*2;
			heightCntTot= heightCnt*2;
			maxRHt = RedWidth;
			maxGHt = GreenWidth+maxRHt;
			maxBHt = BlueWidth;
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();

		}
	}

	public void update(Graphics g)
	{
		Image image = null;
		image = createImage(Cols * f, Rows * f);
		Graphics2D g1 = (Graphics2D) image.getGraphics();
		paint(g1);
		g.drawImage(image, 0, 0, this);
		try {
			ImageIO.write((RenderedImage) image, "JPEG", new File(syspath+"files/simulation.JPG"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private float jetR(double d)
	{
		int i = (int)d;

		if(i < 96)
			return 0.0F;
		if(i < 160){
			//	System.out.println("i less than 160 Red :"+i+"return :"+((float)i - 95F) / 64F);
			return ((float)i - 95F) / 64F;
		}
		if(i < 224){
			//	System.out.println("i less than 224 Red :"+i+"return:"+"1.0F");
			return 1.0F;            
		}
		else{
			//	System.out.println("i >224 Red :"+i+"return:"+(288F - (float)i) / 64F);
			return (288F - (float)i) / 64F;
		}

	}

	private float jetG(double d)
	{
		int i = (int)d;
		// System.out.println("i jet Green:"+i);
		if(i < 32){
			//System.out.println("\ni jet i<32 Green- 0.0F:"+i);
			return 0.0F;

		}
		if(i < 96){
			//System.out.println("\ni jet i<96 Green- :"+((float)i - 31F) / 64F +"\ti:"+i);
			return ((float)i - 31F) / 64F;
		}
		if(i < 160){
			//System.out.println("\ni jet i<160 Green-1.0F :"+i);
			return 1.0F;
		}
		if(i < 224){
			//System.out.println("\ni jet i<224 Green- :"+(224F - (float)i) / 64F+"\ti:"+i);
			return (224F - (float)i) / 64F;
		}
		else
			return 0.0F;
	}

	private float jetB(double d)
	{
		int i = (int)d;
		// System.out.println("i jet Blue:"+i);
		if(i < 32){
			//System.out.println("\ni jet i<32 Blue- :"+((float)i + 33F) / 64F +"\ti:"+i);
			return ((float)i + 33F) / 64F;
		}
		if(i < 96)
			return 1.0F;
		if(i < 160){
			//System.out.println("\ni jet i<160 Blue- :"+(160F - (float)i) / 64F +"\ti:"+i);
			return (160F - (float)i) / 64F;
		}
		else
			return 0.0F;
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

	private float Pixels[][];
	private float init[][];
	private float prob[][];
	private int Rows;
	private int Cols;
	private int f;
	private Graphics buf;
}