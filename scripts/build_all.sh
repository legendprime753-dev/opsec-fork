#!/usr/bin/env bash
set -euo pipefail

# Script to build all Stonecutter versions and collect release JARs for Modrinth
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"
DIST_DIR="$ROOT_DIR/dist"

cd "$ROOT_DIR"

echo "=================================================="
echo "      Building OpSec (Fork) for Modrinth          "
echo "=================================================="

mkdir -p "$DIST_DIR"
rm -f "$DIST_DIR"/*.jar

JAVA_MAJOR=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)

# Auto-detect JDK 25 from Gradle cache if system java is < 25
if [ "$JAVA_MAJOR" -lt 25 ] 2>/dev/null; then
    JDK25_DIR=$(find "$HOME/.gradle/jdks" -maxdepth 1 -type d -name "*25*" 2>/dev/null | head -n 1)
    if [ -n "$JDK25_DIR" ] && [ -x "$JDK25_DIR/bin/java" ]; then
        echo "Using downloaded JDK 25 at: $JDK25_DIR"
        export JAVA_HOME="$JDK25_DIR"
        export PATH="$JAVA_HOME/bin:$PATH"
        JAVA_MAJOR=25
    fi
fi

echo "Active Java version: $(java -version 2>&1 | head -n 1)"
echo "[1/2] Compiling all version targets (1.20.1 to 26.2)..."
./gradlew remapAll

echo "[2/2] Collecting release JARs into dist/..."
find versions -path "*/build/libs/opsec-*.jar" ! -name "*-sources.jar" | while read -r jarfile; do
    fname=$(basename "$jarfile")
    cp -u "$jarfile" "$DIST_DIR/"
    echo "  -> Ready: $fname ($(du -h "$jarfile" | cut -f1))"
done

echo ""
echo "=================================================="
echo "  Build Complete! All 11 Modrinth JARs in dist/:   "
echo "=================================================="
ls -lh "$DIST_DIR"/opsec-*.jar
echo "=================================================="
echo "These files are ready to be uploaded to Modrinth!"
