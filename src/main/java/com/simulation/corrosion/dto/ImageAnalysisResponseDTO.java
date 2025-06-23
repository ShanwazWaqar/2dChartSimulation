package com.simulation.corrosion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for image analysis responses
 */
@Data
@NoArgsConstructor
public class ImageAnalysisResponseDTO {
    
    private boolean success;
    private String message;
    private String error;
    private boolean corrosionDetected;
    private double corrosionPercentage;
    private String analysisResult;
    private String imagePath;
    private String processedImagePath;
    
    // Constructor for success response
    public ImageAnalysisResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    // Constructor for error response
    public ImageAnalysisResponseDTO(String error, boolean success) {
        this.success = success;
        this.error = error;
    }
} 