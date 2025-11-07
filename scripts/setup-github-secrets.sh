#!/bin/bash
# GitHub Secrets Setup Script for Gogidix Ecosystem
# Configures AWS and Docker credentials for GitHub Actions
# NOTE: Replace placeholder values with actual secrets

echo "🔧 Setting up GitHub repository secrets..."

# AWS Secrets - Replace with actual values
echo "Setting AWS_ACCESS_KEY_ID..."
gh secret set AWS_ACCESS_KEY_ID --body "YOUR_AWS_ACCESS_KEY_ID" --repo Gogidix-ecosystem-Saas/-gogidix-ecosystem

echo "Setting AWS_SECRET_ACCESS_KEY..."
gh secret set AWS_SECRET_ACCESS_KEY --body "YOUR_AWS_SECRET_ACCESS_KEY" --repo Gogidix-ecosystem-Saas/-gogidix-ecosystem

echo "Setting AWS_REGION..."
gh secret set AWS_REGION --body "us-east-1" --repo Gogidix-ecosystem-Saas/-gogidix-ecosystem

echo "Setting AWS_ACCOUNT_ID..."
gh secret set AWS_ACCOUNT_ID --body "921332468981" --repo Gogidix-ecosystem-Saas/-gogidix-ecosystem

echo "✅ AWS secrets configured successfully!"

# Docker Cloud Secrets - Replace with actual values
echo "Setting DOCKER_USERNAME..."
gh secret set DOCKER_USERNAME --body "YOUR_DOCKER_USERNAME" --repo Gogidix-ecosystem-Saas/-gogidix-ecosystem

echo "Setting DOCKER_PASSWORD..."
gh secret set DOCKER_PASSWORD --body "YOUR_DOCKER_TOKEN" --repo Gogidix-ecosystem-Saas/-gogidix-ecosystem

echo "✅ Docker Cloud secrets configured successfully!"

echo "🚀 All GitHub secrets configured for infrastructure validation!"