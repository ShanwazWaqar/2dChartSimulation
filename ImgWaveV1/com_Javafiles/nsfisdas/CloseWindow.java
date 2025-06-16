package com.nsfisdas;
import java.awt.event.*;

public class CloseWindow extends WindowAdapter
{
  public void windowClosing ( WindowEvent e )
  {
    e.getWindow().setVisible(false);
  }
}
