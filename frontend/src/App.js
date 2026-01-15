import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./components/Home";
import SimulationForm from "./components/SimulationForm";
import SimulationResults from "./components/SimulationResults";
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
      <div className="min-h-screen bg-gray-50 font-sans text-gray-900 flex flex-col">
        <Navbar />

        {/* Main Content */}
        <main className="w-full max-w-[1920px] mx-auto px-4 sm:px-6 lg:px-8 py-8 animate-in fade-in duration-500 flex-grow">
          <Routes>
            <Route path="/" element={<Home />} />

            {/* Simulation Routes */}
            <Route path="/simulate" element={<SimulationForm />} />
            <Route path="/sim2d" element={<Sim2DSettings />} />
            <Route path="/results" element={<SimulationResults />} />
            <Route path="/settings" element={<Sim2DSettings />} />
            <Route path="/constants" element={<MaterialConstantsForm />} />
            <Route path="/material" element={<MaterialInputForm />} />
            <Route path="/charts" element={<SimulationChart />} />

            {/* Analysis Routes */}
            <Route path="/analysis" element={<ImageAnalysisUpload />} />
            <Route path="/wavelet" element={<WaveletView />} />
            <Route path="/upload" element={<SingleUpload />} />

            {/* Neural Network Routes */}
            <Route path="/neural-network" element={<NeuralNetworkForm />} />
            <Route path="/neural-network-results" element={<NeuralNetworkResults />} />
          </Routes>
        </main>

        <footer className="bg-white border-t border-gray-200 py-8 mt-12">
          <div className="max-w-7xl mx-auto px-4 text-center text-gray-500 text-sm">
            <p>&copy; {new Date().getFullYear()} CorrosionSim. All rights reserved.</p>
            <p className="mt-2">Department of Engineering • University of Georgia</p>
          </div>
        </footer>
      </div>
    </Router>
  );
}

export default App;
