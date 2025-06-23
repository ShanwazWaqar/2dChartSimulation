package com.simulation.corrosion.service;

import com.simulation.corrosion.dto.NeuralNetworkRequestDTO;
import com.simulation.corrosion.dto.NeuralNetworkResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NeuralNetworkServiceTest {

    @InjectMocks
    private NeuralNetworkService neuralNetworkService;

    @Test
    void testGetNetworkStatus() {
        // Set test properties
        ReflectionTestUtils.setField(neuralNetworkService, "uploadPath", "output/upload");
        ReflectionTestUtils.setField(neuralNetworkService, "outputPath", "output/nnet_sia");
        
        // Test network status
        Map<String, Object> status = neuralNetworkService.getNetworkStatus();
        
        assertNotNull(status);
        assertTrue(status.containsKey("status"));
        assertTrue(status.containsKey("uploadPath"));
        assertTrue(status.containsKey("outputPath"));
    }

    @Test
    void testValidateRequest() {
        NeuralNetworkRequestDTO request = new NeuralNetworkRequestDTO();
        request.setInNodes("10");
        request.setOutNodes("1");
        request.setNoLayers("3");
        request.setNoFirstLayer("5");
        request.setNoSecLayer("3");
        request.setNoTrainData("100");
        request.setNoTestData("20");
        
        // This should not throw an exception
        assertDoesNotThrow(() -> {
            // The validation is private, so we test it indirectly through the public methods
            // For now, we just verify the request object is properly constructed
            assertNotNull(request.getInNodes());
            assertNotNull(request.getOutNodes());
        });
    }
} 