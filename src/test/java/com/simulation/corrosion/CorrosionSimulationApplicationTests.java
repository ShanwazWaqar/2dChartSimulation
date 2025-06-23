package com.simulation.corrosion;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "app.upload.path=test-output/upload",
    "app.output.path=test-output/nnet_sia",
    "app.images.path=test-output/images"
})
class CorrosionSimulationApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
        // with all the modernized components
    }
} 