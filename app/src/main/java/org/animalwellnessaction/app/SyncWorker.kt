package org.animalwellnessaction.app

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * Runs once a day (scheduled from AWAApplication) and pulls a fresh copy of the
 * pages listed in PAGES_TO_SYNC into the OfflineCache, so the app has something
 * current to show even without a live connection.
 *
 * Add or remove URLs in PAGES_TO_SYNC to change what gets synced.
 */
class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    companion object {
        const val UNIQUE_WORK_NAME = "awa_daily_sync"

        val PAGES_TO_SYNC = listOf(
            "https://animalwellnessaction.org/",
            "https://animalwellnessaction.org/2026-congressional-profiles/",
            "https://animalwellnessaction.org/2026-election-endorsements/"
        )
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val cache = OfflineCache(applicationContext)
        var sawFailure = false

        for (url in PAGES_TO_SYNC) {
            try {
                val request = Request.Builder().url(url).build()
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val body = response.body?.bytes()
                        val contentType = response.header("Content-Type") ?: "text/html; charset=utf-8"
                        if (body != null) {
                            cache.save(url, contentType, body)
                        }
                    } else {
                        sawFailure = true
                    }
                }
            } catch (e: Exception) {
                sawFailure = true
            }
        }

        cache.setLastSyncTime(System.currentTimeMillis())

        if (sawFailure) Result.retry() else Result.success()
    }
}
