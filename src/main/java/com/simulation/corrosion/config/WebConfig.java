package com.simulation.corrosion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from the 'output' directory at the root of the project
        // This allows access to http://localhost:8080/output/sim2d/result.png
        registry.addResourceHandler("/output/**")
                .addResourceLocations("file:output/");
    }
}
