package com.simulation.corrosion.controller;

import com.simulation.corrosion.dto.SimulationRequestDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/simulation")
public class SimulationController {

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("simulation", new SimulationRequestDTO());
        return "simulate";  // simulate.html page
    }

    @PostMapping
    public String runSimulation(@ModelAttribute SimulationRequestDTO simulation, Model model) {
        // For now, just echo the inputs to test
        model.addAttribute("result", simulation);
        return "result"; // result.html page
    }
}
