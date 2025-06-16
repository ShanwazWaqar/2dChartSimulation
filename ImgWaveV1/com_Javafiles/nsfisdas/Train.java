package com.nsfisdas;
import java.awt.*;
import java.awt.event.*;

public class Train extends Frame implements ActionListener
{

 private Label prompt;
 private Label catPrompt;
 private TextField fileNameField;
 private TextField category;
 private Button submit;
 private Button exit;

 private GridBagLayout gbLayout;
 private GridBagConstraints gbConstraints;

 public Train()
 {
  super();
  setSize(450, 350);

  addWindowListener( new CloseWindow() );
  
  gbLayout = new GridBagLayout();
  setLayout( gbLayout );
  gbConstraints = new GridBagConstraints();

  gbConstraints.fill = GridBagConstraints.BOTH;
   
  prompt  = new Label(" Enter file name");
  gbConstraints.fill = GridBagConstraints.BOTH;
  addComponent(prompt, 1, 0, 1, 1);

   catPrompt  = new Label(" Enter category name");
  gbConstraints.fill = GridBagConstraints.BOTH;
   addComponent(catPrompt, 2, 0, 1, 1);

  fileNameField = new TextField(20);
  addComponent( fileNameField, 1, 1, 1, 1);
  
  category = new TextField(20);
  addComponent(category, 2,1,1,1);

  submit = new Button(" Submit ");
  gbConstraints.fill = GridBagConstraints.BOTH;
  addComponent( submit, 4, 0, 1, 1);
  submit.addActionListener(this);

  exit  = new Button(" Exit ");
  gbConstraints.fill = GridBagConstraints.BOTH;
  addComponent( exit, 4, 1, 1, 1);
  exit.addActionListener(this);
   
 }  


 private void addComponent( Component c, int row, int col, int width, int height)
 {
  gbConstraints.gridx = col;
  gbConstraints.gridy = row;
  
  gbConstraints.gridwidth = width;
  gbConstraints.gridheight = height;

  gbLayout.setConstraints(c, gbConstraints);
  add(c);
 }

 public void actionPerformed( ActionEvent e)
 {
  TrainFrame trainFrame;

  if (e.getSource() == submit)
  {
   setVisible(false);
   trainFrame = new TrainFrame(fileNameField.getText(),category.getText());
  
  }
  
  if( e.getSource() == exit )
   {
    System.exit(0);
   }
 }
     
 public static void main( String args[])
 {
  Train train = new Train();
  train.setVisible( true );
 
 }
}




