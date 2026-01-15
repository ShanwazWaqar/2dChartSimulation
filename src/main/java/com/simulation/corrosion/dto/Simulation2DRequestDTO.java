package com.simulation.corrosion.dto;

public class Simulation2DRequestDTO {
    private int noRows;
    private int noColumns;
    private double param1;
    private double param2;
    private double param3;
    private double param4;
    private double constant;
    private int stressFact;
    private int noIterations;
    private String path;

    // Multi-pit configuration
    private int numPits;
    private int pitSpacing;
    private int pitSize;

    // Getters and setters
    public int getNoRows() {
        return noRows;
    }

    public void setNoRows(int noRows) {
        this.noRows = noRows;
    }

    public int getNoColumns() {
        return noColumns;
    }

    public void setNoColumns(int noColumns) {
        this.noColumns = noColumns;
    }

    public double getParam1() {
        return param1;
    }

    public void setParam1(double param1) {
        this.param1 = param1;
    }

    public double getParam2() {
        return param2;
    }

    public void setParam2(double param2) {
        this.param2 = param2;
    }

    public double getParam3() {
        return param3;
    }

    public void setParam3(double param3) {
        this.param3 = param3;
    }

    public double getParam4() {
        return param4;
    }

    public void setParam4(double param4) {
        this.param4 = param4;
    }

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

    public int getStressFact() {
        return stressFact;
    }

    public void setStressFact(int stressFact) {
        this.stressFact = stressFact;
    }

    public int getNoIterations() {
        return noIterations;
    }

    public void setNoIterations(int noIterations) {
        this.noIterations = noIterations;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNumPits() {
        return numPits;
    }

    public void setNumPits(int numPits) {
        this.numPits = numPits;
    }

    public int getPitSpacing() {
        return pitSpacing;
    }

    public void setPitSpacing(int pitSpacing) {
        this.pitSpacing = pitSpacing;
    }

    public int getPitSize() {
        return pitSize;
    }

    public void setPitSize(int pitSize) {
        this.pitSize = pitSize;
    }
}