# Subscription API

A Spring Boot REST API that provides subscription and plan data with optional region filtering.

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

## Getting Started

### 1. Clone and navigate to the project
```bash
cd java
```

### 2. Build the project
```bash
mvn clean compile
```

### 3. Run the application
```bash
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

## API Endpoints

### GET /subscriptions
Returns a list of subscriptions.

**Optional Query Parameters:**
- `region` - Filter subscriptions by region (Available: `na`, `eu`, `ap`, `sa`, `af`)

**Examples:**
```bash
# Get all subscriptions
curl http://localhost:8080/subscriptions

# Get subscriptions for a specific region
curl http://localhost:8080/subscriptions?region=na
```

### GET /plans
Returns a list of plans.

**Optional Query Parameters:**
- `region` - Filter plans by region (Available: `na`, `eu`, `ap`, `sa`, `af`)

**Examples:**
```bash
# Get all plans
curl http://localhost:8080/plans

# Get plans for a specific region
curl http://localhost:8080/plans?region=eu

# Get plans with a random visitor ID (for feature flag evaluation)
curl -H "X-VISITOR-ID: $(uuidgen)" http://localhost:8080/plans?region=na

# Alternative using random string (for systems without uuidgen)
curl -H "X-VISITOR-ID: visitor-$(date +%s)-$(shuf -i 1000-9999 -n 1)" http://localhost:8080/plans
```

## Sample Data

The API returns mock data for:
- **Subscriptions**: Basic, Pro, Enterprise, Starter, Premium, Business, and Regional plans across different regions
- **Plans**: Detailed plan information including features, pricing, and billing cycles

**Available Regions:**
- `na` - North America
- `eu` - Europe
- `ap` - Asia Pacific
- `sa` - South America
- `af` - Africa

## Configuration

The application runs on port 8080 by default. You can change this in `src/main/resources/application.properties`:

```properties
server.port=8080
```

## Project Structure

```
src/
├── main/
│   ├── java/com/confidence/subscriptionapi/
│   │   ├── SubscriptionApiApplication.java    # Main application class
│   │   ├── controller/
│   │   │   └── ApiController.java             # REST endpoints
│   │   ├── model/
│   │   │   ├── Subscription.java              # Subscription model
│   │   │   └── Plan.java                      # Plan model
│   │   └── service/
│   │       ├── SubscriptionService.java       # Subscription business logic
│   │       └── PlanService.java               # Plan business logic
│   └── resources/
│       └── application.properties             # Application configuration
└── pom.xml                                    # Maven dependencies
```