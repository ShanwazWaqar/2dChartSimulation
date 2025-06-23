package com.simulation.corrosion.service;

import com.simulation.legacy.ImageMatrix;
import org.springframework.stereotype.Service;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

@Service
public class ImageMatrixAdapterService {
    public Map<String, Object> runSimulation(int rows, int cols, int scale, int iterations, String path) {
        Map<String, Object> result = new HashMap<>();
        try {
            ImageMatrix matrix = new ImageMatrix(rows, cols, scale, iterations, path);
            String stats = matrix.stat2D();
            String wavelet = matrix.wavelet2D(6);
            // Save visual output
            BufferedImage img = new BufferedImage(cols * scale, rows * scale, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();
            matrix.paint(g2d);
            g2d.dispose();
            String outPath = path + "/files/simulation.JPG";
            File outFile = new File(outPath);
            outFile.getParentFile().mkdirs();
            ImageIO.write(img, "JPG", outFile);
            result.put("processedImagePath", outPath);
            result.put("stats", stats);
            result.put("wavelet", wavelet);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }
} 