#!/bin/bash

# Configuration
BASE_URL="${BASE_URL:-http://localhost:8080}"
REGIONS=("na" "eu" "asia")

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to generate a UUID (works on both macOS and Linux)
generate_uuid() {
    if command -v uuidgen &> /dev/null; then
        # macOS and some Linux systems have uuidgen
        uuidgen | tr '[:upper:]' '[:lower:]'
    elif [ -f /proc/sys/kernel/random/uuid ]; then
        # Linux systems have this
        cat /proc/sys/kernel/random/uuid
    else
        # Fallback: use date and random number
        echo "$(date +%s)-$(( RANDOM % 10000 ))-$(( RANDOM % 10000 ))-$(( RANDOM % 10000 ))-$(( RANDOM % 100000000 ))"
    fi
}

# Function to make a request
make_request() {
    local region=$1
    local visitor_id=$(generate_uuid)

    echo -e "${BLUE}========================================${NC}"
    echo -e "${YELLOW}Request Details:${NC}"
    echo -e "  Visitor ID: ${GREEN}${visitor_id}${NC}"
    echo -e "  Region:     ${GREEN}${region}${NC}"
    echo ""

    # Make the curl request
    response=$(curl -s -w "\nHTTP_CODE:%{http_code}" \
        -H "X-Visitor-ID: ${visitor_id}" \
        "${BASE_URL}/evaluate?region=${region}")

    # Extract HTTP code and body
    http_code=$(echo "$response" | grep "HTTP_CODE:" | cut -d':' -f2)
    body=$(echo "$response" | sed '/HTTP_CODE:/d')

    # Check if request was successful
    if [ "$http_code" == "200" ]; then
        echo -e "${GREEN}✓ Success (HTTP $http_code)${NC}"
        echo -e "${YELLOW}Response:${NC}"
        echo "$body" | jq '.' 2>/dev/null || echo "$body"
    else
        echo -e "${RED}✗ Failed (HTTP $http_code)${NC}"
        echo -e "${RED}Response:${NC}"
        echo "$body" | jq '.' 2>/dev/null || echo "$body"
    fi

    echo ""
}

# Function to run multiple requests
run_multiple() {
    local count=${1:-5}
    local sleep_time=${2:-1}

    echo -e "${BLUE}╔════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║  Confidence Feature Flag Demo Client  ║${NC}"
    echo -e "${BLUE}╔════════════════════════════════════════╗${NC}"
    echo -e "${YELLOW}Running $count requests...${NC}"
    echo ""

    for i in $(seq 1 $count); do
        # Pick a random region
        region_index=$((RANDOM % ${#REGIONS[@]}))
        region=${REGIONS[$region_index]}

        echo -e "${BLUE}Request $i/$count${NC}"
        make_request "$region"

        # Sleep between requests (except for the last one)
        if [ $i -lt $count ]; then
            sleep $sleep_time
        fi
    done

    echo -e "${GREEN}✓ Completed $count requests${NC}"
}

# Function to show usage
show_usage() {
    cat << EOF
Usage: $0 [OPTIONS]

A client script to test the Confidence feature flag service.

OPTIONS:
    -n <count>      Number of requests to make (default: 5)
    -s <seconds>    Sleep time between requests in seconds (default: 1)
    -r <region>     Specific region to test (na, eu, asia)
    -u <url>        Base URL of the service (default: http://localhost:8080)
    -h              Show this help message

EXAMPLES:
    # Run 5 requests with 1 second sleep
    $0

    # Run 10 requests with 2 second sleep
    $0 -n 10 -s 2

    # Test a specific region
    $0 -r eu -n 3

    # Use a different service URL
    $0 -u http://localhost:9000

ENVIRONMENT VARIABLES:
    BASE_URL        Override the default base URL

EOF
}

# Parse command line arguments
NUM_REQUESTS=5
SLEEP_TIME=1
SPECIFIC_REGION=""

while getopts "n:s:r:u:h" opt; do
    case $opt in
        n)
            NUM_REQUESTS=$OPTARG
            ;;
        s)
            SLEEP_TIME=$OPTARG
            ;;
        r)
            SPECIFIC_REGION=$OPTARG
            ;;
        u)
            BASE_URL=$OPTARG
            ;;
        h)
            show_usage
            exit 0
            ;;
        \?)
            echo "Invalid option: -$OPTARG" >&2
            show_usage
            exit 1
            ;;
    esac
done

# Check if jq is available (for pretty JSON output)
if ! command -v jq &> /dev/null; then
    echo -e "${YELLOW}Warning: 'jq' not found. JSON output will not be formatted.${NC}"
    echo -e "${YELLOW}Install jq for better output: brew install jq (macOS) or apt-get install jq (Linux)${NC}"
    echo ""
fi

# Check if service is running
if ! curl -s "${BASE_URL}/health" > /dev/null 2>&1; then
    echo -e "${RED}Error: Service is not running at ${BASE_URL}${NC}"
    echo -e "${YELLOW}Start the service with: go run service.go${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Service is running at ${BASE_URL}${NC}"
echo ""

# Run requests
if [ -n "$SPECIFIC_REGION" ]; then
    echo -e "${YELLOW}Testing specific region: ${SPECIFIC_REGION}${NC}"
    for i in $(seq 1 $NUM_REQUESTS); do
        echo -e "${BLUE}Request $i/$NUM_REQUESTS${NC}"
        make_request "$SPECIFIC_REGION"
        if [ $i -lt $NUM_REQUESTS ]; then
            sleep $SLEEP_TIME
        fi
    done
else
    run_multiple $NUM_REQUESTS $SLEEP_TIME
fi
