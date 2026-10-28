package com.android.smshero.util

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class FuzzySearchTest {
    @Test
    fun testLevenshtein() {
        assertEquals(0, FuzzySearch.levenshtein("abc", "abc"))
        assertEquals(1, FuzzySearch.levenshtein("abc", "abd"))
        assertEquals(3, FuzzySearch.levenshtein("abc", "xyz"))
    }

    @Test
    fun testSimilarity() {
        assertTrue(FuzzySearch.similarity("WhatsApp", "whatsapp") >= 80)
        assertTrue(FuzzySearch.similarity("Telegram", "telegram") >= 80)
        assertTrue(FuzzySearch.similarity("Google", "ggle") >= 60)
    }

    @Test
    fun testFilter() {
        val items = listOf("WhatsApp", "Telegram", "Google", "Facebook")
        val result = FuzzySearch.filter("what", items) { listOf(it) }
        assertTrue(result.contains("WhatsApp"))
    }
}

class PhoneFormatterTest {
    @Test
    fun testDetectCc() {
        assertEquals("62", PhoneFormatter.detectCc("628123456789"))
        assertEquals("1", PhoneFormatter.detectCc("12125551234"))
        assertEquals("44", PhoneFormatter.detectCc("441234567890"))
    }

    @Test
    fun testFormat() {
        val fmt = PhoneFormatter.format("628123456789")
        assertTrue(fmt.startsWith("+62"))
        assertTrue(fmt.contains(" "))
    }
}
