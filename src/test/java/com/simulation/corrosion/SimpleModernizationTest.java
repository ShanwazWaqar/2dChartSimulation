package com.simulation.corrosion;

import com.simulation.corrosion.dto.NeuralNetworkRequestDTO;
import com.simulation.corrosion.dto.NeuralNetworkResponseDTO;
import com.simulation.corrosion.dto.CorrosionAnalysisRequestDTO;
import com.simulation.corrosion.dto.CorrosionAnalysisResponseDTO;
import com.simulation.corrosion.dto.ImageAnalysisRequestDTO;
import com.simulation.corrosion.dto.ImageAnalysisResponseDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple test to verify the modernization works
 */
class SimpleModernizationTest {

    @Test
    void testNeuralNetworkRequestDTO() {
        NeuralNetworkRequestDTO request = new NeuralNetworkRequestDTO();
        request.setInNodes("10");
        request.setOutNodes("1");
        request.setNoLayers("3");
        request.setNoFirstLayer("5");
        request.setNoSecLayer("3");
        request.setNoTrainData("100");
        request.setNoTestData("20");
        
        assertEquals("10", request.getInNodes());
        assertEquals("1", request.getOutNodes());
        assertEquals("3", request.getNoLayers());
        assertEquals("5", request.getNoFirstLayer());
        assertEquals("3", request.getNoSecLayer());
        assertEquals("100", request.getNoTrainData());
        assertEquals("20", request.getNoTestData());
    }

    @Test
    void testNeuralNetworkResponseDTO() {
        NeuralNetworkResponseDTO response = new NeuralNetworkResponseDTO(true, "Success", "completed");
        
        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
        assertEquals("completed", response.getStatus());
    }

    @Test
    void testCorrosionAnalysisRequestDTO() {
        CorrosionAnalysisRequestDTO request = new CorrosionAnalysisRequestDTO();
        request.setAtomMass("55.85");
        request.setValence("2");
        request.setFarCnst("96485");
        request.setDensity("7.87");
        request.setTemp("298");
        request.setFcoeffsmall("1.0E-10");
        request.setFcoefflong("1.0E-10");
        request.setFexpsmall("3.0");
        request.setFexplong("3.0");
        request.setSigma("200");
        
        assertEquals("55.85", request.getAtomMass());
        assertEquals("2", request.getValence());
        assertEquals("96485", request.getFarCnst());
        assertEquals("7.87", request.getDensity());
        assertEquals("298", request.getTemp());
        assertEquals("1.0E-10", request.getFcoeffsmall());
        assertEquals("1.0E-10", request.getFcoefflong());
        assertEquals("3.0", request.getFexpsmall());
        assertEquals("3.0", request.getFexplong());
        assertEquals("200", request.getSigma());
    }

    @Test
    void testCorrosionAnalysisResponseDTO() {
        CorrosionAnalysisResponseDTO response = new CorrosionAnalysisResponseDTO(true, "Analysis completed");
        
        assertTrue(response.isSuccess());
        assertEquals("Analysis completed", response.getMessage());
    }

    @Test
    void testImageAnalysisRequestDTO() {
        ImageAnalysisRequestDTO request = new ImageAnalysisRequestDTO();
        request.setThreshold(128);
        request.setAnalysisType("corrosion");
        request.setEnable2D(false);
        request.setOutputFormat("json");
        
        assertEquals(128, request.getThreshold());
        assertEquals("corrosion", request.getAnalysisType());
        assertFalse(request.getEnable2D());
        assertEquals("json", request.getOutputFormat());
    }

    @Test
    void testImageAnalysisResponseDTO() {
        ImageAnalysisResponseDTO response = new ImageAnalysisResponseDTO(true, "Image analysis completed");
        
        assertTrue(response.isSuccess());
        assertEquals("Image analysis completed", response.getMessage());
    }

    @Test
    void testLegacyClassesStillExist() {
        // Test that legacy classes are still accessible
        assertDoesNotThrow(() -> {
            // These should still exist and be accessible
            Class.forName("com.simulation.legacy.sim.nnet");
            Class.forName("com.simulation.legacy.sim.CorrosionBean");
            Class.forName("com.simulation.legacy.nsfisdas.BP");
        });
    }
} 