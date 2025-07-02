import React, { useState } from "react";

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
    <div className="p-6 max-w-md mx-auto bg-white rounded-xl shadow-md space-y-4">
      <h2 className="text-lg font-semibold">Single File Upload</h2>
      <input type="file" onChange={handleFileChange} className="block w-full" />
      <button
        onClick={handleUpload}
        className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
      >
        Upload
      </button>
      {uploadStatus && <p className="text-sm text-gray-700">{uploadStatus}</p>}
    </div>
  );
}

export default SingleUpload;
