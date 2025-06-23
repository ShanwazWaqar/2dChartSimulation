package com.simulation.legacy.sim;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Spring Boot REST controller for neural network training and testing
 * Converted from legacy servlet to modern Spring Boot controller
 */
@RestController
@RequestMapping("/api/sia")
public class SiaServlet {

	@PostMapping("/process")
	public ResponseEntity<Map<String, Object>> processNeuralNetwork(
			@RequestParam("action") String action,
			@RequestParam("inNodes") String inNodes,
			@RequestParam("outNodes") String outNodes,
			@RequestParam("noLayers") String noLayers,
			@RequestParam("noFirstLayer") String noFirstLayer,
			@RequestParam("noSecLayer") String noSecLayer,
			@RequestParam("notraindata") String noTrainData,
			@RequestParam("notestdata") String noTestData,
			@RequestParam("radionet") String radioNet,
			@RequestParam("serverpath") String serverPath,
			@RequestParam(value = "trainfile", required = false) MultipartFile trainFile,
			@RequestParam(value = "testfile", required = false) MultipartFile testFile
	) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (!action.equals("Submit")) {
				response.put("success", false);
				response.put("error", "Invalid action: " + action);
				return ResponseEntity.badRequest().body(response);
			}

			// Normalize and create upload directory
			Path uploadDir = Paths.get(serverPath, "upload").toAbsolutePath().normalize();
			Files.createDirectories(uploadDir);

			if (radioNet.equals("train") && trainFile != null && !trainFile.isEmpty()) {
				Path trainPath = uploadDir.resolve("traindata.txt");
				trainFile.transferTo(trainPath);
			} else if (radioNet.equals("test") && testFile != null && !testFile.isEmpty()) {
				Path testPath = uploadDir.resolve("testdata.txt");
				testFile.transferTo(testPath);
			}

			// Call legacy logic
			nnet.run(
					inNodes, outNodes, noLayers, noFirstLayer, noSecLayer,
					noTrainData, noTestData, radioNet, serverPath
			);

			response.put("success", true);
			response.put("status", radioNet.equals("train") ? "trained" : "tested");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("error", e.getMessage());
			response.put("status", "failed");
			return ResponseEntity.internalServerError().body(response);
		}
	}

	@GetMapping("/health")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("SiaServlet is running");
	}

	@GetMapping("/status")
	public ResponseEntity<Map<String, Object>> getStatus() {
		Map<String, Object> status = new HashMap<>();
		try {
			// Check if resource files exist
			File trainData = new File("src/main/resources/legacy-resources/sim/traindata.txt");
			File testData = new File("src/main/resources/legacy-resources/sim/testdata.txt");
			
			status.put("trainDataAvailable", trainData.exists());
			status.put("testDataAvailable", testData.exists());
			status.put("neuralNetworkReady", true);
			status.put("status", "ready");
			
		} catch (Exception e) {
			status.put("trainDataAvailable", false);
			status.put("testDataAvailable", false);
			status.put("neuralNetworkReady", false);
			status.put("status", "error");
			status.put("error", e.getMessage());
		}
		return ResponseEntity.ok(status);
	}
}
