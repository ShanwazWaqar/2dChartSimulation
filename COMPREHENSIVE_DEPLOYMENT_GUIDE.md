# Professional Deployment Guide (Free Tier)

This guide will walk you through hosting your **CorrosionSim** application for free using **Render (Backend)** and **Vercel (Frontend)**.

---

## Part 1: Deploy Backend (Spring Boot) on Render

**Render** offers a free tier for Web Services that is perfect for portfolio projects.

1.  **Sign Up**: Go to [render.com](https://render.com) and sign up with your GitHub account.
2.  **New Web Service**:
    *   Click **"New +"** button -> **"Web Service"**.
    *   Select **"Build and deploy from a Git repository"**.
    *   Connect your repository: `2dChartSimulation`.
3.  **Configuration**:
    *   **Name**: `corrosion-sim-api` (or similar).
    *   **Region**: Choose the one closest to you (e.g., US East).
    *   **Runtime**: Select **Docker**. (Render will automatically detect the `Dockerfile` we just added).
    *   **Plan**: Select **"Free"**.
4.  **Environment Variables**:
    *   Scroll down to "Environment Variables".
    *   Add Key: `PORT`, Value: `8080`.
5.  **Deploy**:
    *   Click **"Create Web Service"**.
    *   Render will start building your Docker image. This may take 5-10 minutes.
6.  **Get URL**:
    *   Once deployed, copy the URL (e.g., `https://corrosion-sim-api.onrender.com`).
    *   **Save this URL**. You need it for the frontend.

---

## Part 2: Configure Frontend for Production

Before deploying the frontend, we need to tell it where the backend lives.

1.  **Environment Variable**:
    *   We will set `REACT_APP_API_URL` in the Vercel dashboard, NOT in the code. This is the secure, professional way.

---

## Part 3: Deploy Frontend (React) on Vercel

**Vercel** is the gold standard for React hosting.

1.  **Sign Up**: Go to [vercel.com](https://vercel.com) and sign up with GitHub.
2.  **Add New Project**:
    *   Click **"Add New..."** -> **"Project"**.
    *   Import your `2dChartSimulation` repository.
3.  **Configure Project**:
    *   **Framework Preset**: It should auto-detect **Create React App**.
    *   **Root Directory**: Click "Edit" and select `frontend`. **(Crucial Step!)**
4.  **Environment Variables**:
    *   Expand the "Environment Variables" section.
    *   **Key**: `REACT_APP_API_URL`
    *   **Value**: Paste your Render Backend URL from Part 1 (e.g., `https://corrosion-sim-api.onrender.com`).
5.  **Deploy**:
    *   Click **"Deploy"**.
    *   Vercel will build your site in ~1 minute.

---

## Part 4: Final Verification

1.  Visit your Vercel URL (e.g., `https://corrosion-sim.vercel.app`).
2.  Go to "Analysis" -> "Neural Network".
3.  Try running a test.
    *   *Note: On the free tier, the first request might take 30-50 seconds to wake up the Render backend. This is normal for free hosting.*

## Troubleshooting

*   **Backend asleep**: If the frontend says "Network Error", wait 1 minute and try again. Render puts free apps to sleep after inactivity.
*   **CORS Issues**: If you see CORS errors in the console, we may need to update the Spring Boot `WebConfig.java` to allow the specific Vercel URL.
    *   *Current config allows `*` (all origins), so it should work out of the box!*
