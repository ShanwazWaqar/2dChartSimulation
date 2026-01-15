import React, { useState } from "react";
import { CloudArrowUpIcon } from '@heroicons/react/24/outline';

// The backend URL is configured via the REACT_APP_API_URL environment variable in the .env file at the project root.

function SingleUpload() {
  const [file, setFile] = useState(null);
  const [uploadStatus, setUploadStatus] = useState("");

  const API_URL = process.env.REACT_APP_API_URL;

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      setUploadStatus("Please select a file to upload.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch(`${API_URL}/api/upload`, {
        method: "POST",
        body: formData,
      });
      const data = await response.json();
      if (response.ok) {
        setUploadStatus("File uploaded successfully.");
      } else {
        setUploadStatus(`Upload failed: ${data.message}`);
      }
    } catch (error) {
      setUploadStatus(`Error uploading file: ${error.message}`);
    }
  };

  return (
    <div className="min-h-screen bg-uga-gray-50 py-8">
      <div className="max-w-md mx-auto px-4">
        <div className="bg-white rounded-2xl shadow-lg border border-uga-gray-200 p-8">
          <h2 className="text-2xl font-bold text-uga-black mb-6 text-center">
            Single File Upload
          </h2>
          
          <div className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-uga-black mb-2">
                Select File
              </label>
              <input 
                type="file" 
                onChange={handleFileChange} 
                className="w-full px-4 py-3 border border-uga-gray-300 rounded-lg focus:ring-2 focus:ring-uga-red focus:border-uga-red file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:text-sm file:font-medium file:bg-uga-gray-50 file:text-uga-black hover:file:bg-uga-gray-100"
              />
            </div>
            
            <button
              onClick={handleUpload}
              className="w-full flex items-center justify-center px-6 py-3 bg-uga-red text-white rounded-lg hover:bg-uga-red-dark focus:outline-none focus:ring-2 focus:ring-uga-red font-medium transition-colors"
            >
              <CloudArrowUpIcon className="h-5 w-5 mr-2" />
              Upload File
            </button>
            
            {uploadStatus && (
              <div className={`p-4 rounded-lg text-sm font-medium ${
                uploadStatus.includes('successfully') 
                  ? 'bg-green-50 text-green-800 border border-green-200' 
                  : uploadStatus.includes('Error') || uploadStatus.includes('failed')
                  ? 'bg-red-50 text-red-800 border border-red-200'
                  : 'bg-uga-gray-50 text-uga-gray-700 border border-uga-gray-200'
              }`}>
                {uploadStatus}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default SingleUpload;
