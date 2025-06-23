package com.simulation.legacy;

import java.io.*;
import java.util.zip.*;

public class Simulate2D {
    public ImageMatrix2D mtrx;

    public String[] PitSize;
    public String[] PitGrowthArray;
    private int t_count;
    private int s_m;
    private int iter;
    private double inputp1;
    private double inputp2;
    private double inputp3;
    private double inputp4;
    private double cst;
    private int sf;
    private String noRows;
    private String noColumns;
    private String param1;
    private String param2;
    private String param3;
    private String param4;
    private String constant;
    private String stressfact;
    private String noIterations;
    private String path;

    public String fileName1;
    public String fileName2;
    public String fileName3;
    public String fileName4;
    public String fileName5;

    public FileWriter fos1;
    public FileWriter fos2;
    public FileWriter fos3;
    public FileWriter fos4;
    public FileWriter fos5;

    public String[] mtrx_stat2D;
    public String[] mtrx_wavelet2D;
    public String[] mtrx_simulate;

    static final int BUFFER = 2048;

    public Simulate2D(String noRows, String noColumns, String param1, String param2, String param3, String param4, String constant, String stressfact, String noIterations, String path) {
        this.noRows = noRows;
        this.noColumns = noColumns;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.constant = constant;
        this.stressfact = stressfact;
        this.noIterations = noIterations;
        this.path = path;
    }

    public void runSimulation() {
        inputp1 = Double.parseDouble(param1);
        inputp2 = Double.parseDouble(param2);
        inputp3 = Double.parseDouble(param3);
        inputp4 = Double.parseDouble(param4);
        cst = Double.parseDouble(constant);
        sf = Integer.parseInt(stressfact);

        int i = Integer.parseInt(noRows);
        int j = Integer.parseInt(noColumns);
        int scale = 400 / i;
        iter = Integer.parseInt(noIterations);
        PitSize = new String[iter];
        PitGrowthArray = new String[iter];
        mtrx = new ImageMatrix2D(i, j, scale, iter, path, inputp1, inputp2, inputp3, inputp4);

        mtrx_stat2D = new String[iter + 1];
        mtrx_wavelet2D = new String[iter + 1];
        mtrx_simulate = new String[iter + 1];

        t_count = 0;

        while (t_count < iter) {
            System.out.println("\nIteration No::" + t_count);
            for (int k = 0; k < 5; k++) {
                simulate(inputp1, inputp3, inputp2, inputp4, cst, sf, t_count);
            }

            mtrx_stat2D[t_count] = mtrx.stat2D();
            mtrx_wavelet2D[t_count] = mtrx.wavelet2D(6);
            PitGrowthArray[t_count] = (t_count + 1) + "," + mtrx.getMaxRedHt() + "," + mtrx.getmaxGreenHt() + "," + mtrx.getmaxBlueHt();
            PitSize[t_count] = (t_count + 1) + "," + mtrx.getWidthCnt() + "," + mtrx.getHeightCnt();
            t_count++;

            try {
                Thread.sleep(400L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        writeSimulationResults();
    }

    private void writeSimulationResults() {
        try {
            new File(path).mkdirs();
            new File(path + "files2D/").mkdirs();

            fileName1 = path + "files2D/Features1.txt";
            fileName2 = path + "files2D/Others1.txt";
            fileName3 = path + "files2D/PitSize1.txt";
            fileName4 = path + "files2D/PitGrowthRate1.txt";
            fileName5 = path + "RedPixels.txt";

            fos1 = new FileWriter(fileName1);
            fos2 = new FileWriter(fileName2);
            fos3 = new FileWriter(fileName3);
            fos4 = new FileWriter(fileName4);
            fos5 = new FileWriter(fileName5);

            fos1.write("Skew,Energy,Entropy,Ratio1,Skew,Energy,Entropy,Ratio2\n");
            fos2.write("pH,potential,concentration: \n");

            for (int i = 0; i < t_count; i++) {
                fos1.write(mtrx_stat2D[i] + mtrx_wavelet2D[i] + "\n");
                fos2.write(mtrx_simulate[i] + "\n");
            }

            fos3.write("Iteration,Width,Height\n");
            fos4.write("Iteration,RedRate,GreenRate,BlueRate\n");

            for (String s : PitSize) fos3.write(s + "\n");
            for (String s : PitGrowthArray) fos4.write(s + "\n");
            fos5.write(mtrx.getRedPixels() + "\n");

            fos1.close();
            fos2.close();
            fos3.close();
            fos4.close();
            fos5.close();

            zipSimulationFiles();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zipSimulationFiles() throws IOException {
        File dir = new File(path + "files2D/");
        String[] files = dir.list();
        if (files == null) return;

        try (FileOutputStream dest = new FileOutputStream(path + "files2D/sim2DFiles.zip");
             ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest))) {
            byte[] data = new byte[BUFFER];

            for (String file : files) {
                try (BufferedInputStream origin = new BufferedInputStream(new FileInputStream(dir + "/" + file), BUFFER)) {
                    ZipEntry entry = new ZipEntry(file);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                }
            }
        }
    }

    public void simulate(double d, double d1, double d2, double d3, double cs, int sf, int itrNo) {
        mtrx_simulate[t_count] = mtrx.simulate(d, d1, d2, d3, 0.5D, 3D, cs, sf, itrNo);
    }
}
