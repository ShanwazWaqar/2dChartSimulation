import React from 'react';

const SimulationResults = ({ results }) => {
  if (!results) {
    return (
      <div className="text-center text-gray-500 mt-8">
        <p>No simulation results available.</p>
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
                  src={`file://${imagePath}`}
                  alt="Original Simulation Input"
                  className="w-full border border-gray-300 rounded-lg"
                />
              </div>
            )}
            {processedImagePath && (
              <div>
                <h3 className="text-lg font-semibold mb-2">Processed Output</h3>
                <img
                  src={`file://${processedImagePath}`}
                  alt="Processed Simulation Result"
                  className="w-full border border-gray-300 rounded-lg"
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
