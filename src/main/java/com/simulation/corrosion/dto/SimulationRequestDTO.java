package com.simulation.corrosion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SimulationRequestDTO {

    @NotNull(message = "Number of rows is required")
    @Min(1)
    private Integer noRows;

    @NotNull(message = "Number of columns is required")
    @Min(1)
    private Integer noColumns;

    private Double phValue;
    private Double temperature;
    private Double potential;
    private Double concentration;
    private Double stressFactor;
    private Integer iterations;

    // Getters and Setters
    public Integer getNoRows() {
        return noRows;
    }

    public void setNoRows(Integer noRows) {
        this.noRows = noRows;
    }

    public Integer getNoColumns() {
        return noColumns;
    }

    public void setNoColumns(Integer noColumns) {
        this.noColumns = noColumns;
    }

    public Double getPhValue() {
        return phValue;
    }

    public void setPhValue(Double phValue) {
        this.phValue = phValue;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPotential() {
        return potential;
    }

    public void setPotential(Double potential) {
        this.potential = potential;
    }

    public Double getConcentration() {
        return concentration;
    }

    public void setConcentration(Double concentration) {
        this.concentration = concentration;
    }

    public Double getStressFactor() {
        return stressFactor;
    }

    public void setStressFactor(Double stressFactor) {
        this.stressFactor = stressFactor;
    }

    public Integer getIterations() {
        return iterations;
    }

    public void setIterations(Integer iterations) {
        this.iterations = iterations;
    }
}
