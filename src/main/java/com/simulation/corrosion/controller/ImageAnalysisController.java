package com.simulation.corrosion.controller;

import com.simulation.corrosion.dto.ImageAnalysisRequestDTO;
import com.simulation.corrosion.dto.ImageAnalysisResponseDTO;
import com.simulation.corrosion.service.ImageAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Encoding;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Modern REST controller for image analysis operations
 */
@RestController
@RequestMapping("/api/image-analysis")
@Tag(name = "Image Analysis", description = "Image analysis for corrosion detection")
public class ImageAnalysisController {

    @Autowired
    private ImageAnalysisService imageAnalysisService;

    @PostMapping("/analyze")
    @Operation(summary = "Analyze image", description = "Analyze image for corrosion detection")
    public ResponseEntity<ImageAnalysisResponseDTO> analyzeImage(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("threshold") Integer threshold,
            @RequestParam(value = "analysisType", defaultValue = "corrosion") String analysisType,
            @RequestParam(value = "enable2D", defaultValue = "false") Boolean enable2D,
            @RequestParam(value = "outputFormat", defaultValue = "json") String outputFormat) {
        try {
            ImageAnalysisRequestDTO request = new ImageAnalysisRequestDTO();
            request.setThreshold(threshold);
            request.setAnalysisType(analysisType);
            request.setEnable2D(enable2D);
            request.setOutputFormat(outputFormat);
            ImageAnalysisResponseDTO response = imageAnalysisService.analyzeImage(request, imageFile);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            ImageAnalysisResponseDTO errorResponse = new ImageAnalysisResponseDTO(
                e.getMessage(), false
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/analyze-2d")
    @Operation(summary = "Analyze image 2D", description = "Perform 2D image analysis for corrosion detection")
    public ResponseEntity<ImageAnalysisResponseDTO> analyzeImage2D(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("threshold") Integer threshold,
            @RequestParam(value = "analysisType", defaultValue = "corrosion") String analysisType,
            @RequestParam(value = "enable2D", defaultValue = "true") Boolean enable2D,
            @RequestParam(value = "outputFormat", defaultValue = "json") String outputFormat) {
        try {
            ImageAnalysisRequestDTO request = new ImageAnalysisRequestDTO();
            request.setThreshold(threshold);
            request.setAnalysisType(analysisType);
            request.setEnable2D(enable2D);
            request.setOutputFormat(outputFormat);
            ImageAnalysisResponseDTO response = imageAnalysisService.analyzeImage2D(request, imageFile);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            ImageAnalysisResponseDTO errorResponse = new ImageAnalysisResponseDTO(
                e.getMessage(), false
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the image analysis service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Image Analysis Service is running");
    }
} 