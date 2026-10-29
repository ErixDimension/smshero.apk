# DECISIONS.md — SMSHERO v2.0

## Network
- OkHttp `4.11.0` remains primary version.
- Added `com.squareup.okhttp3:okhttp-dnsoverhttps:4.11.0`, synchronized with OkHttp.
- Both API clients use Cloudflare DoH `https://1.1.1.1/dns-query`, IPv6 disabled.
- Both API clients force Chrome Android User-Agent to satisfy HeroSMS Cloudflare behavior.
- API keys remain in encrypted preferences and are never logged.

## Auth flow
- Empty key opens Modern Neon login.
- Stored key opens dashboard through `EncryptedPrefs.apiKey` StateFlow.
- Logout removes only API key and returns to login.
- No reflection or duplicated navigation state.

## Polling
- Foreground Service owns polling lifecycle.
- Active order IDs are represented by Room orders.
- One Coroutine Job per order ID in a process-local `ConcurrentHashMap`; repeated starts reuse existing Job.
- Terminal status or expiry cancels that order Job. Poll interval is 5 seconds.
- Service never fabricates OTP data. Provider response must update Room before UI reports OTP received.
- WorkManager is not used as a 5-second timer; Android WorkManager minimum periodic interval is unsuitable.

## Build limitation
- Local environment has no Java/JAVA_HOME, so local Gradle verification is blocked. CI remains required build authority.
