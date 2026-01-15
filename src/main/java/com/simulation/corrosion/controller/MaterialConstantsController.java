package com.simulation.corrosion.controller;

import com.simulation.corrosion.dto.MaterialConstantsRequest;
import com.simulation.corrosion.dto.MaterialConstantsResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/material-constants")
public class MaterialConstantsController {

    @PostMapping
    public MaterialConstantsResponse processMaterialConstants(@RequestBody MaterialConstantsRequest request) {
        MaterialConstantsResponse response = new MaterialConstantsResponse();

        try {
            // TODO: Implement actual corrosion fatigue life prediction calculations
            // This is a placeholder that demonstrates the endpoint works

            double pitCoeff = request.getPitCoeff();
            double freq = request.getFreq();
            double sigma = request.getSigma();
            double apcFinal = request.getApc_final();
            double afFinal = request.getAf_final();
            double scoeff = request.getScoeff();

            // Placeholder calculation - replace with actual model
            double fatigueLife = calculateFatigueLife(pitCoeff, freq, sigma, apcFinal, afFinal, scoeff);

            Map<String, Object> results = new HashMap<>();
            results.put("predictedLifeCycles", fatigueLife);
            results.put("crackGrowthRate", (afFinal - apcFinal) / fatigueLife);
            results.put("timeToFailure", fatigueLife / (freq * 3600)); // Convert to hours

            response.setSuccess(true);
            response.setMessage("Material constants processed successfully.");
            response.setResults(results);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error processing material constants: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    private double calculateFatigueLife(double pitCoeff, double freq, double sigma,
            double apcFinal, double afFinal, double scoeff) {
        // Placeholder calculation - this should be replaced with the actual
        // corrosion fatigue life prediction model from your research
        // For now, return a sample value based on some basic formula
        double crackRange = afFinal - apcFinal;
        double stressEffect = Math.pow(sigma / 100, 2);
        double freqEffect = 1.0 / Math.log10(freq + 1);

        return (crackRange * 1000000 * freqEffect) / (pitCoeff * stressEffect * scoeff);
    }
}
