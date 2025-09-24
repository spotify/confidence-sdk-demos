# Confidence Java API Demo

A Spring Boot REST API demonstrating Confidence feature flags with subscription plan management and regional filtering.

## Running the Project Locally

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher

### Setup
1. **Clone and navigate to the project**
   ```bash
   cd java
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The API will start on `http://localhost:8080`

## API Endpoints

### GET /plans
Returns subscription plans with optional region filtering and feature flag evaluation.

**Query Parameters:**
- `region` (optional) - Filter by region: `na`, `eu`, `ap`, `sa`, `af`

**Headers:**
- `X-VISITOR-ID` (optional) - Visitor identifier for feature flag targeting

**Examples:**
```bash
# Get all plans
curl http://localhost:8080/plans

# Get plans for specific region
curl http://localhost:8080/plans?region=eu

# Get plans with visitor targeting
curl -H "X-VISITOR-ID: $(uuidgen)" http://localhost:8080/plans?region=na
```

## Feature Flags

### `show-enterprise-plan`
Controls whether the Enterprise plan is displayed to users.

**Schema:**
```json
{
  "enabled": boolean,
  "price": string (optional)
}
```

**Purpose:** Dynamically show/hide the Enterprise plan and customize its pricing based on visitor context and region.

## Architecture

- **Framework:** Spring Boot 3.2.0
- **Build Tool:** Maven
- **Feature Flags:** Confidence SDK via OpenFeature
- **Dependency Injection:** Spring IoC container
- **Data:** In-memory mock data with regional distribution

## Key Components

- **OpenFeature Integration:** Singleton client with startup initialization
- **Regional Data Model:** Plans support multiple regions via List<String>
- **Context Evaluation:** Visitor ID and region-based feature flag targeting
- **REST API:** Clean endpoint design with optional query parameters

## Documentation

- [Confidence Documentation](https://docs.confidence.spotify.com/)
- [OpenFeature Java SDK](https://openfeature.dev/docs/reference/technologies/server/java)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)