package main

import (
	"context"
	"flag"
	"fmt"
	"log"
	"math/rand"
	"os"
	"sort"
	"time"

	"github.com/google/uuid"
	"github.com/joho/godotenv"
	o "github.com/open-feature/go-sdk/openfeature"
	c "github.com/spotify/confidence-sdk-go/pkg/confidence"
	p "github.com/spotify/confidence-sdk-go/pkg/provider"
)

// EvaluationResult contains the details of a feature flag evaluation
type EvaluationResult struct {
	VisitorID string
	Country   string
	FlagValue string
}

// RunConfig holds configuration for the runner
type RunConfig struct {
	NumRuns   int
	MinSleep  float64 // milliseconds
	MaxSleep  float64 // milliseconds
	Verbose   bool
	FastMode  bool
}

// RunStats tracks statistics across runs
type RunStats struct {
	TotalRuns      int
	SuccessfulRuns int
	Countries      map[string]int
	FlagValues     map[string]int
}

// setup initializes the OpenFeature API with Confidence Provider
func setup() error {
	// Load environment variables from .env file
	if err := godotenv.Load(); err != nil {
		// .env file is optional, might be set via environment
		log.Println("No .env file found, using environment variables")
	}

	// Get API key from environment variable
	apiKey := os.Getenv("CONFIDENCE_API_KEY")
	if apiKey == "" {
		return fmt.Errorf("CONFIDENCE_API_KEY environment variable is required. Please set it in your .env file")
	}

	// Initialize Confidence SDK
	confidenceSdk := c.NewConfidenceBuilder().
		SetAPIConfig(*c.NewAPIConfig(apiKey)).
		Build()

	// Initialize OpenFeature API with Confidence Provider
	confidenceProvider := p.NewFlagProvider(confidenceSdk)
	o.SetProvider(confidenceProvider)

	return nil
}

// evaluateFeatureFlag performs a single feature flag evaluation
func evaluateFeatureFlag() (*EvaluationResult, error) {
	// Get OpenFeature client
	client := o.NewClient("go-demo")

	// Generate a random visitor ID for this invocation
	visitorID := uuid.New().String()

	// Define country codes and randomly select one
	countryCodes := []string{"US", "SE", "GB"}
	selectedCountry := countryCodes[rand.Intn(len(countryCodes))]

	// Create evaluation context with the visitor ID and country
	attributes := map[string]interface{}{
		"visitor_id": visitorID,
		"country":    selectedCountry,
	}
	evaluationContext := o.NewEvaluationContext(visitorID, attributes)

	// Resolve the feature flag using OpenFeature API
	flagValue, err := client.StringValue(
		context.Background(),
		"nicklas-test-flag.message",
		"default",
		evaluationContext,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to evaluate feature flag: %w", err)
	}

	// Return evaluation details
	return &EvaluationResult{
		VisitorID: visitorID,
		Country:   selectedCountry,
		FlagValue: flagValue,
	}, nil
}

func main() {
	// Parse command line arguments
	config := parseFlags()

	// Seed the random number generator
	rand.Seed(time.Now().UnixNano())

	// Setup the environment
	if err := setup(); err != nil {
		log.Fatalf("Setup failed: %v", err)
	}

	// Single run mode - simplified output
	if config.NumRuns == 1 {
		result, err := evaluateFeatureFlag()
		if err != nil {
			log.Fatalf("Evaluation failed: %v", err)
		}
		fmt.Printf("Visitor ID: %s\n", result.VisitorID)
		fmt.Printf("Country: %s\n", result.Country)
		fmt.Printf("Feature flag value: %s\n", result.FlagValue)
		return
	}

	// Multi-run mode with statistics
	stats := runEvaluations(config)
	printSummary(stats)
}

