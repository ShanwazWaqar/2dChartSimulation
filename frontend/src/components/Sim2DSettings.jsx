import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Activity, Play, RotateCcw, AlertCircle, Grid, Zap, Droplets, Maximize } from 'lucide-react';
import SimulationResults from './SimulationResults';

// The backend URL is configured via the REACT_APP_API_URL environment variable in the .env file at the project root.
const Sim2DSettings = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    noRows: 100,
    noColumns: 100,
    param1: 3, // pH Value
    param2: 290, // Temperature
    param3: 0.6, // Potential
    param4: 0.2, // Concentration
    constant: 1, // Stress factor constant
    stressfact: 0, // Stress factor exponent
    noIterations: 30, // Number of iterations (Time)
    numPits: 5, // Number of pits for multi-pit simulation
    pitSpacing: 20, // Spacing between pits
    pitSize: 10 // Initial pit size
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [results, setResults] = useState(null);

  const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    // Clear error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.noRows || formData.noRows === '') newErrors.noRows = 'Required';
    if (!formData.noColumns || formData.noColumns === '') newErrors.noColumns = 'Required';
    if (!formData.param1 || formData.param1 === '') newErrors.param1 = 'Required';
    if (!formData.param2 || formData.param2 === '') newErrors.param2 = 'Required';
    if (!formData.param3 || formData.param3 === '') newErrors.param3 = 'Required';
    if (!formData.param4 || formData.param4 === '') newErrors.param4 = 'Required';
    if (!formData.constant || formData.constant === '') newErrors.constant = 'Required';

    // Explicitly allow 0 for stressfact
    if (formData.stressfact === undefined || formData.stressfact === null || formData.stressfact === '') {
      newErrors.stressfact = 'Required';
    }

    if (!formData.noIterations || formData.noIterations === '') newErrors.noIterations = 'Required';
    if (!formData.numPits || formData.numPits === '') newErrors.numPits = 'Required';
    if (!formData.pitSpacing || formData.pitSpacing === '') newErrors.pitSpacing = 'Required';
    if (!formData.pitSize || formData.pitSize === '') newErrors.pitSize = 'Required';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    setIsLoading(true);
    setResults(null);

    try {
      // Construct payload to match backend DTO
      const payload = {
        ...formData,
        stressFact: Number(formData.stressfact), // Map to camelCase expected by backend
        path: 'output/sim2d/' // Add required path to prevent backend NPE
      };

      const response = await fetch(`${API_URL}/api/simulate2d`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        const result = await response.json();

        // Flatten the response for SimulationResults component and fix image path
        // The backend returns a relative path like "/output/sim2d/result.png"
        // We need to prepend the API endpoint to serve it correctly
        const processedImagePath = result.processedImagePath
          ? `${API_URL}${result.processedImagePath.startsWith('/') ? '' : '/'}${result.processedImagePath}`
          : null;

        const formattedResults = {
          ...result,
          corrosionDetected: result.stats?.corrosionDetected ?? true,
          corrosionPercentage: result.stats?.corrosionPercentage ?? 0,
          analysisResult: result.stats?.analysisResult ?? "Multi-pit simulation completed.",
          imagePath: result.imagePath || "", // Original image likely not used/generated here same way
          processedImagePath: processedImagePath
        };

        setResults({ results: formattedResults, simulationData: formData });
      } else {
        throw new Error('Simulation failed');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('An error occurred during simulation. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleReset = () => {
    setFormData({
      noRows: 100,
      noColumns: 100,
      param1: 3,
      param2: 290,
      param3: 0.6,
      param4: 0.2,
      constant: 1,
      stressfact: 0,
      noIterations: 30,
      numPits: 5,
      pitSpacing: 20,
      pitSize: 10
    });
    setErrors({});
    setResults(null);
  };

  return (
    <div className="min-h-screen bg-uga-gray-50 py-8">
      <div className="max-w-[1600px] mx-auto px-4 sm:px-6 lg:px-8">

        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 flex items-center">
            <Grid className="w-8 h-8 mr-3 text-uga-red" />
            Multi-Pit Growth Simulation (2D)
          </h1>
          <p className="text-gray-600 mt-2 text-lg">Simulate multiple corrosion pits interacting across a 2D lattice grid.</p>
        </div>

        <div className="grid lg:grid-cols-2 gap-8 items-start">
          {/* LEFT COLUMN: Input Form */}
          <div className="bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden">
            <div className="bg-gray-50/50 px-8 py-4 border-b border-gray-100 flex justify-between items-center">
              <h2 className="text-lg font-semibold text-gray-800">Configuration</h2>
              <span className="text-xs font-medium px-2.5 py-0.5 rounded bg-blue-100 text-blue-800">Multi-Pit Mode</span>
            </div>

            <form onSubmit={handleSubmit} className="p-6 space-y-8">

              {/* Panel Size */}
              <section>
                <h2 className="text-sm uppercase tracking-wide text-gray-500 font-bold mb-4 flex items-center">
                  <Maximize className="w-4 h-4 mr-2" />
                  Grid Dimensions
                </h2>
                <div className="bg-slate-50 rounded-lg p-5 border border-slate-100 transition-all hover:border-slate-300">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-medium text-gray-700 uppercase mb-1">Rows</label>
                      <input
                        type="number"
                        name="noRows"
                        value={formData.noRows}
                        onChange={handleInputChange}
                        className={`w-full px-3 py-2 bg-white border rounded-md text-sm focus:ring-2 focus:ring-uga-red/20 focus:border-uga-red transition-all ${errors.noRows ? 'border-red-500' : 'border-gray-300'}`}
                      />
                    </div>
                    <div>
                      <label className="block text-xs font-medium text-gray-700 uppercase mb-1">Columns</label>
                      <input
                        type="number"
                        name="noColumns"
                        value={formData.noColumns}
                        onChange={handleInputChange}
                        className={`w-full px-3 py-2 bg-white border rounded-md text-sm focus:ring-2 focus:ring-uga-red/20 focus:border-uga-red transition-all ${errors.noColumns ? 'border-red-500' : 'border-gray-300'}`}
                      />
                    </div>
                  </div>
                  {(errors.noRows || errors.noColumns) && <p className="text-red-500 text-xs mt-2 flex items-center"><AlertCircle className="w-3 h-3 mr-1" /> Dimensions required</p>}
                </div>
              </section>

              {/* Chemical Parameters */}
              <section>
                <h2 className="text-sm uppercase tracking-wide text-gray-500 font-bold mb-4 flex items-center">
                  <Droplets className="w-4 h-4 mr-2" />
                  Environment
                </h2>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-medium text-gray-700 mb-1">pH</label>
                    <input
                      type="number"
                      name="param1"
                      value={formData.param1}
                      onChange={handleInputChange}
                      step="0.1"
                      className={`w-full px-3 py-2 border rounded-md text-sm focus:ring-1 focus:ring-uga-red focus:border-uga-red ${errors.param1 ? 'border-red-500' : 'border-gray-300'}`}
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-medium text-gray-700 mb-1">Temp (K)</label>
                    <input
                      type="number"
                      name="param2"
                      value={formData.param2}
                      onChange={handleInputChange}
                      step="0.1"
                      className={`w-full px-3 py-2 border rounded-md text-sm focus:ring-1 focus:ring-uga-red focus:border-uga-red ${errors.param2 ? 'border-red-500' : 'border-gray-300'}`}
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-medium text-gray-700 mb-1">Potential (V)</label>
                    <input
                      type="number"
                      name="param3"
                      value={formData.param3}
                      onChange={handleInputChange}
                      step="0.01"
                      className={`w-full px-3 py-2 border rounded-md text-sm focus:ring-1 focus:ring-uga-red focus:border-uga-red ${errors.param3 ? 'border-red-500' : 'border-gray-300'}`}
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-medium text-gray-700 mb-1">Conc. (M)</label>
                    <input
                      type="number"
                      name="param4"
                      value={formData.param4}
                      onChange={handleInputChange}
                      step="0.01"
                      className={`w-full px-3 py-2 border rounded-md text-sm focus:ring-1 focus:ring-uga-red focus:border-uga-red ${errors.param4 ? 'border-red-500' : 'border-gray-300'}`}
                    />
                  </div>
                </div>
              </section>

              {/* Multi-Pit Config */}
              <section>
                <h2 className="text-sm uppercase tracking-wide text-gray-500 font-bold mb-4 flex items-center">
                  <Grid className="w-4 h-4 mr-2" />
                  Pit Configuration
                </h2>
                <div className="grid grid-cols-3 gap-4 bg-gray-50 p-4 rounded-lg border border-gray-100">
                  <div>
                    <label className="block text-xs font-medium text-gray-700 mb-1">Count</label>
                    <input
                      type="number"
                      name="numPits"
                      value={formData.numPits}
                      onChange={handleInputChange}
                      className="w-full px-3 py-2 border rounded-md text-sm focus:ring-uga-red focus:border-uga-red"
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-medium text-gray-700 mb-1">Spacing (px)</label>
                    <input
                      type="number"
                      name="pitSpacing"
                      value={formData.pitSpacing}
                      onChange={handleInputChange}
                      className="w-full px-3 py-2 border rounded-md text-sm focus:ring-uga-red focus:border-uga-red"
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-medium text-gray-700 mb-1">Size (px)</label>
                    <input
                      type="number"
                      name="pitSize"
                      value={formData.pitSize}
                      onChange={handleInputChange}
                      className="w-full px-3 py-2 border rounded-md text-sm focus:ring-uga-red focus:border-uga-red"
                    />
                  </div>
                </div>
              </section>

              {/* Stress Parameters */}
              <section>
                <h2 className="text-sm uppercase tracking-wide text-gray-500 font-bold mb-4 flex items-center">
                  <Zap className="w-4 h-4 mr-2" />
                  Mechanics
                </h2>
                <div className="grid grid-cols-1 gap-4">
                  <div className="p-4 bg-slate-50 border border-slate-100 rounded-lg">
                    <div className="flex space-x-4">
                      <div className="flex-1">
                        <label className="block text-xs font-medium text-gray-700 mb-1">Stress (K)</label>
                        <input
                          type="number"
                          name="constant"
                          value={formData.constant}
                          onChange={handleInputChange}
                          step="0.01"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-uga-red focus:border-uga-red"
                        />
                      </div>
                      <div className="flex-1">
                        <label className="block text-xs font-medium text-gray-700 mb-1">Exp (δ)</label>
                        <input
                          type="number"
                          name="stressfact"
                          value={formData.stressfact}
                          onChange={handleInputChange}
                          step="0.01"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-uga-red focus:border-uga-red"
                        />
                      </div>
                      <div className="flex-1">
                        <label className="block text-xs font-medium text-gray-700 mb-1">Time (Steps)</label>
                        <input
                          type="number"
                          name="noIterations"
                          value={formData.noIterations}
                          onChange={handleInputChange}
                          className="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-uga-red focus:border-uga-red"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </section>

              {/* Actions */}
              <div className="flex items-center space-x-4 pt-4 border-t border-gray-100">
                <button
                  type="button"
                  onClick={handleReset}
                  className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-uga-red"
                >
                  Reset
                </button>
                <button
                  type="submit"
                  disabled={isLoading}
                  className="flex-1 px-6 py-2 text-sm font-medium text-white bg-uga-red rounded-lg hover:bg-red-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-uga-red disabled:opacity-50 disabled:cursor-wait shadow-md hover:shadow-lg transition-all"
                >
                  {isLoading ? (
                    <span className="flex items-center justify-center">
                      <Activity className="w-4 h-4 mr-2 animate-spin" />
                      Running Multi-Pit Sim...
                    </span>
                  ) : 'Run Simulation'}
                </button>
              </div>
            </form>
          </div>

          {/* RIGHT COLUMN: Results Panel */}
          <div className="flex flex-col h-full">
            {isLoading ? (
              <div className="bg-white rounded-xl shadow-lg border border-gray-200 p-8 h-full min-h-[500px] flex flex-col justify-center items-center animate-pulse">
                <div className="w-16 h-16 bg-gray-200 rounded-full mb-4 animate-bounce"></div>
                <h3 className="text-xl font-semibold text-gray-700 mb-2">Simulating Multi-Pit Growth...</h3>
                <p className="text-gray-500 text-center max-w-xs">Calculating pit interactions and stress factors across the grid.</p>
                <div className="w-full max-w-md h-2 bg-gray-100 rounded-full mt-8 overflow-hidden">
                  <div className="h-full bg-uga-red/50 w-1/2 animate-[shimmer_1s_infinite_linear]"></div>
                </div>
              </div>
            ) : results ? (
              <div className="animate-in fade-in slide-in-from-bottom-4 duration-500">
                {/* Embedded Results Component */}
                <div className="bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden">
                  <div className="bg-gray-50/50 px-6 py-3 border-b border-gray-100 flex justify-between items-center">
                    <span className="text-gray-800 font-medium flex items-center">
                      <Activity className="w-4 h-4 mr-2 text-uga-red" /> Results Ready
                    </span>
                    <span className="text-xs text-gray-500">Multi-Pit Analysis</span>
                  </div>
                  <div className="p-6">
                    <SimulationResults results={results.results} simulationData={results.simulationData} />
                  </div>
                </div>
              </div>
            ) : (
              <div className="bg-white rounded-xl shadow-sm border border-gray-200 border-dashed p-8 h-full min-h-[500px] flex flex-col justify-center items-center text-center">
                <div className="w-20 h-20 bg-gray-50 rounded-full flex items-center justify-center mb-4">
                  <Play className="w-8 h-8 text-gray-300 ml-1" />
                </div>
                <h3 className="text-lg font-medium text-gray-900">Ready to Simulate</h3>
                <p className="text-gray-500 mt-2 max-w-sm">
                  Configure the parameters on the left and click "Run Simulation" to see the multi-pit corrosion growth visualization here.
                </p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Sim2DSettings;
