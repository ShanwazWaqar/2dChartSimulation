package com.simulation.legacy.nsfisdas;
// import java.awt.*;

// public class DisplayMaterialLoss extends Frame
public class DisplayMaterialLoss
{
 boolean corroded;
 float loss;
 public DisplayMaterialLoss()
	{ 
	  // super("Material Loss");
	  // setLocation(500,500);
	  // setSize(300,150);
          corroded = false;
	  loss = 0.0f;
          // setVisible(true);
	}
  public DisplayMaterialLoss(float loss)
        {
          // super("Material Loss");
          // setLocation(500,500);
          // setSize(300,150); 
          corroded = true;
          this.loss = loss;
          // setVisible(true);
        } 
          
  // public void paint(Graphics g)
  // {
  	// if(corroded)
  	// {
  		// g.drawString("Material Loss: "+loss,20,30);
  	// }
  	// else
  	// {
  		// g.drawString("No Corrosion Detected",20,30);
  	// }
  // }
}
