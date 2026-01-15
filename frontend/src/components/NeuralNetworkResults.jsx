import React from 'react';
import { motion } from 'framer-motion';
import { Activity, AlertTriangle, FileText, BrainCircuit } from 'lucide-react';

const NeuralNetworkResults = ({ results }) => {
  if (!results || Object.keys(results).length === 0) {
    return (
      <div className="h-full flex flex-col items-center justify-center text-center p-12 bg-gray-50 rounded-lg border-2 border-dashed border-gray-200 text-gray-400">
        <BrainCircuit className="w-16 h-16 mb-4 opacity-20" />
        <p className="text-lg font-medium">Ready to Analyze</p>
        <p className="text-sm">Configure parameters on the left and start the process to see results here.</p>
      </div>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0, x: 20 }}
      animate={{ opacity: 1, x: 0 }}
      transition={{ duration: 0.5 }}
      className="font-sans"
    >
      <div className="bg-white rounded-lg shadow-lg p-6 border-t-8 border-uga-red">
        <div className="flex justify-between items-center mb-6">
          <div>
            <h2 className="text-2xl font-bold text-gray-900 border-l-4 border-uga-red pl-4">
              Analysis Results
            </h2>
            <p className="text-gray-600 text-sm mt-1 pl-4">
              Performance metrics and model output.
            </p>
          </div>
        </div>

        <div className="space-y-6">
          {/* Configuration */}
          <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">
            <div className="px-4 py-3 bg-gray-50 border-b border-gray-200 flex items-center">
              <BrainCircuit className="w-4 h-4 text-gray-700 mr-2" />
              <h3 className="text-sm font-bold text-gray-900 uppercase">
                Configuration
              </h3>
            </div>
            <div className="p-4 grid grid-cols-3 gap-4">
              <div className="bg-gray-50 p-3 rounded-lg border border-gray-100 text-center">
                <span className="block text-xs font-bold text-gray-500 uppercase">Input</span>
                <span className="block text-xl font-bold text-gray-900">{results.inNodes || '-'}</span>
              </div>
              <div className="bg-gray-50 p-3 rounded-lg border border-gray-100 text-center">
                <span className="block text-xs font-bold text-gray-500 uppercase">Output</span>
                <span className="block text-xl font-bold text-gray-900">{results.outNodes || '-'}</span>
              </div>
              <div className="bg-gray-50 p-3 rounded-lg border border-gray-100 text-center">
                <span className="block text-xs font-bold text-gray-500 uppercase">Hidden</span>
                <span className="block text-xl font-bold text-gray-900">{results.noLayers || '-'}</span>
              </div>
            </div>
          </div>

          {/* Training Results */}
          {results.trainingResults && (
            <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden ring-1 ring-uga-red/10">
              <div className="px-4 py-3 bg-uga-red text-white flex items-center justify-between">
                <div className="flex items-center">
                  <Activity className="w-4 h-4 mr-2" />
                  <h3 className="text-sm font-bold uppercase">Training Performance</h3>
                </div>
                {results.trainingResults.iterations && (
                  <span className="text-xs bg-white/20 px-2 py-0.5 rounded text-white font-medium">
                    {results.trainingResults.iterations} Iterations
                  </span>
                )}
              </div>
              <div className="p-4 grid grid-cols-2 gap-4">
                <div className="text-center p-2">
                  <span className="block text-sm font-medium text-gray-500 mb-1">Accuracy</span>
                  <span className="block text-3xl font-extrabold text-uga-red">
                    {results.trainingResults.accuracy ? `${(results.trainingResults.accuracy * 100).toFixed(2)}%` : 'N/A'}
                  </span>
                </div>
                <div className="text-center p-2 border-l border-gray-100">
                  <span className="block text-sm font-medium text-gray-500 mb-1">Loss (MSE)</span>
                  <span className="block text-3xl font-extrabold text-uga-red">
                    {results.trainingResults.loss ? results.trainingResults.loss.toFixed(4) : 'N/A'}
                  </span>
                </div>
              </div>
            </div>
          )}

          {/* Testing Results */}
          {results.testingResults && (
            <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden ring-1 ring-uga-red/10">
              <div className="px-4 py-3 bg-uga-red text-white flex items-center">
                <Activity className="w-4 h-4 mr-2" />
                <h3 className="text-sm font-bold uppercase">
                  Testing Verification
                </h3>
              </div>
              <div className="p-4 grid grid-cols-2 gap-4">
                <div className="text-center p-2">
                  <span className="block text-sm font-medium text-gray-500 mb-1">Accuracy</span>
                  <span className="block text-3xl font-extrabold text-uga-red">
                    {results.testingResults.accuracy ? `${(results.testingResults.accuracy * 100).toFixed(2)}%` : 'N/A'}
                  </span>
                </div>
                <div className="text-center p-2 border-l border-gray-100">
                  <span className="block text-sm font-medium text-gray-500 mb-1">Loss</span>
                  <span className="block text-3xl font-extrabold text-uga-red">
                    {results.testingResults.loss ? results.testingResults.loss.toFixed(4) : 'N/A'}
                  </span>
                </div>
              </div>
            </div>
          )}

          {/* Predictions Table */}
          {results.predictions && results.predictions.length > 0 && (
            <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">
              <div className="px-4 py-3 border-b border-gray-200 bg-gray-50 flex items-center">
                <FileText className="w-4 h-4 text-gray-600 mr-2" />
                <h3 className="text-sm font-bold text-gray-900 uppercase">
                  Predictions
                </h3>
              </div>
              <div className="max-h-64 overflow-y-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-100 sticky top-0">
                    <tr>
                      <th className="px-4 py-2 text-left text-xs font-bold text-gray-500 uppercase">ID</th>
                      <th className="px-4 py-2 text-left text-xs font-bold text-gray-500 uppercase">Pred</th>
                      <th className="px-4 py-2 text-left text-xs font-bold text-gray-500 uppercase">Act</th>
                      <th className="px-4 py-2 text-left text-xs font-bold text-gray-500 uppercase">Err</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {results.predictions.map((prediction, index) => (
                      <tr key={index} className="hover:bg-gray-50 text-xs text-gray-700">
                        <td className="px-4 py-2 font-medium">#{index + 1}</td>
                        <td className="px-4 py-2">{prediction.predicted === undefined ? '-' : prediction.predicted.toFixed(4)}</td>
                        <td className="px-4 py-2">{prediction.actual === undefined ? '-' : prediction.actual.toFixed(4)}</td>
                        <td className="px-4 py-2">
                          <span className={`px-1.5 py-0.5 rounded text-[10px] font-bold ${prediction.error && Math.abs(prediction.error) < 0.1
                              ? 'bg-green-100 text-green-800'
                              : 'bg-red-100 text-red-800'
                            }`}>
                            {prediction.error === undefined ? '-' : Math.abs(prediction.error).toFixed(4)}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Error Analysis */}
          {results.errorAnalysis && (
            <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">
              <div className="px-4 py-3 border-b border-gray-200 bg-gray-50 flex items-center">
                <AlertTriangle className="w-4 h-4 text-uga-red mr-2" />
                <h3 className="text-sm font-bold text-gray-900 uppercase">
                  Error Metrics
                </h3>
              </div>
              <div className="p-4 grid grid-cols-3 gap-2 text-center">
                <div>
                  <span className="block text-[10px] font-bold text-gray-500 uppercase">MSE</span>
                  <span className="block text-lg font-bold text-gray-900">{results.errorAnalysis.mse === undefined ? '-' : results.errorAnalysis.mse.toFixed(4)}</span>
                </div>
                <div>
                  <span className="block text-[10px] font-bold text-gray-500 uppercase">MAE</span>
                  <span className="block text-lg font-bold text-gray-900">{results.errorAnalysis.mae === undefined ? '-' : results.errorAnalysis.mae.toFixed(4)}</span>
                </div>
                <div>
                  <span className="block text-[10px] font-bold text-gray-500 uppercase">R²</span>
                  <span className="block text-lg font-bold text-gray-900">{results.errorAnalysis.r2 === undefined ? '-' : results.errorAnalysis.r2.toFixed(4)}</span>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </motion.div>
  );
};

export default NeuralNetworkResults;