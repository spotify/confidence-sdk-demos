package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
	"github.com/open-feature/go-sdk/openfeature"
	"github.com/spotify/confidence-resolver-rust/openfeature-provider/go/confidence"
)

// Response represents the JSON response structure
type Response struct {
	VisitorID string `json:"visitor_id"`
	Region    string `json:"region"`
	FlagValue string `json:"flag_value"`
	Message   string `json:"message"`
	Timestamp string `json:"timestamp"`
}

// ErrorResponse represents an error response
type ErrorResponse struct {
	Error   string `json:"error"`
	Message string `json:"message"`
}

var client *openfeature.Client

// setupProvider initializes the OpenFeature API with the local Confidence Provider
func setupProvider() error {
	// Load environment variables from .env file
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found, using environment variables")
	}

	// Get credentials from environment variables
	apiClientID := os.Getenv("CONFIDENCE_API_CLIENT_ID")
	apiClientSecret := os.Getenv("CONFIDENCE_API_CLIENT_SECRET")
	clientSecret := os.Getenv("CONFIDENCE_CLIENT_SECRET")

	if apiClientID == "" || apiClientSecret == "" || clientSecret == "" {
		return fmt.Errorf("CONFIDENCE_API_CLIENT_ID, CONFIDENCE_API_CLIENT_SECRET, and CONFIDENCE_CLIENT_SECRET environment variables are required")
	}

	// Initialize local Confidence Provider
	ctx := context.Background()
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

	// Get OpenFeature client
	client = openfeature.NewClient("go-local-service")

	log.Println("Confidence provider initialized successfully")
	return nil
}

// evaluateHandler handles flag evaluation requests
func evaluateHandler(c *gin.Context) {
	// Get visitor_id from header
	visitorID := c.GetHeader("X-Visitor-ID")
	if visitorID == "" {
		c.JSON(400, ErrorResponse{
			Error:   "missing_visitor_id",
			Message: "X-Visitor-ID header is required",
		})
		return
	}

	// Get region from query parameter
	region := c.DefaultQuery("region", "default")

	// Create evaluation context
	evaluationContext := openfeature.NewEvaluationContext(
		visitorID,
		map[string]interface{}{
			"visitor_id": visitorID,
			"region":     region,
		},
	)

	// Evaluate the feature flag
	flagValue, err := client.StringValue(
		c.Request.Context(),
		"show-enterprise-plan.price",
		"default",
		evaluationContext,
	)
	if err != nil {
		log.Printf("Flag evaluation failed for visitor %s: %v", visitorID, err)
		c.JSON(500, ErrorResponse{
			Error:   "evaluation_failed",
			Message: fmt.Sprintf("Failed to evaluate feature flag: %v", err),
		})
		return
	}

	// Create response message based on flag value
	message := fmt.Sprintf("Enterprise plan price for region '%s': %s", region, flagValue)

	// Build and send response
	c.JSON(200, Response{
		VisitorID: visitorID,
		Region:    region,
		FlagValue: flagValue,
		Message:   message,
		Timestamp: time.Now().Format(time.RFC3339),
	})

	log.Printf("Evaluated flag for visitor=%s region=%s value=%s", visitorID, region, flagValue)
}

// healthHandler handles health check requests
func healthHandler(c *gin.Context) {
	c.JSON(200, gin.H{
		"status": "healthy",
		"time":   time.Now().Format(time.RFC3339),
	})
}

func main() {
	// Setup the provider
	if err := setupProvider(); err != nil {
		log.Fatalf("Failed to setup provider: %v", err)
	}

	// Set Gin to release mode for production
	gin.SetMode(gin.ReleaseMode)

	// Create Gin router
	router := gin.Default()

	// Setup routes
	router.GET("/evaluate", evaluateHandler)
	router.GET("/health", healthHandler)

	// Get port from environment
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}

	// Start server in a goroutine
	go func() {
		log.Printf("Starting server on port %s...", port)
		log.Printf("Endpoints:")
		log.Printf("  GET /evaluate?region=<region> (requires X-Visitor-ID header)")
		log.Printf("  GET /health")
		if err := router.Run(":" + port); err != nil {
			log.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	log.Println("Shutting down server...")

	// Shutdown OpenFeature
	openfeature.Shutdown()
	log.Println("Server stopped")
}
