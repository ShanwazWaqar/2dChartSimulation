package com.simulation.legacy.nsfisdas;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class DisplayImage 
{
 private String name;
 private int rows, cols;
 private int R[][];
 private int G[][];
 private int B[][];
 private BufferedImage image;
 private DisplayPixelInfo displayPixelInfo;
 private String outputDirectory = "output/images/"; // Default output directory

 public DisplayImage(String name,int red[][], int green[][], int blue[][])
 {
  this.name = name;
  rows = red.length;
  cols = red[0].length;
  R = red;
  G = green;
  B = blue;
  displayPixelInfo = new DisplayPixelInfo(name);
  
  // Create output directory if it doesn't exist
  createOutputDirectory();
  
  // Generate and save the image
  getImage();
 }
 
 public DisplayImage(String name, ImageMatrix image)
 { 
   this.name = name;
   rows = image.getRows();
   cols = image.getCols();
   R = image.getRedPointer();
   G = image.getGreenPointer();
   B = image.getBluePointer();
   displayPixelInfo = new DisplayPixelInfo(name);
   
   // Create output directory if it doesn't exist
   createOutputDirectory();
   
   // Generate and save the image
   getImage();
 }

 public DisplayImage(String name, ImageMatrix image, String outputDir)
 { 
   this.name = name;
   this.outputDirectory = outputDir;
   rows = image.getRows();
   cols = image.getCols();
   R = image.getRedPointer();
   G = image.getGreenPointer();
   B = image.getBluePointer();
   displayPixelInfo = new DisplayPixelInfo(name);
   
   // Create output directory if it doesn't exist
   createOutputDirectory();
   
   // Generate and save the image
   getImage();
 }

 private void createOutputDirectory() {
   File dir = new File(outputDirectory);
   if (!dir.exists()) {
     dir.mkdirs();
   }
 }

 public BufferedImage getBufferedImage() {
   return image;
 }

 public byte[] getImageBytes() throws IOException {
   if (image == null) {
     return null;
   }
   ByteArrayOutputStream baos = new ByteArrayOutputStream();
   ImageIO.write(image, "png", baos);
   return baos.toByteArray();
 }

 public String saveImage(String format) {
   if (image == null) {
     return null;
   }
   
   try {
     String fileName = outputDirectory + name + "." + format;
     File outputFile = new File(fileName);
     ImageIO.write(image, format, outputFile);
     System.out.println("Image saved: " + outputFile.getAbsolutePath());
     return outputFile.getAbsolutePath();
   } catch (IOException e) {
     System.err.println("Error saving image: " + e.getMessage());
     return null;
   }
 }

 public String saveImage() {
   return saveImage("png"); // Default to PNG
 }

 private void getImage()
 {
  Color color;
  int rgb;
  image = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
  WritableRaster raster = image.getRaster();
  ColorModel model = image.getColorModel();
  
  for(int i=0; i < cols; i++)
  {
   for(int j=0; j < rows; j++)
   {
     color = new Color(R[j][i], G[j][i], B[j][i]);
	 rgb = color.getRGB();
     Object colorData = model.getDataElements(rgb, null);
	 raster.setDataElements(i,j,colorData);
   }
  }
  
  Graphics2D g2 = image.createGraphics();
  g2.drawImage(image,0,0,null);
  g2.dispose();
  
  // Save as PNG by default
  saveImage("png");
  
  // Also save as GIF using the existing GifEncoder
  saveAsGif();
 }

 private void saveAsGif() {
   try {
     String gifFileName = outputDirectory + name + ".gif";
     File outputFile = new File(gifFileName);
     DataOutputStream output = new DataOutputStream(new FileOutputStream(outputFile));
     GifEncoder gifEncoder = new GifEncoder(image);
     gifEncoder.write(output);
     output.close();
     System.out.println("GIF saved: " + outputFile.getAbsolutePath());
   } catch (IOException ex) { 
     System.err.println("Error saving GIF: " + ex.getMessage());
   }
 }

 public void setOutputDirectory(String outputDir) {
   this.outputDirectory = outputDir;
   createOutputDirectory();
 }

 public String getOutputDirectory() {
   return outputDirectory;
 }
}
