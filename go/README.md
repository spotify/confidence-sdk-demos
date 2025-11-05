# Go Feature Flag Demo

A simple Go application that demonstrates fetching feature flags using the [OpenFeature SDK](https://openfeature.dev/) with the [Confidence Provider](https://github.com/spotify/confidence-sdk-go).

## Setup

1. **Install Go** (version 1.21 or higher)

2. **Download dependencies:**
   ```bash
   go mod download
   ```

3. **Create a `.env` file with your API key:**
   ```bash
   cat > .env << EOF
   CONFIDENCE_API_KEY=your_api_key_here
   EOF
   ```
   Replace `your_api_key_here` with your actual Confidence API key.

## Running the App

The application uses a single `runner.go` file that can perform single or multiple feature flag evaluations.

### Single Evaluation (Default)

Run a single feature flag evaluation:
```bash
go run runner.go              # Single run (default)
go run runner.go -n 1         # Explicit single run
```

### Multiple Evaluations

Run multiple feature flag evaluations with random sleep intervals between runs:

**Basic usage:**
```bash
go run runner.go -n 10          # Run 10 evaluations
go run runner.go -n 25          # Run 25 evaluations
```

**With custom sleep intervals:**
```bash
go run runner.go -n 15 -min-sleep 5 -max-sleep 50   # 5-50 millisecond intervals
go run runner.go -n 50 -fast                        # Fast mode (1-5ms intervals)
```

**Quiet mode for less verbose output:**
```bash
go run runner.go -n 20 -q        # Compact output
```

**View all options:**
```bash
go run runner.go -h
```

**Or build it first:**
```bash
go build -o runner runner.go
./runner -n 10
```

## What it does

The app:
- Initializes the OpenFeature API with the Confidence Provider
- Generates a random visitor ID for each run (using UUID v4)
- Randomly selects a region from 3 options (na, eu, asia)
- Creates an evaluation context with both the visitor ID and region
- Fetches the value of a feature flag called `show-enterprise-plan.price` using OpenFeature
- Prints the flag evaluation details

## Sample Output

### Single Evaluation
```
Visitor ID: d106896d-313e-460f-8ca3-add190df11be
Region: na
Feature flag value: default
```

### Multiple Evaluations
```
Starting 5 feature flag evaluations...
Sleep interval: 1-25 milliseconds
==================================================
Run  1/5: Region=eu | Flag=Let's Talk   | Visitor=45ebc191... | Time=14:54:47
    Sleeping for 12.3 milliseconds...

Run  2/5: Region=na | Flag=default      | Visitor=4cec2cc1... | Time=14:54:47
    Sleeping for 8.7 milliseconds...

Run  3/5: Region=asia | Flag=default    | Visitor=3d3889b7... | Time=14:54:47
==================================================
Completed 5 successful evaluations out of 5 attempts

Summary:
Regions: [asia eu na]
Flag values: [Let's Talk default]

Region distribution:
  asia:  1 ( 20.0%)
  eu:  2 ( 40.0%)
  na:  2 ( 40.0%)
```

## Configuration

- **API Key**: Stored securely in `.env` file (not committed to git)
- **Flag Name**: Set to `show-enterprise-plan.price` in the code
- **Default Values**: Returns `"default"` when the flag is not configured or accessible
- **Regions**: Randomly selects from: `na`, `eu`, `asia` (matching flag targeting rules)

### Environment Variables

The app requires the following environment variable:

- `CONFIDENCE_API_KEY`: Your Confidence API key (required)

Create a `.env` file in the project root with:
```
CONFIDENCE_API_KEY=your_actual_api_key_here
```

## Project Structure

```
go/
├── runner.go            # Main program (single or multi-run with statistics)
├── go.mod               # Go module dependencies
├── go.sum               # Go module checksums
├── .env                 # Environment variables (create this, not in git)
├── .env.example         # Example environment file
├── .gitignore           # Git ignore rules
└── README.md            # This documentation
```

## Dependencies

- `github.com/open-feature/go-sdk` - Vendor-neutral feature flag API
- `github.com/spotify/confidence-sdk-go` - Confidence SDK with OpenFeature provider
- `github.com/joho/godotenv` - Environment variable management from .env files
- `github.com/google/uuid` - UUID generation for visitor IDs

## Security

- The API key is stored in a `.env` file which is excluded from version control via `.gitignore`
- Never commit your `.env` file to git
- The app will return an error if the API key is not found in the environment
