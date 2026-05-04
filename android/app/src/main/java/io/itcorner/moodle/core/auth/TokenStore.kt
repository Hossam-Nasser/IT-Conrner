package io.itcorner.moodle.core.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = run {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "moodle_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    fun token(): String? = prefs.getString(KEY_TOKEN, null)
    fun userId(): Long? = prefs.getLong(KEY_USER_ID, -1L).takeIf { it > 0 }

    fun save(token: String, userId: Long) {
        prefs.edit().putString(KEY_TOKEN, token).putLong(KEY_USER_ID, userId).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN = "wstoken"
        private const val KEY_USER_ID = "userid"
    }
}
