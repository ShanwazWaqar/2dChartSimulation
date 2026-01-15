import React from 'react';
import { useLocation, Link } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';

const SimulationResults = ({ results: propResults, simulationData: propData }) => {
  const location = useLocation();
  const results = propResults || location.state?.results;
  const simulationData = propData || location.state?.simulationData;

  // If used as a standalone page and no results, show the full page error
  // If used as a component (propResults exists) and no results, we just don't render or render null (parent handles loading/empty state)
  if (!results && !propResults) {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
        <div className="text-center p-8 bg-white rounded-xl shadow-md max-w-md w-full border border-gray-100">
          <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <ArrowLeft className="w-8 h-8 text-gray-400" />
          </div>
          <h2 className="text-xl font-bold text-gray-900 mb-2">No Results Found</h2>
          <p className="text-gray-500 mb-6">Please run a simulation first to generate results.</p>
          <Link
            to="/simulate"
            className="inline-flex items-center justify-center w-full px-4 py-2 bg-uga-red text-white rounded-lg hover:bg-red-700 transition-colors font-medium"
          >
            Go to Simulation
          </Link>
        </div>
      </div>
    );
  }

  const {
    success,
    message,
    error,
    corrosionDetected,
    corrosionPercentage,
    analysisResult,
    imagePath,
    processedImagePath,
  } = results;

  return (
    <div className="mt-6 bg-white shadow-md rounded-xl p-6">
      <h2 className="text-2xl font-semibold text-gray-800 mb-4">Simulation Results</h2>

      {success ? (
        <>
          <div className="space-y-2 text-gray-700">
            <p><span className="font-medium">Corrosion Detected:</span> {corrosionDetected ? 'Yes' : 'No'}</p>
            <p><span className="font-medium">Corrosion Percentage:</span> {corrosionPercentage.toFixed(2)}%</p>
            <p><span className="font-medium">Simulation Stats:</span> {analysisResult}</p>
            <p><span className="font-medium">Message:</span> {message}</p>
          </div>

          <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-6">
            {imagePath && (
              <div>
                <h3 className="text-lg font-semibold mb-2">Original Image</h3>
                <img
                  src={imagePath.startsWith('http') ? imagePath : `${process.env.REACT_APP_API_URL || 'http://localhost:8080'}/${imagePath.replace(/^\//, '')}`}
                  alt="Original Simulation Input"
                  className="w-full border border-gray-300 rounded-lg"
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = 'https://placehold.co/400x300?text=Image+Not+Found';
                  }}
                />
              </div>
            )}
            {processedImagePath && (
              <div>
                <h3 className="text-lg font-semibold mb-2">Processed Output</h3>
                <img
                  src={processedImagePath.startsWith('http') ? processedImagePath : `${process.env.REACT_APP_API_URL || 'http://localhost:8080'}/${processedImagePath.replace(/^\//, '')}`}
                  alt="Processed Simulation Result"
                  className="w-full border border-gray-300 rounded-lg"
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = 'https://placehold.co/400x300?text=Image+Not+Found';
                  }}
                />
              </div>
            )}
          </div>
        </>
      ) : (
        <div className="text-red-600 mt-4">
          <p>Error: {error || 'An unexpected error occurred.'}</p>
        </div>
      )}
    </div>
  );
};

export default SimulationResults;
