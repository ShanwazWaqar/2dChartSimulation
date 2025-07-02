package com.simulation.corrosion.service;

import com.simulation.corrosion.dto.ImageAnalysisRequestDTO;
import com.simulation.corrosion.dto.ImageAnalysisResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.simulation.legacy.ImageMatrix;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Modern Spring Boot service for image analysis
 * Provides a clean interface to the legacy image analysis functionality
 */
@Service
public class ImageAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(ImageAnalysisService.class);
    
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

            String projectDir = System.getProperty("user.dir");

            logger.info("Starting 2D image analysis");
            // Step 1: Create matrix for simulation
            int rows = request.getRows();
            int cols = request.getCols();
            int scale = request.getScale();
            int iterations = request.getIterations();

            ImageMatrix matrix = new ImageMatrix(rows, cols, scale, iterations, projectDir);

            

            // Step 2: Set pixel data from uploaded image
            BufferedImage uploadedImg = ImageIO.read(new File(imagePath));
            logger.info("Image read from path: {}", imagePath);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int[] rgb = {
                        (uploadedImg.getRGB(j, i) >> 16) & 0xFF, // Red
                        (uploadedImg.getRGB(j, i) >> 8) & 0xFF,  // Green
                        uploadedImg.getRGB(j, i) & 0xFF          // Blue
                    };
                    float max = Math.max(rgb[0], Math.max(rgb[1], rgb[2]));
                    matrix.setPixel(i, j, max);
                }
            }

            // Step 3: Run simulation
            String simulationResult = matrix.simulate(
                request.getParam1(), request.getParam2(), request.getParam3(),
                request.getParam4(), request.getConstant(), request.getStressFactor(),
                request.getThreshold(), request.getScaleFactor(), iterations
            );

            // Step 4: Save output image
            String outputImagePath = Paths.get(projectDir, "output", "images", "processed_" + System.currentTimeMillis() + ".jpg").toString();
            matrix.saveRenderedImage(outputImagePath);

            // Step 5: Compute corrosion percentage
            int totalPixels = matrix.getRows() * matrix.getCols();
            int corrodedPixels = 0;
            int threshold = request.getThreshold();

            logger.debug("Request parameters: rows={}, cols={}, scale={}, iterations={}, threshold={}",
    rows, cols, scale, iterations, threshold);

            for (int i = 0; i < matrix.getRows(); i++) {
                for (int j = 0; j < matrix.getCols(); j++) {
                    if (matrix.getPixel(i, j) > threshold) {
                        corrodedPixels++;
                    }
                }
            }
            double corrosionPercentage = (corrodedPixels * 100.0) / totalPixels;

            // Step 5: Fill in the response
            response.setSuccess(true);
            response.setMessage("2D image simulation completed successfully");
            response.setCorrosionDetected(corrosionPercentage > 0); // Optional: use logic if needed
            response.setCorrosionPercentage(corrosionPercentage); // Optional: compute from pixels
            response.setAnalysisResult(simulationResult);
            response.setImagePath(imagePath);
            response.setProcessedImagePath(outputImagePath);
            

            logger.info("Simulation completed. Result: {}", simulationResult);
logger.debug("Total pixels: {}, Corroded: {}, Corrosion %: {}", totalPixels, corrodedPixels, corrosionPercentage);
            
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