package com.simulation.corrosion.service;

import com.simulation.corrosion.dto.ImageAnalysisRequestDTO;
import com.simulation.corrosion.dto.ImageAnalysisResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Modern Spring Boot service for image analysis
 * Provides a clean interface to the legacy image analysis functionality
 */
@Service
public class ImageAnalysisService {
    
    /**
     * Analyze image for corrosion detection
     */
    public ImageAnalysisResponseDTO analyzeImage(ImageAnalysisRequestDTO request, MultipartFile imageFile) {
        ImageAnalysisResponseDTO response = new ImageAnalysisResponseDTO();
        
        try {
            // Validate request and file
            validateRequest(request, imageFile);
            
            // Save uploaded image
            String imagePath = saveImageFile(imageFile);
            
            // For now, return a placeholder response
            // The actual integration with legacy ImageAnalysis will be implemented
            // based on the specific requirements and legacy code structure
            
            response.setSuccess(true);
            response.setCorrosionDetected(false); // Placeholder
            response.setCorrosionPercentage(0.0); // Placeholder
            response.setAnalysisResult("Image analysis completed - legacy integration pending");
            response.setMessage("Image analysis completed successfully");
            response.setImagePath(imagePath);
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Perform 2D image analysis
     */
    public ImageAnalysisResponseDTO analyzeImage2D(ImageAnalysisRequestDTO request, MultipartFile imageFile) {
        ImageAnalysisResponseDTO response = new ImageAnalysisResponseDTO();
        
        try {
            // Validate request and file
            validateRequest(request, imageFile);
            
            // Save uploaded image
            String imagePath = saveImageFile(imageFile);
            
            // For now, return a placeholder response
            // The actual integration with legacy ImageMatrix2D will be implemented
            // based on the specific requirements and legacy code structure
            
            response.setSuccess(true);
            response.setCorrosionDetected(false); // Placeholder
            response.setCorrosionPercentage(0.0); // Placeholder
            response.setAnalysisResult("2D image analysis completed - legacy integration pending");
            response.setMessage("2D image analysis completed successfully");
            response.setImagePath(imagePath);
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Validate image analysis request
     */
    private void validateRequest(ImageAnalysisRequestDTO request, MultipartFile imageFile) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be empty");
        }
        
        if (request.getThreshold() < 0 || request.getThreshold() > 255) {
            throw new IllegalArgumentException("Threshold must be between 0 and 255");
        }
        
        // Validate file type
        String contentType = imageFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }
    }
    
    /**
     * Save uploaded image file
     */
    private String saveImageFile(MultipartFile imageFile) throws IOException {
        // Get absolute path of your project root
        String projectDir = System.getProperty("user.dir");
        Path uploadDir = Paths.get(projectDir, "output", "images");
    
        // Ensure directories exist
        Files.createDirectories(uploadDir);
    
        // Generate unique filename
        String originalFilename = imageFile.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
    
        String filename = "image_" + System.currentTimeMillis() + extension;
        Path filePath = uploadDir.resolve(filename);
    
        // Save the file
        imageFile.transferTo(filePath.toFile());
    
        return filePath.toString();
    }
    
} 