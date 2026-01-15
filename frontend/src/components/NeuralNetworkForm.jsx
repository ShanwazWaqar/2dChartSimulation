import React, { useState } from 'react';
import { Play, RotateCcw, Activity, BrainCircuit } from 'lucide-react';
import NeuralNetworkResults from './NeuralNetworkResults';
import StatusModal from './StatusModal';

const NeuralNetworkForm = () => {
  const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
  const [isLoading, setIsLoading] = useState(false);
  const [results, setResults] = useState(null);

  // Modal State
  const [modalState, setModalState] = useState({
    isOpen: false,
    type: 'info',
    title: '',
    message: ''
  });

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

  const showModal = (type, title, message) => {
    setModalState({ isOpen: true, type, title, message });
  };

  const closeModal = () => {
    setModalState(prev => ({ ...prev, isOpen: false }));
  };

  const handleInputChange = (e) => {
    const { name, value, type, files } = e.target;
    if (type === 'file') {
      setFormData({ ...formData, [name]: files[0] });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.radionet === 'train') {
      if (!formData.trainfile) {
        showModal('error', 'Missing File', "Please upload a training data file (e.g., sample_traindata_3x1.txt).");
        return;
      }
      if (formData.inNodes < 1 || formData.outNodes < 1 || formData.noLayers < 2) {
        showModal('error', 'Invalid Parameters', "Please ensure architecture parameters are valid (Inputs > 0, Outputs > 0, Layers >= 2).");
        return;
      }
    } else {
      if (!formData.testfile) {
        showModal('error', 'Missing File', "Please upload a testing data file (e.g., sample_testdata_3x1.txt).");
        return;
      }
    }

    setIsLoading(true);

    try {
      const formDataToSend = new FormData();
      formDataToSend.append('inNodes', formData.inNodes);
      formDataToSend.append('outNodes', formData.outNodes);
      formDataToSend.append('noLayers', formData.noLayers);
      formDataToSend.append('noFirstLayer', formData.noFirstLayer);
      formDataToSend.append('noSecLayer', formData.noSecLayer);
      formDataToSend.append('noTrainData', formData.notraindata);
      formDataToSend.append('noTestData', formData.notestdata);
      formDataToSend.append('serverPath', 'output/');

      const endpoint = formData.radionet === 'train' ? '/train' : '/test';

      if (formData.radionet === 'train') {
        if (formData.trainfile) {
          formDataToSend.append('trainFile', formData.trainfile);
          formDataToSend.append('trainFileName', formData.trainfile.name);
        } else {
          formDataToSend.append('trainFileName', 'default_train.txt');
        }
      } else {
        if (formData.testfile) {
          formDataToSend.append('testFile', formData.testfile);
          formDataToSend.append('testFileName', formData.testfile.name);
        } else {
          formDataToSend.append('testFileName', 'default_test.txt');
        }
      }

      const response = await fetch(`${API_URL}/api/neural-network${endpoint}`, {
        method: 'POST',
        body: formDataToSend
      });

      if (response.ok) {
        const result = await response.json();
        setResults(result);
        // showModal('success', 'Analysis Complete', 'Neural network analysis finished successfully.'); 
        // Optional: Don't show success modal, just show results. Or show modal then results. 
        // Let's rely on results appearing for success feedback to be less intrusive, 
        // or a small toast. But user asked for popups. 
        // Let's sticky with results view appearing as the primary feedback, 
        // as the switch happens automatically.
      } else {
        throw new Error('Network response was not ok');
      }
    } catch (error) {
      console.error('Error:', error);
      showModal('error', 'Processing Error', 'An error occurred while processing the neural network. Please check the backend connection.');
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
    setResults(null);
  };

  return (
    <div className="w-full px-6 font-sans">
      <StatusModal
        isOpen={modalState.isOpen}
        onClose={closeModal}
        type={modalState.type}
        title={modalState.title}
        message={modalState.message}
      />

      <div className="mb-8 border-b border-gray-200 pb-4">
        <h1 className="text-3xl font-bold text-gray-900 border-l-8 border-uga-red pl-4">Neural Network Analysis</h1>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
        <div className="lg:col-span-5 space-y-8 animate-in slide-in-from-left-4 duration-500">
          <form onSubmit={handleSubmit} className="space-y-6">
            <section className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
              <div className="bg-gray-50 px-6 py-4 border-b border-gray-200">
                <h3 className="text-lg font-bold text-gray-900 flex items-center">
                  <BrainCircuit className="w-5 h-5 text-uga-red mr-2" />
                  Configuration Mode
                </h3>
              </div>
              <div className="p-6">
                <div className="flex flex-col gap-4">
                  <label className={`flex items-center p-4 rounded-lg border-2 cursor-pointer transition-all duration-300 ${formData.radionet === 'train' ? 'border-uga-red bg-red-50' : 'border-gray-200 hover:border-gray-300 bg-white'}`}>
                    <input type="radio" name="radionet" value="train" checked={formData.radionet === 'train'} onChange={handleInputChange} className="sr-only" />
                    <div className="flex-1">
                      <div className="flex items-center mb-1">
                        <BrainCircuit className={`w-5 h-5 mr-2 ${formData.radionet === 'train' ? 'text-uga-red' : 'text-gray-400'}`} />
                        <span className={`font-bold ${formData.radionet === 'train' ? 'text-uga-red' : 'text-gray-700'}`}>Train Network</span>
                      </div>
                      <p className="text-xs text-gray-500 ml-7">Train a new model with specific architecture.</p>
                    </div>
                  </label>
                  <label className={`flex items-center p-4 rounded-lg border-2 cursor-pointer transition-all duration-300 ${formData.radionet === 'test' ? 'border-uga-red bg-red-50' : 'border-gray-200 hover:border-gray-300 bg-white'}`}>
                    <input type="radio" name="radionet" value="test" checked={formData.radionet === 'test'} onChange={handleInputChange} className="sr-only" />
                    <div className="flex-1">
                      <div className="flex items-center mb-1">
                        <Activity className={`w-5 h-5 mr-2 ${formData.radionet === 'test' ? 'text-uga-red' : 'text-gray-400'}`} />
                        <span className={`font-bold ${formData.radionet === 'test' ? 'text-uga-red' : 'text-gray-700'}`}>Run Test Verification</span>
                      </div>
                      <p className="text-xs text-gray-500 ml-7">Validate existing model against test data.</p>
                    </div>
                  </label>
                </div>
              </div>
            </section>

            <div className="relative">
              <div className={`transition-all duration-500 ease-in-out ${formData.radionet === 'train' ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-4 absolute top-0 left-0 w-full pointer-events-none'}`}>
                <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 space-y-6">
                  <h3 className="text-sm font-bold text-gray-900 uppercase border-b border-gray-100 pb-2">Network Architecture</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label htmlFor="inNodes" className="block text-xs font-semibold text-gray-500 mb-1">Inputs</label>
                      <input id="inNodes" type="number" name="inNodes" value={formData.inNodes} onChange={handleInputChange} min="1" className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-uga-red focus:border-uga-red outline-none" />
                    </div>
                    <div>
                      <label htmlFor="outNodes" className="block text-xs font-semibold text-gray-500 mb-1">Outputs</label>
                      <input id="outNodes" type="number" name="outNodes" value={formData.outNodes} onChange={handleInputChange} min="1" className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-uga-red focus:border-uga-red outline-none" />
                    </div>
                    <div className="col-span-2">
                      <label htmlFor="noLayers" className="block text-xs font-semibold text-gray-500 mb-1">Hidden Layers</label>
                      <input id="noLayers" type="number" name="noLayers" value={formData.noLayers} onChange={handleInputChange} min="2" className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-uga-red focus:border-uga-red outline-none" />
                    </div>
                    <div>
                      <label htmlFor="noFirstLayer" className="block text-xs font-semibold text-gray-500 mb-1">Layer 1 Nodes</label>
                      <input id="noFirstLayer" type="number" name="noFirstLayer" value={formData.noFirstLayer} onChange={handleInputChange} min="1" className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-uga-red focus:border-uga-red outline-none" />
                    </div>
                    <div>
                      <label htmlFor="noSecLayer" className="block text-xs font-semibold text-gray-500 mb-1">Layer 2 Nodes</label>
                      <input id="noSecLayer" type="number" name="noSecLayer" value={formData.noSecLayer} onChange={handleInputChange} min="1" className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-uga-red focus:border-uga-red outline-none" />
                    </div>
                  </div>
                  <div>
                    <h3 className="text-sm font-bold text-gray-900 uppercase border-b border-gray-100 pb-2 mb-4">Training Data</h3>
                    <div className="space-y-4">
                      <div>
                        <label htmlFor="notraindata" className="block text-xs font-semibold text-gray-500 mb-1">Sample Count</label>
                        <input id="notraindata" type="number" name="notraindata" value={formData.notraindata} onChange={handleInputChange} min="1" className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-uga-red focus:border-uga-red outline-none" />
                      </div>
                      <div>
                        <label htmlFor="trainfile" className="block text-xs font-semibold text-gray-500 mb-1">Upload File</label>
                        <input id="trainfile" type="file" name="trainfile" onChange={handleInputChange} accept=".txt,.csv" className="w-full text-xs file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:bg-gray-100 file:text-gray-700 hover:file:bg-gray-200 transition-colors" />
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div className={`transition-all duration-500 ease-in-out ${formData.radionet === 'test' ? 'opacity-100 translate-x-0' : 'opacity-0 translate-x-4 absolute top-0 left-0 w-full pointer-events-none'}`}>
                <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 space-y-6">
                  <h3 className="text-sm font-bold text-gray-900 uppercase border-b border-gray-100 pb-2">Testing Parameters</h3>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-xs font-semibold text-gray-500 mb-1">Sample Count</label>
                      <input type="number" name="notestdata" value={formData.notestdata} onChange={handleInputChange} min="1" className="w-full px-3 py-2 border rounded focus:ring-2 focus:ring-uga-red focus:border-uga-red outline-none" />
                    </div>
                    <div>
                      <label className="block text-xs font-semibold text-gray-500 mb-1">Upload Test File</label>
                      <input type="file" name="testfile" onChange={handleInputChange} accept=".txt,.csv" className="w-full text-xs file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:bg-gray-100 file:text-gray-700 hover:file:bg-gray-200 transition-colors" />
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="flex gap-4 pt-4">
              <button type="button" onClick={handleReset} className="flex-1 px-4 py-3 bg-white text-gray-700 font-bold rounded-lg border-2 border-gray-200 hover:border-gray-300 hover:bg-gray-50 transition-all">
                Reset
              </button>
              <button type="submit" disabled={isLoading} className="flex-[2] px-4 py-3 bg-uga-red text-white font-bold rounded-lg hover:bg-red-800 transition-all shadow-md hover:shadow-lg flex justify-center items-center">
                {isLoading ? 'Processing...' : (
                  <>
                    <Play className="w-5 h-5 mr-2" />
                    {formData.radionet === 'train' ? 'Start Training' : 'Run Verification'}
                  </>
                )}
              </button>
            </div>
          </form>
        </div>

        <div className="lg:col-span-7 h-full min-h-[500px]">
          <NeuralNetworkResults results={results} />
        </div>
      </div>
    </div>
  );
};

export default NeuralNetworkForm;