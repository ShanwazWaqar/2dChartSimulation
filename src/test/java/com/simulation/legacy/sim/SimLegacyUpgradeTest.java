package com.simulation.legacy.sim;

import com.simulation.corrosion.CorrosionSimulationApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Legacy test file - preserved for reference
 * The original test logic has been moved to modern test classes
 * This file is kept as a placeholder to maintain the test structure
 */
@SpringBootTest(classes = CorrosionSimulationApplication.class)
public class SimLegacyUpgradeTest {

    @Autowired
    private SimulateBean simulateBean;

    @Autowired
    private SimulateBean2D simulateBean2D;

    @Autowired
    private CorrosionBean corrosionBean;

    @Autowired
    private nnet neuralNetwork;

    @Test
    public void testSimulateBean() {
        assertNotNull(simulateBean);
        simulateBean.setNoRows("10");
        simulateBean.setNoColumns("20");
        simulateBean.setparam1("0.5");
        simulateBean.setparam2("0.3");
        simulateBean.setparam3("0.7");
        simulateBean.setparam4("0.2");
        simulateBean.setconstant("1.0");
        simulateBean.setstressfact("0.8");
        simulateBean.setNoIterations("100");
        assertEquals("10", simulateBean.getNoRows());
        assertEquals("20", simulateBean.getNoColumns());
        assertEquals("0.5", simulateBean.getparam1());
        assertEquals("0.3", simulateBean.getparam2());
        assertEquals("0.7", simulateBean.getparam3());
        assertEquals("0.2", simulateBean.getparam4());
        assertEquals("1.0", simulateBean.getconstant());
        assertEquals("0.8", simulateBean.getstressfact());
        assertEquals("100", simulateBean.getNoIterations());
    }

    @Test
    public void testSimulateBean2D() {
        assertNotNull(simulateBean2D);
        simulateBean2D.setNoRows("15");
        simulateBean2D.setNoColumns("25");
        simulateBean2D.setparam1("0.6");
        simulateBean2D.setparam2("0.4");
        simulateBean2D.setparam3("0.8");
        simulateBean2D.setparam4("0.1");
        simulateBean2D.setconstant("1.5");
        simulateBean2D.setstressfact("0.9");
        simulateBean2D.setNoIterations("150");
        assertEquals("15", simulateBean2D.getNoRows());
        assertEquals("25", simulateBean2D.getNoColumns());
        assertEquals("0.6", simulateBean2D.getparam1());
        assertEquals("0.4", simulateBean2D.getparam2());
        assertEquals("0.8", simulateBean2D.getparam3());
        assertEquals("0.1", simulateBean2D.getparam4());
        assertEquals("1.5", simulateBean2D.getconstant());
        assertEquals("0.9", simulateBean2D.getstressfact());
        assertEquals("150", simulateBean2D.getNoIterations());
    }

    @Test
    public void testCorrosionBean() {
        assertNotNull(corrosionBean);
        corrosionBean.setApc_final("0.05");
        corrosionBean.setAf_final("0.03");
        corrosionBean.setScoeff("0.8");
        corrosionBean.setSigma("100.0");
        corrosionBean.setAtomMass("55.85");
        corrosionBean.setValence("2");
        corrosionBean.setFarCnst("96485");
        corrosionBean.setDensity("7.87");
        corrosionBean.setThresRng("10.0");
        corrosionBean.setStressCnst("2.5");
        corrosionBean.setPitCoeff("0.001");
        corrosionBean.setEnthalpy("-285.8");
        corrosionBean.setGasCnst("8.314");
        corrosionBean.setTemp("298");
        corrosionBean.setFreq("1.0");
        assertEquals("0.05", corrosionBean.getApc_final());
        assertEquals("0.03", corrosionBean.getAf_final());
        assertEquals("0.8", corrosionBean.getScoeff());
        assertEquals("100.0", corrosionBean.getSigma());
        assertEquals("55.85", corrosionBean.getAtomMass());
        assertEquals("2", corrosionBean.getValence());
        assertEquals("96485", corrosionBean.getFarCnst());
        assertEquals("7.87", corrosionBean.getDensity());
        assertEquals("10.0", corrosionBean.getThresRng());
        assertEquals("2.5", corrosionBean.getStressCnst());
        assertEquals("0.001", corrosionBean.getPitCoeff());
        assertEquals("-285.8", corrosionBean.getEnthalpy());
        assertEquals("8.314", corrosionBean.getGasCnst());
        assertEquals("298", corrosionBean.getTemp());
        assertEquals("1.0", corrosionBean.getFreq());
    }

    @Test
    public void testNnetBean() {
        assertNotNull(neuralNetwork);
    }
} 