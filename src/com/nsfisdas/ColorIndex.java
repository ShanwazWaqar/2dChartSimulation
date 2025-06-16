package com.nsfisdas;
import java.awt.*;
import java.awt.event.*;

class ColorIndex extends Frame
{
 public ColorIndex()
 {
  super(" Color Index ");
  setLocation(400,400);
  setSize(300,175);
  setBackground(Color.black);
  setVisible(true);
 }
 
 public void paint(Graphics g)
 {
   g.setColor(Color.red);
   g.drawString(" MATERIAL LOSS -- COLOR INDEX",15, 45);
   g.setColor(Color.blue);
   g.drawString("      0%  - 5%  : Blue", 30,70);
   g.setColor(Color.green);
   g.drawString("      5%  - 10% : Green", 30, 90);
   g.setColor(Color.yellow);
   g.drawString("      10% - 15% : Yello", 30, 110);
   g.setColor(Color.magenta);
   g.drawString("      15% - 20% : Magneta",30, 130);
   g.setColor(Color.red);
   g.drawString("      20% - 25% : Red   ",30, 150);
 }
 
  public static void main( String args[])
  {
   ColorIndex colorIndex;
   colorIndex = new ColorIndex();
 }

}


