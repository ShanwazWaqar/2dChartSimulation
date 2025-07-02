// src/components/ImageAnalysisUpload.jsx

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const API_URL = process.env.REACT_APP_API_URL;

const ImageAnalysisUpload = () => {
  const navigate = useNavigate();
  const [selectedFile, setSelectedFile] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [analysisResults, setAnalysisResults] = useState(null);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      // Validate file type
      if (!file.type.startsWith('image/')) {
        setError('Please select a valid image file');
        return;
      }
      
      // Validate file size (max 10MB)
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
    
    if (!validateForm()) {
      return;
    }

    setIsLoading(true);
    setError('');

    try {
      const formData = new FormData();
      formData.append('simImage', selectedFile);
      formData.append('action', 'Submit');

      const response = await fetch(`${API_URL}/api/image-analysis`, {
        method: 'POST',
        body: formData
      });

      if (response.ok) {
        const result = await response.json();
        setAnalysisResults(result);
        // Navigate to wavelet view with results
        navigate('/wavelet', { state: { analysisResults: result } });
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
    // Reset file input
    const fileInput = document.querySelector('input[type="file"]');
    if (fileInput) {
      fileInput.value = '';
    }
  };

  return (
    <div className="max-w-6xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-8">
        <h1 className="text-3xl font-bold text-center mb-8 text-gray-900">
          Analyze Corrosion Image
        </h1>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* File Upload Section */}
          <div className="bg-blue-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold mb-4 text-blue-900">
              Image Upload
            </h2>
            
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Upload Image
                </label>
                <div className="flex items-center justify-center w-full">
                  <label className="flex flex-col w-full h-32 border-4 border-dashed hover:bg-gray-100 hover:border-gray-300 group">
                    <div className="flex flex-col items-center justify-center pt-7">
                      <svg className="w-10 h-10 text-gray-400 group-hover:text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path>
                      </svg>
                      <p className="pt-1 text-sm tracking-wider text-gray-400 group-hover:text-gray-600">
                        {selectedFile ? selectedFile.name : 'Select an image file'}
                      </p>
                    </div>
                    <input
                      type="file"
                      className="opacity-0"
                      accept="image/*"
                      onChange={handleFileChange}
                    />
                  </label>
                </div>
                {error && <p className="text-red-500 text-sm mt-2">{error}</p>}
              </div>

              {selectedFile && (
                <div className="bg-white p-4 rounded-md border">
                  <h3 className="font-medium text-gray-700 mb-2">Selected File:</h3>
                  <div className="flex items-center space-x-3">
                    <div className="flex-shrink-0">
                      <img
                        src={URL.createObjectURL(selectedFile)}
                        alt="Preview"
                        className="h-16 w-16 object-cover rounded"
                      />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium text-gray-900 truncate">
                        {selectedFile.name}
                      </p>
                      <p className="text-sm text-gray-500">
                        {(selectedFile.size / 1024 / 1024).toFixed(2)} MB
                      </p>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex justify-center space-x-4">
            <button
              type="submit"
              disabled={isLoading || !selectedFile}
              className="px-8 py-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed font-medium"
            >
              {isLoading ? 'Analyzing...' : 'Submit'}
            </button>
            <button
              type="button"
              onClick={handleReset}
              className="px-8 py-3 bg-gray-600 text-white rounded-md hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 font-medium"
            >
              Reset
            </button>
          </div>
        </form>

        {/* Analysis Results */}
        {analysisResults && (
          <div className="mt-8 bg-green-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold mb-4 text-green-900">
              Analysis Results
            </h2>
            
            <div className="grid md:grid-cols-2 gap-6">
              <div>
                <h3 className="text-lg font-medium mb-3 text-gray-900">Original Image</h3>
                <div className="bg-white p-4 rounded-md">
                  <img
                    src={analysisResults.originalImagePath || '/images/imgupload.JPG'}
                    alt="Original"
                    className="w-full h-auto rounded"
                    onError={(e) => {
                      e.target.src = '/images/imgupload.JPG';
                    }}
                  />
                </div>
              </div>
              
              <div>
                <h3 className="text-lg font-medium mb-3 text-gray-900">Wavelet Transformed Image</h3>
                <div className="bg-white p-4 rounded-md">
                  <img
                    src={analysisResults.waveletImagePath || '/images/wavelet_placeholder.jpg'}
                    alt="Wavelet Transformed"
                    className="w-full h-auto rounded"
                    onError={(e) => {
                      e.target.src = '/images/wavelet_placeholder.jpg';
                    }}
                  />
                </div>
              </div>
            </div>

            {/* Features File Link */}
            <div className="mt-6 bg-white p-4 rounded-md">
              <h3 className="text-lg font-medium mb-2 text-gray-900">Analysis Data</h3>
              <div className="flex items-center space-x-4">
                <a
                  href="/api/features-file"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-blue-600 hover:text-blue-800 font-medium"
                >
                  📄 Click here to open Features File
                </a>
              </div>
            </div>

            {/* Run Another Analysis */}
            <div className="mt-6 bg-white p-4 rounded-md">
              <h3 className="text-lg font-medium mb-2 text-gray-900">Run Another Analysis</h3>
              <div className="flex space-x-4">
                <button
                  onClick={handleReset}
                  className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors"
                >
                  Yes
                </button>
                <button
                  onClick={() => navigate('/')}
                  className="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 transition-colors"
                >
                  No
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ImageAnalysisUpload;
