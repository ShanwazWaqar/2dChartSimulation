package com.nsfisdas;

public class RGBNode
{
 private int i;
 private int j;
 private RGB rgb;
 private RGBNode next;

  public RGBNode (int x, int y, RGB rgbs )
 {
   i = x;
   j = y;
   rgb = rgbs;
   next = null;
 }
 
 public int  getx()
 {
  return i;
 }
 public int gety()
 {
  return j;
 }

 public RGBNode( int x, int y, RGB rgbs, RGBNode nextNode)
 {
  i = x;
  j = y;
  rgb = rgbs;
  next = nextNode;
 }

 public RGBNode getNextNode()
 {
  return next;
 }

 public RGB getRGB()
 {
  return rgb;
 }
}
