package aurick.opsec.mod.config;

import aurick.opsec.mod.Opsec;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Async jar integrity checker that compares the running mod jar's SHA-256 hash
 * against the expected digest computed from the matching GitHub release asset.
 * All state fields are volatile for cross-thread visibility since the check runs
 * on a background thread and results are read on the render thread.
 */
public final class JarIntegrityChecker {

    private static final String RELEASES_BASE_URL = "https://api.github.com/repos/aurickk/OpSec/releases/tags/V";

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private static volatile boolean tamperDetected = false;
    private static volatile boolean checkComplete = false;
    private static volatile boolean shownThisSession = false;
    private static volatile String expectedDigest = null;
    private static volatile String actualDigest = null;

    private JarIntegrityChecker() {
        // Utility class
    }

    /**
     * Fires an async integrity check that computes the local jar's SHA-256 and
     * compares it against the SHA-256 of the matching GitHub release asset.
     * Non-blocking: runs on a daemon thread via CompletableFuture.
     */
    public static void checkIntegrity() {
        // Disabled for community fork: upstream checks aurickk/OpSec release asset digests,
        // which triggers false positive tamper alarms on fork builds.
        checkComplete = true;
        tamperDetected = false;
        Opsec.LOGGER.debug("[OpSec] Running community fork - upstream integrity check bypassed");
    }

    /**
     * Returns true once the integrity check has finished (regardless of result).
     */
    public static boolean isCheckComplete() {
        return true;
    }

    /**
     * Returns true if tamper was detected, the check is complete, and the
     * warning screen has not been shown this session.
     */
    public static boolean isTamperDetected() {
        return false;
    }

    /**
     * Marks the tamper warning as shown for this session.
     * Prevents the screen from appearing again even if user navigates back.
     */
    public static void markShown() {
        shownThisSession = true;
    }

    /**
     * Resets the shown flag. Used by the config screen reset button.
     */
    public static void resetShown() {
        shownThisSession = false;
    }

    /**
     * Returns the expected SHA-256 digest from the GitHub release asset, or null if not yet checked.
     */
    public static String getExpectedDigest() {
        return expectedDigest;
    }

    /**
     * Returns the actual SHA-256 digest of the running jar, or null if not yet checked.
     */
    public static String getActualDigest() {
        return actualDigest;
    }

    /**
     * Finds the release asset whose version range covers the given MC version.
     * Asset names follow the pattern: opsec-{range}+v{mod_version}.jar
     * where range is either a single version (e.g., "26.1") or a range (e.g., "1.21.2-1.21.5").
     */
    private static JsonObject findMatchingAsset(JsonArray assets, String mcVersion) {
        for (JsonElement element : assets) {
            JsonObject asset = element.getAsJsonObject();
            String name = asset.has("name") ? asset.get("name").getAsString() : "";
            if (!name.startsWith("opsec-") || !name.endsWith(".jar")) continue;

            // Extract version range: between "opsec-" and "+v"
            int rangeStart = 6; // "opsec-".length()
            int rangeEnd = name.indexOf("+v");
            if (rangeEnd <= rangeStart) continue;

            String range = name.substring(rangeStart, rangeEnd);
            int dash = range.indexOf('-');

            if (dash == -1) {
                // Single version: exact match
                if (range.equals(mcVersion)) return asset;
            } else {
                // Range: min-max (inclusive)
                String min = range.substring(0, dash);
                String max = range.substring(dash + 1);
                if (compareVersions(mcVersion, min) >= 0 && compareVersions(mcVersion, max) <= 0) {
                    return asset;
                }
            }
        }
        return null;
    }

    /**
     * Compares two dot-separated version strings numerically.
     * Returns negative if a < b, zero if equal, positive if a > b.
     */
    private static int compareVersions(String a, String b) {
        String[] aParts = a.split("\\.");
        String[] bParts = b.split("\\.");
        int len = Math.max(aParts.length, bParts.length);
        for (int i = 0; i < len; i++) {
            int aNum = i < aParts.length ? Integer.parseInt(aParts[i]) : 0;
            int bNum = i < bParts.length ? Integer.parseInt(bParts[i]) : 0;
            if (aNum != bNum) return Integer.compare(aNum, bNum);
        }
        return 0;
    }

    /**
     * Converts a byte array to a lowercase hex string.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
