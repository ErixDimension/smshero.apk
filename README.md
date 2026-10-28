# SMSHERO Terang Bulan — Android Native

Android native Kotlin implementation of SMSHERO SMS service client.

## Build

```bash
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## Configuration

- Application ID: `com.android.smshero`
- Min SDK: 26 (Android 8.0)
- Target SDK: 34
- Compile SDK: 34

## Features

- Service catalog browsing
- Country and operator selection
- Price variant selection
- Order placement and monitoring
- OTP retrieval
- Secure API key storage (Android Keystore)

## API

Uses HeroSMS API:
- Base: `https://hero-sms.com/api/v1`
- Stubs: `https://hero-sms.com/stubs/handler_api.php`

## Security

API keys stored using `EncryptedSharedPreferences` with AES256-GCM encryption backed by Android Keystore.

Never commit API keys, tokens, or credentials to version control.
