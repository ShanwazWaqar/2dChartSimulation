package com.simulation.corrosion.controller;

import com.simulation.corrosion.dto.CorrosionAnalysisRequestDTO;
import com.simulation.corrosion.dto.CorrosionAnalysisResponseDTO;
import com.simulation.corrosion.service.CorrosionAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Modern REST controller for corrosion analysis operations
 */
@RestController
@RequestMapping("/api/corrosion")
@Tag(name = "Corrosion Analysis", description = "Corrosion analysis and fatigue calculations")
public class CorrosionAnalysisController {

    @Autowired
    private CorrosionAnalysisService corrosionAnalysisService;

    @PostMapping("/analyze")
    @Operation(summary = "Analyze corrosion", description = "Perform corrosion analysis with given parameters")
    public ResponseEntity<CorrosionAnalysisResponseDTO> analyzeCorrosion(
            @Valid @RequestBody CorrosionAnalysisRequestDTO request) {
        
        try {
            CorrosionAnalysisResponseDTO response = corrosionAnalysisService.analyzeCorrosion(request);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            CorrosionAnalysisResponseDTO errorResponse = new CorrosionAnalysisResponseDTO(
                e.getMessage(), false
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the corrosion analysis service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Corrosion Analysis Service is running");
    }
} 