# Notice & Attribution

This project is a fork of **OpSec**, originally created and developed by **Aurick** ([@aurickk](https://github.com/aurickk)).

- **Original Repository**: https://github.com/aurickk/OpSec
- **Original Modrinth Project**: https://modrinth.com/mod/opsec
- **Original License**: GNU General Public License v3.0 (GPL-3.0)

## Fork Information & Modifications
In compliance with Section 5 of the GNU General Public License v3.0:
- **Fork Maintainer**: legendprime753-dev
- **Fork Repository**: https://github.com/legendprime753-dev/opsec-fork
- **Fork Project URL**: https://modrinth.com/mod/opsec-fork
- **Date of Fork Modifications**: September 2026

### Key Changes in this Fork:
1. **Neutralized Upstream Integrity Checker**:
   - Disabled `JarIntegrityChecker` validation against upstream `aurickk/OpSec` GitHub releases to eliminate false-positive tamper warnings ("Jar integrity check failed / computer compromised") on fork builds.
2. **Modrinth Update Integration**:
   - Reconfigured `UpdateChecker` to query the Modrinth API (`opsec-fork`) rather than upstream GitHub releases.
3. **Modrinth Readiness & Multi-Version Packaging**:
   - Updated `fabric.mod.json` metadata with appropriate fork credits and links.
   - Added automated multi-target build script (`scripts/build_all.sh`) and GitHub Actions publishing workflow (`.github/workflows/publish-modrinth.yml`).
   - Prepared `MODRINTH.md` documentation in compliance with Modrinth Content Rules for forks and community editions.
