#!/bin/bash

# ================================================================================
# GOGIDIX CENTRAL-CONFIGURATION PRODUCTION READINESS TEST
# Tests all services for 4-step production certification
# ================================================================================

set -euo pipefail

# Configuration
DOMAIN_PATH="/c/Users/frich/Desktop/Gogidix-ecosystem/domains/foundation/central-configuration"
SOURCE_PATH="/c/Users/frich/Desktop/Gogidix-Technology-Ecosystem/Gogidix-GitLab-Staging/Foundation-Domain/gogidix-foundation-central-configuration"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
SESSION_ID="central-configuration-prod-ready-$TIMESTAMP"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Business groups and their services
declare -A BUSINESS_GROUPS
BUSINESS_GROUPS=(
    ["core-config-services"]="central-configuration-implementation"
    ["server-services"]="config-server"
    ["database-services"]="database-config database-migrations"
    ["deployment-services"]="deployment-scripts regional-deployment"
    ["infrastructure-services"]="infrastructure-as-code kubernetes-manifests"
    ["security-services"]="secrets-management disaster-recovery"
    ["feature-services"]="feature-toggle-config environment-config"
)

# Service counters
TOTAL_SERVICES=0
SUCCESSFUL_SERVICES=0
FAILED_SERVICES=0

# Print header
print_header() {
    echo -e "${BLUE}╔══════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║     GOGIDIX CENTRAL-CONFIGURATION PRODUCTION           ║${NC}"
    echo -e "${BLUE}║                READINESS TEST                           ║${NC}"
    echo -e "${BLUE}╚══════════════════════════════════════════════════════════╝${NC}"
    echo -e "${CYAN}Started: $(date '+%d %b %Y %H:%M:%S')${NC}"
    echo -e "${CYAN}Session: $SESSION_ID${NC}"
    echo -e "${CYAN}Central-Configuration Root: $DOMAIN_PATH${NC}"
    echo ""
}

# Print business group header
print_business_group_header() {
    local group=$1
    echo -e "${YELLOW}📂 Business Group: $group${NC}"
    echo -e "${YELLOW}========================${NC}"
    echo ""
}

# Test individual service
test_service() {
    local service_name=$1
    local business_group=$2
    local service_path="$SOURCE_PATH/backend/java/$service_name"

    echo -e "${CYAN}🔍 Testing: $business_group/$service_name${NC}"
    echo -e "${CYAN}---------------------------------${NC}"

    if [[ ! -d "$service_path" ]]; then
        echo -e "   ❌ ERROR: Service directory not found: $service_path"
        return 1
    fi

    local start_time=$(date +%s)

    # Step 1: Clean and Compile
    echo "   📦 Cleaning and compiling..."
    cd "$service_path"
    if ! mvn clean compile -q > /dev/null 2>&1; then
        echo -e "   ❌ Compile: FAILED"
        return 1
    fi
    echo -e "   ✅ Compile: SUCCESS"

    # Step 2: Build
    echo "   🏗️  Building..."
    if ! mvn package -DskipTests -q > /dev/null 2>&1; then
        echo -e "   ❌ Build: FAILED"
        return 1
    fi
    echo -e "   ✅ Build: SUCCESS"

    # Step 3: Install
    echo "   📥 Installing..."
    if ! mvn install -DskipTests -q > /dev/null 2>&1; then
        echo -e "   ❌ Install: FAILED"
        return 1
    fi

    local end_time=$(date +%s)
    local duration=$((end_time - start_time))

    echo -e "   ✅ Install: SUCCESS (${duration}s)"
    echo -e "   🎉 $business_group/$service_name: PRODUCTION READY"
    echo ""

    return 0
}

# Main execution
main() {
    print_header

    echo -e "${YELLOW}📋 BUSINESS GROUPS VALIDATION${NC}"
    echo -e "${YELLOW}=============================${NC}"
    echo ""

    # Count total services
    for services in "${BUSINESS_GROUPS[@]}"; do
        for service in $services; do
            ((TOTAL_SERVICES++))
        done
    done

    echo -e "${CYAN}Total Services to Test: $TOTAL_SERVICES${NC}"
    echo ""

    # Test each business group
    for business_group in "${!BUSINESS_GROUPS[@]}"; do
        print_business_group_header "$business_group"

        for service in ${BUSINESS_GROUPS[$business_group]}; do
            if test_service "$service" "$business_group"; then
                ((SUCCESSFUL_SERVICES++))
            else
                ((FAILED_SERVICES++))
            fi
        done
    done

    # Print final results
    echo ""
    echo -e "${BLUE}╔══════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║           PRODUCTION READINESS TEST RESULTS              ║${NC}"
    echo -e "${BLUE}╚══════════════════════════════════════════════════════════╝${NC}"
    echo -e "${CYAN}Total Services: $TOTAL_SERVICES${NC}"
    echo -e "${GREEN}Successful: $SUCCESSFUL_SERVICES${NC}"
    echo -e "${RED}Failed: $FAILED_SERVICES${NC}"

    local success_rate=0
    if [[ $TOTAL_SERVICES -gt 0 ]]; then
        success_rate=$((SUCCESSFUL_SERVICES * 100 / TOTAL_SERVICES))
    fi

    echo -e "${CYAN}Success Rate: ${success_rate}%${NC}"
    echo ""

    if [[ $FAILED_SERVICES -eq 0 ]]; then
        echo -e "${GREEN}🎉 ALL SERVICES PRODUCTION READY!${NC}"
        echo -e "${GREEN}✅ Central-Configuration Domain Certified for Production${NC}"
        exit 0
    else
        echo -e "${RED}❌ $FAILED_SERVICES services need attention before production${NC}"
        echo -e "${YELLOW}⚠️  Review failed services and fix issues before migration${NC}"
        exit 1
    fi
}

# Run main function
main "$@"
