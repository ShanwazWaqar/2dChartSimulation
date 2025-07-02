import React from 'react';
import { useLocation, Link } from 'react-router-dom';

const NeuralNetworkResults = () => {
  const location = useLocation();
  const results = location.state?.results || {};

  return (
    <div className="max-w-6xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-8">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">
            Neural Network Analysis Results
          </h1>
          <Link
            to="/neural-network"
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
          >
            ← Back to Form
          </Link>
        </div>

        {Object.keys(results).length === 0 ? (
          <div className="text-center py-12">
            <div className="text-6xl mb-4">🧠</div>
            <h2 className="text-2xl font-semibold mb-4 text-gray-700">
              No Results Available
            </h2>
            <p className="text-gray-600 mb-6">
              Please run a neural network analysis to see results here.
            </p>
            <Link
              to="/neural-network"
              className="px-6 py-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              Start Analysis
            </Link>
          </div>
        ) : (
          <div className="space-y-8">
            {/* Network Configuration Summary */}
            <div className="bg-blue-50 rounded-lg p-6">
              <h2 className="text-xl font-semibold mb-4 text-blue-900">
                Network Configuration
              </h2>
              <div className="grid md:grid-cols-3 gap-4">
                <div className="bg-white p-4 rounded-md">
                  <h3 className="font-medium text-gray-700 mb-2">Input Nodes</h3>
                  <p className="text-2xl font-bold text-blue-600">{results.inNodes || 'N/A'}</p>
                </div>
                <div className="bg-white p-4 rounded-md">
                  <h3 className="font-medium text-gray-700 mb-2">Output Nodes</h3>
                  <p className="text-2xl font-bold text-blue-600">{results.outNodes || 'N/A'}</p>
                </div>
                <div className="bg-white p-4 rounded-md">
                  <h3 className="font-medium text-gray-700 mb-2">Hidden Layers</h3>
                  <p className="text-2xl font-bold text-blue-600">{results.noLayers || 'N/A'}</p>
                </div>
              </div>
            </div>

            {/* Training Results */}
            {results.trainingResults && (
              <div className="bg-green-50 rounded-lg p-6">
                <h2 className="text-xl font-semibold mb-4 text-green-900">
                  Training Results
                </h2>
                <div className="grid md:grid-cols-2 gap-6">
                  <div className="bg-white p-4 rounded-md">
                    <h3 className="font-medium text-gray-700 mb-2">Training Accuracy</h3>
                    <p className="text-2xl font-bold text-green-600">
                      {results.trainingResults.accuracy ? `${(results.trainingResults.accuracy * 100).toFixed(2)}%` : 'N/A'}
                    </p>
                  </div>
                  <div className="bg-white p-4 rounded-md">
                    <h3 className="font-medium text-gray-700 mb-2">Training Loss</h3>
                    <p className="text-2xl font-bold text-green-600">
                      {results.trainingResults.loss ? results.trainingResults.loss.toFixed(4) : 'N/A'}
                    </p>
                  </div>
                </div>
                {results.trainingResults.iterations && (
                  <div className="mt-4 bg-white p-4 rounded-md">
                    <h3 className="font-medium text-gray-700 mb-2">Training Iterations</h3>
                    <p className="text-lg text-gray-900">{results.trainingResults.iterations}</p>
                  </div>
                )}
              </div>
            )}

            {/* Testing Results */}
            {results.testingResults && (
              <div className="bg-purple-50 rounded-lg p-6">
                <h2 className="text-xl font-semibold mb-4 text-purple-900">
                  Testing Results
                </h2>
                <div className="grid md:grid-cols-2 gap-6">
                  <div className="bg-white p-4 rounded-md">
                    <h3 className="font-medium text-gray-700 mb-2">Testing Accuracy</h3>
                    <p className="text-2xl font-bold text-purple-600">
                      {results.testingResults.accuracy ? `${(results.testingResults.accuracy * 100).toFixed(2)}%` : 'N/A'}
                    </p>
                  </div>
                  <div className="bg-white p-4 rounded-md">
                    <h3 className="font-medium text-gray-700 mb-2">Testing Loss</h3>
                    <p className="text-2xl font-bold text-purple-600">
                      {results.testingResults.loss ? results.testingResults.loss.toFixed(4) : 'N/A'}
                    </p>
                  </div>
                </div>
              </div>
            )}

            {/* Predictions */}
            {results.predictions && results.predictions.length > 0 && (
              <div className="bg-yellow-50 rounded-lg p-6">
                <h2 className="text-xl font-semibold mb-4 text-yellow-900">
                  Predictions
                </h2>
                <div className="bg-white rounded-md overflow-hidden">
                  <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Sample
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Predicted Value
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Actual Value
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Error
                        </th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {results.predictions.slice(0, 10).map((prediction, index) => (
                        <tr key={index}>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {index + 1}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {prediction.predicted?.toFixed(4) || 'N/A'}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {prediction.actual?.toFixed(4) || 'N/A'}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                            {prediction.error ? Math.abs(prediction.error).toFixed(4) : 'N/A'}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
                {results.predictions.length > 10 && (
                  <p className="mt-4 text-sm text-gray-600">
                    Showing first 10 predictions of {results.predictions.length} total
                  </p>
                )}
              </div>
            )}

            {/* Error Analysis */}
            {results.errorAnalysis && (
              <div className="bg-red-50 rounded-lg p-6">
                <h2 className="text-xl font-semibold mb-4 text-red-900">
                  Error Analysis
                </h2>
                <div className="grid md:grid-cols-3 gap-4">
                  <div className="bg-white p-4 rounded-md">
                    <h3 className="font-medium text-gray-700 mb-2">Mean Squared Error</h3>
                    <p className="text-2xl font-bold text-red-600">
                      {results.errorAnalysis.mse ? results.errorAnalysis.mse.toFixed(6) : 'N/A'}
                    </p>
                  </div>
                  <div className="bg-white p-4 rounded-md">
                    <h3 className="font-medium text-gray-700 mb-2">Mean Absolute Error</h3>
                    <p className="text-2xl font-bold text-red-600">
                      {results.errorAnalysis.mae ? results.errorAnalysis.mae.toFixed(6) : 'N/A'}
                    </p>
                  </div>
                  <div className="bg-white p-4 rounded-md">
                    <h3 className="font-medium text-gray-700 mb-2">R² Score</h3>
                    <p className="text-2xl font-bold text-red-600">
                      {results.errorAnalysis.r2 ? results.errorAnalysis.r2.toFixed(4) : 'N/A'}
                    </p>
                  </div>
                </div>
              </div>
            )}

            {/* Download Results */}
            <div className="bg-gray-50 rounded-lg p-6">
              <h2 className="text-xl font-semibold mb-4 text-gray-900">
                Download Results
              </h2>
              <div className="flex space-x-4">
                <button className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors">
                  Download Results (CSV)
                </button>
                <button className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors">
                  Download Model
                </button>
                <button className="px-4 py-2 bg-purple-600 text-white rounded-md hover:bg-purple-700 transition-colors">
                  Download Report (PDF)
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default NeuralNetworkResults; 