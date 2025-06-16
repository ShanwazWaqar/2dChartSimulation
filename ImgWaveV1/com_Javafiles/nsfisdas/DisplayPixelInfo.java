package com.nsfisdas;
import java.awt.*;
import java.awt.event.*;
class DisplayPixelInfo extends Frame
{
  private int R,G,B,x,y;
  private boolean set;
	public DisplayPixelInfo(String name)
	{ 
	  super(name);
	  set = false;
	  setLocation(500,500);
	  setSize(300,150);
	  setVisible(false);
	}
	public void setValues(int x, int y, int R, int G, int B)
	{
	  this.R = R;
	  this.G = G;
	  this.B = B;
	  this.x = x;
	  this.y = y;
	  setVisible(true);
	  set = true;
	  repaint();
	}

	public void paint(Graphics g)
	{
	  if( set )
	  {
	   g.clearRect(0,0,100,50);
	   g.drawString("Row: "+y+"  Column: "+x, 30,50);
	   g.drawString("Red: "+R+" Green: "+G+" Blue: "+B, 30,70);
	  }
	}
}
