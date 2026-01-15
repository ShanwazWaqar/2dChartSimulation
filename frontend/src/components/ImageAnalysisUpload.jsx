// src/components/ImageAnalysisUpload.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Upload, X, Image as ImageIcon, AlertCircle, RefreshCw, FileText, CheckCircle } from 'lucide-react';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const ImageAnalysisUpload = () => {
  const navigate = useNavigate();
  const [selectedFile, setSelectedFile] = useState(null);
  const [threshold, setThreshold] = useState(128); // Threshold for corrosion detection
  const [analysisType, setAnalysisType] = useState('wavelet'); // wavelet or edge 
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [analysisResults, setAnalysisResults] = useState(null);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (!file.type.startsWith('image/')) {
        setError('Please select a valid image file');
        return;
      }

      if (file.size > 10 * 1024 * 1024) {
        setError('File size must be less than 10MB');
        return;
      }

      setSelectedFile(file);
      setError('');
    }
  };

  const validateForm = () => {
    if (!selectedFile) {
      setError('Please upload the image first');
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    setIsLoading(true);
    setError('');

    try {
      const formData = new FormData();
      formData.append('image', selectedFile);
      formData.append('threshold', threshold);
      formData.append('analysisType', analysisType);

      const response = await fetch(`${API_URL}/api/image-analysis/analyze`, {
        method: 'POST',
        body: formData
      });

      if (response.ok) {
        const result = await response.json();

        // Convert file system paths to HTTP URLs
        if (result.imagePath) {
          // Extract just the relative path from output directory
          const relativePath = result.imagePath.replace(/\\/g, '/').split('/output/')[1];
          result.originalImagePath = relativePath ? `${API_URL}/output/${relativePath}` : result.imagePath;
        }

        if (result.processedImagePath) {
          const relativePath = result.processedImagePath.replace(/\\/g, '/').split('/output/')[1];
          result.waveletImagePath = relativePath ? `${API_URL}/output/${relativePath}` : result.processedImagePath;
        }

        setAnalysisResults(result);
        // Results will be displayed inline below the form
      } else {
        throw new Error('Image analysis failed');
      }
    } catch (error) {
      console.error('Error:', error);
      setError('An error occurred while analyzing the image. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleReset = () => {
    setSelectedFile(null);
    setError('');
    setAnalysisResults(null);
    const fileInput = document.querySelector('input[type="file"]');
    if (fileInput) fileInput.value = '';
  };

  return (
    <div className="min-h-screen bg-gray-50 py-12">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">

        {/* Header */}
        <div className="text-center mb-12">
          <h1 className="text-3xl font-extrabold text-gray-900 sm:text-4xl">
            Corrosion Image Analysis
          </h1>
          <p className="mt-4 text-lg text-gray-500 max-w-2xl mx-auto">
            Upload corrosion imagery to perform automated wavelet transformation and structural defect detection.
          </p>
        </div>

        <div className="bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden">
          <form onSubmit={handleSubmit} className="p-8">

            {/* Upload Zone */}
            <div className="mb-8">
              <label className="block text-sm font-medium text-gray-700 mb-2">Source Image</label>

              {!selectedFile ? (
                <div className="flex justify-center px-6 pt-10 pb-12 border-2 border-gray-300 border-dashed rounded-lg hover:bg-gray-50 transition-colors group cursor-pointer relative">
                  <input
                    type="file"
                    className="absolute inset-0 w-full h-full opacity-0 cursor-pointer z-10"
                    accept="image/*"
                    onChange={handleFileChange}
                  />
                  <div className="space-y-2 text-center">
                    <div className="mx-auto h-16 w-16 text-gray-400 flex items-center justify-center rounded-full bg-gray-50 group-hover:bg-white transition-colors">
                      <Upload className="h-8 w-8 text-uga-red" />
                    </div>
                    <div className="text-sm text-gray-600">
                      <span className="font-medium text-uga-red hover:text-red-700">Click to upload</span> or drag and drop
                    </div>
                    <p className="text-xs text-gray-400">PNG, JPG, GIF up to 10MB</p>
                  </div>
                </div>
              ) : (
                <div className="bg-gray-50 rounded-lg p-4 border border-gray-200 flex items-center justify-between">
                  <div className="flex items-center space-x-4">
                    <div className="h-16 w-16 bg-white rounded-lg border border-gray-200 overflow-hidden flex-shrink-0">
                      <img
                        src={URL.createObjectURL(selectedFile)}
                        alt="Preview"
                        className="h-full w-full object-cover"
                      />
                    </div>
                    <div>
                      <p className="text-sm font-medium text-gray-900 truncate max-w-xs">{selectedFile.name}</p>
                      <p className="text-xs text-gray-500">{(selectedFile.size / 1024 / 1024).toFixed(2)} MB</p>
                    </div>
                  </div>
                  <button
                    type="button"
                    onClick={() => setSelectedFile(null)}
                    className="text-gray-400 hover:text-red-500 transition-colors bg-white p-2 rounded-full shadow-sm border border-gray-200"
                  >
                    <X className="h-5 w-5" />
                  </button>
                </div>
              )}
            </div>

            {/* Analysis Configuration */}
            {selectedFile && (
              <div className="space-y-6 mb-8 bg-gray-50 p-6 rounded-lg border border-gray-200">
                <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wide">Analysis Configuration</h3>

                {/* Threshold Slider */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Detection Threshold: <span className="text-uga-red font-bold">{threshold}</span>
                  </label>
                  <input
                    type="range"
                    min="0"
                    max="255"
                    value={threshold}
                    onChange={(e) => setThreshold(parseInt(e.target.value))}
                    className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer accent-uga-red"
                  />
                  <div className="flex justify-between text-xs text-gray-500 mt-1">
                    <span>0 (Low Sensitivity)</span>
                    <span>255 (High Sensitivity)</span>
                  </div>
                </div>

                {/* Analysis Type Selector */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Analysis Method</label>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <button
                        type="button"
                        onClick={() => setAnalysisType('wavelet')}
                        className={`w-full px-4 py-3 rounded-lg border-2 transition-colors font-medium ${analysisType === 'wavelet'
                          ? 'bg-uga-red text-white border-uga-red'
                          : 'bg-white text-gray-700 border-gray-300 hover:border-uga-red'
                          }`}
                      >
                        Wavelet Transform
                      </button>
                      <p className="mt-2 text-xs text-gray-500 leading-relaxed">
                        Multi-scale frequency analysis. Highlights corrosion severity in different intensity levels (red=severe, orange=moderate, yellow=early).
                      </p>
                    </div>
                    <div>
                      <button
                        type="button"
                        onClick={() => setAnalysisType('edge')}
                        className={`w-full px-4 py-3 rounded-lg border-2 transition-colors font-medium ${analysisType === 'edge'
                          ? 'bg-uga-red text-white border-uga-red'
                          : 'bg-white text-gray-700 border-gray-300 hover:border-uga-red'
                          }`}
                      >
                        Edge Detection
                      </button>
                      <p className="mt-2 text-xs text-gray-500 leading-relaxed">
                        Sobel operator for boundary detection. Identifies structural defects and cracks (cyan=strong edges, yellow=weak edges).
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            )}

            {error && (
              <div className="mt-4 flex items-center text-sm text-red-700 bg-red-50 p-3 rounded-lg border border-red-200" role="alert">
                <AlertCircle className="h-5 w-5 mr-2 flex-shrink-0" aria-hidden="true" />
                <span className="sr-only">Error:</span>
                {error}
              </div>
            )}

            {/* Actions */}
            <div className="flex justify-end space-x-4 border-t border-gray-100 pt-6">
              <button
                type="button"
                onClick={handleReset}
                className="px-6 py-2.5 bg-white text-gray-700 font-medium rounded-lg border border-gray-300 hover:bg-gray-50 flex items-center transition-colors focus:ring-2 focus:ring-uga-red focus:outline-none"
              >
                Reset
              </button>
              <button
                type="submit"
                disabled={isLoading || !selectedFile}
                className="px-8 py-2.5 bg-uga-red text-white font-medium rounded-lg hover:bg-red-800 disabled:opacity-50 disabled:cursor-not-allowed flex items-center shadow-md hover:shadow-lg transition-all focus:ring-2 focus:ring-offset-2 focus:ring-uga-red focus:outline-none"
              >
                {isLoading ? (
                  <>
                    <RefreshCw className="mr-2 h-4 w-4 animate-spin" aria-hidden="true" />
                    Processing...
                  </>
                ) : (
                  <>
                    Begin Analysis
                  </>
                )}
              </button>
            </div>
          </form>
        </div>

        {/* Results Preview (Conditional) */}
        {
          analysisResults && (
            <div className="mt-12 bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden animate-in fade-in slide-in-from-bottom-4 duration-500">
              <div className="bg-green-50 px-8 py-4 border-b border-green-100 flex items-center">
                <CheckCircle className="h-6 w-6 text-green-600 mr-3" />
                <h2 className="text-lg font-bold text-green-900">Analysis Complete</h2>
              </div>

              <div className="p-8">
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
                  <div>
                    <h3 className="text-sm font-semibold text-gray-500 uppercase tracking-wide mb-3">Original Input</h3>
                    <div className="aspect-video bg-gray-100 rounded-lg overflow-hidden border border-gray-200">
                      {analysisResults.originalImagePath ? (
                        <img
                          src={analysisResults.originalImagePath}
                          alt="Original"
                          className="w-full h-full object-contain"
                          onError={(e) => {
                            e.target.onerror = null;
                            e.target.src = 'https://placehold.co/800x600?text=Original+Image+Not+Found';
                          }}
                        />
                      ) : (
                        <div className="flex items-center justify-center h-full text-gray-400">
                          <span>No image available</span>
                        </div>
                      )}
                    </div>
                  </div>
                  <div>
                    <h3 className="text-sm font-semibold text-gray-500 uppercase tracking-wide mb-3">
                      {analysisType === 'wavelet' ? 'Wavelet Transformation' : 'Edge Detection'}
                    </h3>
                    <div className="aspect-video bg-gray-100 rounded-lg overflow-hidden border border-gray-200">
                      {analysisResults.waveletImagePath ? (
                        <img
                          src={analysisResults.waveletImagePath}
                          alt="Processed"
                          className="w-full h-full object-contain"
                          onError={(e) => {
                            e.target.onerror = null;
                            e.target.src = 'https://placehold.co/800x600?text=Processed+Image+Not+Found';
                          }}
                        />
                      ) : (
                        <div className="flex items-center justify-center h-full text-gray-400">
                          <span>No processed image available</span>
                        </div>
                      )}
                    </div>
                  </div>
                </div>

                <div className="bg-gray-50 p-6 rounded-lg border border-gray-200">
                  <div className="flex items-center mb-4">
                    <FileText className="h-5 w-5 text-gray-400 mr-3" />
                    <h3 className="text-sm font-semibold text-gray-900">Analysis Summary</h3>
                  </div>
                  <div className="space-y-2 text-sm">
                    <div className="flex justify-between">
                      <span className="text-gray-600">Analysis Method:</span>
                      <span className="font-medium">{analysisType === 'wavelet' ? 'Wavelet Transform' : 'Edge Detection'}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-gray-600">Threshold Used:</span>
                      <span className="font-medium">{threshold}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-gray-600">Detection Rate:</span>
                      <span className="font-medium text-uga-red">{analysisResults.corrosionPercentage?.toFixed(2)}%</span>
                    </div>
                  </div>
                </div>

                <div className="mt-8 flex justify-center space-x-4">
                  <button
                    onClick={handleReset}
                    className="px-6 py-2 bg-uga-red text-white text-sm font-medium rounded-lg hover:bg-red-800 transition-colors"
                  >
                    Analyze Another Image
                  </button>
                  <button
                    onClick={() => navigate('/')}
                    className="px-6 py-2 bg-white border border-gray-300 text-gray-700 text-sm font-medium rounded-lg hover:bg-gray-50 transition-colors"
                  >
                    Return to Dashboard
                  </button>
                </div>
              </div>
            </div>
          )
        }
      </div >
    </div >
  );
};

export default ImageAnalysisUpload;
