package com.simulation.corrosion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for neural network training and testing requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeuralNetworkRequestDTO {
    
    @NotBlank(message = "Input nodes cannot be empty")
    private String inNodes;
    
    @NotBlank(message = "Output nodes cannot be empty")
    private String outNodes;
    
    @NotBlank(message = "Number of layers cannot be empty")
    private String noLayers;
    
    @NotBlank(message = "First layer nodes cannot be empty")
    private String noFirstLayer;
    
    @NotBlank(message = "Second layer nodes cannot be empty")
    private String noSecLayer;
    
    @NotBlank(message = "Number of training data cannot be empty")
    private String noTrainData;
    
    @NotBlank(message = "Number of test data cannot be empty")
    private String noTestData;
    
    private String serverPath;
    
    // Additional fields for file uploads
    private String trainFileName;
    private String testFileName;
} 