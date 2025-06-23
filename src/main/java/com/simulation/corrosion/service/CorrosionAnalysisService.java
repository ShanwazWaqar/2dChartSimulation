package com.simulation.corrosion.service;

import com.simulation.corrosion.dto.CorrosionAnalysisRequestDTO;
import com.simulation.corrosion.dto.CorrosionAnalysisResponseDTO;
import com.simulation.legacy.sim.CorrosionBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Modern Spring Boot service for corrosion analysis
 * Provides a clean interface to the legacy corrosion analysis functionality
 */
@Service
public class CorrosionAnalysisService {
    
    /**
     * Perform corrosion analysis with the given parameters
     */
    public CorrosionAnalysisResponseDTO analyzeCorrosion(CorrosionAnalysisRequestDTO request) {
        CorrosionAnalysisResponseDTO response = new CorrosionAnalysisResponseDTO();
        
        try {
            // Validate request
            validateRequest(request);
            
            // Create legacy bean and set parameters
            CorrosionBean corrosionBean = new CorrosionBean();
            
            // Set corrosion parameters
            corrosionBean.setAtomMass(request.getAtomMass());
            corrosionBean.setValence(request.getValence());
            corrosionBean.setFarCnst(request.getFarCnst());
            corrosionBean.setDensity(request.getDensity());
            corrosionBean.setThresRng(request.getThresRng());
            corrosionBean.setStressCnst(request.getStressCnst());
            corrosionBean.setPitCoeff(request.getPitCoeff());
            corrosionBean.setEnthalpy(request.getEnthalpy());
            corrosionBean.setGasCnst(request.getGasCnst());
            corrosionBean.setTemp(request.getTemp());
            corrosionBean.setFreq(request.getFreq());
            
            // Set fatigue parameters
            corrosionBean.setFcoeffsmall(request.getFcoeffsmall());
            corrosionBean.setFcoefflong(request.getFcoefflong());
            corrosionBean.setFexpsmall(request.getFexpsmall());
            corrosionBean.setFexplong(request.getFexplong());
            corrosionBean.setGeoSmall(request.getGeoSmall());
            corrosionBean.setGeoLong(request.getGeoLong());
            corrosionBean.setCrackLen(request.getCrackLen());
            corrosionBean.setFracTough(request.getFracTough());
            corrosionBean.setSigma(request.getSigma());

            // Set safe defaults for apc_final and af_final if not provided
            corrosionBean.setApc_final("0.01"); // safe small value
            corrosionBean.setAf_final("0.1");   // safe small value
            
            // Perform calculations (preserving core logic)
            String initiationLife = corrosionBean.getInitiationLife();
            String crackPropagation = corrosionBean.getcrackPropagation();
            String fatigueLife = corrosionBean.getFatigueLife();
            
            // Build response
            response.setSuccess(true);
            response.setInitiationLife(initiationLife);
            response.setCrackPropagation(crackPropagation);
            response.setFatigueLife(fatigueLife);
            response.setMessage("Corrosion analysis completed successfully");
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setError(e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Validate corrosion analysis request
     */
    private void validateRequest(CorrosionAnalysisRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        // Validate required corrosion parameters
        if (!StringUtils.hasText(request.getAtomMass())) {
            throw new IllegalArgumentException("Atomic mass cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getValence())) {
            throw new IllegalArgumentException("Valence cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getFarCnst())) {
            throw new IllegalArgumentException("Faraday's constant cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getDensity())) {
            throw new IllegalArgumentException("Density cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getTemp())) {
            throw new IllegalArgumentException("Temperature cannot be empty");
        }
        
        // Validate required fatigue parameters
        if (!StringUtils.hasText(request.getFcoeffsmall())) {
            throw new IllegalArgumentException("Fatigue coefficient for small crack cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getFcoefflong())) {
            throw new IllegalArgumentException("Fatigue coefficient for long crack cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getFexpsmall())) {
            throw new IllegalArgumentException("Fatigue exponent for small crack cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getFexplong())) {
            throw new IllegalArgumentException("Fatigue exponent for long crack cannot be empty");
        }
        
        if (!StringUtils.hasText(request.getSigma())) {
            throw new IllegalArgumentException("Stress cannot be empty");
        }
    }
} 