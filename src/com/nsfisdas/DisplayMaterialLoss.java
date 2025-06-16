package com.nsfisdas;
import java.awt.*;
import java.awt.event.*;

public class DisplayMaterialLoss extends Frame
{
 boolean corroded;
 float loss;
 public DisplayMaterialLoss()
	{ 
	  super("Material Loss");
	  setLocation(500,500);
	  setSize(300,150);
          corroded = false;
	  loss = 0.0f;
          setVisible(true);
	}
  public DisplayMaterialLoss(float loss)
        {
          super("Material Loss");
          setLocation(500,500);
          setSize(300,150); 
          corroded = true;
          this.loss = loss;
          setVisible(true);
        } 
          
  public void paint(Graphics g)
  {
 	if(corroded)
        {
         g.clearRect(0,0,100,50);
         g.drawString(" Material Loss of this segment is "+loss, 30,50);
        }
        else
        {
         g.clearRect(0,0,100,50);
         g.drawString(" The segment is not corroded", 30, 50);
        }
  }
}
