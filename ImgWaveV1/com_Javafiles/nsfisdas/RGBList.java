package com.nsfisdas;

public class RGBList
{
 private RGBNode FirstNode;
 private RGBNode LastNode;
 private int numMembers;
 private String name;

 public RGBList (String s)
 {
  name = s;
  FirstNode = LastNode = null;
  numMembers = 0;
 }

 public RGBList()
 {
  name = "RGB List";
  FirstNode = LastNode = null;
  numMembers = 0;
 }

 public RGBNode getFirstNode()
 {
   return FirstNode;
 }
 public RGBNode getLastNode()
 {
  return LastNode;
 }
 public boolean isEmpty()
 {
  return (FirstNode == null);
 }

 public void PushNode(int x, int y, RGB rgbVal)
 {
  if ( isEmpty() )
  {
   FirstNode = LastNode = new RGBNode(x, y, rgbVal );
  }
  else
  {
   FirstNode = new RGBNode(x, y, rgbVal, FirstNode );
  }
  numMembers++;
 }
 
 public int getNumMembers()
 {
  return numMembers;
 } 

}
 
 
