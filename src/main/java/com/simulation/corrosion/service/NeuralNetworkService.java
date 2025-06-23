package com.simulation.corrosion.service;

import com.simulation.corrosion.dto.NeuralNetworkRequestDTO;
import com.simulation.corrosion.dto.NeuralNetworkResponseDTO;
import com.simulation.legacy.sim.nnet;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;

/**
 * Modern Spring Boot service for neural network operations
 * Provides a clean interface to the legacy neural network functionality
 */
@Service("modernNeuralNetworkService")
public class NeuralNetworkService {
    
    @Value("${app.upload.path:output/upload}")
    private String uploadPath;
    
    @Value("${app.output.path:output/nnet_sia}")
    private String outputPath;
    
    /**
     * Train the neural network with the given parameters
     */
    public NeuralNetworkResponseDTO trainNetwork(NeuralNetworkRequestDTO request, MultipartFile trainFile) {
        NeuralNetworkResponseDTO response = new NeuralNetworkResponseDTO();
        
        try {
            // Validate request
            validateRequest(request);
            
            // Ensure directories exist
            createDirectories();
            
            // Handle file upload if provided
            if (trainFile != null && !trainFile.isEmpty()) {
                String fileName = request.getTrainFileName();
                if (!StringUtils.hasText(fileName)) fileName = "traindata.txt";
                Path filePath = Paths.get(uploadPath, fileName);
                trainFile.transferTo(filePath);
                // Preprocess the uploaded file to ensure correct format
                preprocessDataFile(filePath, Integer.parseInt(request.getInNodes()), Integer.parseInt(request.getOutNodes()));
            }
            
            // Prepare parameters for legacy nnet
            String[] args = {
                request.getInNodes(),
                request.getOutNodes(),
                request.getNoLayers(),
                request.getNoFirstLayer(),
                request.getNoSecLayer(),
                request.getNoTrainData(),
                request.getNoTestData(),
                "train",
                getServerPath()
            };
            
            // Call legacy logic (preserving core algorithm)
            nnet.runNeuralNetwork(args);
            
            response.setSuccess(true);
            response.setMessage("Neural network training completed successfully");
            response.setStatus("trained");
            response.setOutputPath(outputPath);
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
            response.setStatus("failed");
        }
        
        return response;
    }
    
    /**
     * Test the neural network with the given parameters
     */
    public NeuralNetworkResponseDTO testNetwork(NeuralNetworkRequestDTO request, MultipartFile testFile) {
        NeuralNetworkResponseDTO response = new NeuralNetworkResponseDTO();
        
        try {
            // Validate request
            validateRequest(request);
            
            // Ensure directories exist
            createDirectories();
            
            // Handle file upload if provided
            if (testFile != null && !testFile.isEmpty()) {
                String fileName = request.getTestFileName();
                if (!StringUtils.hasText(fileName)) fileName = "testdata.txt";
                Path filePath = Paths.get(uploadPath, fileName);
                testFile.transferTo(filePath);
                // Preprocess the uploaded file to ensure correct format
                preprocessDataFile(filePath, Integer.parseInt(request.getInNodes()), Integer.parseInt(request.getOutNodes()));
            }
            
            // Prepare parameters for legacy nnet
            String[] args = {
                request.getInNodes(),
                request.getOutNodes(),
                request.getNoLayers(),
                request.getNoFirstLayer(),
                request.getNoSecLayer(),
                request.getNoTrainData(),
                request.getNoTestData(),
                "test",
                getServerPath()
            };
            
            // Call legacy logic (preserving core algorithm)
            nnet.runNeuralNetwork(args);
            
            response.setSuccess(true);
            response.setMessage("Neural network testing completed successfully");
            response.setStatus("tested");
            response.setOutputPath(outputPath);
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
            response.setStatus("failed");
        }
        
        return response;
    }
    
    /**
     * Get neural network status and configuration
     */
    public Map<String, Object> getNetworkStatus() {
        Map<String, Object> status = new HashMap<>();
        
        try {
            // Check if resource files exist
            File trainData = new ClassPathResource("legacy-resources/sim/traindata.txt").getFile();
            File testData = new ClassPathResource("legacy-resources/sim/testdata.txt").getFile();
            
            // Check output directory
            Path outputDir = Paths.get(outputPath);
            boolean outputDirExists = Files.exists(outputDir);
            
            status.put("trainDataAvailable", trainData.exists());
            status.put("testDataAvailable", testData.exists());
            status.put("outputDirectoryExists", outputDirExists);
            status.put("neuralNetworkReady", true);
            status.put("status", "ready");
            status.put("uploadPath", uploadPath);
            status.put("outputPath", outputPath);
            
        } catch (IOException e) {
            status.put("trainDataAvailable", false);
            status.put("testDataAvailable", false);
            status.put("outputDirectoryExists", false);
            status.put("neuralNetworkReady", false);
            status.put("status", "error");
            status.put("error", e.getMessage());
        }
        
        return status;
    }
    
    /**
     * Validate neural network request
     */
    private void validateRequest(NeuralNetworkRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        if (!StringUtils.hasText(request.getInNodes())) {
            throw new IllegalArgumentException("Input nodes cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getOutNodes())) {
            throw new IllegalArgumentException("Output nodes cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getNoLayers())) {
            throw new IllegalArgumentException("Number of layers cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getNoFirstLayer())) {
            throw new IllegalArgumentException("First layer nodes cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getNoSecLayer())) {
            throw new IllegalArgumentException("Second layer nodes cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getNoTrainData())) {
            throw new IllegalArgumentException("Number of training data cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getNoTestData())) {
            throw new IllegalArgumentException("Number of test data cannot be empty");
        }
    }
    
    /**
     * Create necessary directories
     */
    private void createDirectories() throws IOException {
        Path uploadDir = Paths.get(uploadPath);
        Path outputDir = Paths.get(outputPath);
        
        Files.createDirectories(uploadDir);
        Files.createDirectories(outputDir);
    }
    
    /**
     * Get server path for legacy compatibility
     */
    private String getServerPath() {
        return Paths.get(outputPath).getParent().toString() + "/";
    }
    
    /**
     * Preprocess uploaded data file to ensure it has the correct header and output columns
     */
    private void preprocessDataFile(Path filePath, int numInputs, int numOutputs) throws IOException {
        java.util.List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty()) return;
        boolean hasHeader = false;
        String firstLine = lines.get(0).trim();
        String[] headerParts = firstLine.split("\\s+");
        if (headerParts.length == 2) {
            try {
                Integer.parseInt(headerParts[0]);
                Integer.parseInt(headerParts[1]);
                hasHeader = true;
            } catch (NumberFormatException ignored) {}
        }
        java.util.List<String> newLines = new java.util.ArrayList<>();
        if (!hasHeader) {
            newLines.add(numInputs + " " + numOutputs);
        } else {
            newLines.add(firstLine);
            lines = lines.subList(1, lines.size());
        }
        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0 || line.trim().isEmpty()) continue;
            java.util.List<String> row = new java.util.ArrayList<>(java.util.Arrays.asList(parts));
            // If missing outputs, append default 0s
            while (row.size() < numInputs + numOutputs) {
                row.add("0");
            }
            // If too many columns, trim
            if (row.size() > numInputs + numOutputs) {
                row = row.subList(0, numInputs + numOutputs);
            }
            newLines.add(String.join(" ", row));
        }
        Files.write(filePath, newLines);
    }
} 