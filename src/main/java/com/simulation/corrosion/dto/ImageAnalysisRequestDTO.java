package com.simulation.corrosion.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for image analysis requests
 */
@Data
@NoArgsConstructor
public class ImageAnalysisRequestDTO {
    
    @NotNull(message = "Threshold cannot be null")
    @Min(value = 0, message = "Threshold must be at least 0")
    @Max(value = 255, message = "Threshold must be at most 255")
    private Integer threshold;
    
    private String analysisType = "corrosion";
    
    private Boolean enable2D = false;
    
    private String outputFormat = "json";
} 