import React from "react";
import { Link } from "react-router-dom";

const Home = () => {
  return (
    <div className="text-center">
      {/* Main Banner Image */}
      <div className="mb-8">
        <img 
          src="/images/sim_home.png" 
          alt="Simulation Home" 
          className="max-w-full h-auto mx-auto rounded-lg shadow-lg"
          onError={(e) => {
            e.target.style.display = 'none';
          }}
        />
      </div>

      {/* Welcome Section */}
      <div className="max-w-4xl mx-auto px-4">
        <h1 className="text-4xl font-bold text-gray-900 mb-6">
          Corrosion Simulation & Analysis Portal
        </h1>
        <p className="text-xl text-gray-600 mb-12 leading-relaxed">
          Advanced web-based simulation system for corrosion analysis using 2D image processing, 
          wavelet analysis, neural networks, and material modeling for comprehensive corrosion prediction.
        </p>

        {/* Feature Cards */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8 mb-12">
          <div className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition-shadow">
            <div className="text-3xl mb-4">🔬</div>
            <h3 className="text-lg font-semibold mb-2">Simulation Tools</h3>
            <p className="text-gray-600 mb-4">2D corrosion pit growth simulation with customizable parameters</p>
            <Link to="/simulate" className="text-blue-600 hover:text-blue-800 font-medium">
              Start Simulation →
            </Link>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition-shadow">
            <div className="text-3xl mb-4">📊</div>
            <h3 className="text-lg font-semibold mb-2">Image Analysis</h3>
            <p className="text-gray-600 mb-4">Advanced image processing with wavelet transformation</p>
            <Link to="/analysis" className="text-blue-600 hover:text-blue-800 font-medium">
              Analyze Images →
            </Link>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition-shadow">
            <div className="text-3xl mb-4">🧠</div>
            <h3 className="text-lg font-semibold mb-2">Neural Networks</h3>
            <p className="text-gray-600 mb-4">AI-powered corrosion prediction and analysis</p>
            <Link to="/neural-network" className="text-blue-600 hover:text-blue-800 font-medium">
              Neural Analysis →
            </Link>
          </div>
        </div>

        {/* Quick Navigation */}
        <div className="bg-white rounded-lg shadow-md p-8">
          <h2 className="text-2xl font-bold mb-6 text-gray-900">Quick Navigation</h2>
          <div className="grid md:grid-cols-2 gap-6">
            <div>
              <h3 className="text-lg font-semibold mb-4 text-blue-600">Simulation Applications</h3>
              <ul className="space-y-3 text-left">
                <li>
                  <Link to="/simulate" className="flex items-center text-gray-700 hover:text-blue-600 transition-colors">
                    <span className="mr-2">▶️</span>
                    Corrosion Single Pit Growth Simulation (2D)
                  </Link>
                </li>
                <li>
                  <Link to="/sim2d" className="flex items-center text-gray-700 hover:text-blue-600 transition-colors">
                    <span className="mr-2">▶️</span>
                    Corrosion Multi Pit Growth Simulation (2D)
                  </Link>
                </li>
                <li>
                  <Link to="/constants" className="flex items-center text-gray-700 hover:text-blue-600 transition-colors">
                    <span className="mr-2">▶️</span>
                    Material Constants & Properties
                  </Link>
                </li>
              </ul>
            </div>
            <div>
              <h3 className="text-lg font-semibold mb-4 text-green-600">Analysis Applications</h3>
              <ul className="space-y-3 text-left">
                <li>
                  <Link to="/analysis" className="flex items-center text-gray-700 hover:text-green-600 transition-colors">
                    <span className="mr-2">🔍</span>
                    Corrosion Image Analysis
                  </Link>
                </li>
                <li>
                  <Link to="/neural-network" className="flex items-center text-gray-700 hover:text-green-600 transition-colors">
                    <span className="mr-2">🧠</span>
                    Neural Network Analysis
                  </Link>
                </li>
                <li>
                  <Link to="/wavelet" className="flex items-center text-gray-700 hover:text-green-600 transition-colors">
                    <span className="mr-2">📈</span>
                    Wavelet Data Visualization
                  </Link>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
