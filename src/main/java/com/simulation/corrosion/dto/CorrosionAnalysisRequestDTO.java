package com.simulation.corrosion.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for corrosion analysis requests
 */
@Data
@NoArgsConstructor
public class CorrosionAnalysisRequestDTO {
    
    // Corrosion parameters
    @NotBlank(message = "Atomic mass cannot be empty")
    private String atomMass;
    
    @NotBlank(message = "Valence cannot be empty")
    private String valence;
    
    @NotBlank(message = "Faraday's constant cannot be empty")
    private String farCnst;
    
    @NotBlank(message = "Density cannot be empty")
    private String density;
    
    private String thresRng;
    private String stressCnst;
    private String pitCoeff;
    private String enthalpy;
    private String gasCnst;
    
    @NotBlank(message = "Temperature cannot be empty")
    private String temp;
    
    private String freq;
    
    // Fatigue parameters
    @NotBlank(message = "Fatigue coefficient for small crack cannot be empty")
    private String fcoeffsmall;
    
    @NotBlank(message = "Fatigue coefficient for long crack cannot be empty")
    private String fcoefflong;
    
    @NotBlank(message = "Fatigue exponent for small crack cannot be empty")
    private String fexpsmall;
    
    @NotBlank(message = "Fatigue exponent for long crack cannot be empty")
    private String fexplong;
    
    private String geoSmall;
    private String geoLong;
    private String crackLen;
    private String fracTough;
    
    @NotBlank(message = "Stress cannot be empty")
    private String sigma;
} 