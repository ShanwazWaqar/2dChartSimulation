package com.simulation.corrosion.dto;

import java.util.Map;

public class Simulation2DResponseDTO {
    private boolean success;
    private String message;
    private String processedImagePath;
    private Map<String, Object> stats;
    private Double pitSize;
    private Double pitGrowth;
    // Add more fields as needed

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getProcessedImagePath() { return processedImagePath; }
    public void setProcessedImagePath(String processedImagePath) { this.processedImagePath = processedImagePath; }
    public Map<String, Object> getStats() { return stats; }
    public void setStats(Map<String, Object> stats) { this.stats = stats; }
    public Double getPitSize() { return pitSize; }
    public void setPitSize(Double pitSize) { this.pitSize = pitSize; }
    public Double getPitGrowth() { return pitGrowth; }
    public void setPitGrowth(Double pitGrowth) { this.pitGrowth = pitGrowth; }
} 