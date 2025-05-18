## OSINT Web Application

A self-contained Dockerized web application for running Open-Source Intelligence (OSINT) domain reconnaissance using popular tools and reviewing the findings in a clean UI.

---

### Three-Command Quick Start

```bash
# 1. Clone the repository
git clone https://github.com/AntonHorelyk/osint-web-app.git

# 2. Change into project directory
cd osint-web-app

# 3. Build and launch all services with Docker
docker compose up --build
```

Once all services are running, open your browser and navigate to `http://localhost:3000`.

---

### Project Overview & Scope

This application provides a full-stack solution for domain reconnaissance, featuring:

* **Frontend**: React-based UI for initiating scans, viewing live progress, and browsing scan history.
* **Backend**: Kotlin/Ktor API that launches theHarvester and Amass in parallel, merges and deduplicates results, and persists scan data to a database.
* **Persistence**: Stores scan metadata and artifacts so history remains available after browser refresh.
* **Export**: Allows exporting any scan result to an Excel (XLSX) file using a Kotlin-friendly library.
* **Docker**: All components are containerized and orchestrated via `docker-compose.yml` for easy setup.

#### Functional Highlights

1. Accepts a domain name and optional flags for scanning.
2. Runs theHarvester and Amass concurrently and streams partial results to the UI.
3. Combines, deduplicates, and persists artifacts (subdomains, IPs, emails, etc.).
4. Displays each scan as a card with summary data and detail modal.
5. Exports scan artifacts to XLSX.
6. Implements robust error handling, logging, and test coverage.

---

### Repository Structure

```
osint-web-app/
├── backend/            # Kotlin/Ktor API service
├── frontend/           # React UI application
├── docker-compose.yml  # Orchestrates backend + frontend
└── README.md           # This file
```

---

### Next Steps

See `answers.md` for details on additional production tests, performance benchmarking strategies, and OSINT tool considerations.

---

### License

This project is open-source and available under the MIT License.

### API ###
