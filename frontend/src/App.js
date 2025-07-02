import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Home from "./components/Home";
import SimulationForm from "./components/SimulationForm";
import SimulationResults from "./components/SimulationResults";
import SimulationFlow from "./components/SimulationFlow";
import Sim2DSettings from "./components/Sim2DSettings";
import ImageAnalysisUpload from "./components/ImageAnalysisUpload";
import WaveletView from "./components/WaveletView";
import MaterialInputForm from "./components/MaterialInputForm";
import MaterialConstantsForm from "./components/MaterialConstantsForm";
import SimulationChart from "./components/SimulationChart";
import SingleUpload from "./components/SingleUpload";
import NeuralNetworkForm from "./components/NeuralNetworkForm";
import NeuralNetworkResults from "./components/NeuralNetworkResults";

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        {/* Header with Banner */}
        <header className="bg-white shadow-sm border-b">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between items-center py-4">
              <div className="flex items-center">
                <img 
                  src="/images/UGABanner.jpg" 
                  alt="UGA Banner" 
                  className="h-12 w-auto"
                  onError={(e) => {
                    e.target.style.display = 'none';
                  }}
                />
              </div>
              <nav className="hidden md:flex space-x-8">
                <Link to="/" className="text-gray-700 hover:text-blue-600 px-3 py-2 text-sm font-medium">
                  Home
                </Link>
                <div className="relative group">
                  <button className="text-gray-700 hover:text-blue-600 px-3 py-2 text-sm font-medium">
                    Applications - Simulation
                  </button>
                  <div className="absolute left-0 mt-2 w-80 bg-white rounded-md shadow-lg opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                    <div className="py-1">
                      <Link to="/simulate" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                        Corrosion Single Pit Growth Simulation (2D)
                      </Link>
                      <Link to="/sim2d" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                        Corrosion Multi Pit Growth Simulation (2D)
                      </Link>
                      <Link to="/constants" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                        Material Constants
                      </Link>
                    </div>
                  </div>
                </div>
                <div className="relative group">
                  <button className="text-gray-700 hover:text-blue-600 px-3 py-2 text-sm font-medium">
                    Applications - Analysis
                  </button>
                  <div className="absolute left-0 mt-2 w-80 bg-white rounded-md shadow-lg opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-50">
                    <div className="py-1">
                      <Link to="/analysis" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                        Corrosion Image Analysis
                      </Link>
                      <Link to="/neural-network" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                        Neural Network Analysis
                      </Link>
                    </div>
                  </div>
                </div>
                <Link to="/approach" className="text-gray-700 hover:text-blue-600 px-3 py-2 text-sm font-medium">
                  Approach
                </Link>
                <Link to="/scope" className="text-gray-700 hover:text-blue-600 px-3 py-2 text-sm font-medium">
                  Scope
                </Link>
                <Link to="/publications" className="text-gray-700 hover:text-blue-600 px-3 py-2 text-sm font-medium">
                  Publications
                </Link>
                <Link to="/images" className="text-gray-700 hover:text-blue-600 px-3 py-2 text-sm font-medium">
                  Images
                </Link>
              </nav>
            </div>
          </div>
        </header>

        {/* Main Content */}
        <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/simulate" element={<SimulationForm />} />
            <Route path="/sim2d" element={<Sim2DSettings />} />
            <Route path="/results" element={<SimulationResults />} />
            <Route path="/settings" element={<Sim2DSettings />} />
            <Route path="/analysis" element={<ImageAnalysisUpload />} />
            <Route path="/wavelet" element={<WaveletView />} />
            <Route path="/material" element={<MaterialInputForm />} />
            <Route path="/constants" element={<MaterialConstantsForm />} />
            <Route path="/charts" element={<SimulationChart />} />
            <Route path="/upload" element={<SingleUpload />} />
            <Route path="/neural-network" element={<NeuralNetworkForm />} />
            <Route path="/neural-network-results" element={<NeuralNetworkResults />} />
            <Route path="/approach" element={<div className="text-center py-20"><h1 className="text-2xl font-bold">Approach</h1><p className="mt-4">Coming soon...</p></div>} />
            <Route path="/scope" element={<div className="text-center py-20"><h1 className="text-2xl font-bold">Scope</h1><p className="mt-4">Coming soon...</p></div>} />
            <Route path="/publications" element={<div className="text-center py-20"><h1 className="text-2xl font-bold">Publications</h1><p className="mt-4">Coming soon...</p></div>} />
            <Route path="/images" element={<div className="text-center py-20"><h1 className="text-2xl font-bold">Images</h1><p className="mt-4">Coming soon...</p></div>} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
