import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import StatusModal from '../StatusModal';

describe('StatusModal Component', () => {
    const mockOnClose = jest.fn();

    beforeEach(() => {
        mockOnClose.mockClear();
    });

    test('does not render when isOpen is false', () => {
        render(
            <StatusModal
                isOpen={false}
                onClose={mockOnClose}
                title="Test Title"
                message="Test Message"
            />
        );
        expect(screen.queryByText('Test Title')).not.toBeInTheDocument();
    });

    test('renders correctly when isOpen is true', () => {
        render(
            <StatusModal
                isOpen={true}
                onClose={mockOnClose}
                title="Test Title"
                message="Test Message"
                type="info"
            />
        );
        expect(screen.getByText('Test Title')).toBeInTheDocument();
        expect(screen.getByText('Test Message')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /continue/i })).toBeInTheDocument();
    });

    test('renders error style correctly', () => {
        render(
            <StatusModal
                isOpen={true}
                onClose={mockOnClose}
                title="Error Occurred"
                message="Something went wrong"
                type="error"
            />
        );
        // Button text changes to "Close" for error type
        expect(screen.getByRole('button', { name: /close/i })).toHaveClass('bg-uga-red');
    });

    test('calls onClose when button is clicked', () => {
        render(
            <StatusModal
                isOpen={true}
                onClose={mockOnClose}
                title="Test"
                message="Message"
            />
        );

        const button = screen.getByRole('button', { name: /continue/i });
        fireEvent.click(button);
        expect(mockOnClose).toHaveBeenCalledTimes(1);
    });

    test('calls onClose when close icon is clicked', () => {
        const { container } = render(
            <StatusModal
                isOpen={true}
                onClose={mockOnClose}
                title="Test"
                message="Message"
            />
        );

        // The X icon button is the first button in the generic structure usually
        // Or we can look for the SVG or the container button class
        // In our component: <button onClick={onClose} className="p-1..."><X .../></button>
        // It is the only button without text content equivalent to "Continue"/"Close"
        // Let's refine by getting all buttons
        const buttons = screen.getAllByRole('button');
        const closeIconBtn = buttons[0]; // The header close button
        fireEvent.click(closeIconBtn);
        expect(mockOnClose).toHaveBeenCalledTimes(1);
    });
});
