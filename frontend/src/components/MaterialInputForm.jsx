// src/components/MaterialInputForm.jsx

import React, { useState } from 'react';

const MaterialInputForm = ({ onChange }) => {
  const [formData, setFormData] = useState({
    rows: 100,
    cols: 100,
    iterations: 10,
    scale: 5
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    const updated = { ...formData, [name]: parseInt(value) };
    setFormData(updated);
    if (onChange) onChange(updated); // Send back to parent
  };

  return (
    <div className="bg-white p-8 rounded-2xl shadow-lg border border-uga-gray-200 space-y-6">
      <h2 className="text-2xl font-bold text-uga-black flex items-center">
        <svg className="w-7 h-7 mr-3 text-uga-red" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" />
        </svg>
        Material Input
      </h2>

      <div className="grid md:grid-cols-2 gap-6">
        {[
          { label: "Number of Rows", name: "rows" },
          { label: "Number of Columns", name: "cols" },
          { label: "Iterations", name: "iterations" },
          { label: "Scale", name: "scale" },
        ].map(({ label, name }) => (
          <div key={name}>
            <label className="block text-sm font-semibold text-uga-gray-700 mb-2">{label}</label>
            <input
              type="number"
              name={name}
              value={formData[name]}
              onChange={handleChange}
              className="w-full px-4 py-3 border border-uga-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-uga-red focus:border-uga-red transition-colors duration-200"
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default MaterialInputForm;
