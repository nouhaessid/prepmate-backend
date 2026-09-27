# PrepMate — AI Technical Interview Preparation Platform

PrepMate is an AI-powered technical interview preparation platform built with **Spring Boot**, **Spring Cloud**, **Angular**, **Keycloak**, and **Groq**.

The platform allows users to practice technical interviews by selecting a topic, difficulty, and number of questions. AI-generated questions are presented one at a time, and submitted answers are evaluated automatically with a score, feedback, strengths, weaknesses, and a suggested answer.

The backend follows a **microservices architecture** and is integrated with a separate Angular frontend.

**Frontend:** [PrepMate Frontend](https://github.com/nouhaessid/prepmate-frontend/)

---

## Architecture

PrepMate follows a **microservices architecture**, where different responsibilities are separated into independent services.

The main components include:

* **API Gateway** — single entry point for frontend requests and routing.
* **Config Server** — centralized configuration for backend services.
* **Eureka Discovery Server** — service registration and discovery.
* **User Service** — manages application user profiles and user data.
* **Interview Service** — manages interview sessions, questions, answers, evaluations, and scores.
* **AI Service** — generates interview questions and evaluates submitted answers using Groq.
* **Keycloak** — authentication and authorization.
* **PostgreSQL** — persistent storage for User and Interview services.
* **Docker / Docker Compose** — containerization and infrastructure management.

### System Architecture

![PrepMate System Architecture](./docs/PrepMate%20System%20Architecture%20Diagram.png)

---

## Microservices

| Service           | Responsibility                                    | Database   |
| ----------------- | ------------------------------------------------- | ---------- |
| Config Server     | Centralized configuration                         | —          |
| Discovery Service | Service registration and discovery                | —          |
| Gateway Service   | API entry point and routing                       | —          |
| User Service      | User profile and application user management      | PostgreSQL |
| Interview Service | Interview sessions, questions, answers and scores | PostgreSQL |
| AI Service        | AI question generation and answer evaluation      | —          |

Each business service has a clearly defined responsibility and communicates with other services through APIs.

---

## Interview Workflow

After authentication, users can access their PrepMate dashboard and:

* Start a new technical interview by selecting a topic, difficulty, and number of questions.
* Answer AI-generated questions one at a time and receive an evaluation after each answer.
* Resume interviews that are still in progress.
* Review completed interviews and their detailed results.
* Track interview performance and statistics through the dashboard and profile pages.

During an interview, the **Interview Service** communicates with the **AI Service** to generate questions and evaluate submitted answers. Once all questions are completed, the session is scored and marked as completed.

---

## AI Integration

PrepMate uses an AI service to provide two main capabilities.

### Question Generation

The AI Service generates technical interview questions based on:

* Topic
* Difficulty
* Previously generated questions

Previously generated questions are provided to help avoid repeating questions within the same interview session.

### Answer Evaluation

Submitted answers are evaluated by the AI Service.

Each evaluation contains:

* Score
* Feedback
* Strengths
* Weaknesses
* Suggested answer

The AI response uses a **structured JSON format** to make the communication between the AI Service and Interview Service predictable and consistent.

---

## Authentication & Authorization

**Keycloak** is used for authentication and authorization.

The platform supports:

* User registration
* User authentication
* JWT-based authentication
* Protected backend endpoints
* Ownership-based access to interview sessions

The Angular frontend redirects users to Keycloak for authentication and sends the resulting access token with API requests.

The backend validates the JWT and uses the authenticated **Keycloak user ID** to associate interview sessions with their owner.

Method-level authorization is used to protect operations such as retrieving, submitting answers to, and deleting interview sessions.

---

## Database Architecture

The application uses **PostgreSQL** for persistent application data.

### User Service

The User Service stores application user data associated with the authenticated Keycloak identity.

### Interview Service

The Interview Service stores:

* Interview sessions
* Questions
* Answers
* Scores
* Feedback
* Strengths and weaknesses
* Suggested answers

An interview session contains multiple questions, while each question can have one submitted answer.

The Interview Service stores the **Keycloak user ID** to identify the owner of an interview session. User data is therefore associated through the shared Keycloak identity rather than through a direct database relationship between the two microservices.

### Entity Relationship Diagram

![PrepMate ER Diagram](./docs/PrepMate%20Entity%20Relationship%20Diagram.png)

---

## Service Communication

PrepMate uses several communication mechanisms.

### Synchronous Communication

Services communicate through HTTP APIs.

For example:

```text
Interview Service
       |
       | HTTP / OpenFeign
       ↓
   AI Service
```

The Interview Service uses **OpenFeign** to communicate with the AI Service for question generation and answer evaluation.

### Service Discovery

**Netflix Eureka** is used as the service registry.

Services register themselves with Eureka and use the registry for service discovery.

### API Gateway

The Angular frontend communicates with the backend through the API Gateway.

```text
Angular
   ↓
API Gateway
   ↓
Microservices
```

The AI Service is an internal service and is accessed by the Interview Service rather than directly by the frontend.

---

## Frontend Integration

The backend is integrated with a separate **Angular** frontend.

The frontend communicates with the backend through the API Gateway and uses Keycloak for authentication.

**Frontend repository:** [PrepMate Frontend](https://github.com/nouhaessid/prepmate-frontend/)

The two repositories are maintained separately while forming one complete PrepMate platform.

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/nouhaessid/prepmate-backend.git
cd prepmate-backend
```

### 2. Configure environment variables

Create a `.env` file in the project root and configure the required environment variables.

These include values such as:

* Keycloak configuration
* PostgreSQL configuration
* Groq API credentials
* Service configuration

The `.env` file is not committed to the repository.

### 3. Start the infrastructure

```bash
docker compose up -d
```

### 4. Start the backend services

Start the Spring Boot microservices.

The services will register with Eureka and use the configured service discovery, centralized configuration, and gateway infrastructure.

---

## Future Improvements

* Automated CI/CD pipeline
* Expanded unit and integration test coverage
* Production deployment
* Additional AI-powered interview features
* Improved monitoring and observability
* Additional interview topics and question types

---

## Related Repository

**Frontend:** [PrepMate Frontend](https://github.com/nouhaessid/prepmate-frontend/)

The backend and frontend repositories together form the complete **PrepMate AI technical interview preparation platform**.
