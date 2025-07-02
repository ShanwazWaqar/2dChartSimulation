# Corrosion Simulation Frontend

A modern React-based frontend for the Corrosion Simulation and Analysis Portal, built with React 18, React Router, and Tailwind CSS.

## Features

### 🏠 Home Page
- Professional landing page with feature cards
- Quick navigation to all simulation and analysis tools
- Responsive design with modern UI

### 🔬 Simulation Tools
- **Single Pit Growth Simulation (2D)**: Complete corrosion pit growth simulation with customizable parameters
- **Multi Pit Growth Simulation (2D)**: Advanced multi-pit corrosion simulation with pit configuration
- **Material Constants**: Material properties and fatigue life prediction

### 📊 Analysis Tools
- **Image Analysis**: Upload and analyze corrosion images with wavelet transformation
- **Neural Network Analysis**: AI-powered corrosion prediction with training and testing capabilities
- **Wavelet Data Visualization**: Advanced data visualization for analysis results

## Components

### Core Components
- `App.js` - Main application with navigation and routing
- `Home.jsx` - Landing page with feature overview
- `SimulationForm.jsx` - Single pit growth simulation form
- `Sim2DSettings.jsx` - Multi-pit growth simulation form
- `MaterialConstantsForm.jsx` - Material properties and constants form
- `ImageAnalysisUpload.jsx` - Image upload and analysis interface
- `NeuralNetworkForm.jsx` - Neural network training and testing form
- `NeuralNetworkResults.jsx` - Neural network results display
- `SimulationResults.jsx` - Simulation results visualization
- `WaveletView.jsx` - Wavelet data visualization

### Features
- **Modern UI/UX**: Clean, professional interface with Tailwind CSS
- **Form Validation**: Comprehensive client-side validation with error messages
- **Responsive Design**: Mobile-friendly responsive layout
- **File Upload**: Drag-and-drop file upload with preview
- **Real-time Feedback**: Loading states and progress indicators
- **Navigation**: Dropdown navigation menu matching original JSP structure

## API Endpoints

The frontend expects the following API endpoints from the Spring Boot backend:

- `POST /api/simulate` - Single pit growth simulation
- `POST /api/sim2d` - Multi-pit growth simulation
- `POST /api/material-constants` - Material constants processing
- `POST /api/image-analysis` - Image analysis with wavelet transformation
- `POST /api/neural-network` - Neural network training/testing
- `GET /api/features-file` - Download analysis features file

## Installation

```bash
cd frontend
npm install
npm start
```

## Build for Production

```bash
npm run build
```

## Technologies Used

- **React 18** - Modern React with hooks and functional components
- **React Router 6** - Client-side routing
- **Tailwind CSS** - Utility-first CSS framework
- **Modern JavaScript** - ES6+ features and async/await

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Development Notes

- All forms include comprehensive validation matching the original JSP validation
- File uploads support image files with size validation
- Navigation structure matches the original JSP menu system
- Components are designed to work seamlessly with the existing Spring Boot backend
- Error handling includes user-friendly error messages
- Loading states provide feedback during API calls

## File Structure

```
src/
├── components/
│   ├── Home.jsx
│   ├── SimulationForm.jsx
│   ├── Sim2DSettings.jsx
│   ├── MaterialConstantsForm.jsx
│   ├── ImageAnalysisUpload.jsx
│   ├── NeuralNetworkForm.jsx
│   ├── NeuralNetworkResults.jsx
│   ├── SimulationResults.jsx
│   ├── WaveletView.jsx
│   └── ...
├── App.js
├── index.js
└── index.css
```
