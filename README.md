# Corrosion Simulation - Spring Boot Modernization

This project represents the modernization of a legacy corrosion simulation system to Spring Boot 3.5.0 with Java 21. The core logic has been preserved while providing a modern REST API interface.

## 🚀 Modernization Overview

### What Was Modernized

1. **Legacy Servlet → Spring Boot REST Controllers**
   - `SiaServlet` → `NeuralNetworkController`
   - Legacy form-based requests → JSON REST API
   - File upload handling with proper validation

2. **Legacy Beans → Spring Services**
   - `CorrosionBean` → `CorrosionAnalysisService`
   - `nnet` → `NeuralNetworkService`
   - Proper dependency injection and validation

3. **Data Transfer Objects (DTOs)**
   - Modern request/response DTOs with validation
   - Proper error handling and status codes
   - Swagger/OpenAPI documentation

4. **Configuration & Error Handling**
   - Spring Boot configuration properties
   - Global exception handler
   - Proper logging and monitoring

### Core Logic Preservation

✅ **All core algorithms preserved:**
- Neural network training and testing logic (`nnet.java`)
- Corrosion analysis calculations (`CorrosionBean.java`)
- Image analysis algorithms (legacy classes maintained)
- Backpropagation network (`BP.java`)

## 📁 Project Structure

```
src/main/java/com/simulation/
├── corrosion/                    # Modern Spring Boot components
│   ├── controller/              # REST controllers
│   ├── service/                 # Business logic services
│   ├── dto/                     # Data transfer objects
│   ├── exception/               # Global exception handling
│   └── CorrosionSimulationApplication.java
└── legacy/                      # Original legacy code (preserved)
    ├── sim/                     # Neural network simulation
    ├── nsfisdas/                # Image analysis and BP networks
    └── [other legacy classes]
```

## 🛠️ Technology Stack

- **Spring Boot**: 3.5.0
- **Java**: 21
- **Database**: H2 (in-memory for development)
- **Documentation**: Swagger/OpenAPI 3
- **Validation**: Jakarta Validation
- **Build Tool**: Maven

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone and build:**
   ```bash
   git clone <repository-url>
   cd corrosion-sim
   mvn clean install
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application:**
   - Main application: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

## 🧪 Testing the Modernization

### 1. Health Checks

Test that all services are running:

```bash
# Neural Network Service
curl http://localhost:8080/api/neural-network/health

# Corrosion Analysis Service
curl http://localhost:8080/api/corrosion/health

# Image Analysis Service
curl http://localhost:8080/api/image-analysis/health
```

### 2. Neural Network Operations

#### Check Network Status
```bash
curl http://localhost:8080/api/neural-network/status
```

#### Train Neural Network
```bash
curl -X POST http://localhost:8080/api/neural-network/train \
  -H "Content-Type: application/json" \
  -d '{
    "inNodes": "10",
    "outNodes": "1",
    "noLayers": "3",
    "noFirstLayer": "5",
    "noSecLayer": "3",
    "noTrainData": "100",
    "noTestData": "20"
  }'
```

#### Test Neural Network
```bash
curl -X POST http://localhost:8080/api/neural-network/test \
  -H "Content-Type: application/json" \
  -d '{
    "inNodes": "10",
    "outNodes": "1",
    "noLayers": "3",
    "noFirstLayer": "5",
    "noSecLayer": "3",
    "noTrainData": "100",
    "noTestData": "20"
  }'
```

### 3. Corrosion Analysis

```bash
curl -X POST http://localhost:8080/api/corrosion/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "atomMass": "55.85",
    "valence": "2",
    "farCnst": "96485",
    "density": "7.87",
    "temp": "298",
    "fcoeffsmall": "1.0E-10",
    "fcoefflong": "1.0E-10",
    "fexpsmall": "3.0",
    "fexplong": "3.0",
    "sigma": "200"
  }'
```

### 4. Image Analysis

```bash
curl -X POST http://localhost:8080/api/image-analysis/analyze \
  -F "image=@/path/to/your/image.jpg" \
  -F "threshold=128" \
  -F "analysisType=corrosion"
```

### 5. Unit Tests

Run the test suite:

```bash
mvn test
```

### 6. Integration Tests

Test the complete workflow:

```bash
# 1. Check all services are healthy
curl http://localhost:8080/api/neural-network/status
curl http://localhost:8080/api/corrosion/health
curl http://localhost:8080/api/image-analysis/health

# 2. Test neural network training
# (Use the training curl command above)

# 3. Test corrosion analysis
# (Use the corrosion analysis curl command above)

# 4. Check output files
ls -la output/nnet_sia/
ls -la output/upload/
```

## 📊 API Documentation

### Swagger UI
Access the interactive API documentation at: http://localhost:8080/swagger-ui.html

### Available Endpoints

#### Neural Network API (`/api/neural-network`)
- `GET /health` - Service health check
- `GET /status` - Network status and configuration
- `POST /train` - Train neural network
- `POST /test` - Test neural network

#### Corrosion Analysis API (`/api/corrosion`)
- `GET /health` - Service health check
- `POST /analyze` - Perform corrosion analysis

#### Image Analysis API (`/api/image-analysis`)
- `GET /health` - Service health check
- `POST /analyze` - Analyze image for corrosion
- `POST /analyze-2d` - 2D image analysis

## 🔧 Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# File paths
app.upload.path=output/upload
app.output.path=output/nnet_sia
app.images.path=output/images

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Server configuration
server.port=8080
```

## 🐛 Troubleshooting

### Common Issues

1. **Port already in use:**
   ```bash
   # Change port in application.properties
   server.port=8081
   ```

2. **File upload errors:**
   - Check file size limits in `application.properties`
   - Ensure upload directories exist

3. **Legacy resource not found:**
   - Verify `legacy-resources/` directory exists
   - Check file paths in legacy code

### Logs

Check application logs for detailed error information:

```bash
# View logs
tail -f logs/application.log

# Or check console output when running with mvn spring-boot:run
```

## 🔄 Migration Notes

### What Was Preserved
- ✅ All neural network algorithms
- ✅ Corrosion calculation formulas
- ✅ Image analysis core logic
- ✅ Backpropagation network implementation
- ✅ Legacy resource files and weights

### What Was Modernized
- ✅ Servlet-based interface → REST API
- ✅ Form-based requests → JSON DTOs
- ✅ Manual file handling → Spring file upload
- ✅ Basic error handling → Global exception handler
- ✅ No documentation → Swagger/OpenAPI docs

### Legacy Code Location
All original legacy code is preserved in `src/main/java/com/simulation/legacy/` and continues to function exactly as before. The modernization only adds a modern interface layer on top.

## 📈 Performance

The modernized application maintains the same performance characteristics as the legacy system while adding:
- Better error handling and logging
- Input validation
- RESTful API design
- Modern development practices

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details. 