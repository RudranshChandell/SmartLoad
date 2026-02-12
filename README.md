# SmartLoad Optimizer Service

A high-performance microservice that optimizes truck loads for maximum revenue while respecting weight, volume, and regulation constraints (Hazmat/Lane compatibility).

## 🚀 Features
- **Algorithm:** Recursive Backtracking with Memoization (Knapsack-style optimization).
- **Performance:** Sub-second response time for N=25+ orders.
- **Constraints:** Handles Weight, Volume, Hazmat isolation, and Lane compatibility.
- **Architecture:** Stateless Spring Boot 3.2 microservice.
- **Infrastructure:** Dockerized with a Multi-Stage build (Alpine Linux) for minimal footprint.

## 🛠 Tech Stack
- **Language:** Java 17
- **Framework:** Spring Boot 3
- **Build Tool:** Maven
- **Containerization:** Docker & Docker Compose

## 📦 How to Run

### Prerequisites
- Docker & Docker Compose installed.

### Start the Service
Run the following command in the root directory:
```bash
docker compose up --build
