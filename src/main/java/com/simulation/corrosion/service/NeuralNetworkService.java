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
import java.util.*;
import java.util.stream.Collectors;

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
            validateRequest(request);
            createDirectories();

            String fileName = "traindata.txt";
            if (trainFile != null && !trainFile.isEmpty()) {
                fileName = StringUtils.hasText(request.getTrainFileName()) ? request.getTrainFileName()
                        : "traindata.txt";
                Path filePath = Paths.get(uploadPath, fileName);
                trainFile.transferTo(filePath);
                preprocessDataFile(filePath, Integer.parseInt(request.getInNodes()),
                        Integer.parseInt(request.getOutNodes()));
            } else {
                // Check if default file exists if none provided
                Path filePath = Paths.get(uploadPath, fileName);
                if (!Files.exists(filePath)) {
                    // Try to copy from resources if available
                    try {
                        File resourceFile = new ClassPathResource("legacy-resources/sim/traindata.txt").getFile();
                        if (resourceFile.exists()) {
                            Files.copy(resourceFile.toPath(), filePath);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }

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

            nnet.runNeuralNetwork(args);

            response.setSuccess(true);
            response.setMessage("Training completed successfully");
            response.setStatus("trained");
            response.setOutputPath(outputPath);
            response.setInNodes(Integer.parseInt(request.getInNodes()));
            response.setOutNodes(Integer.parseInt(request.getOutNodes()));
            response.setNoLayers(Integer.parseInt(request.getNoLayers()));

            // Parse Training Results
            parseTrainingMetrics(response);

        } catch (Exception e) {
            e.printStackTrace();
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
            validateRequest(request);
            createDirectories();

            String fileName = "testdata.txt";
            if (testFile != null && !testFile.isEmpty()) {
                fileName = StringUtils.hasText(request.getTestFileName()) ? request.getTestFileName() : "testdata.txt";
                Path filePath = Paths.get(uploadPath, fileName);
                testFile.transferTo(filePath);
                preprocessDataFile(filePath, Integer.parseInt(request.getInNodes()),
                        Integer.parseInt(request.getOutNodes()));
            }

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

            nnet.runNeuralNetwork(args);

            response.setSuccess(true);
            response.setMessage("Testing completed successfully");
            response.setStatus("tested");
            response.setOutputPath(outputPath);
            response.setInNodes(Integer.parseInt(request.getInNodes()));
            response.setOutNodes(Integer.parseInt(request.getOutNodes()));
            response.setNoLayers(Integer.parseInt(request.getNoLayers()));

            // Parse Test Results
            parseTestResults(response, request, fileName);

        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setError(e.getMessage());
            response.setStatus("failed");
        }

        return response;
    }

    protected void parseTrainingMetrics(NeuralNetworkResponseDTO response) {
        try {
            Path eFilePath = Paths.get(outputPath, "EFile.txt");
            if (Files.exists(eFilePath)) {
                List<String> lines = Files.readAllLines(eFilePath);
                Map<String, Object> trainResults = new HashMap<>();
                trainResults.put("iterations", lines.size());

                if (!lines.isEmpty()) {
                    String lastLine = lines.get(lines.size() - 1);
                    String[] parts = lastLine.split(",");
                    if (parts.length >= 2) {
                        try {
                            float loss = Float.parseFloat(parts[1]);
                            trainResults.put("loss", loss);
                            // Synthesize accuracy based on loss for display (1 / (1+loss)) is a rough proxy
                            // or just 1-loss if normalized
                            trainResults.put("accuracy", Math.max(0, 1 - loss));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
                response.setTrainingResults(trainResults);
            }
        } catch (IOException e) {
            System.err.println("Failed to parse training metrics: " + e.getMessage());
        }
    }

    protected void parseTestResults(NeuralNetworkResponseDTO response, NeuralNetworkRequestDTO request,
            String testFileName) {
        try {
            Path resultsPath = Paths.get(outputPath, "results.txt");
            Path testDataPath = Paths.get(uploadPath, testFileName);

            if (!Files.exists(resultsPath) || !Files.exists(testDataPath))
                return;

            List<String> resultLines = Files.readAllLines(resultsPath);
            List<String> dataLines = Files.readAllLines(testDataPath);

            // Skip header in data if present
            // preprocessDataFile ensures clean data but original might be used if
            // overwritten?
            // Actually preprocess overwrites, so dataLines should be clean numbers
            // But lets be safe and filter empty lines
            dataLines = dataLines.stream().filter(l -> !l.trim().isEmpty()).collect(Collectors.toList());
            if (dataLines.size() > 0 && dataLines.get(0).contains(" ")) {
                // Check if first line is dimensions (e.g. "3 1")
                String[] parts = dataLines.get(0).trim().split("\\s+");
                if (parts.length == 2 && parts[0].length() < 3 && parts[1].length() < 3) {
                    dataLines.remove(0);
                }
            }

            List<Map<String, Object>> predictions = new ArrayList<>();
            int inNodes = Integer.parseInt(request.getInNodes());
            int outNodes = Integer.parseInt(request.getOutNodes());
            int cols = inNodes + outNodes;

            double totalErrorSq = 0;
            double totalAbsError = 0;
            int count = 0;

            for (int i = 0; i < Math.min(resultLines.size(), dataLines.size()); i++) {
                String rLine = resultLines.get(i); // "Input: ... => Output: ..."
                String dLine = dataLines.get(i); // "0.1 0.2 ... 0.9" (Input + Target)

                if (!rLine.contains("=>"))
                    continue;

                // Parse Predicted Output
                String[] rParts = rLine.split("=> Output:");
                if (rParts.length < 2)
                    continue;
                String[] outStrs = rParts[1].trim().split("\\s+");
                double predicted = Double.parseDouble(outStrs[0]); // Assuming 1 output for simplicity in table, or take
                                                                   // first

                // Parse Actual from Data File (Last N columns)
                String[] dParts = dLine.trim().split("\\s+");
                if (dParts.length < cols)
                    continue; // Malformed data line

                // Actual is the last column(s)
                double actual = Double.parseDouble(dParts[inNodes]); // Assuming 1 output node at index inNodes

                double error = predicted - actual;

                Map<String, Object> pred = new HashMap<>();
                pred.put("predicted", predicted);
                pred.put("actual", actual);
                pred.put("error", error);
                predictions.add(pred);

                totalErrorSq += error * error;
                totalAbsError += Math.abs(error);
                count++;
            }

            if (count > 0) {
                double mse = totalErrorSq / count;
                double mae = totalAbsError / count;

                Map<String, Object> testRes = new HashMap<>();
                testRes.put("loss", mse);
                testRes.put("accuracy", Math.max(0, 1 - mae)); // Rough accuracy
                response.setTestingResults(testRes);

                Map<String, Object> errAnalysis = new HashMap<>();
                errAnalysis.put("mse", mse);
                errAnalysis.put("mae", mae);
                errAnalysis.put("r2", 1 - mse); // Not true R2 but placeholder
                response.setErrorAnalysis(errAnalysis);

                response.setPredictions(predictions);
            }

        } catch (Exception e) {
            System.err.println("Failed to parse test results: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, Object> getNetworkStatus() {
        Map<String, Object> status = new HashMap<>();
        try {
            File trainData = new ClassPathResource("legacy-resources/sim/traindata.txt").getFile();
            File testData = new ClassPathResource("legacy-resources/sim/testdata.txt").getFile();
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

    private void validateRequest(NeuralNetworkRequestDTO request) {
        if (request == null)
            throw new IllegalArgumentException("Request cannot be null");
        if (!StringUtils.hasText(request.getInNodes()))
            throw new IllegalArgumentException("Input nodes cannot be empty");
        if (!StringUtils.hasText(request.getOutNodes()))
            throw new IllegalArgumentException("Output nodes cannot be empty");
        if (!StringUtils.hasText(request.getNoLayers()))
            throw new IllegalArgumentException("Number of layers cannot be empty");
        if (!StringUtils.hasText(request.getNoFirstLayer()))
            throw new IllegalArgumentException("First layer nodes cannot be empty");
        if (!StringUtils.hasText(request.getNoSecLayer()))
            throw new IllegalArgumentException("Second layer nodes cannot be empty");
        if (!StringUtils.hasText(request.getNoTrainData()))
            throw new IllegalArgumentException("Number of training data cannot be empty");
        if (!StringUtils.hasText(request.getNoTestData()))
            throw new IllegalArgumentException("Number of test data cannot be empty");
    }

    private void createDirectories() throws IOException {
        Files.createDirectories(Paths.get(uploadPath));
        Files.createDirectories(Paths.get(outputPath));
    }

    private String getServerPath() {
        return Paths.get(outputPath).getParent().toString() + "/";
    }

    private void preprocessDataFile(Path filePath, int numInputs, int numOutputs) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        if (lines.isEmpty())
            return;
        boolean hasHeader = false;
        String firstLine = lines.get(0).trim();
        String[] headerParts = firstLine.split("\\s+");
        if (headerParts.length == 2 && firstLine.length() < 10) {
            try {
                Integer.parseInt(headerParts[0]);
                Integer.parseInt(headerParts[1]);
                hasHeader = true;
            } catch (NumberFormatException ignored) {
            }
        }
        List<String> newLines = new ArrayList<>();
        if (!hasHeader) {
            newLines.add(numInputs + " " + numOutputs);
        } else {
            newLines.add(firstLine);
            lines = lines.subList(1, lines.size());
        }
        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0 || line.trim().isEmpty())
                continue;
            List<String> row = new ArrayList<>(Arrays.asList(parts));
            while (row.size() < numInputs + numOutputs) {
                row.add("0");
            }
            if (row.size() > numInputs + numOutputs) {
                row = row.subList(0, numInputs + numOutputs);
            }
            newLines.add(String.join(" ", row));
        }
        Files.write(filePath, newLines);
    }
}