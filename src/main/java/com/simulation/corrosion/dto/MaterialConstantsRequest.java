package com.simulation.corrosion.dto;

public class MaterialConstantsRequest {
    private double pitCoeff;
    private double freq;
    private double sigma;
    private double apc_final;
    private double af_final;
    private double scoeff;
    private String selectedMaterial;

    // Getters and Setters
    public double getPitCoeff() {
        return pitCoeff;
    }

    public void setPitCoeff(double pitCoeff) {
        this.pitCoeff = pitCoeff;
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getApc_final() {
        return apc_final;
    }

    public void setApc_final(double apc_final) {
        this.apc_final = apc_final;
    }

    public double getAf_final() {
        return af_final;
    }

    public void setAf_final(double af_final) {
        this.af_final = af_final;
    }

    public double getScoeff() {
        return scoeff;
    }

    public void setScoeff(double scoeff) {
        this.scoeff = scoeff;
    }

    public String getSelectedMaterial() {
        return selectedMaterial;
    }

    public void setSelectedMaterial(String selectedMaterial) {
        this.selectedMaterial = selectedMaterial;
    }
}
