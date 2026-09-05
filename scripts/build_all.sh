#!/usr/bin/env bash
set -euo pipefail

# Script to build all compatible Stonecutter versions and collect release JARs for Modrinth
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
echo "Current Java version detected: $JAVA_MAJOR"

# Standard versions that build on Java 21+
JAVA21_VERSIONS=(
    ":1.20.1:remapJar"
    ":1.20.2:remapJar"
    ":1.20.4:remapJar"
    ":1.20.6:remapJar"
    ":1.21.1:remapJar"
    ":1.21.4:remapJar"
    ":1.21.6:remapJar"
    ":1.21.9:remapJar"
    ":1.21.11:remapJar"
)

# Future versions that require Java 25+
JAVA25_VERSIONS=(
    ":26.1:jar"
    ":26.2:jar"
)

echo "[1/2] Compiling release targets with Gradle..."

# Always build the standard versions
./gradlew "${JAVA21_VERSIONS[@]}"

# Build 26.x if Java 25+ is installed
if [ "$JAVA_MAJOR" -ge 25 ] 2>/dev/null; then
    echo "Building Minecraft 26.x targets (Java $JAVA_MAJOR)..."
    ./gradlew "${JAVA25_VERSIONS[@]}"
else
    echo "Note: Minecraft 26.x targets require Java 25 (current is $JAVA_MAJOR). Skipping 26.x in local build. (GitHub Actions will build all versions including 26.x with Java 25)."
fi

echo "[2/2] Collecting release JARs into dist/..."
find versions -path "*/build/libs/opsec-*.jar" ! -name "*-sources.jar" | while read -r jarfile; do
    fname=$(basename "$jarfile")
    cp -u "$jarfile" "$DIST_DIR/"
    echo "  -> Ready: $fname ($(du -h "$jarfile" | cut -f1))"
done

echo ""
echo "=================================================="
echo "  Build Complete! Modrinth Release JARs in dist/: "
echo "=================================================="
ls -lh "$DIST_DIR"/opsec-*.jar
echo "=================================================="
echo "These files are ready to be uploaded to Modrinth!"
