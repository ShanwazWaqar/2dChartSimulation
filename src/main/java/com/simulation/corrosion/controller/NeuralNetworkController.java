package com.simulation.corrosion.controller;

import com.simulation.corrosion.dto.NeuralNetworkRequestDTO;
import com.simulation.corrosion.dto.NeuralNetworkResponseDTO;
import com.simulation.corrosion.service.NeuralNetworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Modern REST controller for neural network operations
 * Replaces legacy servlet with Spring Boot REST endpoints
 */
@RestController
@RequestMapping("/api/neural-network")
@Tag(name = "Neural Network", description = "Neural network training and testing operations")
public class NeuralNetworkController {

    private final NeuralNetworkService neuralNetworkService;

    public NeuralNetworkController(@Qualifier("modernNeuralNetworkService") NeuralNetworkService neuralNetworkService) {
        this.neuralNetworkService = neuralNetworkService;
    }

    @PostMapping(value = "/train", consumes = "multipart/form-data")
    @Operation(summary = "Train neural network", description = "Train the neural network with provided parameters")
    public ResponseEntity<NeuralNetworkResponseDTO> trainNetwork(
            @RequestParam("inNodes") String inNodes,
            @RequestParam("outNodes") String outNodes,
            @RequestParam("noLayers") String noLayers,
            @RequestParam("noFirstLayer") String noFirstLayer,
            @RequestParam("noSecLayer") String noSecLayer,
            @RequestParam("noTrainData") String noTrainData,
            @RequestParam("noTestData") String noTestData,
            @RequestParam("serverPath") String serverPath,
            @RequestParam("trainFileName") String trainFileName,
            @RequestParam(value = "trainFile", required = false) MultipartFile trainFile) {
        try {
            NeuralNetworkRequestDTO request = new NeuralNetworkRequestDTO();
            request.setInNodes(inNodes);
            request.setOutNodes(outNodes);
            request.setNoLayers(noLayers);
            request.setNoFirstLayer(noFirstLayer);
            request.setNoSecLayer(noSecLayer);
            request.setNoTrainData(noTrainData);
            request.setNoTestData(noTestData);
            request.setServerPath(serverPath);
            request.setTrainFileName(trainFileName);
            NeuralNetworkResponseDTO response = neuralNetworkService.trainNetwork(request, trainFile);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            NeuralNetworkResponseDTO errorResponse = new NeuralNetworkResponseDTO(
                e.getMessage(), false, "failed"
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping(value = "/test", consumes = "multipart/form-data")
    @Operation(summary = "Test neural network", description = "Test the neural network with provided parameters")
    public ResponseEntity<NeuralNetworkResponseDTO> testNetwork(
            @RequestParam("inNodes") String inNodes,
            @RequestParam("outNodes") String outNodes,
            @RequestParam("noLayers") String noLayers,
            @RequestParam("noFirstLayer") String noFirstLayer,
            @RequestParam("noSecLayer") String noSecLayer,
            @RequestParam("noTrainData") String noTrainData,
            @RequestParam("noTestData") String noTestData,
            @RequestParam("serverPath") String serverPath,
            @RequestParam("testFileName") String testFileName,
            @RequestParam(value = "testFile", required = false) MultipartFile testFile) {
        try {
            NeuralNetworkRequestDTO request = new NeuralNetworkRequestDTO();
            request.setInNodes(inNodes);
            request.setOutNodes(outNodes);
            request.setNoLayers(noLayers);
            request.setNoFirstLayer(noFirstLayer);
            request.setNoSecLayer(noSecLayer);
            request.setNoTrainData(noTrainData);
            request.setNoTestData(noTestData);
            request.setServerPath(serverPath);
            request.setTestFileName(testFileName);
            NeuralNetworkResponseDTO response = neuralNetworkService.testNetwork(request, testFile);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            NeuralNetworkResponseDTO errorResponse = new NeuralNetworkResponseDTO(
                e.getMessage(), false, "failed"
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Get network status", description = "Get the current status of the neural network")
    public ResponseEntity<Map<String, Object>> getNetworkStatus() {
        Map<String, Object> status = neuralNetworkService.getNetworkStatus();
        return ResponseEntity.ok(status);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the neural network service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Neural Network Service is running");
    }

    /**
     * Handle file upload for training or testing data
     */
    private void handleFileUpload(MultipartFile file, String fileName) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadDir = Paths.get("output/upload");
        Files.createDirectories(uploadDir);
        
        // Save the file
        Path filePath = uploadDir.resolve(fileName);
        file.transferTo(filePath.toFile());
    }
} 