# EVIDENCE_LOG.md — SMSHERO v2.0 verification evidence

## UI Step 4
- Replaced placeholder navigation screens with functional Compose screens.
- Added terminal-style `ApiKeyInputScreen` with `BasicTextField`, validation, loading state, encrypted API-key persistence, and balance verification.
- Added service, country, operator, variant, quantity, and monitor screens with search/list/order controls.
- CI build 40723077072 SUCCESS, APK SHA-256: 8ffeb63f020a9d3db41ee7d1c13ae69b1c3ee9d73681e6316e13c15f97ba4b52

## DB Step 5
- Created `SmsHeroDb` with Room + SQLCipher fallback, `OrderDao`, encrypted key from `EncryptedPrefs.getDatabaseKey()`.
- Added `OrderPollingService` foreground service, 5s polling loop, auto-cancel 2min before expiry.
- Added manifest permissions: FOREGROUND_SERVICE, POST_NOTIFICATIONS.
- Added unit tests for fuzzy search and phone formatter.
- Created `build-apk.yml` workflow for debug+release APK artifact.

## Verification
- Pending final CI build with DB/service/workflow.
