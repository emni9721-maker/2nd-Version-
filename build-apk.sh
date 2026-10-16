#!/usr/bin/env bash
set -e

echo "=== Building ZERO Android APK ==="

# Restore keystore if missing
if [ ! -f "debug.keystore" ] && [ -f "debug.keystore.base64" ]; then
  echo "Restoring debug.keystore..."
  base64 -d debug.keystore.base64 > debug.keystore 2>/dev/null || true
fi

# Ensure .env exists
if [ ! -f ".env" ] && [ -f ".env.example" ]; then
  cp .env.example .env
fi

# Set execute permissions
chmod +x ./gradlew

# Build debug APK
./gradlew assembleDebug --no-daemon

echo "=== SUCCESS! APK generated at: ==="
echo "app/build/outputs/apk/debug/app-debug.apk"
