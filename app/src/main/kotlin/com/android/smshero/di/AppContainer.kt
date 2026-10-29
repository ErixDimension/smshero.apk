package com.android.smshero.di

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.android.smshero.core.security.SecureSessionStore
import com.android.smshero.data.local.EncryptedPrefs

interface AppContainer {
    val encryptedPrefs: EncryptedPrefs
    val secureSessionStore: SecureSessionStore
}

class DefaultAppContainer(context: Context) : AppContainer {
    override val encryptedPrefs = EncryptedPrefs(context)
    override val secureSessionStore = SecureSessionStore(context)
}
