#!/bin/bash
# Resolve the absolute path to TARGET_DIR relative to script location
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TARGET_DIR="$(cd "$SCRIPT_DIR/../Code" && pwd)"

# Change working directory directly into TARGET_DIR
cd "$TARGET_DIR" || exit 1

# Run TestAllHyperparameters
java TestAllHyperparameters