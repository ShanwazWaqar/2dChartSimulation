package com.nsfisdas;

public class SegStack
{
 private SegStackNode FirstNode;
 private SegStackNode LastNode;
 private int numMembers;
 private String name;

 public SegStack (String s)
 {
  name = s;
  FirstNode = LastNode = null;
  numMembers = 0;
 }

 public SegStack()
 {
  name = "Segment Stack";
  FirstNode = LastNode = null;
  numMembers = 0;
 }

 public boolean isEmpty()
 {
  return (FirstNode == null);
 }

 public void PushNode(Pixel pixs)
 {
  if ( isEmpty() )
  {
   FirstNode = LastNode = new SegStackNode( pixs );
  }
  else
  {
   FirstNode = new SegStackNode( pixs,FirstNode );
  }
  numMembers++;
 }

 public Pixel PopNode()
 {
  Pixel temp;
  temp = FirstNode.getPixel();
  numMembers--;
  FirstNode = FirstNode.getNextNode();
  return temp;
 }
}
 
 
