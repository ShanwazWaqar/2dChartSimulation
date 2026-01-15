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
            logger.info("Uploaded image saved to: {}", imagePath);

            // Process the image based on analysis type
            String projectDir = System.getProperty("user.dir");
            Path outputDir = Paths.get(projectDir, "output", "images");
            Files.createDirectories(outputDir);

            // Read the uploaded image
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            BufferedImage processedImage;
            double corrosionPercentage;
            String analysisType = request.getAnalysisType();
            int threshold = request.getThreshold();

            if ("edge".equalsIgnoreCase(analysisType)) {
                // Edge Detection using Sobel operator
                processedImage = applyEdgeDetection(originalImage, threshold);
                corrosionPercentage = calculateEdgeBasedCorrosion(processedImage);
            } else {
                // Wavelet Transform (multi-scale intensity analysis)
                processedImage = applyWaveletTransform(originalImage, threshold);
                corrosionPercentage = calculateWaveletBasedCorrosion(originalImage, threshold);
            }

            // Save processed image
            String processedFileName = analysisType + "_processed_" + System.currentTimeMillis() + ".png";
            Path processedImagePath = outputDir.resolve(processedFileName);
            ImageIO.write(processedImage, "png", processedImagePath.toFile());
            logger.info("Processed image saved to: {}", processedImagePath);

            response.setSuccess(true);
            response.setCorrosionDetected(corrosionPercentage > 0);
            response.setCorrosionPercentage(corrosionPercentage);
            response.setAnalysisResult(String.format(
                    "Analysis: %s | Threshold: %d | Detected: %.2f%%",
                    analysisType.toUpperCase(), threshold, corrosionPercentage));
            response.setMessage("Image analysis completed successfully");
            response.setImagePath(imagePath);
            response.setProcessedImagePath(processedImagePath.toString());

        } catch (Exception e) {
            logger.error("Error during image analysis", e);
            response.setSuccess(false);
            response.setError(e.getMessage());
        }

        return response;
    }

    /**
     * Apply Sobel edge detection to highlight boundaries and defects
     */
    private BufferedImage applyEdgeDetection(BufferedImage original, int threshold) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Sobel kernels
        int[][] sobelX = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
        int[][] sobelY = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gx = 0, gy = 0;

                // Apply Sobel operator
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int rgb = original.getRGB(x + j, y + i);
                        int gray = ((rgb >> 16) & 0xFF + (rgb >> 8) & 0xFF + (rgb & 0xFF)) / 3;
                        gx += gray * sobelX[i + 1][j + 1];
                        gy += gray * sobelY[i + 1][j + 1];
                    }
                }

                int magnitude = (int) Math.sqrt(gx * gx + gy * gy);

                // Threshold and colorize edges
                if (magnitude > threshold / 2) {
                    // Strong edges in bright cyan (defects/boundaries)
                    result.setRGB(x, y, 0x00FFFF);
                } else if (magnitude > threshold / 4) {
                    // Weak edges in yellow
                    result.setRGB(x, y, 0xFFFF00);
                } else {
                    // Background in dark gray
                    result.setRGB(x, y, 0x202020);
                }
            }
        }

        return result;
    }

    /**
     * Apply wavelet-like multi-scale intensity analysis
     */
    private BufferedImage applyWaveletTransform(BufferedImage original, int threshold) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = original.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int gray = (r + g + b) / 3;

                // Multi-scale intensity classification
                if (gray > threshold + 50) {
                    // High intensity - severe corrosion (red)
                    result.setRGB(x, y, 0xFF0000);
                } else if (gray > threshold) {
                    // Medium-high intensity - moderate corrosion (orange)
                    result.setRGB(x, y, 0xFF8800);
                } else if (gray > threshold - 50) {
                    // Medium intensity - early corrosion (yellow)
                    result.setRGB(x, y, 0xFFFF00);
                } else {
                    // Low intensity - healthy material (blue-gray)
                    int dimGray = gray / 2;
                    result.setRGB(x, y, (dimGray << 16) | (dimGray << 8) | (dimGray + 50));
                }
            }
        }

        return result;
    }

    private double calculateEdgeBasedCorrosion(BufferedImage processedImage) {
        int totalPixels = processedImage.getWidth() * processedImage.getHeight();
        int edgePixels = 0;

        for (int y = 0; y < processedImage.getHeight(); y++) {
            for (int x = 0; x < processedImage.getWidth(); x++) {
                int rgb = processedImage.getRGB(x, y);
                // Count cyan and yellow pixels (edges)
                if (rgb == 0x00FFFF || rgb == 0xFFFF00) {
                    edgePixels++;
                }
            }
        }

        return (edgePixels * 100.0) / totalPixels;
    }

    private double calculateWaveletBasedCorrosion(BufferedImage original, int threshold) {
        int totalPixels = original.getWidth() * original.getHeight();
        int corrodedPixels = 0;

        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                int rgb = original.getRGB(x, y);
                int gray = ((rgb >> 16) & 0xFF + (rgb >> 8) & 0xFF + (rgb & 0xFF)) / 3;
                if (gray > threshold) {
                    corrodedPixels++;
                }
            }
        }

        return (corrodedPixels * 100.0) / totalPixels;
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
                            (uploadedImg.getRGB(j, i) >> 8) & 0xFF, // Green
                            uploadedImg.getRGB(j, i) & 0xFF // Blue
                    };
                    float max = Math.max(rgb[0], Math.max(rgb[1], rgb[2]));
                    matrix.setPixel(i, j, max);
                }
            }

            // Step 3: Run simulation
            String simulationResult = matrix.simulate(
                    request.getParam1(), request.getParam2(), request.getParam3(),
                    request.getParam4(), request.getConstant(), request.getStressFactor(),
                    request.getThreshold(), request.getScaleFactor(), iterations);

            // Step 4: Save output image
            String outputImagePath = Paths
                    .get(projectDir, "output", "images", "processed_" + System.currentTimeMillis() + ".jpg").toString();
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
            logger.debug("Total pixels: {}, Corroded: {}, Corrosion %: {}", totalPixels, corrodedPixels,
                    corrosionPercentage);

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