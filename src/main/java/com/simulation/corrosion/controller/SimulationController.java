package com.simulation.corrosion.controller;

import com.simulation.corrosion.dto.SimulationRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/simulation")
public class SimulationController {

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("simulation", new SimulationRequestDTO());
        return "simulate"; // simulate.html page
    }

    @PostMapping
    public String runSimulation(@ModelAttribute SimulationRequestDTO simulation, Model model) {
        // For now, just echo the inputs to test
        model.addAttribute("result", simulation);
        return "result"; // result.html page
    }
}

@RestController
@RequestMapping("/api")
class SimulationRestController {
    @PostMapping("/simulate")
    public ResponseEntity<?> runSimulationApi(@RequestBody SimulationRequestDTO request) {
        try {
            // TODO: Replace with real simulation logic/service call
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Simulation completed successfully");
            result.put("input", request);

            // Mock data for frontend visualization
            result.put("corrosionDetected", true);
            result.put("corrosionPercentage", 45.5);
            result.put("analysisResult", "Severe pitting detected in quadrant 3");
            result.put("imagePath", ""); // Placeholder or empty for now
            result.put("processedImagePath", ""); // Placeholder or empty for now

            // Add more result fields as needed
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
