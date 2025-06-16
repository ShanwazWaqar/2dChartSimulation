package com.nsfisdas;
public class Daubechies
{
 public Daubechies()
 {
 }

 public float[] getH1()
 {
  float H1[] = {0.7071f, 0.7071f};
  return H1;
 }
 
 public float[] getG1()
 {
  float G1[] = {0.7071f, -0.7071f};
  return G1;
 }

 public float[] getH2()
 {
  float H2[] = {0.4830f, 0.8365f, 0.2241f, -0.1294f};
  return H2;
 }
 public float[] getG2()
 {
  float G2[] = {-0.1294f, -0.2241f, 0.8365f, -0.4830f};
  return G2;
 }

 public float[] getH3()
 {
  float H3[] = {0.3327f, 0.8069f, 0.4599f, -0.1350f, -0.0854f, 0.0352f};
  return H3;
 }
 public float[] getG3()
 {
  float G3[] = {0.0352f, 0.0854f, -0.1350f, -0.4599f, 0.8069f, -0.3327f};
  return G3;
 }

 public float[] getH4()
 {
  float H4[] = {0.2304f, 0.7148f, 0.6309f, -0.0280f, -0.1870f, 0.0308f, 0.0329f, -0.0106f};
  return H4;
 }
 public float[] getG4()
 {
  float G4[] = {-0.016f, -0.0329f, 0.0308f, 0.1870f, -0.0280f, -0.6309f, 0.7148f, -0.2304f};
  return G4;
 }


 public float[] getH5()
 {
  float H5[] = {0.1601f, 0.6038f, 0.7243f, 0.1384f, -0.2423f, -0.0322f, 0.0776f, -0.0062f, -0.0126f, 0.0033f};
  return H5;
 }
 public float[] getG5()
 {
  float G5[] = {0.0033f, 0.0126f, -0.0062f, -0.0776f, -0.0322f, 0.2423f, 0.1384f, -0.7243f, 0.6038f, -0.1601f};
  return G5;
 }


}
