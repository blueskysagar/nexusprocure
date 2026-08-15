# NexusProcure

## Enterprise Procurement Platform

NexusProcure is an enterprise procurement platform designed to manage procurement workflows including users, vendors, products, purchase requisitions, purchase orders, warehouse inventory, and stock operations.

The platform is built with Java 21 and Spring Boot using a layered backend architecture with event-driven integration through Apache Kafka. It incorporates secure REST APIs, JWT-based authentication and role-based authorization, Redis caching, PostgreSQL persistence, containerization with Docker, and Kubernetes-based deployment.
## Technology Stack

| Category         | Technology                                           |
|------------------|------------------------------------------------------|
| Language         | Java 21                                              |
| Framework        | Spring Boot 3.5.16                                   |
| Build Tool       | Maven                                                |
| Database         | PostgreSQL                                           |
| ORM              | Spring Data JPA / Hibernate                          |
| Security         | Spring Security 6, JWT, RBAC                         |
| API              | REST APIs, Swagger / OpenAPI                         |
| Caching          | Redis, Spring Cache                                  |
| Messaging        | Apache Kafka                                         |
| Reliability      | Kafka Retry, DefaultErrorHandler, Dead Letter Topics |
| Containerization | Docker, Docker Compose                               |
| Orchestration    | Kubernetes                                           |
| Autoscaling      | Kubernetes HPA                                       |
| CI/CD            | GitHub Actions                                       |
## Architecture

NexusProcure follows a layered backend architecture with event-driven integration.

```text
Client
   |
   v
REST Controllers
   |
   v
Service Layer
   |
   +--------------------+
   |                    |
   v                    v
Repository Layer      Redis Cache
   |                    |
   v                    |
PostgreSQL <------------+
   
Domain Events
   |
   v
Apache Kafka
   |
   +--------------------+
   |                    |
   v                    v
Kafka Consumers       Dead Letter Topics



## Core Features

### Authentication & Authorization

- JWT-based authentication
- Role-based access control (RBAC)
- Spring Security 6
- BCrypt password hashing
- Method-level authorization with `@PreAuthorize`

### User Management

- User creation and management
- Role assignment
- Request validation
- Secure password handling

### Vendor Management

- Vendor management workflows
- Vendor information persistence
- Validation and REST APIs

### Product Catalog

- Product management
- Product validation
- Redis-backed caching for frequently accessed product data

### Procurement

- Purchase requisition workflow
- Purchase order management
- Purchase order approval processing

### Warehouse & Inventory

- Warehouse management
- Inventory receiving
- Stock issue processing
- Duplicate event processing protection

### Event-Driven Processing

- Kafka-based asynchronous communication
- Purchase order approval events
- Inventory receiving events
- Stock issue events

### Kafka Reliability

- `DefaultErrorHandler`
- Retry processing
- `DeadLetterPublishingRecoverer`
- Dead Letter Topics (DLT)
- Dedicated DLT listeners for failed events

### API & Validation

- RESTful APIs
- DTO-based request/response handling
- Bean Validation
- Swagger / OpenAPI documentation

### Caching

- Spring Cache
- Redis
- Cache-aside pattern
- Configurable cache expiration

### DevOps & Deployment

- Docker multi-stage build
- Docker Compose
- Docker Hub image publishing
- Kubernetes Deployment
- ConfigMap and Secret configuration
- Startup, readiness, and liveness probes
- Rolling updates
- Horizontal Pod Autoscaling (HPA)


## API Documentation & Testing

NexusProcure exposes REST APIs for the platform's core business modules.

API documentation is provided through Swagger/OpenAPI, allowing developers to explore available endpoints, request models, responses, and authentication requirements.

The application also includes automated tests covering application behavior and integration scenarios.

### API Areas

- Authentication
- User Management
- Vendor Management
- Product Catalog
- Purchase Requisition
- Purchase Order
- Warehouse
- Inventory
- Stock Issue

## API Documentation & Testing

NexusProcure exposes REST APIs for the platform's core business modules.

API documentation is provided through Swagger/OpenAPI, allowing developers to explore available endpoints, request models, responses, and authentication requirements.

The application also includes automated tests covering application behavior and integration scenarios.

### API Areas

- Authentication
- User Management
- Vendor Management
- Product Catalog
- Purchase Requisition
- Purchase Order
- Warehouse
- Inventory
- Stock Issue


## Event-Driven Processing

NexusProcure uses Apache Kafka for asynchronous communication between business workflows.

For example, when a purchase order is approved, the application publishes an event that can be consumed by the inventory processing flow.

```text
Purchase Order Approved
          |
          v
   Application Event
          |
          v
   Kafka Event Publisher
          |
          v
      Kafka Topic
          |
          v
 Inventory Consumer
          |
          v
  Inventory Updated

### Kafka Reliability & Dead Letter Topics

Kafka consumers use Spring Kafka's error handling mechanism to provide retry and failure recovery.

```text
Consumer Failure
      |
      v
DefaultErrorHandler
      |
      v
Retry
      |
      +------ Success ------> Continue Processing
      |
      v
DeadLetterPublishingRecoverer
      |
      v
Dead Letter Topic
      |
      v
DLT Listener

## Containerization

NexusProcure is containerized using Docker.

The project uses a multi-stage Docker build to package the Spring Boot application into a lightweight runtime image.

Docker Compose is provided for local orchestration of the application and its infrastructure dependencies.

### Containerized Infrastructure

- Spring Boot application
- PostgreSQL
- Redis
- Apache Kafka
 
NexusProcure can be deployed to Kubernetes using the provided manifests.

The Kubernetes configuration includes:

- Deployment with multiple replicas
- ClusterIP Services
- ConfigMap for non-sensitive configuration
- Secret for sensitive configuration
- Startup, readiness, and liveness probes
- RollingUpdate deployment strategy
- Horizontal Pod Autoscaler (HPA)
- CPU-based autoscaling


## Running the Application

### Prerequisites

- Java 21
- Maven
- Docker
- Docker Compose

### Run with Maven

```bash
./mvnw spring-boot:run



