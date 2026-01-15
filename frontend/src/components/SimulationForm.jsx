import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Play, RotateCcw, Activity, Droplets, Zap, Clock, Maximize, AlertCircle } from 'lucide-react';
import SimulationResults from './SimulationResults';

// The backend URL is configured via the REACT_APP_API_URL environment variable in the .env file at the project root.

const SimulationForm = () => {
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
    noIterations: 30 // Number of iterations (Time)
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
    if (formData.stressfact === '' || formData.stressfact === null) newErrors.stressfact = 'Required';
    if (!formData.noIterations || formData.noIterations === '') newErrors.noIterations = 'Required';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    setIsLoading(true);
    setResults(null); // Clear previous results on new run

    try {
      const payload = {
        noRows: formData.noRows,
        noColumns: formData.noColumns,
        phValue: formData.param1,
        temperature: formData.param2,
        potential: formData.param3,
        concentration: formData.param4,
        stressFactor: Math.pow(formData.constant, formData.stressfact),
        iterations: formData.noIterations
      };

      const response = await fetch(`${API_URL}/api/simulate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        const result = await response.json();
        setResults({ results: result, simulationData: payload });
        // navigate('/results', { state: { simulationData: payload, results: result } }); // Removed navigation
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
      noIterations: 30
    });
    setErrors({});
    setResults(null);
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-[1600px] mx-auto px-4 sm:px-6 lg:px-8">

        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 flex items-center">
            <Activity className="w-8 h-8 mr-3 text-uga-red" />
            Single Pit Growth Simulation
          </h1>
          <p className="text-gray-600 mt-2 text-lg">Configure 2D cellular automata parameters and view real-time corrosion results.</p>
        </div>

        <div className="grid lg:grid-cols-2 gap-8 items-start">
          {/* LEFT COLUMN: Input Form */}
          <div className="bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden">
            <div className="bg-gray-50/50 px-8 py-4 border-b border-gray-100 flex justify-between items-center">
              <h2 className="text-lg font-semibold text-gray-800">Configuration</h2>
              <span className="text-xs font-medium px-2.5 py-0.5 rounded bg-blue-100 text-blue-800">Parameters</span>
            </div>

            <form onSubmit={handleSubmit} className="p-6 space-y-8">
              {/* Panel Size */}
              <section>
                <h2 className="text-sm uppercase tracking-wide text-gray-500 font-bold mb-4 flex items-center">
                  <Maximize className="w-4 h-4 mr-2" />
                  Lattice Grid
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
                        placeholder="100"
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
                        placeholder="100"
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
                    <label className="block text-xs font-medium text-gray-700 mb-1">pH Level</label>
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
                    <label className="block text-xs font-medium text-gray-700 mb-1">Temperature (K)</label>
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
                    <label className="block text-xs font-medium text-gray-700 mb-1">Concentration (M)</label>
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

              {/* Stress Parameters */}
              <section>
                <h2 className="text-sm uppercase tracking-wide text-gray-500 font-bold mb-4 flex items-center">
                  <Zap className="w-4 h-4 mr-2" />
                  Mechanics & Time
                </h2>
                <div className="grid grid-cols-1 gap-4">
                  <div className="p-4 bg-slate-50 border border-slate-100 rounded-lg">
                    <div className="flex space-x-4">
                      <div className="flex-1">
                        <label className="block text-xs font-medium text-gray-700 mb-1">Stress Constant (K)</label>
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
                        <label className="block text-xs font-medium text-gray-700 mb-1">Exponent (δ)</label>
                        <input
                          type="number"
                          name="stressfact"
                          value={formData.stressfact}
                          onChange={handleInputChange}
                          step="0.01"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:ring-uga-red focus:border-uga-red"
                        />
                      </div>
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2 flex items-center">
                      <Clock className="w-4 h-4 mr-1 text-gray-500" /> Simulation Iterations
                    </label>
                    <input
                      type="number"
                      name="noIterations"
                      value={formData.noIterations}
                      onChange={handleInputChange}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-uga-red focus:border-uga-red"
                    />
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
                      Running Simulation...
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
                <h3 className="text-xl font-semibold text-gray-700 mb-2">Simulating Corrosion...</h3>
                <p className="text-gray-500 text-center max-w-xs">Processing lattice automata and environmental interactions.</p>
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
                    <span className="text-xs text-gray-500">Execution time: ~1.2s</span>
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
                  Configure the parameters on the left and click "Run Simulation" to see the corrosion growth visualization here.
                </p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div >
  );
};

export default SimulationForm;
