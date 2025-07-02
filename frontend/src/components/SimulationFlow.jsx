import React, { useState } from 'react';
import SimulationForm from './SimulationForm';
import Sim2DSettings from './Sim2DSettings';
import SimulationResults from './SimulationResults';
import axios from 'axios';

// The backend URL is configured via the REACT_APP_API_URL environment variable in the .env file at the project root.
const SimulationFlow = () => {
  const [formData, setFormData] = useState(null);
  const [settingsData, setSettingsData] = useState(null);
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [step, setStep] = useState(1);
  const [error, setError] = useState(null);

  const API_URL = process.env.REACT_APP_API_URL;

  const handleFormSubmit = (data) => {
    setFormData(data);
    setStep(2);
  };

  const handleSettingsSubmit = async (settings) => {
    setSettingsData(settings);
    setLoading(true);
    setError(null);

    try {
      const response = await axios.post(`${API_URL}/api/simulate-2d`, {
        ...formData,
        ...settings,
      });

      if (response.data.success) {
        setResults(response.data);
        setStep(3);
      } else {
        setError(response.data.message || 'Simulation failed');
      }
    } catch (err) {
      setError(err.message || 'Something went wrong during simulation.');
    } finally {
      setLoading(false);
    }
  };

  const handleBack = () => {
    if (step === 2) {
      setStep(1);
    } else if (step === 3) {
      setStep(2);
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-6 bg-white shadow-lg rounded-md">
      {step === 1 && (
        <SimulationForm onSubmit={handleFormSubmit} />
      )}

      {step === 2 && (
        <Sim2DSettings
          initialData={settingsData}
          onSubmit={handleSettingsSubmit}
          onBack={handleBack}
        />
      )}

      {step === 3 && (
        <SimulationResults
          results={results}
          onBack={handleBack}
        />
      )}

      {loading && (
        <div className="text-center mt-4 text-blue-600 font-medium">Running simulation...</div>
      )}

      {error && (
        <div className="text-center mt-4 text-red-500 font-semibold">{error}</div>
      )}
    </div>
  );
};

export default SimulationFlow;
