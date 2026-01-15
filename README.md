# CorrosionSim: Advanced Corrosion Analysis Platform

![CorrosionSim Dashboard](paper/screen.png)

## Overview
**CorrosionSim** is a next-generation scientific analysis platform designed for the modern web. It bridges the gap between complex electrochemical simulation and intuitive user experience, adhering to the highest standards of software engineering and accessibility.

This project uses a modern **React 19** frontend with **UGA (University of Georgia) branding** and a robust **Spring Boot** backend for high-performance computation.

![Application Demo](paper/demo.webp)

## Modern Engineering & Standards

### 🌐 Professional UX/UI
Designed with the "Awe" factor in mind, the interface features:
*   **Split-View Architecture**: Seamlessly configures inputs while visualizing results in real-time.
*   **Animated Transitions**: Smooth state changes using `tailwindcss-animate` for a polished feel.
*   **Premium Typography**: Utilizes **Inter**, the industry-standard font for readability and elegance.

### ♿ Accessibility (WCAG 2.1 Compliant)
We prioritize inclusivity. The application passes strict WCAG 2.1 AA checks:
*   **Focus Management**: High-visibility focus rings (UGA Red) for keyboard navigation.
*   **Semantic HTML**: Proper labeling of form inputs (`htmlFor`, `aria-label`) ensures screen reader compatibility.
*   **Contrast Ratios**: Verified high-contrast color pairings (#BA0C2F on White) for readability.

## Key Features

### 1. Neural Network Analysis
*   **Predictive Modeling**: Uses trained neural networks (MLP) to predict corrosion outcome based on architectural parameters.
*   **Verification Mode**: Validates models against test datasets with real-time error analysis (MSE, MAE).
*   **Visual Results**: Displays training loss, accuracy, and detailed prediction tables.

### 2. 2D Corrosion Simulation
*   **Cellular Automata**: Simulates pit growth over time using configurable electrochemical parameters.
*   **Multi-Site Capability**: Models interactions between multiple corrosion pits.

### 3. Image Analysis
*   **Wavelet Transformation**: Processes surface imagery to detect early stages of corrosion.
*   **Statistical Metrics**: Calculates entropy, energy, and localized intensity variances.

## Tech Stack

### Frontend
-   **React 19**: Component-based UI architecture.
-   **Tailwind CSS**: Utility-first styling with custom UGA color palette (`#BA0C2F`).
-   **Framer Motion**: Smooth, professional animations and transitions.
-   **Recharts**: Data visualization for loss/accuracy curves.
-   **Lucide React**: Modern, consistent iconography.

### Backend
-   **Java Spring Boot**: REST API handling large-scale simulation requests.
-   **Legacy Integration**: Seamlessly bridges modern web interfaces with legacy Fortran/Java simulation kernels.
-   **File Processing**: Robust handling of large datasets (`.txt`, `.csv`) for training/testing.

## Getting Started

### Prerequisites
-   Node.js (v18+)
-   Java JDK 17+
-   Maven

### Installation

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/your-username/corrosion-sim.git
    cd corrosion-sim
    ```

2.  **Backend Setup**
    ```bash
    mvn spring-boot:run
    ```
    The server will start on `http://localhost:8080`.

3.  **Frontend Setup**
    ```bash
    cd frontend
    npm install
    npm start
    ```
    The application will run on `http://localhost:3000`.

## Testing
The project includes a comprehensive end-to-end test suite ensuring UI reliability and data accuracy.

```bash
cd frontend
npm test
```

## License
University of Georgia - Engineering Department. All Rights Reserved.