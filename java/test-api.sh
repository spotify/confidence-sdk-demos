#!/bin/bash

# Test script for Plans API
# Run this script after starting the application with: mvn spring-boot:run

echo "🚀 Testing Plans API"
echo "================================"

BASE_URL="http://localhost:8080"

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo ""
echo -e "${BLUE}📦 Testing /plans endpoint${NC}"
echo "--------------------------------"

echo -e "${YELLOW}1. Get all plans${NC}"
curl -s "$BASE_URL/plans" | jq '.'
echo ""

echo -e "${YELLOW}2. Get plans for North America (na)${NC}"
curl -s "$BASE_URL/plans?region=na" | jq '.'
echo ""

echo -e "${YELLOW}3. Get plans for Europe (eu)${NC}"
curl -s "$BASE_URL/plans?region=eu" | jq '.'
echo ""

echo -e "${YELLOW}4. Get plans for Asia Pacific (ap)${NC}"
curl -s "$BASE_URL/plans?region=ap" | jq '.'
echo ""

echo -e "${YELLOW}5. Get plans for South America (sa)${NC}"
curl -s "$BASE_URL/plans?region=sa" | jq '.'
echo ""

echo -e "${YELLOW}6. Get plans for Africa (af)${NC}"
curl -s "$BASE_URL/plans?region=af" | jq '.'
echo ""

echo ""
echo -e "${BLUE}🎯 Testing with visitor IDs (OpenFeature)${NC}"
echo "--------------------------------"

echo -e "${YELLOW}7. Get plans with random UUID visitor ID${NC}"
VISITOR_ID=$(uuidgen)
echo "Using visitor ID: $VISITOR_ID"
curl -s -H "X-VISITOR-ID: $VISITOR_ID" "$BASE_URL/plans?region=na" | jq '.'
echo ""

echo -e "${YELLOW}8. Get plans with timestamp-based visitor ID${NC}"
VISITOR_ID="visitor-$(date +%s)-$(shuf -i 1000-9999 -n 1 2>/dev/null || echo $RANDOM)"
echo "Using visitor ID: $VISITOR_ID"
curl -s -H "X-VISITOR-ID: $VISITOR_ID" "$BASE_URL/plans?region=eu" | jq '.'
echo ""

echo -e "${YELLOW}9. Get plans with specific visitor ID (test-user-1)${NC}"
curl -s -H "X-VISITOR-ID: test-user-1" "$BASE_URL/plans?region=ap" | jq '.'
echo ""

echo -e "${YELLOW}10. Get plans with specific visitor ID (test-user-2)${NC}"
curl -s -H "X-VISITOR-ID: test-user-2" "$BASE_URL/plans?region=sa" | jq '.'
echo ""

echo -e "${YELLOW}11. Get plans without visitor ID (should use 'anonymous')${NC}"
curl -s "$BASE_URL/plans?region=af" | jq '.'
echo ""

echo ""
echo -e "${BLUE}🌍 Testing edge cases${NC}"
echo "--------------------------------"

echo -e "${YELLOW}12. Test with invalid region${NC}"
curl -s "$BASE_URL/plans?region=invalid" | jq '.'
echo ""

echo -e "${YELLOW}13. Test with empty region parameter${NC}"
curl -s "$BASE_URL/plans?region=" | jq '.'
echo ""

echo -e "${YELLOW}14. Test with empty visitor ID header${NC}"
curl -s -H "X-VISITOR-ID: " "$BASE_URL/plans?region=na" | jq '.'
echo ""

echo ""
echo -e "${GREEN}✅ All tests completed!${NC}"
echo ""
echo "Note: The enterprise plan may or may not appear depending on the"
echo "OpenFeature flag evaluation for 'show-enterprise-plan'."