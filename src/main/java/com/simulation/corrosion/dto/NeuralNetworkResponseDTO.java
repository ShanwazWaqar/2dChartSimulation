package com.simulation.corrosion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * DTO for neural network responses
 */
@Data
@NoArgsConstructor
public class NeuralNetworkResponseDTO {

    private boolean success;
    private String message;
    private String status;
    private String error;
    private String outputPath;
    private String resultsFile;
    private String weightsFile;
    private String errorFile;

    private Map<String, Object> trainingResults;
    private Map<String, Object> testingResults;
    private java.util.List<Map<String, Object>> predictions;
    private Map<String, Object> errorAnalysis;
    private Integer inNodes;
    private Integer outNodes;
    private Integer noLayers;

    // Constructor for success response
    public NeuralNetworkResponseDTO(boolean success, String message, String status) {
        this.success = success;
        this.message = message;
        this.status = status;
    }

    // Constructor for error response with different parameter order
    public NeuralNetworkResponseDTO(String error, boolean success, String status) {
        this.success = success;
        this.error = error;
        this.status = status;
    }
}