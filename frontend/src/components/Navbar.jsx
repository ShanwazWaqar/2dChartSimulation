import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import {
    Home,
    Activity,
    Microscope,
    Database,
    ChevronDown,
    Menu,
    X
} from 'lucide-react';

const Navbar = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [simDropdownOpen, setSimDropdownOpen] = useState(false);
    const [analysisDropdownOpen, setAnalysisDropdownOpen] = useState(false);
    const location = useLocation();

    const isActive = (path) => location.pathname === path;

    return (
        <nav className="bg-white border-b border-gray-200 shadow-sm sticky top-0 z-50">
            <div className="w-full max-w-[1920px] mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    <div className="flex">
                        <Link to="/" className="flex-shrink-0 flex items-center">
                            <span className="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-uga-red to-red-800">
                                CorrosionSim
                            </span>
                        </Link>
                    </div>

                    {/* Desktop Menu */}
                    <div className="hidden md:ml-6 md:flex md:space-x-4 items-center">
                        <Link
                            to="/"
                            className={`inline-flex items-center px-3 py-2 text-sm font-medium transition-colors ${isActive('/') ? 'text-uga-red' : 'text-gray-600 hover:text-uga-red'}`}
                        >
                            <Home className="w-4 h-4 mr-2" />
                            Home
                        </Link>

                        {/* Simulation Dropdown */}
                        <div
                            className="relative"
                            onMouseEnter={() => setSimDropdownOpen(true)}
                            onMouseLeave={() => setSimDropdownOpen(false)}
                        >
                            <button
                                className={`inline-flex items-center px-3 py-2 text-sm font-medium transition-colors ${location.pathname.includes('/sim') ? 'text-uga-red' : 'text-gray-700 hover:text-uga-red'}`}
                                aria-expanded={simDropdownOpen}
                                aria-haspopup="true"
                            >
                                <Activity className="w-4 h-4 mr-2" aria-hidden="true" />
                                Simulation
                                <ChevronDown className="w-4 h-4 ml-1" aria-hidden="true" />
                            </button>

                            {simDropdownOpen && (
                                <div className="absolute left-0 mt-0 w-64 bg-white rounded-md shadow-lg ring-1 ring-black ring-opacity-5 py-1 z-50 animate-in fade-in slide-in-from-top-2 duration-200" role="menu">
                                    <div className="px-4 py-2 border-b border-gray-100 bg-gray-50 text-xs font-semibold text-gray-500 uppercase tracking-wider">
                                        Experiments
                                    </div>
                                    <Link to="/simulate" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-uga-red" role="menuitem">
                                        Single Pit Growth (2D)
                                    </Link>
                                    <Link to="/sim2d" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-uga-red" role="menuitem">
                                        Multi Pit Growth (2D)
                                    </Link>
                                    <div className="px-4 py-2 border-b border-gray-100 bg-gray-50 text-xs font-semibold text-gray-500 uppercase tracking-wider mt-1">
                                        Configuration
                                    </div>
                                    <Link to="/constants" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-uga-red" role="menuitem">
                                        Material Constants
                                    </Link>
                                </div>
                            )}
                        </div>

                        {/* Analysis Dropdown */}
                        <div
                            className="relative"
                            onMouseEnter={() => setAnalysisDropdownOpen(true)}
                            onMouseLeave={() => setAnalysisDropdownOpen(false)}
                            onFocus={() => setAnalysisDropdownOpen(true)}
                            onBlur={() => setAnalysisDropdownOpen(false)}
                        >
                            <button
                                className={`inline-flex items-center px-3 py-2 text-sm font-medium transition-colors ${location.pathname.includes('analysis') ? 'text-uga-red' : 'text-gray-700 hover:text-uga-red'}`}
                                aria-expanded={analysisDropdownOpen}
                                aria-haspopup="true"
                            >
                                <Microscope className="w-4 h-4 mr-2" aria-hidden="true" />
                                Analysis
                                <ChevronDown className="w-4 h-4 ml-1" aria-hidden="true" />
                            </button>

                            {/* ... existing code ... */}

                            {analysisDropdownOpen && (
                                <div className="absolute right-0 mt-0 w-64 bg-white rounded-md shadow-lg ring-1 ring-black ring-opacity-5 py-1 z-50 animate-in fade-in slide-in-from-top-2 duration-200" role="menu">
                                    <Link to="/analysis" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-uga-red" role="menuitem">
                                        Corrosion Image Analysis
                                    </Link>
                                    <Link to="/neural-network" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-uga-red" role="menuitem">
                                        Neural Network Analysis
                                    </Link>
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Mobile menu button */}
                    <div className="flex items-center md:hidden">
                        <button
                            onClick={() => setIsOpen(!isOpen)}
                            className="inline-flex items-center justify-center p-2 rounded-md text-gray-500 hover:text-gray-700 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-uga-red"
                            aria-expanded={isOpen}
                            aria-label="Main menu"
                        >
                            {isOpen ? <X className="block h-6 w-6" aria-hidden="true" /> : <Menu className="block h-6 w-6" aria-hidden="true" />}
                        </button>
                    </div>
                </div>
            </div>

            {/* Mobile Menu */}
            {isOpen && (
                <div className="md:hidden bg-white border-t border-gray-200">
                    <div className="pt-2 pb-3 space-y-1">
                        <Link to="/" className="block pl-3 pr-4 py-2 border-l-4 border-transparent text-base font-medium text-gray-600 hover:bg-gray-50 hover:border-uga-red hover:text-uga-red">
                            Home
                        </Link>
                        {/* Simulation Link for Mobile - Simplified as expanded menu is complex for this snippet, assuming existing mobile structure or just top level links */}
                        <Link to="/simulate" className="block pl-3 pr-4 py-2 border-l-4 border-transparent text-base font-medium text-gray-600 hover:bg-gray-50 hover:border-uga-red hover:text-uga-red">
                            Simulation
                        </Link>
                        <Link to="/analysis" className="block pl-3 pr-4 py-2 border-l-4 border-transparent text-base font-medium text-gray-600 hover:bg-gray-50 hover:border-uga-red hover:text-uga-red">
                            Analysis
                        </Link>
                    </div>
                </div>
            )}
        </nav>
    );
};

export default Navbar;
