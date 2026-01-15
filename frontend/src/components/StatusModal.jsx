import React, { useEffect } from 'react';
import { X, CheckCircle, AlertTriangle, Info } from 'lucide-react';

const StatusModal = ({ isOpen, onClose, type = 'info', title, message, autoClose = 0 }) => {
    useEffect(() => {
        if (isOpen && autoClose > 0) {
            const timer = setTimeout(() => {
                onClose();
            }, autoClose);
            return () => clearTimeout(timer);
        }
    }, [isOpen, autoClose, onClose]);

    if (!isOpen) return null;

    const getIcon = () => {
        switch (type) {
            case 'success':
                return <CheckCircle className="w-12 h-12 text-green-500" />;
            case 'error':
                return <AlertTriangle className="w-12 h-12 text-uga-red" />;
            default:
                return <Info className="w-12 h-12 text-blue-500" />;
        }
    };

    const getButtonColor = () => {
        switch (type) {
            case 'success':
                return 'bg-green-600 hover:bg-green-700 focus:ring-green-500';
            case 'error':
                return 'bg-uga-red hover:bg-red-800 focus:ring-uga-red';
            default:
                return 'bg-blue-600 hover:bg-blue-700 focus:ring-blue-500';
        }
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm animate-in fade-in duration-200">
            <div className="bg-white rounded-xl shadow-2xl w-full max-w-md overflow-hidden animate-in zoom-in-95 duration-200 scale-100 transform transition-all">
                {/* Header */}
                <div className="flex justify-end p-2">
                    <button
                        onClick={onClose}
                        className="p-1 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-full transition-colors"
                    >
                        <X className="w-5 h-5" />
                    </button>
                </div>

                {/* Content */}
                <div className="flex flex-col items-center px-6 pb-6 text-center">
                    <div className="mb-4 p-3 bg-gray-50 rounded-full">
                        {getIcon()}
                    </div>
                    <h3 className="text-xl font-bold text-gray-900 mb-2">
                        {title}
                    </h3>
                    <p className="text-gray-600 mb-6 leading-relaxed">
                        {message}
                    </p>

                    <button
                        onClick={onClose}
                        className={`w-full py-3 px-4 text-white font-bold rounded-lg shadow-md transition-all focus:outline-none focus:ring-2 focus:ring-offset-2 ${getButtonColor()}`}
                    >
                        {type === 'error' ? 'Close' : 'Continue'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default StatusModal;
