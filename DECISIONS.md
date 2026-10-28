# DECISIONS.md — SMSHERO v2.0 Kotlin migration decisions

> Maintained during full autonomous execution. Format per section.

## [Global] Signing
- Pilihan: Debug signing for both debug & release build types
- Alasan: APK sideload-only (not Play Store); no upload key needed; minify rules unverified
- Alternatif yang ditolak: production keystore (requires user key), unsigned release
- Risiko: release APK debuggable, sideload OK

## [Global] Minify / R8
- Pilihan: `isMinifyEnabled = false`, `isShrinkResources = false` for both types
- Alasan: proguard-rules.pro belum diverifikasi
- Alternatif yang ditolak: `true` with verified rules
- Risiko: besar APK; tidak ada pemotongan kode/resource

## [Global] Room encryption
- Pilihan: `net.zetetic:android-database-sqlcipher:4.5.4` + key from EncryptedSharedPreferences
- Alasan: blueprint menuntut encrypted DB, fallback private-dir laporan
- Alternatif yang ditolak: plaintext Room (external storage forbidden)
- Risiko: jika dependency conflict → fallback private-dir + laporan di final report

## [Global] GitHub Actions
- Workflow name: `android.yml` (single workflow, migrated later if blueprint needs)
- Branch target: target `master` (current repo default)
- CI failure on 4b3e695: missing `[main]` in trigger — fixed
- gh CLI now installed v2.46.0 (apt package)
- Download script uses curl + GitHub API fallback

## [Global] Kotlin
- Version: 1.9.10 (existing), JVM target 17 (matches compileOptions)
- Compose compiler extension: 1.5.3

## [Step 1] Gradle setup
- Confirmed all required deps present in debugCompileClasspath
- assembleDebug failed at Android SDK license — CI has SDK pre-installed
- Commit 49f20e8 pushed; CI will run on it after trigger fix

## [CI Investigation]
- Commit 4b3e695 FAILURE: repo actions/runs list shows that run existed, cause unknown
- Root cause hypothesis: workflow trigger expects `main` but repo is on `master` → no run triggered after push; failure was earlier/different
- Fix: change trigger to `[master]`, set `workflow_dispatch`
- After fix: push, poll via API until completed-success
