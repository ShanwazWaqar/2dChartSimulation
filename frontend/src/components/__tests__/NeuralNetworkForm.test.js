import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import NeuralNetworkForm from '../NeuralNetworkForm';

// Global Fetch Mock
global.fetch = jest.fn();

describe('NeuralNetworkForm Component', () => {
    beforeEach(() => {
        fetch.mockClear();
    });

    test('renders form with default values', () => {
        render(<NeuralNetworkForm />);
        expect(screen.getByText('Neural Network Analysis')).toBeInTheDocument();
        expect(screen.getByLabelText(/Inputs/i)).toHaveValue(3);
        expect(screen.getByLabelText(/Outputs/i)).toHaveValue(1);
        // Button default text
        expect(screen.getByText('Start Training')).toBeInTheDocument();
    });

    test('shows modal error when training without file', async () => {
        render(<NeuralNetworkForm />);

        // Default is train mode, no file selected
        const submitBtn = screen.getByText('Start Training');

        fireEvent.click(submitBtn);

        // Expect StatusModal to appear
        expect(await screen.findByText('Missing File')).toBeInTheDocument();
        expect(screen.getByText(/Please upload a training data file/i)).toBeInTheDocument();
    });

    test('switches to test mode and updates UI', () => {
        render(<NeuralNetworkForm />);

        // Click on "Run Test Verification" card (Input radio label)
        // There are multiple "Run Test Verification" strings (one in label title, one in button if switched)
        // The radio label has helper text inside, so getByLabelText might be tricky.
        // Let's use getByText for the large bold label
        const testModeLabel = screen.getByText('Run Test Verification');
        fireEvent.click(testModeLabel);

        // Check if submit button text changed
        expect(screen.getByText('Run Verification')).toBeInTheDocument();

        // Check if test file input is required validation
        // Try to submit without file
        fireEvent.click(screen.getByText('Run Verification'));
        expect(screen.getByText('Missing File')).toBeInTheDocument();
    });

    test('submits correctly with valid data', async () => {
        render(<NeuralNetworkForm />);

        // Mock successful API response
        fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({ success: true, trainingResults: { loss: 0.1, accuracy: 0.9 } })
        });

        // Upload a dummy file
        const file = new File(['dummy content'], 'sample_traindata_3x1.txt', { type: 'text/plain' });
        // Label is "Upload File" inside the Training Data section
        const fileInputs = screen.getAllByLabelText(/Upload File/i);
        // The training file input is likely the first one or visible one
        // But since "Upload Test File" is different ("testfile"), "Upload File" should match the train one ("trainfile")
        const trainInput = fileInputs[0];

        fireEvent.change(trainInput, { target: { files: [file] } });

        // Submit
        const submitBtn = screen.getByText('Start Training');
        fireEvent.click(submitBtn);

        // Verify loading state
        expect(screen.getByText('Processing...')).toBeInTheDocument();

        // Verify fetch call
        await waitFor(() => {
            expect(fetch).toHaveBeenCalledTimes(1);
            expect(fetch).toHaveBeenCalledWith(
                expect.stringContaining('/train'),
                expect.objectContaining({ method: 'POST' })
            );
        });
    });
});
