package com.nsfisdas;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Confirm extends Frame implements ActionListener
{
 private final float FRACTION = 0.75f; // 90% of the pixels in the segment would be used to 
									  // to form the coefficeients
 private final int NUM_SAMPLES = 4;  // the number of samples that are to be formed using the pixels

 private final String FileName = "Equalized-Coeffs-W1-Test-S";

 private float Mat[][];
 private RGBList rgbList;
 private int rows, cols;
 private int coords[]; // i * (cols) + j
 private int numFeatures,count;
 private int vectorLength; // numFeatures + 2 (one for corrosion 1/0 and one for amount 0-1)
 int num_rows_written_so_far; 
 private boolean answer, canPaint;
 private RandomAccessFile output;
 private Button more;
 private Button exit;
 private Label blankline;
 private Label AskCorroded;
 private TextField AcceptCorroded;
 private Label AskAmount;
 private TextField AcceptAmount;
 private GridBagLayout gbLayout;
 private GridBagConstraints gbConstraints;
 private Isolate isoHandle;

 public Confirm(Isolate isolateHandle, RGBList RGBlist, float waveMat[][])
 {
  super(" Confirm ");
  Mat = waveMat;
  numFeatures = Mat[0].length;
  rgbList = RGBlist;
  isoHandle = isolateHandle;
  Point p;
  p = new Point(300,300);
  setLocation(p);
  setSize(500,300);
  gbLayout = new GridBagLayout();
  setLayout( gbLayout );
  gbConstraints = new GridBagConstraints();
  gbConstraints.fill = GridBagConstraints.BOTH;
  blankline = new Label("");
  addComponent( blankline, 1,1, 1,4);
  AskCorroded = new Label("Is the highlighted segment a corroded region? (y/n) ");
  addComponent(AskCorroded,2,1,5,1);
  AcceptCorroded = new TextField(1);
  addComponent(AcceptCorroded,2,6,1,1);
  AskAmount = new Label("Enter material loss 0-1 ");
  addComponent(AskAmount, 4,1,4,1);
  AcceptAmount = new TextField(3);
  AcceptAmount.setText("0.0");
  addComponent(AcceptAmount,4,6,1,1);

  answer = false;
  addComponent( blankline, 6, 1, 7, 3);
  more = new Button(" More Corroded Segments" );
  more.addActionListener( this );
  addComponent(more, 9, 4, 1, 1);
  exit = new Button(" Exit " );
  exit.addActionListener( this );
  addComponent(exit, 9, 5, 1, 1);
  canPaint = false;
  // open file to write the feature vector
   openFeatureFile(); //  randomaccessfile with "rw"
 }

 public void setRowsCols(int r, int c)
 {
  rows = r;
  cols = c;
 }

 public void formCoords()
 {
  RGBNode firstNode;
  RGBNode lastNode;
  RGBNode ptr;
  int ctr,i,j;
  ctr =0;
  count = rgbList.getNumMembers();
  firstNode = rgbList.getFirstNode();
  lastNode = rgbList.getLastNode();
  coords = new int[count];
  
  for ( ptr = firstNode; ptr != null; ptr = ptr.getNextNode() )
  {
   i = ptr.getx(); //row
   j = ptr.gety(); //col           
   coords[ctr] = i * cols + j; 
   ctr++;
  }
  canPaint = true;
  repaint();
 }

 public void paint( Graphics g)
 { 
  /* if( canPaint)
	{
	 RGB rgb;
	 int red,green,blue,i,j;
	 RGBNode firstNode;
     RGBNode lastNode;
     RGBNode ptr;
	 count = rgbList.getNumMembers();
     firstNode = rgbList.getFirstNode();
     lastNode = rgbList.getLastNode();
     for ( ptr = firstNode; ptr != null; ptr = ptr.getNextNode() )
     {
       i = ptr.getx(); //row
       j = ptr.gety(); //col           
       rgb = ptr.getRGB();
	   red = rgb.getRed();
	   green = rgb.getGreen();
	   blue = rgb.getBlue();
	   g.setColor( new Color(red, green, blue)); //(int) Mat[i*cols+j][0], (int)Mat[i*cols+j][1],(int)Mat[i*cols+j][2]));//
  	   g.drawLine( 30+j , 50+i, 30+j , 50+i);
	 }
   }
  */
 }

 private void addComponent( Component c, int row, int col, int width, int height)
 {
   gbConstraints.gridx = col;
   gbConstraints.gridy = row;
 
   gbConstraints.gridwidth = width;
   gbConstraints.gridheight = height;
  
   gbLayout.setConstraints( c, gbConstraints );
   add(c);
 }

 public void actionPerformed( ActionEvent e)
 {
  String ans;
  float amount, corroded;
   if (e.getSource() == exit) 
   {
     closeFeatureFile();
     setVisible(false);
     //System.exit(0);
   }
   ans = AcceptCorroded.getText().trim();
   amount = Float.parseFloat(AcceptAmount.getText());

//   for( int i=0; i< count; i++)
  //    System.out.print("   "+Mat[coords[i]][0]);

   if (ans.equalsIgnoreCase("N"))
   {
     System.out.println(" The segement is not corroded "+amount);
     corroded = 0.0f;
   }
   else
   {
     System.out.println(" The segement is corroded "+amount);
     corroded = 1.0f;
   }
   answer = true;

   formGroups(corroded, amount);
   setVisible( false);
  
 }

 private void formGroups(float corroded, float amount)
 {
   float aveWave[];
  aveWave = new float[numFeatures];
   for(int i=0; i < NUM_SAMPLES; i++)
    {
     averageGroup(aveWave, FRACTION);
	 writeVector(aveWave, corroded, amount);
	 System.out.println(" Features length "+aveWave.length);
    }
    closeFeatureFile();
 }

 private void averageGroup( float aveWave[], float percent)
 {
  int ctr =0;
 
  for(int i=0; i < (int) count * percent; i++)
  {
   for(int j=0; j < numFeatures; j++)
   {
    aveWave[j] += Mat[coords[(int)(Math.random() * count)]][j];
   }
   ctr++;
  }
  for(int j=0; j < numFeatures; j++)
    aveWave[j] /=ctr;
 }

 private void openFeatureFile()
 {
  try
  {
   output = new RandomAccessFile(FileName,"rw"); 
  }
  catch( IOException ex)
  {
   System.err.println(ex.toString() );
  }
 }

 private void closeFeatureFile()
 {
  try
  {
   output.close();
  }
  catch( IOException ex)
  {
   System.err.println(ex.toString() );
  }
 }


 private void writeVector(float vector[], float corroded, float amount)
 {
   int numRows;
   try
   {
    if( output.length() == 0)
	{
	 numRows = 0;
	 vectorLength = numFeatures + 2;
	}
	else
	{
	 output.seek(0);
	 numRows = output.readInt();
	 vectorLength = output.readInt();
	}
	System.out.println(" NumRows so far "+numRows);
	numRows++;
	output.seek(0);
	output.writeInt(numRows);
	output.writeInt(vectorLength);

   	output.seek( output.length());     // go to the end of the file
	                                  // and write the feature vector
	

    for(int j=0; j < numFeatures; j++)
	 output.writeFloat(vector[j]);
   
    output.writeFloat(corroded);
    output.writeFloat(amount);
  }
  catch(IOException ex)
  {
   System.out.println(" Excpetion "+ex.toString() );
  }
 }



}