import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import NeuralNetworkResults from '../NeuralNetworkResults';

describe('NeuralNetworkResults Component', () => {
    const mockResults = {
        inNodes: 3,
        outNodes: 1,
        noLayers: 3,
        trainingResults: {
            loss: 0.05,
            accuracy: 0.95,
            iterations: 1000
        },
        testingResults: {
            loss: 0.08,
            accuracy: 0.92
        },
        errorAnalysis: {
            mse: 0.08,
            mae: 0.04
        },
        predictions: [
            { predicted: 0.85, actual: 0.80, error: 0.05 },
            { predicted: 0.15, actual: 0.10, error: 0.05 }
        ]
    };

    test('renders empty state when no results provided', () => {
        render(<NeuralNetworkResults results={null} />);
        expect(screen.getByText(/Ready to Analyze/i)).toBeInTheDocument();
    });

    test('renders training results correctly', () => {
        render(<NeuralNetworkResults results={mockResults} />);

        expect(screen.getByText(/Training Performance/i)).toBeInTheDocument();
        expect(screen.getByText(/95\.00%/)).toBeInTheDocument();
        // Loss 0.05 appears in training loss and prediction error. Use getAllByText.
        expect(screen.getAllByText(/0\.0500/).length).toBeGreaterThan(0);
    });

    test('renders predictions table correctly', () => {
        render(<NeuralNetworkResults results={mockResults} />);

        // Check for table headers
        expect(screen.getByText(/^Pred$/i)).toBeInTheDocument();
        expect(screen.getByText(/^Act$/i)).toBeInTheDocument();
        expect(screen.getByText(/^Err$/i)).toBeInTheDocument();

        expect(screen.getByText(/0\.8500/)).toBeInTheDocument();
        expect(screen.getByText(/0\.8000/)).toBeInTheDocument();
    });

    test('renders architecture summary', () => {
        render(<NeuralNetworkResults results={mockResults} />);
        // Title is "Configuration"
        expect(screen.getByText(/Configuration/i)).toBeInTheDocument();
        // Expect 3 Inputs, 1 Output (Multiple occurrences, look for > 0)
        expect(screen.getAllByText(/^3$/).length).toBeGreaterThan(0);
        expect(screen.getByText(/^1$/)).toBeInTheDocument();
    });
});
