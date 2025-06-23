package com.simulation.corrosion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for corrosion analysis responses
 */
@Data
@NoArgsConstructor
public class CorrosionAnalysisResponseDTO {
    
    private boolean success;
    private String message;
    private String error;
    private String initiationLife;
    private String crackPropagation;
    private String fatigueLife;
    
    // Constructor for success response
    public CorrosionAnalysisResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    // Constructor for error response
    public CorrosionAnalysisResponseDTO(String error, boolean success) {
        this.success = success;
        this.error = error;
    }
} 