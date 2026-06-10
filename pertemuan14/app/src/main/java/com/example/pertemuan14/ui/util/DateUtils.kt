package com.example.pertemuan14.ui.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

/**
 * Converts ISO 8601 date string to a human-readable relative time (e.g. "3 days ago").
 */
fun formatTimeAgo(publishedAt: String): String {
    return try {
        val formats = listOf(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        )
        var date: java.util.Date? = null
        for (fmt in formats) {
            try {
                val sdf = SimpleDateFormat(fmt, Locale.US)
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                date = sdf.parse(publishedAt)
                if (date != null) break
            } catch (_: Exception) {}
        }
        if (date == null) return publishedAt
        val diff = System.currentTimeMillis() - date.time
        val seconds = diff / 1000L
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        when {
            days >= 1 -> "${days}d ago"
            hours >= 1 -> "${hours}h ago"
            minutes >= 1 -> "${minutes}m ago"
            else -> "just now"
        }
    } catch (e: Exception) {
        publishedAt
    }
}

/**
 * Generate a pseudo-random view count derived from the article URL hash.
 */
fun formatViewCount(url: String): String {
    val seed = abs(url.hashCode()).toLong()
    val count = 40_000L + (seed % 260_000L)
    return if (count >= 1_000) {
        val k = count / 1_000
        val dec = (count % 1_000) / 100
        if (dec == 0L) "${k}K" else "${k}.${dec}K"
    } else "$count"
}

/**
 * Generate a pseudo-random comment count derived from the article URL hash.
 */
fun formatCommentCount(url: String): String {
    val seed = abs((url + "comments").hashCode()).toLong()
    val count = 500L + (seed % 4_500L)
    return if (count >= 1_000) {
        val k = count / 1_000
        val dec = (count % 1_000) / 100
        if (dec == 0L) "${k}K" else "${k}.${dec}K"
    } else "$count"
}
