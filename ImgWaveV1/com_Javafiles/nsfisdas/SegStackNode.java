package com.nsfisdas;

public class SegStackNode
{
 private Pixel pixs;
 private SegStackNode next;

 public SegStackNode ( Pixel P )
 {
   pixs = P;
   next = null;
 }

 public SegStackNode( Pixel P,SegStackNode nextNode )
 {
  pixs = P;
  next = nextNode;
 }

 public Pixel getPixel()
 {
  return pixs;
 }
 public SegStackNode getNextNode()
 {
  return next;
 }
}
