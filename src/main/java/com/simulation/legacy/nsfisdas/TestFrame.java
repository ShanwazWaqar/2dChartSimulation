package com.simulation.legacy.nsfisdas;

// import javax.swing.*;

// import java.applet.Applet;
// import java.awt.*;
// import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class TestFrame // extends Frame
{
	private String fileName;
	private BufferedReader input; 
	private PpmReader ppmhandler;
	private int height, width;
	private ImageMatrix OriginalImage;
	private ImageMatrix SegmentedImage;
	private BufferedImage image;
	private MarkSegments markSegments;
	private String catString;
	private float waveMat[][];
	// private Image img;
	public URL path;
	public String str_path;
	// MediaTracker mt;
	// private Thread imgTest;
	// public TextArea feat;
	// public TextArea para;
	// Panel p1;
	// Panel p2;
	// public DisplayImage imgdisp;
	private String outputDirectory = "output/test/";

	public TestFrame(String imagePath) 
	{
		this.fileName = imagePath;
		this.catString = "EC-04k"; // Default category
		initialize();
	}

	public TestFrame(String imagePath, String category) 
	{
		this.fileName = imagePath;
		this.catString = category;
		initialize();
	}

	public TestFrame(String imagePath, String category, String outputDir) 
	{
		this.fileName = imagePath;
		this.catString = category;
		this.outputDirectory = outputDir;
		initialize();
	}

	private void initialize() {
		OriginalImage = new ImageMatrix();
		
		// Create output directory
		createOutputDirectory();
		
		// Load and process the image
		if (loadImageFromFile()) {
			processImage();
		} else {
			System.err.println("Failed to load image: " + fileName);
		}
	}

	private void createOutputDirectory() {
		File dir = new File(outputDirectory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	private boolean loadImageFromFile() {
		try {
			File imageFile = new File(fileName);
			if (!imageFile.exists()) {
				System.err.println("Image file not found: " + fileName);
				return false;
			}
			
			image = ImageIO.read(imageFile);
			if (image == null) {
				System.err.println("Failed to read image: " + fileName);
				return false;
			}
			
			width = image.getWidth();
			height = image.getHeight();
			
			// Convert BufferedImage to ImageMatrix
			convertImageToMatrix();
			return true;
		} catch (IOException e) {
			System.err.println("Error loading image: " + e.getMessage());
			return false;
		}
	}

	private void convertImageToMatrix() {
		int TempR[][], TempG[][], TempB[][];
		TempR = new int[height][width];
		TempG = new int[height][width];
		TempB = new int[height][width];
		
		for(int i=0; i < height; i++) {
			for(int j=0; j < width; j++) {
				int rgb = image.getRGB(j, i);
				TempR[i][j] = (rgb >> 16) & 0xFF; // Red
				TempG[i][j] = (rgb >> 8) & 0xFF;  // Green
				TempB[i][j] = rgb & 0xFF;         // Blue
			}
		}
		
		OriginalImage.setRedPointer(TempR);
		OriginalImage.setGreenPointer(TempG);
		OriginalImage.setBluePointer(TempB);
	}

	private void processImage() {
		ImageMatrix stretchedImage, equalizedImage;
		DisplayImage display;
		HistoEqualizer histoEqualize;

		// Save original image
		display = new DisplayImage(outputDirectory + "Original_" + fileName, OriginalImage);

		// Histogram equalization
		histoEqualize = new HistoEqualizer(OriginalImage, catString);  
		equalizedImage = histoEqualize.testHistoMap();
		display = new DisplayImage(outputDirectory + "Equalized_" + fileName, equalizedImage);

		// Wavelet transform and segmentation
		WTransform1 wTransform = new WTransform1(equalizedImage);
		wTransform.Segment();
		SegmentedImage = wTransform.getSegmentedImage();
		display = new DisplayImage(outputDirectory + "Segmented_" + fileName, SegmentedImage);
		waveMat = wTransform.getMatrix();

		// Mark segments
		try {
			markSegments = new MarkSegments(fileName, SegmentedImage, OriginalImage, waveMat, catString);
			System.out.println("Segment marking completed");
		} catch (Exception e) {
			System.err.println("Error in segment marking: " + e.getMessage());
		}
		
		System.out.println("Image processing completed for: " + fileName);
		System.out.println("Output saved to: " + outputDirectory);
	}

	public ImageMatrix getOriginalImage() {
		return OriginalImage;
	}

	public ImageMatrix getSegmentedImage() {
		return SegmentedImage;
	}

	public float[][] getWaveMatrix() {
		return waveMat;
	}

	public MarkSegments getMarkSegments() {
		return markSegments;
	}

	public void setOutputDirectory(String outputDir) {
		this.outputDirectory = outputDir;
		createOutputDirectory();
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	// Static method for easy usage
	public static TestFrame processImageFile(String imagePath) {
		return new TestFrame(imagePath);
	}

	public static TestFrame processImageFile(String imagePath, String category) {
		return new TestFrame(imagePath, category);
	}

	public static TestFrame processImageFile(String imagePath, String category, String outputDir) {
		return new TestFrame(imagePath, category, outputDir);
	}
}


