package com.android.smshero.util

object PhoneFormatter {
    private val cc3 = setOf("212", "213", "216")
    private val cc2 = setOf("20", "27", "30", "62", "91", "44", "55", "86", "380")
    private val cc1 = setOf("1", "7")

    fun detectCc(phone: String): String {
        val s = phone.trimStart('+')
        return when {
            s.take(3) in cc3 -> s.take(3)
            s.take(2) in cc2 -> s.take(2)
            s.take(1) in cc1 -> s.take(1)
            else -> s.take(2)
        }
    }

    fun format(phone: String, ccHint: String? = null): String {
        val s = phone.trimStart('+')
        val cc = ccHint?.takeIf { s.startsWith(it) } ?: detectCc(s)
        val rest = s.removePrefix(cc)
        val groups = rest.chunked(3)
        return "+$cc ${groups.joinToString(" ")}"
    }
}

object FuzzySearch {
    fun levenshtein(a: String, b: String): Int {
        val dp = Array(a.length + 1) { IntArray(b.length + 1) }
        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j
        for (i in 1..a.length) for (j in 1..b.length) {
            dp[i][j] = minOf(
                dp[i - 1][j] + 1,
                dp[i][j - 1] + 1,
                dp[i - 1][j - 1] + (if (a[i - 1] == b[j - 1]) 0 else 1)
            )
        }
        return dp[a.length][b.length]
    }

    fun similarity(a: String, b: String): Int {
        val len = maxOf(a.length, b.length)
        if (len == 0) return 100
        val dist = levenshtein(a.lowercase(), b.lowercase())
        return ((len - dist) * 100) / len
    }

    fun <T> filter(query: String, items: List<T>, scorer: (T) -> List<String>): List<T> {
        if (query.isBlank()) return items
        return items.map { item ->
            val scores = scorer(item).map { similarity(query.lowercase(), it.lowercase()) }
            item to maxOf(0, *scores.toIntArray())
        }.filter { it.second >= 60 }.sortedByDescending { it.second }.map { it.first }
    }
}
