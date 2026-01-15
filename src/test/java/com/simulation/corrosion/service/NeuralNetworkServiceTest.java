package com.simulation.corrosion.service;

import com.simulation.corrosion.dto.NeuralNetworkRequestDTO;
import com.simulation.corrosion.dto.NeuralNetworkResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NeuralNetworkServiceTest {

    @InjectMocks
    private NeuralNetworkService neuralNetworkService;

    @TempDir
    Path tempDir;

    private Path uploadDir;
    private Path outputDir;

    @BeforeEach
    void setUp() throws Exception {
        uploadDir = tempDir.resolve("upload");
        outputDir = tempDir.resolve("nnet_sia");
        Files.createDirectories(uploadDir);
        Files.createDirectories(outputDir);

        ReflectionTestUtils.setField(neuralNetworkService, "uploadPath", uploadDir.toString());
        ReflectionTestUtils.setField(neuralNetworkService, "outputPath", outputDir.toString());
    }

    @Test
    void testGetNetworkStatus() {
        Map<String, Object> status = neuralNetworkService.getNetworkStatus();
        assertNotNull(status);
        assertTrue(status.containsKey("status"));
        assertTrue(status.containsKey("uploadPath"));
        assertTrue(status.containsKey("outputPath"));
    }

    @Test
    void testParseTrainingMetrics() throws Exception {
        // Create a dummy EFile.txt
        Path eFile = outputDir.resolve("EFile.txt");
        List<String> lines = Arrays.asList(
                "1,0.500",
                "2,0.400",
                "3,0.1234");
        Files.write(eFile, lines);

        NeuralNetworkResponseDTO response = new NeuralNetworkResponseDTO();
        neuralNetworkService.parseTrainingMetrics(response);

        assertNotNull(response.getTrainingResults());
        assertEquals(3, response.getTrainingResults().get("iterations"));
        assertEquals(0.1234f, response.getTrainingResults().get("loss"));
    }

    @Test
    void testParseTestResults() throws Exception {
        // Create dummy results.txt and testdata.txt
        Path resultsFile = outputDir.resolve("results.txt");
        // Format: Input: ... => Output: ...
        List<String> resultLines = Arrays.asList(
                "Input: 0.1 \t => Output: 0.85 ",
                "Input: 0.2 \t => Output: 0.95 ");
        Files.write(resultsFile, resultLines);

        Path testDataFile = uploadDir.resolve("testdata.txt");
        // Format: Input Target (assuming 1 in, 1 out for simplicity in this test)
        // Service expects space separated
        List<String> dataLines = Arrays.asList(
                "0.1 0.80",
                "0.2 0.90");
        Files.write(testDataFile, dataLines);

        NeuralNetworkRequestDTO request = new NeuralNetworkRequestDTO();
        request.setInNodes("1");
        request.setOutNodes("1");

        NeuralNetworkResponseDTO response = new NeuralNetworkResponseDTO();

        neuralNetworkService.parseTestResults(response, request, "testdata.txt");

        assertNotNull(response.getTestingResults());
        assertNotNull(response.getPredictions());
        assertEquals(2, response.getPredictions().size());

        // Check first prediction error: 0.85 - 0.80 = 0.05
        Map<String, Object> pred1 = response.getPredictions().get(0);
        assertEquals(0.85, (Double) pred1.get("predicted"), 0.001);
        assertEquals(0.80, (Double) pred1.get("actual"), 0.001);
        assertEquals(0.05, (Double) pred1.get("error"), 0.001);
    }
}