package com.simulation.corrosion.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.simulation.legacy.ImageMatrix2D;

@Service
public class ImageMatrix2DAdapterService {
    public Map<String, Object> runSimulation(int rows, int cols, int scale, int iterations, String path, double input1, double input2, double input3, double input4) {
        Map<String, Object> result = new HashMap<>();
        try {
            // Use the legacy ImageMatrix2D class (assumed available in classpath)
            ImageMatrix2D matrix = new ImageMatrix2D(rows, cols, scale, iterations, path, input1, input2, input3, input4);
            // Optionally, run simulation logic here if needed
            // For demonstration, just return the output image path and some stats
            result.put("processedImagePath", path + "/files2D/simulation1.JPG");
            result.put("maxRedHeight", matrix.getMaxRedHt());
            result.put("maxGreenHeight", matrix.getmaxGreenHt());
            result.put("maxBlueHeight", matrix.getmaxBlueHt());
            result.put("widthCnt", matrix.getWidthCnt());
            result.put("heightCnt", matrix.getHeightCnt());
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }
} 