# OpSec (Fork)

<p align="center">
  <img src="https://raw.githubusercontent.com/aurickk/OpSec/main/src/main/resources/assets/opsec/icon.png" alt="OpSec Logo" width="128" height="128"/>
</p>

<p align="center">
  <strong>Client-side privacy protection, brand spoofing, and tracking exploit defenses for modern Minecraft.</strong>
</p>

---

## 📢 Fork Notice & Attribution

> [!IMPORTANT]
> **This is an independently maintained community fork of [OpSec by Aurick](https://modrinth.com/mod/opsec)** ([GitHub Repository](https://github.com/aurickk/OpSec)).
>
> All original concept, design, and code credits belong to **[Aurick](https://github.com/aurickk)**. This fork is licensed under the **GNU General Public License v3.0 (GPL-3.0)** in full compliance with open-source distribution rules.

### Why this Fork?
- **Removes False-Positive Tamper Warnings**: Upstream OpSec contains a strict SHA-256 hash check hardcoded against Aurick's GitHub releases. On custom builds and forks, this caused a terrifying red warning screen (*"Your computer may be compromised"*). This fork safely neutralizes that check.
- **Native Modrinth Integration**: Update checking points directly to Modrinth releases.
- **Multi-Version Packaging & Maintenance**: Maintained builds across supported Minecraft versions from 1.20.1 to 26.2.

---

## 🛡️ Key Features

- **Spoof as Vanilla**: Set client brand identifier to vanilla Minecraft and block server-side mod detection.
- **Channel Spoofing**: Conditionally block mod network channels during server handshake to prevent passive mod fingerprinting.
- **Known-Pack Filtering**: Conditionally strip built-in server pack identifiers from the configuration handshake.
- **Isolate Pack Cache**: Isolate server resource packs per account to prevent cross-account tracking and identification.
- **Block Local URLs**: Block malicious resource pack redirects to private/local network addresses (SSRF protection).
- **Bypass Server Pack Requirement**: Regain control over server resource packs. Choose whether to accept, ask, or ignore forced packs.
- **Strip Mod Shader Overrides**: Protect against server resource packs attempting to probe or override client-side shader assets.
- **Key Resolution Protection**: Protect against key resolution exploits in incoming server packets used to detect installed mods.
- **Meteor Client Fix**: Safely cancels Meteor Client's broken key resolution protection mixin to allow OpSec's proper handling.
- **Mod Whitelist**: Whitelist trusted mods with automatic channel detection or manual selection.
- **Chat Signing Control**: Configure chat message signing behavior (Off / Auto / On).
- **Account Manager**: Built-in account switcher supporting Microsoft session tokens and offline profiles.
- **Telemetry Blocking**: Disable telemetry and diagnostics data sent to Mojang.

---

## ⚙️ Configuration & GUI

Open the OpSec settings menu anytime:
- Via the **OpSec** button in the multiplayer server selection screen header.
- Or via **[Mod Menu](https://modrinth.com/mod/modmenu)**.

### Protection Settings
| Option | Description |
|---|---|
| **Spoof as vanilla** | Masks brand as Vanilla and blocks mod detection packets |
| **Isolate Pack Cache** | Keeps server packs in separate account caches |
| **Block Local Pack URLs** | Prevents servers from probing your local network |
| **Bypass Server Pack Requirement** | Toggle forced server resource packs freely |
| **Strip Mod Shader Overrides** | Prevents server shaders from detecting other mods |
| **Key Resolution Spoofing** | Masks translation key values to default client strings |
| **Signing Mode** | Auto (sign only when required) / Off (never sign) / On |
| **Disable Telemetry** | Blocks analytics data from leaving your client |

---

## 📦 Requirements & Installation

1. Install **[Fabric Loader](https://fabricmc.net/)** (version 0.16.0+).
2. Install **[Fabric API](https://modrinth.com/mod/fabric-api)** matching your Minecraft version.
3. (Optional) Install **[Mod Menu](https://modrinth.com/mod/modmenu)** for in-game configuration.
4. Download `opsec-[mc_version]+v[version].jar` from the **Versions** tab and place it into your `.minecraft/mods/` folder.

---

## ⚖️ License & Credits

- **Original Creator**: [Aurick](https://github.com/aurickk)
- **Fork Maintainer**: [legendprime753-dev](https://github.com/legendprime753-dev)
- **Fork Repository**: [GitHub (legendprime753-dev/opsec-fork)](https://github.com/legendprime753-dev/opsec-fork)
- **Original Source Code**: [GitHub (aurickk/OpSec)](https://github.com/aurickk/OpSec)
- **License**: GNU General Public License v3.0 (GPL-3.0)
