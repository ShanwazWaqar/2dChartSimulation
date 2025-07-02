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
    <div className="bg-white p-6 rounded-lg shadow space-y-4">
      <h2 className="text-xl font-semibold">Material Input</h2>

      {[
        { label: "Number of Rows", name: "rows" },
        { label: "Number of Columns", name: "cols" },
        { label: "Iterations", name: "iterations" },
        { label: "Scale", name: "scale" },
      ].map(({ label, name }) => (
        <div key={name}>
          <label className="block font-medium mb-1">{label}</label>
          <input
            type="number"
            name={name}
            value={formData[name]}
            onChange={handleChange}
            className="input input-bordered w-full"
          />
        </div>
      ))}
    </div>
  );
};

export default MaterialInputForm;
