package com.simulation.corrosion.controller;

import com.simulation.corrosion.dto.Simulation2DRequestDTO;
import com.simulation.corrosion.dto.Simulation2DResponseDTO;
import com.simulation.corrosion.service.Simulation2DService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulate2d")
public class Simulation2DController {
    @Autowired
    private Simulation2DService simulation2DService;

    @PostMapping
    public Simulation2DResponseDTO runSimulation(@RequestBody Simulation2DRequestDTO request) {
        return simulation2DService.runSimulation(request);
    }
} 