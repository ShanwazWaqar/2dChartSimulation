package com.simulation.corrosion;

import com.simulation.legacy.nsfisdas.BP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

@SpringBootApplication
@ComponentScan(basePackages = {
	"com.simulation.corrosion"
})
public class CorrosionSimulationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorrosionSimulationApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner testBP() {
		return args -> {
			try {
				System.out.println("=== Running BP Test ===");
				new BP(); // should load resources and run inference
				System.out.println("=== BP Test Completed ===");
			} catch (Exception e) {
				System.err.println("BP Test failed: " + e.getMessage());
				e.printStackTrace();
			}
		};
	}*/

	private String saveImageFile(MultipartFile imageFile) throws IOException {
		// Use project root as base
		Path uploadDir = Paths.get(System.getProperty("user.dir"), "output", "images");
		Files.createDirectories(uploadDir);

		// Generate unique filename
		String originalFilename = imageFile.getOriginalFilename();
		String extension = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}

		String filename = "image_" + System.currentTimeMillis() + extension;
		Path filePath = uploadDir.resolve(filename);

		// Log the absolute path for debugging
		System.out.println("Saving image to: " + filePath.toAbsolutePath());

		// Save the file
		imageFile.transferTo(filePath.toFile());

		return filePath.toString();
	}
}

