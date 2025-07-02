// src/components/MaterialConstantsForm.jsx

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const MaterialConstantsForm = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    pitCoeff: 3.52, // Pitting current coefficient
    freq: 10, // Frequency
    sigma: 100, // Sigma
    apc_final: '', // Crack size from pitting to crack
    af_final: '', // Final crack size
    scoeff: 1.01, // Stress coefficient
    selectedMaterial: ''
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

  const selectMaterial = (materialId) => {
    // Open material selection modal/popup
    window.open(`/material?id=${encodeURIComponent(materialId)}`, 'popuppage',
      'width=500,toolbar=1,resizable=1,scrollbars=yes,height=600,top=20,left=50');
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.pitCoeff || formData.pitCoeff === '') {
      newErrors.pitCoeff = 'Please enter the Pit Coefficient value';
    }
    if (!formData.freq || formData.freq === '') {
      newErrors.freq = 'Please enter the frequency value';
    }
    if (!formData.sigma || formData.sigma === '') {
      newErrors.sigma = 'Please enter the sigma value';
    }
    if (!formData.apc_final || formData.apc_final === '') {
      newErrors.apc_final = 'Please enter the crack size apc value';
    }
    if (!formData.af_final || formData.af_final === '') {
      newErrors.af_final = 'Please enter the final crack size value';
    }
    if (!formData.scoeff || formData.scoeff === '') {
      newErrors.scoeff = 'Please enter the stress coefficient value';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setIsLoading(true);

    try {
      const response = await fetch(`${API_URL}/api/material-constants`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        const result = await response.json();
        alert('Material constants processed successfully!');
        // Navigate to results or reset form
        handleReset();
      } else {
        throw new Error('Processing failed');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('An error occurred while processing the material constants. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleReset = () => {
    setFormData({
      pitCoeff: 3.52,
      freq: 10,
      sigma: 100,
      apc_final: '',
      af_final: '',
      scoeff: 1.01,
      selectedMaterial: ''
    });
    setErrors({});
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-8">
        <h1 className="text-3xl font-bold text-center mb-8 text-gray-900">
          Corrosion Fatigue Life Prediction
        </h1>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Material Selection */}
          <div className="bg-blue-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold mb-4 text-blue-900">
              Material Selection
            </h2>
            <div className="grid md:grid-cols-3 gap-4">
              <button
                type="button"
                onClick={() => selectMaterial('Material 1')}
                className="px-4 py-3 bg-white border-2 border-blue-300 rounded-md hover:border-blue-500 hover:bg-blue-50 transition-colors font-medium"
              >
                Material 1
              </button>
              <button
                type="button"
                onClick={() => selectMaterial('Material 2')}
                className="px-4 py-3 bg-white border-2 border-blue-300 rounded-md hover:border-blue-500 hover:bg-blue-50 transition-colors font-medium"
              >
                Material 2
              </button>
              <button
                type="button"
                onClick={() => selectMaterial('Material 3')}
                className="px-4 py-3 bg-white border-2 border-blue-300 rounded-md hover:border-blue-500 hover:bg-blue-50 transition-colors font-medium"
              >
                Material 3
              </button>
            </div>
            {formData.selectedMaterial && (
              <p className="mt-3 text-sm text-blue-700">
                Selected: <strong>{formData.selectedMaterial}</strong>
              </p>
            )}
          </div>

          {/* Material Parameters */}
          <div className="bg-green-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold mb-4 text-green-900">
              Material Parameters
            </h2>
            <div className="grid md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Pitting current coefficient, I<sub>p0</sub> (C/s)
                </label>
                <div className="flex items-center">
                  <input
                    type="number"
                    name="pitCoeff"
                    value={formData.pitCoeff}
                    onChange={handleInputChange}
                    className={`flex-1 px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 ${
                      errors.pitCoeff ? 'border-red-500' : 'border-gray-300'
                    }`}
                    step="0.01"
                  />
                  <span className="ml-2 text-gray-500">× 10<sup>-2</sup></span>
                </div>
                {errors.pitCoeff && <p className="text-red-500 text-sm mt-1">{errors.pitCoeff}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Frequency, f (Hz)
                </label>
                <input
                  type="number"
                  name="freq"
                  value={formData.freq}
                  onChange={handleInputChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 ${
                    errors.freq ? 'border-red-500' : 'border-gray-300'
                  }`}
                  step="0.1"
                />
                {errors.freq && <p className="text-red-500 text-sm mt-1">{errors.freq}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Sigma, σ
                </label>
                <input
                  type="number"
                  name="sigma"
                  value={formData.sigma}
                  onChange={handleInputChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 ${
                    errors.sigma ? 'border-red-500' : 'border-gray-300'
                  }`}
                  step="0.1"
                />
                {errors.sigma && <p className="text-red-500 text-sm mt-1">{errors.sigma}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Stress Coefficient, C
                </label>
                <input
                  type="number"
                  name="scoeff"
                  value={formData.scoeff}
                  onChange={handleInputChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 ${
                    errors.scoeff ? 'border-red-500' : 'border-gray-300'
                  }`}
                  step="0.01"
                />
                {errors.scoeff && <p className="text-red-500 text-sm mt-1">{errors.scoeff}</p>}
              </div>
            </div>
          </div>

          {/* Crack Size Parameters */}
          <div className="bg-yellow-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold mb-4 text-yellow-900">
              Crack Size Parameters
            </h2>
            <div className="grid md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Crack Size from pitting to crack, a<sub>pc</sub>
                </label>
                <input
                  type="number"
                  name="apc_final"
                  value={formData.apc_final}
                  onChange={handleInputChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500 ${
                    errors.apc_final ? 'border-red-500' : 'border-gray-300'
                  }`}
                  step="0.001"
                />
                {errors.apc_final && <p className="text-red-500 text-sm mt-1">{errors.apc_final}</p>}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Final Crack Size, a<sub>f</sub>
                </label>
                <input
                  type="number"
                  name="af_final"
                  value={formData.af_final}
                  onChange={handleInputChange}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500 ${
                    errors.af_final ? 'border-red-500' : 'border-gray-300'
                  }`}
                  step="0.001"
                />
                {errors.af_final && <p className="text-red-500 text-sm mt-1">{errors.af_final}</p>}
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
              {isLoading ? 'Processing...' : 'Run Model'}
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

export default MaterialConstantsForm;
