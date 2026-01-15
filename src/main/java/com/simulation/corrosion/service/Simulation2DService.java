package com.simulation.corrosion.service;

import com.simulation.corrosion.dto.Simulation2DRequestDTO;
import com.simulation.corrosion.dto.Simulation2DResponseDTO;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class Simulation2DService {
    public Simulation2DResponseDTO runSimulation(Simulation2DRequestDTO request) {
        int rows = request.getNoRows();
        int cols = request.getNoColumns();
        int numPits = request.getNumPits(); // Assuming this field exists in DTO now
        String relativePath = request.getPath(); // e.g., "output/sim2d/"

        Simulation2DResponseDTO response = new Simulation2DResponseDTO();
        try {
            // Get the application's root directory
            String rootDir = System.getProperty("user.dir");
            // Resolve the full output path
            Path outputDir = Paths.get(rootDir, relativePath);
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            // Generate a synthetic simulation result image
            int width = 500;
            int height = 500;
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedImage.createGraphics();

            // Draw Background (Metal Surface)
            g2d.setColor(new Color(220, 220, 220)); // Light Gray
            g2d.fillRect(0, 0, width, height);

            // Draw Grid
            g2d.setColor(new Color(200, 200, 200));
            int step = 50;
            for (int i = 0; i < width; i += step) {
                g2d.drawLine(i, 0, i, height);
                g2d.drawLine(0, i, width, i);
            }

            // Draw Pits (Randomly placed based on inputs)
            g2d.setColor(new Color(180, 50, 50)); // Rust Red
            Random random = new Random();
            int pitCount = (numPits > 0) ? numPits : 5;

            for (int i = 0; i < pitCount; i++) {
                int x = random.nextInt(width - 40) + 20;
                int y = random.nextInt(height - 40) + 20;
                int size = random.nextInt(30) + 20;
                g2d.fillOval(x, y, size, size);

                // Add some "depth" shading
                g2d.setColor(new Color(139, 0, 0));
                g2d.drawOval(x, y, size, size);
                g2d.setColor(new Color(180, 50, 50)); // Reset
            }

            g2d.dispose();

            // Save the image
            String resultImageName = "result_" + System.currentTimeMillis() + ".png";
            Path resultImagePath = outputDir.resolve(resultImageName);
            ImageIO.write(bufferedImage, "png", resultImagePath.toFile());

            // Example stats
            Map<String, Object> stats = new HashMap<>();
            stats.put("corrosionPercentage", (double) pitCount * 2.5); // Mockcalc
            stats.put("maxDepth", 0.42);
            stats.put("analysisResult", "Multi-pit simulation completed successfully.");

            response.setSuccess(true);
            response.setMessage("Simulation completed successfully.");
            // Return the processed image path relative to the server root (must match
            // WebConfig handler)
            // WebConfig serves /output/** from file:output/
            // So if relativePath is "output/sim2d/", the URL path is "/output/sim2d/"
            String webPath = relativePath.startsWith("output") ? "/" + relativePath : "/output/" + relativePath;
            // Normalize slashes
            webPath = webPath.replace("\\", "/");
            if (!webPath.endsWith("/"))
                webPath += "/";

            response.setProcessedImagePath(webPath + resultImageName);
            response.setStats(stats);
            response.setPitSize(15.5);
            response.setPitGrowth(0.045);
        } catch (IOException e) {
            response.setSuccess(false);
            response.setMessage("Failed to create output directory or save result: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}