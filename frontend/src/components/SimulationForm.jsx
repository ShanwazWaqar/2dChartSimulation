import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

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

  const API_URL = process.env.REACT_APP_API_URL;

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

    if (!formData.noRows || formData.noRows === '') {
      newErrors.noRows = 'Please enter No of rows';
    }
    if (!formData.noColumns || formData.noColumns === '') {
      newErrors.noColumns = 'Please enter No of columns';
    }
    if (!formData.param1 || formData.param1 === '') {
      newErrors.param1 = 'Please enter PH Value';
    }
    if (!formData.param2 || formData.param2 === '') {
      newErrors.param2 = 'Please enter temperature';
    }
    if (!formData.param3 || formData.param3 === '') {
      newErrors.param3 = 'Please enter potential';
    }
    if (!formData.param4 || formData.param4 === '') {
      newErrors.param4 = 'Please enter concentration';
    }
    if (!formData.constant || formData.constant === '') {
      newErrors.constant = 'Please enter the constant value';
    }
    if (formData.stressfact === '' || formData.stressfact === null || formData.stressfact === undefined) {
      newErrors.stressfact = 'Please enter the stress factor';
    }
    if (!formData.noIterations || formData.noIterations === '') {
      newErrors.noIterations = 'Please enter the No of iterations';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (!validateForm()) return;
  
    setIsLoading(true);
  
    try {
      const payload = {
        noRows: formData.noRows,
        noColumns: formData.noColumns,
        phValue: formData.param1,
        temperature: formData.param2,
        potential: formData.param3,
        concentration: formData.param4,
        stressFactor: Math.pow(formData.constant, formData.stressfact), // optional exponentiation
        iterations: formData.noIterations
      };
  
      const response = await fetch(`${API_URL}/api/simulate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
  
      if (response.ok) {
        const result = await response.json();
        navigate('/results', { state: { simulationData: payload, results: result } });
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
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-8">
        <h1 className="text-3xl font-bold text-center mb-8 text-gray-900">
          Corrosion Single Pit Growth Simulation (2D)
        </h1>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Panel Size */}
          <div className="bg-blue-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold mb-4 text-blue-900">
              Panel Size Configuration
            </h2>
            <div className="grid md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Panel Size (Rows × Columns)
                </label>
                <div className="flex items-center space-x-2">
                  <input
                    type="number"
                    name="noRows"
                    value={formData.noRows}
                    onChange={handleInputChange}
                    className={`flex-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                      errors.noRows ? 'border-red-500' : 'border-gray-300'
                    }`}
                    min="1"
                    max="1000"
                  />
                  <span className="text-gray-500">×</span>
                  <input
                    type="number"
                    name="noColumns"
                    value={formData.noColumns}
                    onChange={handleInputChange}
                    className={`flex-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                      errors.noColumns ? 'border-red-500' : 'border-gray-300'
                    }`}
                    min="1"
                    max="1000"
                  />
                </div>
                {errors.noRows && <p className="text-red-500 text-sm mt-1">{errors.noRows}</p>}
                {errors.noColumns && <p className="text-red-500 text-sm mt-1">{errors.noColumns}</p>}
              </div>
            </div>
          </div>

          {/* Chemical Parameters */}
          <div className="bg-green-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold mb-4 text-green-900">
              Chemical Parameters
            </h2>
            <div className="grid md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  pH Value
                </label>
                <input
                  type="number"
                  name="param1"
                  value={formData.param1}
                  onChange={handleInputChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 ${
                    errors.param1 ? 'border-red-500' : 'border-gray-300'
                  }`}
                  step="0.1"
                />
                {errors.param1 && <p className="text-red-500 text-sm mt-1">{errors.param1}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Temperature (°C)
                </label>
                <input
                  type="number"
                  name="param2"
                  value={formData.param2}
                  onChange={handleInputChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 ${
                    errors.param2 ? 'border-red-500' : 'border-gray-300'
                  }`}
                  step="0.1"
                />
                {errors.param2 && <p className="text-red-500 text-sm mt-1">{errors.param2}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Potential (V)
                </label>
                <input
                  type="number"
                  name="param3"
                  value={formData.param3}
                  onChange={handleInputChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 ${
                    errors.param3 ? 'border-red-500' : 'border-gray-300'
                  }`}
                  step="0.01"
                />
                {errors.param3 && <p className="text-red-500 text-sm mt-1">{errors.param3}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Concentration (mol/L)
                </label>
                <input
                  type="number"
                  name="param4"
                  value={formData.param4}
                  onChange={handleInputChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 ${
                    errors.param4 ? 'border-red-500' : 'border-gray-300'
                  }`}
                  step="0.01"
                />
                {errors.param4 && <p className="text-red-500 text-sm mt-1">{errors.param4}</p>}
              </div>
            </div>
          </div>

          {/* Stress Parameters */}
          <div className="bg-yellow-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold mb-4 text-yellow-900">
              Stress Parameters
            </h2>
            <div className="grid md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Stress Factor (Constant ^ δ)
                </label>
                <div className="flex items-center space-x-2">
                  <input
                    type="number"
                    name="constant"
                    value={formData.constant}
                    onChange={handleInputChange}
                    className={`flex-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500 ${
                      errors.constant ? 'border-red-500' : 'border-gray-300'
                    }`}
                    step="0.01"
                  />
                  <span className="text-gray-500">^</span>
                  <input
                    type="number"
                    name="stressfact"
                    value={formData.stressfact}
                    onChange={handleInputChange}
                    className={`flex-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500 ${
                      errors.stressfact ? 'border-red-500' : 'border-gray-300'
                    }`}
                    step="0.01"
                  />
                </div>
                {errors.constant && <p className="text-red-500 text-sm mt-1">{errors.constant}</p>}
                {errors.stressfact && <p className="text-red-500 text-sm mt-1">{errors.stressfact}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Number of Iterations (Time)
                </label>
                <input
                  type="number"
                  name="noIterations"
                  value={formData.noIterations}
                  onChange={handleInputChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500 ${
                    errors.noIterations ? 'border-red-500' : 'border-gray-300'
                  }`}
                  min="1"
                  max="1000"
                />
                {errors.noIterations && <p className="text-red-500 text-sm mt-1">{errors.noIterations}</p>}
              </div>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex justify-center space-x-4">
            <button
              type="submit"
              disabled={isLoading}
              className="px-8 py-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed font-medium"
            >
              {isLoading ? 'Running Simulation...' : 'Run Simulation'}
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
      </div>
    </div>
  );
};

export default SimulationForm;
