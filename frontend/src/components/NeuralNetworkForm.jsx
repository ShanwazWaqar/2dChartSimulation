import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

// The backend URL is configured via the REACT_APP_API_URL environment variable in the .env file at the project root.
const NeuralNetworkForm = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    inNodes: 3,
    outNodes: 1,
    noLayers: 3,
    noFirstLayer: 15,
    noSecLayer: 4,
    notraindata: 37,
    notestdata: 100,
    radionet: 'train',
    trainfile: null,
    testfile: null
  });
  const [isLoading, setIsLoading] = useState(false);

  const API_URL = process.env.REACT_APP_API_URL;

  const handleInputChange = (e) => {
    const { name, value, type, files } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'file' ? files[0] : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const formDataToSend = new FormData();
      Object.keys(formData).forEach(key => {
        if (formData[key] !== null) {
          formDataToSend.append(key, formData[key]);
        }
      });

      const response = await fetch(`${API_URL}/api/neural-network`, {
        method: 'POST',
        body: formDataToSend
      });

      if (response.ok) {
        const result = await response.json();
        navigate('/neural-network-results', { state: { results: result } });
      } else {
        throw new Error('Network response was not ok');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('An error occurred while processing the neural network. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleReset = () => {
    setFormData({
      inNodes: 3,
      outNodes: 1,
      noLayers: 3,
      noFirstLayer: 15,
      noSecLayer: 4,
      notraindata: 37,
      notestdata: 100,
      radionet: 'train',
      trainfile: null,
      testfile: null
    });
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white rounded-lg shadow-md p-8">
        <h1 className="text-3xl font-bold text-center mb-8 text-gray-900">
          Neural Network Analysis
        </h1>

        <form onSubmit={handleSubmit} className="space-y-8">
          {/* Training Section */}
          <div className="bg-blue-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold mb-4 text-blue-900">
              🧠 Train the Neural Network
            </h2>
            
            <div className="grid md:grid-cols-2 gap-6">
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Number of Input Nodes
                  </label>
                  <input
                    type="number"
                    name="inNodes"
                    value={formData.inNodes}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    min="1"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Number of Output Nodes
                  </label>
                  <input
                    type="number"
                    name="outNodes"
                    value={formData.outNodes}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    min="1"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Number of Layers
                  </label>
                  <input
                    type="number"
                    name="noLayers"
                    value={formData.noLayers}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    min="2"
                  />
                </div>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Nodes in First Hidden Layer
                  </label>
                  <input
                    type="number"
                    name="noFirstLayer"
                    value={formData.noFirstLayer}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    min="1"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Nodes in Second Hidden Layer
                  </label>
                  <input
                    type="number"
                    name="noSecLayer"
                    value={formData.noSecLayer}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    min="1"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Number of Training Data
                  </label>
                  <input
                    type="number"
                    name="notraindata"
                    value={formData.notraindata}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    min="1"
                  />
                </div>
              </div>
            </div>

            <div className="mt-6">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Training Data File
              </label>
              <input
                type="file"
                name="trainfile"
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                accept=".txt,.csv"
              />
            </div>
          </div>

          {/* Testing Section */}
          <div className="bg-green-50 rounded-lg p-6">
            <h2 className="text-xl font-semibold mb-4 text-green-900">
              🧪 Test the Neural Network
            </h2>
            
            <div className="grid md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Number of Testing Data
                </label>
                <input
                  type="number"
                  name="notestdata"
                  value={formData.notestdata}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  min="1"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Testing Data File
                </label>
                <input
                  type="file"
                  name="testfile"
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  accept=".txt,.csv"
                />
              </div>
            </div>
          </div>

          {/* Network Option */}
          <div className="bg-gray-50 rounded-lg p-6">
            <h3 className="text-lg font-semibold mb-4 text-gray-900">
              Select Neural Network Operation
            </h3>
            <div className="space-y-3">
              <label className="flex items-center">
                <input
                  type="radio"
                  name="radionet"
                  value="train"
                  checked={formData.radionet === 'train'}
                  onChange={handleInputChange}
                  className="mr-3 text-blue-600 focus:ring-blue-500"
                />
                <span className="text-gray-700">Train the neural network</span>
              </label>
              <label className="flex items-center">
                <input
                  type="radio"
                  name="radionet"
                  value="test"
                  checked={formData.radionet === 'test'}
                  onChange={handleInputChange}
                  className="mr-3 text-blue-600 focus:ring-blue-500"
                />
                <span className="text-gray-700">Test the neural network</span>
              </label>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex justify-center space-x-4">
            <button
              type="submit"
              disabled={isLoading}
              className="px-6 py-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isLoading ? 'Processing...' : 'Submit'}
            </button>
            <button
              type="button"
              onClick={handleReset}
              className="px-6 py-3 bg-gray-600 text-white rounded-md hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500"
            >
              Reset
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default NeuralNetworkForm; 