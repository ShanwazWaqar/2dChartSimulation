package com.simulation.legacy.sim;

import org.springframework.stereotype.Component;

/**
 * Corrosion Analysis utility class
 * Provides static methods for corrosion analysis messages
 */
@Component
public class CorrosionAnal {
	
	public CorrosionAnal() {
		
	}
	
	/**
	 * Get the original message
	 */
	public static String getMessage() {
		return "This is the original message";
	}
	
	/**
	 * Get the initialization message
	 */
	public static String getInitmessage() {
		return "This is the new message";
	}
}