package com.simulation.legacy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

public class ImageAnalysis {
	public ImageAnalysisMatrix mtrx;
	public static String IMG = null;

	public String[] mtrx_stat2D;
	public String[] mtrx_wavelet2D;
	public int t_count;
	public String fileName1;
	public String fileName2;
	public FileWriter fos1;
	public FileWriter fos2;
	public String imgPath;

	public ImageAnalysis(String imgPath) {
		this.imgPath = imgPath;
	}

	public void runImageAnalysis() {
		try {
			t_count = 1;
			getPixelsData();
			fileName1 = imgPath + "/Features.txt";
			fos1 = new FileWriter(fileName1);
			fos1.write("Skew,Energy,Entropy,Ratio1,Skew,Energy,Entropy,Ratio2\n");
			fos1.write(mtrx_stat2D[t_count] + mtrx_wavelet2D[t_count] + "\n");
			fos1.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	private void getPixelsData() {
		BufferedImage image;
		try {
			fileName2 = imgPath + "/pixels.txt";
			fos2 = new FileWriter(fileName2);
			IMG = imgPath + "/imgupload.JPG";

			image = ImageIO.read(new File(IMG));
			float[][] pixelData = new float[100][100];

			for (int i = 0; i < 100; i++) {
				for (int j = 0; j < 100; j++) {
					int[] rgb = getPixelData(image, i, j);
					pixelData[i][j] = getMaxValue(rgb);
					fos2.write(pixelData[i][j] + "\n");
				}
			}
			fos2.close();

			mtrx = new ImageAnalysisMatrix(100, 100, 4, null);
			mtrx_stat2D = new String[2];
			mtrx_wavelet2D = new String[2];

			mtrx_stat2D[t_count] = mtrx.stat2D(pixelData);
			mtrx_wavelet2D[t_count] = mtrx.wavelet2D(6, pixelData);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getMaxValue(int[] numbers) {
		int maxValue = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > maxValue) {
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}

	private int[] getPixelData(BufferedImage img, int x, int y) {
		int argb = img.getRGB(x, y);
		return new int[]{
				(argb >> 16) & 0xff, // red
				(argb >> 8) & 0xff,  // green
				(argb) & 0xff        // blue
		};
	}
}
