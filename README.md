# EmpowerMe Digital Marketing Platform

## Overview
EmpowerMe is a comprehensive digital marketing management platform designed for entrepreneurs. It integrates **Spring Boot** for the backend API and **OSGi** for modular business logic, with a modern **HTML/CSS/JS** frontend.

## Project Structure
*   `empowerme-frontend`: The HTML, CSS, and JavaScript frontend application.
*   `empowerme-spring`: The Spring Boot Backend API (Main entry point).
*   `empowerme-osgi`: The OSGi modules (API, Implementation, and Commands).

## Prerequisites
*   Java 17 or higher
*   Maven 3.6+
*   A modern web browser

## 1. Running the Spring Boot Backend
The Spring Boot application serves the REST API and acts as the core backend.

1.  Open your terminal.
2.  Navigate to the `empowerme-spring` directory:
    ```bash
    cd empowerme-spring
    ```
3.  Run the application using Maven:
    ```bash
    mvn spring-boot:run
    ```
    *   The server will start on `http://localhost:8080`.
    *   It will automatically seed demo data (Content Assets, User, Campaigns).

## 2. Building the OSGi Modules
The OSGi project demonstrates modularity. To build all bundles:

1.  Open your terminal.
2.  Navigate to the `empowerme-osgi` directory:
    ```bash
    cd empowerme-osgi
    ```
3.  Build and install clean:
    ```bash
    mvn clean install
    ```
    *   This will build `empowerme-osgi-api`, `empowerme-osgi-impl`, `empowerme-osgi-command`, etc.

## 3. Running OSGi in Apache Karaf
To verify the OSGi modules in a real runtime environment (Apache Karaf):

1.  **Download Apache Karaf**: Get the latest version from [karaf.apache.org](https://karaf.apache.org/download.html).
2.  **Start Karaf**:
    *   Run `bin/karaf` (Mac/Linux) or `bin\karaf.bat` (Windows).
3.  **Install Bundles**:
    Locate the JAR files produced by the Maven build (found in the `target/` subdirectory of each module). Install them in the Karaf console in the following order using the `install -s` command followed by the absolute path to the JAR.

    **Installation Order:**
    1.  `empowerme-osgi-api`
    2.  `empowerme-osgi-impl`
    3.  `empowerme-osgi-command`

    *Example command:*
    `install -s file:///path/to/your/project/empowerme-osgi-api/target/empowerme-osgi-api-1.0.0-SNAPSHOT.jar`

4.  **Verify & Interact**:
    *   Type `list` to see your bundles. Ensure they are all **Active**.
    *   Type `service:list com.empowerme.osgi.api.ContentService` to verify the service is registered.
    *   **Run Commands**: Use the Gogo shell commands defined in the project (e.g., `content:list`) to interact with the logic.

    > **Tip**: If you modify the code, rebuild with `mvn clean install`, then use `update [Bundle-ID]` in Karaf to reload the changes without restarting.

## 4. Running the Frontend
The frontend is a static web application that connects to the Spring Boot backend.

1.  Ensure the Spring Boot backend is running (Step 1).
2.  Navigate to the `empowerme-frontend` directory.
3.  Open `index.html` in your browser.
    *   **Mac**: `open index.html`
    *   **Windows**: Double-click `index.html`
    *   **Linux**: `xdg-open index.html`

## Features & Verification
*   **Dashboard**: Shows real-time statistics from the backend.
*   **Content Inventory**: Upload new assets, view status, and push to LIVE.
*   **Campaigns**: Create campaigns using LIVE assets.
    *   *Note*: Creating a campaign deducts $50 from the user's wallet.
*   **User Profile**: View current wallet balance and risk score.
*   **Reports**: View aggregated analytics and export to CSV.

## Troubleshooting
*   **Blank Page?**: Ensure you are using a modern browser. Hard refresh (Cmd+Shift+R) to clear legacy scripts.
*   **Data Not Loading?**: Ensure the Spring Boot backend is running on port 8080 and there are no CORS errors in the browser console.