func parseFlags() *RunConfig {
	config := &RunConfig{}

	flag.IntVar(&config.NumRuns, "n", 1, "Number of evaluations to perform")
	flag.IntVar(&config.NumRuns, "num-runs", 1, "Number of evaluations to perform")
	flag.Float64Var(&config.MinSleep, "min-sleep", 1.0, "Minimum sleep time between runs in milliseconds")
	flag.Float64Var(&config.MaxSleep, "max-sleep", 25.0, "Maximum sleep time between runs in milliseconds")
	flag.BoolVar(&config.Verbose, "q", false, "Quiet mode - less verbose output")
	flag.BoolVar(&config.FastMode, "fast", false, "Fast mode - minimal sleep (1-5 milliseconds)")

	flag.Parse()

	// Invert quiet flag to verbose
	config.Verbose = !config.Verbose

	// Handle fast mode
	if config.FastMode {
		config.MinSleep = 1.0
		config.MaxSleep = 5.0
	}

	// Validate arguments
	if config.NumRuns <= 0 {
		log.Fatal("Error: Number of runs must be positive")
	}
	if config.MinSleep < 0 || config.MaxSleep < 0 {
		log.Fatal("Error: Sleep times must be non-negative")
	}
	if config.MinSleep > config.MaxSleep {
		log.Fatal("Error: Minimum sleep time cannot be greater than maximum sleep time")
	}

	return config
}

func runEvaluations(config *RunConfig) *RunStats {
	fmt.Printf("Starting %d feature flag evaluations...\n", config.NumRuns)
	fmt.Printf("Sleep interval: %.0f-%.0f milliseconds\n", config.MinSleep, config.MaxSleep)
	fmt.Println("==================================================")

	stats := &RunStats{
		TotalRuns:  config.NumRuns,
		Countries:  make(map[string]int),
		FlagValues: make(map[string]int),
	}

	for i := 0; i < config.NumRuns; i++ {
		runStart := time.Now()

		// Evaluate the feature flag
		result, err := evaluateFeatureFlag()
		if err != nil {
			fmt.Printf("Run %2d/%d: ERROR - %v\n", i+1, config.NumRuns, err)
			continue
		}

		stats.SuccessfulRuns++
		stats.Countries[result.Country]++
		stats.FlagValues[result.FlagValue]++

		if config.Verbose {
			fmt.Printf("Run %2d/%d: Country=%s | Flag=%-8s | Visitor=%s... | Time=%s\n",
				i+1, config.NumRuns,
				result.Country,
				result.FlagValue,
				result.VisitorID[:8],
				runStart.Format("15:04:05"))
		} else {
			fmt.Printf("Run %2d/%d: %s -> %s\n", i+1, config.NumRuns, result.Country, result.FlagValue)
		}

		// Sleep before next run (except for the last run)
		if i < config.NumRuns-1 {
			sleepTimeMs := config.MinSleep + rand.Float64()*(config.MaxSleep-config.MinSleep)
			sleepDuration := time.Duration(sleepTimeMs * float64(time.Millisecond))
			if config.Verbose {
				fmt.Printf("    Sleeping for %.1f milliseconds...\n", sleepTimeMs)
			}
			time.Sleep(sleepDuration)
			fmt.Println() // Empty line for readability
		}
	}

	fmt.Println("==================================================")
	fmt.Printf("Completed %d successful evaluations out of %d attempts\n",
		stats.SuccessfulRuns, stats.TotalRuns)

	return stats
}

func printSummary(stats *RunStats) {
	if stats.SuccessfulRuns == 0 {
		return
	}

	fmt.Println("\nSummary:")

	// Print unique countries
	countries := make([]string, 0, len(stats.Countries))
	for country := range stats.Countries {
		countries = append(countries, country)
	}
	sort.Strings(countries)
	fmt.Printf("Countries: %v\n", countries)

	// Print unique flag values
	flagValues := make([]string, 0, len(stats.FlagValues))
	for value := range stats.FlagValues {
		flagValues = append(flagValues, value)
	}
	sort.Strings(flagValues)
	fmt.Printf("Flag values: %v\n", flagValues)

	// Print country distribution
	fmt.Println("\nCountry distribution:")
	for _, country := range countries {
		count := stats.Countries[country]
		percentage := float64(count) / float64(stats.SuccessfulRuns) * 100
		fmt.Printf("  %s: %2d (%5.1f%%)\n", country, count, percentage)
	}
}
