package org.animalwellnessaction.app

import android.content.Context
import android.content.SharedPreferences
import java.io.File
import java.security.MessageDigest

/**
 * Stores a daily snapshot of key pages on disk so the app has something to show
 * when the device is offline. This is not a full mirror of the site — it caches
 * the HTML (and inline-referenced same-origin assets are still loaded live when
 * available) for a configured list of important URLs.
 */
class OfflineCache(context: Context) {

    private val dir: File = File(context.filesDir, "site_cache").apply { mkdirs() }
    private val prefs: SharedPreferences =
        context.getSharedPreferences("awa_sync_prefs", Context.MODE_PRIVATE)

    fun keyFor(url: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(url.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun save(url: String, contentType: String, bytes: ByteArray) {
        val key = keyFor(url)
        File(dir, "$key.body").writeBytes(bytes)
        File(dir, "$key.type").writeText(contentType)
    }

    fun load(url: String): Pair<String, ByteArray>? {
        val key = keyFor(url)
        val bodyFile = File(dir, "$key.body")
        val typeFile = File(dir, "$key.type")
        if (!bodyFile.exists() || !typeFile.exists()) return null
        return typeFile.readText() to bodyFile.readBytes()
    }

    fun has(url: String): Boolean = File(dir, "${keyFor(url)}.body").exists()

    fun setLastSyncTime(timeMillis: Long) {
        prefs.edit().putLong("last_sync", timeMillis).apply()
    }

    fun getLastSyncTime(): Long = prefs.getLong("last_sync", 0L)
}
