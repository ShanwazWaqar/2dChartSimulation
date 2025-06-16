
# 2dChartSimulation (Legacy)

This is the original legacy version of the 2D Corrosion Chart Simulation project.

## Overview

A JSP/Servlet-based simulation tool that models corrosion using 2D pit growth visualization.
Includes functionality for image upload and analysis using neural network-based feature extraction.

## Technologies Used

- Java 17/22 (Tested with JDK 22)
- Apache Tomcat 9.0.106
- Java Servlets and JSP
- Java Applets (Legacy)
- File-based neural network computation
- No external database dependencies

---

## Project Structure

```
Simulation/
├── WEB-INF/                  # Web config and compiled classes
├── src/                      # Java source files
├── images/                   # Static images for banner and simulation
├── js/                       # JavaScript files for UI functionality
├── css/                      # Style sheets
├── ImgWaveV1/                # Legacy neural net processing module
├── nnet_sia/                 # Neural net weights and results
├── Sim2D*.jsp                # JSP pages for 2D simulation
├── image*.jsp                # JSPs for image upload/analysis
├── *.jar                     # Supporting legacy libraries
└── index.jsp                 # Entry point for the web application
```

---

## How to Run (Local Setup)

### 1. Install Apache Tomcat 9
- Download from: https://tomcat.apache.org/download-90.cgi
- Extract to: `C:/apache-tomcat-9.0.106`

### 2. Clone this Repository into Tomcat Webapps

```bash
cd C:/apache-tomcat-9.0.106/webapps
git clone https://github.com/YOUR_USERNAME/2dChartSimulation.git Simulation
cd Simulation
git checkout legacy
```

### 3. Compile Java Source Files (if required)

```bash
cd src
javac -classpath "../WEB-INF/classes;C:/apache-tomcat-9.0.106/lib/servlet-api.jar" -d ../WEB-INF/classes com/filter/URLRewriteFilter.java
```

> You can compile other `.java` files similarly if needed.

### 4. Start Tomcat Server

```bash
cd C:/apache-tomcat-9.0.106/bin
startup.bat
```

### 5. Open in Browser

```text
http://localhost:8080/Simulation/
```

---

## Notes

- Works best in older browsers that support Java Applets.
- No database or ORM — all data processing is file/in-memory based.
- `nnet_sia/` contains neural net model weights.
- `WEB-INF/classes` should contain all compiled `.class` files.

---

## Modernization Plan

This branch (`legacy`) will be preserved as-is. Future upgrades to the project will include:

- **Spring Boot** for backend services
- **React** for frontend UI
- **REST API** in place of JSP/Servlets
- **Modern charting** using Chart.js or D3.js
- **Database integration** with PostgreSQL or SQLite

The modernization will begin on the `main` branch.

---

_Last updated: 16-Jun-2025_
