package com.simulation.corrosion.service;

import com.simulation.corrosion.dto.Simulation2DRequestDTO;
import com.simulation.corrosion.dto.Simulation2DResponseDTO;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;

@Service
public class Simulation2DService {
    public Simulation2DResponseDTO runSimulation(Simulation2DRequestDTO request) {
        int rows = request.getNoRows();
        int cols = request.getNoColumns();
        double p1 = request.getParam1();
        double p2 = request.getParam2();
        double p3 = request.getParam3();
        double p4 = request.getParam4();
        double constant = request.getConstant();
        int stressFact = request.getStressFact();
        int iterations = request.getNoIterations();
        String relativePath = request.getPath(); // e.g., "output/sim2d/"

        Simulation2DResponseDTO response = new Simulation2DResponseDTO();
        try {
            // Get the application's root directory
            String rootDir = System.getProperty("user.dir");
            // Resolve the full output path
            Path outputDir = Paths.get(rootDir, relativePath);
            Files.createDirectories(outputDir);
            // Example: Save your image here (replace with real logic)
            String resultImageName = "result.png";
            Path resultImagePath = outputDir.resolve(resultImageName);
            // ImageIO.write(bufferedImage, "png", resultImagePath.toFile());

            // Example stats (replace with real simulation results)
            Map<String, Object> stats = new HashMap<>();
            stats.put("corrosionPercentage", 12.5);
            stats.put("maxDepth", 0.42);

            response.setSuccess(true);
            response.setMessage("Simulation completed successfully.");
            // Return the processed image path relative to the server root
            response.setProcessedImagePath("/" + relativePath.replace("\\", "/") + resultImageName);
            response.setStats(stats);
            response.setPitSize(0.123);
            response.setPitGrowth(0.045);
        } catch (IOException e) {
            response.setSuccess(false);
            response.setMessage("Failed to create output directory or save result: " + e.getMessage());
        }
        return response;
    }
} 