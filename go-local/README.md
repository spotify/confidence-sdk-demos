# Go Local Feature Flag Demo

A Go application demonstrating local feature flag evaluation using the [OpenFeature SDK](https://openfeature.dev/) with the [Confidence Local Provider](https://github.com/spotify/confidence-resolver-rust/tree/main/openfeature-provider/go). This provider uses WebAssembly for low-latency, local flag resolution without network calls during evaluation.

## Key Differences from Standard Go Demo

- **Local Resolution**: Uses WebAssembly-based local provider instead of remote SDK
- **No Network Calls**: Flag evaluation happens locally with zero latency
- **Periodic Sync**: Flag configurations are synced periodically in the background
- **Additional Credentials**: Requires API credentials for configuration sync

## Setup

1. **Install Go** (version 1.24 or higher required by OpenFeature SDK v1.16.0)

2. **Download dependencies:**
   ```bash
   GOTOOLCHAIN=go1.24.9 go mod download
   ```

3. **Create a `.env` file with your credentials:**
   ```bash
   cat > .env << EOF
   CONFIDENCE_API_CLIENT_ID=your_api_client_id_here
   CONFIDENCE_API_CLIENT_SECRET=your_api_client_secret_here
   CONFIDENCE_CLIENT_SECRET=your_client_secret_here
   EOF
   ```

   You'll need to obtain these credentials from the Confidence dashboard:
   - **API Client ID & Secret**: OAuth credentials for syncing flag configurations
   - **Client Secret**: Used for local flag resolution

## Running the Apps

This demo includes two programs:
1. **cmd/cli** - Command-line tool for running multiple evaluations
2. **cmd/service** - HTTP service for on-demand flag evaluation

### Command-Line Tool (cmd/cli)

The app runs multiple feature flag evaluations based on the `-n` argument.

**Basic usage:**
```bash
GOTOOLCHAIN=go1.24.9 go run ./cmd/cli -n 10          # Run 10 evaluations (default, verbose)
GOTOOLCHAIN=go1.24.9 go run ./cmd/cli -n 25          # Run 25 evaluations
```

**Quiet mode for compact output:**
```bash
GOTOOLCHAIN=go1.24.9 go run ./cmd/cli -n 20 -q       # Compact output
```

**View all options:**
```bash
GOTOOLCHAIN=go1.24.9 go run ./cmd/cli -h
```

### HTTP Service (cmd/service)

The service exposes an HTTP endpoint for on-demand feature flag evaluation with visitor-specific context.

**Start the service:**
```bash
GOTOOLCHAIN=go1.24.9 go run ./cmd/service
```

The service will start on port 8080 (configurable via `PORT` environment variable) and expose:
- `GET /evaluate?region=<region>` - Evaluate flags (requires `X-Visitor-ID` header)
- `GET /health` - Health check endpoint

**Using the client script:**

A bash script `client.sh` is provided to easily test the service:

```bash
# Run 5 random requests (default)
./client.sh

# Run 10 requests with 2 second intervals
./client.sh -n 10 -s 2

# Test a specific region
./client.sh -r eu -n 3

# View all options
./client.sh -h
```

**Manual curl example:**
```bash
# Generate a visitor ID and make a request
VISITOR_ID=$(uuidgen | tr '[:upper:]' '[:lower:]')
curl -H "X-Visitor-ID: $VISITOR_ID" \
     "http://localhost:8080/evaluate?region=eu"
```

**Example response:**
```json
{
  "visitor_id": "d7515baf-6d3d-44b2-a569-764d91d5f83e",
  "region": "eu",
  "flag_value": "Let's Talk",
  "message": "Enterprise plan price for region 'eu': Let's Talk",
  "timestamp": "2025-11-05T09:18:05+01:00"
}
```

## What They Do

### Command-Line Tool

The app:
- Initializes the OpenFeature API with the local Confidence Provider
- Uses WebAssembly for local flag resolution (no network calls during evaluation)
- Generates a random visitor ID for each run (using UUID v4)
- Randomly selects a region from 3 options (na, eu, asia)
- Creates an evaluation context with both the visitor ID and region
- Fetches the value of a feature flag called `show-enterprise-plan.price` using OpenFeature
- Prints the flag evaluation details and statistics

### HTTP Service

The service:
- Runs continuously as an HTTP server
- Accepts requests with visitor ID in the header and region as a query parameter
- Evaluates feature flags on-demand for each request
- Uses the local WebAssembly provider for zero-latency evaluation
- Periodically syncs flag configurations in the background
- Returns JSON responses with flag values and metadata

## Sample Output

### Verbose Mode (default)
```
Starting 5 feature flag evaluations with local provider...
==================================================
Run  1/5: Region=na | Flag=nine-nine-nine | Visitor=45ebc191... | Time=14:54:47
Run  2/5: Region=eu | Flag=lets-talk     | Visitor=4cec2cc1... | Time=14:54:47
Run  3/5: Region=asia | Flag=disabled   | Visitor=3d3889b7... | Time=14:54:47
==================================================
Completed 5 successful evaluations out of 5 attempts

Summary:
Regions: [asia eu na]
Flag values: [disabled lets-talk nine-nine-nine]

Region distribution:
  asia:  1 ( 20.0%)
  eu:  2 ( 40.0%)
  na:  2 ( 40.0%)
```

### Quiet Mode
```
Starting 5 feature flag evaluations with local provider...
==================================================
Run  1/5: na -> nine-nine-nine
Run  2/5: eu -> lets-talk
Run  3/5: asia -> disabled
==================================================
Completed 5 successful evaluations out of 5 attempts

Summary:
Regions: [asia eu na]
Flag values: [disabled lets-talk nine-nine-nine]

Region distribution:
  asia:  1 ( 20.0%)
  eu:  2 ( 40.0%)
  na:  2 ( 40.0%)
```

## Configuration

- **API Client Credentials**: Stored securely in `.env` file (not committed to git)
- **Flag Name**: Set to `show-enterprise-plan.price` in the code
- **Default Values**: Returns `"default"` when the flag is not configured or accessible
- **Regions**: Randomly selects from: `na`, `eu`, `asia` (matching flag targeting rules)

### Environment Variables

The app requires the following environment variables:

- `CONFIDENCE_API_CLIENT_ID`: Your Confidence API client ID (for syncing)
- `CONFIDENCE_API_CLIENT_SECRET`: Your Confidence API client secret (for syncing)
- `CONFIDENCE_CLIENT_SECRET`: Your Confidence client secret (for resolution)

Create a `.env` file in the project root with:
```
CONFIDENCE_API_CLIENT_ID=your_api_client_id_here
CONFIDENCE_API_CLIENT_SECRET=your_api_client_secret_here
CONFIDENCE_CLIENT_SECRET=your_client_secret_here
```

## Project Structure

```
go-local/
├── cmd/
│   ├── cli/
│   │   └── main.go      # Command-line tool for batch evaluations
│   └── service/
│       └── main.go      # HTTP service for on-demand evaluations
├── client.sh            # Bash script to test the HTTP service
├── go.mod               # Go module dependencies
├── go.sum               # Go module checksums (generated)
├── .env                 # Environment variables (create this, not in git)
├── .env.example         # Example environment file
├── .gitignore           # Git ignore rules
└── README.md            # This documentation
```

## Dependencies

- `github.com/open-feature/go-sdk` (v1.16.0+) - Vendor-neutral feature flag API
- `github.com/spotify/confidence-resolver-rust/openfeature-provider/go/confidence` - Local Confidence provider with WebAssembly
- `github.com/joho/godotenv` - Environment variable management from .env files
- `github.com/google/uuid` - UUID generation for visitor IDs

## Performance Benefits

The local provider offers several performance advantages:

- **Zero-latency evaluation**: No network calls during flag evaluation
- **WebAssembly-based resolution**: Fast, local flag resolution
- **Background sync**: Flag configurations are synced periodically
- **Offline capability**: Continues working even if sync temporarily fails

## Security

- All credentials are stored in a `.env` file which is excluded from version control via `.gitignore`
- Never commit your `.env` file to git
- The app will return an error if any required credentials are not found in the environment
