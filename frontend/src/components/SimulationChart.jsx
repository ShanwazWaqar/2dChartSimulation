import React from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';

const SimulationChart = ({ data, title = "Corrosion Simulation Trend", xKey = "iteration", yKey = "corrosionPercentage" }) => {
  if (!data || !Array.isArray(data) || data.length === 0) {
    return <p className="text-center text-gray-600">No simulation data available to display.</p>;
  }

  return (
    <div className="w-full h-[400px] bg-white rounded-md shadow-md p-4">
      <h2 className="text-xl font-semibold mb-4 text-center">{title}</h2>
      <ResponsiveContainer width="100%" height="90%">
        <LineChart data={data} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey={xKey} label={{ value: 'Iteration', position: 'insideBottom', offset: -5 }} />
          <YAxis label={{ value: 'Corrosion %', angle: -90, position: 'insideLeft' }} />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey={yKey} stroke="#2563eb" strokeWidth={2} dot={{ r: 4 }} activeDot={{ r: 6 }} />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default SimulationChart;
