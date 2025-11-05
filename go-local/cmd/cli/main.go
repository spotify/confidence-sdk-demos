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
	"github.com/open-feature/go-sdk/openfeature"
	"github.com/spotify/confidence-resolver-rust/openfeature-provider/go/confidence"
)

// EvaluationResult contains the details of a feature flag evaluation
type EvaluationResult struct {
	VisitorID string
	Region    string
	FlagValue string
}

// RunStats tracks statistics across runs
type RunStats struct {
	TotalRuns      int
	SuccessfulRuns int
	Regions        map[string]int
	FlagValues     map[string]int
}

// setup initializes the OpenFeature API with the local Confidence Provider
func setup(ctx context.Context) error {
	// Load environment variables from .env file
	if err := godotenv.Load(); err != nil {
		// .env file is optional, might be set via environment
		log.Println("No .env file found, using environment variables")
	}

	// Get credentials from environment variables
	apiClientID := os.Getenv("CONFIDENCE_API_CLIENT_ID")
	apiClientSecret := os.Getenv("CONFIDENCE_API_CLIENT_SECRET")
	clientSecret := os.Getenv("CONFIDENCE_CLIENT_SECRET")

	if apiClientID == "" || apiClientSecret == "" || clientSecret == "" {
		return fmt.Errorf("CONFIDENCE_API_CLIENT_ID, CONFIDENCE_API_CLIENT_SECRET, and CONFIDENCE_CLIENT_SECRET environment variables are required. Please set them in your .env file")
	}

	// Initialize local Confidence Provider (with WebAssembly-based local resolution)
	provider, err := confidence.NewProvider(ctx, confidence.ProviderConfig{
		APIClientID:     apiClientID,
		APIClientSecret: apiClientSecret,
		ClientSecret:    clientSecret,
	})
	if err != nil {
		return fmt.Errorf("failed to create Confidence provider: %w", err)
	}

	// Set the provider and wait for it to be ready
	openfeature.SetProviderAndWait(provider)

	return nil
}

// evaluateFeatureFlag performs a single feature flag evaluation
func evaluateFeatureFlag(ctx context.Context) (*EvaluationResult, error) {
	// Get OpenFeature client
	client := openfeature.NewClient("go-local-demo")

	// Generate a random visitor ID for this invocation
	visitorID := uuid.New().String()

	// Define regions and randomly select one (matching flag targeting rules)
	regions := []string{"na", "eu", "asia"}
	selectedRegion := regions[rand.Intn(len(regions))]

	// Create evaluation context with the visitor ID and region
	evaluationContext := openfeature.NewEvaluationContext(
		visitorID,
		map[string]interface{}{
			"visitor_id": visitorID,
			"region":     selectedRegion,
		},
	)

	// Resolve the feature flag using OpenFeature API with local provider
	flagValue, err := client.StringValue(
		ctx,
		"show-enterprise-plan.price",
		"default",
		evaluationContext,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to evaluate feature flag: %w", err)
	}

	// Return evaluation details
	return &EvaluationResult{
		VisitorID: visitorID,
		Region:    selectedRegion,
		FlagValue: flagValue,
	}, nil
}

func runEvaluations(ctx context.Context, numRuns int, verbose bool) *RunStats {
	fmt.Printf("Starting %d feature flag evaluations with local provider...\n", numRuns)
	fmt.Println("==================================================")

	stats := &RunStats{
		TotalRuns:  numRuns,
		Regions:    make(map[string]int),
		FlagValues: make(map[string]int),
	}

	for i := 0; i < numRuns; i++ {
		runStart := time.Now()

		// Evaluate the feature flag
		result, err := evaluateFeatureFlag(ctx)
		if err != nil {
			fmt.Printf("Run %2d/%d: ERROR - %v\n", i+1, numRuns, err)
			continue
		}

		stats.SuccessfulRuns++
		stats.Regions[result.Region]++
		stats.FlagValues[result.FlagValue]++

		if verbose {
			fmt.Printf("Run %2d/%d: Region=%s | Flag=%-12s | Visitor=%s... | Time=%s\n",
				i+1, numRuns,
				result.Region,
				result.FlagValue,
				result.VisitorID[:8],
				runStart.Format("15:04:05"))
		} else {
			fmt.Printf("Run %2d/%d: %s -> %s\n", i+1, numRuns, result.Region, result.FlagValue)
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

	// Print unique regions
	regions := make([]string, 0, len(stats.Regions))
	for region := range stats.Regions {
		regions = append(regions, region)
	}
	sort.Strings(regions)
	fmt.Printf("Regions: %v\n", regions)

	// Print unique flag values
	flagValues := make([]string, 0, len(stats.FlagValues))
	for value := range stats.FlagValues {
		flagValues = append(flagValues, value)
	}
	sort.Strings(flagValues)
	fmt.Printf("Flag values: %v\n", flagValues)

	// Print region distribution
	fmt.Println("\nRegion distribution:")
	for _, region := range regions {
		count := stats.Regions[region]
		percentage := float64(count) / float64(stats.SuccessfulRuns) * 100
		fmt.Printf("  %s: %2d (%5.1f%%)\n", region, count, percentage)
	}
}

func main() {
	// Parse command line arguments
	numRuns := flag.Int("n", 10, "Number of evaluations to perform")
	verbose := flag.Bool("v", false, "Verbose output")
	quiet := flag.Bool("q", false, "Quiet mode - less verbose output")
	flag.Parse()

	// Validate arguments
	if *numRuns <= 0 {
		log.Fatal("Error: Number of runs must be positive")
	}

	// Determine verbosity
	verboseMode := *verbose
	if *quiet {
		verboseMode = false
	} else if !*verbose {
		verboseMode = true // Default to verbose
	}

	// Seed the random number generator
	rand.Seed(time.Now().UnixNano())

	// Create context
	ctx := context.Background()

	// Setup the environment
	if err := setup(ctx); err != nil {
		log.Fatalf("Setup failed: %v", err)
	}

	// Run evaluations
	stats := runEvaluations(ctx, *numRuns, verboseMode)

	// Print summary
	printSummary(stats)
	openfeature.Shutdown()
	fmt.Println("OpenFeature shutdown complete")
	os.Exit(0)
}
