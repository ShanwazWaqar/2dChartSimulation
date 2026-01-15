import React from "react";
import { Link } from "react-router-dom";
import { Activity, Microscope, BrainCircuit, ArrowRight, Layers, Zap } from 'lucide-react';

const FeatureCard = ({ icon: Icon, title, description, link, linkText }) => (
  <div className="bg-white p-8 rounded-xl shadow-sm hover:shadow-md transition-all duration-300 border border-gray-100 group">
    <div className="w-14 h-14 bg-uga-red/10 rounded-lg flex items-center justify-center mb-6 group-hover:bg-uga-red/20 transition-colors">
      <Icon className="w-7 h-7 text-uga-red" strokeWidth={1.5} aria-hidden="true" />
    </div>
    <h3 className="text-xl font-bold mb-3 text-gray-900 group-hover:text-uga-red transition-colors">{title}</h3>
    <p className="text-gray-600 mb-6 leading-relaxed text-sm">{description}</p>
    <Link
      to={link}
      className="inline-flex items-center text-uga-red font-semibold text-sm hover:text-red-800 transition-colors"
    >
      {linkText}
      <ArrowRight className="w-4 h-4 ml-2 group-hover:translate-x-1 transition-transform" aria-hidden="true" />
    </Link>
  </div>
);

const QuickLink = ({ to, icon: Icon, text }) => (
  <li>
    <Link
      to={to}
      className="flex items-center p-3 rounded-lg hover:bg-gray-50 transition-colors duration-200 group border border-transparent hover:border-gray-100"
    >
      <div className="w-8 h-8 bg-gray-100 rounded-md flex items-center justify-center mr-3 group-hover:bg-white group-hover:shadow-sm transition-all">
        <Icon className="w-4 h-4 text-gray-500 group-hover:text-uga-red" aria-hidden="true" />
      </div>
      <span className="text-gray-700 group-hover:text-gray-900 font-medium text-sm">
        {text}
      </span>
    </Link>
  </li>
);

const Home = () => {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <div className="bg-white border-b border-gray-200 relative overflow-hidden">
        {/* Abstract shapes for visual interest */}
        <div className="absolute top-0 right-0 -mt-20 -mr-20 w-80 h-80 bg-uga-red/5 rounded-full blur-3xl"></div>
        <div className="absolute bottom-0 left-0 -mb-20 -ml-20 w-80 h-80 bg-blue-50 rounded-full blur-3xl"></div>

        <div className="w-full max-w-[1920px] mx-auto px-6 sm:px-12 py-24 relative z-10 text-center">

          <h1 className="text-5xl md:text-7xl font-extrabold mb-6 text-gray-900 tracking-tight leading-tight">
            Advanced Corrosion <br />
            <span className="text-uga-red bg-clip-text text-transparent bg-gradient-to-r from-uga-red to-red-600">
              Simulation & Analysis
            </span>
          </h1>
          <p className="text-2xl text-gray-600 mb-12 leading-relaxed max-w-4xl mx-auto">
            A comprehensive platform for corrosion pit growth simulation using 2D cellular automata,
            coupled with advanced wavelet image analysis and neural network prediction models.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link
              to="/simulate"
              className="inline-flex items-center justify-center px-8 py-4 bg-uga-red text-white font-medium rounded-xl hover:bg-red-700 transition-colors shadow-lg hover:shadow-xl hover:-translate-y-0.5 duration-200 focus:ring-2 focus:ring-offset-2 focus:ring-uga-red"
            >
              Start Simulation
              <ArrowRight className="w-4 h-4 ml-2" aria-hidden="true" />
            </Link>
            <Link
              to="/analysis"
              className="inline-flex items-center justify-center px-8 py-4 bg-white text-gray-700 border border-gray-200 font-medium rounded-xl hover:bg-gray-50 transition-colors shadow-sm hover:shadow-md hover:-translate-y-0.5 duration-200 focus:ring-2 focus:ring-gray-300"
            >
              Analyze Data
            </Link>
          </div>
        </div>
      </div>

      {/* Main Features Grid */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="grid md:grid-cols-3 gap-8 relative z-20">
          <FeatureCard
            icon={Activity}
            title="Pit Growth"
            description="Simulate single and multi-pit corrosion growth patterns using customizable electrochemical parameters."
            link="/simulate"
            linkText="Run Simulation"
          />
          <FeatureCard
            icon={Microscope}
            title="Image Analysis"
            description="Process and analyze corrosion imagery using advanced wavelet transformations to detect structural anomalies."
            link="/analysis"
            linkText="Start Analysis"
          />
          <FeatureCard
            icon={BrainCircuit}
            title="Neural Prediction"
            description="Leverage trained neural networks to predict corrosion behavior based on historical test data."
            link="/neural-network"
            linkText="Access Models"
          />
        </div>

        {/* Quick Navigation Section - Centered */}
        <div className="mt-16 max-w-4xl mx-auto">
          <div className="bg-white p-8 rounded-xl border border-gray-100 shadow-sm">
            <h2 className="text-2xl font-bold mb-6 text-gray-900 flex items-center justify-center">
              <Layers className="w-6 h-6 mr-3 text-uga-red" aria-hidden="true" />
              Quick Access
            </h2>
            <div className="grid md:grid-cols-2 gap-12">
              <div>
                <h3 className="text-sm font-semibold text-gray-400 uppercase tracking-wider mb-4 border-b pb-2">Simulation Modules</h3>
                <ul className="space-y-2">
                  <QuickLink to="/simulate" icon={Activity} text="Single Pit Growth (2D)" />
                  <QuickLink to="/sim2d" icon={Layers} text="Multi Pit Growth (2D)" />
                  <QuickLink to="/constants" icon={Zap} text="Material Constants" />
                </ul>
              </div>
              <div>
                <h3 className="text-sm font-semibold text-gray-400 uppercase tracking-wider mb-4 border-b pb-2">Data & Analysis</h3>
                <ul className="space-y-2">
                  <QuickLink to="/analysis" icon={Microscope} text="Image Analysis" />
                  <QuickLink to="/neural-network" icon={BrainCircuit} text="Neural Networks" />
                  <QuickLink to="/wavelet" icon={Activity} text="Wavelet Visualization" />
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
