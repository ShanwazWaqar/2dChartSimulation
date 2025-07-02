// src/components/WaveletView.jsx

import React, { useState } from 'react';
import axios from 'axios';

const WaveletView = () => {
  const [imageFile, setImageFile] = useState(null);
  const [waveletImageUrl, setWaveletImageUrl] = useState('');
  const [loading, setLoading] = useState(false);

  const handleFileChange = (e) => {
    setImageFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!imageFile) return;

    const formData = new FormData();
    formData.append('image', imageFile);

    try {
      setLoading(true);
      const response = await axios.post('/api/image/wavelet-transform', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      const { processedImagePath } = response.data;
      setWaveletImageUrl(`http://localhost:8080/${processedImagePath}`);
    } catch (err) {
      console.error('Wavelet transform failed:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow space-y-4">
      <h2 className="text-xl font-semibold">Wavelet Transform</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="file"
          accept="image/*"
          onChange={handleFileChange}
          className="file-input file-input-bordered w-full"
        />
        <button
          type="submit"
          className="btn btn-primary"
          disabled={loading}
        >
          {loading ? 'Processing...' : 'Transform'}
        </button>
      </form>

      {waveletImageUrl && (
        <div className="mt-4">
          <h3 className="font-semibold">Processed Image:</h3>
          <img src={waveletImageUrl} alt="Wavelet Output" className="mt-2 border rounded max-w-full" />
          <a
            href={waveletImageUrl}
            download
            className="btn btn-outline mt-3"
          >
            Download Image
          </a>
        </div>
      )}
    </div>
  );
};

export default WaveletView;
