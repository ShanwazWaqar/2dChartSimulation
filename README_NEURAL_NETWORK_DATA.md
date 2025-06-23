# Neural Network Data Format Guide

## Overview
This guide explains the data format for training and testing the corrosion simulation neural network.

## Data Structure

### Format: Tab-Separated Values (TSV)
Each line contains: `[INPUT_FEATURES] [OUTPUT_FLAGS]`

- **INPUT_FEATURES**: First N columns (corrosion parameters)
- **OUTPUT_FLAGS**: Last M columns (target values/classifications)

### Total Columns = FEATURES + FLAGS

## Sample Data Files

### 1. Basic Configuration (3x1)
- **File**: `sample_traindata_3x1.txt` / `sample_testdata_3x1.txt`
- **Structure**: 3 input features + 1 output flag
- **Use Case**: Simple corrosion prediction

**Format:**
```
Feature1  Feature2  Feature3  Output1
0.1       0.05      0.047     0.0
0.5       0.05      0.047     0.0
...
```

### 2. Advanced Configuration (5x2)
- **File**: `sample_traindata_5x2.txt` / `sample_testdata_5x2.txt`
- **Structure**: 5 input features + 2 output flags
- **Use Case**: Complex corrosion analysis with multiple outputs

**Format:**
```
Feature1  Feature2  Feature3  Feature4  Feature5  Output1  Output2
0.1       0.05      0.047     0.2       0.3       0.0      0.1
0.5       0.05      0.047     0.2       0.3       0.0      0.1
...
```

## How to Use with API

### Training Endpoint: `/api/neural-network/train`

**Parameters:**
- `inNodes`: Number of input features (e.g., "3" for 3x1, "5" for 5x2)
- `outNodes`: Number of output flags (e.g., "1" for 3x1, "2" for 5x2)
- `noLayers`: Number of hidden layers (typically "3")
- `noFirstLayer`: Nodes in first hidden layer (e.g., "8")
- `noSecLayer`: Nodes in second hidden layer (e.g., "4")
- `noTrainData`: Number of training samples (e.g., "40")
- `noTestData`: Number of test samples (e.g., "10")
- `serverPath`: Output directory (e.g., "output/")
- `trainFileName`: Name for training file (e.g., "traindata.txt")
- `trainFile`: Upload the training data file

### Testing Endpoint: `/api/neural-network/test`

**Parameters:**
- Same as training, but use `testFileName` and `testFile`

## Example API Calls

### For 3x1 Configuration:
```bash
# Training
curl -X POST "http://localhost:8080/api/neural-network/train" \
  -F "inNodes=3" \
  -F "outNodes=1" \
  -F "noLayers=3" \
  -F "noFirstLayer=8" \
  -F "noSecLayer=4" \
  -F "noTrainData=40" \
  -F "noTestData=10" \
  -F "serverPath=output/" \
  -F "trainFileName=traindata.txt" \
  -F "trainFile=@sample_traindata_3x1.txt"

# Testing
curl -X POST "http://localhost:8080/api/neural-network/test" \
  -F "inNodes=3" \
  -F "outNodes=1" \
  -F "noLayers=3" \
  -F "noFirstLayer=8" \
  -F "noSecLayer=4" \
  -F "noTrainData=40" \
  -F "noTestData=10" \
  -F "serverPath=output/" \
  -F "testFileName=testdata.txt" \
  -F "testFile=@sample_testdata_3x1.txt"
```

### For 5x2 Configuration:
```bash
# Training
curl -X POST "http://localhost:8080/api/neural-network/train" \
  -F "inNodes=5" \
  -F "outNodes=2" \
  -F "noLayers=3" \
  -F "noFirstLayer=10" \
  -F "noSecLayer=6" \
  -F "noTrainData=40" \
  -F "noTestData=10" \
  -F "serverPath=output/" \
  -F "trainFileName=traindata.txt" \
  -F "trainFile=@sample_traindata_5x2.txt"
```

## Data Interpretation

### Input Features (Corrosion Parameters)
- **Feature 1**: Temperature factor (0.0 - 1.0)
- **Feature 2**: Humidity factor (0.0 - 1.0)  
- **Feature 3**: Chemical concentration (0.0 - 1.0)
- **Feature 4**: Time factor (0.0 - 1.0) [for 5x2]
- **Feature 5**: Material factor (0.0 - 1.0) [for 5x2]

### Output Flags (Predictions)
- **Output 1**: Corrosion rate (0.0 - 1.0)
- **Output 2**: Corrosion type classification (0.0 - 1.0) [for 5x2]

## Expected Results

### Success Response:
```json
{
  "message": "Neural network training completed successfully",
  "success": true,
  "status": "trained",
  "outputPath": "output/nnet_sia"
}
```

### Output Files Generated:
- `output/nnet_sia/EFile.txt`: Training error progression
- `output/nnet_sia/WFile.txt`: Network weights
- `output/nnet_sia/WtFile.txt`: Weight updates
- `output/nnet_sia/results.txt`: Test results and predictions

## Tips for Creating Your Own Data

1. **Normalize Values**: Keep all values between 0.0 and 1.0
2. **Consistent Format**: Use tab separation, no headers
3. **Balanced Data**: Include various combinations of input parameters
4. **Realistic Targets**: Output values should reflect expected corrosion behavior
5. **Sufficient Samples**: At least 20-30 samples for training, 5-10 for testing

## Troubleshooting

- **"Invalid line" errors**: Check for proper tab separation and correct number of columns
- **"Array index mismatch"**: Ensure `inNodes` + `outNodes` matches total columns in data
- **"File not found"**: Verify file upload and correct `trainFileName`/`testFileName` 